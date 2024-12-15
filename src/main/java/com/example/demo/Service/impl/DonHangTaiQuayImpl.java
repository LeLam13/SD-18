package com.example.demo.Service.impl;

import com.example.demo.Service.DonHangTaiQuayService;
import com.example.demo.dto.request.DonHangTaiQuayStatusRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repo.*;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class DonHangTaiQuayImpl implements DonHangTaiQuayService {
    @Autowired
    DonHangRepo donHangRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @Autowired
    SanPhamChiTietRepo sanPhamChiTietRepo;
    @Autowired
    TrangThaiRepo trangThaiRepo;
    @Autowired
    taikhoanRepo taikhoanRepo;
    @Autowired
    NhanVienRepo nhanVienRepo;
    @Autowired
    KhuyenMaiRepo khuyenMaiRepo;
    @Autowired
    HoaDonRepo hoaDonRepo;
    @Autowired
    HoaDonChiTietRepo hoaDonChiTietRepo;

    @Override
    public List<DonHang> getAllOrder() {
        //List<DonHang>  donHang = donHangRepo.findAll();
        List<DonHang> donHang = donHangRepo.findAll(Sort.by(Sort.Direction.DESC, "idDonHang"));
        return donHang;
    }

    @Override
    public List<DonHangChiTiet> getOrderDetailById(Integer id) {
        DonHang donHang = donHangRepo.findById(id).get();
        if(donHang == null){
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
        List<DonHangChiTiet> donHangChiTiet = donHangChiTietRepo.findByDonHangId(id);
        if(donHangChiTiet == null){
            throw new RuntimeException("Đơn hàng không có đơn hàng chi tiết!");
        }
        return donHangChiTiet;
    }

    @Override
    public DonHang updateOrderStatus(DonHangTaiQuayStatusRequestDTO donHangStatus, String username) {
        DonHang oldOrder = donHangRepo.findById(donHangStatus.getIdDonHang()).get();
        HoaDon hoaDon = hoaDonRepo.findByDonHangId(donHangStatus.getIdDonHang());

        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
//            System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());
            nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
            if(getNV != null){
                oldOrder.setNhanVien(getNV);
                oldOrder.setUpdateBy(getNV.getHoTen());
                hoaDon.setUpdateBy(getNV.getHoTen());
            }
        }

        if(oldOrder == null){
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
        TrangThai status = trangThaiRepo.findById(donHangStatus.getIdTrangThai()).get();
//        if(donHangStatus.getIdTrangThai() < 5){
//            status = trangThaiRepo.findById(donHangStatus.getIdTrangThai()+1).get();
//        }
        if(status.getIdTrangThai() ==5){
            hoaDon.setTrangThaiThanhToan(true);
            oldOrder.setTrangThaiThanhToan(true);
        }
        LocalDate localDate = LocalDate.now();
        hoaDon.setUpdateDate(localDate);
        hoaDon.setTrangThai(status);
        hoaDonRepo.save(hoaDon);

        oldOrder.setUpdateDate(localDate);
        oldOrder.setTrangThai(status);
        donHangRepo.save(oldOrder);
        return oldOrder;
    }

    @Override
    public List<DonHang> searchMaDonhang(String maHD) {
        return donHangRepo.searchByMaDonHang(maHD);
    }

    @Override
    public List<DonHang> searchLoaiDonhang(Integer loaiDonHang) {
        return donHangRepo.findDonHangByLoaiDonHangNative(loaiDonHang);
    }

    @Override
    public List<DonHang> searchNgayTao(LocalDate startDate, LocalDate endDate) {
        return donHangRepo.findDonHangByDateRangeNative(startDate,endDate);
    }

    @Override
    public List<TrangThai> getAllStatus() {
        return trangThaiRepo.findAll();
    }

    @Override
    public DonHang cancelOrderStatus(DonHangTaiQuayStatusRequestDTO donHangStatus,String username) {
        DonHang donHang = donHangRepo.findById(donHangStatus.getIdDonHang()).get();
        HoaDon hoaDon = hoaDonRepo.findByDonHangId(donHangStatus.getIdDonHang());
        System.out.println("donHang: "+donHang);

        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
//            System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());
            nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
            if(getNV != null){
                donHang.setNhanVien(getNV);
                donHang.setUpdateBy(getNV.getHoTen());
                hoaDon.setUpdateBy(getNV.getHoTen());
            }
        }

        if(donHang == null){
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }
        if(donHang.getKhuyenMai() != null){
            KhuyenMai khuyenMai = null;
            Optional<KhuyenMai> optionalKhuyenMai = khuyenMaiRepo.findById(donHang.getKhuyenMai().getIdKhuyenMai());

            if (optionalKhuyenMai.isPresent()) {
                khuyenMai = optionalKhuyenMai.get();
                System.out.println("check khuyenmaix: " + khuyenMai);

                // Tăng số lượng khuyến mãi và lưu lại
                khuyenMai.setSoLuong(khuyenMai.getSoLuong() + 1);
                khuyenMaiRepo.save(khuyenMai);
            }
        }

        List<DonHangChiTiet> chiTietDonHangList = donHangChiTietRepo.findByDonHangId(donHang.getIdDonHang());

        // Cập nhật lại số lượng sản phẩm
        for (DonHangChiTiet chiTiet : chiTietDonHangList) {
            SanPhamChiTiet sanPham = chiTiet.getSanPhamChiTiet();
            sanPham.setSoLuong(sanPham.getSoLuong() + chiTiet.getSoLuong());
            sanPhamChiTietRepo.save(sanPham);
        }

        TrangThai trangThai = trangThaiRepo.findById(6).orElse(null);
        LocalDate localDate = LocalDate.now();
        //cập nhật hd
        hoaDon.setTrangThai(trangThai);
        hoaDon.setUpdateDate(localDate);
        hoaDonRepo.save(hoaDon);

        donHang.setTrangThai(trangThai);
        donHang.setUpdateDate(localDate);

        donHang.setGhiChu(donHangStatus.getGhiChu());
        donHangRepo.save(donHang);
        return donHang;
    }

    @Override
    public List<DonHang> searchTrangThaiDonhang(Integer idTrangThai) {
        return donHangRepo.findByTrangThaiId(idTrangThai);
    }

    @Override
    public HoaDon getInvoice(Integer id) {
        HoaDon hoaDon = hoaDonRepo.findByDonHangId(id);
        return hoaDon;
    }

    @Override
    public String printerInvoice(Integer id) {
        try{
            HoaDon getHoaDon = hoaDonRepo.findById(id).get();
            if(getHoaDon == null){
                System.out.println("không tìm thấy hoá đơn");
                throw  new RuntimeException("không tìm thấy hoá đơn");
            }
//            String pdfFilePath  = "C:\\Users\\Admin\\Desktop\\TTS-XUONG\\invoice.pdf";
            String folderPath = Paths.get("src", "main", "resources", "images").toAbsolutePath().toString();
            String pdfFilePath = folderPath + File.separator + getHoaDon.getMaHoaDon() + ".pdf";

            // Tạo thư mục nếu chưa tồn tại
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            //String pdfFilePath = "images"+getHoaDon.getMaHoaDon()+".pdf";
            File file = new File(pdfFilePath);
            if (file.exists()) {
                file.delete(); // Xóa tệp nếu tồn tại
            }

            PdfWriter pdfWriter = new PdfWriter(pdfFilePath);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

//            String imagePath = "D:\\DATN-FALL2024\\testgit\\src\\main\\resources\\images\\hinh1.jpg";
//            ImageData imageData = ImageDataFactory.create(imagePath);
//            Image image = new Image(imageData);
//            float x = pdfDocument.getDefaultPageSize().getWidth()/2;
//            float y = pdfDocument.getDefaultPageSize().getHeight()/2;
//            image.setFixedPosition(x -150,y-170);
//            image.setOpacity(0.1f);
//            document.add(image);


            float threecol = 190f;
            float towcol= 185f;
            float towcol150 = towcol +150f;
            //float towColumwidth[] = {400f, 400f};
            float towColumwidth[] = {towcol150 ,towcol};
            float columnWidths[] = {threecol*3}; // Define table column widths
            float threeColumnWidth[] ={threecol,threecol,threecol};
            // Định nghĩa lại chiều rộng của 5 cột
            float[] fiveColumnWidth = {threecol, threecol, threecol, threecol, threecol}; // Cân đối các cột

//            String fontPath = "C:\\Windows\\Fonts\\times.ttf";
//            PdfFont pdfFont = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
            InputStream fontStream = HoaDonServiceImpl.class.getClassLoader().getResourceAsStream("fonts/times.ttf");
            PdfFont pdfFont = PdfFontFactory.createFont(fontStream.readAllBytes(), PdfEncodings.IDENTITY_H, true);
            Paragraph paragraph = new Paragraph("\n");

            // Title
            Text nameShop = new Text("EIGHTTEN POLO").setFont(pdfFont);
            Paragraph paragraphNameShop = new Paragraph()
                    .add(nameShop)
                    .setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(18f);
            document.add(paragraphNameShop.add("\n\n"));

            // Information block
            Text phoneShop = new Text("Số điện thoại: 0123456789").setFont(pdfFont);
            Text emailShop = new Text("Email: eighteenpolo@gmail.com").setFont(pdfFont);
            Text addressShop = new Text("Địa chỉ: Tòa nhà FPT Polytechnic...").setFont(pdfFont);
            //document.add(new Paragraph("Số điện thoại: 0123456789\nEmail: beeshirt@gmail.com\nĐịa chỉ: Tòa nhà FPT Polytechnic..."));
            Paragraph paragraphInfo = new Paragraph()
                    .add(phoneShop).add("\n")
                    .add(emailShop).add("\n")
                    .add(addressShop)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(paragraphInfo);

            Border gb = new SolidBorder(new DeviceGray(0.5f),1f/2f);
            Table divider = new Table(columnWidths);
            divider.setBorder(gb);

            document.add(paragraph);
            document.add(divider);
            //document.add(paragraph);

            Text text2 = new Text("Mã Hoá Đơn:").setFont(pdfFont);
            Text text1 = new Text("Hoá Đơn Bán Hàng").setFont(pdfFont);
            //thông tin
            Text textSDT = new Text(getHoaDon.getSoDienThoaiKhachNhan()).setFont(pdfFont);
            Text textEmail = new Text(getHoaDon.getEmailKhachNhan()).setFont(pdfFont);;
            Text textHoTen = new Text(getHoaDon.getTenKhachNhan()).setFont(pdfFont);
            Text textMaHD = new Text(getHoaDon.getMaHoaDon()).setFont(pdfFont);

//            if(getHoaDon.getKhachHang().getSoDienThoai() != null){
//                textSDT = new Text(getHoaDon.getKhachHang().getSoDienThoai()).setFont(pdfFont);
//            }else {
//                textSDT = new Text("").setFont(pdfFont);
//            }
//            if(getHoaDon.getKhachHang().getEmail() != null){
//                textEmail = new Text(getHoaDon.getKhachHang().getEmail()).setFont(pdfFont);
//            }else {
//                textEmail = new Text("").setFont(pdfFont);
//            }
            Text diaChiNhan = new Text(getHoaDon.getDiaChiNhan()).setFont(pdfFont);
//            if(getHoaDon.getDiaChiNhan() == null){
//                diaChiNhan = new Text(getHoaDon.getKhachHang().getDiaChi()).setFont(pdfFont);
//            }else {
//                diaChiNhan = new Text(getHoaDon.getDiaChiNhan()).setFont(pdfFont);
//            }

            LocalDate createDate = getHoaDon.getCreateDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = createDate.format(formatter);
            Text textNgayTao = new Text(formattedDate).setFont(pdfFont);

            Table towColumnTable = new Table(columnWidths);
            Cell cell = new Cell().add(new Paragraph(text1)
                    .setFontSize(16f)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER))
                    .setBorder(Border.NO_BORDER);
            towColumnTable.addCell(cell);
            document.add(towColumnTable.setMarginBottom(10f));

            Table twoColTable2 = new Table(towColumwidth);
            twoColTable2.addCell(getCell10fleft("Tên Khách Hàng:",true));
            twoColTable2.addCell(getCell10fleft(text2,true));
//            twoColTable2.addCell(getCell10fleft("Coding Errors",false));
            twoColTable2.addCell(getCell10fleft(textHoTen,false));
            twoColTable2.addCell(getCell10fleft(textMaHD,false));
            twoColTable2.setWidthPercent(100);
            document.add(twoColTable2);

            Text text3 = new Text("Địa chỉ nhận hàng:").setFont(pdfFont);
            Text text4 = new Text("Ngày Tạo:").setFont(pdfFont);
            Text text5 = new Text("Số Điện Thoại:").setFont(pdfFont);
            Text text6 = new Text("Email:").setFont(pdfFont);
            Table twoColTable3 = new Table(towColumwidth);
            twoColTable3.addCell(getCell10fleft(text3,true));
            twoColTable3.addCell(getCell10fleft(text4,true));
            twoColTable3.addCell(getCell10fleft(diaChiNhan,false));
            twoColTable3.addCell(getCell10fleft(textNgayTao,false));
            twoColTable3.addCell(getCell10fleft(text5,true));
            twoColTable3.addCell(getCell10fleft(text6,true));
            twoColTable3.addCell(getCell10fleft(textSDT,false));
            twoColTable3.addCell(getCell10fleft(textEmail,false));
            twoColTable3.setWidthPercent(100);
            document.add(twoColTable3);



            Table tableDivider = new Table(columnWidths);
            Border dbg = new DashedBorder(Color.GRAY,0.5f);
            document.add(tableDivider.setBorder(dbg));

            Text textProduct = new Text("Danh Sách Sản Phẩm").setFont(pdfFont).setFontSize(12f).setBold();
            Paragraph productPara = new Paragraph(textProduct);
            document.add(productPara);

            //Table threeColTable1 = new Table(threeColumnWidth);
            Table threeColTable1 = new Table(fiveColumnWidth);
            threeColTable1.setBackgroundColor(Color.BLACK,0.7f);

            threeColTable1.addCell(new Cell().add("Tên Sản Phẩm").setBold().setFont(pdfFont).setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
            threeColTable1.addCell(new Cell().add("Màu Sắc").setBold().setFont(pdfFont).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER));
            threeColTable1.addCell(new Cell().add("Kích Thước").setBold().setFont(pdfFont).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER));
            threeColTable1.addCell(new Cell().add("Số Lượng").setBold().setFont(pdfFont).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER));
            threeColTable1.addCell(new Cell().add("Đơn Giá").setBold().setFont(pdfFont).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
            document.add(threeColTable1);

            List<HoaDonChiTiet> listSanPham = hoaDonChiTietRepo.findByHoaDonId(getHoaDon.getIdHoaDon());
            //Table threeColTable2 = new Table(threeColumnWidth);
            Table threeColTable2 = new Table(fiveColumnWidth);
            float totalSum =0;
            for(HoaDonChiTiet spct:listSanPham){
                float total = spct.getDonGia()* spct.getSoLuong();
                totalSum +=total;
                threeColTable2.addCell(new Cell().add(spct.getSanPhamChiTiet().getIdSanPham().getTen()).setFont(pdfFont).setBorder(Border.NO_BORDER).setMarginLeft(10f));
                threeColTable2.addCell(new Cell().add(spct.getSanPhamChiTiet().getIdMauSac().getTen()).setFont(pdfFont).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                threeColTable2.addCell(new Cell().add(spct.getSanPhamChiTiet().getIdKichCo().getTen()).setFont(pdfFont).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                threeColTable2.addCell(new Cell().add(String.valueOf(spct.getSoLuong())).setFont(pdfFont).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                threeColTable2.addCell(new Cell().add(String.valueOf(spct.getDonGia())).setFont(pdfFont).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            }
            document.add(threeColTable2.setMarginBottom(20f));

            float oneCol[] ={threecol+125f,threecol*2};
            Table threeColTable4 = new Table(oneCol);
            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add(tableDivider).setBorder(Border.NO_BORDER));
            document.add(threeColTable4);

            Text tong = new Text("Tổng").setFont(pdfFont);
            Paragraph paragraphTong = new Paragraph().add(tong);
            Text tongHoaDon = new Text("Tổng hoá đơn").setFont(pdfFont);
            Paragraph paragraphTongHoaDon = new Paragraph().add(tongHoaDon);
            Table threeColTable3 = new Table(threeColumnWidth);

            threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setMarginLeft(10f));
            threeColTable3.addCell(new Cell().add(paragraphTong).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            threeColTable3.addCell(new Cell().add(String.valueOf(totalSum)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
            //phí ship
            if(getHoaDon.getPhiVanChuyen() >0){
                threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setMarginLeft(10f));
                threeColTable3.addCell(new Cell().add("Phí Ship").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
                threeColTable3.addCell(new Cell().add(String.valueOf(getHoaDon.getPhiVanChuyen())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
                //tong + phi van chuyen
                threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setMarginLeft(15f));
                threeColTable3.addCell(new Cell().add(paragraphTongHoaDon).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
                threeColTable3.addCell(new Cell().add(String.valueOf(totalSum + getHoaDon.getPhiVanChuyen())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
            }
//            else {
//                threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setMarginLeft(10f));
//                threeColTable3.addCell(new Cell().add(paragraphTong).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
//                threeColTable3.addCell(new Cell().add(String.valueOf(totalSum)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
//            }
//            threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setMarginLeft(10f));
//            threeColTable3.addCell(new Cell().add(paragraphTong).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
//            threeColTable3.addCell(new Cell().add(String.valueOf(totalSum)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));


            //threeColTable3.addCell(new Cell().add(String.valueOf(totalSum)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
            document.add(threeColTable3);
            document.add(tableDivider);
            document.add(new Paragraph("\n"));
            document.add(divider.setBorder(new SolidBorder(Color.GRAY,1)).setMarginBottom(15f));

            Table tb = new Table(columnWidths);
            tb.addCell(new Cell().add("Tems and conditition")).setBold().setBorder(Border.NO_BORDER);
            tb.addCell(new Cell().add("1.Tems")).setBorder(Border.NO_BORDER);
            tb.addCell(new Cell().add("2.Tems")).setBorder(Border.NO_BORDER);
            document.add(tb);



            document.close();
            pdfDocument.close();
            pdfWriter.close();
            return pdfFilePath;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Cell getBillingShippingCell(Object  textValue) {
        Paragraph paragraph;
        // Kiểm tra xem textValue là kiểu String hay Text
        if (textValue instanceof String) {
            paragraph = new Paragraph((String) textValue); // Nếu là chuỗi
        } else if (textValue instanceof Text) {
            paragraph = new Paragraph().add((Text) textValue); // Nếu là đối tượng Text
        } else {
            throw new IllegalArgumentException("Invalid text value type. Must be String or Text.");
        }
        System.out.println("Text Value: " + textValue);
        return new Cell().add(paragraph).setFontSize(12f).setBold()
                .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    @Override
    public  Cell getCell10fleft(Object textValue, Boolean isBoolean) {
        Paragraph paragraph;
        // Kiểm tra xem textValue là kiểu String hay Text
        if (textValue instanceof String) {
            paragraph = new Paragraph((String) textValue); // Nếu là chuỗi
        } else if (textValue instanceof Text) {
            paragraph = new Paragraph().add((Text) textValue); // Nếu là đối tượng Text
        } else {
            throw new IllegalArgumentException("Invalid text value type. Must be String or Text.");
        }

        Cell cell = new Cell().add(paragraph).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBoolean ? cell.setBold() : cell;
    }
}
