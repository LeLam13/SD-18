var app = angular.module("nhan-vien", []);
app.controller("nhan-vien-ctrl", function ($scope, $http) {

    $scope.page = 0;  // Trang hiện tại
    $scope.size = 2; // Số lượng bản ghi trên mỗi trang
    $scope.totalPages = 0; // Tổng số trang
    $scope.pageInput = 1; // Giá trị nhập từ ô input
    $scope.searchQuery = ""; // Biến lưu trữ từ khóa tìm kiếm
    $scope.isSearching = false; // Trạng thái tìm kiếm

    // Hàm tìm tất cả nhân viên
    $scope.findAll = function () {
        let url;
        if ($scope.isSearching) {
            // Nếu đang tìm kiếm, gọi API tìm kiếm
            url = `/admin/nhan-vien/search?keyword=${$scope.searchQuery}&page=${$scope.page}&size=${$scope.size}`;
        } else {
            // Nếu không thì gọi API tìm tất cả
            url = `/admin/nhan-vien/find-all?page=${$scope.page}&size=${$scope.size}`;
        }
        $http.get(url).then(resp => {
            $scope.items = resp.data.content;
            $scope.totalPages = resp.data.totalPages; // Cập nhật tổng số trang
        }).catch(error => {
            console.log(error);
        });
    };

    // Hàm tìm kiếm nhân viên
    $scope.search = function () {
        $scope.isSearching = true; // Đặt trạng thái tìm kiếm
        $scope.page = 0; // Đặt lại trang về đầu
        $scope.findAll(); // Tải lại danh sách theo từ khóa tìm kiếm
    };

    // Hàm làm mới dữ liệu
    $scope.refresh = function () {
        $scope.isSearching = false; // Đặt lại trạng thái tìm kiếm
        $scope.searchQuery = ""; // Xóa từ khóa tìm kiếm
        $scope.page = 0; // Đặt lại trang về đầu
        $scope.findAll(); // Tải lại danh sách tất cả nhân viên
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
        };

        $http.post(url, updateNhanVien).then(function (response) {
            alertify.success("Cập nhật nhân viên thành công")
            $scope.findAll(); // Tải lại trang để xem các thay đổi
        }).catch(function (error) {
            console.log("Cập nhật không thành công:", error);
        });
    };

    // Hàm xóa mềm nhân viên
    $scope.softDelete = function (idNhanVien) {
        if (confirm("Bạn có chắc chắn muốn xóa nhân viên này không?")) {
            var url = `/admin/nhan-vien/delete/${idNhanVien}`;
            $http.post(url).then(function (response) {
                alert("Xóa nhân viên thành công!");
                $scope.findAll(); // Tải lại danh sách nhân viên
            }).catch(function (error) {
                console.log("Xóa không thành công:", error);
            });
        }
    };

});
