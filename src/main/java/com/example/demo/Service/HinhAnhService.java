package com.example.demo.Service;

import com.example.demo.dto.request.HinhAnhRequestDTO;
import com.example.demo.entity.HinhAnh;
import org.springframework.stereotype.Service;

@Service
public interface HinhAnhService {
    public HinhAnh findbyTen(String ten);

    public HinhAnh createHinhAnh(HinhAnhRequestDTO hinhAnhRequestDTO);
}
