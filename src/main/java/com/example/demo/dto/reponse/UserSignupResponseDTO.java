package com.example.demo.dto.reponse;

import lombok.Data;

@Data
public class UserSignupRequestDTO {
    private int idKhachHang;     // ID khách hàng
    private String username;      // Tên đăng nhập
    private String message;       // Thông điệp phản hồi
    private boolean trangThai;    // Trạng thái tài khoản
}
