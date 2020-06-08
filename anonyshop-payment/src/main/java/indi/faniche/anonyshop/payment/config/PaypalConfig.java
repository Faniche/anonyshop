package indi.faniche.anonyshop.payment.config;

/* File:   PaypalConfig.java
 * -------------------------
 * Author: faniche
 * Date:   5/5/20
 */

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaypalConfig {

    // Replace with your application client ID and secret
    String clientId = "AXWYebeycBkHEz1o9QcMKklbUWj4LjxoLwINFNR6e4TA9RxyIKF1WwK9sWAAMLyS-1C_-QPbDIl185tj";
    String clientSecret = "EIDMouJk8cPVk3c96DQX8TBPJeq99zgIQn1cjtrohyfHUW_Z6pLNhzuUuvoukNbBeguZUpiSJyqGLLKW";

    APIContext context = new APIContext(clientId, clientSecret, "sandbox");

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", "sandbox");
        return configMap;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context = new APIContext(clientId, clientSecret, "sandbox");
        return context;
    }
}
