package com.polyroot.coinbot.configuration;

import com.polyroot.coinbot.streaming.manage.FluxAdaptersManager;
import com.polyroot.coinbot.streaming.manage.MonoAdaptersManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FluxManagerConfig {
    @Bean(name="FluxManager")
    public FluxAdaptersManager initFluxManager(){
        return new FluxAdaptersManager();
    }

    @Bean(name="MonoWireManager")
    public MonoAdaptersManager initMonoManager(){
        return new MonoAdaptersManager();
    }

}
