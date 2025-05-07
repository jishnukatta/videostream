package com.videostreaming.apigateway.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.WebFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    private static final String SECRET_KEY = "jishnu";  // Secret key for JWT

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeExchange()
            .pathMatchers("/auth/**").permitAll()
            .pathMatchers("/test").permitAll()
            .pathMatchers("/finalize").permitAll()
            .pathMatchers("/videos/**").permitAll()
            .pathMatchers("/stream/**").permitAll()
            .pathMatchers("/user/**").hasAnyRole("USER","ADMIN")  
            .anyExchange().authenticated()  // Protect all other paths
            .and()
            .addFilterBefore(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);  // Add the custom filter before authentication

        return http.build();
    }

    @Bean
    public WebFilter jwtAuthenticationFilter() {
        return (exchange, chain) -> {
        	String path = exchange.getRequest().getPath().value();
			if (path.startsWith("/auth/") || path.startsWith("/test") || path.startsWith("/finalize")
					|| path.startsWith("/videos") || path.startsWith("/stream"))
        	{
        		return chain.filter(exchange);
        	}
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);  
            }

            if (token != null) {
                try {
                    // Parse the JWT token
                    Claims claims = Jwts.parser()
                            .setSigningKey(SECRET_KEY)
                            .parseClaimsJws(token)
                            .getBody();
                    
                    System.out.println(claims);

                    // Extract authorities from JWT claims
                    List<SimpleGrantedAuthority> authorities = extractAuthorities(claims);
                    
                    System.out.println("authorities: " + authorities);

                    // Create the Authentication object with roles
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            authorities);

                    System.out.println("JWT authenticated");
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)); //placing authentication in security context
                    

                } catch (ExpiredJwtException e) {
                    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired"));
                } catch (SignatureException e) {
                    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature"));
                } catch (MalformedJwtException e) {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed token"));
                } catch (Exception e) {
                	System.out.println(e);
                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error while validating token"));
                }
            }

            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or expired token"));
        };
    }

	private List<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
		 Object roleClaim = claims.get("role");

		    if (roleClaim instanceof String) {
		        return List.of(new SimpleGrantedAuthority("ROLE_"+roleClaim));
		    } 
		    
		    return List.of();  
	}
}
