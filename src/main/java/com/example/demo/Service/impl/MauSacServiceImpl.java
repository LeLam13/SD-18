package com.example.demo.Service.impl;

import com.example.demo.Service.MauSacService;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.XuatXu;
import com.example.demo.repo.MauSacRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MauSacServiceImpl implements MauSacService {
    @Autowired
    private MauSacRepo mauSacRepo;

    Date date = new Date();

    @Override
    public List<MauSac> getAll() {
        return mauSacRepo.findAll();
    }

    @Override
    public List<MauSac> getAllByTT() {
        return mauSacRepo.getAllByTT();
    }

    @Override
    public List<MauSac> getAllbyCT(Integer idSanPham) {
        return mauSacRepo.getAllByCT(idSanPham);
    }

    @Override
    public Page<MauSac> findAll(Pageable pageable) {
        return mauSacRepo.findAll(pageable);
    }

    @Override
    public MauSac createMauSac(MauSacRequestDTO mauSacRequestDTO) {
        MauSac ms = new MauSac();
        ms.setMa(mauSacRequestDTO.getMa());
        ms.setTen(mauSacRequestDTO.getTen());
        ms.setCreateDate(date);
        ms.setTrangThai(true);
        ms.setCreateBy(getCurrentUsername());
        return mauSacRepo.save(ms);
    }

    @Override
    public MauSac updateMauSac(MauSacRequestDTO mauSacRequestDTO) {
        MauSac ms = mauSacRepo.findByIdMauSac(mauSacRequestDTO.getIdMauSac());
        ms.setMa(mauSacRequestDTO.getMa());
        ms.setTen(mauSacRequestDTO.getTen());
        ms.setUpdateBy(getCurrentUsername());
        ms.setUpdateDate(date);
        return mauSacRepo.save(ms);
    }

    @Override
    public MauSac getMauSac(Integer idMauSac) {
        return mauSacRepo.findByIdMauSac(idMauSac);
    }

    @Override
    public MauSac updateTrangThai(Integer idMauSac) {
        MauSac ms = mauSacRepo.findByIdMauSac(idMauSac);
        if(ms.getTrangThai()==true){
            ms.setTrangThai(false);
        }
        else{
            ms.setTrangThai(true);
        }
        return mauSacRepo.save(ms);
    }


    @Override
    public MauSac deleteMauSac(Integer idMauSac) {
        mauSacRepo.deleteById(idMauSac);
        return null;
    }

    @Override
    public Page<MauSac> search(String query, Pageable pageable) {
        return mauSacRepo.searchIgnoreCaseAndDiacritics(query, pageable);
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
