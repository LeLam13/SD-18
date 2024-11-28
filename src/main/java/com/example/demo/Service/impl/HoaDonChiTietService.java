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
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfGState;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;

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
//            chiTietMap.put("chatLieu", chiTiet.getSanPhamChiTiet().getIdChatLieu().getTen());
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

    public byte[] generateInvoicePdf(Integer idHoaDon)
            throws DocumentException, IOException, com.itextpdf.text.DocumentException {
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
            Font italicFont = new Font(baseFont, 10, Font.ITALIC, BaseColor.BLACK);

            // **Thêm logo góc trên bên trái cùng 1 hàng với tiêu đề**
            Image logoLeft = Image.getInstance(getClass().getClassLoader().getResource("images/img.png").toString());
            logoLeft.scaleToFit(50, 50); // Kích thước logo (chiều rộng, chiều cao)
            logoLeft.setAlignment(Image.ALIGN_LEFT);

            // **Thêm logo làm background**
            PdfContentByte canvas = writer.getDirectContentUnder();
            Image logo = Image.getInstance(getClass().getClassLoader().getResource("images/img.png").toString());
            logo.setAbsolutePosition(150, 300); // Vị trí logo (x, y)
            logo.scaleToFit(300, 300); // Kích thước logo
            PdfGState gState = new PdfGState();
            gState.setFillOpacity(0.1f); // Độ mờ của logo (10%)
            canvas.setGState(gState);
            canvas.addImage(logo);

            PdfPTable titleTable = new PdfPTable(3); // Chia thành 3 cột: logo, tiêu đề, và mẫu/ký hiệu/số
            titleTable.setWidthPercentage(100);
            titleTable.setWidths(new int[] { 2, 6, 2 }); // Tỉ lệ các cột

            // Logo
            PdfPCell logoCell = new PdfPCell(logoLeft);
            logoCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            titleTable.addCell(logoCell);

            // Tiêu đề
            PdfPCell titleCell = new PdfPCell();
            titleCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            // Thêm "HÓA ĐƠN GIÁ TRỊ GIA TĂNG"
            Paragraph titleParagraph = new Paragraph("HÓA ĐƠN GIÁ TRỊ GIA TĂNG", titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            titleCell.addElement(titleParagraph);

            // Thêm "VAT INVOICE"
            Paragraph vatInvoiceParagraph = new Paragraph("VAT INVOICE",
                    new Font(baseFont, 12, Font.NORMAL, BaseColor.BLACK));
            vatInvoiceParagraph.setAlignment(Element.ALIGN_CENTER);
            titleCell.addElement(vatInvoiceParagraph);

            // Thêm ngày tháng năm
            LocalDate createdDate = hoaDon.getCreateDate();
            String dateText = String.format("Ngày %d Tháng %d Năm %d",
                    createdDate.getDayOfMonth(),
                    createdDate.getMonthValue(),
                    createdDate.getYear());
            Paragraph dateParagraph = new Paragraph(dateText, new Font(baseFont, 10, Font.NORMAL, BaseColor.BLACK));
            dateParagraph.setAlignment(Element.ALIGN_CENTER);
            titleCell.addElement(dateParagraph);

            // Thêm tiêu đề vào bảng
            titleTable.addCell(titleCell);

            // Mẫu số, ký hiệu và số
            PdfPTable serialTable = new PdfPTable(1); // Bảng lồng để hiển thị mẫu số, ký hiệu và số
            serialTable.addCell(createNoBorderCell("Mẫu số: 01GTKT0/001", normalFont));
            serialTable.addCell(createNoBorderCell("Ký hiệu: TS/20E", normalFont));
            serialTable.addCell(createNoBorderCell("Số: 00000" + hoaDon.getMaHoaDon(), normalFont));
            PdfPCell serialCell = new PdfPCell(serialTable);
            serialCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            serialCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            titleTable.addCell(serialCell);

            // Thêm bảng tiêu đề vào tài liệu
            document.add(titleTable);

            // **Thông tin công ty**
            PdfPTable companyInfoTable = new PdfPTable(1); // Thông tin công ty hiển thị trong 1 ô duy nhất
            companyInfoTable.setWidthPercentage(100);
            companyInfoTable.setSpacingBefore(10); // Khoảng cách trước box thông tin công ty
            PdfPCell companyInfoCell = new PdfPCell();
            companyInfoCell.addElement(new Phrase("CÔNG TY TNHH ÁO POLO NAM MƯỜI TÁM", boldFont));
            companyInfoCell.addElement(new Phrase("Mã số thuế: 0101300842", normalFont));
            companyInfoCell.addElement(
                    new Phrase(
                            "Địa chỉ: Số 123, Phố Trịnh Văn Bô, Phường Phương Canh, Quận Nam Từ Liêm, Thành phố Hà Nội, Việt Nam",
                            normalFont));
            companyInfoCell.addElement(new Phrase("Điện thoại: 024.3754 5222 - Fax: 024.3754 5223", normalFont));
            companyInfoCell.addElement(new Phrase(
                    "Số tài khoản: 0491001474862 tại Ngân hàng TMCP Ngoại Thương, CN Thăng Long - Hà Nội", normalFont));
            companyInfoCell.setBorder(PdfPCell.BOX); // Có viền xung quanh
            companyInfoCell.setPadding(10); // Khoảng cách bên trong
            companyInfoTable.addCell(companyInfoCell);
            document.add(companyInfoTable);

            // **Thông tin khách hàng**
            PdfPTable customerInfoTable = new PdfPTable(1); // Thông tin khách hàng hiển thị trong 1 ô duy nhất
            customerInfoTable.setWidthPercentage(100);
            customerInfoTable.setSpacingBefore(10); // Khoảng cách trước box thông tin khách hàng

            PdfPCell customerInfoCell = new PdfPCell();
            customerInfoCell.setBorder(PdfPCell.BOX); // Có viền xung quanh
            customerInfoCell.setPadding(10); // Khoảng cách bên trong
            customerInfoCell.addElement(new Phrase(
                    "Họ tên người mua hàng (Customer's name): " + hoaDon.getKhachHang().getHoTen(), normalFont));
            customerInfoCell.addElement(
                    new Phrase("Số điện thoại (Phone number): " + hoaDon.getKhachHang().getSoDienThoai(), normalFont));
            customerInfoCell
                    .addElement(new Phrase("Địa chỉ (Address): " + hoaDon.getKhachHang().getDiaChi(), normalFont));
            String paymentMethod = hoaDon.getTrangThaiThanhToan() ? "Chuyển khoản" : "Tiền mặt";
            customerInfoCell
                    .addElement(new Phrase("Hình thức thanh toán (Payment method): " + paymentMethod, normalFont));

            // Thêm ô thông tin khách hàng vào bảng
            customerInfoTable.addCell(customerInfoCell);

            // Thêm bảng thông tin khách hàng vào tài liệu
            document.add(customerInfoTable);

            // **Bảng chi tiết sản phẩm**
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
            NumberFormat currencyFormat = NumberFormat.getInstance();
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
                        createCell(currencyFormat.format(chiTiet.getDonGia()), normalFont, Element.ALIGN_RIGHT));
                detailsTable.addCell(createCell(currencyFormat.format(amount), normalFont, Element.ALIGN_RIGHT));

            }
            document.add(detailsTable);

            // **Tổng cộng và thuế GTGT**
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new int[] { 7, 3 });

            // Tạo NumberFormat cho định dạng tiền tệ

            currencyFormat.setGroupingUsed(true); // Bật tính năng ngăn cách hàng nghìn

            totalTable.addCell(createCell("Cộng tiền hàng (Total):", boldFont, Element.ALIGN_RIGHT));
            totalTable.addCell(createCell(currencyFormat.format(totalAmount), normalFont, Element.ALIGN_RIGHT));

            double vatAmount = totalAmount * 0.1; // Thuế suất 8%
            totalTable.addCell(createCell("Thuế suất GTGT (VAT rate): 10%", boldFont, Element.ALIGN_RIGHT));
            totalTable.addCell(createCell(currencyFormat.format(vatAmount), normalFont, Element.ALIGN_RIGHT));

            double totalPayment = totalAmount + vatAmount;
            totalTable.addCell(createCell("Tổng cộng tiền thanh toán (Total payment):", boldFont, Element.ALIGN_RIGHT));
            totalTable.addCell(createCell(currencyFormat.format(totalPayment), normalFont, Element.ALIGN_RIGHT));

            document.add(totalTable);
            PdfContentByte canva = writer.getDirectContent();
            Font italicFontDes = new Font(baseFont, 10, Font.ITALIC, BaseColor.BLACK);

            // Thiết lập màu cho canvas là màu đen
            canva.setColorFill(BaseColor.BLACK);

            // Tạo nội dung lưu ý
            Phrase notePhrase = new Phrase("Theo form mẫu của Bộ Tài chính - Mẫu số 01GTKT0/001", italicFontDes);

            // Xác định vị trí của dòng chữ (góc dưới cùng bên phải)
            ColumnText.showTextAligned(
                    canva,
                    Element.ALIGN_RIGHT, // Căn phải
                    notePhrase,
                    document.right(), // Tọa độ x (cạnh phải tài liệu)
                    document.bottom() - 10, // Tọa độ y (dưới phần nội dung chính 10px)
                    0 // Góc xoay (0 độ)
            );
            // Tạo bảng chữ ký
            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidthPercentage(100);
            signatureTable.setWidths(new int[] { 5, 5 });
            signatureTable.setSpacingBefore(20);

            // Người mua hàng
            PdfPCell buyerCell = new PdfPCell();
            buyerCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            buyerCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Căn giữa ngang
            buyerCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Căn giữa dọc

            // Thêm dòng "Người mua hàng" (in đậm)
            Paragraph buyerTitle = new Paragraph("Người mua hàng", boldFont);
            buyerTitle.setAlignment(Element.ALIGN_CENTER); // Căn giữa ngang
            buyerCell.addElement(buyerTitle);

            // Thêm dòng "(Ký, ghi rõ họ tên)" (in nghiêng)
            Paragraph buyerSignature = new Paragraph("(Ký, ghi rõ họ tên)", italicFont);
            buyerSignature.setAlignment(Element.ALIGN_CENTER); // Căn giữa ngang
            buyerCell.addElement(buyerSignature);

            buyerCell.setFixedHeight(50); // Chiều cao cố định
            signatureTable.addCell(buyerCell);

            // Người bán hàng
            PdfPCell sellerCell = new PdfPCell();
            sellerCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            sellerCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Căn giữa ngang
            sellerCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Căn giữa dọc

            // Thêm dòng "Người bán hàng" (in đậm)
            Paragraph sellerTitle = new Paragraph("Người bán hàng", boldFont);
            sellerTitle.setAlignment(Element.ALIGN_CENTER); // Căn giữa ngang
            sellerCell.addElement(sellerTitle);

            // Thêm dòng "(Ký, ghi rõ họ tên)" (in nghiêng)
            Paragraph sellerSignature = new Paragraph("(Ký, ghi rõ họ tên)", italicFont);
            sellerSignature.setAlignment(Element.ALIGN_CENTER); // Căn giữa ngang
            sellerCell.addElement(sellerSignature);

            sellerCell.setFixedHeight(50); // Chiều cao cố định
            signatureTable.addCell(sellerCell);

            // Thêm bảng chữ ký vào tài liệu
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

    private PdfPCell createNoBorderCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBorder(Rectangle.NO_BORDER); // Loại bỏ viền
        cell.setPadding(5); // Thêm khoảng cách padding
        cell.setHorizontalAlignment(Element.ALIGN_LEFT); // Căn lề trái (nếu cần)
        return cell;
    }

    private PdfPCell createNoBorderCellWith(String content, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        cell.setBorder(Rectangle.NO_BORDER); // Loại bỏ viền
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

}
