package com.example.demo.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DistrictResponseDTO {
    @JsonProperty("DistrictID")
    private Integer DistrictID;

    @JsonProperty("DistrictName")
    private String DistrictName;

    @JsonProperty("ProvinceID")
    private Integer ProvinceID;

    private List<DistrictResponseDTO> data;
}
