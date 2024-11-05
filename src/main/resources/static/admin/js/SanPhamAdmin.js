var app = angular.module('product-admin', []);
app.controller('ctrl', function ($scope, $http) {


    // $scope.items =[];
    // $scope.form ={};
    // $scope.filterDto = {};
    // $scope.page = 0;  // Trang hiện tại
    // $scope.size = 4; // Số lượng bản ghi trên mỗi trang
    // $scope.totalPages = 0; // Tổng số trang
    // $scope.pageInput = 1; // Giá trị nhập từ ô input
    // var isfilter = false;

    //
    //
    // $http.get("/admin/san-pham/get-all").then(r => {
    //     $scope.items = r.data.content;
    //     $scope.totalPage = r.data.totalPages;
    //     $scope.getPageNumbers(r.data.totalPages)
    //     $scope.filterData = {}
    // }).catch(e => console.log(e))
    //
    // $scope.getAll = function (pageNumber){
    //     $scope.pageNumber = pageNumber;
    //     //
    //     // let pagination = document.getElementsByClassName("pagination")[0]
    //     // let pageItem = pagination.getElementsByClassName("pageNumber")
    //
    //     // pageItem.forEach(p => {
    //     //     p.getElementsByTagName("a")[0].className = "page-link"
    //     // })
    //     //
    //     // document.getElementById(pageNumber+"").getElementsByTagName("a")[0].className = "page-link active"
    //
    //     if(!isfilter){
    //         $http.get("/admin/san-pham/get-all?pageNumber="+pageNumber).then(r => {
    //             $scope.items = r.data.content;
    //             // $scope.filterData = {}
    //         }).catch(e => console.log(e))
    //     }else{
    //         $http.post("/admin/san-pham/filter?pageNumber="+pageNumber,$scope.filterDto).then(r => {
    //             $scope.items = r.data.content;
    //         }).catch(e => console.log(e))
    //     }
    // }
    //
    // $scope.getPageNumbers = function (totalPages){
    //     $scope.pageNumbers = []
    //     for (let i = 0; i< totalPages;i++){
    //         $scope.pageNumbers.push(i);
    //     }
    // }

    $scope.items = []
    $scope.page = 0;  // Trang hiện tại
    $scope.size = 4; // Số lượng bản ghi trên mỗi trang
    $scope.totalPages = 0; // Tổng số trang
    $scope.pageInput = 1; // Giá trị nhập từ ô input


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
            ten: $scope.ten
        }

        // Kiểm tra tính hợp lệ của tên
        if ($scope.ten == undefined || $scope.ten.length == 0) {
            document.getElementById("eTenMau").innerText = "Vui lòng nhập tên!!!";
            return;
        }

        if ($scope.ten.length > 100) {
            document.getElementById("eTenMau").innerText = "Tên tối đa 100 ký tự!!!";
            return;
        }

        // Gọi getAll để kiểm tra xem tên đã tồn tại chưa
        $http.get("/admin/san-pham/get-all").then(function (response) {
            var existingSanPham = response.data;
            var tenTonTai = false;

            // Kiểm tra xem tên có trùng với dữ liệu hiện có không
            angular.forEach(existingSanPham, function (item) {
                if (item.ten.toLowerCase() === $scope.ten.toLowerCase()) {
                    tenTonTai = true;
                }
            });

            // Nếu tên đã tồn tại, hiển thị thông báo
            if (tenTonTai) {
                document.getElementById("eTenMau").innerText = "Tên đã tồn tại";
            } else {
                // Nếu không, gửi yêu cầu tạo mới
                $http.post("/admin/san-pham/add", SanPham).then(function (r) {
                    $scope.findAll();
                    alert("Thêm thành công");
                }).catch(function (err) {
                    console.log("Thêm không thành công", err);
                });
            }
        }).catch(function (err) {
            console.log("Lỗi khi lấy dữ liệu", err);
        });
    }


    $scope.getSanPham = function (ma) {
        var url = "/admin/san-pham/chiTiet" + "/" + ma;
        console.log(url)
        $http.get(url).then(function (r) {
            console.log(r.data)
            let SanPham = r.data;
            $scope.idSanPham = SanPham.idSanPham;
            $scope.ma = SanPham.ma;
            $scope.ten = SanPham.ten;
            $scope.createBy = SanPham.createBy;
            $scope.createDate = SanPham.createDate;
            $scope.updateDate = SanPham.updateDate;
            $scope.updateBy = SanPham.updateBy;
            $scope.trangThai = SanPham.trangThai;
        })
    }

    $scope.update = function (ma) {
        if ($scope.ten == undefined || $scope.ten.length == 0) {
            document.getElementById("eTenMauUd").innerText = "Vui lòng nhập tên!!!";
            return
        }
        if ($scope.ten.length > 100) {
            document.getElementById("eTenMauUd").innerText = "Tên tối đa 100 ký tự!!!";
            return
        }

        $http.get("/admin/san-pham/get-all").then(function (response) {
            var existingSanPham = response.data;
            var tenTonTai = false;
            angular.forEach(existingSanPham, function (item) {
                if (item.ten.toLowerCase() === $scope.ten.toLowerCase() && item.ma !== ma) {
                    tenTonTai = true;
                }
            });

            if (tenTonTai) {
                document.getElementById("eTenMauUd").innerText = "Tên đã tồn tại";
                return;
            } else {
                var url = "/admin/san-pham/update" + "/" + ma;
                var updateSanPham = {
                    ma: ma,
                    ten: $scope.ten
                }

                $http.post(url, updateSanPham).then(function (r) {
                    $scope.findAll();
                    alert("Update thành công")
                }).catch(function (err) {
                    console.log("Update khong thanh cong", err);
                })
            }
        }).catch(function (err) {
            console.log("Lỗi khi lấy dữ liệu", err);
        });
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





