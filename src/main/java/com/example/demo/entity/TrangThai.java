package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
//@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "trang_thai")
public class TrangThai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trang_thai")
    private Integer idTrangThai;

    @Column(name = "ma_trang_thai", nullable = false, length = 50)
    private String maTrangThai;

    @Column(name = "ten_trang_thai", nullable = false, length = 150)
    private String tenTrangThai;

    @Column(name = "trang_thai")
    private Boolean trangThai;
}
