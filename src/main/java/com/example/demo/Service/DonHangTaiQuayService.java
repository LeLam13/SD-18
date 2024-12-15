package com.example.demo.Service;

import com.example.demo.dto.request.DonHangTaiQuayStatusRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.TrangThai;
import com.itextpdf.layout.element.Cell;

import java.time.LocalDate;
import java.util.List;

public interface DonHangTaiQuayService {
    List<DonHang> getAllOrder();

    List<DonHangChiTiet> getOrderDetailById(Integer id);

    DonHang updateOrderStatus(DonHangTaiQuayStatusRequestDTO donHangStatus, String username);

    List<DonHang> searchMaDonhang(String maHD);

    List<DonHang> searchLoaiDonhang(Integer loaiDonHang);

    List<DonHang> searchNgayTao(LocalDate startDate, LocalDate endDate);

    List<TrangThai> getAllStatus();

    DonHang cancelOrderStatus(DonHangTaiQuayStatusRequestDTO donHangStatus,String username);

    List<DonHang> searchTrangThaiDonhang(Integer idTrangThai);

    HoaDon getInvoice(Integer id);

    String printerInvoice(Integer id);

    Cell getBillingShippingCell(Object  textValue);
    Cell getCell10fleft(Object textValue, Boolean isBoolean);
}
