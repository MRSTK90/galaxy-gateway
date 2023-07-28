package kr.co.galaxy.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SampleCustomFilter extends AbstractGatewayFilterFactory<SampleCustomFilter.Config> {

    public SampleCustomFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        log.debug("SampleCustomFilter::apply");

        return ((exchange, chain) -> {
            log.debug("[PRE] SampleCustomFilter");

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            Mono<Boolean> isAuth = ReactiveSecurityContextHolder.getContext()
                    .map(SecurityContext::getAuthentication)
                    .map(Authentication::isAuthenticated);


            return chain.filter(exchange).then(Mono.fromRunnable(() ->{
                log.debug("[POST] SampleCustomFilter");
            }));
        });
    }

    static class Config {

    }

}
