package uk.gov.hmcts.reform.demo.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/user-access/**").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/users/search").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/memory/upload").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/plans/{planId}/users").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/plans/{planId}/date-window").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/history/{historyId}/memories").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/history/{id}").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/plans").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/users/{userId}").hasRole("USER")

            )
            //.formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())
            .rememberMe(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request -> request
                .requestMatchers(new AntPathRequestMatcher("/api/register/basic")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/register/complete")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/signin")).permitAll()
                .anyRequest().hasRole("ADMIN")
            )
        ;

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin =
            User.withDefaultPasswordEncoder()
                .username("admin")
                .password("adminpassword")
                .roles("ADMIN", "USER")
                .build();

        UserDetails user =
            User.withDefaultPasswordEncoder()
                .username("username")
                .password("userpassword")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
