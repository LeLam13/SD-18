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

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        DotGiamGia dotGiamGia = dotGiamGiaService.getDotGiamGiaById(id); // Lấy đợt giảm giá cần cập nhật
        model.addAttribute("dotGiamGia", dotGiamGia);
        return "admin/updatedgg"; // Trang cập nhật
    }
    @PostMapping("/update")
    public String updateDotGiamGia(@RequestParam("idGiamGia") Integer idGiamGia,
                                   @RequestParam("discountType") String discountType,
                                   @RequestParam(value = "giamGiaPercent", required = false) String giamGiaPercentStr,
                                   @RequestParam(value = "giamGiaAmount", required = false) String giamGiaAmountStr,
                                   @RequestParam("thoiGianBatDau") LocalDateTime thoiGianBatDau,
                                   @RequestParam("thoiGianKetThuc") LocalDateTime thoiGianKetThuc,
                                   Model model) {

        DotGiamGia dotGiamGia = dotGiamGiaService.getDotGiamGiaById(idGiamGia);

        // Kiểm tra nếu ngày bắt đầu lớn hơn ngày kết thúc
        if (thoiGianBatDau.isAfter(thoiGianKetThuc)) {
            model.addAttribute("error2", "Ngày bắt đầu không được lớn hơn ngày kết thúc.");
            model.addAttribute("dotGiamGia", dotGiamGia);
            return "admin/updatedgg";
        }

        // Kiểm tra nếu thời gian đã thay đổi
        boolean isThoiGianChanged = !dotGiamGia.getThoiGianBatDau().equals(thoiGianBatDau) || !dotGiamGia.getThoiGianKetThuc().equals(thoiGianKetThuc);

        if (isThoiGianChanged) {
            // Tìm các đợt giảm giá đang hoạt động có thời gian trùng lặp với thời gian bắt đầu và kết thúc mới
            List<DotGiamGia> overlappingPromotions = dotGiamGiaService.findOverlappingPromotionsExceptCurrent(thoiGianBatDau, thoiGianKetThuc,idGiamGia);

            // Kiểm tra nếu có đợt giảm giá trùng lặp với đợt giảm giá hiện tại
            for (DotGiamGia activeDotGiamGia : overlappingPromotions) {
                // Kiểm tra không phải là chính đợt giảm giá đang cập nhật
                if (!activeDotGiamGia.getIdGiamGia().equals(dotGiamGia.getIdGiamGia())) {
                    // Ngày bắt đầu của đợt giảm giá mới phải lớn hơn ngày kết thúc của đợt giảm giá hiện tại
                    model.addAttribute("error2", "Thời gian của đợt giảm giá không được trùng thời gian lên đợt giảm giá khác");
                    model.addAttribute("dotGiamGia", dotGiamGia);
                    return "admin/updatedgg"; // Trả về trang cập nhật
                }
            }
        }
        // Kiểm tra phần trăm giảm giá nếu là giảm giá theo phần trăm
        if ("percent".equals(discountType)) {
            try {
                Double giamGiaPercent = Double.parseDouble(giamGiaPercentStr); // Chuyển đổi từ String sang Double
                if (giamGiaPercent < 0 || giamGiaPercent > 100) {
                    model.addAttribute("error2", "Giảm giá theo phần trăm phải nằm trong khoảng từ 0 đến 100.");
                    model.addAttribute("dotGiamGia", dotGiamGia);
                    return "admin/updatedgg";
                }
                dotGiamGia.setGiamGia(giamGiaPercent);
                dotGiamGia.setLoaiGiamGia(0); // Giảm giá theo %
            } catch (NumberFormatException e) {
                model.addAttribute("error2", "Giảm giá theo phần trăm phải là một số hợp lệ.");
                model.addAttribute("dotGiamGia", dotGiamGia);
                return "admin/updatedgg";
            }
        } else { // Giảm giá theo tiền
            try {
                Double giamGiaAmount = Double.parseDouble(giamGiaAmountStr); // Chuyển đổi từ String sang Double
                if (giamGiaAmount < 0) {
                    model.addAttribute("error2", "Giảm giá theo tiền không được là số âm.");
                    model.addAttribute("dotGiamGia", dotGiamGia);
                    return "admin/updatedgg";
                }
                dotGiamGia.setGiamGia(giamGiaAmount);
                dotGiamGia.setLoaiGiamGia(1); // Giảm giá theo tiền
            } catch (NumberFormatException e) {
                model.addAttribute("error2", "Giảm giá theo tiền phải là một số hợp lệ.");
                model.addAttribute("dotGiamGia", dotGiamGia);
                return "admin/updatedgg";
            }
        }

        dotGiamGia.setThoiGianBatDau(thoiGianBatDau);
        dotGiamGia.setThoiGianKetThuc(thoiGianKetThuc);

        // Cập nhật trạng thái nếu cần
        dotGiamGiaService.updateStatus(dotGiamGia);

        // Gọi phương thức update trong service để cập nhật dữ liệu
        dotGiamGiaService.updateDotGiamGia(dotGiamGia);

        // Chuyển hướng về danh sách sau khi cập nhật
        return "redirect:/admin/dot-giam-gia";
    }


}