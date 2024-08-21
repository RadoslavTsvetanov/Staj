package uk.gov.hmcts.reform.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import uk.gov.hmcts.reform.demo.filter.JwtAuthenticationFilter;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);

        // Set the authenticationManager for the filter
        jwtAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/users/search").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/memory/upload").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/plans/{planId}/users").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/plans/{planId}/date-window").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/history/{historyId}/memories").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/history/{id}").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/users/{userId}").hasRole("USER")

            )
            //.formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())
            .rememberMe(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(request -> request
                .requestMatchers(new AntPathRequestMatcher("/api/auth/register/basic")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/auth/register/complete")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/auth/signin")).permitAll()
                //.requestMatchers(new AntPathRequestMatcher("/user-access/**")).permitAll()
                .requestMatchers(HttpMethod.POST, "/user-access/profile/update").permitAll()
                .requestMatchers(HttpMethod.GET, "/user-access/profile").permitAll()
                .requestMatchers(HttpMethod.GET, "/user-access/plans").permitAll()
                .requestMatchers(HttpMethod.POST, "/plans").permitAll()
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
