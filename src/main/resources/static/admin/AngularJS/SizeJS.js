var app = angular.module("size", [])
app.controller("size-ctrl" ,function ($scope, $http){
    $scope.items = []
    $scope.page = 0;  // Trang hiện tại
    $scope.size = 4; // Số lượng bản ghi trên mỗi trang
    $scope.totalPages = 0; // Tổng số trang
    $scope.pageInput = 1; // Giá trị nhập từ ô input


    $scope.findAll = function () {
        var url = `/admin/size/find-all?page=${$scope.page}&size=${$scope.size}`;
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

    $scope.generateRandomString = function (length) {
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
        let result = '';

        for (let i = 0; i < length; i++) {
            const randomIndex = Math.floor(Math.random() * characters.length);
            result += characters[randomIndex];
        }

        return result;
    };

    $scope.getAll = function () {
        $http.get("/admin/size/get-all").then(resp => {
            console.log(resp.data)
            $scope.items = resp.data;
        }).catch(error => {
            console.log(error)
        });
    }

    $scope.findAll();

    $scope.create = function () {
        var KichCo = {
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
        $http.get("/admin/size/get-all").then(function (response) {
            var existingKichCo = response.data;
            var tenTonTai = false;

            // Kiểm tra xem tên có trùng với dữ liệu hiện có không
            angular.forEach(existingKichCo, function (item) {
                if (item.ten.toLowerCase() === $scope.ten.toLowerCase()) {
                    tenTonTai = true;
                }
            });

            // Nếu tên đã tồn tại, hiển thị thông báo
            if (tenTonTai) {
                document.getElementById("eTenMau").innerText = "Tên đã tồn tại";
            } else {
                // Nếu không, gửi yêu cầu tạo mới
                $http.post("/admin/size/add", KichCo).then(function (r) {
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


    $scope.getKichCo = function (ma) {
        var url = "/admin/size/chiTiet" + "/" + ma;
        console.log(url)
        $http.get(url).then(function (r) {
            console.log(r.data)
            let KichCo = r.data;
            $scope.idKichCo=KichCo.idKichCo;
            $scope.ma = KichCo.ma;
            $scope.ten = KichCo.ten;
            $scope.createBy = KichCo.createBy;
            $scope.createDate = KichCo.createDate;
            $scope.updateDate = KichCo.updateDate;
            $scope.updateBy = KichCo.updateBy;
            $scope.trangThai = KichCo.trangThai;
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

        $http.get("/admin/size/get-all").then(function (response) {
            var existingKichCo = response.data;
            var tenTonTai = false;
            angular.forEach(existingKichCo, function (item) {
                if (item.ten.toLowerCase() === $scope.ten.toLowerCase() && item.ma !== ma) {
                    tenTonTai = true;
                }
            });

            if (tenTonTai) {
                document.getElementById("eTenMauUd").innerText = "Tên đã tồn tại";
                return;
            } else {
                var url = "/admin/size/update" + "/" + ma;
                var KichCo = {
                    ma: ma,
                    ten: $scope.ten
                }

                $http.post(url, KichCo).then(function (r) {
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

    $scope.updateTT = function (idKichCo) {
        if (confirm("Xác nhận đổi?")) {
            var url = "/admin/size/updateTT" + "/" + idKichCo;
            $http.post(url).then(function (r) {
                alert("Doi thành công!!!")
                $scope.findAll();
            }).catch(function (err) {
                console.log("Loi: ", err);
            })
        }
    }

    $scope.delete = function (idKichCo) {
        if (confirm("Xác nhận xóa?")) {
            var url = "/admin/size/delete" + "/" + idKichCo;
            $http.delete(url).then(function (r) {
                alert("Delete thành công!!!")
                $scope.findAll();
            }).catch(error => {
                alert("Lỗi Xóa !")
                console.log("error", error);
            })
        }
    }
})

