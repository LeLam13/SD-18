package com.example.demo.dto.request;



import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class DonHangRequestDTO {
    private Integer idDonHang;

    private String tenKhachNhan;

    private String soDienThoaiKhachNhan;

    private String diaChiKhachNhan;

    private String maDonHang;

    private Float tongTien;

    private Float tongTienKhuyenMai;

    private Float tongTienSauKhuyenMai;

    private String ghiChu;

    private Boolean trangThaiThanhToan;

    private Integer loaiDonHang;
    private Integer phuongThucNhan;

    private Integer idNhanVien;

    private Integer idKhachHang;

    private Integer idTrangThai;

    private Integer idPhuongThucThanhToan;

    private Integer idKhuyenMai;
}
