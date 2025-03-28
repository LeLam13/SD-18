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
           $scope.tenSP=sanPham.ten;
            console.log("san pham: ", $scope.sanPham);
        }).catch(error => {
            console.error("Failed to fetch product details:", error);
            alert("Could not load product details. Please try again later.");
        });
    } else {
        console.error("Product ID is missing from the URL.");
        alert("Invalid product ID. Please check the URL and try again.");
    }

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


    $scope.tables = []; // Mảng chứa các bảng màu với các kích thước tương ứng

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
                $scope.tables.push({mau: mau, size: allSizes});
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

    $scope.form = {}; // Lưu thông tin nhập liệu và thông báo lỗi cho từng ô input

    $scope.create = function () {
        // Xóa các thông báo lỗi trước khi kiểm tra
        $scope.tables.forEach(function (table) {
            table.size.forEach(function (size) {
                if (!$scope.form[table.mau.idMauSac]) $scope.form[table.mau.idMauSac] = {};
                if (!$scope.form[table.mau.idMauSac][size.idKichCo]) $scope.form[table.mau.idMauSac][size.idKichCo] = {};
                $scope.form[table.mau.idMauSac][size.idKichCo].errorMessages = {}; // Reset lỗi cho mỗi ô input
            });
        });

        var hasError = false;
        $scope.tables.forEach(function (table) {
            table.size.forEach(function (size) {
                var sizeForm = $scope.form[table.mau.idMauSac][size.idKichCo];

                if (sizeForm) {
                    // Kiểm tra số lượng
                    if (sizeForm.soLuong <= 0) {
                        sizeForm.errorMessages.soLuong = "Số lượng phải lớn hơn 0";
                        hasError = true;
                    }
                    if (sizeForm.soLuong == null) {
                        sizeForm.errorMessages.soLuong = "Số lượng không được để trống";
                        hasError = true;
                    }
                    // Kiểm tra giá nhập
                    if (sizeForm.giaNhap <= 0) {
                        sizeForm.errorMessages.giaNhap = "Giá nhập phải lớn hơn 0";
                        hasError = true;
                    }
                    if (sizeForm.giaNhap == null) { // Sửa lỗi thiếu dấu ngoặc tròn
                        sizeForm.errorMessages.giaNhap = "Giá nhập không được để trống";
                        hasError = true;
                    }
                    // Kiểm tra giá bán
                    if (sizeForm.giaBan <= 0) {
                        sizeForm.errorMessages.giaBan = "Giá bán phải lớn hơn 0";
                        hasError = true;
                    }
                    if (sizeForm.giaBan == null) {
                        sizeForm.errorMessages.giaBan = "Giá bán không được để trống";
                        hasError = true;
                    }
                    // Kiểm tra giá nhập < giá bán
                    if (sizeForm.giaNhap >= sizeForm.giaBan) {
                        sizeForm.errorMessages.giaNhap = "Giá nhập phải nhỏ hơn giá bán";
                        sizeForm.errorMessages.giaBan = "Giá bán phải lớn hơn giá nhập";
                        hasError = true;
                    }
                }
            });
        });

        // Nếu có lỗi, dừng lại và hiển thị thông báo lỗi, ngược lại tiếp tục thêm
        if (hasError) {
            return;
        }
        // Xử lý thêm sản phẩm chi tiết nếu không có lỗi
        var SanPhamChiTietList = [];
        $scope.tables.forEach(function (table) {
            table.size.forEach(function (size) {
                var sizeForm = $scope.form[table.mau.idMauSac][size.idKichCo];
                var SanPhamChiTiet = {
                    ma: $scope.generateRandomString(8),
                    idMauSac: table.mau.idMauSac,
                    idKichCo: size.idKichCo,
                    idChatLieu: $scope.selectedChatLieu,
                    idXuatXu: $scope.selectedXuatXu,
                    idSanPham: idSanPham,
                    idThuongHieu: $scope.selectedThuongHieu,
                    idKieuDang: $scope.selectedKieuDang,
                    soLuong: sizeForm.soLuong,
                    giaNhap: sizeForm.giaNhap,
                    giaBan: sizeForm.giaBan
                };
                SanPhamChiTietList.push(SanPhamChiTiet);
            });
        });

        // Gửi dữ liệu lên server
        $http.post("/admin/san-pham/chi-tiet/add", SanPhamChiTietList).then(function (r) {
            alert("Thêm sản phẩm chi tiết thành công!");
            location.href = `/admin/san-pham/` + idSanPham;
        }).catch(function (err) {
            console.log("Thêm sản phẩm không thành công", err);
        });

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

// Hàm cập nhật giá trị cho tất cả các hàng được chọn khi nhập vào một trường bất kỳ
    $scope.updateAllSelectedValues = function (field, value, table) {
        const colorId = table.mau.idMauSac;

        table.size.forEach(function (size) {
            const sizeForm = $scope.form[colorId][size.idKichCo];

            // Chỉ cập nhật các hàng được chọn
            if (sizeForm && sizeForm.selected) {
                sizeForm[field] = value;
            }
        });
    };

// Hàm để đồng bộ khi thay đổi giá trị ở checkbox con
    $scope.syncSelectedValues = function (table, size) {
        const colorId = table.mau.idMauSac;
        const sizeForm = $scope.form[colorId][size.idKichCo];

        // Nếu tất cả checkbox con đều được chọn, đánh dấu checkbox chính
        $scope.form[colorId].allSelected = table.size.every(size =>
            $scope.form[colorId][size.idKichCo] && $scope.form[colorId][size.idKichCo].selected
        );
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


    $scope.handleFileSelect = function(event, colorId, sizeId) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                // Create image data URL for preview
                if (!$scope.form[colorId][sizeId]) {
                    $scope.form[colorId][sizeId] = {};
                }
                $scope.form[colorId][sizeId].imageSrc = e.target.result;
                $scope.form[colorId][sizeId].imageFile = file; // Store the file if you want to upload it later
                $scope.$apply();
            };
            reader.readAsDataURL(file);
        }
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

