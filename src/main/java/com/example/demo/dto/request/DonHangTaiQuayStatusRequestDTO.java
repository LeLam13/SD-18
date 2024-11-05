package com.example.demo.dto.request;

import lombok.Data;

@Data
public class DonHangTaiQuayStatusRequestDTO {
    private Integer idDonHang;
    private Integer idTrangThai;
    private String ghiChu;
}
