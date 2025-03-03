package lk.joblk.Joblk.config;


import lk.joblk.Joblk.service.UserService;
import lk.joblk.Joblk.utils.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf (AbstractHttpConfigurer::disable).authorizeHttpRequests (request -> request.requestMatchers (
                "/api/v1/user/register", "/api/v1/user/login").permitAll ().anyRequest ().authenticated ())
//                .authorizeHttpRequests (request -> request.requestMatchers (
//                        ""
//
//                ).hasRole ("").anyRequest ().authenticated ())
                .addFilterBefore (jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).build ();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider ();
        provider.setPasswordEncoder (NoOpPasswordEncoder.getInstance ());
        provider.setUserDetailsService (userService);
        return provider;
    }

}
