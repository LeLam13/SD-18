package com.example.demo.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ShippingFeeResponseDTO {
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("total")
    private Integer total_fee;//tổng phí dịch vụ
    @JsonProperty("insurance_fee")
    private Integer insurance_fee;//Phí khai giá hàng hóa
    @JsonProperty("service_fee")
    private Integer service_fee;//Phí dịch vụ.
    private ShippingFeeResponseDTO data;
}
