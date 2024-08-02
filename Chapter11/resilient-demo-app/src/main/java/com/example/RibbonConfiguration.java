package com.example;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerListSubsetFilter;
import com.netflix.loadbalancer.WeightedResponseTimeRule;

@Configuration
@RibbonClient(name = "serviceA")
public class RibbonConfiguration {

    @Bean
    public IRule ribbonRule() {
        return new WeightedResponseTimeRule();
    }

    @Bean
    public IPing ribbonPing() {
        return new PingUrl(false, "/health");
    }

    @Bean
    public IClientConfig ribbonClientConfig() {
        DefaultClientConfigImpl config = new DefaultClientConfigImpl();
        config.loadProperties("serviceA");
        config.set(CommonClientConfigKey.MaxAutoRetries, 1);
        config.set(CommonClientConfigKey.MaxAutoRetriesNextServer, 1);
        config.set(CommonClientConfigKey.ConnectTimeout, 1000);
        config.set(CommonClientConfigKey.ReadTimeout, 3000);
        return config;
    }

    @Bean
    public ServerListSubsetFilter<Server> serverListFilter() {
        return new ServerListSubsetFilter<>();
    }
}
