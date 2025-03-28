package com.example.demo.dto.reponse;

import com.example.demo.entity.khachhang;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonHangResponseDTO {
    private Integer idDonHang;
    private String maDonHang;
    private String tenKhachNhan;
    private String soDienThoaiKhachNhan;
    private String diaChiNhan;
    private Float tongTien;
    private Float tongTienKhuyenMai;
    private Float tongTienSauKhuyenMai;
    private String ghiChu;
    private Boolean trangThaiThanhToan;
    private Integer loaiDonHang;
    private String nhanVien;
    private String khachHang;
    private Long id_khach_hang;
    private khachhang oldKhachHang;

}
