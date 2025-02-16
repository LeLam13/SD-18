package com.example.demo.repo;

import com.example.demo.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HoaDonRepo extends JpaRepository<HoaDon, Integer> {

    List<HoaDon> findByKhachHang_IdKhachHang(Integer idKhachHang);

    // @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = true")
    // Page<HoaDon> findAllByTrangThaiThanhToan(Pageable pageable);

    @Query("SELECT h FROM HoaDon h")
    Page<HoaDon> findAllByTrangThaiThanhToan(Pageable pageable);

    @Query(value = "SELECT * FROM hoa_don WHERE ma_hoa_don LIKE %:maHoaDon%", nativeQuery = true)
    List<HoaDon> findByMaHoaDonContaining(String maHoaDon);

    @Query("SELECT h FROM HoaDon h " +
            "LEFT JOIN h.khachHang kh " +
            "LEFT JOIN h.donHang dh " +
            "WHERE " +
            "(:keyword IS NULL OR h.maHoaDon LIKE %:keyword% " +
            "OR h.tenKhachNhan LIKE %:keyword% " +
            "OR h.emailKhachNhan LIKE %:keyword% " +
            "OR h.soDienThoaiKhachNhan LIKE %:keyword% " +
            "OR kh.hoTen LIKE %:keyword% " +
            "OR kh.email LIKE %:keyword% " +
            "OR kh.soDienThoai LIKE %:keyword% " +
            "OR dh.maDonHang LIKE %:keyword% " +
            "OR dh.tenKhachNhan LIKE %:keyword% " +
            "OR dh.emailKhachNhan LIKE %:keyword% " +
            "OR dh.soDienThoaiKhachNhan LIKE %:keyword% " +
            ") AND " +
            "(h.trangThaiThanhToan = :filterTrangThaiThanhToan OR :filterTrangThaiThanhToan IS NULL) AND " +
            "(h.phuongThucNhan = :filterLoaiDonHang OR :filterLoaiDonHang IS NULL)")
    List<HoaDon> searchFilterHoaDons(
            @Param("keyword") String keyword,
            @Param("filterTrangThaiThanhToan") Boolean filterTrangThaiThanhToan,
            @Param("filterLoaiDonHang") Integer filterLoaiDonHang);

    HoaDon findByMaHoaDon(String maHoaDon);

    @Query("SELECT h FROM HoaDon h WHERE h.donHang.idDonHang = :idDonHang")
    HoaDon findByDonHangId(@Param("idDonHang") Integer idDonHang);

    // Tổng doanh thu
    @Query("SELECT COALESCE(SUM(h.tongTienSauKhuyenMai), 0) FROM HoaDon h")
    Float calculateTotalRevenue();

    // Doanh thu theo ngày hiện tại
    @Query(value = "SELECT COALESCE(SUM(tong_tien_sau_khuyen_mai), 0) FROM hoa_don WHERE CAST(create_date AS DATE) = CAST(GETDATE() AS DATE)", nativeQuery = true)
    Float calculateRevenueToday();

    // Doanh thu ngày hôm qua
    @Query(value = "SELECT COALESCE(SUM(tong_tien_sau_khuyen_mai), 0) FROM hoa_don WHERE CAST(create_date AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE)", nativeQuery = true)
    Float calculateRevenueYesterday();

    // Doanh thu theo tháng hiện tại
    @Query(value = "SELECT COALESCE(SUM(tong_tien_sau_khuyen_mai), 0) FROM hoa_don WHERE YEAR(create_date) = YEAR(GETDATE()) AND MONTH(create_date) = MONTH(GETDATE())", nativeQuery = true)
    Float calculateRevenueThisMonth();

    // Doanh thu theo năm hiện tại
    @Query(value = "SELECT COALESCE(SUM(tong_tien_sau_khuyen_mai), 0) FROM hoa_don WHERE YEAR(create_date) = YEAR(GETDATE())", nativeQuery = true)
    Float calculateRevenueThisYear();

    // Doanh thu tháng trước
    @Query(value = "SELECT COALESCE(SUM(tong_tien_sau_khuyen_mai), 0) FROM hoa_don WHERE YEAR(create_date) = YEAR(DATEADD(MONTH, -1, GETDATE())) AND MONTH(create_date) = MONTH(DATEADD(MONTH, -1, GETDATE()))", nativeQuery = true)
    Float calculateRevenueLastMonth();

    // Doanh thu năm trước
    @Query(value = "SELECT COALESCE(SUM(tong_tien_sau_khuyen_mai), 0) FROM hoa_don WHERE YEAR(create_date) = YEAR(DATEADD(YEAR, -1, GETDATE()))", nativeQuery = true)
    Float calculateRevenueLastYear();

    // Dữ liệu doanh thu theo thời gian cho biểu đồ
    @Query("SELECT FORMAT(h.createDate, 'yyyy-MM-dd') AS date, SUM(h.tongTienSauKhuyenMai) " +
            "FROM HoaDon h GROUP BY FORMAT(h.createDate, 'yyyy-MM-dd') " +
            "ORDER BY FORMAT(h.createDate, 'yyyy-MM-dd')")
    List<Object[]> getRevenueOverTime();

    // Doanh thu trong khoảng thời gian cụ thể
    @Query("SELECT SUM(h.tongTienSauKhuyenMai) FROM HoaDon h WHERE h.createDate BETWEEN :start AND :end GROUP BY h.createDate ORDER BY h.createDate")
    List<Float> findRevenueInRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

    // Ngày trong khoảng thời gian cụ thể
    @Query("SELECT FORMAT(h.createDate, 'yyyy-MM-dd') FROM HoaDon h WHERE h.createDate BETWEEN :start AND :end GROUP BY h.createDate ORDER BY h.createDate")
    List<String> findDatesInRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query(value = """
            SELECT TOP 5 sp.ten, SUM(hdc.so_luong) as total_quantity
            FROM hoa_don hd
            JOIN hoa_don_chi_tiet hdc ON hd.id_hoa_don = hdc.id_hoa_don
            JOIN san_pham_chi_tiet spct ON hdc.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
            JOIN san_pham sp ON spct.id_san_pham = sp.id_san_pham
            WHERE hd.create_date >= DATEADD(MONTH, -1, GETDATE())
            GROUP BY sp.ten
            ORDER BY total_quantity DESC
            """, nativeQuery = true)
    List<Object[]> findTopSellingProducts();

    // Tính tỷ lệ tăng trưởng doanh thu theo ngày
    default Float calculateDailyGrowthRate() {
        Float revenueToday = calculateRevenueToday();
        Float revenueYesterday = calculateRevenueYesterday();

        if (revenueYesterday != null && revenueYesterday > 0) {
            return ((revenueToday - revenueYesterday) / revenueYesterday) * 100;
        } else {
            return 0.0f; // Trường hợp không có doanh thu hôm qua hoặc là 0
        }
    }
}
