package com.example.demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.List;

@Controller
@RequestMapping("${admin.domain}/mau-sac")
public class MauSacController {
    @GetMapping("")
    public String show() {
        return "/admin/mauSac";
    }
}

