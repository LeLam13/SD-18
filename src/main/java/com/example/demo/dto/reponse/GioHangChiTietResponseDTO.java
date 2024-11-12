package com.example.demo.dto.reponse;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.SanPhamChiTiet;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class GioHangChiTietResponseDTO {
    private Integer idGioHangChiTiet;
    private String maGioHangChiTiet;
    private Integer soLuong;
    private Float giaBan;
    private SanPhamChiTiet idSanPham;
    private GioHang gioHang;
}
