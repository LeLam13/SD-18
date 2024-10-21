var app = angular.module("kieudang", [])
app.controller("kieudang-ctrl", function ($scope, $http) {
    const url = "http://localhost:8080/admin/kieu-dang"

    $scope.itemss = [];

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
        $http.get("/admin/kieu-dang/find-all").then(r => {
            console.log(r.data)
            $scope.itemss = r.data;
        }).catch(e => console.log(e))
    }

    $scope.getAll();


    //add
    $scope.create = function () {
        if ($scope.ten == undefined || $scope.ten.length == 0) {
            document.getElementById('erTen').innerText = "Vui lòng nhập tên kiểu dáng"
            return;
        }
        if ($scope.ten.length > 100) {
            document.getElementById('erTen').innerText = "Tên kiểu dáng tối đa 100 ký tự"
            return;
        }
        $http.get("/admin/kieu-dang/find-all").then(function (response) {
            var existingKieuDang = response.data;
            var tenTonTai = false;
            angular.forEach(existingKieuDang, function (item) {
                if (item.ten.toLowerCase() === $scope.ten.toLowerCase()) {
                    tenTonTai = true;
                }
            });

            if (tenTonTai) {
                document.getElementById("erTen").innerText = "Tên đã tồn tại";
                return;
            } else {
                var kieuDang = {
                    ma: $scope.generateRandomString(8),
                    ten: $scope.ten
                }
                var url = "/admin/kieu-dang/add";
                $http.post(url, kieuDang).then(function (response) {
                    $scope.getAll();
                    alertify.success("Thêm Kiểu Dáng thành công")
                }).catch
                (function (err) {
                    console.log("Loi: ", err);
                })
            }
        })
    };

    //Chi tiet
    $scope.findByMa = function (ma) {
        var url = "/admin/kieu-dang/chiTiet" + "/" + ma;
        $http.get(url).then(function (res) {
            const kieudang = res.data;
            $scope.ma = kieudang.ma;
            $scope.ten = kieudang.ten;
        });
    }


    //update
    $scope.update = function (ma) {
        if ($scope.ten == undefined || $scope.ten.length == 0) {
            document.getElementById('erTenUd').innerText = "Vui lòng nhập tên kiểu dáng"
            return;
        }
        if ($scope.ten.length > 100) {
            document.getElementById('erTenUd').innerText = "Tên kiểu dáng tối đa 100 ký tự"
            return;
        }
        $http.get("/admin/kieu-dang/find-all").then(function (response) {
            var existingKieuDang = response.data;
            var tenTonTai = false;
            angular.forEach(existingKieuDang, function (item) {
                if (item.ten.toLowerCase() === $scope.ten.toLowerCase() && item.ma !== ma) {
                    tenTonTai = true;
                }
            });

            if (tenTonTai) {
                document.getElementById("erTenUd").innerText = "Tên đã tồn tại";
                return;
            } else {
                var url = "/admin/kieu-dang/update" + "/" + ma;
                var kieudang = {
                    ma: ma,
                    ten: $scope.ten
                }
                $http.post(url, kieudang).then(function (resp) {
                    $scope.getAll();
                    alert("Cập nhật thành công");
                }).catch(function (err) {
                    console.log("Loi: ", err);
                })
            }
        })
    }

// xóa
    $scope.delete = function (idKieuDang) {
        if (confirm("Bạn muốn xóa Kiểu Dáng này?")) {
            var url = "/admin/kieu-dang/delete" + "/" + idKieuDang;
            $http.delete(url).then(function (res) {
                location.reload();
                alertify.success("Xóa Kiểu Dáng thành công")
            }).catch(error => {
                alertify.error("Xóa Kiểu Dáng thất bại")
                console.log("error", error);
            })
        }
    }
})