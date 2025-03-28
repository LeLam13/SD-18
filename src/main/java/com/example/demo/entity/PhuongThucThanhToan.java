package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
//@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "phuong_thuc_thanh_toan")
public class PhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phuong_thuc_thanh_toan")
    private Integer idPhuongThucThanhToan;

    @Column(name = "ma_phuong_thuc_thanh_toan", nullable = false, length = 50)
    private String maPhuongThucThanhToan;

    @Column(name = "ten_trang_thai", nullable = false, length = 150)
    private String tenTrangThai;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createDate;

    @Column(name = "create_by", nullable = false, length = 100)
    private String createBy;

    @Column(name = "update_date")
    @Temporal(TemporalType.DATE)
    private Date updateDate;

    @Column(name = "update_by", length = 100)
    private String updateBy;
}
