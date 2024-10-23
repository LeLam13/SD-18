package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name ="hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_hoa_don")
    private Integer idHoaDon;

    @Column(name="ma_hoa_don")
    private String maHoaDon;
    @Column(name = "create_by")
    private Date createBy;
    @Column(name="update_date")
    private Date updateDate;
    @Column(name = "update_by")
    private Date updateBy;
    @Column(name = "tong_tien")
    private float tongTien;
    @Column(name = "tong_tien_khuyen_mai")
    private float tongTienKhuyenMai;
    @Column(name = "tong_tien_sau_khuyen_mai")
    private float tongTienSauKhuyenMai;
    @Column(name="ghi_chu")
    private String ghiChu;
    @Column(name="trang_thai_thanh_toan")
    private boolean trangThaiThanhToan;
}