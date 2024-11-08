package com.example.demo.dto.reponse;

import com.example.demo.entity.SanPhamChiTiet;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

public class DotGiamGiaResponse {

    private Integer idGiamGia;

    private Double giamGia;

    private LocalDateTime thoiGianBatDau;

    private LocalDateTime thoiGianKetThuc;

    private int trangThai;

    private int loaiGiamGia;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private List<SanPhamChiTiet> sanPhamChiTietList;
}
