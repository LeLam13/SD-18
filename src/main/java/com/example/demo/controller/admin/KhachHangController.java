package com.example.demo.controller.admin;

import com.example.demo.Service.KhachHangService;
import com.example.demo.dto.reponse.CustomException;
import com.example.demo.dto.reponse.ErrorResponseDTO;
import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.dto.reponse.LichSuMuaHangResponseDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> addKhachHang(
            @RequestBody @Valid KhachHangRequestDTO khachHangRequestDTO) {
        try {
            // Trả về KhachHangResponseDTO khi thành công
            KhachHangResponseDTO savedKhachHang = khachHangService.addKhachHang(khachHangRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKhachHang);
        } catch (CustomException ex) {
            // Trả về ErrorResponseDTO khi có lỗi
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), ex.getErrorCode());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // Trả về lỗi 500 cho các trường hợp lỗi không xác định
            ErrorResponseDTO errorResponse = new ErrorResponseDTO("Có lỗi hệ thống. Vui lòng thử lại sau.",
                    "internal_server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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

            // Validate and update customer fields
            if (khachHangRequestDTO.getHoTen() != null && !khachHangRequestDTO.getHoTen().trim().isEmpty()) {
                existingKhachHang.setHoTen(khachHangRequestDTO.getHoTen());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Họ tên không được để trống.");
            }

            if (khachHangRequestDTO.getSoDienThoai() != null
                    && !khachHangRequestDTO.getSoDienThoai().trim().isEmpty()) {
                existingKhachHang.setSoDienThoai(khachHangRequestDTO.getSoDienThoai());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số điện thoại không được để trống.");
            }

            if (khachHangRequestDTO.getDiaChi() != null && !khachHangRequestDTO.getDiaChi().trim().isEmpty()) {
                existingKhachHang.setDiaChi(khachHangRequestDTO.getDiaChi());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Địa chỉ không được để trống.");
            }

            if (khachHangRequestDTO.getEmail() != null && !khachHangRequestDTO.getEmail().trim().isEmpty()) {
                existingKhachHang.setEmail(khachHangRequestDTO.getEmail());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email không được để trống.");
            }

            // Validate and update gender
            Boolean gioiTinh = khachHangRequestDTO.getGioiTinh();
            if (gioiTinh == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Giới tính không được để trống.");
            }
            existingKhachHang.setGioiTinh(gioiTinh);

            // Validate and update date of birth
            try {
                if (khachHangRequestDTO.getNgaySinh() != null) {
                    LocalDate ngaySinh = LocalDate.parse(khachHangRequestDTO.getNgaySinh());
                    existingKhachHang.setNgaySinh(ngaySinh);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ngày sinh không được để trống.");
                }
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
            responseDTO.setEmail(updatedKhachHang.getEmail());

            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/api/page")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllKhachHangPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String searchQuery) { // Tham số tìm kiếm

        Pageable pageable = PageRequest.of(page, size);

        // Gọi service với tham số tìm kiếm
        Page<KhachHangResponseDTO> pagedKhachHangList = khachHangService.getAllKhachHangPaged(searchQuery, pageable);

        // Chuẩn bị dữ liệu trả về
        Map<String, Object> response = new HashMap<>();
        response.put("data", pagedKhachHangList.getContent()); // Dữ liệu của trang hiện tại
        response.put("currentPage", pagedKhachHangList.getNumber()); // Trang hiện tại
        response.put("totalItems", pagedKhachHangList.getTotalElements()); // Tổng số khách hàng
        response.put("totalPages", pagedKhachHangList.getTotalPages()); // Tổng số trang

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/purchase-history/{idKhachHang}")
    @ResponseBody
    public ResponseEntity<?> getPurchaseHistory(@PathVariable("idKhachHang") Integer idKhachHang) {
        try {
            // Lấy dữ liệu lịch sử mua hàng từ service
            List<LichSuMuaHangResponseDTO> lichSuMuaHangList = khachHangService.getLichSuMuaHang(idKhachHang);
            return ResponseEntity.ok(lichSuMuaHangList);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Khách hàng không tồn tại hoặc không có lịch sử mua hàng.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi xử lý yêu cầu.");
        }
    }

}
