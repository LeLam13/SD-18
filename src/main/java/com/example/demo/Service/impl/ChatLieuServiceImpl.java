package com.example.demo.Service.impl;

import com.example.demo.Service.ChatLieuService;
import com.example.demo.dto.request.ChatLieuRequestDTO;
import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.XuatXu;
import com.example.demo.repo.ChatLieuRepo;
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
public class ChatLieuServiceImpl implements ChatLieuService {
    @Autowired
    private ChatLieuRepo chatLieuRepo;

    Date date = new Date();

    @Override
    public List<ChatLieu> getAll() {
        return chatLieuRepo.findAll();
    }

    @Override
    public List<ChatLieu> getAllByTT() {
        return chatLieuRepo.getAllByTT();
    }

    @Override
    public Page<ChatLieu> findAll(Pageable pageable) {
        return chatLieuRepo.findAll(pageable);
    }

    @Override
    public ChatLieu createChatLieu(ChatLieuRequestDTO chatLieuRequestDTO) {
        ChatLieu cl = new ChatLieu();
        cl.setMa(chatLieuRequestDTO.getMa());
        cl.setTen(chatLieuRequestDTO.getTen());
        cl.setCreateDate(date);
        cl.setCreateBy(getCurrentUsername());
        cl.setTrangThai(true);
        return chatLieuRepo.save(cl);
    }

    @Override
    public ChatLieu updateChatLieu(ChatLieuRequestDTO chatLieuRequestDTO) {
        ChatLieu cl = chatLieuRepo.findByMa(chatLieuRequestDTO.getMa());
        cl.setTen(chatLieuRequestDTO.getTen());
        cl.setUpdateBy(getCurrentUsername());
        cl.setUpdateDate(date);
        return chatLieuRepo.save(cl);
    }

    @Override
    public ChatLieu getChatLieu(String ma) {
        return chatLieuRepo.findByMa(ma);
    }

    @Override
    public ChatLieu updateTrangThai(Integer idChatLieu) {
        ChatLieu cl = chatLieuRepo.findByIdChatLieu(idChatLieu);
        if(cl.getTrangThai()==true){
            cl.setTrangThai(false);
        }
        else{
            cl.setTrangThai(true);
        }
        return chatLieuRepo.save(cl);
    }


    @Override
    public ChatLieu deleteChatLieu(Integer idChatLieu) {
        chatLieuRepo.deleteById(idChatLieu);
        return null;
    }

    @Override
    public Page<ChatLieu> search(String query, Pageable pageable) {
        return chatLieuRepo.searchIgnoreCaseAndDiacritics(query, pageable);
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
