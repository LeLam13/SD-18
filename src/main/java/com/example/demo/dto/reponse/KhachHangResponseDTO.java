package com.example.demo.dto.reponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHangResponseDTO {
    private int idKhachHang;

    private String maKhachHang;

    private String hoTen;

    private int soDienThoai;

    private String diaChi;
}
