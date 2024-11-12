package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

//@Data
@Entity
@Table(name = "nhan_vien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class     nhanvien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nhan_vien")
    private Integer idNhanVien;

    @Column(name = "ma_nhan_vien", nullable = false)
    private String maNhanVien;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;


    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "ngay_sinh")
    @Temporal(TemporalType.DATE)
    private LocalDate ngaySinh;

    @Column(name = "so_can_cuoc_cong_dan")
    private String soCanCuocCongDan;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "gioi_tinh")
    private String gioiTinh;

    @Column(name = "trang_thai", columnDefinition = "BIT DEFAULT 1")
    private boolean trangThai;


    @OneToOne
    @JoinColumn(name = "username_tai_khoan", referencedColumnName = "username", unique = true)
    private taikhoan taikhoan;


    @Column(name = "create_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime  createDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "delete_by")
    private String deleteBy;

}
