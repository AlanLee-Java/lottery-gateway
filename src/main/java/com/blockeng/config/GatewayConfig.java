package com.blockeng.config;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author qiang
 */
@Configuration
public class GatewayConfig {

    @Bean
    public WebExceptionHandler exceptionHandler() {
        return (ServerWebExchange exchange, Throwable ex) -> Mono.error(ex);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                return chain.filter(exchange);
            }
        };
    }

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            ServerHttpResponse response = ctx.getResponse();
            HttpHeaders responseHeaders = response.getHeaders();
            responseHeaders.add("X-Served-By", "d3d3LmJsb2NrZW5nLnBybw==");
            if (CorsUtils.isCorsRequest(request)) {
                responseHeaders.add("Access-Control-Allow-Origin", request.getHeaders().getOrigin());
                responseHeaders.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
                responseHeaders.add("Access-Control-Max-Age", "18000");
                responseHeaders.add("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN, Cookie, Set-Cookie, lang, token, username, client, geetest_challenge, geetest_validate, geetest_seccode, sign, clientType");
                responseHeaders.add("Access-Control-Expose-Headers", "*");
                responseHeaders.add("Access-Control-Allow-Credentials", "true");
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx.mutate().request(request).build());
        };
    }

    @Bean
    public RouteDefinitionLocator discoveryClientRouteDefinitionLocator(DiscoveryClient discoveryClient,
                                                                        DiscoveryLocatorProperties properties) {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, properties);
    }

    @Bean
    public RouteLocator gateway(RouteLocatorBuilder rlb) {
        return rlb.routes()
                .route(r -> r.path("/v2/w/**")
                        .filters(f -> f
                                .rewritePath("/v2/w/(?<segment>.*)", "/$\\{segment}")
                                .retry(config -> config.setRetries(2).setStatuses(HttpStatus.INTERNAL_SERVER_ERROR))
                        ).uri("lb://lottery-web")
                )
                .route(r -> r.path("/v2/c/**")
                        .filters(f -> f
                                .rewritePath("/v2/c/(?<segment>.*)", "/$\\{segment}")
                                .retry(config -> config.setRetries(2).setStatuses(HttpStatus.INTERNAL_SERVER_ERROR))
                        ).uri("lb://cpoms")
                )
                .route(r -> r.path("/v2/t/**")
                        .filters(f -> f
                                .rewritePath("/v2/t/(?<segment>.*)", "/$\\{segment}")
                                .retry(config -> config.setRetries(2).setStatuses(HttpStatus.INTERNAL_SERVER_ERROR))
                        ).uri("lb://lottery-task")
                )
                .build();
    }
}
