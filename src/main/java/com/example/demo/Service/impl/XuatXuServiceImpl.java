package com.example.demo.Service.impl;

import com.example.demo.Service.XuatXuService;
import com.example.demo.dto.request.XuatXuRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.XuatXu;
import com.example.demo.repo.XuatXuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class XuatXuServiceImpl implements XuatXuService {
    @Autowired
    private XuatXuRepo xuatXuRepo;

    Date date = new Date();

    @Override
    public List<XuatXu> getAll() {
        return xuatXuRepo.findAll();
    }

    @Override
    public List<XuatXu> getAllByTT() {
        return xuatXuRepo.getAllByTT();
    }

    @Override
    public Page<XuatXu> findAll(Pageable pageable) {
        return xuatXuRepo.findAll(pageable);
    }

    @Override
    public XuatXu createXuatXu(XuatXuRequestDTO xuatXuRequestDTO) {
        XuatXu xx = new XuatXu();
        xx.setMa(xuatXuRequestDTO.getMa());
        xx.setTen(xuatXuRequestDTO.getTen());
        xx.setCreateDate(date);
        xx.setCreateBy(getCurrentUsername());
        xx.setTrangThai(true);
        return xuatXuRepo.save(xx);
    }

    @Override
    public XuatXu updateXuatXu(XuatXuRequestDTO xuatXuRequestDTO) {
        XuatXu xx = xuatXuRepo.findByMa(xuatXuRequestDTO.getMa());
        xx.setTen(xuatXuRequestDTO.getTen());
        xx.setUpdateBy(getCurrentUsername());
        xx.setUpdateDate(date);
        return xuatXuRepo.save(xx);
    }

    @Override
    public XuatXu getXuatXu(String ma) {
        return xuatXuRepo.findByMa(ma);
    }

    @Override
    public XuatXu updateTrangThai(Integer idXuatXu) {
        XuatXu xx = xuatXuRepo.findByIdXuatXu(idXuatXu);
        if (xx.getTrangThai() == true) {
            xx.setTrangThai(false);
        } else {
            xx.setTrangThai(true);
        }
        return xuatXuRepo.save(xx);
    }


    @Override
    public XuatXu deleteXuatXu(Integer idXuatXu) {
        xuatXuRepo.deleteById(idXuatXu);
        return null;
    }

    @Override
    public Page<XuatXu> search(String query, Pageable pageable) {
        return xuatXuRepo.searchIgnoreCaseAndDiacritics(query, pageable);
    }

    public String getCurrentUsername() {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                // Trường hợp principal là UserDetails
                username = ((UserDetails) principal).getUsername();
                System.out.println("Username (UserDetails): " + username);
            } else {
                // Trường hợp principal là chuỗi (vd: OAuth2)
                username = principal.toString();
                System.out.println("Username (String): " + username);
            }
        }
        return username;
    }
}
