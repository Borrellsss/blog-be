package it.itj.academy.blogbe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.itj.academy.blogbe.dto.output.user.UserOutputDto;
import it.itj.academy.blogbe.repository.UserRepository;
import it.itj.academy.blogbe.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class BeanConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JWTUtil jwtUtil, ObjectMapper objectMapper, UserRepository userRepository) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
            .cors(corsConfigurer -> corsConfigurer.configurationSource(request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.addAllowedOrigin("http://localhost:4200/");
                corsConfiguration.addAllowedMethod("*");
                corsConfiguration.addAllowedHeader("*");
                return corsConfiguration;
            }))
            .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                // TESTING ----------------------------------------------------------------
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/**")
                    .permitAll();
//                // POST -------------------------------------------------------------------
//                authorizationManagerRequestMatcherRegistry
//                    .requestMatchers(HttpMethod.POST,
//                        "/users/sign-up",
//                        "/users/sign-in")
//                    .permitAll();
//                // GET --------------------------------------------------------------------
//                authorizationManagerRequestMatcherRegistry
//                    .requestMatchers(HttpMethod.GET,
//                        "/users")
//                    .hasAnyRole("ADMIN", "SUPER_ADMIN");
//                authorizationManagerRequestMatcherRegistry
//                    .requestMatchers(HttpMethod.GET,
//                        "/users/{id}",
//                        "/users/username/{username}")
//                    .hasAnyRole("USER", "MODERATOR", "ADMIN", "SUPER_ADMIN");
//                // UPDATE -----------------------------------------------------------------
//                authorizationManagerRequestMatcherRegistry
//                    .requestMatchers(HttpMethod.PUT,
//                        "/users/id/{id}")
//                    .hasAnyRole("USER", "MODERATOR", "ADMIN", "SUPER_ADMIN");
//                // DELETE -----------------------------------------------------------------
//                authorizationManagerRequestMatcherRegistry
//                    .requestMatchers(HttpMethod.DELETE,
//                        "/users/{id}")
//                    .hasAnyRole("USER", "MODERATOR", "ADMIN", "SUPER_ADMIN");
//                // ALL --------------------------------------------------------------------
//                authorizationManagerRequestMatcherRegistry
//                    .requestMatchers("roles/**", "/validations/**", "error-messages/**")
//                    .hasAnyRole("ADMIN", "SUPER_ADMIN");
            }).addFilterBefore((ServletRequest servletRequest,ServletResponse servletResponse, FilterChain filterChain) -> {
                HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
                String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
                if (authorization != null && authorization.startsWith(jwtUtil.getPrefix())) {
                    try {
                        String jwt = authorization.split(" ")[1];
                        String userClaim = jwtUtil.decode(jwt).getClaim("user").asString();
                        UserOutputDto userOutputDto = objectMapper.readValue(userClaim, UserOutputDto.class);
                        userRepository.findByUsername(userOutputDto.getUsername())
                            .ifPresent(user ->
                                SecurityContextHolder.getContext()
                                    .setAuthentication(new UsernamePasswordAuthenticationToken(user, null, List.of(user.getRole()))));
                    } catch (Exception e) {
                        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        httpServletResponse.getWriter().write("Invalid Token");
                        return;
                    }
                }
                filterChain.doFilter(servletRequest, servletResponse);
            }, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
