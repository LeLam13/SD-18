package com.example.demo.rest;

import com.example.demo.Service.DonHangCuaKhachService;
import com.example.demo.dto.reponse.DonHangChiTietResponseDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DonHangCuaKhachRestController {
    @Autowired
    DonHangCuaKhachService donHangCuaKhachService;

    @GetMapping("/don-hang-cua-khach/lay-don-hang")
    public ResponseEntity<?> getAllOrderUsername(){
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

        List<DonHang> donHang = donHangCuaKhachService.getAllOrderUsername(username);
        return ResponseEntity.ok(donHang);
    }

    @GetMapping("/don-hang-cua-khach/lay-don-hang/{id}")
    public ResponseEntity<?> getAllProductDonHang(@PathVariable("id") Integer id){
        List<DonHangChiTiet> spct = donHangCuaKhachService.getAllProductsOrder(id);
//        System.out.println("log check responseDTOList: "+spct);
        return ResponseEntity.ok(spct);
    }

}
