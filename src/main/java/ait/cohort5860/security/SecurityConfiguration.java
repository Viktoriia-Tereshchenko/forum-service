package ait.cohort5860.security;

import ait.cohort5860.accounting.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
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
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CustomWebSecurity webSecurity;

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
                .requestMatchers(HttpMethod.PATCH, "/account/user/{login}", "/forum/post/{id}/comment/{login}")
                    .access(new WebExpressionAuthorizationManager("#login == authentication.name")) // only owner
                .requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
                    .access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMINISTRATOR')")) // only owner or ADMINISTRATOR
                .requestMatchers(HttpMethod.POST, "/forum/post/{author}")
                    .access(new WebExpressionAuthorizationManager("#author == authentication.name"))
                .requestMatchers(HttpMethod.PATCH, "/forum/post/{id}")
                    .access(((authentication, context) ->
                        new AuthorizationDecision(webSecurity.checkPostAuthor(context.getVariables().get("id"), authentication.get().getName()))))
                .requestMatchers(HttpMethod.DELETE, "/forum/post/{id}")
                    .access((authentication, context) -> {
                    boolean isAuthor = webSecurity.checkPostAuthor(context.getVariables().get("id"), authentication.get().getName());
                    boolean isModerator = context.getRequest().isUserInRole(Role.MODERATOR.name());
                    return new AuthorizationDecision(isAuthor || isModerator);
                })
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
