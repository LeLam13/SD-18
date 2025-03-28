package com.example.demo.dto.request;

import com.example.demo.entity.SanPhamChiTiet;
import lombok.Data;

@Data
public class GioHAngChiTietRequestDTO {
    private Integer idGioHang;
    private String maGioHangChiTiet;
    private Integer idSanPhamChiTiet;
    private Integer soLuong;
    private Float giaBan;
}
