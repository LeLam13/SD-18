package com.example.demo.Config;
import com.example.demo.Service.impl.CustomAuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import java.io.IOException;
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
                        .failureUrl("/login?error=true") // Chuyển hướng đến trang login với tham số error=true khi đăng nhập thất bại
                        .successHandler(customAuthenticationSuccessHandler)
                )
                .logout(l -> l
                        .logoutUrl("/logout") // Đường dẫn cho logout
                        .logoutSuccessHandler(new LogoutSuccessHandler() {
                            @Override
                            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                                if (authentication != null) {
                                    // Kiểm tra vai trò của người dùng
                                    boolean isAdmin = authentication.getAuthorities().stream()
                                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
                                    boolean isStaff = authentication.getAuthorities().stream()
                                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_STAFF"));

                                    // Chuyển hướng theo vai trò
                                    if (isAdmin || isStaff) {
                                        response.sendRedirect("/login"); // Chuyển hướng đến trang đăng nhập
                                    } else {
                                        response.sendRedirect("/trang-chu"); // Chuyển hướng đến trang chủ
                                    }
                                } else {
                                    response.sendRedirect("/login"); // Nếu không có thông tin đăng nhập, chuyển hướng đến trang đăng nhập
                                }
                            }
                        }) // Gán LogoutSuccessHandler tùy chỉnh
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