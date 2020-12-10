package zust.bjx.pay.controller;

import com.lly835.bestpay.model.PayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import zust.bjx.pay.service.impl.PayService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author EnochStar
 * @title: PayController
 * @projectName pay
 * @description: TODO
 * @date 2020/12/721:19
 */
@Controller
@RequestMapping("/pay")
// @CrossOrigin(allowCredentials = "*")
public class PayController {

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount) {
        PayResponse payResponse = payService.create(orderId, amount);
        Map map = new HashMap();
        map.put("codeUrl",payResponse.getCodeUrl());
        return new ModelAndView("create",map);
    }

    @PostMapping("/notify")
    public void asyncNotify(@RequestBody String notifyData) {

    }
}
