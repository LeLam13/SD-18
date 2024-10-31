package com.example.demo.Service;

import com.example.demo.dto.request.ChatLieuRequestDTO;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatLieuService {

    public List<ChatLieu> getAll();

    public Page<ChatLieu> findAll(Pageable pageable);

    public ChatLieu createChatLieu(ChatLieuRequestDTO chatLieuRequestDTO);

    public ChatLieu updateChatLieu(ChatLieuRequestDTO chatLieuRequestDTO);

    public ChatLieu getChatLieu(String ma);

    public ChatLieu updateTrangThai(Integer idChatLieu);

    public ChatLieu deleteChatLieu(Integer idChatLieu);

}
