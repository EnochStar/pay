package zust.bjx.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.service.BestPayService;

/**
 * @author EnochStar
 * @title: PayPlatformEnum
 * @projectName pay
 * @description: TODO
 * @date 2020/12/12 16:05
 */
public enum PayPlatformEnum {
    // 1-支付宝 ， 2-微信
    ALIPAY(1),
    WX(2);
    Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {
        // if (bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.ALIPAY.name())) {
        //     return ALIPAY;
        // }else if (bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.WX.name())){
        //     return WX;
        // }
        for (PayPlatformEnum payPlatformEnum : PayPlatformEnum.values()) {
            if (bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name()))
                return payPlatformEnum;
        }throw new RuntimeException("错误的支付平台:" + bestPayTypeEnum.name());
    }
}
