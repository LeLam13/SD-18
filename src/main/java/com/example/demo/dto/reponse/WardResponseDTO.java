package com.example.demo.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WardResponseDTO {
    @JsonProperty("WardCode")
    private String WardCode;

    @JsonProperty("WardName")
    private String WardName;

    @JsonProperty("DistrictID")
    private Integer DistrictID;

    private List<WardResponseDTO> data;
}
