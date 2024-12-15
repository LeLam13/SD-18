package com.example.demo.Service;

import com.example.demo.dto.request.DonHangOnlineRequestDTO;
import com.example.demo.dto.request.GioHAngChiTietRequestDTO;
import com.example.demo.dto.request.GioHangRequestDTO;
import com.example.demo.dto.request.HoaDonOnlineRequestDTO;
import com.example.demo.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DonHangOnlineService {
    List<SanPhamChiTiet> getAllProducts();

    SanPhamChiTiet getProductsByID(Integer id);

    DonHang createOrder(DonHangOnlineRequestDTO donHangOnlineRequestDTO,String username);
    String generateRandomString(int length);


    khachhang getUserLogin(String username);

    GioHang createCart(GioHangRequestDTO gioHangRequestDTO);

    GioHangChiTiet createDetailCart(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO);

    GioHang getCart(String username);

    List<GioHangChiTiet> getDetailCart(Integer idGioHang);

    GioHangChiTiet updateCartDetail(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO);
    GioHangChiTiet  deleteAndReturnBySanPhamChiTietId(Integer idSanPhamChiTiet,Integer idGioHangChiTiet);

    GioHangChiTiet updateCartDetailPlus(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO);

    GioHangChiTiet updateCartDetailReduce(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO);

    GioHangChiTiet updateCartDetailChange(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO);

    HoaDon createInvoice(HoaDonOnlineRequestDTO hoaDonOnlineRequestDTO, String username);

    void deleCartDetailByIdGioHang(Integer id);

    DonHang findByID(Integer idDonHang);

    DonHang findByMaDonHang(String txnRef);

    HoaDon updateInvoice(HoaDonOnlineRequestDTO hoaDonOnlineRequestDTO, String username);
}
