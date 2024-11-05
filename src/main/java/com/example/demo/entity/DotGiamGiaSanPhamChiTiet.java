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
    @Column(name = "id") // Khóa chính cho bảng trung gian
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_giam_gia", referencedColumnName = "id_giam_gia", nullable = false)
    private DotGiamGia dotGiamGia; // Liên kết với thực thể DotGiamGia

    @ManyToOne
    @JoinColumn(name = "id_san_pham_chi_tiet", referencedColumnName = "id_san_pham_chi_tiet", nullable = false)
    private SanPhamChiTiet sanPhamChiTiet; // Liên kết với thực thể SanPhamChiTiet

    @CreationTimestamp
    @Column(name = "create_date") // Thời gian tạo, tự động được gán khi bản ghi được tạo
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date") // Thời gian cập nhật, tự động được gán khi bản ghi được cập nhật
    private LocalDateTime updateDate;

    // Có thể thêm các trường khác nếu cần
}
