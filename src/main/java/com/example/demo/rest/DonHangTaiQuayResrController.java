package com.example.demo.rest;

import com.example.demo.Service.DonHangTaiQuayService;
import com.example.demo.dto.request.DonHangTaiQuayStatusRequestDTO;
import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class DonHangTaiQuayResrController {
    @Autowired
    DonHangTaiQuayService donHangTaiQuayService;

    @GetMapping("/don-hang-tai-quay")
    public ResponseEntity<?> getAllOrder(){
        List<DonHang> donHang= donHangTaiQuayService.getAllOrder();
        return ResponseEntity.ok(donHang);
    }

    @GetMapping("/don-hang-tai-quay/{id}")
    public ResponseEntity<?> getOrderID(@PathVariable("id") Integer id){
        try {
            List<DonHangChiTiet> donHangCT = donHangTaiQuayService.getOrderDetailById(id);
            return ResponseEntity.ok(donHangCT);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Trả về lỗi với thông báo
        }
    }

    @PutMapping("/don-hang-tai-quay/cap-nhat-trang-thai")
    public ResponseEntity<?> updateStatusOrder(@RequestBody DonHangTaiQuayStatusRequestDTO donHangStatus){
        try {
            String username =null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    //return ((UserDetails) principal).getUsername();
                    System.out.println("test get user1: "+((UserDetails) principal).getUsername());
                    username = ((UserDetails) principal).getUsername();
                } else {
                    System.out.println("test get user2: "+principal.toString());
                    //return principal.toString();
                }
            }
            System.out.println("check status order: "+ donHangStatus);
            DonHang donHangTaiQuay = donHangTaiQuayService.updateOrderStatus(donHangStatus,username);
            return ResponseEntity.ok(donHangTaiQuay);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Trả về lỗi với thông báo
        }
    }
    @PutMapping("/don-hang-tai-quay/huy-don-hang")
    public ResponseEntity<?> cancelStatusOrder(@RequestBody DonHangTaiQuayStatusRequestDTO donHangStatus){
        try {
            String username =null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    //return ((UserDetails) principal).getUsername();
                    System.out.println("test get user1: "+((UserDetails) principal).getUsername());
                    username = ((UserDetails) principal).getUsername();
                } else {
                    System.out.println("test get user2: "+principal.toString());
                    //return principal.toString();
                }
            }
            System.out.println("check status order: "+ donHangStatus);
            DonHang donHangTaiQuay = donHangTaiQuayService.cancelOrderStatus(donHangStatus,username);
            return ResponseEntity.ok(donHangTaiQuay);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Trả về lỗi với thông báo
        }
    }
    @GetMapping("/don-hang/tim-kiem-ma-don-hang")
    public  ResponseEntity<?> searchMaDonHang(@RequestParam("maHD") String maHD){
        List<DonHang> donHangList = donHangTaiQuayService.searchMaDonhang(maHD);
        return ResponseEntity.ok(donHangList);
    }
    @GetMapping("/don-hang/tim-kiem-loai-don-hang")
    public  ResponseEntity<?> searchLoaiDonHang(@RequestParam("loaiDonHang") Integer loaiDonHang){
        List<DonHang> donHangList = donHangTaiQuayService.searchLoaiDonhang(loaiDonHang);
        return ResponseEntity.ok(donHangList);
    }

    @GetMapping("/don-hang/tim-kiem-trang-thai-don-hang")
    public  ResponseEntity<?> searchTrangThaiDonHang(@RequestParam("idTrangThai") Integer idTrangThai){
        List<DonHang> donHangList = donHangTaiQuayService.searchTrangThaiDonhang(idTrangThai);
        return ResponseEntity.ok(donHangList);
    }

    @GetMapping("/don-hang/tim-kiem-theo-ngay")
    public  ResponseEntity<?> searchNgayTao(@RequestParam(required = false) String ngayBatDau,
                                            @RequestParam(required = false) String ngayKetThuc){
        // Định dạng ngày "dd/MM/yyyy" để chuyển đổi chuỗi từ request
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Chuyển đổi chuỗi thành LocalDate
        LocalDate startDate = (ngayBatDau != null && !ngayBatDau.isEmpty())
                ? LocalDate.parse(ngayBatDau, formatter) : null;
        LocalDate endDate = (ngayKetThuc != null && !ngayKetThuc.isEmpty())
                ? LocalDate.parse(ngayKetThuc, formatter) : null;

       List<DonHang> donHangList = donHangTaiQuayService.searchNgayTao(startDate,endDate);
        return ResponseEntity.ok(donHangList);
    }
    @GetMapping("/don-hang/lay-trang-thai")
    public  ResponseEntity<?> getAllStatus(){
        List<TrangThai> trangThaiList = donHangTaiQuayService.getAllStatus();
        return ResponseEntity.ok(trangThaiList);
    }

    @GetMapping("/don-hang/get-invoice/{id}")
    public ResponseEntity<?> getInvoice(@PathVariable("id") Integer id) {
        System.out.println("checkID: " + id);
        try {
            HoaDon hoaDon = donHangTaiQuayService.getInvoice(id);
            System.out.println("hoaDon: " + hoaDon);
            return ResponseEntity.ok(hoaDon);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
        // return ResponseEntity.ok("");
    }

    @GetMapping("/don-hang/invoice/{id}")
    public ResponseEntity<?> printerInvoice(@PathVariable("id") Integer id) {
        System.out.println("checkID: " + id);
        try {
            String path = donHangTaiQuayService.printerInvoice(id);
            System.out.println("path" + path);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
        // return ResponseEntity.ok("");
    }
}
