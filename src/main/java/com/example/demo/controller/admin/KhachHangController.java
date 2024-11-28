package com.example.demo.controller.admin;

import com.example.demo.Service.KhachHangService;
import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.dto.request.KhachHangRequestDTO;
import com.example.demo.entity.khachhang;
import com.example.demo.repo.khachhangRePo;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("${admin.domain}/khach-hang")
public class KhachHangController {

    @Autowired
    private khachhangRePo khachHangRepo;

    @Autowired
    private KhachHangService khachHangService;

    // Display customer view page
    @GetMapping("")
    public String getKhachHangView(Model model) {
        List<khachhang> khachhangList = khachHangRepo.findAll();
        model.addAttribute("khachhangList", khachhangList);
        return "admin/KhachHang";
    }

    // Fetch all customers for API
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<KhachHangResponseDTO>> getAllKhachHangApi() {
        try {
            List<KhachHangResponseDTO> khachHangList = khachHangService.getAllKhachHang();
            return ResponseEntity.ok(khachHangList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<KhachHangResponseDTO> addKhachHang(
            @RequestBody @Valid KhachHangRequestDTO khachHangRequestDTO) {
        try {
            KhachHangResponseDTO savedKhachHang = khachHangService.addKhachHang(khachHangRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKhachHang);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/api/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateKhachHang(
            @PathVariable("id") Long id,
            @RequestBody KhachHangRequestDTO khachHangRequestDTO) {
        try {
            // Fetch existing customer
            khachhang existingKhachHang = khachHangRepo.findById(id.intValue())
                    .orElseThrow(() -> new RuntimeException("Customer with ID " + id + " not found"));

            // Update fields
            existingKhachHang.setHoTen(khachHangRequestDTO.getHoTen());
            existingKhachHang.setSoDienThoai(khachHangRequestDTO.getSoDienThoai());
            existingKhachHang.setDiaChi(khachHangRequestDTO.getDiaChi());
            existingKhachHang.setEmail(khachHangRequestDTO.getEmail()); // Update email

            // Validate and update gender
            Boolean gioiTinh = khachHangRequestDTO.getGioiTinh();
            if (gioiTinh == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Giới tính không được để trống.");
            }
            existingKhachHang.setGioiTinh(gioiTinh);

            // Validate and update date of birth
            try {
                LocalDate ngaySinh = LocalDate.parse(khachHangRequestDTO.getNgaySinh());
                existingKhachHang.setNgaySinh(ngaySinh);
            } catch (DateTimeParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Ngày sinh không hợp lệ. Định dạng đúng là yyyy-MM-dd.");
            }

            // Save updated customer
            khachhang updatedKhachHang = khachHangRepo.save(existingKhachHang);

            // Convert to response DTO
            KhachHangResponseDTO responseDTO = new KhachHangResponseDTO();
            responseDTO.setId_khach_hang(updatedKhachHang.getIdKhachHang());
            responseDTO.setMaKhachHang(updatedKhachHang.getMaKhachHang());
            responseDTO.setHoTen(updatedKhachHang.getHoTen());
            responseDTO.setSoDienThoai(updatedKhachHang.getSoDienThoai());
            responseDTO.setDiaChi(updatedKhachHang.getDiaChi());
            responseDTO.setGioiTinh(updatedKhachHang.isGioiTinh());
            responseDTO.setNgaySinh(updatedKhachHang.getNgaySinh().toString());
            responseDTO.setEmail(updatedKhachHang.getEmail()); // Add email to response DTO

            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // Optional: Pagination for fetching customers
    @GetMapping("/api/page")
    @ResponseBody
    public ResponseEntity<Page<KhachHangResponseDTO>> getAllKhachHangPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHangResponseDTO> pagedKhachHangList = khachHangService.getAllKhachHangPaged(pageable);
        return ResponseEntity.ok(pagedKhachHangList);
    }
}
