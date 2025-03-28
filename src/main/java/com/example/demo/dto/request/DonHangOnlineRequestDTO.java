package com.example.demo.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class DonHangOnlineRequestDTO {
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
    private Integer idPhuongThucThanhToan;
    private Integer idKhuyenMai;
//    private List<OrderDetailDTO> orderDetail;
    private List<DonHangChiTietRequestDTO> orderDetail;
}
