package com.example.demo.rest;

import com.example.demo.Service.QuanLyDonHangOnlineService;
import com.example.demo.dto.request.DonHangOnlineRequestDTO;
import com.example.demo.dto.request.DonHangOnlineStatusRequestDTO;
import com.example.demo.dto.request.HoaDonOnlineRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.HoaDon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DonHangOnlineRestController {
    @Autowired
    QuanLyDonHangOnlineService quanLyDonHangOnlineService;

    @GetMapping("/don-hang-online")
    public ResponseEntity<?> getAllOrderByOrderType(){
        List<DonHang> listDonHang = quanLyDonHangOnlineService.getAllOrderByOrderType();
        return ResponseEntity.ok(listDonHang);
    }

    @GetMapping("/don-hang-online/{id}")
    public ResponseEntity<?> getOrderByIdOrderType(@PathVariable("id") Integer id){
        List<DonHangChiTiet> donHangChiTiet = quanLyDonHangOnlineService.getOrderByIdOrderType(id);
        return ResponseEntity.ok(donHangChiTiet);
    }

    @PutMapping("/don-hang-online/cap-nhat-trang-thai")
    public ResponseEntity<?> updateStatusOrder(@RequestBody DonHangOnlineStatusRequestDTO donHangOnlineStatusRequestDTO){
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
        try {
            System.out.println("check status: "+donHangOnlineStatusRequestDTO);
            DonHang donHang = quanLyDonHangOnlineService.updateStatusOrder(donHangOnlineStatusRequestDTO,username);
            return ResponseEntity.ok(donHang);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Trả về lỗi với thông báo
        }
    }

    @PutMapping("/don-hang-online/huy-don")
    public ResponseEntity<?> cancelOrder(@RequestBody DonHangOnlineStatusRequestDTO donHangOnlineStatusRequestDTO){
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
        try {
            System.out.println("check status calcel: "+donHangOnlineStatusRequestDTO);
            DonHang donHang = quanLyDonHangOnlineService.cancelStatusOrder(donHangOnlineStatusRequestDTO,username);
            return ResponseEntity.ok(donHang);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Trả về lỗi với thông báo
        }
    }

    @PostMapping("/don-hang-online/tao-hoa-don")
    public ResponseEntity<?> createInvoice(@RequestBody HoaDonOnlineRequestDTO hoaDonOnlineRequestDTO){
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

        try {
            System.out.println("hoaDonOnlineRequestDTO: "+hoaDonOnlineRequestDTO);
            HoaDon hoaDon = quanLyDonHangOnlineService.createInvoice(hoaDonOnlineRequestDTO,username);
            return ResponseEntity.ok(hoaDon);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Trả về lỗi với thông báo
        }

    }

    @GetMapping("/api/getTrangThai/{id}")
    public ResponseEntity<?> getStatus(@PathVariable("id") Integer id){
        DonHang donHang = quanLyDonHangOnlineService.getStuats(id);
        return ResponseEntity.ok(donHang);
    }

}
