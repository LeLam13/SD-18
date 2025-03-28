package com.example.demo.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHangChiTietRequestDTO {
    private String maDonHangChiTiet;
    private Integer soLuong;
    private Float giaBan;
    private Integer idĐonHang;
    private Integer idSanPhamChiTiet;
}
