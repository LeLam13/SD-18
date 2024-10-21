var app = angular.module("thuonghieu", [])
app.controller("thuonghieu-ctrl", function ($scope, $http) {
        const url = "http://localhost:8080/admin/thuong-hieu"

        $scope.generateRandomString = function (length) {
            const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
            let result = '';

            for (let i = 0; i < length; i++) {
                const randomIndex = Math.floor(Math.random() * characters.length);
                result += characters[randomIndex];
            }

            return result;
        };

        $scope.itemss = [];
        // get all
        $scope.getAll = function () {
            $http.get("/admin/thuong-hieu/find-all").then(r => {
                console.log(r.data)
                $scope.itemss = r.data;
            }).catch(e => console.log(e))
        }
        $scope.getAll();

        $scope.create = function () {
            if ($scope.ten == undefined || $scope.ten.length == 0) {
                document.getElementById('erTen').innerText = "Vui lòng nhập tên thương hiệu"
                return;
            }
            if ($scope.ten.length > 100) {
                document.getElementById('erTen').innerText = "Tên thương hiệu tối đa 100 ký tự"
                return;
            }

            $http.get("/admin/thuong-hieu/find-all").then(function (response) {
                var existingThuongHieu = response.data;
                var tenTonTai = false;
                angular.forEach(existingThuongHieu, function (item) {
                    if (item.ten.toLowerCase() === $scope.ten.toLowerCase()) {
                        tenTonTai = true;
                    }
                });

                if (tenTonTai) {
                    document.getElementById("erTen").innerText = "Tên đã tồn tại";
                    return;
                } else {
                    var thuonghieu = {
                        ma: $scope.generateRandomString(8),
                        ten: $scope.ten
                    }
                    var url = "/admin/thuong-hieu/add";
                    $http.post(url, thuonghieu).then(function (response) {
                        $scope.getAll();
                        alert("Thêm Thành Công !");
                    }).catch(error => {
                        console.log("Thêm không thành công", err);
                    });
                }
            });
        }

        // chi tiết
        $scope.findByMa = function (ma) {
            var url = "/admin/thuong-hieu/chiTiet" + "/" + ma;
            $http.get(url).then(function (res) {
                const thuonghieu = res.data;
                $scope.ma = thuonghieu.ma;
                $scope.ten = thuonghieu.ten;
            });
        }
        // add

        //update

        $scope.update = function (ma) {
            if ($scope.ten == undefined || $scope.ten.length == 0) {
                document.getElementById('erTenUd').innerText = "Vui lòng nhập tên thương hiệu"
                return;
            }
            if ($scope.ten.length > 100) {
                document.getElementById('erTenUd').innerText = "Tên thương hiệu tối đa 100 ký tự"
                return;
            }
            $http.get("/admin/thuong-hieu/find-all").then(function (response) {
                var existingThuongHieu = response.data;
                var tenTonTai = false;
                angular.forEach(existingThuongHieu, function (item) {
                    if (item.ten.toLowerCase() === $scope.ten.toLowerCase() && item.ma !== ma) {
                        tenTonTai = true;
                    }
                });

                if (tenTonTai) {
                    document.getElementById("erTenUd").innerText = "Tên đã tồn tại";
                    return;
                } else {
                    var url = "/admin/thuong-hieu/update" + "/" + ma;
                    var thuonghieu = {
                        ma: ma,
                        ten: $scope.ten
                    }
                    $http.post(url, thuonghieu).then(function (resp) {
                        $scope.getAll();
                        alert("Cập Nhật Thành Công")
                    }).catch(function (err) {
                        console.log("Loi: ", err);
                    })
                }
            });
        }


        $scope.delete = function (idThuongHieu) {
            if (confirm("Bạn muốn xóa sản phẩm này?")) {
                var url = "/admin/thuong-hieu/delete" + "/" + idThuongHieu;
                $http.delete(url).then(function (res) {
                    $scope.getAll()
                    alert("Xóa Thành Công !");
                }).catch(error => {
                    alert("Lỗi Xóa !")
                    console.log("error", error);
                })
            }
        }
    }
)
