package com.example.demo.Service;

import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.dto.request.ThuongHieuRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ThuongHieuService {

    public List<ThuongHieu>  getAll();

    public Page<ThuongHieu> findAll(Pageable pageable);

    public ThuongHieu createThuongHieu(ThuongHieuRequestDTO thuongHieuRequestDTO);

    public ThuongHieu updateThuongHieu(ThuongHieuRequestDTO thuongHieuRequestDTO);

    public ThuongHieu getThuongHieu(String ma);

    public ThuongHieu updateTrangThai(Integer idThuongHieu);

    public ThuongHieu deleteThuongHieu(Integer idThuongHieu);
}
