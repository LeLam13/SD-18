package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "khuyen_mai")
public class KhuyenMai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_khuyen_mai")
    private Integer idKhuyenMai;

    @Column(name = "ma_khuyen_mai", nullable = false, length = 100)
    private String maKhuyenMai;

    @Column(name = "ten_khuyen_mai", nullable = false, length = 100)
    private String tenKhuyenMai;

    @Column(name = "muc_giam_gia", nullable = false, length = 100)
    private Integer mucGiamGia;

    @Column(name = "thoi_gian_bat_dau", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date thoiGianKetThuc;

    @Column(name = "mo_ta", length = 100)
    private String moTa;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "dieu_kien_ap_dung", length = 100)
    private String dieuKienApDung;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "so_tien_toi_thieu", nullable = false)
    private Integer soTienToiThieu; // Điều kiện áp dụng: số tiền tối thiểu
}
