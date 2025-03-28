package com.example.demo.dto.reponse;

import com.example.demo.entity.PhuongThucThanhToan;
import com.example.demo.entity.TrangThai;
import lombok.Data;

@Data
public class DonHangOnlineResponseDTO {
    private Integer idDonHang;
    private String tenKhachHang;
    private String soDienThoaiKhachHang;
    private String diaChiKhachHang;
    private String emailKhachHang;
    private String maDonHang;
    private Float tongTien;
    private Float tongTienKhuyenMai;
    private Float tongTienSauKhuyenMai;
    private Float tongTienThanhToan;
    private Float phiVanChuyen;
    private String ghiChu;
    private Boolean trangThaiThanhToan;
    private Integer loaiDonHang;
    private Integer idTrangThai;
    private TrangThai trangThai;
    private Integer idPhuongThucThanhToan;
    private PhuongThucThanhToan phuongThucThanhToan;
    private Integer idKhuyenMai;
}
