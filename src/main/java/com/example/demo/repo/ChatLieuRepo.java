package com.example.demo.repo;

import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatLieuRepo extends JpaRepository<ChatLieu,Integer> {
    ChatLieu findByMa(String ma);

    ChatLieu findByIdChatLieu(Integer idChatLieu);

    @Query("SELECT m FROM ChatLieu m WHERE " +
            "LOWER(REPLACE(m.ma, 'đ', 'd')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'đ', 'd')) OR " +
            "LOWER(REPLACE(m.ten, 'đ', 'd')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'đ', 'd'))")
    Page<ChatLieu> searchIgnoreCaseAndDiacritics(@Param("query") String query, Pageable pageable);
}
