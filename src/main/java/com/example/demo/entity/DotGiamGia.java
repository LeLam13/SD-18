package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "giam_gia")
public class DotGiamGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_giam_gia") // Khóa chính cho bảng giảm giá
    private int idGiamGia;

    @Column(name = "giam_gia") // Trường này có thể là số tiền giảm hoặc tỷ lệ giảm
    private float giamGia;

    @Column(name = "thoi_gian_bat_dau") // Thời gian bắt đầu của chương trình giảm giá
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc") // Thời gian kết thúc của chương trình giảm giá
    private LocalDateTime thoiGianKetThuc;

    @Column(name = "trang_thai") // 0: sắp diễn ra, 1: đang diễn ra, 2: đã kết thúc
    private int trangThai;


    @Column(name = "loai_giam_gia")
    private int  loaiGiamGia; // hoặc boolean nếu bạn không cần giá trị null

    @CreationTimestamp
    @Column(name = "create_date") // Thời gian tạo, tự động được gán khi bản ghi được tạo
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date") // Thời gian cập nhật, tự động được gán khi bản ghi được cập nhật
    private LocalDateTime updateDate;


}
