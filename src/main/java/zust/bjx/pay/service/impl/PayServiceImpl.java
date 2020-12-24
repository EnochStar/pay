package zust.bjx.pay.service.impl;

import com.google.gson.Gson;
import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zust.bjx.pay.dao.PayInfoMapper;
import zust.bjx.pay.enums.PayPlatformEnum;
import zust.bjx.pay.pojo.PayInfo;
import zust.bjx.pay.service.IPayService;

import java.math.BigDecimal;

/**
 * @author EnochStar
 * @title: PayService
 * @projectName pay
 * @description:
 * @date 2020/12/720:03
 */
@Slf4j
@Service
public class PayServiceImpl implements IPayService {

    private final static String QUEUE_PAY_NOTIFY = "payNotify";

    @Autowired
    private BestPayService bestPayService;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public PayResponse create(String orderId, BigDecimal amount,BestPayTypeEnum bestPayTypeEnum) {
        //写入数据库
        PayInfo payInfo = new PayInfo(Long.parseLong(orderId),
                PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                OrderStatusEnum.NOTPAY.name(),
                amount
                );
        payInfoMapper.insertSelective(payInfo);
        PayRequest request = new PayRequest();
        // 订单名称
        request.setOrderName("4559066-最好的支付sdk");
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        // 支付场景
        request.setPayTypeEnum(bestPayTypeEnum);
        PayResponse response = bestPayService.pay(request);
        log.info("发起支付 response={}",response);
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
        log.info("异步通知 payResponse={}",payResponse);

        //2.金额校验(从数据库)
        // 比较严重（正常情况下不会发生的） 发出告警：钉钉、短信
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
        if (payInfo == null) {
            throw new RuntimeException("通过orderNo查询的结果是null");
        }
        //如果订单支付状态不是”已支付“
        if (!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())){
            //Double类型比较大小不好
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                throw new RuntimeException("异步通知中的金额与数据库的不匹配，orderNo=" + payResponse.getOrderId());
            }
            //3.修改订单支付状态
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            // 由支付平台产生的交易流水号
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());
            // 由mysql自己设置更新时间
            payInfo.setUpdateTime(null);
            // 注意此处updateTime 和 createTime值在创建payInfo时已经一起设置了
            // 由updateByPrimaryKeySelective可知 将updateTime设置为null即可
            payInfoMapper.updateByPrimaryKeySelective(payInfo);
        }
        //TODO pay发送MQ消息，mall接收MQ消息
        amqpTemplate.convertAndSend(QUEUE_PAY_NOTIFY,new Gson().toJson(payInfo));


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

    @Override
    public PayInfo queryByOrderId(String orderId) {
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(orderId));
        return payInfo;
    }
}
