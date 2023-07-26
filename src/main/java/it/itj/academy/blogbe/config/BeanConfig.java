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
import org.springframework.http.HttpMethod;
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
                // ERROR ///////////////////////////////////////////////////////////////////////////////////////////////
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/error/**")
                    .permitAll();
                // USER ////////////////////////////////////////////////////////////////////////////////////////////////
                // POST
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST,
                        "/users/sign-up",
                        "/users/sign-in"
                    ).permitAll();
                // GET
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET,
                        "/users/**"
                    ).authenticated();
                // PUT
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.PUT,
                        "/users/{id}",
                        "/users/{id}/password"
                    ).authenticated();
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.PUT,
                        "/users/{id}/role/{roleId}"
                    ).hasAnyRole("ADMIN", "SUPER_ADMIN");
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.PUT,
                        "/users/{id}/block-or-unblock"
                    ).hasRole("MODERATOR");
                // DELETE
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE,
                        "/users/{id}"
                    ).authenticated();

                // ROLE ////////////////////////////////////////////////////////////////////////////////////////////////
                // POST
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST,
                        "/roles"
                    ).hasAnyRole("ADMIN", "SUPER_ADMIN");
                // GET
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET,
                        "/roles/**"
                    ).authenticated();
                // PUT
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.PUT,
                        "/roles/{id}"
                    ).hasAnyRole("ADMIN", "SUPER_ADMIN");
                // DELETE
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE,
                        "/roles/{id}"
                    ).hasAnyRole("ADMIN", "SUPER_ADMIN");

                // CATEGORY ////////////////////////////////////////////////////////////////////////////////////////////
                // POST
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST,
                        "/categories"
                    ).hasAnyRole("MODERATOR", "ADMIN", "SUPER_ADMIN");
                // GET
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET,
                        "/categories/**"
                    ).authenticated();
                // PUT
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.PUT,
                        "/categories/{id}"
                    ).hasAnyRole("MODERATOR", "ADMIN", "SUPER_ADMIN");
                // DELETE
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE,
                        "/categories/{id}"
                    ).hasAnyRole("MODERATOR", "ADMIN", "SUPER_ADMIN");

                // TAG /////////////////////////////////////////////////////////////////////////////////////////////////
                // POST
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST,
                        "/tags"
                    ).hasAnyRole("MODERATOR", "ADMIN", "SUPER_ADMIN");
                // GET
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET,
                        "/tags/**"
                    ).authenticated();
                // PUT
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.PUT,
                        "/tags/{id}"
                    ).hasAnyRole("MODERATOR", "ADMIN", "SUPER_ADMIN");
                // DELETE
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE,
                        "/tags/{id}"
                    ).hasAnyRole("MODERATOR", "ADMIN", "SUPER_ADMIN");

                // POST ////////////////////////////////////////////////////////////////////////////////////////////////
                // POST
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST,
                        "/posts"
                    ).authenticated();
                // GET
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET,
                        "/posts/**"
                    ).authenticated();
                // PUT
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.PUT,
                        "/posts/{id}"
                    ).authenticated();
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.PUT,
                        "/posts/{id}/accept",
                        "/posts/{id}/reject"
                    ).hasRole("MODERATOR");
                // DELETE
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE,
                        "/posts/{id}"
                    ).authenticated();

                // VOTE ////////////////////////////////////////////////////////////////////////////////////////////////
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/votes/**")
                    .authenticated();

                // COMMENT /////////////////////////////////////////////////////////////////////////////////////////////
                // POST
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST,
                        "/comments"
                    ).authenticated();
                // GET
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET,
                        "/comments/**"
                    ).authenticated();
                // PUT
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.PUT,
                        "/comments/{id}"
                    ).authenticated();
                // DELETE
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE,
                        "/comments/{id}"
                    ).authenticated();

                // VALIDATION //////////////////////////////////////////////////////////////////////////////////////////
                // POST
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST,
                        "/validations"
                    ).hasAnyRole("ADMIN", "SUPER_ADMIN");
                // GET
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET,
                        "/validations/**"
                    ).permitAll();
                // PUT
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.PUT,
                        "/validations/{code}"
                    ).hasAnyRole("ADMIN", "SUPER_ADMIN");
                // DELETE
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE,
                        "/validations/{code}"
                    ).hasAnyRole("ADMIN", "SUPER_ADMIN");

                // ERROR MESSAGE ///////////////////////////////////////////////////////////////////////////////////////
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/error-messages/**")
                    .hasAnyRole("ADMIN", "SUPER_ADMIN");
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
