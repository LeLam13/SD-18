package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSanPham;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "create_date")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date createDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_date")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "mo_ta")
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "id_thuong_hieu")
    private ThuongHieu idThuongHieu;

    @ManyToOne
    @JoinColumn(name = "id_kieu_dang")
    private KieuDang idKieuDang;

    @ManyToOne
    @JoinColumn(name = "id_chat_lieu")
    private ChatLieu idChatLieu;

    @ManyToOne
    @JoinColumn(name = "id_xuat_xu")
    private XuatXu idXuatXu;

    @Transient
    private String hinhAnh;

    @Transient
    private Integer totalSold;

    @Transient
    private Integer totalInventory;

    @Transient
    private Float minGiaBan;

}
