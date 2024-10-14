package com.example.demo.dto.request;

import lombok.Data;

@Data
public class UserSignupRequestDTO {
    private String username;     // Tên đăng nhập
    private String password;     // Mật khẩu
    private String email;        // Địa chỉ email
    private String hoTen;        // Họ và tên
    private String soDienThoai;  // Số điện thoại
    private String diaChi;       // Địa chỉ
    private boolean gioiTinh;    // Giới tính
}