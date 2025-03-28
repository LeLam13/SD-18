package com.example.demo.rest;

import com.example.demo.Service.DonHangService;
import com.example.demo.Service.HoaDonService;
import com.example.demo.dto.reponse.DonHangChiTietResponseDTO;
import com.example.demo.dto.reponse.DonHangResponseDTO;
import com.example.demo.dto.reponse.DonHangTongSoLuongResponseDTO;
import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.dto.request.DonHangChiTietRequestDTO;
import com.example.demo.dto.request.DonHangRequestDTO;
import com.example.demo.dto.request.HoaDonResquestDTO;
import com.example.demo.dto.request.KhachHangRequestDTO;
import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DonHangRestController {
    @Autowired
    DonHangService donHangService;
    @Autowired
    HoaDonService hoaDonService;

    //sản phẩm chi tiết
    @GetMapping("/don-hang/san-pham-chi-tiet")
    public ResponseEntity<?> getAllProduct(){
        List<SanPhamChiTiet> spct = donHangService.getAllProducts();
        System.out.println("log check: "+spct);
        return ResponseEntity.ok(spct);
    }

    @GetMapping("/don-hang/san-pham-chi-tiet/tim-kiem")
    public ResponseEntity<?> searchSanPhamChiTietDH(@RequestParam("tenSanPham") String ten){
        List<SanPhamChiTiet> ListSpct = donHangService.searchSanPhamChiTiet(ten);
        return ResponseEntity.ok(ListSpct);
    }

    //don hàng chi tiết
    @GetMapping("/don-hang/don-hang-chi-tiet/{id}")
    public ResponseEntity<?> getAllProductDonHang(@PathVariable("id") Integer id){
        List<DonHangChiTiet> spct = donHangService.getAllProductsOrder(id);
                List<DonHangChiTietResponseDTO> responseDTOList = new ArrayList<>();
        for (DonHangChiTiet donHangChiTiet : spct) {
            DonHangChiTietResponseDTO responseDTO = new DonHangChiTietResponseDTO();
            responseDTO.setIdDonHangChiTiet(donHangChiTiet.getIdDonHangChiTiet());
            responseDTO.setMaDonHangChiTiet(donHangChiTiet.getMaDonHangChiTiet());
            responseDTO.setSoLuong(donHangChiTiet.getSoLuong());
//            responseDTO.setDonGia(donHangChiTiet.getDonGia());
            responseDTO.setGiaBan(donHangChiTiet.getDonGia());
            responseDTO.setIdSanPham(donHangChiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet());
//            responseDTO.setTenSanPham(donHangChiTiet.getSanPhamChiTiet().getTen());

            responseDTOList.add(responseDTO);
        }
        System.out.println("log check responseDTOList: "+responseDTOList);
        return ResponseEntity.ok(responseDTOList);
    }

    @PostMapping("/don-hang/don-hang-chi-tiet/them-moi")
    public ResponseEntity<?> addDonHangChiTiet(@RequestBody DonHangChiTietRequestDTO donHangCTDTO){
        DonHangChiTiet dhct = donHangService.createDonHangChitiet(donHangCTDTO);

        DonHangChiTietResponseDTO responseDTO1 = new DonHangChiTietResponseDTO();
        responseDTO1.setIdDonHangChiTiet(dhct.getIdDonHangChiTiet());
        responseDTO1.setMaDonHangChiTiet(dhct.getMaDonHangChiTiet());
        responseDTO1.setSoLuong(dhct.getSoLuong());
//        responseDTO1.setDonGia(dhct.getDonGia());
        responseDTO1.setGiaBan(dhct.getDonGia());

        responseDTO1.setTenSanPham(dhct.getSanPhamChiTiet().getIdSanPham().getTen());
//        responseDTO1.setTenSanPham(dhct.getSanPhamChiTiet().getTen());
        responseDTO1.setIdSanPham(dhct.getSanPhamChiTiet().getIdSanPhamChiTiet());

        return ResponseEntity.ok(responseDTO1);
    }

    @PutMapping("/don-hang/don-hang-chi-tiet/cap-nhat")
    public ResponseEntity<?> updateDonHangChiTiet(@RequestBody DonHangChiTietRequestDTO donHangCTDTO){
//        DonHang donHang = donHangService.createDonHAng(donHangDTO);
        System.out.println("log check DonHangChiTietRequestDTO: "+donHangCTDTO);
        DonHangChiTiet donHangChiTiet = donHangService.updateDonHangChitiet(donHangCTDTO);

        DonHangChiTietResponseDTO responseDTO = new DonHangChiTietResponseDTO();
        responseDTO.setIdDonHangChiTiet(donHangChiTiet.getIdDonHangChiTiet());
        responseDTO.setMaDonHangChiTiet(donHangChiTiet.getMaDonHangChiTiet());
        responseDTO.setSoLuong(donHangChiTiet.getSoLuong());
//        responseDTO.setDonGia(donHangChiTiet.getDonGia());
        responseDTO.setGiaBan(donHangChiTiet.getDonGia());
//        responseDTO.setTenSanPham(donHangChiTiet.getSanPhamChiTiet().getTen());
        responseDTO.setIdSanPham(donHangChiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet());
        System.out.println("donHangChiTiet: "+donHangChiTiet);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/don-hang/don-hang-chi-tiet/xoa/{id}")
    public ResponseEntity<?> deleteDonHangchiTiet(@PathVariable("id") Integer id){
        boolean isDelete = donHangService.deleDonHangChiTiet(id);
        if(isDelete){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    //đơn hàng
    @GetMapping("/don-hang/get-don-hang")//lấy tất cả đơn hàng
    public ResponseEntity<?> getDonHang(){
        List<DonHangTongSoLuongResponseDTO> donHang = donHangService.getTongSoLuongDonHang();
        System.out.println("log check: "+donHang);
        return ResponseEntity.ok(donHang);
    }

    //lấy chi tiết đơn hàng theo id
    @GetMapping("/don-hang/get-don-hang/{id}")
    public ResponseEntity<?> getDonHang(@PathVariable("id") Integer id){
        DonHang donHang = donHangService.getDonHangByID(id);
        System.out.println("log check: "+donHang);
        DonHangResponseDTO donHangResponse = new DonHangResponseDTO();

        donHangResponse.setIdDonHang(donHang.getIdDonHang());
        donHangResponse.setMaDonHang(donHang.getMaDonHang());
        donHangResponse.setTongTien(donHang.getTongTien());
        donHangResponse.setTongTienKhuyenMai(donHang.getTongTienKhuyenMai());
        donHangResponse.setTongTienSauKhuyenMai(donHang.getTongTienSauKhuyenMai());
        donHangResponse.setGhiChu(donHang.getGhiChu());
        donHangResponse.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
        donHangResponse.setOldKhachHang(donHang.getKhachHang());
        donHangResponse.setId_khach_hang(donHang.getKhachHang().getIdKhachHang());
        //System.out.println("log check: "+donHang);
        return ResponseEntity.ok(donHangResponse);
    }

    @PostMapping("/don-hang/them-moi")
    public ResponseEntity<?> addDonHang(@RequestBody DonHangRequestDTO donHangDTO){
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
        DonHang donHang = donHangService.createDonHAng(donHangDTO,username);
        System.out.println("log check username: "+username);
        DonHangResponseDTO responseDTO = new DonHangResponseDTO();
        responseDTO.setIdDonHang(donHang.getIdDonHang());
        responseDTO.setMaDonHang(donHang.getMaDonHang());
        responseDTO.setTongTien(donHang.getTongTien());
        responseDTO.setTongTienKhuyenMai(donHang.getTongTienKhuyenMai());
        responseDTO.setTongTienSauKhuyenMai(donHang.getTongTienSauKhuyenMai());
        responseDTO.setGhiChu(donHang.getGhiChu());
        responseDTO.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());

        // Nếu muốn trả về tên nhân viên và khách hàng
        if (donHang.getNhanVien() != null) {
            responseDTO.setNhanVien(donHang.getNhanVien().getHoTen()); // Giả sử bạn có phương thức getTen()
        }
        if (donHang.getKhachHang() != null) {
            responseDTO.setKhachHang(donHang.getKhachHang().getHoTen()); // Giả sử bạn có phương thức getTen()
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/don-hang/update-don-hang/{idDH}/{id}")
    public ResponseEntity<?> update(@PathVariable("idDH") Integer idDH, @PathVariable("id")Integer id){
        DonHang donHang = donHangService.updateDonHangKH(idDH,id);

        DonHangResponseDTO responseDTO = new DonHangResponseDTO();

        responseDTO.setIdDonHang(donHang.getIdDonHang());
        responseDTO.setMaDonHang(donHang.getMaDonHang());
        responseDTO.setTongTien(donHang.getTongTien());
        responseDTO.setTongTienKhuyenMai(donHang.getTongTienKhuyenMai());
        responseDTO.setTongTienSauKhuyenMai(donHang.getTongTienSauKhuyenMai());
        responseDTO.setGhiChu(donHang.getGhiChu());
        responseDTO.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
//        responseDTO.setIDkhachHang(donHang.getKhachHang().getIdKhachHang());
        responseDTO.setKhachHang(donHang.getKhachHang().getHoTen());
        responseDTO.setOldKhachHang(donHang.getKhachHang());

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/don-hang/don-hang/xoa/{id}")
    public ResponseEntity<?> deleteDonHang(@PathVariable("id") Integer id){
        boolean isDelete = donHangService.deleDonHang(id);
        if(isDelete){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    //khách hàng
    @GetMapping("/don-hang/get-khach-hang")
    public ResponseEntity<?> getAllKhachHang(){
        List<khachhang> listKhachHang = donHangService.getAllKhachHang();

        List<KhachHangResponseDTO> responseKHDTOList = new ArrayList<>();
        for (khachhang khachhang : listKhachHang) {
            KhachHangResponseDTO responseDTO = new KhachHangResponseDTO();
            responseDTO.setId_khach_hang(khachhang.getIdKhachHang());
            responseDTO.setMaKhachHang(khachhang.getMaKhachHang());
            responseDTO.setHoTen(khachhang.getHoTen());
            responseDTO.setSoDienThoai(khachhang.getSoDienThoai());
            responseDTO.setDiaChi(khachhang.getDiaChi());

            responseKHDTOList.add(responseDTO);
        }
        return ResponseEntity.ok(responseKHDTOList);
    }

    @GetMapping("/don-hang/get-khach-hang/{id}")
    public ResponseEntity<?> getKhachHangByID(@PathVariable("id") Integer id){
        KhachHangResponseDTO khachHang = donHangService.getKhachHangById(id);
        return  ResponseEntity.ok(khachHang);
    }

    @PostMapping("/don-hang/them-khach-hang")
    public ResponseEntity<?> addKhachHang(@RequestBody KhachHangRequestDTO khachHangRequestDTO){
        String username = null;
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
        try {
            khachhang kh = donHangService.addKhachHang(khachHangRequestDTO, username);
            return ResponseEntity.ok(kh);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Trả về lỗi với thông báo
        }
    }

    @GetMapping("/don-hang/khach-hang/tim-kiem")
    public ResponseEntity<?> searchKhachHang(@RequestParam String sdt){
        System.out.println("check Search KH: "+ sdt);

        List<khachhang> listKH = donHangService.searchKhachHang(sdt);
        List<KhachHangResponseDTO> responseKHDTOList = new ArrayList<>();

        for(khachhang kh: listKH){
            KhachHangResponseDTO khachHangResponseDTO = new KhachHangResponseDTO();

            khachHangResponseDTO.setId_khach_hang(kh.getIdKhachHang());
            khachHangResponseDTO.setMaKhachHang(kh.getMaKhachHang());
            khachHangResponseDTO.setHoTen(kh.getHoTen());
            khachHangResponseDTO.setDiaChi(kh.getDiaChi());
            khachHangResponseDTO.setSoDienThoai(kh.getSoDienThoai());
            responseKHDTOList.add(khachHangResponseDTO);
        }

        System.out.println("check Search KH11: "+ responseKHDTOList);
        return ResponseEntity.ok(responseKHDTOList);
    }

    //hoá đơn
    @PostMapping("/hoa-don/them-moi")
    public ResponseEntity<?> createHoaDon(@RequestBody HoaDonResquestDTO hoaDon){
        String username = null;
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

       HoaDon newHoaDon = hoaDonService.createHoaDon(hoaDon,username);

        System.out.println("hoa don checkll: "+hoaDon);
//        return ResponseEntity.ok("");
        return ResponseEntity.ok(newHoaDon);
    }
    @GetMapping("/hoa-don/invoice")
    public ResponseEntity<?> printerInvoice(){
        try{
            String path = hoaDonService.printerInvoice();
            return ResponseEntity.ok("");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }

}
