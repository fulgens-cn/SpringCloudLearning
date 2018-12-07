package cn.fulgens.order.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Slf4j
@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {

    @Autowired
    private RestTemplate restTemplate;

    // @HystrixCommand(fallbackMethod = "fallback")
    // 超时设置，默认超时时间1000ms，属性名见com.netflix.hystrix.HystrixCommandProperties
    /*@HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })*/
    // 熔断设置（断路器三种状态：closed; open; harf open;）
    // default_circuitBreakerEnabled = true;
    // default_metricsRollingStatisticalWindowBuckets = 10;// default => statisticalWindowBuckets: 10 = 10 buckets in a 10 second window so each bucket is 1 second
    // default_circuitBreakerRequestVolumeThreshold = 20;// default => statisticalWindowVolumeThreshold: 20 requests in 10 seconds must occur before statistics matter
    // default_circuitBreakerSleepWindowInMilliseconds = 5000;// default => sleepWindow: 5000 = 5 seconds that we will sleep before trying again after tripping the circuit
    // default_circuitBreakerErrorThresholdPercentage = 50;// default => errorThresholdPercentage = 50 = if 50%+ of requests in 10 seconds are failures or latent then we will trip the circuit
    /*@HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
            @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
    })*/
    // default => the name of annotated method
    @HystrixCommand
    @GetMapping("/getProductInfo")
    public String getProductInfo(@RequestParam Integer num) {

        if (num % 2 == 0) return "success";

        String response = restTemplate.postForObject("http://127.0.0.1:8080/product/listForOrder",
                Arrays.asList("157875227953464068"), String.class);
        return response;
    }

    private String fallback() {
        return "当前服务不可用，请稍后再试！";
    }

    private String defaultFallback() {
        return "当前服务不可用，请稍后再试！（默认）";
    }

}
