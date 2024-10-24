package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="tai_khoan")
//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class taikhoan {
    @Id
    String username;
    String password;
    String email;
    boolean trangthai;

    @ManyToOne
    @JoinColumn(name = "role") // Khóa ngoại tới bảng vai trò
    private vaitro vaiTro;
    @JsonIgnore
    @OneToOne(mappedBy = "taikhoan") // Ánh xạ ngược lại đến NhanVien
    private nhanvien nhanVien;
    @JsonIgnore
    @OneToOne(mappedBy = "taikhoan") // Ánh xạ ngược lại đến KhachHang
    private khachhang khachHang;

}
