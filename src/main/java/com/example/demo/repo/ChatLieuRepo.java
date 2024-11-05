package com.example.demo.repo;

import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatLieuRepo extends JpaRepository<ChatLieu,Integer> {
    ChatLieu findByMa(String ma);

    ChatLieu findByIdChatLieu(Integer idChatLieu);
}
