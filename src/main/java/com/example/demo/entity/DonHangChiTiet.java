package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
//@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "don_hang_chi_tiet")
public class DonHangChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_don_hang_chi_tiet")
    private Integer idDonHangChiTiet;

    @Column(name = "ma_don_hang_chi_tiet", length = 50)
    private String maDonHangChiTiet;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "don_gia")
    private Float donGia;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    @ManyToOne
    @JoinColumn(name = "id_don_hang", nullable = false)
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "id_san_pham_chi_tiet", nullable = false)
    private SanPhamChiTiet sanPhamChiTiet;
}
