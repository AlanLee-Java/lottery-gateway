package com.blockeng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author qiang
 */
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http.httpBasic().and()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(
                        "/actuator/**",
                        "/v2/s/actuator/**",
                        "/v2/u/actuator/**",
                        "/v2/a/actuator/**",
                        "/v2/t/actuator/**",

                        "/v2/s/coin/selectById",
                        "/v2/s/conf/**",
                        "/v2/s/currency/selectById",
                        "/v2/s/sms/sendTo",
                        "/v2/s/trade/market/refresh/**",
                        "/v2/s/api/v1/dm/verify",

                        "/v2/u/cards/selectByUserIdAndCurrencyId",
                        "/v2/u/cards/selectById",
                        "/v2/u/user/selectById",
                        "/v2/u/user/transferAddress",
                        "/v2/u/user/loadUserByUsername",
                        "/v2/u/user/security/checkGA",
                        "/v2/u/user/security/checkPayPassword",
                        "/v2/u/user/wallet/selectById",
                        "/v2/u/user/wallet/insert"
                ).authenticated()
                .anyExchange().permitAll()
                .and().formLogin()
                .and().build();
    }
}
