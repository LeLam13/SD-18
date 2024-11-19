package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class KhachHangRequestDTO {

    @JsonProperty("ma_khach_hang")
    @NotNull(message = "Mã khách hàng không được để trống")
    @Size(max = 50, message = "Mã khách hàng không được vượt quá 50 ký tự")
    private String maKhachHang;

    @JsonProperty("ho_ten")
    @NotNull(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
    private String hoTen;

    @JsonProperty("ngay_sinh")
    @NotNull(message = "Ngày sinh không được để trống")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Ngày sinh phải theo định dạng yyyy-MM-dd")
    private String ngaySinh;

    @JsonProperty("so_dien_thoai")
    @NotNull(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng 0 và có 10-11 chữ số")
    private String soDienThoai;

    @JsonProperty("gioi_tinh")
    @NotNull(message = "Giới tính không được để trống")
    private Boolean gioiTinh;

    @JsonProperty("dia_chi")
    @Size(max = 300, message = "Địa chỉ không được vượt quá 300 ký tự")
    private String diaChi;

    @JsonProperty("username_tai_khoan")
    @Size(max = 50, message = "Tên tài khoản không được vượt quá 50 ký tự")
    private String usernameTaiKhoan;

    @JsonProperty("email")
    @NotNull(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;
}
