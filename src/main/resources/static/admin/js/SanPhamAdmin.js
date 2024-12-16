var app = angular.module('product-admin', []);
app.controller('ctrl', function ($scope, $http) {

    $scope.items = []
    $scope.page = 0;  // Trang hiện tại
    $scope.size = 4; // Số lượng bản ghi trên mỗi trang
    $scope.totalPages = 0; // Tổng số trang
    $scope.pageInput = 1; // Giá trị nhập từ ô input
    $scope.selectedChatLieu = "";
    $scope.selectedKieuDang = "";
    $scope.selectedThuongHieu = "";
    $scope.selectedXuatXu = "";
    $scope.moTa = "";

    $scope.findAll = function () {
        var url = `/admin/san-pham/find-all?page=${$scope.page}&size=${$scope.size}`;
        $http.get(url).then(resp => {
            $scope.items = resp.data.content;
            $scope.totalPages = resp.data.totalPages; // Cập nhật tổng số trang
        }).catch(error => {
            console.log(error);
        });
    };


    // Hàm chuyển tới trang trước
    $scope.previousPage = function () {
        if ($scope.page > 0) {
            $scope.page--;
            $scope.findAll();
        }
    };

    // Hàm chuyển tới trang sau
    $scope.nextPage = function () {
        if ($scope.page < $scope.totalPages - 1) {
            $scope.page++;
            $scope.findAll();
        }
    };

    // Hàm chuyển tới trang đầu
    $scope.goToFirstPage = function () {
        if ($scope.page > 0) { // Kiểm tra nếu không phải trang đầu
            $scope.page = 0;
            $scope.findAll();
        }
    };

// Hàm chuyển tới trang cuối
    $scope.goToLastPage = function () {
        if ($scope.page < $scope.totalPages - 1) { // Kiểm tra nếu không phải trang cuối
            $scope.page = $scope.totalPages - 1;
            $scope.findAll();
        }
    };

    $scope.findAll();

    $scope.viewChiTiet = function (idSanPham) {
        // Chuyển hướng đến trang chi tiết sản phẩm
        location.href = `/admin/san-pham/` + idSanPham;
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

    $scope.create = function () {
        var SanPham = {
            ma: $scope.generateRandomString(8),
            ten: $scope.ten,
            idXuatXu: $scope.selectedXuatXu || null,
            idChatLieu: $scope.selectedChatLieu || null,
            idKieuDang: $scope.selectedKieuDang || null,
            idThuongHieu: $scope.selectedThuongHieu || null,
            moTa: $scope.moTa
        };

        // Biến flag để kiểm tra tính hợp lệ
        var isValid = true;

        // Kiểm tra tính hợp lệ của tên
        if (!$scope.ten || $scope.ten.trim().length === 0) {
            document.getElementById("eTenMau").innerText = "Vui lòng nhập tên!!!";
            isValid = false;
        } else if ($scope.ten.length > 30) {
            document.getElementById("eTenMau").innerText = "Tên tối đa 30 ký tự!!!";
            isValid = false;
        } else {
            document.getElementById("eTenMau").innerText = "";
        }

        // Kiểm tra tên có chứa ký tự đặc biệt
        const containsSpecialChar = /[^a-zA-Z\s]/; // Biểu thức kiểm tra ký tự đặc biệt
        if (containsSpecialChar.test($scope.ten)) {
            document.getElementById('eTenMau').innerText = "Tên không được chứa ký tự đặc biệt";
            isValid = false;
        }else {
            document.getElementById("eTenMau").innerText = "";
        }
        // Kiểm tra rỗng các trường select
        if (!$scope.selectedXuatXu) {
            document.getElementById("eXuatXu").innerText = "Vui lòng chọn xuất xứ!!!";
            isValid = false;
        } else {
            document.getElementById("eXuatXu").innerText = "";
        }

        if (!$scope.selectedChatLieu) {
            document.getElementById("eChatLieu").innerText = "Vui lòng chọn chất liệu!!!";
            isValid = false;
        } else {
            document.getElementById("eChatLieu").innerText = "";
        }

        if (!$scope.selectedThuongHieu) {
            document.getElementById("eThuongHieu").innerText = "Vui lòng chọn thương hiệu!!!";
            isValid = false;
        } else {
            document.getElementById("eThuongHieu").innerText = "";
        }

        if (!$scope.selectedKieuDang) {
            document.getElementById("eKieuDang").innerText = "Vui lòng chọn kiểu dáng!!!";
            isValid = false;
        } else {
            document.getElementById("eKieuDang").innerText = "";
        }

        // Kiểm tra tính hợp lệ của mô tả
        if (!$scope.moTa || $scope.moTa.trim().length === 0) {
            document.getElementById("eMoTa").innerText = "Vui lòng nhập mô tả!!!";
            isValid = false;
        } else if ($scope.moTa.length > 500) {
            document.getElementById("eMoTa").innerText = "Mô tả tối đa 500 ký tự!!!";
            isValid = false;
        } else {
            document.getElementById("eMoTa").innerText = "";
        }

        // Nếu có lỗi, dừng thực hiện
        if (!isValid) return;

        // Gọi getAll để kiểm tra xem tên đã tồn tại chưa
        $http.get("/admin/san-pham/get-all").then(function (response) {
            var existingSanPham = response.data;
            var tenTonTai = existingSanPham.some(function (item) {
                return item.ten.toLowerCase() === $scope.ten.toLowerCase();
            });

            if (tenTonTai) {
                document.getElementById("eTenMau").innerText = "Tên đã tồn tại";
            } else {
                // Nếu không, gửi yêu cầu tạo mới
                $http.post("/admin/san-pham/add", SanPham)
                    .then(function (r) {
                        $scope.findAll();
                        alert("Thêm thành công");
                    })
                    .catch(function (err) {
                        console.log("Thêm không thành công", err);
                    });
            }
        }).catch(function (err) {
            console.log("Lỗi khi lấy dữ liệu", err);
        });
    };


    // Hàm reset form
    $scope.resetForm = function () {
        $scope.ten = '';
        $scope.selectedXuatXu = null;
        $scope.selectedChatLieu = null;
        $scope.selectedKieuDang = null;
        $scope.selectedThuongHieu = null;
        $scope.moTa = '';
        document.getElementById("eTenMau").innerText = "";
        document.getElementById("eXuatXu").innerText = "";
        document.getElementById("eChatLieu").innerText = "";
        document.getElementById("eThuongHieu").innerText = "";
        document.getElementById("eKieuDang").innerText = "";
        document.getElementById("eMoTa").innerText = "";
    };

    $('#spModal').on('hide.bs.modal', function () {
        angular.element(this).scope().resetForm(); // Reset dữ liệu trong AngularJS
        angular.element(this).scope().$apply();
    });



    $scope.getSanPham = function (ma) {
        var url = "/admin/san-pham/chiTiet" + "/" + ma;
        $http.get(url).then(function (r) {
            console.log(r.data)
            $scope.sp = r.data;
            $scope.ma= $scope.sp.ma
        })
    }


    $scope.resetErrors = function () {
        // Xóa các thông báo lỗi
        document.getElementById("eTenMauUd").innerText = "";
        document.getElementById("eMoTaUd").innerText = "";

    };


    $scope.update = function (ma) {
        // Tạo đối tượng sản phẩm để cập nhật
        var updateSanPham = {
            ma: ma,
            ten: $scope.sp.ten,
            idXuatXu: $scope.sp.idXuatXu.idXuatXu,
            idChatLieu: $scope.sp.idChatLieu.idChatLieu,
            idKieuDang: $scope.sp.idKieuDang.idKieuDang,
            idThuongHieu: $scope.sp.idThuongHieu.idThuongHieu,
            moTa: $scope.sp.moTa
        }

        var isValid = true;

        // Kiểm tra tính hợp lệ của tên sản phẩm
        if (!$scope.sp.ten || $scope.sp.ten.trim().length === 0) {
            document.getElementById("eTenMauUd").innerText = "Vui lòng nhập tên!!!";
            isValid = false;
        } else if ($scope.sp.ten.length > 30) {
            document.getElementById("eTenMauUd").innerText = "Tên tối đa 30 ký tự!!!";
            isValid = false;
        } else {
            document.getElementById("eTenMauUd").innerText = "";
        }
        // Kiểm tra tên có chứa ký tự đặc biệt
        const containsSpecialChar = /[^a-zA-Z\s]/; // Biểu thức kiểm tra ký tự đặc biệt
        if (containsSpecialChar.test($scope.sp.ten)) {
            document.getElementById('eTenMauUd').innerText = "Tên không được chứa ký tự đặc biệt";
            isValid = false;
        }else {
            document.getElementById("eTenMauUd").innerText = "";
        }
        // Kiểm tra tính hợp lệ của mô tả
        if (!$scope.sp.moTa || $scope.sp.moTa.trim().length === 0) {
            document.getElementById("eMoTaUd").innerText = "Vui lòng nhập mô tả!!!";
            isValid = false;
        } else if ($scope.sp.moTa.length > 500) {
            document.getElementById("eMoTaUd").innerText = "Mô tả tối đa 500 ký tự!!!";
            isValid = false;
        } else {
            document.getElementById("eMoTaUd").innerText = "";
        }

        // Nếu có lỗi, dừng thực hiện
        if (!isValid) return;

        // Kiểm tra tên sản phẩm có trùng không
        $http.get("/admin/san-pham/get-all").then(function (response) {
            var existingSanPham = response.data;
            var tenTonTai = false;

            angular.forEach(existingSanPham, function (item) {
                // Kiểm tra xem tên sản phẩm đã tồn tại trong danh sách và không phải sản phẩm hiện tại
                if (item.ten && item.ten.toLowerCase() === $scope.sp.ten.toLowerCase() && item.ma !== ma) {
                    tenTonTai = true;
                }
            });

            // Nếu tên sản phẩm đã tồn tại, hiển thị thông báo lỗi
            if (tenTonTai) {
                document.getElementById("eTenMauUd").innerText = "Tên đã tồn tại";
                return;
            } else {
                // Nếu không có lỗi, gửi yêu cầu cập nhật sản phẩm
                var url = "/admin/san-pham/update" + "/" + ma;
                $http.post(url, updateSanPham).then(function (r) {
                    $scope.findAll(); // Cập nhật lại danh sách sản phẩm
                    alert("Update thành công");
                }).catch(function (err) {
                    console.log("Update không thành công", err);
                });
            }
        }).catch(function (err) {
            console.log("Lỗi khi lấy dữ liệu", err);
        });
    }


    function removeVietnameseTones(str) {
        return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/đ/g, 'd').replace(/Đ/g, 'D');
    }

    $scope.updateTT = function (idSanPham) {
        if (confirm("Xác nhận đổi?")) {
            var url = "/admin/san-pham/updateTT" + "/" + idSanPham;
            $http.post(url).then(function (r) {
                alert("Doi thành công!!!")
                $scope.findAll();
            }).catch(function (err) {
                console.log("Loi: ", err);
            })
        }
    }


    // $scope.delete = function (ma) {
    //
    //
    //     alertify.confirm("Xóa sản phẩm?", function () {
    //         $http.delete("/admin/san-pham/delete/" + ma).then(r => {
    //             alertify.success("Xóa thành công")
    //             if ($scope.pageNumber == $scope.totalPage - 1) {
    //                 if ($scope.items.length == 1 && $scope.pageNumber > 0) {
    //                     $scope.pageNumber -= 1;
    //                     $scope.totalPage -= 1;
    //                 }
    //             }
    //
    //             $scope.getPageNumbers($scope.totalPage)
    //             $scope.getAll($scope.pageNumber)
    //         }).catch(e => {
    //             alertify.error("Xóa thất bại")
    //             console.log(e)
    //         });
    //     }, function () {
    //         alertify.error("Xóa thất bại")
    //     })
    // }
    //
    // $scope.getChiTietSP = function (ma) {
    //     location.href = "/admin/san-pham/" + ma;
    // }

    $scope.updateTrangThaiHienThi = function (switchId, maSP) {
        let trangThai = document.getElementById(switchId).checked
        $http.put("/admin/san-pham/update-TrangThai-HienThi/" + maSP, trangThai).then(r => {
            alertify.success("Cập nhật thành công")
        }).catch(e => {
            alertify.error("Cập nhật thất bại")
            document.getElementById(switchId).checked = trangThai == true ? false : true
        });
    }

    $scope.getPropertiesInFilter = function () {
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
    }

    $scope.getPropertiesInFilter();

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

    $scope.getHinhAnh = function (HinhAnh) {
        console.log("getHinhAnh gọi với:", HinhAnh);

        if (!HinhAnh || !HinhAnh.ten) {
            console.log("Không có tên ảnh.");
            return Promise.resolve(null);  // Trả về null nếu không có tên
        }

        return $http.get("/admin/hinh-anh", { params: { ten: HinhAnh.ten } })
            .then(function (response) {
                console.log("API trả về:", response.data);
                const existingImage = response.data;

                if (!existingImage) {
                    var addHinhAnh = {
                        ma: $scope.generateRandomString(8),
                        ten: HinhAnh.ten
                    };

                    return $http.post("/admin/hinh-anh/add", addHinhAnh)
                        .then(function (addResponse) {
                            console.log("Dữ liệu ảnh mới:", addResponse.data);
                            return addResponse.data;
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

    $scope.triggerFileInput = function (idSanPham) {
        // Tìm thẻ input file tương ứng và kích hoạt click
        const fileInput = document.getElementById("formFile-" + idSanPham);
        if (fileInput) {
            fileInput.click();
        } else {
            console.error("Không tìm thấy input file với id:", idSanPham);
        }
    };

    $scope.onFileSelected1 = function (event) {
        console.log("File selection initiated.");
        console.log("check obj",$scope.sp);
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

        var HinhAnh = { ten: file.name };
        console.log("HinhAnh được chọn:", HinhAnh);
        // Gọi hàm kiểm tra xem HinhAnh đã tồn tại hay chưa
        $scope.getHinhAnh(HinhAnh).then(function (result) {
            console.log("check result:", result);
            if (result) {
                // Nếu tìm thấy, gán idHinhAnh vào sp
                $scope.sp.idHinhAnh = result;
                console.log("Tìm thấy idHinhAnh:", $scope.sp.idHinhAnh);
            } else {
                // Nếu không tìm thấy, xử lý khác (ví dụ: thông báo lỗi)
                console.warn("Hình ảnh không tồn tại trong cơ sở dữ liệu.");
                $scope.sp.idHinhAnh = null;
            }

            $scope.$applyAsync(); // Cập nhật view
        }).catch(function (err) {
            console.error("Lỗi khi gọi getHinhAnh:", err);
            $scope.sp.idHinhAnh = null;
            $scope.$applyAsync();
        });
    };


    $scope.selectedImage = {}; // Lưu ảnh được chọn
    $scope.idHinhAnhCr="";

    $scope.triggerFileInputCr = function () {
        const fileInput = document.getElementById('fileInput');
        if (fileInput) {
            fileInput.click();
        } else {
            console.error("Không tìm thấy input file.");
        }
    };

    $scope.onFileSelectedCr = function (event) {
        console.log("File selection initiated.");

        // Lấy file từ input
        var file = event.target.files[0];
        if (!file) {
            console.warn("Không có tệp nào được chọn.");
            $scope.selectedImage = null; // Xóa biến tạm
            $scope.$applyAsync();
            return;
        }

        // Lưu tên file vào biến tạm
        $scope.selectedImage = file.name;
        console.log("File được chọn:", $scope.selectedImage);

        var HinhAnh = { ten: file.name };
        console.log("HinhAnh được chọn:", HinhAnh);

        // Gọi hàm kiểm tra xem HinhAnh đã tồn tại hay chưa
        $scope.getHinhAnh(HinhAnh).then(function (result) {
            console.log("check result:", result);
            if (result) {
                // Nếu tìm thấy, gán idHinhAnh vào newProduct (thay vì $scope.sp)
                $scope.idHinhAnhCr = result;
                console.log("Tìm thấy idHinhAnh:", $scope.idHinhAnhCr);
            } else {
                // Nếu không tìm thấy, xử lý khác (ví dụ: thông báo lỗi)
                console.warn("Hình ảnh không tồn tại trong cơ sở dữ liệu.");
                $scope.idHinhAnhCr = null;
            }

            $scope.$applyAsync(); // Cập nhật view
        }).catch(function (err) {
            console.error("Lỗi khi gọi getHinhAnh:", err);
            $scope.idHinhAnhCr = null;  // Đảm bảo không bị null
            $scope.$applyAsync();
        });
    };

    // Hàm lọc sản phẩm
    $scope.filter = function (filterData) {
        // Loại bỏ các thuộc tính không hợp lệ (rỗng/null)
        for (const [key, value] of Object.entries(filterData)) {
            if (!value || value.length === 0) {
                delete filterData[key];
            }
        }

        // Gửi yêu cầu lọc đến server với phân trang
        $http.post(`/admin/san-pham/filter?page=${$scope.page}&size=${$scope.size}`, filterData).then(function (response) {
            $scope.items = response.data.content; // Gán danh sách sản phẩm sau khi lọc
            $scope.totalPages = response.data.totalPages; // Tổng số trang
            $scope.pageNumber = 0; // Reset lại trang hiện tại sau khi lọc
            console.log("Dữ liệu lọc: ", $scope.items);

            // Hiển thị số bộ lọc đang được áp dụng
            if (Object.keys(filterData).length > 0) {
                document.getElementById('lengthFilter').innerText = Object.keys(filterData).length;
            } else {
                document.getElementById('lengthFilter').innerText = "";
            }

        }).catch(function (error) {
            console.error("Lỗi khi lọc sản phẩm:", error);
            alertify.error("Không thể lọc sản phẩm!");
        });
    };


    // Hàm xóa bộ lọc
    $scope.clearFilter = function () {
        $scope.filterData = {}; // Reset dữ liệu lọc
        document.getElementById('lengthFilter').innerText = "";
        // $scope.filter($scope.filterData); // Gọi lại hàm lọc để làm mới danh sách
        $scope.findAll();
    };

    // $scope.filter = function (filterData) {
    //     for (const [key, value] of Object.entries(filterData)) {
    //         if (value.length == 0) delete filterData[key]
    //     }
    //     console.log(filterData)
    //     console.log(isNaN(filterData.giaBan))
    //     if (filterData.giaBan != undefined) {
    //         if (isNaN(filterData.giaBan)) {
    //             alertify.error("Giá min phải là số!!")
    //             return
    //         } else {
    //             if (filterData.giaBan < 10000) {
    //                 alertify.error("Giá min phải > 10.000đ!!")
    //                 return
    //             }
    //         }
    //     }
    //     if (filterData.giaMax != undefined) {
    //         if (isNaN(filterData.giaMax)) {
    //             alertify.error("Giá max phải là số!!")
    //             return
    //         } else {
    //             if (filterData.giaMax > 100000000) {
    //                 alertify.error("Giá max phải < 100.000.000đ !!")
    //                 return
    //             } else {
    //                 if (filterData.giaBan != undefined) {
    //                     if (parseFloat(filterData.giaBan) > parseFloat(filterData.giaMax)) {
    //                         console.log("max", parseFloat(filterData.giaMax))
    //                         console.log("min", parseFloat(filterData.giaBan))
    //                         alertify.error("Giá max phải > giá min!!")
    //                         return
    //                     }
    //                 }
    //             }
    //         }
    //     }
    //
    //     $scope.pageNumber = 0
    //     $scope.filterDto = filterData
    //     $scope.pageNumbers = []
    //     $http.post("/admin/san-pham/filter", $scope.filterDto).then(r => {
    //         if (Object.keys($scope.filterDto).length > 0) {
    //             document.getElementById('lengthFilter').innerText = Object.keys($scope.filterDto).length
    //         } else {
    //             document.getElementById('lengthFilter').innerText = ""
    //         }
    //         $scope.items = r.data.content;
    //         $scope.totalPage = r.data.totalPages;
    //         $scope.getPageNumbers(r.data.totalPages)
    //         isfilter = true;
    //     }).catch(e => console.log(e))
    // }
    //
    // $scope.clearFilter = function () {
    //
    //     $scope.pageNumber = 0
    //     $scope.pageNumbers = []
    //     $http.get("/admin/san-pham/get-all").then(r => {
    //         document.getElementById('lengthFilter').innerText = ""
    //         $scope.items = r.data.content;
    //         $scope.getPageNumbers(r.data.totalPages)
    //         $scope.filterData = {}
    //         $scope.filterDto = {}
    //         isfilter = false;
    //     }).catch(e => console.log(e))
    // }
    //
    // $scope.sortName = function () {
    //     let button = document.getElementById("sortName")
    //     if (button.className == "bx bx-sort-up") {
    //         $scope.resetIconButton()
    //         button.className = "bx bx-sort-down"
    //         $scope.filterDto.sort = 3
    //     } else if (button.className == "bx bx-sort") {
    //         $scope.resetIconButton()
    //         button.className = "bx bx-sort-up"
    //         $scope.filterDto.sort = 4
    //     } else {
    //         button.className = "bx bx-sort"
    //         // $scope.clearFilter()
    //         delete $scope.filterDto.sort
    //     }
    //
    //     $scope.filter($scope.filterDto)

    // $scope.pageNumber = 0
    // $scope.pageNumbers = []
    // $http.post("/admin/san-pham/filter",$scope.filterDto).then(r => {
    //     $scope.items = r.data.content;
    //     $scope.getPageNumbers(r.data.totalPages)
    //     isfilter = true;
    // }).catch(e => console.log(e))
    // }
    // $scope.sortColor = function () {
    //     let button = document.getElementById("sortColor")
    //     if (button.className == "bx bx-sort-up") {
    //         $scope.resetIconButton()
    //         $scope.filterDto.sort = 7
    //         button.className = "bx bx-sort-down"
    //     } else if (button.className == "bx bx-sort") {
    //         $scope.resetIconButton()
    //         $scope.filterDto.sort = 8
    //         button.className = "bx bx-sort-up"
    //     } else {
    //         button.className = "bx bx-sort"
    //         delete $scope.filterDto.sort
    //     }
    //     $scope.filter($scope.filterDto)
    // }
    // $scope.sortBrand = function () {
    //     let button = document.getElementById("sortBrand")
    //     if (button.className == "bx bx-sort-up") {
    //         $scope.resetIconButton()
    //         $scope.filterDto.sort = 9
    //         button.className = "bx bx-sort-down"
    //     } else if (button.className == "bx bx-sort") {
    //         $scope.resetIconButton()
    //         $scope.filterDto.sort = 10
    //         button.className = "bx bx-sort-up"
    //     } else {
    //         button.className = "bx bx-sort"
    //         delete $scope.filterDto.sort
    //     }
    //     $scope.filter($scope.filterDto)
    // }
    // $scope.sortAcount = function () {
    //     let button = document.getElementById("sortAcount")
    //     if (button.className == "bx bx-sort-up") {
    //         $scope.resetIconButton()
    //         button.className = "bx bx-sort-down"
    //         $scope.filterDto.sort = 5
    //     } else if (button.className == "bx bx-sort") {
    //         $scope.resetIconButton()
    //         button.className = "bx bx-sort-up"
    //         $scope.filterDto.sort = 6
    //     } else {
    //         button.className = "bx bx-sort"
    //         delete $scope.filterDto.sort
    //     }
    //     $scope.filter($scope.filterDto)
    // }
    // $scope.sortPrice = function () {
    //     let button = document.getElementById("sortPrice")
    //     if (button.className == "bx bx-sort-up") {
    //         $scope.resetIconButton()
    //         button.className = "bx bx-sort-down"
    //         $scope.filterDto.sort = 1
    //     } else if (button.className == "bx bx-sort") {
    //         $scope.resetIconButton()
    //         button.className = "bx bx-sort-up"
    //         $scope.filterDto.sort = 2
    //     } else {
    //         button.className = "bx bx-sort"
    //         delete $scope.filterDto.sort
    //     }
    //     $scope.filter($scope.filterDto)
    // }
    // $scope.resetIconButton = function () {
    //     document.getElementById("sortName").className = "bx bx-sort";
    //     document.getElementById("sortColor").className = "bx bx-sort";
    //     document.getElementById("sortBrand").className = "bx bx-sort";
    //     document.getElementById("sortPrice").className = "bx bx-sort";
    //     document.getElementById("sortAcount").className = "bx bx-sort";
    // }
    //
    //
    // $scope.toastSuccess = function (text) {
    //
    //     $.toast({
    //         heading: 'Thành công',
    //         text: text,
    //         position: 'top-right',
    //         icon: 'success',
    //         stack: false
    //     })
    // }
    // // $scope.toastSuccess("Thành công")
    // $scope.toastError = function (text) {
    //
    //     $.toast({
    //         heading: 'Thành công',
    //         text: text,
    //         position: 'top-right',
    //         icon: 'error',
    //         stack: false
    //     })
    // }

});





