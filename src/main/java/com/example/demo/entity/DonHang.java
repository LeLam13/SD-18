package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
//@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "don_hang")
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_don_hang")
    private Integer idDonHang;

    @Column(name = "ma_don_hang", nullable = false, length = 50)
    private String maDonHang;

    @Column(name = "tong_tien")
    private Float tongTien;

    @Column(name = "tong_tien_khuyen_mai")
    private Float tongTienKhuyenMai;

    @Column(name = "tong_tien_sau_khuyen_mai")
    private Float tongTienSauKhuyenMai;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    @Column(name = "trang_thai_thanh_toan")
    private Boolean trangThaiThanhToan;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien")
    private nhanvien nhanVien;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang")
    private khachhang khachHang;

    @ManyToOne
    @JoinColumn(name = "id_trang_thai")
    private TrangThai trangThai;

    @ManyToOne
    @JoinColumn(name = "id_phuong_thuc_thanh_toan")
    private PhuongThucThanhToan phuongThucThanhToan;

    @ManyToOne
    @JoinColumn(name = "id_khuyen_mai")
    private KhuyenMai khuyenMai;
}
