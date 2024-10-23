package com.example.demo.Config;

import com.example.demo.Service.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
        String[] adminPermitAll = {"/assets/**","/admin/AngularJs/**", "/admin/assets/**", "/admin/css/**", "/admin/images/**", "/admin/js/**"};
        http.csrf().disable().cors().disable();

        http.authorizeHttpRequests(r -> r
                        .requestMatchers(adminPermitAll).permitAll()
                        .requestMatchers("/admin/nhan-vien").hasRole("ADMIN")
                        .requestMatchers("/guest/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().permitAll()
                )
                .formLogin(f -> f
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                )
                .logout(l -> l
                        .logoutUrl("/admin/logout") // Đường dẫn cho logout
                        .logoutSuccessUrl("/login") // Trang chuyển hướng sau khi logout
                        .invalidateHttpSession(true) // Vô hiệu hóa phiên làm việc
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true) // Xóa thông tin xác thực
                )
                .exceptionHandling(e -> e
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }


}