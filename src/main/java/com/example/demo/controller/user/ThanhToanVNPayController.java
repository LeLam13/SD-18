package com.example.demo.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class ThanhToanVNPayController {
    @GetMapping("/vnpay/response")
    public String handleVNPayResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            return "/user/authen/thanhToanSuccess";
        } else {
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
