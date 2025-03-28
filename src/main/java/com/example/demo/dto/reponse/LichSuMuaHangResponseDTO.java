package com.example.demo.dto.reponse;

import com.example.demo.entity.HoaDonChiTiet;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class LichSuMuaHangResponseDTO {
    private String maHoaDon; // Mã hóa đơn
    private LocalDate ngayMua; // Ngày mua
    private Float tongTien; // Tổng tiền hóa đơn
    private List<DonHangChiTietResponseDTO> chiTietSanPham; // Danh sách chi tiết sản phẩm
}
