package com.example.demo.entity;

import com.example.demo.entity.DotGiamGiaSanPhamChiTiet;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "giam_gia")
public class DotGiamGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_giam_gia")
    private int idGiamGia;

    @Column(name = "giam_gia")
    private float giamGia;

    @Column(name = "thoi_gian_bat_dau")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    private LocalDateTime thoiGianKetThuc;

    @Column(name = "trang_thai")
    private int trangThai;

    @Column(name = "loai_giam_gia")
    private int loaiGiamGia;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    // Thiết lập mối quan hệ với bảng trung gian
    @OneToMany(mappedBy = "dotGiamGia")
    private List<DotGiamGiaSanPhamChiTiet> sanPhamChiTietList; // Liên kết với bảng trung gian
}
