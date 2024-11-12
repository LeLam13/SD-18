package com.example.demo.controller.admin;

import com.example.demo.Service.HoaDonService;
import com.example.demo.Service.impl.HoaDonServiceImpl;

import com.example.demo.entity.HoaDon;
import com.example.demo.repo.HoaDonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Controller
@RequestMapping("${admin.domain}/hoa-don")
public class HoaDonController {

    @Autowired
    HoaDonRepo hoaDonRepo;
    @Autowired
    HoaDonService hoaDonService;

//    @GetMapping("")
//    public String getHoaDonList(Model model) {
//        List<HoaDon> hoaDonList = hoaDonRepo.findAll();
//        model.addAttribute("hoaDons", hoaDonList);
//        return "admin/hoaDon";
//    }
//
//    @GetMapping("/api")
//    @ResponseBody
//    public List<HoaDon> getAllHoaDons() {
//
//        return hoaDonRepo.findAll();
//
//    }

    // sao ko return cai template ra
    @GetMapping("")
    public String getHoaDonList(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<HoaDon> hoaDonPage = hoaDonService.getAllHoaDons(pageable);
        model.addAttribute("hoaDons", hoaDonPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        return "admin/hoaDon"; // Trả về template
    }

    @GetMapping("/api")
    @ResponseBody
    public Page<HoaDon> getAllHoaDons(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return hoaDonService.getAllHoaDons(pageable);
    }
    @GetMapping("/search")
    @ResponseBody
    public List<HoaDon> searchHoaDons(@RequestParam String maHoaDon) {
        return hoaDonService.searchHoaDonsByMaHoaDon(maHoaDon);
    }

}
