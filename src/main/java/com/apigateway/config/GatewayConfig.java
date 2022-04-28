package com.apigateway.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.apigateway.filter.JwtAuthenticationFilter;

@Configuration
public class GatewayConfig {
	@Autowired
	JwtAuthenticationFilter jwtFilter;

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder routeLocatorBuilder) {

		return routeLocatorBuilder.routes()
				.route("authentication-authorization-service", rt -> rt.path("/auth/**").filters(f -> f.filter(jwtFilter)).uri("http://localhost:3000/"))
				.route("user-service", rt -> rt.path("/api/v1/users/**").filters(f -> f.filter(jwtFilter)).uri("http://localhost:3005/"))
				.route("post-service", rt -> rt.path("/api/v1/posts/**").filters(f -> f.filter(jwtFilter)).uri("http://localhost:3010/"))
				.route("comment-service", rt -> rt.path("/api/v1/posts/{postId}/comments/**").filters(f -> f.filter(jwtFilter)).uri("http://localhost:3015/"))
				.route("like-service", rt -> rt.path("/api/v1/postsOrComments/{postorcommentId}/likes/**").filters(f -> f.filter(jwtFilter)).uri("http://localhost:3020/")).build();
	}
}
//	public RouteLocator routes(RouteLocatorBuilder builder,JwtAuthenticationFilter filter) {
//		return builder.routes().route("USER-SERVICE", r -> r.path("/users/**").filters(f -> f.filter(filter)).uri("http://localhost:3005"))
//				.route("COMMENT-SERVICE", r -> r.path("/posts/{postId}/comments/**").filters(f -> f.filter(filter)).uri("http://localhost:3015"))
//				.route("POST-SERVICE", r -> r.path("/posts/**").filters(f -> f.filter(filter)).uri("http://localhost:3010"))
//				.route("LIKE-SERVICE", r -> r.path("/postsOrComments/{postOrCommentId}/**").filters(f -> f.filter(filter)).uri("http://localhost:3020"))
//				.route("AUTHENTICATION-AUTHORIZATION-SERVICE", r -> r.path("/auth/**").filters(f -> f.filter(filter)).uri("http://localhost:3000")).build();
//	}

//}
