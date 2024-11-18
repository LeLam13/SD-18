// var app = angular.module("gio-hang", [])
app.controller("donhanguser-ctrl", function ($scope, $http,$interval){
    //Khai báo
    $scope.listOrder = [];
    $scope.listOrderDetail =[];


    //function

    //lấy tất cả hoá đơn của username đăng nhập
    $scope.getOrderOfUser = function (){
        $http.get("/don-hang-cua-khach/lay-don-hang").then(function (response) {
            console.log("check order username: ",response.data);
            $scope.listOrder = response.data;
        }).catch(function (errors) {
            console.error("có lỗi xảy ra: ",errors);
        })
    }

    //idDonHang lấy chi tiết của đơn hàng
    $scope.getOrderOfUserByIdDonHang = function (idDonHang){
        $http.get("/don-hang-cua-khach/lay-don-hang/"+idDonHang).then(function (response) {
            console.log("check order detail username: ",response.data);
            $scope.listOrderDetail = response.data;
            console.log("listOrderDetail; ",$scope.listOrderDetail);
            var itemOrder = null
            var index =0;
            response.data.forEach((item, index) => {
                itemOrder = item.donHang;
            });
            if (response.data && itemOrder) {
                $('#trang-thai').text(itemOrder.trangThai.tenTrangThai);
                //$('#phi-van-chuyen').text(itemOrder.phiVanChuyen);
                $('#ma-don-hang').text(itemOrder.maDonHang);
                // $('#tong-tien').text(itemOrder.tongTien);
                $('#loai-don-hang').text(itemOrder.loaiDonHang === 2 ? "Đơn hàng online" : "Đơn hàng tại quầy");
                // if (itemOrder.trangThaiThanhToan) {
                //     $('#tong-tien-thanh-toan').text(itemOrder.tongTienThanhToan);
                // } else {
                //     $('#tong-tien-thanh-toan').text(0 + itemOrder.phiVanChuyen);
                // }
                $('#trang-thai-thanh-toan').text(itemOrder.trangThaiThanhToan ? "Đã thanh toán" : "Chưa thanh toán");

                //khách hàng
                $('#ten-khach-hang').text(itemOrder.tenKhachNhan);
                $('#email-khach-hang').text(itemOrder.emailKhachNhan);
                $('#sdt-khach-hang').text(itemOrder.soDienThoaiKhachNhan);
                $('#dia-chi-khach-hang').text(itemOrder.diaChiNhan);
            }
        }).catch(function (errors) {
            console.error("có lỗi xảy ra: ",errors);
        })
    }

    //format VND
    $scope.formatCurrency = function (amount, currencyCode = 'VND') {
        return amount.toLocaleString('vi-VN', { style: 'currency', currency: currencyCode });
    };

    $scope.getSum = function (){
        if($scope.listOrderDetail === null || $scope.listOrderDetail.length === 0){
            return 0;
        }
        let sumMoney = 0;
        $scope.listOrderDetail.forEach(function (details){
            sumMoney += details.soLuong * details.donGia;
        });
        console.log("listOrderDetail; ",sumMoney);
        return sumMoney;
        //return  sumMoney;
    }

    $scope.getSumPay = function (){
        var phiVanChuyen = $('#phi-van-chuyen').text();
        if($scope.listOrderDetail === null || $scope.listOrderDetail.length === 0){
            return 0;
        }
        let sumMoney = 0;
        $scope.listOrderDetail.forEach(function (details){
            sumMoney += details.soLuong * details.donGia;
        });

        console.log("listOrderDetail; ",sumMoney);
        if($scope.listOrderDetail.donHang.trangThaiThanhToan){
            return sumMoney+ phiVanChuyen;
        }
        return  0;
        //return  sumMoney;
    }

    $scope.getShip = function (){
        if($scope.listOrderDetail === null || $scope.listOrderDetail.length === 0){
            return 0;
        }
        let sumMoney = 0;
        $scope.listOrderDetail.forEach(function (orderDetail) {
            if (orderDetail.donHang && orderDetail.donHang.phiVanChuyen !== undefined) {
                sumMoney = orderDetail.donHang.phiVanChuyen;
            }
        });
        return  sumMoney;
    }

    $scope.activeStep = 1;
    $scope.setActiveStep = function(stepId) {
        $scope.activeStep = stepId;
        var currentWidth = getProgressLineWidth();
        console.log("Current Width:", currentWidth);
        updateProgressLine(stepId, currentWidth);
    };

    function getProgressLineWidth() {
        var progressLine = document.querySelector('.progress-line'); // Lấy phần tử progress-line
        var computedStyle = window.getComputedStyle(progressLine, '::before'); // Lấy computed style của ::before

        // Trả về chiều rộng của pseudo-element
        return parseFloat(computedStyle.width);
    }

    function updateProgressLine(stepId, currentWidth) {
        //$(".step").removeClass("active"); // Loại bỏ lớp active khỏi tất cả các bước
        $("#" + "step-" + stepId).addClass("active"); // Thêm lớp active vào bước được chọn
        var width = ((stepId) * 20) +currentWidth;
        var width = currentWidth + currentWidth;
        console.log("Old Width:", currentWidth, "New Width:", width);
        // Cập nhật phần đường thẳng với chiều rộng mới
        $(".progress-line::before").css("width", width + "%");
    }

    // Khởi tạo giá trị idTrangThai
    $scope.idTrangThai = 1; // Bắt đầu từ bước 1

    // Hàm để hiển thị các biểu tượng và thay đổi màu sắc theo idTrangThai
    $scope.showActive = function(idTrangThai) {
        // Đặt lại tất cả các biểu tượng về trạng thái ban đầu (màu sắc mặc định)
        $(".step").removeClass("active");

        // Lặp qua từng biểu tượng và thay đổi màu sắc dựa trên idTrangThai
        for (let i = 1; i <= idTrangThai; i++) {
            $("#" + "step-" + i).addClass("active"); // Thêm class active cho các bước từ 1 đến idTrangThai
            $("#" + "step-" + i + " .icon").css("background-color", "#2196F3"); // Đổi màu icon
            $("#" + "step-" + i + " .arrow").css("background-color", "#2196F3"); // Đổi màu arrow
        }
    };

    // Hàm tự động tăng idTrangThai từ 1 đến 5 mỗi giây
    $interval(function() {
        if ($scope.idTrangThai < 5) {
            $scope.idTrangThai++;  // Tăng idTrangThai lên 1
        } else {
            $scope.idTrangThai = 1;  // Khi đạt đến 5, reset lại về 1
        }
    }, 3000);  // Cập nhật mỗi 1000ms (1 giây)

    // Gọi showActive mỗi khi idTrangThai thay đổi
    $scope.$watch('idTrangThai', function(newVal) {
        $scope.showActive(newVal); // Gọi showActive mỗi khi idTrangThai thay đổi
    });

    //load data
    $scope.getOrderOfUser();
});