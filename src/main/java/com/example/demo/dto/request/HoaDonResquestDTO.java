package com.example.demo.dto.request;

import lombok.Data;

@Data
public class HoaDonResquestDTO {
    private String maHoaDon;
    private Integer  idKhuyenMai;
    private Integer idTrangThai;
    private Integer idPhuongThucThanhToan;
    private Integer idDonHang;
    private Integer  idNhanVien;
    private String tenKhachHang;
    private Float tongTien;
    private Float tongTienKhuyenMai;
    private Float tongTienSauKhuyenMai;
    private String ghiChu;
}
