package zust.bjx.pay.service.impl;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zust.bjx.pay.service.IPayService;

import java.math.BigDecimal;

/**
 * @author EnochStar
 * @title: PayService
 * @projectName pay
 * @description: TODO
 * @date 2020/12/720:03
 */
@Slf4j
@Service
public class PayService implements IPayService {
    @Override
    public PayResponse create(String orderId, BigDecimal amount) {
        //微信支付配置
        WxPayConfig wxPayConfig = new WxPayConfig();
        //公众号Id
        wxPayConfig.setAppId("wxd898fcb01713c658");
        //支付商户资料
        wxPayConfig.setMchId("1483469312");

        wxPayConfig.setMchKey("098F6BCD4621D373CADE4E832627B4F6");
        wxPayConfig.setNotifyUrl("http://testi.test.utools.club");

        //支付类, 所有方法都在这个类里
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);

        PayRequest request = new PayRequest();

        request.setOrderName("4559066-最好的支付sdk");
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        // 支付场景
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE);
        PayResponse response = bestPayService.pay(request);
        log.info("response={}",response);
        return response;
    }
}
