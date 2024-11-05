package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class khachhang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKhachHang; // Change to Long to match DTO

    @Column(name = "ma_khach_hang", length = 100)
    private String maKhachHang;

    @Column(name = "ho_ten", length = 100)
    private String hoTen;

    @Column(name = "email")
    private String email;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh; // Use LocalDate

    @Column(name = "so_dien_thoai")

    private String soDienThoai; // Thay đổi thành String để tránh vấn đề với số không đầu


    @Column(name = "gioi_tinh")
    private boolean gioiTinh; // This is fine as boolean

    @Column(name = "dia_chi", length = 300)
    private String diaChi;

    @OneToOne
    @JoinColumn(name = "username_tai_khoan", referencedColumnName = "username", unique = true)
    private taikhoan taikhoan; // Reference to the account


}
