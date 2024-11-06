package com.example.demo.Service.impl;

import com.example.demo.Service.ChatLieuService;
import com.example.demo.dto.request.ChatLieuRequestDTO;
import com.example.demo.entity.ChatLieu;
import com.example.demo.repo.ChatLieuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<ChatLieu> findAll(Pageable pageable) {
        return chatLieuRepo.findAll(pageable);
    }

    @Override
    public ChatLieu createChatLieu(ChatLieuRequestDTO chatLieuRequestDTO) {
        ChatLieu cl = new ChatLieu();
        cl.setMa(chatLieuRequestDTO.getMa());
        cl.setTen(chatLieuRequestDTO.getTen());
        cl.setCreateDate(date);
        cl.setTrangThai(true);
        return chatLieuRepo.save(cl);
    }

    @Override
    public ChatLieu updateChatLieu(ChatLieuRequestDTO chatLieuRequestDTO) {
        ChatLieu cl = chatLieuRepo.findByMa(chatLieuRequestDTO.getMa());
        cl.setTen(chatLieuRequestDTO.getTen());
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


}
