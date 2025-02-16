package com.example.demo.controller.user;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LienHeController {
    @GetMapping("lien-he")
    public String trangChu() {
        return "/user/lienhe";
    }

}
