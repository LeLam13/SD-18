package com.example.demo.Service.impl;

import com.example.demo.Service.ThuongHieuService;
import com.example.demo.dto.request.ThuongHieuRequestDTO;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.ThuongHieu;
import com.example.demo.entity.XuatXu;
import com.example.demo.repo.ThuongHieuRepo;
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
public class ThuongHieuServiceImpl implements ThuongHieuService {
    @Autowired
    private ThuongHieuRepo thuongHieuRepo;

    Date date=new Date();

    @Override
    public List<ThuongHieu> getAll() {
        return thuongHieuRepo.findAll();
    }

    @Override
    public List<ThuongHieu> getAllByTT() {
        return thuongHieuRepo.getAllByTT();
    }

    @Override
    public Page<ThuongHieu> findAll(Pageable pageable) {
        return thuongHieuRepo.findAll(pageable);
    }

    @Override
    public ThuongHieu createThuongHieu(ThuongHieuRequestDTO thuongHieuRequestDTO) {
        ThuongHieu th = new ThuongHieu();
        th.setMa(thuongHieuRequestDTO.getMa());
        th.setTen(thuongHieuRequestDTO.getTen());
        th.setCreateDate(date);
        th.setUpdateDate(date);
        th.setCreateBy(getCurrentUsername());
        th.setTrangThai(true);
        return thuongHieuRepo.save(th);
    }

    @Override
    public ThuongHieu updateThuongHieu(ThuongHieuRequestDTO thuongHieuRequestDTO) {
        ThuongHieu th = thuongHieuRepo.findByMa(thuongHieuRequestDTO.getMa());
        th.setTen(thuongHieuRequestDTO.getTen());
        th.setUpdateDate(date);
        th.setUpdateBy(getCurrentUsername());
        return thuongHieuRepo.save(th);
    }

    @Override
    public ThuongHieu getThuongHieu(String ma) {
        return thuongHieuRepo.findByMa(ma);
    }

    @Override
    public ThuongHieu updateTrangThai(Integer idThuongHieu) {
        ThuongHieu th = thuongHieuRepo.findByIdThuongHieu(idThuongHieu);
        if(th.getTrangThai()==true){
            th.setTrangThai(false);
        }
        else{
            th.setTrangThai(true);
        }
        return thuongHieuRepo.save(th);
    }


    @Override
    public ThuongHieu deleteThuongHieu(Integer idThuongHieu) {
        thuongHieuRepo.deleteById(idThuongHieu);
        return null;
    }

    @Override
    public Page<ThuongHieu> search(String query, Pageable pageable) {
        return thuongHieuRepo.searchIgnoreCaseAndDiacritics(query, pageable);
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
