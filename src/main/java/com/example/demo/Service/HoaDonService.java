package com.example.demo.Service;

import com.example.demo.dto.request.HoaDonResquestDTO;
import com.example.demo.entity.HoaDon;

import java.util.List;

public interface HoaDonService {
    HoaDon createHoaDon(HoaDonResquestDTO hoaDon, String username);
    String generateRandomString(int length);
    List<HoaDon> getAllHoaDons();


}
