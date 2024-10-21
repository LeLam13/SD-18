package com.example.demo.Service;

import com.example.demo.dto.request.HoaDonResquestDTO;
import com.example.demo.entity.HoaDon;

public interface HoaDonService {
    HoaDon createHoaDon(HoaDonResquestDTO hoaDon, String username);
    String generateRandomString(int length);
}
