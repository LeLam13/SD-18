package com.example.demo.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonHangResponseDTO {
    private Integer idDonHang;
    private String maDonHang;
    private Float tongTien;
    private Float tongTienKhuyenMai;
    private Float tongTienSauKhuyenMai;
    private String ghiChu;
    private Boolean trangThaiThanhToan;
    private String nhanVien;
    private String khachHang;

}
