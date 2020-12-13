package zust.bjx.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author EnochStar
 * @title: AlipayAccountConfig
 * @projectName pay
 * @description:
 * @date 2020/12/13 17:12
 */
@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayAccountConfig {
    private String appId;
    private String privateKey;
    private String publicKey;
    private String notifyUrl;
    private String returnUrl;
}
