package com.example.demo.Service.impl;

import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.repo.HoaDonChiTietRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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
            redirectUrl = "/trang-chu";
        } else if(role.equals("ROLE_STAFF")) {
            redirectUrl = "/admin/trang-chu";
        } else {
            redirectUrl = "/"; // Trang mặc định nếu không phải ADMIN hoặc STAFF
        }

        response.sendRedirect(redirectUrl);
    }

}
