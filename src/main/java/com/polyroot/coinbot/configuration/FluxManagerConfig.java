package com.polyroot.coinbot.configuration;

import com.polyroot.coinbot.model.dto.IncomingRequestEnvelope;
import com.polyroot.coinbot.streaming.manage.FluxAdaptersManager;
import com.polyroot.coinbot.streaming.manage.MonoAdaptersManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FluxManagerConfig {
    @Bean
    public FluxAdaptersManager<IncomingRequestEnvelope> initFluxManager(){
        return new FluxAdaptersManager<>();
    }

    @Bean
    public MonoAdaptersManager initMonoManager(){
        return new MonoAdaptersManager<>();
    }

}
