package com.abdav.giri_guide.config;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.Midtrans;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MidtransConfig {
    @Value("${midtrans.server-key}")
    private String serverKey;

    @Value("${midtrans.server-client}")
    private String clientKey;

    @Bean
    public Config midtrans(){
        return new ConfigFactory(new Config(serverKey, clientKey,false)).getConfig();
    }


}
