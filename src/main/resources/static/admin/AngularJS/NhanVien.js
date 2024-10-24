var app = angular.module("nhan-vien", []);
app.controller("nhan-vien-ctrl", function ($scope, $http) {

    $scope.page = 0;  // Trang hiện tại
    $scope.size = 2; // Số lượng bản ghi trên mỗi trang
    $scope.totalPages = 0; // Tổng số trang
    $scope.pageInput = 1; // Giá trị nhập từ ô input

    // Hàm tìm tất cả nhân viên
    $scope.findAll = function () {
        var url = `/admin/nhan-vien/find-all?page=${$scope.page}&size=${$scope.size}`;
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

    // Hàm nhảy tới trang theo số trang nhập từ ô input
    $scope.goToPage = function () {
        var pageNumber = $scope.pageInput - 1; // Đảm bảo trang bắt đầu từ 0
        if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
            $scope.page = pageNumber;
            $scope.findAll();
        } else {
            alert("Số trang không hợp lệ.");
        }
    };
    // Khởi động để load dữ liệu
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
