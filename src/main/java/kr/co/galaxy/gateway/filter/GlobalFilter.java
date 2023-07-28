package kr.co.galaxy.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.galaxy.gateway.security.UserAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter() {
        super(Config.class);
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.debug("Global Filter - Request ID = {}", request.getId());

            return ReactiveSecurityContextHolder.getContext()
                    .map(SecurityContext::getAuthentication)
                    .map(Authentication::isAuthenticated)
                    .flatMap(isAuth ->{
                        if(isAuth){
                            log.debug("isAuthentication true");
                            return chain.filter(exchange);
                        }else{
                            log.debug("isAUthentication false");
                            return chain.filter(exchange);
                        }
                    });


/*
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.debug("Global Filter - Response Header : {}", response.getHeaders());
                log.debug("Global Filter - Response Authentication : {}",
                        response.getHeaders().get("auth"));
                log.debug("Global Filter - Response Code = {}", response.getStatusCode());

                ObjectMapper objectMapper = new ObjectMapper();

                String authStr = response.getHeaders().get("auth").get(0);
                log.debug("Authentication String : {} ", authStr);
                Authentication authentication = null;
                try {
                    //authentication = objectMapper.readValue(authStr, UserAuthentication.class);
                    authentication = objectMapper.readValue(authStr, UserAuthentication.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                ReactiveSecurityContextHolder.withAuthentication(authentication);
            }));
 */
        });
    }


}
