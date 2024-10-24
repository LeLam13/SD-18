package com.example.demo.dto.reponse;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class HoaDonResponseDTO {
    private String maHoaDon;

    private Float tongTien;

    private Float tongTienKhuyenMai;

    private Float tongTienSauKhuyenMai;

    private String ghiChu;

    private Boolean trangThaiThanhToan;

    private Integer idKhuyenMai;
    private Integer idTrangThai;
    private Integer idPTTT;
    private Integer idDonHang;
    private Integer idNhanVien;
    private Integer idKhachHang;
}
