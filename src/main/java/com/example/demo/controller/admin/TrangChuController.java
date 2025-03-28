package com.example.demo.controller.admin;

import com.example.demo.repo.HoaDonRepo;
import com.example.demo.repo.SanPhamRepo;
import com.example.demo.repo.khachhangRePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequestMapping("${admin.domain}/trang-chu")
public class TrangChuController {

    @Autowired
    private HoaDonRepo hoaDonRepository;

    @Autowired
    private SanPhamRepo sanPhamRepository;

    @Autowired
    private khachhangRePo khachHangRepository;

    @GetMapping("")
    public String show(Model model) {
        // Tổng số sản phẩm, đơn hàng, khách hàng
        Long totalProducts = sanPhamRepository.count();
        Long totalInvoices = hoaDonRepository.count();
        Long totalCustomers = khachHangRepository.count();

        // Doanh thu hiện tại và doanh thu so sánh
        Float totalRevenue = hoaDonRepository.calculateTotalRevenue();
        Float revenueToday = hoaDonRepository.calculateRevenueToday();
        Float revenueYesterday = hoaDonRepository.calculateRevenueYesterday();
        Float revenueThisMonth = hoaDonRepository.calculateRevenueThisMonth();
        Float revenueThisYear = hoaDonRepository.calculateRevenueThisYear();
        Float revenueLastMonth = hoaDonRepository.calculateRevenueLastMonth();
        Float revenueLastYear = hoaDonRepository.calculateRevenueLastYear();

        // Tỷ lệ tăng trưởng với làm tròn 2 chữ số thập phân
        Float dailyGrowthRate = hoaDonRepository.calculateDailyGrowthRate();
        Float monthlyGrowthRate = (revenueThisMonth != null && revenueLastMonth != null && revenueLastMonth > 0)
                ? ((revenueThisMonth - revenueLastMonth) / revenueLastMonth) * 100
                : 0.0f;
        Float yearlyGrowthRate = (revenueThisYear != null && revenueLastYear != null && revenueLastYear > 0)
                ? ((revenueThisYear - revenueLastYear) / revenueLastYear) * 100
                : 0.0f;

        // Làm tròn tỷ lệ tăng trưởng đến 2 chữ số thập phân
        dailyGrowthRate = roundToTwoDecimalPlaces(dailyGrowthRate);
        monthlyGrowthRate = roundToTwoDecimalPlaces(monthlyGrowthRate);
        yearlyGrowthRate = roundToTwoDecimalPlaces(yearlyGrowthRate);

        // Lấy top 5 sản phẩm bán chạy
        List<Object[]> topSellingProducts = hoaDonRepository.findTopSellingProducts();

        // Đưa dữ liệu vào model
        model.addAttribute("totalProducts", totalProducts != null ? totalProducts : 0L);
        model.addAttribute("totalInvoices", totalInvoices != null ? totalInvoices : 0L);
        model.addAttribute("totalCustomers", totalCustomers != null ? totalCustomers : 0L);
        model.addAttribute("totalRevenue", totalRevenue != null ? totalRevenue : 0.0f);
        model.addAttribute("revenueToday", revenueToday != null ? revenueToday : 0.0f);
        model.addAttribute("revenueYesterday", revenueYesterday != null ? revenueYesterday : 0.0f);
        model.addAttribute("revenueThisMonth", revenueThisMonth != null ? revenueThisMonth : 0.0f);
        model.addAttribute("revenueLastMonth", revenueLastMonth != null ? revenueLastMonth : 0.0f);
        model.addAttribute("revenueThisYear", revenueThisYear != null ? revenueThisYear : 0.0f);
        model.addAttribute("revenueLastYear", revenueLastYear != null ? revenueLastYear : 0.0f);
        model.addAttribute("dailyGrowthRate", dailyGrowthRate);
        model.addAttribute("monthlyGrowthRate", monthlyGrowthRate);
        model.addAttribute("yearlyGrowthRate", yearlyGrowthRate);
        model.addAttribute("topSellingProducts", topSellingProducts);

        return "admin/thongke/tongQuat";
    }

    // Hàm làm tròn đến 2 chữ số thập phân
    private Float roundToTwoDecimalPlaces(Float value) {
        if (value == null)
            return 0.0f;
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
