package com.example.demo.Service.impl;


import com.example.demo.Service.DonHangOnlineService;
import com.example.demo.dto.request.*;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repo.SanPhamChiTietRepo;

import com.example.demo.entity.*;
import com.example.demo.repo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DonHangOnlineServiceImpl implements DonHangOnlineService {
    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;
    @Autowired
    TrangThaiRepo trangThaiRepo;
    @Autowired
    PhuongThucThanhToanRepo phuongThucThanhToanRepo;
    @Autowired
    KhuyenMaiRepo khuyenMaiRepo;
    @Autowired
    DonHangRepo donHangRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @Autowired
    taikhoanRepo taikhoanRepo;
    @Autowired
    khachhangRePo khachhangRePo;
    @Autowired
    GioHangRepo gioHangRepo;
    @Autowired
    GioHangchiTietRepo gioHangchiTietRepo;
    @Autowired
    HoaDonRepo hoaDonRepo;
    @Autowired
    HoaDonChiTietRepo hoaDonChiTietRepo;

    @Override
    public List<SanPhamChiTiet> getAllProducts() {
        List<SanPhamChiTiet> listSanPham = sanPhamChiTietRepo.findAll();
//
        return listSanPham;
    }

    @Override
    public SanPhamChiTiet getProductsByID(Integer id) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(id).get();
        return sanPhamChiTiet;
    }

    @Override
    public DonHang createOrder(DonHangOnlineRequestDTO donHangOnlineRequestDTO, String username) {
        DonHang donHang = new DonHang();

        donHang.setMaDonHang(donHangOnlineRequestDTO.getMaDonHang());
        donHang.setTenKhachNhan(donHangOnlineRequestDTO.getTenKhachHang());
        donHang.setSoDienThoaiKhachNhan(donHangOnlineRequestDTO.getSoDienThoaiKhachHang());
        donHang.setDiaChiNhan(donHangOnlineRequestDTO.getDiaChiKhachHang());
        donHang.setEmailKhachNhan(donHangOnlineRequestDTO.getEmailKhachHang());
        donHang.setTongTien(donHangOnlineRequestDTO.getTongTien());
        donHang.setTongTienKhuyenMai(donHangOnlineRequestDTO.getTongTienKhuyenMai());
        donHang.setTongTienSauKhuyenMai(donHangOnlineRequestDTO.getTongTienSauKhuyenMai());

        donHang.setTongTienThanhToan(donHangOnlineRequestDTO.getTongTienThanhToan());
        donHang.setPhiVanChuyen(donHangOnlineRequestDTO.getPhiVanChuyen());

        donHang.setGhiChu(donHangOnlineRequestDTO.getGhiChu());
        donHang.setTrangThaiThanhToan(donHangOnlineRequestDTO.getTrangThaiThanhToan());
        donHang.setLoaiDonHang(2);
        donHang.setPhuongThucNhan(2);
        TrangThai trangThai = trangThaiRepo.findById(donHangOnlineRequestDTO.getIdTrangThai()).get();
        donHang.setTrangThai(trangThai);
        PhuongThucThanhToan phuongThucThanhToan= phuongThucThanhToanRepo.findById(donHangOnlineRequestDTO.getIdPhuongThucThanhToan()).get();
        donHang.setPhuongThucThanhToan(phuongThucThanhToan);

        if(donHangOnlineRequestDTO.getIdKhuyenMai() != null){
            KhuyenMai khuyenMai = khuyenMaiRepo.findById(donHangOnlineRequestDTO.getIdKhuyenMai()).get();
            donHang.setKhuyenMai(khuyenMai);

            khuyenMai.setSoLuong(khuyenMai.getSoLuong()-1);
            khuyenMaiRepo.save(khuyenMai);
        }

        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(username!= null || username.length()>0){
            System.out.println("check TK: "+oldTaiKoan.toString());
            System.out.println("check TK: "+oldTaiKoan.getKhachHang().getIdKhachHang());
            khachhang getKH = khachhangRePo.findByIdKhachHang(oldTaiKoan.getKhachHang().getIdKhachHang());
            donHang.setKhachHang(getKH);
        }

        //ngày
        LocalDate localDate = LocalDate.now();
        donHang.setCreateDate(localDate);
        donHang.setCreateBy(donHangOnlineRequestDTO.getTenKhachHang());

        donHangRepo.save(donHang);

        //tạo đơn hàng chi tiết GioHangChiTiet
        DonHang donHang1 = donHangRepo.findByMaDonHang(donHangOnlineRequestDTO.getMaDonHang());
        List<DonHangChiTietRequestDTO> donHangChiTietList = donHangOnlineRequestDTO.getOrderDetail();
//        List<GioHangChiTiet> donHangChiTietList = donHangOnlineRequestDTO.getOrderDetail();
        System.out.println("check getOrderDetail: "+donHangOnlineRequestDTO.getOrderDetail());
        for (DonHangChiTietRequestDTO dto : donHangChiTietList) {
            DonHangChiTiet donHangChiTiet = new DonHangChiTiet();

            donHangChiTiet.setMaDonHangChiTiet(generateRandomString(8));
            donHangChiTiet.setSoLuong(dto.getSoLuong());
            donHangChiTiet.setDonGia(dto.getGiaBan());
            donHangChiTiet.setDonHang(donHang1);

            // Giả sử bạn có phương thức để tìm SanPhamChiTiet từ id
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(dto.getIdSanPhamChiTiet()).get();
            donHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);

            donHangChiTietRepo.save(donHangChiTiet);
        }
        return donHang;
    }

    @Override
    public String generateRandomString(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public khachhang getUserLogin(String username) {
        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if (oldTaiKoan == null) {
            throw new RuntimeException("Không tìm thấy tài khoản");
        }

        khachhang kh = khachhangRePo.findByUsername(username);
        return kh;
    }

    @Override
    public GioHang createCart(GioHangRequestDTO gioHangRequestDTO) {
        GioHang gioHang = new GioHang();
        gioHang.setMaGioHang(gioHangRequestDTO.getMaGioHang());
        gioHang.setTrangThai(true);
        taikhoan taikhoan= taikhoanRepo.findByUsername(gioHangRequestDTO.getUserName().getTaikhoan().getUsername());
        gioHang.setTaiKhoan(taikhoan);
        gioHangRepo.save(gioHang);
        return gioHang;
    }

    @Override
    public GioHangChiTiet createDetailCart(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO) {
        GioHangChiTiet gioHangChiTiet = new GioHangChiTiet();
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(gioHAngChiTietRequestDTO.getIdSanPhamChiTiet()).get();
        GioHang gioHang = gioHangRepo.findById(gioHAngChiTietRequestDTO.getIdGioHang()).get();

        gioHangChiTiet.setGioHang(gioHang);
        gioHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
        gioHangChiTiet.setMaGioHangChiTiet(gioHAngChiTietRequestDTO.getMaGioHangChiTiet());
        gioHangChiTiet.setSoLuong(gioHAngChiTietRequestDTO.getSoLuong());
//        gioHangChiTiet.setDonGia(sanPhamChiTiet.getGiaBan());
        gioHangChiTiet.setGiaBan(sanPhamChiTiet.getGiaBan());
        gioHangChiTiet.setTrangThai(true);

        gioHangchiTietRepo.save(gioHangChiTiet);

        SanPhamChiTiet oldSanPhamCT = sanPhamChiTietRepo.findById(gioHAngChiTietRequestDTO.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết!"));
        //cập nhật lại số lượng sản phẩm
        if (oldSanPhamCT.getSoLuong() < gioHAngChiTietRequestDTO.getSoLuong()) {
            throw new RuntimeException("Số lượng sản phẩm không đủ!");
        }
        // Cập nhật lại số lượng tồn kho của sản phẩm
        oldSanPhamCT.setSoLuong(oldSanPhamCT.getSoLuong() - gioHAngChiTietRequestDTO.getSoLuong());
        sanPhamChiTietRepo.save(oldSanPhamCT);
        return gioHangChiTiet;
    }

    @Override
    public  GioHang getCart(String username) {
        GioHang gioHang = gioHangRepo.findByUsername(username);
        return gioHang;
    }

    @Override
    public List<GioHangChiTiet> getDetailCart(Integer idGioHang) {
        List<GioHangChiTiet> gioHangChiTiet = gioHangchiTietRepo.findByGioHangId(idGioHang);
        return gioHangChiTiet;
    }

    @Override
    public GioHangChiTiet updateCartDetail(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO) {
        //DonHangChiTiet oldDonHangCT = donHangChiTietRepo.findBySanPhamID(chitietRequestDTO.getIdSanPhamChiTiet(),chitietRequestDTO.getIdĐonHang());
        GioHangChiTiet oldGioHAngChiTiet = gioHangchiTietRepo.findBySanPhamIdAndGioHangId(
                gioHAngChiTietRequestDTO.getIdSanPhamChiTiet(), gioHAngChiTietRequestDTO.getIdGioHang());
        //số lượng cập nhật > sô lượng có
        SanPhamChiTiet oldSacPhamCT = sanPhamChiTietRepo.findById(gioHAngChiTietRequestDTO.getIdSanPhamChiTiet()).get();
        if (oldSacPhamCT.getSoLuong() < gioHAngChiTietRequestDTO.getSoLuong()) {
            throw new RuntimeException("Số lượng sản phẩm không đủ!");
        }
        //cập nhật số lượng của sản phẩm chi tiết
        oldSacPhamCT.setSoLuong(oldSacPhamCT.getSoLuong() - gioHAngChiTietRequestDTO.getSoLuong());
        sanPhamChiTietRepo.save(oldSacPhamCT);

        //cập nhật số lượng đon hàng chi tiết
        oldGioHAngChiTiet.setIdGioHangChiTiet(oldGioHAngChiTiet.getIdGioHangChiTiet());
        Integer soLuong = oldGioHAngChiTiet.getSoLuong() + gioHAngChiTietRequestDTO.getSoLuong();
        oldGioHAngChiTiet.setSoLuong(soLuong);
        gioHangchiTietRepo.save(oldGioHAngChiTiet);
        return oldGioHAngChiTiet;
    }

    @Override
    public GioHangChiTiet deleteAndReturnBySanPhamChiTietId(Integer idSanPhamChiTiet) {
        GioHangChiTiet gioHangChiTiet = gioHangchiTietRepo.findBySanPhamChiTiet_IdSanPhamChiTiet(idSanPhamChiTiet);
        if (gioHangChiTiet != null) {
            gioHangchiTietRepo.delete(gioHangChiTiet);
        }
        return gioHangChiTiet;
    }

    @Override
    public GioHangChiTiet updateCartDetailPlus(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO) {
        GioHangChiTiet oldDonHangCT = gioHangchiTietRepo.findBySanPhamIdAndGioHangId(gioHAngChiTietRequestDTO.getIdSanPhamChiTiet(),gioHAngChiTietRequestDTO.getIdGioHang());

        //số lượng cập nhật > sô lượng có
        SanPhamChiTiet oldSacPhamCT = sanPhamChiTietRepo.findById(gioHAngChiTietRequestDTO.getIdSanPhamChiTiet()).get();
        if (oldSacPhamCT.getSoLuong() < gioHAngChiTietRequestDTO.getSoLuong()) {
            throw new RuntimeException("Số lượng sản phẩm không đủ!");
        }
        //cập nhật số lượng của sản phẩm chi tiết
        oldSacPhamCT.setSoLuong(oldSacPhamCT.getSoLuong() - gioHAngChiTietRequestDTO.getSoLuong());
        sanPhamChiTietRepo.save(oldSacPhamCT);

        //cập nhật số lượng đon hàng chi tiết
        oldDonHangCT.setIdGioHangChiTiet(oldDonHangCT.getIdGioHangChiTiet());
        Integer soLuong = oldDonHangCT.getSoLuong() + gioHAngChiTietRequestDTO.getSoLuong();
        oldDonHangCT.setSoLuong(soLuong);
        gioHangchiTietRepo.save(oldDonHangCT);
        return oldDonHangCT;
    }

    @Override
    public GioHangChiTiet updateCartDetailReduce(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO) {
        GioHangChiTiet oldDonHangCT = gioHangchiTietRepo.findBySanPhamIdAndGioHangId(gioHAngChiTietRequestDTO.getIdSanPhamChiTiet(),gioHAngChiTietRequestDTO.getIdGioHang());

        //số lượng cập nhật > sô lượng có
        SanPhamChiTiet oldSacPhamCT = sanPhamChiTietRepo.findById(gioHAngChiTietRequestDTO.getIdSanPhamChiTiet()).get();
        if (oldSacPhamCT.getSoLuong() < gioHAngChiTietRequestDTO.getSoLuong()) {
            throw new RuntimeException("Số lượng sản phẩm không đủ!");
        }
        //cập nhật số lượng của sản phẩm chi tiết
        oldSacPhamCT.setSoLuong(oldSacPhamCT.getSoLuong() + gioHAngChiTietRequestDTO.getSoLuong());
        sanPhamChiTietRepo.save(oldSacPhamCT);

        //cập nhật số lượng đon hàng chi tiết
        oldDonHangCT.setIdGioHangChiTiet(oldDonHangCT.getIdGioHangChiTiet());
        Integer soLuong = oldDonHangCT.getSoLuong() - gioHAngChiTietRequestDTO.getSoLuong();
        oldDonHangCT.setSoLuong(soLuong);
        gioHangchiTietRepo.save(oldDonHangCT);
        return oldDonHangCT;
    }

    @Override
    public GioHangChiTiet updateCartDetailChange(GioHAngChiTietRequestDTO gioHAngChiTietRequestDTO) {
        GioHangChiTiet oldDonHangCT = gioHangchiTietRepo.findBySanPhamIdAndGioHangId(gioHAngChiTietRequestDTO.getIdSanPhamChiTiet(),gioHAngChiTietRequestDTO.getIdGioHang());

        //số lượng cập nhật > sô lượng có
        SanPhamChiTiet oldSacPhamCT = sanPhamChiTietRepo.findById(gioHAngChiTietRequestDTO.getIdSanPhamChiTiet()).get();
        if (oldSacPhamCT.getSoLuong() < gioHAngChiTietRequestDTO.getSoLuong()) {
            throw new RuntimeException("Số lượng sản phẩm không đủ!");
        }
        //cập nhật số lượng của sản phẩm chi tiết
        int soLuongChange=0;
        if(gioHAngChiTietRequestDTO.getSoLuong() > oldDonHangCT.getSoLuong()){
            soLuongChange= gioHAngChiTietRequestDTO.getSoLuong()- oldDonHangCT.getSoLuong();
            oldSacPhamCT.setSoLuong(oldSacPhamCT.getSoLuong() - soLuongChange);
        }else {
            soLuongChange = oldDonHangCT.getSoLuong() - gioHAngChiTietRequestDTO.getSoLuong();
            oldSacPhamCT.setSoLuong(oldSacPhamCT.getSoLuong() + soLuongChange);
        }

        //oldSacPhamCT.setSoLuong(oldSacPhamCT.getSoLuong() - soLuongChange);
        sanPhamChiTietRepo.save(oldSacPhamCT);

        //cập nhật số lượng đon hàng chi tiết
        oldDonHangCT.setIdGioHangChiTiet(oldDonHangCT.getIdGioHangChiTiet());
        //Integer soLuong = oldDonHangCT.getSoLuong() - gioHAngChiTietRequestDTO.getSoLuong();
        oldDonHangCT.setSoLuong(gioHAngChiTietRequestDTO.getSoLuong());
        gioHangchiTietRepo.save(oldDonHangCT);
        return oldDonHangCT;
    }

    @Override
    public HoaDon createInvoice(HoaDonOnlineRequestDTO hoaDonOnlineRequestDTO, String username) {
        HoaDon hoaDon = new HoaDon();
        String nameCustorm = null;

        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
            //System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());
//            nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
//            hoaDon.setNhanVien(getNV);
            khachhang khachhang = khachhangRePo.findByIdKhachHang(oldTaiKoan.getKhachHang().getIdKhachHang());
            nameCustorm = khachhang.getHoTen();
            hoaDon.setKhachHang(khachhang);
        }

        DonHang donHang = donHangRepo.findById(hoaDonOnlineRequestDTO.getIdDonHang()).get();
        if(donHang == null){
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }
        //set khuyến mãi
        if(donHang.getKhuyenMai() != null){
            hoaDon.setKhuyenMai(donHang.getKhuyenMai());
        }
        //set trang Thai
        hoaDon.setTrangThai(donHang.getTrangThai());
        //set phương thuc thanh toan
        hoaDon.setPhuongThucThanhToan(donHang.getPhuongThucThanhToan());
        //set don hàng
        hoaDon.setDonHang(donHang);
        //set khach hàng
        hoaDon.setKhachHang(donHang.getKhachHang());
        //set ma hd
        hoaDon.setMaHoaDon(hoaDonOnlineRequestDTO.getMaHoaDon());
        //set ten kn
        hoaDon.setTenKhachNhan(donHang.getTenKhachNhan());
        //set email kn
        hoaDon.setEmailKhachNhan(donHang.getEmailKhachNhan());
        //set sdt kn
        hoaDon.setSoDienThoaiKhachNhan(donHang.getSoDienThoaiKhachNhan());
        //set dia chỉ kn
        hoaDon.setDiaChiNhan(donHang.getDiaChiNhan());
        //tổng tiên
        hoaDon.setTongTien(donHang.getTongTien());
        //tong tiền km
        hoaDon.setTongTienKhuyenMai(donHang.getTongTienKhuyenMai());
        //tong tien sau km
        hoaDon.setTongTienSauKhuyenMai(donHang.getTongTienSauKhuyenMai());
        //tong tien phai thanh toan
        hoaDon.setTongTienThanhToan(donHang.getTongTienThanhToan());
        //phi van chuyen
        hoaDon.setPhiVanChuyen(donHang.getPhiVanChuyen());
        //ghi chu
        hoaDon.setGhiChu(donHang.getGhiChu());
        //trang thai thanh toan
        hoaDon.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
        //phuong thuc nhan
        hoaDon.setPhuongThucNhan(donHang.getPhuongThucNhan());

        //ngày tháng người tạo hoá đơn
        LocalDate localDate = LocalDate.now();
        donHang.setCreateDate(localDate);
        donHang.setCreateBy(nameCustorm);

        hoaDonRepo.save(hoaDon);

        //tạo háo đơn chi tiết
        HoaDon findHoaDon = hoaDonRepo.findByMaHoaDon(hoaDonOnlineRequestDTO.getMaHoaDon());
        List<DonHangChiTiet> donHangChiTietLits = donHangChiTietRepo.findByDonHangId(donHang.getIdDonHang());
        for(DonHangChiTiet donHangChiTiet: donHangChiTietLits){
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            hoaDonChiTiet.setMaHoaDonChiTiet(generateRandomString(8));
            hoaDonChiTiet.setHoaDon(findHoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(donHangChiTiet.getSanPhamChiTiet());
            hoaDonChiTiet.setSoLuong(donHangChiTiet.getSoLuong());
            hoaDonChiTiet.setDonGia(donHangChiTiet.getDonGia());
            hoaDonChiTiet.setTrangThai(true);
            hoaDonChiTiet.setGhiChu(donHangChiTiet.getGhiChu());

            hoaDonChiTietRepo.save(hoaDonChiTiet);
        }
        return hoaDon;
    }

    @Override
    public void deleCartDetailByIdGioHang(Integer id) {
        gioHangchiTietRepo.deleteByGioHangId(id);
    }

    @Override
    public DonHang findByID(Integer idDonHang) {
        return donHangRepo.findById(idDonHang).get();
    }

}
