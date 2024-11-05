package com.example.demo.Service;

import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.dto.request.XuatXuRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.XuatXu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface XuatXuService {

    public List<XuatXu> getAll();

    public Page<XuatXu> findAll(Pageable pageable);

    public XuatXu createXuatXu(XuatXuRequestDTO xuatXuRequestDTO);

    public XuatXu updateXuatXu(XuatXuRequestDTO xuatXuRequestDTO);

    public XuatXu getXuatXu(String ma);

    public XuatXu updateTrangThai(Integer idXuatXu);

    public XuatXu deleteXuatXu(Integer idXuatXu);

}
