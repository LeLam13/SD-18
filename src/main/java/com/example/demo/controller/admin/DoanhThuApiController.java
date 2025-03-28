package com.example.demo.controller.admin;

import com.example.demo.repo.HoaDonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DoanhThuApiController {
    @Autowired
    private HoaDonRepo hoaDonRepository;

    @GetMapping("/revenue")
    public Map<String, Object> getRevenueInRange(@RequestParam("start") String start, @RequestParam("end") String end) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Chuyển đổi chuỗi start và end sang kiểu LocalDate
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);

            // Lấy dữ liệu doanh thu và ngày trong khoảng thời gian
            List<Float> revenueValues = hoaDonRepository.findRevenueInRange(startDate, endDate);
            List<String> dates = hoaDonRepository.findDatesInRange(startDate, endDate);

            response.put("success", true);
            response.put("dates", dates);
            response.put("revenueValues", revenueValues);
        } catch (DateTimeParseException e) {
            response.put("success", false);
            response.put("message", "Invalid date format: " + e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching revenue data: " + e.getMessage());
        }

        return response;
    }
}
