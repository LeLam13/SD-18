package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
    private Integer idGiamGia;

    @Column(name = "giam_gia")
    private Double giamGia;

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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "giam_gia_san_pham_chi_tiet",
            joinColumns = @JoinColumn(name = "id_giam_gia"),
            inverseJoinColumns = @JoinColumn(name = "id_san_pham_chi_tiet")
    )
    @Fetch(FetchMode.JOIN)
    @JsonManagedReference
    private List<SanPhamChiTiet> sanPhamChiTietList;
}
