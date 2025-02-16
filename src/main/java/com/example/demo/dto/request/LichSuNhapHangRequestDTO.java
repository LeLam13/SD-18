package com.example.demo.dto.request;

import com.example.demo.entity.SanPhamChiTiet;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class LichSuNhapHangRequestDTO {
    private Integer idLichSuNhapHang;

    private String ma;

    private Date createDate;

    private String createBy;

    private Integer soLuongNhap;

    private Float giaNhapNhap;

    private Integer idSanPhamChiTiet;

    private Integer idMauSacNhap;

    private Integer idKichCoNhap;

    private Integer idSanPham;
}
