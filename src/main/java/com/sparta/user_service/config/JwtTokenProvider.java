package com.sparta.user_service.config;

import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.sparta.user_service.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	private final String SECRET_KEY = "9XzL1p6UytGLNrc5PaZy4wD6Urs89eQ5iVgD+sjUYoE=";
	private final long EXPIRATION = 1000L * 60 * 60;

	public String generateToken(User user) {
		Claims claims = Jwts.claims().setSubject(user.getUsername());
		claims.put("roles", user.getRoles().stream().map(r -> r.getRole().name()).toList());

		Date now = new Date();
		Date expiry = new Date(now.getTime() + EXPIRATION);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
			.compact();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);
		String username = claims.getSubject();
		List<SimpleGrantedAuthority> authorities = ((List<String>) claims.get("roles")).stream()
			.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
			.toList();

		return new UsernamePasswordAuthenticationToken(username, "", authorities);
	}

	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Claims parseClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
}

