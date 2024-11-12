package com.example.demo.dto.reponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHangChiTietResponseDTO {
    private Integer idDonHangChiTiet;
    private String maDonHangChiTiet;
    private Integer soLuong;
    private Float giaBan;
    private String  tenSanPham;
    private Integer idSanPham;
}
