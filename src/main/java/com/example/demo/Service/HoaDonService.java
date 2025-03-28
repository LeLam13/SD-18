package com.example.demo.Service;

import com.example.demo.dto.request.HoaDonResquestDTO;
import com.example.demo.entity.HoaDon;

import com.itextpdf.layout.element.Cell;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


public interface HoaDonService {
    HoaDon createHoaDon(HoaDonResquestDTO hoaDon, String username);
    String generateRandomString(int length);

    String  printerInvoice();
    Cell getBillingShippingCell(Object  textValue);
    Cell getCell10fleft(Object textValue, Boolean isBoolean);

    List<HoaDon> getAllHoaDons();
    Page<HoaDon> getAllHoaDons(Pageable pageable);
    List<HoaDon> searchHoaDonsByMaHoaDon(String maHoaDon);


}
