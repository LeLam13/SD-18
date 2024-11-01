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



create table xuat_xu
(
id_xuat_xu int identity(1,1)  not null primary key,
ma varchar(30) null,
ten nvarchar(30) null,
trang_thai bit null,
create_date date null,
create_by nvarchar(30) null,
update_date date null,
update_by nvarchar(30) null
)
go



create table kich_co
(
id_kich_co int identity(1,1)  not null primary key,
ma varchar(30) null,
ten nvarchar(30) null,
trang_thai bit null,
create_date date null,
create_by nvarchar(30) null,
update_date date null,
update_by nvarchar(30) null
)
go




create table kieu_co_ao
(
id_kieu_co_ao int identity(1,1)  not null primary key,
ma varchar(30) null,
ten nvarchar(30) null,
trang_thai bit null,
create_date date null,
create_by nvarchar(30) null,
update_date date null,
update_by nvarchar(30) null
)
go



create table kieu_tay_ao
(
id_kieu_tay_ao int identity(1,1)  not null primary key,
ma varchar(30) null,
ten nvarchar(30) null,
trang_thai bit null,
create_date date null,
create_by nvarchar(30) null,
update_date date null,
update_by nvarchar(30) null
)
go



create table chat_lieu
(
id_chat_lieu int identity(1,1)  not null primary key,
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
so_luong int null,
don_gia float null,
mo_ta nvarchar(100) null,
hinh_anh nvarchar(300) null,
id_san_pham int null,
id_mau_sac int null, 
id_thuong_hieu int null,
id_kieu_dang int null,
id_xuat_xu int null,
id_kich_co int null,
id_kieu_co_ao int null,
id_kieu_tay_ao int null,
id_chat_lieu int null,
foreign key (id_san_pham) references san_pham(id_san_pham),
foreign key (id_mau_sac) references mau_sac(id_mau_sac),
foreign key (id_thuong_hieu) references thuong_hieu(id_thuong_hieu),
foreign key (id_kieu_dang) references kieu_dang(id_kieu_dang),
foreign key (id_xuat_xu) references  xuat_xu(id_xuat_xu),
foreign key (id_kich_co) references kich_co(id_kich_co),
foreign key (id_kieu_co_ao) references kieu_co_ao(id_kieu_co_ao),
foreign key (id_kieu_tay_ao) references kieu_tay_ao(id_kieu_tay_ao),
foreign key (id_chat_lieu) references chat_lieu(id_chat_lieu)
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
    so_dien_thoai VARCHAR (100) ,
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



insert into san_pham (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
values ('SP001', N'Áo Polo 1', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('SP002', N'Áo Polo 2', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('SP003', N'Áo Polo 3', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('SP004', N'Áo Polo 4', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('SP005', N'Áo Polo 5', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('SP006', N'Áo Polo 6', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('SP007', N'Áo Polo 7', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('SP008', N'Áo Polo 8', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('SP009', N'Áo Polo 9', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('SP010', N'Áo Polo 10', 1, '2024-01-01', 'admin', '2024-01-02', 'admin');


insert into kieu_dang (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
values ('KD001', N'Cổ điển', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KD002', N'Hiện đại', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KD003', N'Thể thao', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KD004', N'Dễ thương', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KD005', N'Bụi bặm', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KD006', N'Năng động', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KD007', N'Thanh lịch', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KD008', N'Sành điệu', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KD009', N'Phóng khoáng', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KD010', N'Cá tính', 1, '2024-01-01', 'admin', '2024-01-02', 'admin');


insert into thuong_hieu (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
values ('TH001', N'Adidas', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('TH002', N'Nike', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('TH003', N'Puma', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('TH004', N'Uniqlo', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('TH005', N'H&M', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('TH006', N'Zara', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('TH007', N'Levi', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('TH008', N'Gucci', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('TH009', N'Chanel', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('TH010', N'Louis Vuitton', 1, '2024-01-01', 'admin', '2024-01-02', 'admin');


insert into mau_sac (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
values ('MS001', N'Đỏ', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('MS002', N'Xanh', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('MS003', N'Vàng', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('MS004', N'Trắng', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('MS005', N'Đen', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('MS006', N'Hồng', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('MS007', N'Xám', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('MS008', N'Cam', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('MS009', N'Tím', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('MS010', N'Nâu', 1, '2024-01-01', 'admin', '2024-01-02', 'admin');


insert into xuat_xu (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
values ('XX001', N'Việt Nam', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('XX002', N'Nhật Bản', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('XX003', N'Trung Quốc', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('XX004', N'Hàn Quốc', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('XX005', N'Mỹ', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('XX006', N'Đức', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('XX007', N'Pháp', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('XX008', N'Ý', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('XX009', N'Thái Lan', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('XX010', N'Ấn Độ', 1, '2024-01-01', 'admin', '2024-01-02', 'admin');



insert into chat_lieu (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
values ('CL001', N'Cotton', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('CL002', N'Polyester', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('CL003', N'Lụa', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('CL004', N'Denim', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('CL005', N'Len', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('CL006', N'Viscose', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('CL007', N'Thun', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('CL008', N'Da', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('CL009', N'Vải bố', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('CL010', N'Nylon', 1, '2024-01-01', 'admin', '2024-01-02', 'admin');



insert into kieu_co_ao (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
values ('KCA001', N'Cổ tròn', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KCA002', N'Cổ V', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KCA003', N'Cổ bẻ', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KCA004', N'Cổ lọ', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KCA005', N'Cổ sen', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KCA006', N'Cổ yếm', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KCA007', N'Cổ vuông', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KCA008', N'Cổ choker', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KCA009', N'Cổ lệch vai', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KCA010', N'Cổ sơ mi', 1, '2024-01-01', 'admin', '2024-01-02', 'admin');



insert into kieu_tay_ao (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
values ('KTA001', N'Tay ngắn', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KTA002', N'Tay dài', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KTA003', N'Tay lỡ', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KTA004', N'Tay phồng', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KTA005', N'Tay loe', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KTA006', N'Tay bó', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KTA007', N'Tay cánh dơi', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KTA008', N'Tay bèo', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KTA009', N'Tay sát nách', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KTA010', N'Tay chẽn', 1, '2024-01-01', 'admin', '2024-01-02', 'admin');


insert into kich_co (ma, ten, trang_thai, create_date, create_by, update_date, update_by)
values 
       ('KC001', N'XS', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KC002', N'S', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KC003', N'M', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KC004', N'L', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KC005', N'XL', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KC006', N'XXL', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KC007', N'XXXL', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KC008', N'4XL', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KC009', N'5XL', 1, '2024-01-01', 'admin', '2024-01-02', 'admin'),
       ('KC010', N'6XL', 1, '2024-01-01', 'admin', '2024-01-02', 'admin');


insert into san_pham_chi_tiet (ma, ten, trang_thai, create_date, create_by, update_date, update_by, so_luong, don_gia, mo_ta, hinh_anh, id_san_pham, id_mau_sac, id_thuong_hieu, id_kieu_dang, id_xuat_xu, id_kich_co, id_kieu_co_ao, id_kieu_tay_ao, id_chat_lieu)
values ('SPCT001', N'Áo Thun Đỏ S', 1, '2024-01-01', 'admin', '2024-01-02', 'admin', 100, 150000, 'Áo thun đỏ size S', '/images/aothun_do.jpg', 1, 1, 1, 1, 1, 1, 1, 1, 1),
       ('SPCT002', N'Áo Thun Xanh M', 1, '2024-01-01', 'admin', '2024-01-02', 'admin', 80, 160000, 'Áo thun xanh size M', '/images/aothun_xanh.jpg', 1, 2, 1, 2, 2, 2, 2, 2, 2),
       ('SPCT003', N'Quần Jean Đen S', 1, '2024-01-01', 'admin', '2024-01-02', 'admin', 60, 300000, 'Quần jean đen size S', '/images/jean_den.jpg', 2, 5, 3, 3, 3, 3, 3, 3, 3),
       ('SPCT004', N'Áo Khoác Xám L', 1, '2024-01-01', 'admin', '2024-01-02', 'admin', 40, 500000, 'Áo khoác xám size L', '/images/khoac_xam.jpg', 3, 7, 2, 4, 4, 4, 4, 4, 4),
       ('SPCT005', N'Mũ Lưỡi Trai Đỏ', 1, '2024-01-01', 'admin', '2024-01-02', 'admin', 200, 80000, 'Mũ lưỡi trai đỏ', '/images/mu_do.jpg', 5, 1, 5, 5, 5, 5, 5, 5, 5),
       ('SPCT006', N'Váy Ngắn Hồng', 1, '2024-01-01', 'admin', '2024-01-02', 'admin', 30, 250000, 'Váy ngắn màu hồng', '/images/vay_hong.jpg', 8, 6, 6, 6, 6, 6, 6, 6, 6),
       ('SPCT007', N'Áo Len Nâu', 1, '2024-01-01', 'admin', '2024-01-02', 'admin', 25, 200000, 'Áo len màu nâu', '/images/ao_len_nau.jpg', 10, 10, 7, 7, 7, 7, 7, 7, 7),
       ('SPCT008', N'Quần Short Cam', 1, '2024-01-01', 'admin', '2024-01-02', 'admin', 120, 120000, 'Quần short cam', '/images/short_cam.jpg', 7, 8, 8, 8, 8, 8, 8, 8, 8),
       ('SPCT009', N'Áo Polo Tím', 1, '2024-01-01', 'admin', '2024-01-02', 'admin', 70, 180000, 'Áo polo tím', '/images/polo_tim.jpg', 9, 9, 9, 9, 9, 9, 9, 9, 9),
       ('SPCT010', N'Áo Sơ Mi Trắng', 1, '2024-01-01', 'admin', '2024-01-02', 'admin', 90, 220000, 'Áo sơ mi trắng', '/images/so_mi_trang.jpg', 6, 4, 10, 10, 10, 10, 10, 10, 10);


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



INSERT INTO hoa_don (id_khuyen_mai, id_trang_thai, id_phuong_thuc_thanh_toan, id_don_hang, id_nhan_vien, id_khach_hang, ma_hoa_don, create_by, tong_tien, tong_tien_khuyen_mai, tong_tien_sau_khuyen_mai, ghi_chu, trang_thai_thanh_toan)
VALUES 
(1, 1, 1, 1, 1, 1, 'HD001', '2023-01-10', 750000, 50000, 700000, N'Hóa đơn 1', 1),
(NULL, 2, 2, 2, 2, 2, 'HD002', '2023-01-11', 1200000, 100000, 1100000, N'Hóa đơn 2', 1);



INSERT INTO hoa_don_chi_tiet (ma_hoa_don_chi_tiet, id_hoa_don, id_san_pham_chi_tiet, so_luong, don_gia, trang_thai, ghi_chu)
VALUES 
('HDCT001', 1, 1, 2, 200000, 1, N'Áo thun xanh'),
('HDCT002', 2, 2, 1, 350000, 1, N'Quần jean đen');

