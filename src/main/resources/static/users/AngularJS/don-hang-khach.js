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

    //phân trang
    $scope.currentPage = 1; // Trang hiện tại
    $scope.pageSize = 5;

    //function

    //lấy tất cả hoá đơn của username đăng nhập
    $scope.getOrderOfUser = function (){
        $http.get("/don-hang-cua-khach/lay-don-hang").then(function (response) {
            //console.log("check order username: ",response.data);
            $scope.listOrder = response.data;
            $scope.totalPages = Math.ceil($scope.listOrder.length / $scope.pageSize); // Tổng số trang
        }).catch(function (error) {
            console.error("có lỗi xảy ra: ",error);
            if (error.status === -1 || error.status ===500) { // Lỗi kết nối server
                console.log("Server không phản hồi. Dừng tự động kiểm tra danh sách đơn hàng.");
                isAutoCheckStopped = true; // Đặt cờ để ngăn việc khởi động lại
                if (intervalPromiseDH) {
                    $interval.cancel(intervalPromiseDH);
                    intervalPromiseDH = null;
                }
                if (controlTimeout) {
                    $timeout.cancel(controlTimeout);
                    controlTimeout = null;
                }
                if (intervalPromise) {
                    $interval.cancel(intervalPromise);
                    intervalPromise = null;
                }
            }
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
        isAutoCheckStopped = true;
        $scope.stopAutoCheck();
        if (!inputData || inputData.trim() === "") {
            $scope.getOrderOfUser();
        } else {
            $http.get("/don-hang-cua-khach/tim-kiem", {
                params: { maDonHang: inputData }
            }).then(function (response) {
                $scope.listOrder = response.data;
                $scope.totalPages = Math.ceil($scope.listOrder.length / $scope.pageSize); // Tổng số trang
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
            $scope.showNotification("Chưa điền lý do huỷ đơn!",'error');
            return;
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
            $('#cancel-order').hide();
            $scope.showNotification('Huỷ Đơn Thành công!', 'success');
        }).catch(function (error) {
            console.error('Có lỗi xảy ra:', error);
            if (error.data && error.data.message) {
                $scope.showNotification(error.data.message, 'error');
            } else {
                $scope.showNotification('Huỷ Đơn Thất Bại! Đã xảy ra lỗi không xác định.', 'error');
            }
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
        isAutoCheckStopped = true;
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
                            $('#cancel-order').hide();
                            $scope.stopAutoCheck(); // Dừng interval
                        }

                        $('#trang-thai').text(response.data.trangThai.tenTrangThai);
                    }
                })
                .catch(function(error) {
                    console.error("Có lỗi khi lấy trạng thái", error);
                    if (error.status === -1 || error.status === 500) { // Lỗi kết nối server
                        console.log("Server không phản hồi. Dừng tự động kiểm tra.");
                        $scope.stopAutoCheck();
                    }
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
    //phân trang
    $scope.getPagedProducts = function () {
        const start = ($scope.currentPage - 1) * $scope.pageSize;
        const end = start + $scope.pageSize;
        return $scope.listOrder.slice(start, end); // Lấy danh sách đơn hàng cho trang hiện tại
    };

    // Chuyển đến trang khác
    $scope.setPage = function (page) {
        if (page >= 1 && page <= $scope.totalPages) {
            $scope.currentPage = page;
        }
    };

    $scope.getPaginationRange = function () {
        const rangeSize = 5; // Số lượng trang muốn hiển thị (mặc định là 5)
        let start = Math.max($scope.currentPage - Math.floor(rangeSize / 2), 1);
        const end = Math.min(start + rangeSize - 1, $scope.totalPages);

        // Điều chỉnh lại nếu các trang bị vượt giới hạn
        start = Math.max(Math.min(start, $scope.totalPages - rangeSize + 1), 1);

        const pages = [];
        for (let i = start; i <= end; i++) {
            pages.push(i);
        }
        return pages;
    };

    $scope.isPaginationVisible = function () {
        return $scope.listOrder.length > $scope.pageSize;
    };

    //lấy tất cả trạng thái
    $scope.listStatus = [];
    $scope.getAllStatus = function (){
        $http.get("/don-hang/lay-trang-thai").then(function (response) {
            $scope.listStatus = response.data;
            console.log("check $scope.listStatus: ",$scope.listStatus);
        }).catch(function (errors) {
            console.error("Có lỗi xảy ra trong quá trình ",errors);
            $scope.showNotification("Có lỗi xảy ra trong quá trình ", "error");
        })
    }

    $scope.selectedTrangThai = null;
    $scope.searchTrangThai = function (){
        $scope.stopAutoCheck();
        var idTrangThai = $scope.selectedTrangThai; // Lấy giá trị từ ng-model
        if (!idTrangThai || idTrangThai.length === 0) {
            $scope.getOrderOfUser();
        }
        $http({
            method: 'GET',
            url: '/don-hang/tim-kiem-trang-thai-don-hang', // URL cơ bản
            params: { idTrangThai: idTrangThai } // Truyền trực tiếp tham số
        }).then(function (response) {
            if(response && response.data === null){
                $scope.getOrderOfUser();
            }else {
                $scope.listOrder = response.data;
                console.log("get all order search: ",response.data);
                $scope.totalPages = Math.ceil($scope.listOrder.length / $scope.pageSize); // Tổng số trang
            }
        }).catch(function (error) {
            console.error("Có lỗi khi lấy trạng thái", error);
        })
    }


    //load data
    $scope.hideCancel();
    $scope.getOrderOfUser();
    $scope.getAllStatus();

    var intervalPromiseDH; // Biến quản lý $interval
    var controlTimeout;    // Biến quản lý $timeout
    var intervalTime = 1500; // Thời gian lặp lại $interval
    var pauseTime = 3000;   // Thời gian tạm dừng $interval
    var resumeTime = 2000;  // Thời gian để khởi động lại
    // intervalPromiseDH = $interval(function() {
    //     $scope.getOrderOfUser();
    // }, 1500);
    let isAutoCheckStopped = false;
    $scope.startAutoCheckOrder = function() {
        if (isAutoCheckStopped) {
            console.log("Tự động kiểm tra đã bị dừng, không khởi động lại.");
            return;
        }

        function manageInterval() {
            if (!intervalPromiseDH) {
                intervalPromiseDH = $interval(function() {
                    if (isAutoCheckStopped) {
                        $interval.cancel(intervalPromiseDH);
                        intervalPromiseDH = null;
                        return;
                    }
                    $scope.getOrderOfUser();
                    console.log("Đang kiểm tra danh sách đơn hàng...");
                }, intervalTime);
                console.log("Đã bắt đầu tự động kiểm tra danh sách đơn hàng.");
            }

            $timeout(function() {
                if (intervalPromiseDH) {
                    $interval.cancel(intervalPromiseDH);
                    intervalPromiseDH = null;
                    console.log("Tạm dừng tự động kiểm tra sau " + pauseTime + "ms.");
                }

                if (!isAutoCheckStopped) {
                    controlTimeout = $timeout(manageInterval, resumeTime);
                }
            }, pauseTime);
        }

        manageInterval();
    };
    $scope.startAutoCheckOrder();
    
    // Hủy $interval và $timeout khi controller bị hủy
    // $scope.$on('$destroy', function() {
    //     if (intervalPromise) {
    //         $interval.cancel(intervalPromise);
    //         intervalPromise = null; // Giải phóng interval
    //         console.log("Đã dừng interval khi chuyển trang.");
    //     }
    // });

    $scope.$on('$destroy', function() {
        isAutoCheckStopped = true;
        if (intervalPromiseDH) {
            $interval.cancel(intervalPromiseDH);
            intervalPromiseDH = null;
            console.log("Đã dừng $interval khi chuyển trang.");
        }
        if (controlTimeout) {
            $timeout.cancel(controlTimeout);
            controlTimeout = null;
            console.log("Đã dừng $timeout khi chuyển trang.");
        }
            if (intervalPromise) {
                $interval.cancel(intervalPromise);
                intervalPromise = null; // Giải phóng interval
                console.log("Đã dừng interval khi chuyển trang.");
            }
    });
});