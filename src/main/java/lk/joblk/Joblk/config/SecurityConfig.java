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
        return httpSecurity.csrf (AbstractHttpConfigurer::disable).authorizeHttpRequests (request -> request.requestMatchers ("/api/v1/user/register", "/api/v1/user/login", "/api/v1/user/getAllUsers", "/api/v1/user/getUser/{username}", "/api/v1/course/addCourse/**", "/api/v1/course/updateCourse", "/api/v1/course/deleteCourse/**", "/api/v1/course/searchCourse/**", "/api/v1/course/getAllCourse", "/api/v1/course/upload/**", "/api/v1/course/get/image/**", "/api/v1/job/addJob/**", "/api/v1/job/updateJob", "/api/v1/job/deleteJob/**", "/api/v1/job/searchJob/**", "/api/v1/job/getAllJobs", "/api/v1/job/upload/**", "/api/v1/job/get/image/**", "/api/v1/job/delete/image/**", "/api/v1/job/update/image/**", "/api/v1/course/update/image/**", "/api/v1/course/delete/image/**", "/api/v1/user/updateUser", "/api/v1/user/deleteUser/**", "/api/v1/user/uploadProfile/**", "/api/v1/user/get/imageProfile/**", "/api/v1/user/delete/imageProfile/**", "/api/v1/user/update/imageProfile/**", "/api/v1/user/uploadCover/**", "/api/v1/user/get/imageCover/**", "/api/v1/user/delete/imageCover/**", "/api/v1/user/update/imageCover/**"

        ).permitAll ().anyRequest ().authenticated ()).addFilterBefore (jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).build ();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider ();
        provider.setPasswordEncoder (NoOpPasswordEncoder.getInstance ());
        provider.setUserDetailsService (userService);
        return provider;
    }

}
