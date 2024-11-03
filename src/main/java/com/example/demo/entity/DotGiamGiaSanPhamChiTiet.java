package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "giam_gia_san_pham_chi_tiet")
public class DotGiamGiaSanPhamChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Khóa chính cho bảng trung gian

    @ManyToOne
    @JoinColumn(name = "id_giam_gia", referencedColumnName = "id_giam_gia", nullable = false)
    private DotGiamGia dotGiamGia; // Liên kết với thực thể DotGiamGia

    @ManyToOne
    @JoinColumn(name = "id_san_pham_chi_tiet", referencedColumnName = "id_san_pham_chi_tiet", nullable = false)
    private SanPhamChiTiet sanPhamChiTiet; // Liên kết với thực thể SanPhamChiTiet

    @CreationTimestamp
    private LocalDateTime createDate; // Thời gian tạo, tự động được gán khi bản ghi được tạo

    @UpdateTimestamp
    private LocalDateTime updateDate; // Thời gian cập nhật, tự động được gán khi bản ghi được cập nhật
}
