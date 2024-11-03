package com.example.demo.rest;

import com.example.demo.Service.DonHangTaiQuayService;
import com.example.demo.dto.request.DonHangTaiQuayStatusRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.khachhang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            System.out.println("check status order: "+ donHangStatus);
            DonHang donHangTaiQuay = donHangTaiQuayService.updateOrderStatus(donHangStatus);
            return ResponseEntity.ok(donHangTaiQuay);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Trả về lỗi với thông báo
        }
    }
}
