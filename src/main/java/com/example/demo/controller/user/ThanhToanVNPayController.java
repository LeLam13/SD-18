package com.example.demo.controller.user;

import com.example.demo.Service.DonHangOnlineService;
import com.example.demo.dto.request.HoaDonOnlineRequestDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.HoaDon;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class ThanhToanVNPayController {
    @Autowired
    DonHangOnlineService donHangOnlineService;
//    @Autowired
//    HoaDon hoaDon;

    @GetMapping("/response")
    public String handleVNPayResponse(HttpServletRequest request, Model model) throws IOException {
        // Lấy các tham số từ VNPay
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String amount = request.getParameter("vnp_Amount");
        String txnRef = request.getParameter("vnp_TxnRef");
        String bankCode = request.getParameter("vnp_BankCode");

        // Nếu muốn bỏ qua kiểm tra chữ ký, chỉ cần tiếp tục với các tham số
        if ("00".equals(vnp_ResponseCode)) {
            // Thành công, chuyển hướng đến trang success kèm tham số
            long amountInVND = Long.parseLong(amount) / 100;
            System.out.println("check return: "+amountInVND +"/"+txnRef+"/"+bankCode);
            DonHang donHang =donHangOnlineService.findByMaDonHang(txnRef);
            System.out.println("check tạo hoá đon = don hang: "+donHang);
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

            HoaDonOnlineRequestDTO hoaDonOnlineRequestDTO = new HoaDonOnlineRequestDTO();
            hoaDonOnlineRequestDTO.setIdDonHang(donHang.getIdDonHang());
            hoaDonOnlineRequestDTO.setMaHoaDon(donHang.getMaDonHang());
            System.out.println("check tạo hoá đon = hoaDonOnlineRequestDTO: "+hoaDonOnlineRequestDTO);
            //HoaDon hoaDon = donHangOnlineService.createInvoice(hoaDonOnlineRequestDTO,"ss");
            HoaDon hoaDon = donHangOnlineService.updateInvoice(hoaDonOnlineRequestDTO,username);
            System.out.println("test get hoaDon: "+hoaDon);
            model.addAttribute("amount", amountInVND);
            model.addAttribute("orderId", hoaDon.getDonHang().getMaDonHang());
            model.addAttribute("username", username);
            return "/user/authen/thanhToanSuccess";
        } else {
            DonHang donHang = donHangOnlineService.findByMaDonHang(txnRef);
            model.addAttribute("donHang", donHang); // Gửi thông tin đơn hàng về view

            // Thất bại, chuyển hướng đến trang faild
            return "/user/authen/thanhToanFail";
        }

    }

    @GetMapping("/payment-success")
    public String paymentSuccess() {
        return "/user/authen/thanhToanSuccess";  // Đường dẫn tương đối trong thư mục templates
    }

    @GetMapping("/payment-fail")
    public String paymentFail() {
        return "/user/authen/thanhToanFail";  // Đường dẫn tương đối trong thư mục templates
    }
}
