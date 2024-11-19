package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequestDTO {
    private String ten;
    private Float giaMin;
    private Float giaMax;
    private Integer idSanPham;
    private Integer idXuatXu;
    private Integer idMauSac;
    private Integer idThuongHieu;
    private Integer idKieuDang;
    private Integer idChatLieu;
}
