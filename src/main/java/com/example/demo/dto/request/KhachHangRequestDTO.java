package com.example.demo.dto.request;


import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class KhachHangRequestDTO {
    public Integer idKhachHang;
    public String maKhachHang;
    public String soDienThoai;
//
//public class KhachHangRequestDTO {
//
//    @NotNull(message = "Customer code cannot be null")
//    @Size(max = 50, message = "Customer code must be less than 50 characters")
//    private String maKhachHang;
//
//    @NotNull(message = "Name cannot be null")
//    @Size(max = 100, message = "Name must be less than 100 characters")
//    private String hoTen;
//
//    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Birth date must be in the format YYYY-MM-DD")
//    private String ngaySinh;
//
//    @NotNull(message = "Phone number cannot be null")
//    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 and 15 digits")
//    private String soDienThoai;
//
//    @NotNull(message = "Gender cannot be null")
//    private boolean gioiTinh;
//
//    @NotNull(message = "Address cannot be null")
//    @Size(max = 255, message = "Address must be less than 255 characters")
//    private String diaChi;
//
//    @NotNull(message = "Username cannot be null")
//    private String usernameTaiKhoan;

}
