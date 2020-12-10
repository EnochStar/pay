package zust.bjx.pay.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;

/**
 * @author EnochStar
 * @title: IPayServiceTest
 * @projectName pay
 * @description: TODO
 * @date 2020/12/720:43
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class IPayServiceTest {
    @Autowired
    private IPayService ipayService;
    @Test
    public void create() {
        //BigDecimal.valueOf(0.01)  »ò new BigDecimal("0.01")
        ipayService.create("123321", BigDecimal.valueOf(0.01));
    }
}