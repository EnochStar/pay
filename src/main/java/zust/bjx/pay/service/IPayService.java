package zust.bjx.pay.service;

import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

/**
 * @author EnochStar
 * @title: IPayService
 * @projectName pay
 * @description: TODO
 * @date 2020/12/720:01
 */
public interface IPayService {

    /**
     * @param orderId 商品id
     * @param amount 金额
     * 创建发起支付
     */
    PayResponse create(String orderId, BigDecimal amount);
}
