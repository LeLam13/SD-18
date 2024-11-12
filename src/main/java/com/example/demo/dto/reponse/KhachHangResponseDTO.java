package com.example.demo.dto.reponse;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class KhachHangResponseDTO {
//    private int idKhachHang;
//
//    private String maKhachHang;
//
//    private String hoTen;
//
//    private String soDienThoai;
//
//    private String diaChi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class KhachHangResponseDTO {

    @JsonProperty("id_khach_hang")
    private Long id_khach_hang; // Match the type with entity

    @JsonProperty("ma_khach_hang")
    private String maKhachHang;

    @JsonProperty("ho_ten")
    private String hoTen;

    @JsonProperty("ngay_sinh")
    private String ngaySinh; // Keep this as String for formatted date

    @JsonProperty("so_dien_thoai")
    private String soDienThoai; // Match the type with entity

    @JsonProperty("gioi_tinh")
    private Boolean gioiTinh; // Use String for 'Nam'/'Nữ'

    @JsonProperty("dia_chi")
    private String diaChi;

    @JsonProperty("username_tai_khoan")
    private String usernameTaiKhoan;



}
