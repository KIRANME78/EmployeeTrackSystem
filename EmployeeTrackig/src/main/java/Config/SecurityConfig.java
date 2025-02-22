package Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/employees").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                        .requestMatchers("/employees/{id}").access((authentication, request) -> {
                            String path = request.getRequest().getRequestURI();
                            Long id = Long.valueOf(path.substring(path.lastIndexOf('/') + 1));
                            boolean authorized = authentication.get().getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")) ||
                                    webSecurity.checkEmployeeId(authentication.get(), id);
                            return new AuthorizationDecision(authorized);
                        })
                        .requestMatchers("/departments").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                        .requestMatchers("/projects").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                        .requestMatchers("/employees/search").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                        .requestMatchers("/departments/{id}/projects").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/my-client-oidc")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error")
                );
        return http.build();
    }

    private final WebSecurity webSecurity;

    public SecurityConfig(WebSecurity webSecurity) {
        this.webSecurity = webSecurity;
    }
}
