// var app = angular.module("gio-hang", [])
app.controller("donhanguser-ctrl", function ($scope, $http,$interval,$sce,$timeout){
    //Khai báo
    $scope.listOrder = [];
    $scope.listOrderDetail =[];

    $scope.notification = {
        show: false,
        message: '',
        type: '',
        icon: ''
    };

    //function

    //lấy tất cả hoá đơn của username đăng nhập
    $scope.getOrderOfUser = function (){
        $http.get("/don-hang-cua-khach/lay-don-hang").then(function (response) {
            //console.log("check order username: ",response.data);
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
            //console.log("listOrderDetail; ",$scope.listOrderDetail);
            var itemOrder = null
            var index =0;
            response.data.forEach((item, index) => {
                itemOrder = item.donHang;
            });
            idDonHangShow = itemOrder.idDonHang;
            if (response.data && itemOrder) {
                if(itemOrder.trangThai.idTrangThai ===1){
                    $('#cancel-order').show();
                }else {
                    $('#cancel-order').hide();
                }
                $('#trang-thai').text(itemOrder.trangThai.tenTrangThai);
                $('#ma-don-hang').text(itemOrder.maDonHang);
                $('#loai-don-hang').text(itemOrder.loaiDonHang === 2 ? "Đơn hàng online" : "Đơn hàng tại quầy");

                $('#trang-thai-thanh-toan').text(itemOrder.trangThaiThanhToan ? "Đã thanh toán" : "Chưa thanh toán");

                //khách hàng
                if(itemOrder.loaiDonHang === 1){
                    $('#ten-khach-hang').text(itemOrder.khachHang.hoTen);
                    $('#email-khach-hang').text(itemOrder.khachHang.email);
                    $('#sdt-khach-hang').text(itemOrder.khachHang.soDienThoai);
                    $('#dia-chi-khach-hang').text(itemOrder.khachHang.diaChi);
                }else {
                    $('#ten-khach-hang').text(itemOrder.tenKhachNhan);
                    $('#email-khach-hang').text(itemOrder.emailKhachNhan);
                    $('#sdt-khach-hang').text(itemOrder.soDienThoaiKhachNhan);
                    $('#dia-chi-khach-hang').text(itemOrder.diaChiNhan);
                }

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
        //console.log("listOrderDetail; ",sumMoney);
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

        //console.log("listOrderDetail; ",sumMoney);
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
    $scope.inputData = "";
    $scope.searchOrder = function (inputData){
        if (!inputData || inputData.trim() === "") {
            $scope.getOrderOfUser();
        } else {
            $http.get("/don-hang-cua-khach/tim-kiem", {
                params: { maDonHang: inputData }
            }).then(function (response) {
                $scope.listOrder = response.data;
                //console.log("Check PD search: ", response.data);
            }).catch(function (errors) {
                console.error('Có lỗi xảy ra:', errors);
            });
        }
    }

    $scope.showModalCancel = function (){
        $('#confirmModal').modal('hide');
        $('#modal-status').modal('show');
    }
    $scope.showModalConfirm = function (){
        if(idDonHangShow === null){
            $scope.showNotification('Chưa chọn đơn hàng!','error');
        }else {
            $('#confirmModal').modal('show');
        }

    }

    //huỷ đơn
    $scope.cancelOrderStatus = function (){
        var ghichu = $('#ghi-chu').val();
        $scope.calcel ={
            idDonHang: idDonHangShow,
            idTrangThai: 6,
            ghiChu: ghichu
        }
        if(ghichu === null || ghichu ===""){
            $scope.showNotification("Chưa điền lý do huỷ đơn!",'error')
        }
        var calcelData = angular.copy($scope.calcel);
        console.log("calcelData: ",calcelData);
        $http.put('/don-hang-cua-khach/huy-don', calcelData, {
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            console.log("check order after cancel: ", response.data);
            $scope.getOrderOfUser();
            $('#modal-status').modal('hide');
            $scope.showNotification('Huỷ Đơn Thành công!', 'success');
        }).catch(function (error) {
            console.error('Có lỗi xảy ra:', error);
            $scope.showNotification('Huỷ Đơn Thất Bại!', 'error');
        });

    };


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
    //$scope.idTrangThai = null;
    function checkTrangThai() {
        if(idDonHangShow ===null){
            $scope.showActive(1);
        }else {
            $http.get('/api/getTrangThai/' +idDonHangShow)  // Gọi API để lấy trạng thái mới
                .then(function(response) {
                    // Cập nhật idTrangThai từ phản hồi server
                    const newTrangThai = response.data.trangThai.idTrangThai;
                    // Chỉ cập nhật giao diện nếu trạng thái thay đổi
                    if ($scope.idTrangThai !== newTrangThai) {
                        console.log("check.....",response.data.trangThai)
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
                        if(newTrangThai === 6){
                            console.log("Trạng thái đạt 6, dừng tự động!");
                            $scope.showCancelOrder();
                            $scope.stopAutoCheck(); // Dừng interval
                        }

                        $('#trang-thai').text(response.data.trangThai.tenTrangThai);
                    }
                })
                .catch(function(error) {
                    console.error("Có lỗi khi lấy trạng thái", error);
                });
        }

        $scope.getOrderOfUser();
    }
    $scope.showActive = function(idTrangThai) {
        $(".step").removeClass("active");
        for (let i = 1; i <= idTrangThai; i++) {
            $("#" + "step-" + i).addClass("active"); // Thêm class active cho các bước từ 1 đến idTrangThai
        }
    };
    $scope.showCancelOrder = function() {
        $(".step").removeClass("active");
        $("#step-6").show();
        $("#step-1").addClass("active");
        $("#step-6").addClass("active");
    };

    //hiển thị thông báo
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
        }, 2000);
    };

    $scope.hideCancel = function (){
        if(idDonHangShow !==1){
            $('#cancel-order').hide();
        }
        $('#step-6').hide();
    }
    //load data
    $scope.hideCancel();
    $scope.getOrderOfUser();
});