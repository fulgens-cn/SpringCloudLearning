package cn.fulgens.apigateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "cn.fulgens.security.auth-request-urls")
public class AuthRequestUrlsProperties {

    private Set<String> buyer;

    private Set<String> seller;

    public AuthRequestUrlsProperties() {
    }

    public AuthRequestUrlsProperties(Set<String> buyer, Set<String> seller) {
        this.buyer = buyer;
        this.seller = seller;
    }

    public Set<String> getBuyer() {
        return buyer;
    }

    public void setBuyer(Set<String> buyer) {
        this.buyer = buyer;
    }

    public Set<String> getSeller() {
        return seller;
    }

    public void setSeller(Set<String> seller) {
        this.seller = seller;
    }
}
