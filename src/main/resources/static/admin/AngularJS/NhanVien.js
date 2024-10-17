var app = angular.module("nhan-vien", []);
app.controller("nhan-vien-ctrl", function ($scope, $http) {

    $scope.form = {};
    $scope.items = [];

    $scope.findAll = function () {
        $http.get("/admin/nhan-vien/find-all").then(resp => {
            console.log(resp.data); // Thêm dòng này để kiểm tra dữ liệu trả về
            $scope.items = resp.data;
        }).catch(error => {
            console.log(error);
        });
    };

    $scope.findAll();

    $scope.getNhanVien = function (idNhanVien) {
        var url = "/admin/nhan-vien/chiTiet" + "/" + idNhanVien;
        console.log(url);
        $http.get(url).then(function (response) {
            console.log(response.data);
            let nhanVien = response.data;
            $scope.idNhanVien = nhanVien.idNhanVien;
            $scope.hoTen = nhanVien.hoTen;
            $scope.soDienThoai = nhanVien.soDienThoai;
            $scope.ngaySinh = nhanVien.ngaySinh;
            $scope.soCanCuocCongDan = nhanVien.soCanCuocCongDan;
            $scope.diaChi = nhanVien.diaChi;
            $scope.gioiTinh = nhanVien.gioiTinh;
            $scope.username = nhanVien.taikhoan.username;
            $scope.email = nhanVien.taikhoan.email;
        }).catch(function (error) {
            console.log("Lỗi khi lấy thông tin nhân viên:", error);
        });
    };

    $scope.update = function (idNhanVien) {
        if ($scope.hoTen == undefined || $scope.hoTen.length == 0) {
            document.getElementById("eHoTen").innerText = "Vui lòng nhập họ tên!!!";
            return;
        }
        if ($scope.hoTen.length > 100) {
            document.getElementById("eHoTen").innerText = "Họ tên tối đa 100 ký tự!!!";
            return;
        }
        var url = "/admin/nhan-vien/update" + "/" + idNhanVien;
        var updateNhanVien = {
            idNhanVien: idNhanVien,
            hoTen: $scope.hoTen,
            soDienThoai: $scope.soDienThoai,
            ngaySinh: $scope.ngaySinh,
            soCanCuocCongDan: $scope.soCanCuocCongDan,
            diaChi: $scope.diaChi,
            gioiTinh: $scope.gioiTinh,
            taikhoan: {
                username: $scope.username,
                email: $scope.email
            }
        };

        $http.post(url, updateNhanVien).then(function (response) {
            alert("Cập nhật thành công!");
            location.reload(); // Tải lại trang để xem các thay đổi
        }).catch(function (error) {
            console.log("Cập nhật không thành công:", error);
        });
    };


});
