package com.example.demo.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHangTongSoLuongResponseDTO {
    private Integer idDonHang;
    private Long tongSoLuong;
}
