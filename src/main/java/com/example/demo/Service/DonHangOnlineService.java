package com.example.demo.Service;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DonHangOnlineService {
    List<SanPhamChiTiet> getAllProducts();

    SanPhamChiTiet getProductsByID(Integer id);

//    Page<SanPhamChiTiet> findByIdSanPham(Integer idSanPham, Pageable pageable);
}
