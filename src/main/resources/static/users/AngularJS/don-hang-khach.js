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
    var idDonHangShow = null;
    $scope.getOrderOfUserByIdDonHang = function (idDonHang){
        $scope.startAutoCheck();
        $http.get("/don-hang-cua-khach/lay-don-hang/"+idDonHang).then(function (response) {
            console.log("check order detail username: ",response.data);
            $scope.listOrderDetail = response.data;
            console.log("listOrderDetail; ",$scope.listOrderDetail);
            var itemOrder = null
            var index =0;
            response.data.forEach((item, index) => {
                itemOrder = item.donHang;
            });
            idDonHangShow = itemOrder.idDonHang;
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

    // $scope.activeStep = 1;
    // $scope.setActiveStep = function(stepId) {
    //     $scope.activeStep = stepId;
    //     updateProgressLine(stepId);
    // };
    //
    // function updateProgressLine(stepId) {
    //     $(".step").removeClass("active"); // Loại bỏ lớp active khỏi tất cả các bước
    //
    //     for(let i = 1; i <= stepId; i++){
    //         $("#" + "step-" + i).addClass("active");
    //     }
    // }
    var intervalPromise;
    $scope.startAutoCheck = function() {
        // Khởi động interval khi nhấn nút
        if (!intervalPromise) {
            intervalPromise = $interval(checkTrangThai, 3000); // Lưu tham chiếu interval
            console.log("Đã bắt đầu tự động kiểm tra trạng thái.");
        }
    };
    $scope.stopAutoCheck = function() {
        // Dừng interval khi trạng thái đạt 5
        if (intervalPromise) {
            $interval.cancel(intervalPromise);
            intervalPromise = null; // Đặt lại tham chiếu interval
            console.log("Đã dừng tự động kiểm tra trạng thái.");
        }
    };
    // var intervalPromise = $interval(checkTrangThai, 3000); // Lưu tham chiếu interval

    function checkTrangThai() {
        if(idDonHangShow ===null){
            $scope.showActive(1);
        }else {

            $http.get('/api/getTrangThai/' +idDonHangShow)  // Gọi API để lấy trạng thái mới
                .then(function(response) {
                    // Cập nhật idTrangThai từ phản hồi server
                    //$scope.idTrangThai = response.data.trangThai.idTrangThai;
                    const newTrangThai = response.data.trangThai.idTrangThai;
                    // Chỉ cập nhật giao diện nếu trạng thái thay đổi
                    if ($scope.idTrangThai !== newTrangThai) {
                        console.log("check.....")
                        if(newTrangThai ===1){
                            console.log(newTrangThai)
                            $scope.showActive(1);
                        }
                        if(newTrangThai ===7){
                            console.log(newTrangThai)
                            $scope.showActive(2);
                        }
                        if(newTrangThai ===2){
                            console.log(newTrangThai)
                            $scope.showActive(3);
                        }
                        if(newTrangThai ===3){
                            console.log(newTrangThai)
                            $scope.showActive(4);
                        }
                        if(newTrangThai ===5){
                            console.log("Trạng thái đạt 5, dừng tự động!");
                            $scope.showActive(5);
                            $scope.stopAutoCheck(); // Dừng interval

                        }
                        // $scope.idTrangThai = newTrangThai;
                        // $scope.showActive($scope.idTrangThai); // Cập nhật giao diện
                    }
                    //$scope.showActive($scope.idTrangThai);
                })
                .catch(function(error) {
                    console.error("Có lỗi khi lấy trạng thái", error);
                });
        }

    }
    $scope.showActive = function(idTrangThai) {
        $(".step").removeClass("active");
        for (let i = 1; i <= idTrangThai; i++) {
            $("#" + "step-" + i).addClass("active"); // Thêm class active cho các bước từ 1 đến idTrangThai
        }
    };
    //load data
    $scope.getOrderOfUser();
});