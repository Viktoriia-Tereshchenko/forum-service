package ait.cohort5860.security;

import ait.cohort5860.accounting.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

// Realization of AUTHORIZATION

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // since we made @Bean, the default authorization does not work
    @Bean
    SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        // our customization
        // each request must have an AUTHORIZATION header
        http.httpBasic(Customizer.withDefaults()); // // Basic Auth enabled
        http.csrf(csrf -> csrf.disable()); // csrf
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/account/register", "forum/posts/**")
                    .permitAll() // anyone can register
                .requestMatchers(("/account/user/{login}/role/{role}"))
                    .hasRole(Role.ADMINISTRATOR.name()) // only ADMINISTRATOR
                .requestMatchers(HttpMethod.PATCH, "/account/user/{login}")
                    .access(new WebExpressionAuthorizationManager("#login == authentication.name")) // only owner
                .requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
                    .access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMINISTRATOR')")) // only owner or ADMINISTRATOR

                .requestMatchers("/forum/post/{author}", "/forum/post/{id}/comment/{author}")
                    .access(new WebExpressionAuthorizationManager("#author == authentication.name"))

                .anyRequest()
                    .authenticated()); // authentication is required
        //.permitAll()); // allowed to all
        // request without header
        // or with header but correct (correct login and password)
        return http.build();
    }

    // WebExpressionAuthorizationManager makes an object from a String expression, that manages authorization
    // /**  - any subdirectories

    // crypting (with a key and using the system)
    // hashing - one way, can't unhash!

    // Spring Security uses BCrypt
    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
