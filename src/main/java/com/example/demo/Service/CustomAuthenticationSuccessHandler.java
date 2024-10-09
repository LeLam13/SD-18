package com.example.demo.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        String redirectUrl;

        if (role.equals("ROLE_ADMIN")) {
            redirectUrl = "/admin/trang-chu";
        } else if(role.equals("ROLE_USER")) {
            redirectUrl = "/admin/trang-chu";
        } else if(role.equals("ROLE_STAFF")) {
            redirectUrl = "/staff/home";
        } else {
            redirectUrl = "/"; // Trang mặc định nếu không phải ADMIN hoặc STAFF
        }

        response.sendRedirect(redirectUrl);
    }
}
