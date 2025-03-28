package com.example.demo.dto.request;

import lombok.Data;

import java.util.List;
@Data
public class AddProductsToDiscountRequest {
    private Integer idGiamGia;
    private List<Integer> productIds;
}
