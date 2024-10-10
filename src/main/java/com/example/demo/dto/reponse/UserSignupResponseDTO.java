package com.example.demo.dto.reponse;

import lombok.Data;

@Data
public class UserSignupResponseDTO {
    private int idKhachHang;     // ID khách hàng
    private String username;      // Tên đăng nhập
    private String message;       // Thông điệp phản hồi
    private boolean trangthai;    // Trạng thái tài khoản
}
