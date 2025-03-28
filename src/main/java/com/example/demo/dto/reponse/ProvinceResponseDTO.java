package com.example.demo.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProvinceResponseDTO {
    @JsonProperty("ProvinceID")
    private Integer ProvinceID;
    @JsonProperty("ProvinceName")
    private String ProvinceName;
    private List<ProvinceResponseDTO> data;
}
