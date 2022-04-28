package com.apigateway.util;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.apigateway.exception.JwtTokenMalformedException;
import com.apigateway.exception.JwtTokenMissingException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtil {



	@Value("${jwt.secret}")
	private String jwtSecret;

	public Claims getClaims(final String token){
		try {
			return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		} catch (Exception e) {
			System.out.println(e.getMessage() + " => " + e);


		}
		return null ;
	}

	public void validateToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
		try {

			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);


		} catch (SignatureException ex) {

			throw new JwtTokenMalformedException("Invalid JWT signature");
		} catch (MalformedJwtException ex) {

			throw new JwtTokenMalformedException("Invalid JWT token");
		} catch (ExpiredJwtException ex) {

			throw new JwtTokenMalformedException("Expired JWT token");
		} catch (UnsupportedJwtException ex) {

			throw new JwtTokenMalformedException("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {

			throw new JwtTokenMissingException("JWT claims string is empty.");
		}
	}

}
