package com.example.demo.dto.request;

import lombok.Data;

@Data
public class SanPhamWithImageDto {
    private Long idSanPham;
    private String tenSanPham;
    private String tenHinhAnh;
}
