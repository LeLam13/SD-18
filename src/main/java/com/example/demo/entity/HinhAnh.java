package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "hinh_anh")
public class HinhAnh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHinhAnh;

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
}
