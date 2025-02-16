package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShippingRequest {
    private Integer service_type_id;
    @JsonProperty("from_district_id")
    private Integer fromDistrictId ;//3440
    @JsonProperty("from_ward_code")
    private String  from_ward_code;
//    private Integer toProvinceId;
    @JsonProperty("to_district_id")
    private Integer toDistrictId;
    @JsonProperty("to_ward_code")
    private String toWardCode;
    private Integer weight;
}
