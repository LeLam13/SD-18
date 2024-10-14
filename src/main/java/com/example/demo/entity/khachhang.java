package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

//@Data
@Entity
@Table(name = "khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class khachhang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idKhachHang;

    @Column(name = "ma_khach_hang", length = 100)
    private String maKhachHang;

    @Column(name = "ho_ten", length = 100)
    private String hoTen;

    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Column(name = "so_dien_thoai")
    private int soDienThoai; // Thay đổi thành String để tránh vấn đề với số không đầu

    @Column(name = "gioi_tinh")
    private boolean gioiTinh;

    @Column(name = "dia_chi", length = 300)
    private String diaChi;

    @OneToOne
    @JoinColumn(name = "username_tai_khoan", referencedColumnName = "username", unique = true)
    private taikhoan taikhoan; // Tham chiếu đến tài khoản
}
