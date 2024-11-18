package com.example.demo.Service.impl;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.khachhang;
import com.example.demo.repo.HoaDonChiTietRepo;
import com.example.demo.repo.HoaDonRepo;
import com.lowagie.text.DocumentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfGState;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class HoaDonChiTietService {
    @Autowired
    private HoaDonRepo hoaDonRepo;
    @Autowired
    private HoaDonChiTietRepo hoaDonChiTietRepo;

    public Map<String, Object> findDetailsByHoaDonId(Integer id) {
        Map<String, Object> result = new HashMap<>();

        // Lấy thông tin hóa đơn
        HoaDon hoaDon = hoaDonRepo.findById(id).orElse(null);
        if (hoaDon == null) {
            throw new RuntimeException("Hóa đơn không tồn tại với ID: " + id);
        }

        // Lấy thông tin khách hàng từ bảng KhachHang
        khachhang khachHang = hoaDon.getKhachHang(); // Lấy thông tin khách hàng từ hóa đơn

        // Chuẩn bị thông tin hóa đơn chỉ với các trường cần thiết
        Map<String, Object> hoaDonMap = new HashMap<>();
        hoaDonMap.put("maHoaDon", hoaDon.getMaHoaDon());
        hoaDonMap.put("tenKhachNhan", khachHang != null ? khachHang.getHoTen() : null); // Tên khách từ KhachHang
        hoaDonMap.put("emailKhachNhan", khachHang != null ? khachHang.getEmail() : null); // Email khách
        hoaDonMap.put("soDienThoaiKhachNhan", khachHang != null ? khachHang.getSoDienThoai() : null); // Số điện thoại
        // khách
        hoaDonMap.put("diaChiNhan", khachHang != null ? khachHang.getDiaChi() : null); // Địa chỉ khách
        hoaDonMap.put("tongTien", hoaDon.getTongTien());
        hoaDonMap.put("trangThaiThanhToan", hoaDon.getTrangThaiThanhToan());

        // Lấy danh sách chi tiết hóa đơn
        List<Map<String, Object>> chiTietHoaDonList = new ArrayList<>();
        List<HoaDonChiTiet> listHDCT = hoaDonChiTietRepo.findById1(id);

        // Chuyển đổi chi tiết hóa đơn thành map
        for (HoaDonChiTiet chiTiet : listHDCT) {
            Map<String, Object> chiTietMap = new HashMap<>();
            chiTietMap.put("sanPhamTen", chiTiet.getSanPhamChiTiet().getIdSanPham().getTen());
            chiTietMap.put("maSanPhamChiTiet", chiTiet.getSanPhamChiTiet().getMa());
            chiTietMap.put("chatLieu", chiTiet.getSanPhamChiTiet().getIdChatLieu().getTen());
            chiTietMap.put("kichCo", chiTiet.getSanPhamChiTiet().getIdKichCo().getTen());
            chiTietMap.put("soLuong", chiTiet.getSoLuong());
            chiTietMap.put("donGia", chiTiet.getDonGia());
            chiTietMap.put("hinhAnh", chiTiet.getSanPhamChiTiet().getIdHinhAnh().getTen());
            chiTietHoaDonList.add(chiTietMap);
        }

        // Đưa dữ liệu vào map kết quả
        result.put("hoaDon", hoaDonMap);
        result.put("chiTietHoaDon", chiTietHoaDonList);

        return result;
    }

    public byte[] generateInvoicePdf(Integer idHoaDon, String customerName, String companyName, String taxCode,
                                     String address, String paymentMethod) throws DocumentException, IOException, com.itextpdf.text.DocumentException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            // Lấy thông tin hóa đơn và chi tiết hóa đơn
            HoaDon hoaDon = hoaDonRepo.findById(idHoaDon)
                    .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));
            List<HoaDonChiTiet> chiTietList = hoaDonChiTietRepo.findById1(idHoaDon);

            // Tạo tài liệu PDF
            Document document = new Document(PageSize.A4, 20, 20, 20, 20);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Font hỗ trợ Unicode
            BaseFont baseFont = BaseFont.createFont(
                    getClass().getClassLoader().getResource("fonts/times.ttf").toString(),
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font titleFont = new Font(baseFont, 16, Font.BOLD, BaseColor.RED);
            Font headerFont = new Font(baseFont, 12, Font.BOLD, BaseColor.BLACK);
            Font normalFont = new Font(baseFont, 10, Font.NORMAL, BaseColor.BLACK);
            Font boldFont = new Font(baseFont, 10, Font.BOLD, BaseColor.BLACK);

            // **Thêm logo làm background**
            PdfContentByte canvas = writer.getDirectContentUnder();
            Image logo = Image.getInstance(getClass().getClassLoader().getResource("images/img.png").toString());
            logo.setAbsolutePosition(150, 300); // Vị trí logo (x, y)
            logo.scaleToFit(300, 300); // Kích thước logo
            PdfGState gState = new PdfGState();
            gState.setFillOpacity(0.1f); // Độ mờ của logo (10%)
            canvas.setGState(gState);
            canvas.addImage(logo);

            // Tiêu đề hóa đơn
            Paragraph title = new Paragraph("HÓA ĐƠN GIÁ TRỊ GIA TĂNG\nVAT INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);

            // Thông tin công ty
            PdfPTable companyInfoTable = new PdfPTable(2);
            companyInfoTable.setWidthPercentage(100);
            companyInfoTable.setWidths(new int[] { 7, 3 });

            companyInfoTable.addCell(createCell("Mã số thuế (Tax code): 0316121804", normalFont));
            companyInfoTable.addCell(createCell("Ký hiệu (Serial): 1C24TTB", normalFont, Element.ALIGN_RIGHT));
            companyInfoTable.addCell(createCell("CÔNG TY TNHH TINH ÁO TUỔI MƯỜI TÁM", boldFont, Element.ALIGN_LEFT, 2));
            companyInfoTable.addCell(createCell(
                    "Địa chỉ (Address): 1197/9 Gò Nổi, Phường Phú Hữu, Quận Hoàn Kiếm, TP. Hà Nội, Việt Nam",
                    normalFont, Element.ALIGN_LEFT, 2));
            companyInfoTable.addCell(createCell("Điện thoại (Tel): 0937 005 105", normalFont));
            companyInfoTable.addCell(createCell("Số (No): " + hoaDon.getMaHoaDon(), normalFont, Element.ALIGN_RIGHT));
            companyInfoTable.addCell(createCell(
                    "Số tài khoản (A/c No.): 10815397 Tại Ngân hàng thương mại Á Châu (ACB) - Phòng Giao Dịch An Phú",
                    normalFont, Element.ALIGN_LEFT, 2));
            document.add(companyInfoTable);

            // Thông tin khách hàng
            PdfPTable customerInfoTable = new PdfPTable(2);
            customerInfoTable.setWidthPercentage(100);
            customerInfoTable.setWidths(new int[] { 3, 7 });

            customerInfoTable.addCell(createCell("Họ tên người mua hàng (Customer's name):", boldFont));
            customerInfoTable.addCell(createCell(customerName, normalFont));
            customerInfoTable.addCell(createCell("Tên đơn vị (Company):", boldFont));
            customerInfoTable.addCell(createCell(companyName, normalFont));
            customerInfoTable.addCell(createCell("Mã số thuế (Tax code):", boldFont));
            customerInfoTable.addCell(createCell(taxCode, normalFont));
            customerInfoTable.addCell(createCell("Địa chỉ (Address):", boldFont));
            customerInfoTable.addCell(createCell(address, normalFont));
            customerInfoTable.addCell(createCell("Hình thức thanh toán (Payment method):", boldFont));
            customerInfoTable.addCell(createCell(paymentMethod, normalFont));
            document.add(customerInfoTable);

            // Bảng chi tiết sản phẩm
            PdfPTable detailsTable = new PdfPTable(6);
            detailsTable.setWidthPercentage(100);
            detailsTable.setSpacingBefore(10);
            detailsTable.setWidths(new int[] { 1, 4, 2, 2, 2, 3 });

            detailsTable.addCell(createCell("STT", headerFont, Element.ALIGN_CENTER));
            detailsTable.addCell(createCell("Tên hàng hóa, dịch vụ (Description)", headerFont, Element.ALIGN_CENTER));
            detailsTable.addCell(createCell("Đơn vị tính (Unit)", headerFont, Element.ALIGN_CENTER));
            detailsTable.addCell(createCell("Số lượng (Quantity)", headerFont, Element.ALIGN_CENTER));
            detailsTable.addCell(createCell("Đơn giá (Unit Price)", headerFont, Element.ALIGN_CENTER));
            detailsTable.addCell(createCell("Thành tiền (Amount)", headerFont, Element.ALIGN_CENTER));

            int stt = 1;
            double totalAmount = 0;
            for (HoaDonChiTiet chiTiet : chiTietList) {
                double amount = chiTiet.getSoLuong() * chiTiet.getDonGia();
                totalAmount += amount;

                detailsTable.addCell(createCell(String.valueOf(stt++), normalFont, Element.ALIGN_CENTER));
                detailsTable.addCell(createCell(chiTiet.getSanPhamChiTiet().getIdSanPham().getTen(), normalFont));
                detailsTable.addCell(createCell("Cái", normalFont, Element.ALIGN_CENTER));
                detailsTable
                        .addCell(createCell(String.valueOf(chiTiet.getSoLuong()), normalFont, Element.ALIGN_CENTER));
                detailsTable.addCell(
                        createCell(String.format("%.0f", chiTiet.getDonGia()), normalFont, Element.ALIGN_RIGHT));
                detailsTable.addCell(createCell(String.format("%.0f", amount), normalFont, Element.ALIGN_RIGHT));
            }
            document.add(detailsTable);

            // Tổng cộng
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new int[] { 7, 3 });
            totalTable.addCell(createCell("Tổng cộng (Total):", boldFont, Element.ALIGN_RIGHT));
            totalTable.addCell(createCell(String.format("%.0f VND", totalAmount), boldFont, Element.ALIGN_RIGHT));
            document.add(totalTable);

            // Chữ ký
            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidthPercentage(100);
            signatureTable.setWidths(new int[] { 5, 5 });
            signatureTable.setSpacingBefore(20);
            signatureTable.addCell(createCell("Người mua hàng (Buyer)", normalFont, Element.ALIGN_CENTER));
            signatureTable.addCell(createCell("Người bán hàng (Seller)", normalFont, Element.ALIGN_CENTER));
            signatureTable.addCell(createCell("\n\n(Ký tên, đóng dấu)", normalFont, Element.ALIGN_CENTER));
            signatureTable.addCell(createCell("\n\n(Ký tên, đóng dấu)", normalFont, Element.ALIGN_CENTER));
            document.add(signatureTable);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    // Hàm hỗ trợ tạo cell trong bảng
    private PdfPCell createCell(String content, Font font) {
        return createCell(content, font, Element.ALIGN_LEFT);
    }

    private PdfPCell createCell(String content, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private PdfPCell createCell(String content, Font font, int alignment, int colspan) {
        PdfPCell cell = createCell(content, font, alignment);
        cell.setColspan(colspan);
        return cell;
    }
}
