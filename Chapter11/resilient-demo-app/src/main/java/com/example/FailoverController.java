package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@RestController
public class FailoverController {

    private final EurekaClient eurekaClient;

    public FailoverController(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/failover")
    public String failover() {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("serviceB", false);
        return "Failing over to " + instance.getHomePageUrl();
    }
}
