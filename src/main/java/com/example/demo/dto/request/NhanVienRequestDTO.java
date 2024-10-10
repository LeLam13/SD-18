package com.example.demosecurity.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class NhanVienRequestDTO {
    private int idNhanVien;
    private String maNhanVien;
    private String hoTen;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private String soCanCuocCongDan;
    private String diaChi;
    private String gioiTinh;
    private boolean trangThai;
}
