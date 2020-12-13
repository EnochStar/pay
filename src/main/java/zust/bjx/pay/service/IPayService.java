package zust.bjx.pay.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import zust.bjx.pay.pojo.PayInfo;

import java.math.BigDecimal;

/**
 * @author EnochStar
 * @title: IPayService
 * @projectName pay
 * @description:
 * @date 2020/12/720:01
 */
public interface IPayService {

    /**
     * @param orderId 商品id
     * @param amount 金额
     * 创建发起支付
     */
    PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

    /**
     * @param notifyData 通知数据
     * 异步通知处理
     */
    String asyncNotify(String notifyData);

    /**
     * 查询支付记录 通过订单号
     * @param orderId
     * @return
     */
    PayInfo queryByOrderId(String orderId);
}
