package com.example.demo.rest;

import com.example.demo.Service.DonHangOnlineService;
import com.example.demo.dto.reponse.*;
import com.example.demo.dto.request.DonHangOnlineRequestDTO;
import com.example.demo.dto.request.GioHAngChiTietRequestDTO;
import com.example.demo.dto.request.GioHangRequestDTO;
import com.example.demo.dto.request.ShippingRequest;
import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class restDonHangOnlineController {
    @Autowired
    DonHangOnlineService donHangOnlineService;

//    @Value("${ghn.api.token}")
//    private String ghnApiToken;
    private String ghnApiToken ="8a26d3d7-9bf3-11ef-8862-de918cd0adb5";

    private final String GHN_PROVINCE_API_URL = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";

    @GetMapping("/lay-tai-khoan")
    public ResponseEntity<?> getUserLogin(){
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
        if (username == null) {
            return ResponseEntity.ok(null);
        }
        khachhang kh = donHangOnlineService.getUserLogin(username);
        return ResponseEntity.ok(kh);
    }

    @GetMapping("/danh-sach-san-pham")
    public ResponseEntity<?> getAllProductsUser(){
        List<SanPhamChiTiet> listSanPham = donHangOnlineService.getAllProducts();
         return ResponseEntity.ok(listSanPham);
    }

    @GetMapping("/danh-sach-san-pham/{id}")
    public ResponseEntity<?> getProductByIdUser(@PathVariable("id") Integer id){
        SanPhamChiTiet sanPhamChiTiet = donHangOnlineService.getProductsByID(id);
        return ResponseEntity.ok(sanPhamChiTiet);
    }

    @GetMapping("/api/provinces")
    public ResponseEntity<?> getProvinces(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnApiToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ProvinceResponseDTO> response = restTemplate.exchange(
                GHN_PROVINCE_API_URL,
                HttpMethod.GET,
                entity,
                ProvinceResponseDTO.class
        );
        System.out.println("check get provices API: "+response.getBody().getData());
        return ResponseEntity.ok(response.getBody().getData());
    }

    @GetMapping("/api/districts/{province_id}")
    public ResponseEntity<?> getDistrics(@PathVariable("province_id") Integer province_id){
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnApiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("{\"province_id\": " + province_id + "}", headers);

        ResponseEntity<DistrictResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                DistrictResponseDTO.class
        );
        System.out.println("check get District API: "+response.getBody().getData());
        return ResponseEntity.ok(response.getBody().getData());
    }

    @GetMapping("/api/ward/{district_id}")
    public ResponseEntity<?> getWard(@PathVariable("district_id") Integer district_id){
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnApiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("{\"district_id\": " + district_id + "}", headers);

        ResponseEntity<WardResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                WardResponseDTO.class
        );
        System.out.println("check get Ward API: "+response.getBody().getData());
        return ResponseEntity.ok(response.getBody().getData());
    }

    @PostMapping("/api/fee")
    public ResponseEntity<?> getFeeShipping(@RequestBody ShippingRequest request){
        //System.out.println("check data shipping: "+request);
        String url = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnApiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ShippingRequest> entity = new HttpEntity<>(request, headers);
        //System.out.println("entity: "+entity);
        ResponseEntity<ShippingFeeResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                ShippingFeeResponseDTO.class
        );
        System.out.println("check get fee API: "+response.getBody().getData());
        return ResponseEntity.ok(response.getBody().getData());
    }

    @PostMapping("/don-hang-online/them-moi")
    public ResponseEntity<?> createOrderOnline(@RequestBody DonHangOnlineRequestDTO donHangOnlineRequestDTO){
        System.out.println("check order online create: "+donHangOnlineRequestDTO);
        DonHang donHang = donHangOnlineService.createOrder(donHangOnlineRequestDTO);

        DonHangOnlineResponseDTO donHangOnlineResponseDTO = new DonHangOnlineResponseDTO();

        donHangOnlineResponseDTO.setIdDonHang(donHang.getIdDonHang());
        donHangOnlineResponseDTO.setTenKhachHang(donHang.getTenKhachNhan());
        donHangOnlineResponseDTO.setSoDienThoaiKhachHang(donHang.getSoDienThoaiKhachNhan());
        donHangOnlineResponseDTO.setDiaChiKhachHang(donHang.getDiaChiNhan());
        donHangOnlineResponseDTO.setMaDonHang(donHang.getMaDonHang());
        donHangOnlineResponseDTO.setTongTien(donHang.getTongTien());
        donHangOnlineResponseDTO.setTongTienKhuyenMai(donHang.getTongTienKhuyenMai());
        donHangOnlineResponseDTO.setTongTienSauKhuyenMai(donHang.getTongTienSauKhuyenMai());
        donHangOnlineResponseDTO.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
        donHangOnlineResponseDTO.setLoaiDonHang(donHang.getLoaiDonHang());
        donHangOnlineResponseDTO.setIdTrangThai(donHang.getTrangThai().getIdTrangThai());
        donHangOnlineResponseDTO.setTrangThai(donHang.getTrangThai());
        donHangOnlineResponseDTO.setPhuongThucThanhToan(donHang.getPhuongThucThanhToan());

        return ResponseEntity.ok(donHangOnlineResponseDTO);
    }

    //tạo giỏ hàng
    @GetMapping("/gio-hang/lay-theo-user")
    public ResponseEntity<?> getCartUser(){
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
        GioHang gioHang = donHangOnlineService.getCart(username);
        return ResponseEntity.ok(gioHang);
    }
    @PostMapping("/gio-hang/them-moi")
    public ResponseEntity<?> createCart(@RequestBody GioHangRequestDTO gioHangRequestDTO){
        System.out.println("check cart: "+ gioHangRequestDTO);
        GioHang gioHang = donHangOnlineService.createCart(gioHangRequestDTO);
        return ResponseEntity.ok(gioHang);

    }

    //tạo giỏ hàng chi tiết
    @GetMapping("/gio-hang-chi-tiet/{idGioHang}")
    public ResponseEntity<?> getDetailCart(@PathVariable("idGioHang") Integer idGioHang){
        List<GioHangChiTiet> gioHang = donHangOnlineService.getDetailCart(idGioHang);
        return ResponseEntity.ok(gioHang);
    }
    @PostMapping("/gio-hang-chi-tiet/them-moi")
    public ResponseEntity<?> createCartDetail(@RequestBody GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO){
        System.out.println("check cart: "+ gioHAngChiTietRequestDTO);
        GioHangChiTiet gioHangChiTiet = donHangOnlineService.createDetailCart(gioHAngChiTietRequestDTO);
        GioHangChiTietResponseDTO responseDTO = new GioHangChiTietResponseDTO();

        responseDTO.setIdGioHangChiTiet(gioHangChiTiet.getIdGioHangChiTiet());
//        responseDTO.setDonGia(gioHangChiTiet.getDonGia());
        responseDTO.setGiaBan(gioHangChiTiet.getDonGia());
        responseDTO.setSoLuong(gioHangChiTiet.getSoLuong());
        responseDTO.setIdSanPham(gioHangChiTiet.getSanPhamChiTiet());
        responseDTO.setMaGioHangChiTiet(gioHangChiTiet.getMaGioHangChiTiet());
        responseDTO.setGioHang(gioHangChiTiet.getGioHang());
        return ResponseEntity.ok(responseDTO);

    }

    @PutMapping("/gio-hang-chi-tiet/cap-nhat-so-luong")
    public ResponseEntity<?> updateOrderDetail(@RequestBody GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO){
        try {
            System.out.println("check cart Detail update so luong: "+gioHAngChiTietRequestDTO);
            GioHangChiTiet gioHangChiTiet = donHangOnlineService.updateCartDetail(gioHAngChiTietRequestDTO);
            return ResponseEntity.ok(gioHangChiTiet);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Trả về lỗi với thông báo
        }

    }

    @DeleteMapping("/gio-hang-chi-tiet/xoa-theo-id-san-pham/{id}")
    public ResponseEntity<?> deleteCartdetail(@PathVariable("id")Integer id){
        System.out.println("check id delete: "+id);
        GioHangChiTiet deletedGioHangChiTiet = donHangOnlineService.deleteAndReturnBySanPhamChiTietId(id);
        if (deletedGioHangChiTiet != null) {
            return ResponseEntity.ok(deletedGioHangChiTiet); // Trả về đối tượng đã xóa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm trong giỏ hàng với ID sản phẩm: " + id);
        }

    }
}
