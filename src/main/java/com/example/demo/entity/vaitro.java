package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="vai_tro")
@Data
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class vaitro {
    @Id String ma;
    String ten;
}
