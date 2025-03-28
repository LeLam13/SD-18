package com.example.demo.Service.impl;

import com.example.demo.Service.DonHangService;
import com.example.demo.dto.reponse.DonHangTongSoLuongResponseDTO;
import com.example.demo.dto.reponse.KhachHangResponseDTO;
import com.example.demo.dto.request.DonHangChiTietRequestDTO;
import com.example.demo.dto.request.DonHangRequestDTO;
import com.example.demo.dto.request.KhachHangRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DonHangServiceImpl implements DonHangService {
    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;
    @Autowired
    DonHangRepo donHangRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @Autowired
    taikhoanRepo taikhoanRepo;
    @Autowired
    NhanVienRepo nhanVienRepo;
    @Autowired
    TrangThaiRepo trangThaiRepo;
    @Autowired
    khachhangRePo khachhangRePo;

    @Override
    public List<SanPhamChiTiet> getAllProducts() {
        return sanPhamChiTietRepo.findAll();
    }



    @Override
    public DonHang createDonHAng(DonHangRequestDTO donHangDTO, String username) {
        DonHang newDonHang = new DonHang();
        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
            System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());

            newDonHang.setMaDonHang(donHangDTO.getMaDonHang());
            newDonHang.setTrangThaiThanhToan(donHangDTO.getTrangThaiThanhToan());
            nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
            newDonHang.setNhanVien(getNV);
            TrangThai trangThai = trangThaiRepo.findById(donHangDTO.getIdTrangThai()).get();
            newDonHang.setTrangThai(trangThai);
        }
        return donHangRepo.save(newDonHang);
    }

    @Override
    public List<DonHangTongSoLuongResponseDTO> getTongSoLuongDonHang() {
        return donHangRepo.findTongSoLuongDonHang();
    }

    @Override
    public List<DonHangChiTiet> getAllProductsOrder(Integer id) {
        return donHangChiTietRepo.findByDonHangId(id);
    }

    @Override
    public DonHangChiTiet updateDonHangChitiet(DonHangChiTietRequestDTO chitietRequestDTO) {
        DonHangChiTiet oldDonHangCT = donHangChiTietRepo.findBySanPhamID(chitietRequestDTO.getIdSanPhamChiTiet(),chitietRequestDTO.getIdĐonHang());

        //số lượng cập nhật > sô lượng có
        SanPhamChiTiet oldSacPhamCT = sanPhamChiTietRepo.findById(chitietRequestDTO.getIdSanPhamChiTiet()).get();
        if (oldSacPhamCT.getSoLuong() < chitietRequestDTO.getSoLuong()) {
            throw new RuntimeException("Số lượng sản phẩm không đủ!");
        }
        //cập nhật số lượng của sản phẩm chi tiết
        oldSacPhamCT.setSoLuong(oldSacPhamCT.getSoLuong() - chitietRequestDTO.getSoLuong());
        sanPhamChiTietRepo.save(oldSacPhamCT);

        //cập nhật số lượng đon hàng chi tiết
        oldDonHangCT.setIdDonHangChiTiet(oldDonHangCT.getIdDonHangChiTiet());
        Integer soLuong = oldDonHangCT.getSoLuong() + chitietRequestDTO.getSoLuong();
        oldDonHangCT.setSoLuong(soLuong);
        return donHangChiTietRepo.save(oldDonHangCT);
    }

    @Override
    public DonHangChiTiet createDonHangChitiet(DonHangChiTietRequestDTO chitietRequestDTO) {
        SanPhamChiTiet oldSanPhamCT = sanPhamChiTietRepo.findById(chitietRequestDTO.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết!"));
        //cập nhật lại số lượng sản phẩm
        if (oldSanPhamCT.getSoLuong() < chitietRequestDTO.getSoLuong()) {
            throw new RuntimeException("Số lượng sản phẩm không đủ!");
        }
        // Cập nhật lại số lượng tồn kho của sản phẩm
        oldSanPhamCT.setSoLuong(oldSanPhamCT.getSoLuong() - chitietRequestDTO.getSoLuong());
        sanPhamChiTietRepo.save(oldSanPhamCT);

        //tạo đơn hàng chi tiết mới
        DonHangChiTiet newDonHangCT = new DonHangChiTiet();

        newDonHangCT.setMaDonHangChiTiet(chitietRequestDTO.getMaDonHangChiTiet());
        newDonHangCT.setSoLuong(chitietRequestDTO.getSoLuong());
        newDonHangCT.setDonGia(oldSanPhamCT.getGiaBan());

        DonHang oldDonHang = donHangRepo.findById(chitietRequestDTO.getIdĐonHang()).get();
        newDonHangCT.setDonHang(oldDonHang);

        newDonHangCT.setSanPhamChiTiet(oldSanPhamCT);

        return donHangChiTietRepo.save(newDonHangCT);
    }

    @Override
    public boolean deleDonHangChiTiet(Integer id) {
        if(donHangChiTietRepo.existsById(id)){
            DonHangChiTiet donHangChiTiet = donHangChiTietRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng chi tiết!"));

            // Lấy sản phẩm chi tiết từ đơn hàng chi tiết
            SanPhamChiTiet sanPhamChiTiet = donHangChiTiet.getSanPhamChiTiet();
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + donHangChiTiet.getSoLuong());

            // Lưu sản phẩm chi tiết sau khi cập nhật lại số lượng
            sanPhamChiTietRepo.save(sanPhamChiTiet);
            //xoá đơn hàng
            donHangChiTietRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleDonHang(Integer id) {
        //xoá đơn hàng
        Optional<DonHang> optionalDonHang = donHangRepo.findById(id);
        if (optionalDonHang.isPresent()) {
            List<DonHangChiTiet> chiTietDonHangList = donHangChiTietRepo.findByDonHangId(id);

            // Cập nhật lại số lượng sản phẩm
            for (DonHangChiTiet chiTiet : chiTietDonHangList) {
                SanPhamChiTiet sanPham = chiTiet.getSanPhamChiTiet();
                sanPham.setSoLuong(sanPham.getSoLuong() + chiTiet.getSoLuong());
                sanPhamChiTietRepo.save(sanPham);
            }

            //Xoá các chi tiết đơn hàng
            donHangChiTietRepo.deleteByDonHangId(id);

            //Xoá đơn hàng
            donHangRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<khachhang> getAllKhachHang() {
        return khachhangRePo.findAll();
    }

    @Override
    public KhachHangResponseDTO getKhachHangById(Integer id) {
        khachhang kh = khachhangRePo.findById(id).get();
        KhachHangResponseDTO  responseDTO = new KhachHangResponseDTO();
        responseDTO.setId_khach_hang(kh.getIdKhachHang());
        responseDTO.setMaKhachHang(kh.getMaKhachHang());
        responseDTO.setDiaChi(kh.getDiaChi());
        responseDTO.setHoTen(kh.getHoTen());
        responseDTO.setSoDienThoai(kh.getSoDienThoai());
        return responseDTO;
    }

    @Override
    public khachhang addKhachHang(KhachHangRequestDTO khachHangRequestDTO,String username) {
        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if (oldTaiKoan == null) {
            throw new RuntimeException("Không tìm thấy tài khoản");
        }
        nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).orElse(null);
        if (getNV == null) {
            throw new RuntimeException("Không tìm thấy nhân viên");
        }

        List<khachhang> khachhangList = khachhangRePo.findBySoDienThoai(khachHangRequestDTO.getSoDienThoai());
        if (!khachhangList.isEmpty()) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        khachhang newkhachhang = new khachhang();

        newkhachhang.setMaKhachHang(khachHangRequestDTO.getMaKhachHang());
        newkhachhang.setHoTen("Khách Lẻ");
        newkhachhang.setSoDienThoai(khachHangRequestDTO.getSoDienThoai());
        khachhangRePo.save(newkhachhang);
        return newkhachhang;
    }

    @Override
    public DonHang updateDonHangKH(Integer idDH, Integer id) {
        khachhang oldKhachHang = khachhangRePo.findById(id).get();
        if(oldKhachHang == null){
            throw new RuntimeException("Không tìm thấy khách hàng");
        }
        DonHang oldDonHang = donHangRepo.findById(idDH).get();
        if(oldDonHang == null){
            throw new RuntimeException("Không tìm thấy đơn hàng");
        }
        oldDonHang.setKhachHang(oldKhachHang);
        return donHangRepo.save(oldDonHang);
    }

    @Override
    public DonHang getDonHangByID(Integer id) {
        return donHangRepo.findById(id).get();
    }

    @Override
    public  List<khachhang> searchKhachHang(String sdt) {
        return khachhangRePo.findBySoDienThoai(sdt);
    }

    @Override
    public List<SanPhamChiTiet> searchSanPhamChiTiet(String tenSP) {
        return sanPhamChiTietRepo.findBySanPhamTenContainingIgnoreCase(tenSP);
    }

}
