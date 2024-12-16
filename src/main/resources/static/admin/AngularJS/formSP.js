var app = angular.module("formSP-app", [])
app.controller("san-pham-ctrl", function ($scope, $http) {

    $scope.mauSac = [];
    $scope.chatLieu = [];
    $scope.thuongHieu = [];
    $scope.xuatXu = [];
    $scope.kieuDang = [];
    $scope.kichCo = [];
    $scope.selectedMauSac = "";
    $scope.selectedKichCo = "";
    $scope.selectedChatLieu = "";
    $scope.selectedKieuDang = "";
    $scope.selectedThuongHieu = "";
    $scope.selectedXuatXu = "";


    const pathName = window.location.pathname.split('/');
    var idSanPham = pathName[pathName.length - 1];
    if (idSanPham) {
        $http.get("/admin/san-pham/get/" + idSanPham).then(response => {
            let sanPham = response.data;
            $scope.tenSP = sanPham.ten;
            $scope.selectedXuatXu = sanPham.idXuatXu && sanPham.idXuatXu.idXuatXu ? sanPham.idXuatXu.idXuatXu : null;
            $scope.selectedChatLieu = sanPham.idChatLieu && sanPham.idChatLieu.idChatLieu ? sanPham.idChatLieu.idChatLieu : null;
            $scope.selectedKieuDang = sanPham.idKieuDang && sanPham.idKieuDang.idKieuDang ? sanPham.idKieuDang.idKieuDang : null;
            $scope.selectedThuongHieu = sanPham.idThuongHieu && sanPham.idThuongHieu.idThuongHieu ? sanPham.idThuongHieu.idThuongHieu : null;
        }).catch(error => {
            console.error("Failed to fetch product details:", error);
        });
    } else {
        console.error("Product ID is missing from the URL.");
        alert("Invalid product ID. Please check the URL and try again.");
    }

    // $scope.getTenXuatXu = function () {
    //     if ($scope.selectedXuatXu) {
    //         const found = $scope.xuatXu.find(x => x.idXuatXu === $scope.selectedXuatXu);
    //         return found ? found.ten : "Không tìm thấy";
    //     }
    //     return null;
    // };
    //
    // $scope.getTenChatLieu = function () {
    //     if ($scope.selectedChatLieu) {
    //         const found = $scope.chatLieu.find(c => c.idChatLieu === $scope.selectedChatLieu);
    //         return found ? found.ten : "Không tìm thấy";
    //     }
    //     return null;
    // };
    //
    // $scope.getTenKieuDang = function () {
    //     if ($scope.selectedChatLieu) {
    //         const found = $scope.kieuDang.find(k => k.idKieuDang === $scope.selectedKieuDang);
    //         return found ? found.ten : "Không tìm thấy";
    //     }
    //     return null;
    // };
    //
    // $scope.getTenThuongHieu = function () {
    //     if ($scope.selectedChatLieu) {
    //         const found = $scope.thuongHieu.find(c => c.idThuongHieu === $scope.selectedThuongHieu);
    //         return found ? found.ten : "Không tìm thấy";
    //     }
    //     return null;
    // };

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


    $scope.generateRandomString = function (length) {
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
        let result = '';

        for (let i = 0; i < length; i++) {
            const randomIndex = Math.floor(Math.random() * characters.length);
            result += characters[randomIndex];
        }

        return result;
    };


    $scope.tables = []; // Mảng chứa các bảng màu với các kích thước tương ứng cùng ảnh

// Khi chọn màu
    $scope.selectColor = function () {
        $scope.selectedMauSac.forEach(function (mau) {
            // Kiểm tra xem màu đã có trong tables chưa
            let existingTable = $scope.tables.find(t => t.mau.ten === mau.ten);

            // Nếu chưa tồn tại, thêm màu mới với tất cả kích cỡ hiện có từ các bảng trong tables
            if (!existingTable) {
                // Tạo một danh sách kích cỡ mới bằng cách hợp nhất tất cả các kích cỡ hiện có trong các bảng
                let allSizes = [];
                $scope.tables.forEach(table => {
                    table.size.forEach(size => {
                        if (!allSizes.some(s => s.ten === size.ten)) {
                            allSizes.push(size); // Thêm kích cỡ nếu chưa tồn tại trong allSizes
                        }
                    });
                });

                // Thêm màu mới với tất cả kích cỡ hiện có vào tables
                $scope.tables.push({mau: mau, size: allSizes, img: {imageSrc: ""}});
            }
        });
    };


// Khi chọn kích cỡ
    $scope.selectSize = function () {
        $scope.selectedKichCo.forEach(function (size) {
            // Duyệt qua từng bảng màu hiện có và thêm kích cỡ vào từng bảng
            $scope.tables.forEach(function (table) {
                // Kiểm tra kích cỡ đã tồn tại trong bảng màu hiện tại chưa
                if (!table.size.some(s => s.ten === size.ten)) {
                    table.size.push(size); // Thêm kích cỡ nếu chưa có
                }
            });
        });
    };

// Hàm để loại bỏ dấu tiếng Việt
    function removeVietnameseTones(str) {
        return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/đ/g, 'd').replace(/Đ/g, 'D');
    }

// Bộ lọc tùy chỉnh cho màu sắc
    $scope.colorFilterFunction = function (mau) {
        if (!$scope.colorSearch) return true;
        let searchText = removeVietnameseTones($scope.colorSearch.toLowerCase());
        let colorName = removeVietnameseTones(mau.ten.toLowerCase());
        return colorName.includes(searchText);
    };

// Bộ lọc tùy chỉnh cho kích cỡ
    $scope.sizeFilterFunction = function (size) {
        if (!$scope.sizeSearch) return true;
        let searchText = removeVietnameseTones($scope.sizeSearch.toLowerCase());
        let sizeName = removeVietnameseTones(size.ten.toLowerCase());
        return sizeName.includes(searchText);
    };

// Chọn màu phù hợp nhất tự động khi nhập
    $scope.autoSelectColor = function () {
        let filteredColors = $scope.mauSac.filter(mau => $scope.colorFilterFunction(mau));

        if (filteredColors.length > 0) {
            $scope.selectedMauSac = filteredColors[0];
            $scope.selectColor();  // Cập nhật màu sắc đã chọn
        }
    };

// Chọn kích cỡ phù hợp nhất tự động khi nhập
    $scope.autoSelectSize = function () {
        let filteredSizes = $scope.kichCo.filter(size => $scope.sizeFilterFunction(size));

        if (filteredSizes.length > 0) {
            $scope.selectedKichCo = filteredSizes[0];
            $scope.selectSize();  // Cập nhật kích cỡ đã chọn
        }
    };


    // Hàm xóa table màu
    $scope.removeMau = function (table) {
        const index = $scope.tables.indexOf(table);
        if (index !== -1) {
            $scope.tables.splice(index, 1);
        }
    };


    // Hàm xóa hàng dựa trên trạng thái của checkbox
    $scope.removeSize = function (table, size) {
        // Kiểm tra và khởi tạo đối tượng nếu chưa có trong $scope.form
        if (!$scope.form[table.mau.idMauSac]) {
            $scope.form[table.mau.idMauSac] = {allSelected: false};
        }


        if (!$scope.form[table.mau.idMauSac][size.idKichCo]) {
            $scope.form[table.mau.idMauSac][size.idKichCo] = {selected: false};
        }

        // Nếu checkbox chính được chọn, xóa tất cả các hàng trong bảng
        if ($scope.form[table.mau.idMauSac].allSelected) {
            table.size = []; // Xóa tất cả các kích cỡ
        } else {
            // Nếu checkbox chính không được chọn, kiểm tra checkbox con
            // Nếu checkbox con của kích cỡ không được chọn, vẫn có thể xóa
            if (!$scope.form[table.mau.idMauSac][size.idKichCo].selected) {
                // Xóa kích cỡ riêng lẻ nếu checkbox con không được chọn
                let index = table.size.indexOf(size);
                if (index > -1) {
                    table.size.splice(index, 1); // Xóa kích cỡ khỏi bảng
                }
            } else {
                // Nếu checkbox con được chọn, chỉ xóa hàng đó
                if ($scope.form[table.mau.idMauSac][size.idKichCo].selected) {
                    table.size = table.size.filter(function (sizeItem) {
// Kiểm tra và khởi tạo selected nếu chưa tồn tại cho kích cỡ cụ thể
                        if (!$scope.form[table.mau.idMauSac][sizeItem.idKichCo]) {
                            $scope.form[table.mau.idMauSac][sizeItem.idKichCo] = {selected: false};
                        }
                        return !$scope.form[table.mau.idMauSac][sizeItem.idKichCo].selected;
                    });
                }
            }
        }
    };

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
        // Nếu HinhAnh là null hoặc không có tên, không thực hiện gì
        if (!HinhAnh || !HinhAnh.ten) {
            return Promise.resolve(null);  // Trả về null ngay lập tức mà không gọi API
        }

        // Kiểm tra nếu ảnh đã tồn tại
        return $http.get("/admin/hinh-anh", {params: {ten: HinhAnh.ten}}).then(function (r) {
            const existingImage = r.data;

            if (!existingImage) {
                // Nếu ảnh chưa tồn tại, thêm ảnh mới
                var addHinhAnh = {
                    ma: $scope.generateRandomString(8),
                    ten: HinhAnh.ten
                };
                // Thực hiện POST để thêm ảnh
                return $http.post("/admin/hinh-anh/add", addHinhAnh).then(function (addResponse) {
                    console.log("Dữ liệu thêm ảnh mới:", addResponse.data);

                    // Sau khi thêm ảnh mới, thực hiện GET để lấy lại ảnh với ID
                    return $http.get("/admin/hinh-anh", {params: {ten: HinhAnh.ten}}).then(function (getResponse) {
                        console.log("Dữ liệu ảnh mới với ID:", getResponse.data);
                        return getResponse.data; // Trả về dữ liệu ảnh mới với idHinhAnh
                    });
                }).catch(function (err) {
                    console.error("Lỗi khi thêm ảnh mới:", err);
                    return null;  // Trả về null nếu có lỗi
                });
            } else {
                // Nếu ảnh đã tồn tại, trả về ảnh đã có
                return existingImage;
            }
        }).catch(function (err) {
            console.error("Lỗi khi kiểm tra ảnh:", err);
            return null;  // Trả về null nếu có lỗi
        });
    };


    $scope.form = {}; // Lưu thông tin nhập liệu và thông báo lỗi cho từng ô input


    $scope.onXuatXuChange = function () {
        console.log("Selected Xuat Xu:", $scope.selectedXuatXu);
    };


    $scope.create = function () {
        var hasError = false;
        $scope.errorMessages = {};

        // Kiểm tra lỗi cho từng màu sắc (tables)
        if ($scope.tables.length === 0 || !$scope.tables.some(table => table.mau)) {
            $scope.errorMessages.mauSac = "Vui lòng chọn ít nhất một màu sắc!";
            hasError = true;
        } else {
            delete $scope.errorMessages.mauSac;
        }

        // Kiểm tra lỗi cho từng table và từng size
        $scope.tables.forEach(function (table) {
            table.errorMessages = table.errorMessages || {};

            // Kiểm tra kích cỡ
            if (table.size.length === 0) {
                table.errorMessages.size = "Vui lòng chọn ít nhất một kích cỡ!";
                hasError = true;
            } else {
                delete table.errorMessages.size;
            }

            // Kiểm tra ảnh
            if (!table.img || !table.img.imageSrc) {
                table.errorMessages.img = "Vui lòng chọn ảnh!";
                hasError = true;
            } else {
                delete table.errorMessages.img;
            }

            // Kiểm tra lỗi trong từng kích cỡ
            table.size.forEach(function (size) {
                var sizeForm = $scope.form[table.mau.idMauSac] && $scope.form[table.mau.idMauSac][size.idKichCo];
                if (sizeForm) {
                    sizeForm.errorMessages = sizeForm.errorMessages || {};

                    // Kiểm tra số lượng
                    if (sizeForm.soLuong <= 0 || sizeForm.soLuong == null) {
                        sizeForm.errorMessages.soLuong = sizeForm.soLuong == null
                            ? "Số lượng không được để trống"
                            : "Số lượng phải lớn hơn 0";
                        hasError = true;
                    } else {
                        delete sizeForm.errorMessages.soLuong;
                    }

                    // Kiểm tra giá nhập
                    if (sizeForm.giaNhap <= 0 || sizeForm.giaNhap == null) {
                        sizeForm.errorMessages.giaNhap = sizeForm.giaNhap == null
                            ? "Giá nhập không được để trống"
                            : "Giá nhập phải lớn hơn 0";
                        hasError = true;
                    } else {
                        delete sizeForm.errorMessages.giaNhap;
                    }

                    // Kiểm tra giá bán
                    if (sizeForm.giaBan <= 0 || sizeForm.giaBan == null) {
                        sizeForm.errorMessages.giaBan = sizeForm.giaBan == null
                            ? "Giá bán không được để trống"
                            : "Giá bán phải lớn hơn 0";
                        hasError = true;
                    } else {
                        delete sizeForm.errorMessages.giaBan;
                    }

                    // Kiểm tra giá nhập phải nhỏ hơn giá bán
                    if (sizeForm.giaNhap >= sizeForm.giaBan) {
                        sizeForm.errorMessages.giaNhap = "Giá nhập phải nhỏ hơn giá bán";
                        sizeForm.errorMessages.giaBan = "Giá bán phải lớn hơn giá nhập";
                        hasError = true;
                    } else {
                        delete sizeForm.errorMessages.giaNhap;
                        delete sizeForm.errorMessages.giaBan;
                    }
                }
            });
        });

        // Nếu có lỗi, dừng lại
        if (hasError) return;

        // Kiểm tra sự tồn tại của sản phẩm chi tiết trong cơ sở dữ liệu
        var checkExistPromises = [];
        $scope.tables.forEach(function (table) {
            table.size.forEach(function (size) {
                var sizeForm = $scope.form[table.mau.idMauSac] && $scope.form[table.mau.idMauSac][size.idKichCo];
                if (sizeForm) {
                    var promise = $http.get("/admin/san-pham/chi-tiet/check-existence/" + idSanPham, {
                        params: {
                            idMauSac: table.mau.idMauSac,
                            idKichCo: size.idKichCo
                        }
                    }).then(function (response) {
                        if (response.data) {
<<<<<<< HEAD
                            // Kiểm tra nếu sizeForm tồn tại trước khi thay đổi thuộc tính
                            if (!sizeForm.errorMessages) {
                                sizeForm.errorMessages = {};
                            }

                            sizeForm.errorMessages.exists = "Đã tồn tại!";

                            sizeForm.errorMessages.exists = "Sản phẩm chi tiết đã tồn tại!";

=======
                            sizeForm.errorMessages.exists = "Đã tồn tại";
>>>>>>> feature/sanpham
                            hasError = true;
                        }
                    }).catch(function (err) {
                        console.error("Lỗi khi kiểm tra sự tồn tại của sản phẩm chi tiết:", err);
                    });
                    checkExistPromises.push(promise);
                }
            });
        });

        // Nếu có lỗi tồn tại, không tiếp tục
        if (hasError) return;

        // Chờ kiểm tra sự tồn tại hoàn tất
        Promise.all(checkExistPromises).then(function () {
            if (hasError) return;

            // Tiếp tục thực hiện các bước tạo sản phẩm chi tiết
            var SanPhamChiTietList = [];
            var promises = [];
            $scope.tables.forEach(function (table) {
                var HinhAnh = {ten: table.img ? table.img.imageSrc : null};
                var promise = $scope.getHinhAnh(HinhAnh).then(function (hinhAnh) {
                    table.idHinhAnh = hinhAnh && hinhAnh.idHinhAnh ? hinhAnh.idHinhAnh : null;
                }).catch(function (err) {
                    console.error("Lỗi khi xử lý ảnh:", err);
                    table.idHinhAnh = null;
                });
                promises.push(promise);
            });

            Promise.all(promises).then(function () {
                $scope.tables.forEach(function (table) {
                    var tableHinhAnh = table.idHinhAnh;

                    table.size.forEach(function (size) {
                        var sizeForm = $scope.form[table.mau.idMauSac] && $scope.form[table.mau.idMauSac][size.idKichCo];
                        if (sizeForm) {
                            var SanPhamChiTiet = {
                                ma: $scope.generateRandomString(8),
                                idMauSac: table.mau.idMauSac,
                                idKichCo: size.idKichCo,
                                idSanPham: idSanPham,
                                soLuong: sizeForm.soLuong,
                                giaNhap: sizeForm.giaNhap,
                                giaBan: sizeForm.giaBan,
                                idHinhAnh: tableHinhAnh
                            };
                            SanPhamChiTietList.push(SanPhamChiTiet);
                        }
                    });
                });

                if (SanPhamChiTietList.length > 0) {
                    $http.post("/admin/san-pham/chi-tiet/add", SanPhamChiTietList).then(function (response) {
                        alert("Thêm sản phẩm chi tiết thành công!");
                        $scope.viewChiTiet();
                    }).catch(function (err) {
                        console.error("Thêm sản phẩm không thành công", err);
                    });
                } else {
                    console.error("Không có sản phẩm chi tiết hợp lệ để thêm.");
                }
            }).catch(function (err) {
                console.error("Lỗi khi xử lý các ảnh:", err);
            });
        }).catch(function (err) {
            console.error("Lỗi khi kiểm tra sự tồn tại của sản phẩm chi tiết:", err);
        });
    };



    $scope.back = function () {
        window.location.href = '/admin/san-pham';
    };

    $scope.viewChiTiet = function () {
        // Chuyển hướng đến trang chi tiết sản phẩm
        location.href = `/admin/san-pham/` + idSanPham;
    };

    $scope.confirmUpdateSize = function (table, size) {
        // Hiển thị hộp thoại xác nhận
        var confirmed = confirm("Bạn có chắc chắn muốn cập nhật kích thước này?");
        if (confirmed) {
            $scope.updateSize(table, size);
        }
    };


    $scope.updateSize = function (table, size) {
        var sizeForm = $scope.form[table.mau.idMauSac][size.idKichCo];
        var hasError = false;

        if (sizeForm) {
            sizeForm.errorMessages = sizeForm.errorMessages || {};

            // Kiểm tra lỗi cho các input (số lượng, giá nhập, giá bán)
            if (sizeForm.soLuong <= 0 || sizeForm.soLuong == null) {
                sizeForm.errorMessages.soLuong = sizeForm.soLuong == null
                    ? "Số lượng không được để trống"
                    : "Số lượng phải lớn hơn 0";
                hasError = true;
            } else {
                delete sizeForm.errorMessages.soLuong;  // Xóa lỗi khi giá trị hợp lệ
            }

            if (sizeForm.giaNhap <= 0 || sizeForm.giaNhap == null) {
                sizeForm.errorMessages.giaNhap = sizeForm.giaNhap == null
                    ? "Giá nhập không được để trống"
                    : "Giá nhập phải lớn hơn 0";
                hasError = true;
            } else {
                delete sizeForm.errorMessages.giaNhap;  // Xóa lỗi khi giá trị hợp lệ
            }

            if (sizeForm.giaBan <= 0 || sizeForm.giaBan == null) {
                sizeForm.errorMessages.giaBan = sizeForm.giaBan == null
                    ? "Giá bán không được để trống"
                    : "Giá bán phải lớn hơn 0";
                hasError = true;
            } else {
                delete sizeForm.errorMessages.giaBan;  // Xóa lỗi khi giá trị hợp lệ
            }

            if (sizeForm.giaNhap >= sizeForm.giaBan) {
                sizeForm.errorMessages.giaNhap = "Giá nhập phải nhỏ hơn giá bán";
                sizeForm.errorMessages.giaBan = "Giá bán phải lớn hơn giá nhập";
                hasError = true;
            } else {
                delete sizeForm.errorMessages.giaNhap;  // Xóa lỗi khi giá trị hợp lệ
                delete sizeForm.errorMessages.giaBan;  // Xóa lỗi khi giá trị hợp lệ
            }

            // Nếu có lỗi, không tiếp tục xử lý
            if (hasError) {
                return;
            }

            // Logic cập nhật sản phẩm chi tiết
            var updateCTSP = {
                idSanPham: idSanPham,
                idMauSac: table.mau.idMauSac,
                idKichCo: size.idKichCo,
                soLuong: sizeForm.soLuong,
                giaNhap: sizeForm.giaNhap,
                giaBan: sizeForm.giaBan
            };
            console.log("updateCTSP", updateCTSP);

            $http.post("/admin/san-pham/chi-tiet/update-by-size/" + idSanPham, updateCTSP).then(function (response) {
                // Xóa dòng tương ứng trong bảng
                var index = table.size.indexOf(size);
                if (index > -1) {
                    table.size.splice(index, 1); // Xóa phần tử tại vị trí index
                }

                // Xóa lỗi hoặc cập nhật lại các thông tin cần thiết
                delete sizeForm.errorMessages.exists;
            }).catch(function (err) {
                // Xử lý khi có lỗi
                console.error("Lỗi khi cập nhật sản phẩm chi tiết:", err);
            });
        }
    };



// Hàm chọn tất cả checkbox con khi chọn checkbox chính
    $scope.toggleAllCheckboxes = function (table) {
        const colorId = table.mau.idMauSac;
        const allSelected = $scope.form[colorId].allSelected;

        table.size.forEach(function (size) {
            // Đảm bảo rằng đối tượng form cho từng size tồn tại
            if (!$scope.form[colorId][size.idKichCo]) {
                $scope.form[colorId][size.idKichCo] = {};
            }
            $scope.form[colorId][size.idKichCo].selected = allSelected;
        });
    };


    $scope.onValueChanged = function (field, value, table, size) {
        const colorId = table.mau.idMauSac;
        const sizeForm = $scope.form[colorId][size.idKichCo];

        // Kiểm tra ô này đã được chọn hay chưa
        if (sizeForm && sizeForm.selected === true) {
            // Nếu được chọn, gọi updateAllSelectedValues
            $scope.updateAllSelectedValues(field, value, table);
        }
    };


// Hàm cập nhật giá trị cho tất cả các hàng được chọn khi nhập vào một trường bất kỳ
    $scope.updateAllSelectedValues = function (field, value, table) {
        const colorId = table.mau.idMauSac;

        // Chỉ đồng bộ khi giá trị được thay đổi ở ô đã chọn (selected = true)
        table.size.forEach(function (size) {
            const sizeForm = $scope.form[colorId][size.idKichCo];

            // Kiểm tra nếu ô này đã được chọn (selected = true)
            if (sizeForm && sizeForm.selected === true) {
                // Chỉ đồng bộ giá trị cho ô đã được chọn
                sizeForm[field] = value;
            }
        });
    };






// Hàm để đồng bộ khi thay đổi giá trị ở checkbox con
    $scope.syncSelectedValues = function (table, size) {
        const colorId = table.mau.idMauSac;
        const sizeForm = $scope.form[colorId][size.idKichCo];

        // Cập nhật checkbox chính chỉ khi tất cả các ô con đều được chọn
        $scope.form[colorId].allSelected = table.size.every(function (size) {
            const currentSizeForm = $scope.form[colorId][size.idKichCo];
            return currentSizeForm && currentSizeForm.selected;
        });
    };



// Hàm toggle cho màu sắc
    $scope.toggleColorSelection = function (mau) {
        if (mau.isSelected) {
            $scope.selectedMauSac.push(mau);
        } else {
            const index = $scope.selectedMauSac.indexOf(mau);
            if (index > -1) $scope.selectedMauSac.splice(index, 1);
        }
    };

// Hàm toggle cho kích cỡ
    $scope.toggleSizeSelection = function (size) {
        if (size.isSelected) {
            $scope.selectedKichCo.push(size);
        } else {
            const index = $scope.selectedKichCo.indexOf(size);
            if (index > -1) $scope.selectedKichCo.splice(index, 1);
        }
    };


    // $scope.nameFile = null;
    // $scope.fileModel = null;
    $scope.onFileSelected1 = function (event) {
        console.log("File selection initiated.");

        // Lấy thông tin bảng từ data attribute
        var table = angular.element(event.target).data('table');
        var file = event.target.files[0]; // Lấy file từ input

        if (!table) {
            console.error("Không tìm thấy bảng (table) từ sự kiện.");
            return;
        }

        if (!file) {
            console.warn("Không có tệp nào được chọn.");

            // Xóa dữ liệu của table.img.imageSrc nếu người dùng nhấn Cancel
            if (table.img) {
                table.img.imageSrc = null;
            }

            // Cập nhật lại trong $scope.tables
            for (let i = 0; i < $scope.tables.length; i++) {
                if ($scope.tables[i].mau.idMauSac === table.mau.idMauSac) {
                    if ($scope.tables[i].img) {
                        $scope.tables[i].img.imageSrc = null;
                        $scope.tables[i].idHinhAnh = null;
                    }
                    break;
                }
            }

            console.log("Dữ liệu imageSrc đã được xóa:", $scope.tables);
            $scope.$applyAsync();
            return;
        }

        console.log("File được chọn:", file.name);
        console.log("Bảng hiện tại:", table);

        // Cập nhật tên file vào imageSrc của bảng
        table.img = table.img || {}; // Đảm bảo img tồn tại
        // table.img.imageSrc = file.name;

        // Cập nhật mảng $scope.tables
        for (let i = 0; i < $scope.tables.length; i++) {
            if ($scope.tables[i].mau.idMauSac === table.mau.idMauSac) {
                $scope.tables[i].img = $scope.tables[i].img || {};
                $scope.tables[i].img.imageSrc = file.name;
                break;
            }
        }

        console.log("Cập nhật bảng trong $scope.tables:", $scope.tables);

        // Gọi getHinhAnh để xử lý ảnh
        var HinhAnh = {ten: file.name};
        $scope.getHinhAnh(HinhAnh).then(function (result) {
            if (result) {
                // Cập nhật idHinhAnh vào table
                table.idHinhAnh = result.idHinhAnh;

                // Đồng bộ lại idHinhAnh trong $scope.tables
                for (let i = 0; i < $scope.tables.length; i++) {
                    if ($scope.tables[i].mau.idMauSac === table.mau.idMauSac) {
                        $scope.tables[i].idHinhAnh = result.idHinhAnh;
                        break;
                    }
                }
                console.log("Đã cập nhật idHinhAnh:", table.idHinhAnh);
            } else {
                console.warn("Không lấy được idHinhAnh.");
            }
        }).catch(function (err) {
            console.error("Lỗi khi gọi getHinhAnh:", err);
        });

        // Đảm bảo AngularJS nhận diện thay đổi
        $scope.$applyAsync();
    };


    // var httpThuocTinh = "";
    // var thuocTinhSL = undefined;
    // var filesTransfer = new DataTransfer();
    // var checkViewModal = false;
    // $scope.tenThuocTinh = ""
    // $scope.er = {}
    // document.getElementById("pro-image").files = filesTransfer.files
    //
    // $("#viewAdd").on('hide.bs.modal', function () {
    //     if (checkViewModal == false) thuocTinhSL.selectedIndex = 0;
    //     $scope.tenThuocTinh = "";
    //     document.getElementById("viewAddThuongHieu").style.display = "none"
    // });
    //
    // $scope.viewAddMauSac = function () {
    //     httpThuocTinh = "/admin/mau-sac/add"
    //     thuocTinhSL = document.getElementById("mauSac");
    //
    //     if (thuocTinhSL.selectedIndex == thuocTinhSL.length - 1) {
    //         $('#viewAdd').modal('show');
    //         checkViewModal = false
    //     }
    // }
    // $scope.viewAddXuatXu = function () {
    //     httpThuocTinh = "/admin/xuat-xu/add"
    //     thuocTinhSL = document.getElementById("xuatXu");
    //
    //     if (thuocTinhSL.selectedIndex == thuocTinhSL.length - 1) {
    //         $('#viewAdd').modal('show');
    //         checkViewModal = false
    //     }
    // }
    // $scope.viewAddDongSP = function () {
    //     httpThuocTinh = "/admin/dong-san-pham/add"
    //     thuocTinhSL = document.getElementById("dongSP");
    //
    //     if (thuocTinhSL.selectedIndex == thuocTinhSL.length - 1) {
    //         document.getElementById("viewAddThuongHieu").style.display = "inline-block"
    //         $('#viewAdd').modal('show');
    //         checkViewModal = false
    //     }
    // }
    //
    // $scope.viewAddKieuDang = function () {
    //     httpThuocTinh = "/admin/kieu-dang"
    //     thuocTinhSL = document.getElementById("kieuDang");
    //
    //     if (thuocTinhSL.selectedIndex == thuocTinhSL.length - 1) {
    //         $('#viewAdd').modal('show');
    //         checkViewModal = false
    //     }
    // }
    //
    // $scope.viewAddChatLieu = function () {
    //     httpThuocTinh = "/admin/chat-lieu/add"
    //     thuocTinhSL = document.getElementById("chatLieu");
    //
    //     if (thuocTinhSL.selectedIndex == thuocTinhSL.length - 1) {
    //         $('#viewAdd').modal('show');
    //         checkViewModal = false
    //     }
    // }
    // $scope.addThuocTinh = function () {
    //     if (thuocTinhSL.id == "dongSP"){
    //         if($scope.thuongHieu == undefined){
    //             document.getElementById('erAddDongSP').innerText = "Vui lòng chọn thương hiệu"
    //             return;
    //         }
    //     }
    //     if($scope.tenThuocTinh==undefined || $scope.tenThuocTinh.length==0){
    //         document.getElementById('etenThuocTinh').innerText = "Vui lòng nhập tên"
    //         return
    //     }
    //     if($scope.tenThuocTinh.length>100){
    //         document.getElementById('etenThuocTinh').innerText = "Tên tối đa 100 ký tự"
    //         return
    //     }
    //
    //     $http.post(httpThuocTinh, {thuongHieu: $scope.thuongHieu, ten: $scope.tenThuocTinh}).then(r => {
    //         // document.getElementById("viewAddThuongHieu").style.display = "none"
    //         // if (thuocTinhSL.id == "dongSP") $scope.addOtpInDongSP(r.data)
    //         // else {
    //             var option = document.createElement("option");
    //             option.text = r.data.ten;
    //             option.value = r.data.id == undefined ? r.data.ma : r.data.id
    //             thuocTinhSL.add(option, thuocTinhSL[thuocTinhSL.length - 1]);
    //             thuocTinhSL.value = option.value;
    //         // }
    //
    //         $scope.tenThuocTinh = "";
    //         checkViewModal = true
    //         $('#viewAdd').modal('hide');
    //         alertify.success("Thêm thành công")
    //     }).catch(e => {
    //         alertify.error("Thêm thất bại")
    //         console.log(e)
    //     })
    //
    // }
    // $scope.addOtpInDongSP = function (data) {
    //     let otpGroup = document.getElementById($scope.thuongHieu+"");
    //     console.log(otpGroup)
    //     console.log(data)
    //     let option = document.createElement("option");
    //     option.setAttribute("value",data.id);
    //     option.innerHTML=data.ten;
    //     // option.text = data.ten;
    //     // option.value = data.id;
    //     otpGroup.append(option)
    //     thuocTinhSL.value = option.value;
    //
    // }
    //
    //
    // $scope.removeER = function (id) {
    //     document.getElementById(id).innerText = "";
    // }
    // $scope.closeModal = function () {
    //     $('#viewAdd').modal('hide');
    // }
    //
    // $scope.appendFile = function () {
    //     $scope.removeER('erImg')
    //     let files = document.getElementById("pro-image").files
    //     console.log(files.length + filesTransfer.files.length)
    //     if(files.length + filesTransfer.files.length > 5){
    //         document.getElementById("erImg").innerText = "Sản phẩm chỉ tối đa 5 ảnh"
    //         return
    //     }
    //
    //     files.forEach(f => {
    //         if(f.size > 1 * 1024 * 1024){
    //             document.getElementById("erImg").innerText = "Kích thước tối đa của ảnh là 1mb"
    //             return
    //         }
    //     })
    //
    //     document.getElementById("erImg").innerText = ""
    //     files.forEach(f => filesTransfer.items.add(f))
    //     // document.getElementById("pro-image").files = filesTransfer.files
    // }
    // $scope.removeFile = function (key) {
    //     $scope.removeER('erImg')
    //     let index;
    //     let files1 = new DataTransfer();
    //     filesTransfer.files.forEach(f => {
    //         if (f.lastModified != key) {
    //             files1.items.add(f);
    //         }
    //     })
    //
    //     filesTransfer = files1
    //     // document.getElementById("pro-image").files = filesTransfer.files
    // }
    // $scope.loadImgProduct = function (fileName) {
    //     const image = new File([fileName], fileName, {
    //         lastModified: new Date(),
    //     });
    //     let buttonCancel = document.getElementById(fileName).getElementsByClassName('image-cancel')
    //     buttonCancel[0].setAttribute("id", image.lastModified);
    //     console.log(buttonCancel)
    //
    //     filesTransfer.items.add(image);
    //     document.getElementById("pro-image").files = filesTransfer.files
    // }
    // $scope.sortFiles = function () {
    //     console.log(document.getElementsByClassName("image-cancel"))
    //
    //     let indexs = []
    //
    //     let files1 = new DataTransfer();
    //     document.getElementsByClassName("image-cancel").forEach(item => {
    //         console.log(item)
    //         for(let i = 0;i<filesTransfer.files.length ;i++)
    //             if (filesTransfer.files[i].lastModified == item.id && indexs.includes(i)==false) {
    //                 indexs.push(i)
    //                 files1.items.add(filesTransfer.files[i]);
    //             }
    //     })
    //     filesTransfer = files1
    //     document.getElementById("pro-image").files = filesTransfer.files
    //     $scope.check()
    //
    //     // filesTransfer = new DataTransfer();
    //     // document.getElementById("pro-image").files = filesTransfer.files
    //
    // }
    // $scope.check = function (){
    //     console.log(filesTransfer.files, document.getElementById("pro-image").files)
    // }

})

