create database DATN
go


use DATN
go

create table san_pham
(
id_san_pham int identity(1,1)  not null primary key,
ma varchar(30) null,
ten nvarchar(30) null,
trang_thai bit null,
create_date date null,
create_by nvarchar(30) null,
update_date date null,
update_by nvarchar(30) null
)
go

create table kieu_dang
(
id_kieu_dang int identity(1,1)  not null primary key,
ma varchar(30) null,
ten nvarchar(30) null,
trang_thai bit null,
create_date date null,
create_by nvarchar(30) null,
update_date date null,
update_by nvarchar(30) null
)
go


create table thuong_hieu
(
id_thuong_hieu int identity(1,1)  not null primary key,
ma varchar(30) null,
ten nvarchar(30) null,
trang_thai bit null,
create_date date null,
create_by nvarchar(30) null,
update_date date null,
update_by nvarchar(30) null
)
go

create table mau_sac
(
id_mau_sac int identity(1,1)  not null primary key,
ma varchar(30) null,
ten nvarchar(30) null,
trang_thai bit null,
create_date date null,
create_by nvarchar(30) null,
update_date date null,
update_by nvarchar(30) null
)
go


create table san_pham_chi_tiet
(
id_san_pham_chi_tiet int identity(1,1)  not null primary key,
ma varchar(30) null,
ten nvarchar(30) null,
trang_thai bit null,
create_date date null,
create_by nvarchar(30) null,
update_date date null,
update_by nvarchar(30) null,
chat_lieu nvarchar(30) null,
size varchar(30) null,
so_luong int null,
don_gia float null,
mo_ta nvarchar(100) null,
hinh_anh nvarchar(300) null,
id_san_pham int null,
id_mau_sac int null, 
id_thuong_hieu int null,
id_kieu_dang int null,
foreign key (id_san_pham) references san_pham(id_san_pham),
foreign key (id_mau_sac) references mau_sac(id_mau_sac),
foreign key (id_thuong_hieu) references thuong_hieu(id_thuong_hieu),
foreign key (id_kieu_dang) references kieu_dang(id_kieu_dang)
)
go


CREATE TABLE vai_tro (
    ma VARCHAR(20) PRIMARY KEY,
    ten NVARCHAR(30)
);

CREATE TABLE tai_khoan (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(200) NOT NULL,
    role VARCHAR(20),
    trangthai BIT,
    email VARCHAR(100),
    FOREIGN KEY (role) REFERENCES vai_tro(ma) 
);



CREATE TABLE nhan_vien (
    id_nhan_vien INT PRIMARY KEY IDENTITY(1,1),
    ma_nhan_vien VARCHAR(50) ,
    ho_ten NVARCHAR(100) ,
    so_dien_thoai VARCHAR(15),
    ngay_sinh DATE,
    so_can_cuoc_cong_dan VARCHAR(50),
    dia_chi NVARCHAR(255),
    gioi_tinh NVARCHAR(10),
    trang_thai BIT DEFAULT 1,
    username_tai_khoan VARCHAR(50),      
    create_date DATETIME DEFAULT GETDATE(),
    create_by NVARCHAR(50),
    update_date DATETIME,
    update_by NVARCHAR(50),
    delete_by NVARCHAR(50),
    FOREIGN KEY (username_tai_khoan) REFERENCES tai_khoan(username)
	)


CREATE TABLE khuyen_mai (
    ID_khuyen_mai INT IDENTITY(1,1) PRIMARY KEY ,
    ma_khuyen_mai VARCHAR (100),
    ten_khuyen_mai NVARCHAR (100),
    muc_giam_gia VARCHAR (100) ,
    thoi_gian_bat_dau DATE  ,
    thoi_gian_ket_thuc DATE,
    mo_ta NVARCHAR(100),
    trang_thai BIT,
    dieu_kien_ap_dung NVARCHAR(100),

);


 CREATE TABLE khach_hang (
    ID_khach_hang INT IDENTITY(1,1) PRIMARY KEY ,
    ma_khach_hang VARCHAR (100),
    ho_ten NVARCHAR (100),
    ngay_sinh DATE ,
    so_dien_thoai INT ,
    email VARCHAR (100) ,
    gioi_tinh BIT ,
    dia_chi NVARCHAR(300) ,
	username_tai_khoan VARCHAR(50),           
    FOREIGN KEY (username_tai_khoan) REFERENCES tai_khoan(username)
);


CREATE TABLE trang_thai(
	id_trang_thai INT PRIMARY KEY IDENTITY(1,1),
	ma_trang_thai VARCHAR(50),
	ten_trang_thai VARCHAR(150),
	trang_thai BIT
);


CREATE TABLE phuong_thuc_thanh_toan(
	id_phuong_thuc_thanh_toan INT PRIMARY KEY IDENTITY(1,1),
	ma_phuong_thuc_thanh_toan VARCHAR(50),
	ten_trang_thai VARCHAR(150),
	trang_thai BIT,
	create_date DATE ,
	create_by VARCHAR(100),
	update_date DATE NULL,
	update_by VARCHAR(100) NULL,
);


CREATE TABLE gio_hang(
	id_gio_hang INT PRIMARY KEY IDENTITY(1,1),
	ma_gio_hang VARCHAR(50),
	username varchar(50),
	trang_thai BIT
	FOREIGN KEY (username) REFERENCES tai_khoan(username)
);



CREATE TABLE gio_hang_chi_tiet(
	id_gio_hang_chi_tiet INT PRIMARY KEY IDENTITY(1,1),
	id_gio_hang INT,
	id_san_pham_chi_tiet INT,
	ma_gio_hang_chi_tiet VARCHAR(50),
	so_luong INT,
	don_gia FLOAT,
	trang_thai BIT,
    FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id_san_pham_chi_tiet),
    FOREIGN KEY (id_gio_hang) REFERENCES gio_hang(id_gio_hang)
);


CREATE TABLE don_hang(
	id_don_hang INT PRIMARY KEY IDENTITY(1,1),
	ma_don_hang VARCHAR(50),
	tong_tien FLOAT,
	tong_tien_khuyen_mai FLOAT,
	tong_tien_sau_khuyen_mai FLOAT,
	ghi_chu NVARCHAR(255),
	trang_thai_thanh_toan BIT,
	id_nhan_vien INT NULL,
	id_khach_hang INT,
	id_trang_thai INT,
	id_phuong_thuc_thanh_toan INT,
	id_khuyen_mai int,
	--username varchar(50),
	--FOREIGN KEY (username) REFERENCES tai_khoan(username),
    FOREIGN KEY (id_trang_thai) REFERENCES trang_thai(id_trang_thai),
	FOREIGN KEY (id_khuyen_mai) REFERENCES khuyen_mai(id_khuyen_mai),
    FOREIGN KEY (id_phuong_thuc_thanh_toan) REFERENCES phuong_thuc_thanh_toan(id_phuong_thuc_thanh_toan),
	FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id_nhan_vien),
	FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id_khach_hang),
	--thắc mắc không biết nên cho id khuyến mại vào không
);

CREATE TABLE don_hang_chi_tiet(
	id_don_hang_chi_tiet INT PRIMARY KEY IDENTITY(1,1),
	ma_don_hang_chi_tiet VARCHAR(50),
	so_luong INT,
	don_gia FLOAT,
	ghi_chu NVARCHAR(255),
	id_don_hang INT,
	id_san_pham_chi_tiet INT
	FOREIGN KEY (id_don_hang) REFERENCES don_hang(id_don_hang)
);


CREATE TABLE hoa_don(
 id_hoa_don int PRIMARY KEY IDENTITY(1,1),
 id_khuyen_mai INT,
 id_trang_thai INT,
 id_phuong_thuc_thanh_toan INT,
 id_don_hang INT,
 id_nhan_vien INT NULL,
 id_khach_hang INT,
 ma_hoa_don VARCHAR(50),
 create_by DATE,
 update_date DATE,
 update_by DATE,
 tong_tien FLOAT, 
 tong_tien_khuyen_mai FLOAT,
 tong_tien_sau_khuyen_mai FLOAT,
 ghi_chu NVARCHAR(255),
 trang_thai_thanh_toan BIT,
 --username varchar(50),
 --FOREIGN KEY (username) REFERENCES tai_khoan(username),
 FOREIGN KEY (id_khuyen_mai) REFERENCES khuyen_mai(id_khuyen_mai),
 FOREIGN KEY (id_trang_thai) REFERENCES trang_thai(id_trang_thai),
 FOREIGN KEY (id_phuong_thuc_thanh_toan) REFERENCES phuong_thuc_thanh_toan(id_phuong_thuc_thanh_toan),
 FOREIGN KEY (id_don_hang) REFERENCES don_hang(id_don_hang),
 FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id_nhan_vien),
 FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id_khach_hang),
 );
 

CREATE TABLE hoa_don_chi_tiet(
	id_hoa_don_chi_tiet INT PRIMARY KEY IDENTITY(1,1),
	ma_hoa_don_chi_tiet VARCHAR(50),
	id_hoa_don INT,
	id_san_pham_chi_tiet INT,
	so_luong INT,
	don_gia FLOAT,
	trang_thai BIT,
	ghi_chu NVARCHAR(255)
    FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id_hoa_don),
	FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id_san_pham_chi_tiet)
);



INSERT INTO san_pham (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
VALUES 
('SP001', N'Sản phẩm 1', 1, '2024-01-01', N'Admin', NULL, NULL),
('SP002', N'Sản phẩm 2', 1, '2024-02-01', N'Admin', NULL, NULL);



INSERT INTO kieu_dang (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
VALUES 
('KD001', N'Kiểu dáng 1', 1, '2024-01-01', N'Admin', NULL, NULL),
('KD002', N'Kiểu dáng 2', 1, '2024-02-01', N'Admin', NULL, NULL);



INSERT INTO thuong_hieu (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
VALUES 
('TH001', N'Thương hiệu 1', 1, '2024-01-01', N'Admin', NULL, NULL),
('TH002', N'Thương hiệu 2', 1, '2024-02-01', N'Admin', NULL, NULL);



INSERT INTO mau_sac (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
VALUES 
('MS001', N'Màu sắc 1', 1, '2024-01-01', N'Admin', NULL, NULL),
('MS002', N'Màu sắc 2', 1, '2024-02-01', N'Admin', NULL, NULL);



INSERT INTO san_pham_chi_tiet 
(ma, ten, trang_thai, create_date, create_by, chat_lieu, size, so_luong, don_gia, mo_ta, hinh_anh, id_san_pham, id_mau_sac, id_thuong_hieu, id_kieu_dang)
VALUES 
('SPCT001', N'Sản phẩm chi tiết 1', 1, '2024-01-01', N'Admin', N'Cotton', 'M', 100, 200.0, N'Mô tả sản phẩm 1', N'hinh1.jpg', 1, 1, 1, 1),
('SPCT002', N'Sản phẩm chi tiết 2', 1, '2024-02-01', N'Admin', N'Polyester', 'L', 150, 300.0, N'Mô tả sản phẩm 2', N'hinh2.jpg', 2, 2, 2, 2);


INSERT INTO vai_tro (ma, ten)
VALUES 
('ADMIN', N'Quản trị viên'),
('STAFF', N'Nhân viên'),
('USER', N'Người dùng');

INSERT INTO tai_khoan (username, password, role, trangthai, email)
VALUES 
('admin', '$2a$10$PI0IFwH4e3QghalX9Z1wQ..Ks79YZVxVGpASvk2A.HkYtL9ENiNkS', 'ADMIN', 1, 'admin@example.com'),
('staff1', '$2a$10$PI0IFwH4e3QghalX9Z1wQ..Ks79YZVxVGpASvk2A.HkYtL9ENiNkS', 'STAFF', 1, 'staff1@example.com'),
('staff2', '$2a$10$PI0IFwH4e3QghalX9Z1wQ..Ks79YZVxVGpASvk2A.HkYtL9ENiNkS', 'STAFF', 1, 'staff2@example.com'),
('user1', '$2a$10$PI0IFwH4e3QghalX9Z1wQ..Ks79YZVxVGpASvk2A.HkYtL9ENiNkS', 'USER', 1, 'user1@example.com'),
('user2', '$2a$10$PI0IFwH4e3QghalX9Z1wQ..Ks79YZVxVGpASvk2A.HkYtL9ENiNkS', 'USER', 1, 'user2@example.com');



INSERT INTO nhan_vien (ma_nhan_vien, ho_ten, so_dien_thoai, ngay_sinh, so_can_cuoc_cong_dan, dia_chi, gioi_tinh, trang_thai, username_tai_khoan, create_by)
VALUES 
('NV001', N'Nguyễn Văn A', '0909123456', '1990-01-01', '123456789', N'123 Đường ABC', N'Nam', 1, 'staff1', N'Nhân viên'),
('NV002', N'Nguyễn Văn B', '0909123456', '1996-06-06', '123456789', N'123 Đường ABC', N'Nữ', 1, 'staff2', N'Nhân viên');



INSERT INTO khuyen_mai (ma_khuyen_mai, ten_khuyen_mai, muc_giam_gia, thoi_gian_bat_dau, thoi_gian_ket_thuc, mo_ta, trang_thai, dieu_kien_ap_dung)
VALUES 
('KM001', N'Giảm giá 10%', '10%', '2024-01-01', '2024-12-31', N'Khuyến mãi giảm 10%', 1, N'Mua trên 500k');



INSERT INTO khach_hang (ma_khach_hang, ho_ten, ngay_sinh, so_dien_thoai, email, gioi_tinh, dia_chi, username_tai_khoan)
VALUES 
('KH001', N'Nguyễn Thị B', '1995-02-01', '0909765432', 'ntb@example.com', 0, N'123 Đường XYZ', 'user1'),
('KH002', N'Nguyễn Thị A', '1995-02-01', '0909765432', 'ntb@example.com', 1, N'ABC', 'user2');


INSERT INTO trang_thai (ma_trang_thai, ten_trang_thai, trang_thai)
VALUES 
('TT001', N'Chờ xử lý', 1),
('TT002', N'Đã hoàn thành', 1),
('TT003', N'Đã hủy', 1);



INSERT INTO phuong_thuc_thanh_toan (ma_phuong_thuc_thanh_toan, ten_trang_thai, trang_thai, create_date, create_by)
VALUES 
('PTTT001', N'Thanh toán khi nhận hàng', 1, '2023-01-01', 'admin'),
('PTTT002', N'Thanh toán bằng thẻ', 1, '2023-01-02', 'admin'),
('PTTT003', N'Thanh toán qua ví điện tử', 1, '2023-01-03', 'admin');



INSERT INTO gio_hang (ma_gio_hang, username, trang_thai)
VALUES 
('GH001', 'user1', 1),
('GH002', 'user1', 1);



INSERT INTO gio_hang_chi_tiet (id_gio_hang, id_san_pham_chi_tiet, ma_gio_hang_chi_tiet, so_luong, don_gia, trang_thai)
VALUES 
(1, 1, 'GHCT001', 2, 400000, 1),
(1, 2, 'GHCT002', 1, 350000, 1);



INSERT INTO don_hang (ma_don_hang, tong_tien, tong_tien_khuyen_mai, tong_tien_sau_khuyen_mai, ghi_chu, trang_thai_thanh_toan, id_nhan_vien, id_khach_hang, id_trang_thai, id_phuong_thuc_thanh_toan, id_khuyen_mai)
VALUES 
('DH001', 750000, 50000, 700000, N'Giao hàng nhanh', 1, 1, 1, 1, 1, 1),
('DH002', 1200000, 100000, 1100000, N'Giao hàng tiêu chuẩn', 1, 2, 2, 2, 2, NULL);




INSERT INTO don_hang_chi_tiet (ma_don_hang_chi_tiet, so_luong, don_gia, ghi_chu, id_don_hang, id_san_pham_chi_tiet)
VALUES 
('DHCT001', 2, 200000, N'Màu xanh', 1, 1),
('DHCT002', 1, 350000, N'Màu đen', 2, 2);


select * from hoa_don
INSERT INTO hoa_don (id_khuyen_mai, id_trang_thai, id_phuong_thuc_thanh_toan, id_don_hang, id_nhan_vien, id_khach_hang, ma_hoa_don, create_by, tong_tien, tong_tien_khuyen_mai, tong_tien_sau_khuyen_mai, ghi_chu, trang_thai_thanh_toan)
VALUES 
(1, 1, 1, 1, 1, 1, 'HD001', '2023-01-10', 750000, 50000, 700000, N'Hóa đơn 1', 1),
(NULL, 2, 2, 2, 2, 2, 'HD002', '2023-01-11', 1200000, 100000, 1100000, N'Hóa đơn 2', 1);



INSERT INTO hoa_don_chi_tiet (ma_hoa_don_chi_tiet, id_hoa_don, id_san_pham_chi_tiet, so_luong, don_gia, trang_thai, ghi_chu)
VALUES 
('HDCT001', 1, 1, 2, 200000, 1, N'Áo thun xanh'),
('HDCT002', 2, 2, 1, 350000, 1, N'Quần jean đen');

