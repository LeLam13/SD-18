var app = angular.module('chiTietSP-app', []);
app.controller('chiTietSP-ctrl', function ($scope, $http) {

    $scope.items = [];
    $scope.page = 0;  // Trang hiện tại
    $scope.size = 4; // Số lượng bản ghi trên mỗi trang
    $scope.totalPages = 0; // Tổng số trang
    $scope.pageInput = 1; // Giá trị nhập từ ô input
    $scope.filterData = {};

    const pathName = window.location.pathname.split('/');
    var idSanPham = pathName[pathName.length - 1];

    $scope.findAll = function () {
        var url = `/admin/san-pham/` + idSanPham + `/find-all?page=${$scope.page}&size=${$scope.size}`;
        $http.get(url).then(resp => {
            $scope.items = resp.data.content;
            $scope.totalPages = resp.data.totalPages; // Cập nhật tổng số trang
        }).catch(error => {
            console.log(error);
        });
    };

    $scope.getThuocTinh = function () {
        $http.get("/admin/mau-sac/get-all").then(r => {
            $scope.mauSac = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/chat-lieu/get-all").then(r => {
            $scope.chatLieu = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/thuong-hieu/get-all").then(r => {
            $scope.thuongHieu = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/xuat-xu/get-all").then(r => {
            $scope.xuatXu = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/kieu-dang/get-all").then(r => {
            $scope.kieuDang = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/size/get-all").then(r => {
            $scope.kichCo = r.data;
        }).catch(e => console.log(e))
    }

    $scope.getThuocTinh();

    $scope.getAll = function () {
        var url = `/admin/san-pham/{idSanPham}/get-all`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;
        }).catch(error => {
            console.log(error);
        });
    };

    $scope.findAll();

    $scope.loadData = function () {
        if (Object.keys($scope.filterData).length > 0) {
            $scope.filter($scope.filterData);
        } else {
            $scope.findAll();
        }
    };

    $scope.previousPage = function () {
        if ($scope.page > 0) {
            $scope.page--;
            $scope.loadData();
        }
    };

    $scope.nextPage = function () {
        if ($scope.page < $scope.totalPages - 1) {
            $scope.page++;
            $scope.loadData();
        }
    };

    $scope.goToFirstPage = function () {
        if ($scope.page > 0) {
            $scope.page = 0;
            $scope.loadData();
        }
    };

    $scope.goToLastPage = function () {
        if ($scope.page < $scope.totalPages - 1) {
            $scope.page = $scope.totalPages - 1;
            $scope.loadData();
        }
    };

    $scope.getSanPhamChiTiet = function (ma) {
        var url = "/admin/san-pham/chi-tiet/view" + "/" + ma;
        console.log(url)
        $http.get(url).then(function (r) {
            console.log(r.data)
            $scope.spct = r.data;
            // console.log("check:",$scope.spct.idMauSac);
        })
    }

    // Hàm kiểm tra sự tồn tại của các giá trị
    $scope.checkIfExists = function () {
        var checks = [];

        // Kiểm tra từng trường (chỉ cần kiểm tra các trường có giá trị hợp lệ)
        if ($scope.spct.idMauSac && $scope.spct.idMauSac.idMauSac) {
            checks.push($http.get("/admin/san-pham/chi-tiet", {
                params: {
                    idMauSac: $scope.spct.idMauSac.idMauSac,
                    excludeId: $scope.spct.ma
                }
            }));
        }

        if ($scope.spct.idThuongHieu && $scope.spct.idThuongHieu.idThuongHieu) {
            checks.push($http.get("/admin/san-pham/chi-tiet", {
                params: {
                    idThuongHieu: $scope.spct.idThuongHieu.idThuongHieu,
                    excludeId: $scope.spct.ma
                }
            }));
        }

        if ($scope.spct.idKieuDang && $scope.spct.idKieuDang.idKieuDang) {
            checks.push($http.get("/admin/san-pham/chi-tiet", {
                params: {
                    idKieuDang: $scope.spct.idKieuDang.idKieuDang,
                    excludeId: $scope.spct.ma
                }
            }));
        }

        if ($scope.spct.idChatLieu && $scope.spct.idChatLieu.idChatLieu) {
            checks.push($http.get("/admin/san-pham/chi-tiet", {
                params: {
                    idChatLieu: $scope.spct.idChatLieu.idChatLieu,
                    excludeId: $scope.spct.ma
                }
            }));
        }

        if ($scope.spct.idKichCo && $scope.spct.idKichCo.idKichCo) {
            checks.push($http.get("/admin/san-pham/chi-tiet", {
                params: {
                    idKichCo: $scope.spct.idKichCo.idKichCo,
                    excludeId: $scope.spct.ma
                }
            }));
        }

        if ($scope.spct.idXuatXu && $scope.spct.idXuatXu.idXuatXu) {
            checks.push($http.get("/admin/san-pham/chi-tiet", {
                params: {
                    idXuatXu: $scope.spct.idXuatXu.idXuatXu,
                    excludeId: $scope.spct.ma
                }
            }));
        }

        // Kiểm tra tất cả các giá trị cùng một lúc
        return $q.all(checks).then(function (responses) {
            // Kiểm tra tất cả các phản hồi, nếu có bất kỳ giá trị trùng nào, trả về false
            for (let response of responses) {
                if (response.data && response.data.length > 0) {
                    return false; // Nếu tìm thấy sản phẩm trùng với giá trị, trả về false
                }
            }
            return true; // Nếu không có giá trị trùng, trả về true
        }).catch(function () {
            return false; // Nếu có lỗi xảy ra trong khi kiểm tra
        });
    };


    $scope.update = function (ma) {
        var url = "/admin/san-pham/chi-tiet/update" + "/" + ma;
        var updateSPCT = {
            ma: ma,
            soLuong: $scope.spct.soLuong,
            giaBan: $scope.spct.giaBan,
            idMauSac: $scope.spct.idMauSac.idMauSac,
            // idThuongHieu: $scope.spct.idThuongHieu.idThuongHieu,
            // idKieuDang: $scope.spct.idKieuDang.idKieuDang,
            // idChatLieu: $scope.spct.idChatLieu.idChatLieu,
            // idXuatXu: $scope.spct.idXuatXu.idXuatXu,
            idKichCo: $scope.spct.idKichCo.idKichCo,
            idHinhAnh: $scope.spct.idHinhAnh.idHinhAnh
        }
        $http.post(url, updateSPCT).then(function (r) {
            alert("Update thành công");
            console.log($scope.spct)
            $scope.findAll();
        }).catch(function (err) {
            console.log("Update khong thanh cong", err);
        })
    }

    $scope.updateTT = function (idSanPhamChiTiet) {
        if (confirm("Xác nhận đổi?")) {
            var url = "/admin/san-pham/chi-tiet/updateTT" + "/" + idSanPhamChiTiet;
            $http.post(url).then(function (r) {
                alert("Doi thành công!!!")
                $scope.findAll();
            }).catch(function (err) {
                console.log("Loi: ", err);
            })
        }
    }


    $scope.navigateToForm = function () {
        window.location.href = '/admin/san-pham/formAdd/' + idSanPham;
    };

    $scope.navigateToHistory = function () {
        window.location.href = '/admin/lich-su-nhap-hang/add/' + idSanPham;
    };

    $scope.navigateToViewHistory = function () {
        window.location.href = '/admin/lich-su-nhap-hang/view/' + idSanPham;
    };

    $scope.back = function () {
        window.location.href = '/admin/san-pham';
    };

    // Hàm lọc sản phẩm
    $scope.filter = function (filterData) {
        // Loại bỏ các thuộc tính không hợp lệ (rỗng/null)
        for (const [key, value] of Object.entries(filterData)) {
            if (!value || value.length === 0) {
                delete filterData[key];
            }
        }

        filterData.idSanPham = idSanPham;
        console.log("Dữ liệu lọc: ", filterData);

        // Gửi yêu cầu lọc đến server với phân trang
        $http.post(`/admin/san-pham/chi-tiet/filter?page=${$scope.page}&size=${$scope.size}`, filterData).then(function (response) {
            $scope.items = response.data.content; // Gán danh sách sản phẩm sau khi lọc
            $scope.totalPages = response.data.totalPages; // Tổng số trang
            $scope.pageNumber = 0; // Reset lại trang hiện tại sau khi lọc
            console.log("Dữ liệu lọc: ", $scope.items);

            // Hiển thị số bộ lọc đang được áp dụng
            if (Object.keys(filterData).length > 1) {
                document.getElementById('lengthFilter').innerText = Object.keys(filterData).length - 1;
            } else {
                document.getElementById('lengthFilter').innerText = "";
            }

        }).catch(function (error) {
            console.error("Lỗi khi lọc sản phẩm:", error);
            alertify.error("Không thể lọc sản phẩm. Vui lòng thử lại sau!");
        });
    };


    // Hàm xóa bộ lọc
    $scope.clearFilter = function () {
        $scope.filterData = {}; // Reset dữ liệu lọc
        document.getElementById('lengthFilter').innerText = "";
        // $scope.filter($scope.filterData); // Gọi lại hàm lọc để làm mới danh sách
        $scope.findAll();
    };

    // Hàm lấy danh sách số trang (nếu muốn phân trang)
    $scope.getPageNumbers = function (totalPages) {
        $scope.pageNumbers = Array.from({length: totalPages}, (_, i) => i + 1);
    };


    $scope.getTextColor = function (color) {
        // Kiểm tra độ sáng của màu nền để chọn màu chữ (đen hoặc trắng)
        var r = parseInt(color.substring(1, 3), 16);
        var g = parseInt(color.substring(3, 5), 16);
        var b = parseInt(color.substring(5, 7), 16);

        // Tính độ sáng của màu (theo công thức Y = 0.2126*R + 0.7152*G + 0.0722*B)
        var brightness = (0.2126 * r + 0.7152 * g + 0.0722 * b);

        // Nếu độ sáng > 128 thì chọn màu chữ đen, ngược lại chọn trắng
        return brightness > 128 ? 'black' : 'white';
    };

    $scope.generateRandomString = function (length) {
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
        let result = '';

        for (let i = 0; i < length; i++) {
            const randomIndex = Math.floor(Math.random() * characters.length);
            result += characters[randomIndex];
        }

        return result;
    };


    $scope.getHinhAnh = function (HinhAnh) {
        console.log("getHinhAnh gọi với:", HinhAnh);

        if (!HinhAnh || !HinhAnh.ten) {
            console.log("Không có tên ảnh.");
            return Promise.resolve(null);  // Trả về null nếu không có tên
        }

        return $http.get("/admin/hinh-anh", {params: {ten: HinhAnh.ten}})
            .then(function (response) {
                console.log("API trả về:", response.data);
                const existingImage = response.data;

                if (!existingImage) {
                    var addHinhAnh = {
                        ma: $scope.generateRandomString(8),
                        ten: HinhAnh.ten
                    };

                    return $http.post("/admin/hinh-anh/add", addHinhAnh).then(function (addResponse) {
                        return $http.get("/admin/hinh-anh", {params: {ten: HinhAnh.ten}}).then(function (getResponse) {
                            console.log("Dữ liệu ảnh mới với ID:", getResponse.data);
                            return getResponse.data; // Trả về dữ liệu ảnh mới với idHinhAnh
                        });
                    });
                } else {
                    return existingImage;  // Trả về ảnh đã có
                }
            })
            .catch(function (err) {
                console.error("Lỗi khi gọi API:", err);
                return Promise.resolve(null);  // Đảm bảo Promise vẫn trả về, không gây lỗi
            });
    };


    $scope.triggerFileInput = function (idSanPhamChiTiet) {
        // Tìm thẻ input file tương ứng và kích hoạt click
        const fileInput = document.getElementById("formFile-" + idSanPhamChiTiet);
        if (fileInput) {
            fileInput.click();
        } else {
            console.error("Không tìm thấy input file với id:", idSanPhamChiTiet);
        }
    };

    $scope.onFileSelected1 = function (event) {
        console.log("File selection initiated.");
        console.log("check obj", $scope.spct);
        // Lấy file từ input
        var file = event.target.files[0];
        if (!file) {
            console.warn("Không có tệp nào được chọn.");
            $scope.selectedFileName = null; // Xoá biến tạm
            $scope.$applyAsync();
            return;
        }

        // Lưu tên file vào biến tạm
        $scope.selectedFileName = file.name;
        console.log("File được chọn:", $scope.selectedFileName);

        var HinhAnh = {ten: file.name};
        console.log("HinhAnh được chọn:", HinhAnh);
        // Gọi hàm kiểm tra xem HinhAnh đã tồn tại hay chưa
        $scope.getHinhAnh(HinhAnh).then(function (result) {
            console.log("check result:", result);
            if (result) {
                // Nếu tìm thấy, gán idHinhAnh vào spct
                $scope.spct.idHinhAnh = result;
                console.log("Tìm thấy idHinhAnh:", $scope.spct.idHinhAnh);
            } else {
                // Nếu không tìm thấy, xử lý khác (ví dụ: thông báo lỗi)
                console.warn("Hình ảnh không tồn tại trong cơ sở dữ liệu.");
                $scope.spct.idHinhAnh = null;
            }

            $scope.$applyAsync(); // Cập nhật view
        }).catch(function (err) {
            console.error("Lỗi khi gọi getHinhAnh:", err);
            $scope.spct.idHinhAnh = null;
            $scope.$applyAsync();
        });
    };


    $scope.getThuocTinhCT = function () {
        $http.get("/admin/mau-sac/get-all-by-chi-tiet/" + idSanPham).then(r => {
            $scope.mauSacCT = r.data;
        }).catch(e => console.log(e))


        $http.get("/admin/size/get-all-by-chi-tiet/" + idSanPham).then(r => {
            $scope.kichCoCT = r.data;
        }).catch(e => console.log(e))
    }

    $scope.getThuocTinhCT();

<<<<<<< HEAD

    $scope.getThuocTinhByTrangThai = function () {
        $http.get("/admin/mau-sac/get-all/trang-thai").then(r => {
            $scope.mauSacCr = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/chat-lieu/get-all/trang-thai").then(r => {
            $scope.chatLieuCr = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/thuong-hieu/get-all/trang-thai").then(r => {
            $scope.thuongHieuCr = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/xuat-xu/get-all/trang-thai").then(r => {
            $scope.xuatXuCr = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/kieu-dang/get-all/trang-thai").then(r => {
            $scope.kieuDangCr = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/size/get-all/trang-thai").then(r => {
            $scope.kichCoCr = r.data;
        }).catch(e => console.log(e))
    }

    $scope.getThuocTinhByTrangThai();

=======
>>>>>>> feature/khuyenmai
    $scope.updateKichCo = function () {
        if (!$scope.idMauSacNhap) {
            // Nếu không chọn màu sắc, hiển thị tất cả kích cỡ
            $scope.filteredKichCo = $scope.kichCoCT;
            return;
        }

        const idMauSac = $scope.idMauSacNhap.idMauSac;

        $http.get(`/admin/size/get-all-by-mau-sac/` + idSanPham, {
            params: { idMauSac: idMauSac}
        }).then(r => {
            $scope.filteredKichCo = r.data; // Cập nhật danh sách kích cỡ
            $scope.idKichCoNhap = null; // Reset lựa chọn kích cỡ
        }).catch(e => console.log(e));
    };

    $scope.updateByNhap = function () {
        var NhapHang = {
            idSanPham: idSanPham,
            ma: $scope.generateRandomString(8),
            soLuongNhap: $scope.soLuongNhap,
            giaNhapNhap: $scope.giaNhapNhap,
            idMauSacNhap: $scope.idMauSacNhap.idMauSac,
            idKichCoNhap: $scope.idKichCoNhap.idKichCo
        }
        $http.post("/admin/lich-su-nhap-hang/add", NhapHang).then(r => {
            var upDateCT = {
                idSanPham: idSanPham,
                soLuong: $scope.soLuongNhap,
                giaNhap: $scope.giaNhapNhap,
                idMauSac: $scope.idMauSacNhap.idMauSac,
                idKichCo: $scope.idKichCoNhap.idKichCo
            }
            $http.post("/admin/san-pham/chi-tiet/updateNhap", upDateCT).then(r => {
                alert("Them thanh cong");
                $scope.viewChiTiet();
            })
        }).catch(function (err) {
            console.error("Lỗi khi gọi :", err);
        });
    }

    $scope.viewChiTiet = function () {
        // Chuyển hướng đến trang chi tiết sản phẩm
        location.href = `/admin/san-pham/` + idSanPham;
    };


    $scope.lichSu = [];
    $scope.getLichSuNhap = function () {
        $http.get(`/admin/lich-su-nhap-hang/` + idSanPham + `/find-all?page=${$scope.page}&size=${$scope.size}`)
            .then(function (response) {
                $scope.lichSu = response.data.content;
                $scope.totalPages = response.data.totalPages;
                console.log("check lich su:", $scope.lichSu)
            }).catch(error => {
            console.log(error);
        });
    }

    $scope.getLichSuNhap();

// Hàm kiểm tra sự tồn tại của các giá trị
    $scope.checkIfExists = function () {
        var checks = [];

        // Kiểm tra từng trường
        if ($scope.spct.idMauSac && $scope.spct.idMauSac.idMauSac) {
            checks.push($http.get("/admin/mau-sac", {params: {idMauSac: $scope.spct.idMauSac.idMauSac}}));
        }

        if ($scope.spct.idThuongHieu && $scope.spct.idThuongHieu.idThuongHieu) {
            checks.push($http.get("/admin/thuong-hieu", {params: {idThuongHieu: $scope.spct.idThuongHieu.idThuongHieu}}));
        }

        if ($scope.spct.idKieuDang && $scope.spct.idKieuDang.idKieuDang) {
            checks.push($http.get("/admin/kieu-dang", {params: {idKieuDang: $scope.spct.idKieuDang.idKieuDang}}));
        }

        if ($scope.spct.idChatLieu && $scope.spct.idChatLieu.idChatLieu) {
            checks.push($http.get("/admin/chat-lieu", {params: {idChatLieu: $scope.spct.idChatLieu.idChatLieu}}));
        }

        if ($scope.spct.idKichCo && $scope.spct.idKichCo.idKichCo) {
            checks.push($http.get("/admin/kich-co", {params: {idKichCo: $scope.spct.idKichCo.idKichCo}}));
        }

        if ($scope.spct.idXuatXu && $scope.spct.idXuatXu.idXuatXu) {
            checks.push($http.get("/admin/xuat-xu", {params: {idXuatXu: $scope.spct.idXuatXu.idXuatXu}}));
        }

        // Kiểm tra tất cả các giá trị cùng một lúc
        return $q.all(checks).then(function (responses) {
            // Nếu có ít nhất 1 kết quả trả về lỗi, thì coi như không hợp lệ
            for (let response of responses) {
                if (!response.data || response.data.length === 0) {
                    return false; // Trả về false nếu không tìm thấy dữ liệu
                }
            }
            return true; // Tất cả đều hợp lệ
        }).catch(function () {
            return false; // Nếu có lỗi xảy ra trong khi kiểm tra
        });
    };



//     const pathName = window.location.pathname.split('/');
//     const idSP = pathName[pathName.length - 1]
//
//     $scope.items =[];
//     $scope.form ={
//         sanPham : idSP
//     };
//     $scope.sizes = [];
//     $scope.itemUpdate = {};
//
//     $scope.getAll = function (){
//         $http.get("/admin/san-pham/"+idSP+"/get-all").then(r => {
//             $scope.items = r.data;
//         }).catch(e => console.log(e))
//     }
//     $scope.getSizes = function (){
//         $http.get("/admin/san-pham/"+idSP+"/test").then(r => {
//             $scope.sizes = r.data;
//         }).catch(e => console.log(e))
//     }
//     $scope.getAll();
//     $scope.getSizes();
//
//
//     // $scope.delete = function (ma){
//     //     $http.delete("/admin/san-pham/delete/"+ma).then(r => {
//     //         var index = $scope.items.findIndex(i => i.ma == ma);
//     //         console.log(index)
//     //         $scope.items.splice(index,1);
//     //         // $scope.getAll();
//     //     }).catch(e => console.log(e));
//     // }
//
//     $scope.getChiTietSP = function (ma){
//         location.href = "/admin/san-pham/"+ma;
//     }
//
//     //Thêm
//     $scope.add = function (){
//
//         let data =[];
//         let sizesInForm = $scope.form.sizes
//         if(sizesInForm == undefined) sizesInForm = [];
//
//             $http.post("/admin/san-pham/"+idSP+"/add?sizes="+sizesInForm,{
//                 soLuong : $scope.form.soLuong
//             }).then(r =>{
//                 $scope.removeSizeInForm(sizesInForm);
//                 $scope.items = $scope.items.concat(r.data);
//                 $scope.form.soLuong = ""
//                 alertify.success("Thêm thành công "+sizesInForm.length+" chi tiết sản phẩm")
//             }).catch(e => {
//                 document.getElementById("eSize").innerText = e.data.eSize == undefined ? "" : e.data.eSize
//                 document.getElementById("eSoLuong").innerText =  e.data.soLuong == undefined ? "" : e.data.soLuong
//                 console.log(e)
//                 alertify.error("Thêm thất bại")
//             })
//     }
//
//     //Xóa
//     $scope.delete = function (item){
//         console.log(item)
//         alertify.confirm("Xóa chi tiết sản phẩm size "+item.size, function () {
//             $http.delete("/admin/san-pham/"+idSP+"/delete/"+item.id).then(r => {
//                 let index = $scope.items.findIndex(i => i.id == item.id);
//                 $scope.items.splice(index,1);
//                 $scope.getSizes();
//                 alertify.success("Xóa thành công chi tiết sản phẩm size "+item.size);
//             }).catch(e => {
//                 alertify.error("Xóa thất bại")
//                 console.log(e);
//             })
//
//         }, function () {
//             alertify.error("Xóa thất bại")
//         })
//     }
//
//     //Cập nhật
//     $scope.viewUpdate = function (item){
//         $scope.itemUpdate = angular.copy(item)
//         console.log($scope.itemUpdate)
//     }
//     $scope.update = function (){
//             alertify.confirm("Cập nhật chi tiết sản phẩm size "+$scope.itemUpdate.size+" ?", function () {
//                 // $scope.itemUpdate.sanPham = idSP
//                 $http.put("/admin/san-pham/"+idSP+"/update",$scope.itemUpdate).then(r =>{
//                     let index = $scope.items.findIndex(i => i.id == $scope.itemUpdate.id)
//                     $scope.items[index] = r.data
//                     console.log("cl", $("#cancelModal").click())
//                     alertify.success("Cập nhật thành công số lượng size "+$scope.itemUpdate.size)
//                 }).catch(e => {
//                     document.getElementById("eSoLuongUpdate").innerText = e.data.soLuong
//                     console.log(e)
//                     alertify.error("Cập nhật thất bại")
//                 })
//             }, function () {
//                 alertify.error("Cập nhật thất bại")
//             })
//     }
//     $scope.updateSlInTable = function (soLuong,id){
//         console.log(id)
//         $scope.itemUpdate.id = id
//         $scope.itemUpdate.soLuong = parseInt(soLuong);
//         console.log($scope.itemUpdate)
//         $http.put("/admin/san-pham/"+idSP+"/update",$scope.itemUpdate).then(r =>{
//             alertify.success("Cập nhật số lượng thành công")
//         }).catch(e => {
//             $http.get("/admin/san-pham/"+idSP+"/getSoLuong/"+id).then(r => {
//                 document.getElementById(id+"").value = r.data.soLuong
//             })
//             alertify.error(e.data.soLuong)
//         })
//     }
//
//     $scope.removeSizeInForm = function (size){
//         for (let i = 0 ;i< size.length;i++){
//             let index = $scope.sizes.findIndex(s => s.ma == size[i])
//             $scope.sizes.splice(index,1);
//         }
//     }
//     $scope.removeER = function (id){
//         document.getElementById(id).innerText = "";
//     }
//
//     $scope.selectAllSize = function (){
//         let elm = document.getElementById("sizeSL")
//         if(elm.selectedIndex == 0) {
//             elm.options[0].selected = false;
//             console.log(elm.options[0].selected)
//             for (let i = 0; i < elm.options.length; i++) {
//                 elm.options[i].selected = true;
//             }
//         }
//     }
// });
// $( '#sizeSL' ).select2( {
//     theme: "bootstrap-5",
//     width: $( this ).data( 'width' ) ? $( this ).data( 'width' ) : $( this ).hasClass( 'w-100' ) ? '100%' : 'style',
//     placeholder: $( this ).data( 'placeholder' ),
//     closeOnSelect: false,
//     allowClear: true,
});






