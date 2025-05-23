package com.agile.shipmanagement.ShipManagement.config;


//import com.agile.shipmanagement.ShipManagement.services.CustomUserDetailsService;
//import com.agile.shipmanagement.ShipManagement.services.JwtService;
//import com.agile.shipmanagement.ShipManagement.utils.CustomAuthenticationEntryPoint;
//import com.agile.shipmanagement.ShipManagement.utils.JwtAuthFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig  {

//    private final CustomAuthenticationEntryPoint authenticationEntryPoint;


//    @Autowired
//    private CustomUserDetailsService userDetailsService;

//    @Autowired
//    private JwtService jwtService;

//    public ProjectSecurityConfig(CustomAuthenticationEntryPoint authenticationEntryPoint) {
//        this.authenticationEntryPoint = authenticationEntryPoint;
//    }


    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080") // replace with actual frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

    }
*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                .requestMatchers("/ships/**").permitAll()
//                        .requestMatchers("/api/v1/**").authenticated()
//                                .requestMatchers("/api/v1/allTodo").hasRole("ADMIN")

                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//                .addFilterBefore(new JwtAuthFilter(jwtService, userDetailsService),
//                        UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(authenticationEntryPoint) // 👈 This handles 401
//                );


        return http.build();
    }

   /* @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Fix: Define AuthenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

