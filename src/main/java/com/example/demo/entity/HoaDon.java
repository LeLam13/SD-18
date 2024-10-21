package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "hoa_don")
@Data
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHoaDon;

    @Column(name = "ma_hoa_don")
    private String maHoaDon;

    @Column(name = "create_date")
    @Temporal(TemporalType.DATE)
    private LocalDate createDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_date")
    @Temporal(TemporalType.DATE)
    private LocalDate updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "tong_tien")
    private Float tongTien;

    @Column(name = "tong_tien_khuyen_mai")
    private Float tongTienKhuyenMai;

    @Column(name = "tong_tien_sau_khuyen_mai")
    private Float tongTienSauKhuyenMai;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "trang_thai_thanh_toan")
    private Boolean trangThaiThanhToan;

    @ManyToOne
    @JoinColumn(name = "id_khuyen_mai")
    private KhuyenMai khuyenMai;

    @ManyToOne
    @JoinColumn(name = "id_trang_thai")
    private TrangThai trangThai;

    @ManyToOne
    @JoinColumn(name = "id_phuong_thuc_thanh_toan")
    private PhuongThucThanhToan phuongThucThanhToan;

    @ManyToOne
    @JoinColumn(name  = "id_don_hang")
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien")
    private nhanvien nhanVien;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang")
    private khachhang khachHang;

}
