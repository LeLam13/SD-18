package com.example.demo.dto.request;

import com.example.demo.Service.ThuongHieuService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThuongHieuRequestDTO {
    private Integer idThuongHieu;
    private String ma;
    private String ten;
    private Date createDate;
    private Date updateDate;

}
