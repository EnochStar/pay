package zust.bjx.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author EnochStar
 * @title: wxAccountConfig
 * @projectName pay
 * @description:
 * @date 2020/12/13 17:01
 */
@ConfigurationProperties(prefix = "wx")
@Component
@Data
public class WxAccountConfig {
    private String appId;
    private String mchId;
    private String mchKey;
    private String notifyUrl;
    private String returnUrl;
}
