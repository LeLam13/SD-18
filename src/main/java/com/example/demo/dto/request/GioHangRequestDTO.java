package com.example.demo.dto.request;

import com.example.demo.entity.khachhang;
import lombok.Data;

@Data
public class GioHangRequestDTO {
    private String maGioHang;
    private khachhang userName;
}
