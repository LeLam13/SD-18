package com.example.demo.dto.request;

import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.HinhAnh;
import com.example.demo.entity.KichCo;
import com.example.demo.entity.KieuDang;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.ThuongHieu;
import com.example.demo.entity.XuatXu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamChiTietRequestDTO {
    private Integer idSanPhamChiTiet;
    private String ma;
    private Date createDate;
    private Date updateDate;
    private Integer soLuong;
    private Float giaNhap;
    private Float giaBan;
    private String moTa;
    private Integer idSanPham;
    private Integer idMauSac;
    private Integer idThuongHieu;
    private Integer idKieuDang;
    private Integer idChatLieu;
    private Integer idKichCo;
    private Integer idXuatXu;
    private Integer idHinhAnh;
}
