package me.malkon.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component // poderá ser injetada em outras classes como componente
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	// gera um token a partir de um usuário. Jwts.builder gera token
	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())// como vou assinar meu token e c qual algoritmo?
				.compact();
	}

	public boolean tokenValido(String token) {
		Claims claims = getClaims(token);// armazena as reivindicações do token, tempo expiração
		if (claims != null) {
			String username = claims.getSubject(); // obtem o username
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			// testa se o token está expirado
			if (username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}

	public String getUsername(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}

	// para obter os claims a partir de um token. Se o tk for invalido vai dar uma
	// exceção
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}
}