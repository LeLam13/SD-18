package com.example.demo.Service;

import com.example.demo.dto.request.DonHangOnlineStatusRequestDTO;
import com.example.demo.dto.request.HoaDonOnlineRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.HoaDon;
import com.itextpdf.layout.element.Cell;

import java.util.List;

public interface QuanLyDonHangOnlineService {
    List<DonHang> getAllOrderByOrderType();

    List<DonHangChiTiet>  getOrderByIdOrderType(Integer id);

    DonHang updateStatusOrder(DonHangOnlineStatusRequestDTO donHangOnlineStatusRequestDTO,String username);

    HoaDon createInvoice(HoaDonOnlineRequestDTO hoaDonOnlineRequestDTO, String username);
    String generateRandomString(int length);

    DonHang cancelStatusOrder(DonHangOnlineStatusRequestDTO donHangOnlineStatusRequestDTO, String username);

    DonHang getStuats(Integer id);

    String printerInvoiceOnlice(Integer id);
    Cell getBillingShippingCell(Object  textValue);
    Cell getCell10fleft(Object textValue, Boolean isBoolean);
}
