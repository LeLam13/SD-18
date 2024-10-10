package com.example.demo.dto.reponse;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class NhanVienResponseDTO {
    private int idNhanVien;
    private String maNhanVien;
    private String hoTen;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private String soCanCuocCongDan;
    private String diaChi;
    private String gioiTinh;
    private boolean trangThai;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
