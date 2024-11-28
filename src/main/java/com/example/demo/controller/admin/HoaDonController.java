package com.example.demo.controller.admin;

import com.example.demo.Service.HoaDonService;
import com.example.demo.Service.impl.HoaDonChiTietService;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.repo.HoaDonChiTietRepo;
import com.example.demo.repo.HoaDonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;

@Controller
@RequestMapping("${admin.domain}/hoa-don")
public class HoaDonController {

    @Autowired
    HoaDonRepo hoaDonRepo;
    @Autowired
    HoaDonService hoaDonService;
    @Autowired
    HoaDonChiTietRepo hoaDonChiTietRepo;
    @Autowired
    HoaDonChiTietService hoaDonChiTietService;

    // @GetMapping("")
    // public String getHoaDonList(Model model) {
    // List<HoaDon> hoaDonList = hoaDonRepo.findAll();
    // model.addAttribute("hoaDons", hoaDonList);
    // return "admin/hoaDon";
    // }
    //
    // @GetMapping("/api")
    // @ResponseBody
    // public List<HoaDon> getAllHoaDons() {
    //
    // return hoaDonRepo.findAll();
    //
    // }

    // sao ko return cai template ra
    @GetMapping("")
    public String getHoaDonList(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<HoaDon> hoaDonPage = hoaDonService.getAllHoaDons(pageable);
        for (HoaDon hoaDon : hoaDonPage.getContent()) {
            // System.out.println("abvcdf"+hoaDon.isTrangThaiThanhToan());
        }
        model.addAttribute("hoaDons", hoaDonPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        return "admin/hoaDon";
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

    @GetMapping("/detail/{id}")
    @ResponseBody
    public Map<String, Object> getHoaDonChiTiet(@PathVariable Integer id) {
        return hoaDonChiTietService.findDetailsByHoaDonId(id);
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportInvoice(@RequestParam Integer idHoaDon) {
        try {
            byte[] pdfContent = hoaDonChiTietService.generateInvoicePdf(idHoaDon);

            // Thiết lập header HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "HoaDon_" + idHoaDon + ".pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
