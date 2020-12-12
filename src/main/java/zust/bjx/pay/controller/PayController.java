package zust.bjx.pay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePageMergePayRequest;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import zust.bjx.pay.config.AlipayConfig;
import zust.bjx.pay.service.impl.PayService;

import javax.xml.transform.Result;
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
@Slf4j
public class PayController {

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType") BestPayTypeEnum bestPayTypeEnum) {
        PayResponse payResponse = payService.create(orderId, amount,bestPayTypeEnum);
        Map map = new HashMap();
        // 微信返回的是二维码的地址 然后用文本转化为二维码
        // 支付宝返回的是表单
        // 支付方式不同 渲染不同
        if (bestPayTypeEnum == BestPayTypeEnum.WXPAY_NATIVE){
            map.put("codeUrl",payResponse.getCodeUrl());
            return new ModelAndView("createForWxNative",map);
        }else if(bestPayTypeEnum == BestPayTypeEnum.ALIPAY_PC){
            map.put("body",payResponse.getBody());
            return new ModelAndView("createForAlipayPc",map);
        }
        throw new RuntimeException("暂不支持的支付类型");
    }

    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData) {
         return payService.asyncNotify(notifyData);
    }
}
