package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_san_pham_chi_tiet")
    private int idSanPhamChiTiet;

    @Column(name = "ma")
    private String ma;

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


//    @Column(name = "chat_lieu")
//    private String chatLieu;


//    @Column(name = "size")
//    private String size;
//
    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "gia_nhap")
    private Float giaNhap;

    @Column(name = "gia_ban")
    private Float giaBan;

    @Column(name = "so_tien_giam")
    private Float soTienGiam;

    @Column(name = "mo_ta")
    private String moTa;


    @ManyToOne
    @JoinColumn(name = "id_san_pham")
    private SanPham idSanPham;


    @ManyToOne
    @JoinColumn(name = "id_mau_sac")
    private MauSac idMauSac;

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
    @JoinColumn(name = "id_kich_co")
    private KichCo idKichCo;

    @ManyToOne
    @JoinColumn(name = "id_xuat_xu")
    private XuatXu idXuatXu;

    @ManyToOne
    @JoinColumn(name = "id_hinh_anh")
    private HinhAnh idHinhAnh;

    @ManyToMany(mappedBy = "sanPhamChiTietList",fetch = FetchType.LAZY)
    @JsonBackReference
    private List<DotGiamGia> dotGiamGiaList; // Liên kết với bảng trung gian
}
