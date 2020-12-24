package zust.bjx.pay.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;

/**
 * @author EnochStar
 * @title: IPayServiceTest
 * @projectName pay
 * @description:
 * @date 2020/12/720:43
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class IPayServiceTest {
    @Autowired
    private IPayService ipayService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void create() {
        //BigDecimal.valueOf(0.01)  或 new BigDecimal("0.01")
        ipayService.create("123321", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
    }
    @Test
    public void sendMQMsg(){
        // s:队列的名字 o:消息
        amqpTemplate.convertAndSend("payNotify","hello");
    }
}