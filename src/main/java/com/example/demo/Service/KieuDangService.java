package com.example.demo.Service;

import com.example.demo.dto.request.KieuDangRequestDTO;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.entity.KieuDang;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KieuDangService {
   public List<KieuDang> getAll();

   public Page<KieuDang> findAll(Pageable pageable);

   public KieuDang createKieuDang(KieuDangRequestDTO kieuDangRequestDTO);

   public KieuDang updateKieuDang(KieuDangRequestDTO kieuDangRequestDTO);

   public KieuDang getKieuDang(String ma);

   public KieuDang updateTrangThai(Integer idKieuDang);

   public KieuDang deleteKieuDang(Integer idKieuDang);
}
