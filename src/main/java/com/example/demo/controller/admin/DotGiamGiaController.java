package com.example.demo.controller.admin;


import com.example.demo.Service.impl.DotGiamGiaServiceImpl;
import com.example.demo.dto.request.DotGiamGiaDTO;
import com.example.demo.entity.DotGiamGia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/dot-giam-gia")
public class DotGiamGiaController {

    @Autowired
    private DotGiamGiaServiceImpl dotGiamGiaService;


    @PostMapping("/create")
    public String createDotGiamGia(@RequestParam("discountType") String discountType,
                                   @RequestParam(value = "giamGiaPercent", required = false) Double giamGiaPercent,
                                   @RequestParam(value = "giamGiaAmount", required = false) Double giamGiaAmount,
                                   @RequestParam("thoiGianBatDau") LocalDateTime thoiGianBatDau,
                                   @RequestParam("thoiGianKetThuc") LocalDateTime thoiGianKetThuc,
                                   Model model) {

        // Kiểm tra ngày bắt đầu không được lớn hơn ngày kết thúc
        if (thoiGianBatDau.isAfter(thoiGianKetThuc)) {
            model.addAttribute("error", "Ngày bắt đầu không được lớn hơn ngày kết thúc.");
            return listDotGiamGia(model);
        }

        // Lấy đợt giảm giá gần nhất có trạng thái "sắp diễn ra" hoặc "đang diễn ra"
        DotGiamGia lastActiveDotGiamGia = dotGiamGiaService.getLastActiveDotGiamGia();
        if (lastActiveDotGiamGia != null && thoiGianBatDau.isBefore(lastActiveDotGiamGia.getThoiGianKetThuc())) {
            model.addAttribute("error", "Ngày bắt đầu của đợt giảm giá mới phải lớn hơn ngày kết thúc của đợt giảm giá hiện tại.");
            return listDotGiamGia(model);
        }

        DotGiamGia dotGiamGia = new DotGiamGia();

        if ("percent".equals(discountType)) {
            dotGiamGia.setGiamGia(giamGiaPercent); // Giảm giá theo phần trăm
            dotGiamGia.setLoaiGiamGia(0); // 0 cho giảm giá theo %
        } else {
            dotGiamGia.setGiamGia(giamGiaAmount); // Giảm giá theo tiền
            dotGiamGia.setLoaiGiamGia(1); // 1 cho giảm giá theo tiền
        }

        dotGiamGia.setThoiGianBatDau(thoiGianBatDau);
        dotGiamGia.setThoiGianKetThuc(thoiGianKetThuc);

        // Gọi service để lưu vào cơ sở dữ liệu
        dotGiamGiaService.createDotGiamGia(dotGiamGia);

        return "redirect:/admin/dot-giam-gia"; // Chuyển hướng sau khi thêm
    }




    @GetMapping("")
    public String listDotGiamGia(Model model) {
        List<DotGiamGia> dotGiamGias = dotGiamGiaService.getAllDotGiamGia();

        List<DotGiamGiaDTO> dotGiamGiaDTOs = dotGiamGias.stream()
                .map(dotGiamGia -> {
                    DotGiamGiaDTO dto = new DotGiamGiaDTO();
                    dto.setIdGiamGia(dotGiamGia.getIdGiamGia());
                    dto.setGiamGia(dotGiamGia.getGiamGia());
                    dto.setThoiGianBatDau(dotGiamGia.getThoiGianBatDau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    dto.setThoiGianKetThuc(dotGiamGia.getThoiGianKetThuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    dto.setTrangThai(dotGiamGia.getTrangThai());
                    dto.setLoaiGiamGia(dotGiamGia.getLoaiGiamGia());
                    return dto;
                })
                .collect(Collectors.toList());

        model.addAttribute("dotGiamGias", dotGiamGiaDTOs);
        return "admin/create_dot_giam_gia";
    }

    @GetMapping("/detail/{id}")
    public String viewDotGiamGiaDetail(@PathVariable("id") Integer id, Model model) {
        DotGiamGia dotGiamGia = dotGiamGiaService.getDotGiamGiaById(id);
        if (dotGiamGia == null) {
            model.addAttribute("error", "Đợt giảm giá không tồn tại.");
            return "redirect:/admin/dot-giam-gia";
        }
        DotGiamGiaDTO dto = new DotGiamGiaDTO();
        dto.setGiamGia(dotGiamGia.getGiamGia());
        dto.setIdGiamGia(dotGiamGia.getIdGiamGia());
        dto.setThoiGianBatDau(dotGiamGia.getThoiGianBatDau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dto.setThoiGianKetThuc(dotGiamGia.getThoiGianKetThuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dto.setTrangThai(dotGiamGia.getTrangThai());
        dto.setLoaiGiamGia(dotGiamGia.getLoaiGiamGia());

        model.addAttribute("dotGiamGia", dto);
        return "admin/detail_dot_giam_gia"; // Trang hiển thị chi tiết đợt giảm giá
    }



}