package com.example.demo.rest;

import com.example.demo.Service.ChatLieuService;
import com.example.demo.Service.MauSacService;
import com.example.demo.dto.request.ChatLieuRequestDTO;
import com.example.demo.dto.request.MauSacRequestDTO;
import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.MauSac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatLieuRestController {
    @Autowired
    private ChatLieuService chatLieuService;


    @GetMapping("/admin/chat-lieu/find-all")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatLieu> cl = chatLieuService.findAll(pageable); // Phân trang
        return ResponseEntity.ok(cl); // Trả về trang hiện tại cùng dữ liệu
    }

    @GetMapping("/admin/chat-lieu/get-all")
    public ResponseEntity<?> getAll() {
        List<ChatLieu> cl = chatLieuService.getAll();
        return ResponseEntity.ok(cl);
    }

    @PostMapping("/admin/chat-lieu/add")
    public ResponseEntity<?> createChatLieu(@RequestBody ChatLieuRequestDTO chatLieuRequestDTO) {
        chatLieuService.createChatLieu(chatLieuRequestDTO);
        return ResponseEntity.ok(chatLieuRequestDTO);
    }

    @GetMapping("/admin/chat-lieu/chiTiet/{ma}")
    public ResponseEntity<?> getChatLieu(@PathVariable("ma") String ma) {
        ChatLieu cl = chatLieuService.getChatLieu(ma);
        return ResponseEntity.ok(cl);
    }

    @PostMapping("/admin/chat-lieu/update/{ma}")
    public ResponseEntity<?> updateChatLieu(@RequestBody ChatLieuRequestDTO chatLieuRequestDTO) {
        chatLieuService.updateChatLieu(chatLieuRequestDTO);
        return ResponseEntity.ok(chatLieuRequestDTO);
    }

    @PostMapping("/admin/chat-lieu/updateTT/{idChatLieu}")
    public ResponseEntity<?> updateTrangThai(@PathVariable("idChatLieu") Integer idChatLieu) {
        chatLieuService.updateTrangThai(idChatLieu);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/admin/chat-lieu/delete/{idChatLieu}")
    public ResponseEntity<?> ChatLieu(@PathVariable("idChatLieu") Integer idChatLieu) {
        chatLieuService.deleteChatLieu(idChatLieu);
        return ResponseEntity.ok("");
    }


}
