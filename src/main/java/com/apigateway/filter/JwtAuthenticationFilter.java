package com.apigateway.filter;

import java.util.List;
import java.util.function.Predicate;

import com.apigateway.constfile.ConstFile;
import com.apigateway.exception.JwtTokenMalformedException;
import com.apigateway.exception.JwtTokenMissingException;
import com.apigateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

	@Autowired
	private JwtUtil jwtUtil;


	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		final List<String> apiEndpoints = List.of("/signup", "/login");


		Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
				.noneMatch(uri -> r.getURI().getPath().contains(uri));

		if (isApiSecured.test(request)) {

			if (!request.getHeaders().containsKey(ConstFile.AUTHORIZATION) || !request.getHeaders().get(ConstFile.AUTHORIZATION).get(0).startsWith(ConstFile.BEARER)) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.UNAUTHORIZED);

				return response.setComplete();
			}

			final String token = request.getHeaders().getOrEmpty(ConstFile.AUTHORIZATION).get(0).substring(7);

			try {
				jwtUtil.validateToken(token);
				
			} catch (JwtTokenMalformedException | JwtTokenMissingException e) {


				ServerHttpResponse response = exchange.getResponse();

				response.setStatusCode(HttpStatus.BAD_REQUEST);

				return response.setComplete();
			}


			Claims claims = jwtUtil.getClaims(token);
			exchange.getRequest().mutate().header("id", String.valueOf(claims.get("id"))).build();
		}

		return chain.filter(exchange);
	}

}
