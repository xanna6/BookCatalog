package com.apiotrowska.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator myRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("eureka", r -> r.path("/eureka/web")
                        .filters(f -> f.redirect(302, "http://localhost:8761"))
                        .uri("http://localhost:8761"))
                .build();
    }
}
