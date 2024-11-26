package com.example.demo.Service.impl;

import com.example.demo.Service.QuanLyDonHangOnlineService;
import com.example.demo.dto.request.DonHangOnlineStatusRequestDTO;
import com.example.demo.dto.request.HoaDonOnlineRequestDTO;
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
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class QuanLyDonHangOnlineServiceImpl implements QuanLyDonHangOnlineService {
    @Autowired
    DonHangRepo donHangRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
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
    public List<DonHang> getAllOrderByOrderType() {
        List<DonHang> listOrder = donHangRepo.findDonHangByLoaiDonHang(2);
        return listOrder;
    }

    @Override
    public List<DonHangChiTiet>  getOrderByIdOrderType(Integer id) {
        List<DonHangChiTiet>  donHangChiTiet = donHangChiTietRepo.findByDonHangId(id);
        return donHangChiTiet;
    }

    @Override
    public DonHang updateStatusOrder(DonHangOnlineStatusRequestDTO donHangOnlineStatusRequestDTO, String username) {
        DonHang donHang = donHangRepo.findById(donHangOnlineStatusRequestDTO.getIdDonHang()).get();
        System.out.println("donHang: "+donHang);

        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
            System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());
            nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
            donHang.setNhanVien(getNV);

        }

        if(donHang == null){
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }

        TrangThai trangThai = trangThaiRepo.findById(donHangOnlineStatusRequestDTO.getIdTrangThai()).orElse(null);
        donHang.setTrangThai(trangThai);
//        int currentTrangThaiId = donHang.getTrangThai().getIdTrangThai();
//        if (currentTrangThaiId == 1) {
//            TrangThai trangThai = trangThaiRepo.findById(7).orElse(null);
//            donHang.setTrangThai(trangThai);
//        } else if (currentTrangThaiId == 7) {
//            TrangThai trangThai = trangThaiRepo.findById(2).orElse(null);
//            donHang.setTrangThai(trangThai);
//        } else if (currentTrangThaiId == 2) {
//            TrangThai trangThai = trangThaiRepo.findById(3).orElse(null);
//            donHang.setTrangThai(trangThai);
//        } else if (currentTrangThaiId == 3) {
//            TrangThai trangThai = trangThaiRepo.findById(5).orElse(null);
//            donHang.setTrangThai(trangThai);
//        }

//        if(donHang.getTrangThai().getIdTrangThai() ==3){
//            TrangThai trangThai = trangThaiRepo.findById(4).get();
//            donHang.setTrangThai(trangThai);
//        }

        donHang.setGhiChu(donHangOnlineStatusRequestDTO.getGhiChu());
        donHangRepo.save(donHang);
        return donHang;
    }

    @Override
    public HoaDon createInvoice(HoaDonOnlineRequestDTO hoaDonOnlineRequestDTO, String username) {
        HoaDon hoaDon = new HoaDon();

        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
            System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());
            nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
            hoaDon.setNhanVien(getNV);

        }

        DonHang donHang = donHangRepo.findById(hoaDonOnlineRequestDTO.getIdDonHang()).get();
        if(donHang == null){
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }
        //set khuyến mãi
        hoaDon.setKhuyenMai(donHang.getKhuyenMai());
        //set trang Thai
        hoaDon.setTrangThai(donHang.getTrangThai());
        //set phương thuc thanh toan
        hoaDon.setPhuongThucThanhToan(donHang.getPhuongThucThanhToan());
        //set don hàng
        hoaDon.setDonHang(donHang);
        //set khach hàng
        hoaDon.setKhachHang(donHang.getKhachHang());
        //set ma hd
        hoaDon.setMaHoaDon(hoaDonOnlineRequestDTO.getMaHoaDon());
        //set ten kn
        hoaDon.setTenKhachNhan(donHang.getTenKhachNhan());
        //set email kn
        hoaDon.setEmailKhachNhan(donHang.getEmailKhachNhan());
        //set sdt kn
        hoaDon.setSoDienThoaiKhachNhan(donHang.getSoDienThoaiKhachNhan());
        //set dia chỉ kn
        hoaDon.setDiaChiNhan(donHang.getDiaChiNhan());
        //tổng tiên
        hoaDon.setTongTien(donHang.getTongTien());
        //tong tiền km
        hoaDon.setTongTienKhuyenMai(donHang.getTongTienKhuyenMai());
        //tong tien sau km
        hoaDon.setTongTienSauKhuyenMai(donHang.getTongTienSauKhuyenMai());
        //tong tien phai thanh toan
        hoaDon.setTongTienThanhToan(donHang.getTongTienThanhToan());
        //phi van chuyen
        hoaDon.setPhiVanChuyen(donHang.getPhiVanChuyen());
        //ghi chu
        hoaDon.setGhiChu(donHang.getGhiChu());
        //trang thai thanh toan
        hoaDon.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
        //phuong thuc nhan
        hoaDon.setPhuongThucNhan(donHang.getPhuongThucNhan());

        hoaDonRepo.save(hoaDon);


        //tạo háo đơn chi tiết
        HoaDon findHoaDon = hoaDonRepo.findByMaHoaDon(hoaDonOnlineRequestDTO.getMaHoaDon());
        List<DonHangChiTiet> donHangChiTietLits = donHangChiTietRepo.findByDonHangId(donHang.getIdDonHang());
        for(DonHangChiTiet donHangChiTiet: donHangChiTietLits){

            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            hoaDonChiTiet.setMaHoaDonChiTiet(generateRandomString(8));
            hoaDonChiTiet.setHoaDon(findHoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(donHangChiTiet.getSanPhamChiTiet());
            hoaDonChiTiet.setSoLuong(donHangChiTiet.getSoLuong());
            hoaDonChiTiet.setDonGia(donHangChiTiet.getDonGia());
            hoaDonChiTiet.setTrangThai(true);
            hoaDonChiTiet.setGhiChu(donHangChiTiet.getGhiChu());

            hoaDonChiTietRepo.save(hoaDonChiTiet);
        }


        return hoaDon;
    }

    @Override
    public String generateRandomString(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public DonHang cancelStatusOrder(DonHangOnlineStatusRequestDTO donHangOnlineStatusRequestDTO, String username) {
        DonHang donHang = donHangRepo.findById(donHangOnlineStatusRequestDTO.getIdDonHang()).get();
        System.out.println("donHang: "+donHang);

        taikhoan oldTaiKoan = taikhoanRepo.findByUsername(username);
        if(oldTaiKoan!= null){
            System.out.println("check TK: "+oldTaiKoan.toString());
            System.out.println("check TK: "+oldTaiKoan.getNhanVien().getIdNhanVien());
            nhanvien getNV = nhanVienRepo.findById(oldTaiKoan.getNhanVien().getIdNhanVien()).get();
            donHang.setNhanVien(getNV);

        }

        if(donHang == null){
            throw new RuntimeException("Không tìm thấy đơn hàng!");
        }

//        int currentTrangThaiId = donHang.getTrangThai().getIdTrangThai();
//
//        if (currentTrangThaiId == 1) {
//            TrangThai trangThai = trangThaiRepo.findById(7).orElse(null);
//            donHang.setTrangThai(trangThai);
//        } else if (currentTrangThaiId == 7) {
//            TrangThai trangThai = trangThaiRepo.findById(2).orElse(null);
//            donHang.setTrangThai(trangThai);
//        } else if (currentTrangThaiId == 2) {
//            TrangThai trangThai = trangThaiRepo.findById(3).orElse(null);
//            donHang.setTrangThai(trangThai);
//        } else if (currentTrangThaiId == 3) {
//            TrangThai trangThai = trangThaiRepo.findById(5).orElse(null);
//            donHang.setTrangThai(trangThai);
//        }
        TrangThai trangThai = trangThaiRepo.findById(6).orElse(null);
            donHang.setTrangThai(trangThai);
//        if(donHang.getTrangThai().getIdTrangThai() ==3){
//            TrangThai trangThai = trangThaiRepo.findById(4).get();
//            donHang.setTrangThai(trangThai);
//        }

        donHang.setGhiChu(donHangOnlineStatusRequestDTO.getGhiChu());
        donHangRepo.save(donHang);
        return donHang;
    }

    @Override
    public DonHang getStuats(Integer id) {
        return donHangRepo.findById(id).get();
    }

    @Override
    public String printerInvoiceOnlice(Integer id) {
        try{
//            String pdfFilePath  = "C:\\Users\\Admin\\Desktop\\TTS-XUONG\\invoice.pdf";
            String pdfFilePath = "C:\\Users\\Admin\\Desktop\\TTS-XUONG\\invoice1.pdf";
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

            HoaDon getHoaDon = hoaDonRepo.findById(id).get();
            if(getHoaDon == null){
                System.out.println("không tìm thấy hoá đơn");
                throw  new RuntimeException("không tìm thấy hoá đơn");
            }


            float threecol = 190f;
            float towcol= 185f;
            float towcol150 = towcol +150f;
            //float towColumwidth[] = {400f, 400f};
            float towColumwidth[] = {towcol150 ,towcol};
            float columnWidths[] = {threecol*3}; // Define table column widths
            float threeColumnWidth[] ={threecol,threecol,threecol};

            String fontPath = "C:\\Windows\\Fonts\\times.ttf";
            PdfFont pdfFont = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
            Paragraph paragraph = new Paragraph("\n");

            // Title
            Text nameShop = new Text("EIGHTTEN POLO").setFont(pdfFont).setTextAlignment(TextAlignment.CENTER);
//            Paragraph paragraphTong = new Paragraph().add(nameShop);
            document.add(new Paragraph(nameShop+"\n\n"));

            // Information block
            Text phoneShop = new Text("Số điện thoại: 0123456789").setFont(pdfFont);
            Text emailShop = new Text("Email: eighteenpolo@gmail.com").setFont(pdfFont);
            Text addressShop = new Text("Địa chỉ: Tòa nhà FPT Polytechnic...").setFont(pdfFont);
            //document.add(new Paragraph("Số điện thoại: 0123456789\nEmail: beeshirt@gmail.com\nĐịa chỉ: Tòa nhà FPT Polytechnic..."));
            document.add(new Paragraph(phoneShop+"\n"+emailShop+"\n"+addressShop));

            Border gb = new SolidBorder(new DeviceGray(0.5f),1f/2f);
            Table divider = new Table(columnWidths);
            divider.setBorder(gb);

            document.add(paragraph);
            document.add(divider);
            //document.add(paragraph);


            Text text2 = new Text("Mã Hoá Đơn:").setFont(pdfFont);
            Text text1 = new Text("Hoá Đơn Bán Hàng").setFont(pdfFont);
            //thông tin
            Text textSDT;
            Text textEmail;
            Text textHoTen = new Text(getHoaDon.getKhachHang().getHoTen()).setFont(pdfFont);
            Text textMaHD = new Text(getHoaDon.getMaHoaDon()).setFont(pdfFont);

            if(getHoaDon.getKhachHang().getSoDienThoai() != null){
                textSDT = new Text(getHoaDon.getKhachHang().getSoDienThoai()).setFont(pdfFont);
            }else {
                textSDT = new Text("").setFont(pdfFont);
            }
            if(getHoaDon.getKhachHang().getEmail() != null){
                textEmail = new Text(getHoaDon.getKhachHang().getEmail()).setFont(pdfFont);
            }else {
                textEmail = new Text("").setFont(pdfFont);
            }

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
            twoColTable3.addCell(getCell10fleft("",false));
            twoColTable3.addCell(getCell10fleft(textNgayTao,false));
            twoColTable3.addCell(getCell10fleft(text5,true));
            twoColTable3.addCell(getCell10fleft(text6,true));
            twoColTable3.addCell(getCell10fleft(textSDT,false));
            twoColTable3.addCell(getCell10fleft(textEmail,false));
            twoColTable3.setWidthPercent(100);
            document.add(twoColTable3);

            document.add(divider);
            document.add(new Paragraph(phoneShop+"\n"+emailShop+"\n"+addressShop));
            document.add(divider);

            Table tableDivider = new Table(columnWidths);
            Border dbg = new DashedBorder(Color.GRAY,0.5f);
            document.add(tableDivider.setBorder(dbg));

            Text textProduct = new Text("Danh Sách Sản Phẩm").setFont(pdfFont).setFontSize(12f).setBold();
            Paragraph productPara = new Paragraph(textProduct);
            document.add(productPara);

            Table threeColTable1 = new Table(threeColumnWidth);
            threeColTable1.setBackgroundColor(Color.BLACK,0.7f);

            threeColTable1.addCell(new Cell().add("Tên Sản Phẩm").setBold().setFont(pdfFont).setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
            threeColTable1.addCell(new Cell().add("Số Lượng").setBold().setFont(pdfFont).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER));
            threeColTable1.addCell(new Cell().add("Đơn Giá").setBold().setFont(pdfFont).setFontColor(Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
            document.add(threeColTable1);

            List<HoaDonChiTiet> listSanPham = hoaDonChiTietRepo.findByHoaDonId(getHoaDon.getIdHoaDon());
            Table threeColTable2 = new Table(threeColumnWidth);
            float totalSum =0;
            for(HoaDonChiTiet spct:listSanPham){
                float total = spct.getDonGia()* spct.getSoLuong();
                totalSum +=total;
                threeColTable2.addCell(new Cell().add(spct.getSanPhamChiTiet().getIdSanPham().getTen()).setFont(pdfFont).setBorder(Border.NO_BORDER).setMarginLeft(10f));
                threeColTable2.addCell(new Cell().add(String.valueOf(spct.getSoLuong())).setFont(pdfFont).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                threeColTable2.addCell(new Cell().add(String.valueOf(spct.getDonGia())).setFont(pdfFont).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            }
            document.add(threeColTable2.setMarginBottom(20f));
            float oneCol[] ={threecol+125f,threecol*2};
            Table threeColTable4 = new Table(oneCol);
            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add(tableDivider).setBorder(Border.NO_BORDER));
            document.add(threeColTable4);

            Text tong = new Text("Tổng Hoá Đơn").setFont(pdfFont);
            Paragraph paragraphTong = new Paragraph().add(tong);
            Table threeColTable3 = new Table(threeColumnWidth);
            threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setMarginLeft(10f));
            if(getHoaDon.getPhiVanChuyen() >0){
                threeColTable3.addCell(new Cell().add("Phỉ Ship").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
                threeColTable3.addCell(new Cell().add(String.valueOf(getHoaDon.getPhiVanChuyen())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
            }
            threeColTable3.addCell(new Cell().add(paragraphTong).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            threeColTable3.addCell(new Cell().add(String.valueOf(totalSum)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
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
    public Cell getBillingShippingCell(Object textValue) {
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
    public Cell getCell10fleft(Object textValue, Boolean isBoolean) {
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
