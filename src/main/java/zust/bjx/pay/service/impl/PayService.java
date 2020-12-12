package zust.bjx.pay.service.impl;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private BestPayService bestPayService;
    @Override
    public PayResponse create(String orderId, BigDecimal amount,BestPayTypeEnum bestPayTypeEnum) {
        PayRequest request = new PayRequest();

        request.setOrderName("4559066-最好的支付sdk");
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        // 支付场景
        request.setPayTypeEnum(bestPayTypeEnum);
        PayResponse response = bestPayService.pay(request);
        log.info("response={}",response);
        return response;
    }

    /**
     * 异步通知处理
     *
     * @param notifyData 通知数据
     */
    @Override
    public String asyncNotify(String notifyData) {
        //签名校验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("payResponse={}",payResponse);

        //2.金额校验(从数据库)

        //3.修改订单支付状态

        //4.告诉微信不要再通知了
        if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        }else if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            return "success";
        }
        throw new RuntimeException("异步通知中，错误的支付平台");
    }
}
