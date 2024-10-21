package com.example.demo.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
public class NhanVienRequetsDTO {
    private Integer idNhanVien;
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String maNhanVien;
    private String hoTen;
    private String soDienThoai;

    @DateTimeFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày phù hợp với form
    private LocalDate ngaySinh;  // Chuyển sang kiểu Date

    private String soCanCuocCongDan;
    private String diaChi;
    private String gioiTinh;
    private String vaiTro;
    private Date createDate;
    private Date updateDate;
}
