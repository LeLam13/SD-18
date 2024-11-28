var app = angular.module("mau-sac", [])
app.controller("mau-sac-ctrl", function ($scope, $http,$sce,$timeout) {

    $scope.items = []
    $scope.page = 0;  // Trang hiện tại
    $scope.size = 4; // Số lượng bản ghi trên mỗi trang
    $scope.totalPages = 0; // Tổng số trang
    $scope.pageInput = 1; // Giá trị nhập từ ô input


    $scope.findAll = function () {
        var url = `/admin/mau-sac/find-all?page=${$scope.page}&size=${$scope.size}`;
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

    $scope.notification = {
        show: false,
        message: '',
        type: '',
        icon: ''
    };

    $scope.showNotification = function(message, type) {
        $scope.notification.message = message;
        $scope.notification.type = type;

        // Chọn icon dựa trên loại thông báo
        if (type === 'success') {
            $scope.notification.icon = $sce.trustAsHtml('✔️');
        } else if (type === 'error') {
            $scope.notification.icon = $sce.trustAsHtml('❌');
        } else {
            $scope.notification.icon = $sce.trustAsHtml('ℹ️');
        }

        $scope.notification.show = true;

        // Sử dụng $timeout để tự động ẩn sau 5 giây
        $timeout(function() {
            $scope.notification.show = false;
        }, 3000);
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
        $http.get("/admin/mau-sac/get-all").then(resp => {
            console.log(resp.data)
            $scope.items = resp.data;
        }).catch(error => {
            console.log(error)
        });
    }

    $scope.findAll();

    $scope.create = function () {
        var mauSac = {
            ma: $scope.ma?.trim(),
            ten: $scope.ten?.trim()
        };

        let check = true;

        // Hàm hiển thị lỗi
        const showError = (id, message) => {
            document.getElementById(id).innerText = message || "";
        };

        // Xóa lỗi trước khi kiểm tra
        showError("eMaMau", "");
        showError("eTenMau", "");

        // Kiểm tra mã
        if (!mauSac.ma) {
            showError("eMaMau", "Vui lòng chọn mã!!!");
            check = false;
        }

        // Kiểm tra tên
        if (!mauSac.ten) {
            showError("eTenMau", "Vui lòng nhập tên!!!");
            check = false;
        } else if (mauSac.ten.length > 100) {
            showError("eTenMau", "Tên tối đa 100 ký tự!!!");
            check = false;
        }

        if (!check) return; // Dừng nếu có lỗi

        // Gọi getAll để kiểm tra xem tên đã tồn tại chưa
        $http.get("/admin/mau-sac/get-all").then(function (response) {
            var existingMauSac = response.data;

            // Kiểm tra trùng tên
            const tenTonTai = existingMauSac.some(item => item.ten.toLowerCase() === mauSac.ten.toLowerCase());

            if (tenTonTai) {
                showError("eTenMau", "Tên đã tồn tại");
            } else {
                // Gửi yêu cầu tạo mới
                $http.post("/admin/mau-sac/add", mauSac).then(function (r) {
                    $scope.findAll();
                    alert("Thêm thành công");
                }).catch(function (err) {
                    console.error("Thêm không thành công", err);
                });
            }
        }).catch(function (err) {
            console.error("Lỗi khi lấy dữ liệu", err);
        });
    };


    $scope.getMauSac = function (idMauSac) {
        var url = "/admin/mau-sac/chiTiet" + "/" + idMauSac;
        console.log(url)
        $http.get(url).then(function (r) {
            console.log(r.data)
            let mauSac = r.data;
            $scope.idMauSac = mauSac.idMauSac;
            $scope.ma = mauSac.ma;
            $scope.ten = mauSac.ten;
            $scope.createBy = mauSac.createBy;
            $scope.createDate = mauSac.createDate;
            $scope.updateDate = mauSac.updateDate;
            $scope.updateBy = mauSac.updateBy;
            $scope.trangThai = mauSac.trangThai;
        })
    }


    $scope.update = function (idMauSac) {
        let check = true;
// Hàm hiển thị lỗi
        const showError = (id, message) => {
            document.getElementById(id).innerText = message || "";
        };

        // Xóa lỗi trước khi kiểm tra
        showError("eMaMauUd", "");
        showError("eTenMauUd", "");

        // Kiểm tra mã
        if ($scope.ma == undefined || $scope.ma.length == 0) {
            showError("eMaMauUd", "Vui lòng chọn mã!!!");
            check =

                false;
        }

        // Kiểm tra tên
        if ($scope.ten == undefined || $scope.ten.length == 0) {
            showError("eTenMauUd", "Vui lòng nhập tên!!!");
            check = false;
        } else if ($scope.ten.length > 100) {
            showError("eTenMauUd", "Tên tối đa 100 ký tự!!!");
            check = false;
        }

        if (!check) return; // Dừng nếu có lỗi


        $http.get("/admin/mau-sac/get-all").then(function (response) {
            var existingMauSac = response.data;
            var tenTonTai = false;
            angular.forEach(existingMauSac, function (item) {
                if (item.ten.toLowerCase() === $scope.ten.toLowerCase() && item.idMauSac !== idMauSac) {
                    tenTonTai = true;
                }
            });

            if (tenTonTai) {
                document.getElementById("eTenMauUd").innerText = "Tên đã tồn tại";
                return;
            } else {
                var url = "/admin/mau-sac/update" + "/" + idMauSac;
                var updateMau = {
                    idMauSac:idMauSac,
                    ma: $scope.ma,
                    ten: $scope.ten
                }
                console.log("data", updateMau);
                $http.post(url, updateMau).then(function (r) {
                    $scope.findAll();
                    alert("Update thanh cong");
                }).catch(function (err) {
                    console.log("Update khong thanh cong", err);
                })
            }
        }).catch(function (err) {
            console.log("Lỗi khi lấy dữ liệu", err);
        });
    }

    $scope.updateTT = function (idMauSac) {
        if (confirm("Xác nhận đổi?")) {
            var url = "/admin/mau-sac/updateTT" + "/" + idMauSac;
            $http.post(url).then(function (r) {
                alert("Doi thành công!!!")
                $scope.findAll();
            }).catch(function (err) {
                console.log("Loi: ", err);
            })
        }
    }

    $scope.delete = function (idMauSac) {
        if (confirm("Xác nhận xóa?")) {
            var url = "/admin/mau-sac/delete" + "/" + idMauSac;
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