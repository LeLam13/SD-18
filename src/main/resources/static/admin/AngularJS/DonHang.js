var app = angular.module("donhang-app", [])
app.controller("donhang-ctrl", function ($scope, $http,$sce,$timeout,$interval) {
    $scope.generateRandomString = function(length) {
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
        let result = '';

        for (let i = 0; i < length; i++) {
            const randomIndex = Math.floor(Math.random() * characters.length);
            result += characters[randomIndex];
        }

        return result;
    };


    $scope.message ="hello";
    $scope.listOrderOnline = [];
    $scope.listOrderDetail =[];
    $scope.idDonHang= null;
    // $scope.idTrangThai = null;
    var item = null;
    var idHoaDoncheck = null;
    $scope.idTrangThai = 1;

    $scope.notification = {
        show: false,
        message: '',
        type: '',
        icon: ''
    };
    $scope.isCancelDisabled = false;
    //phân trang
    $scope.currentPage = 1; // Trang hiện tại
    $scope.pageSize = 5;
    //idDonHang
    $scope.getAllOrderOnline = function (){
        $http.get("/don-hang-online").then(function (response) {
            $scope.listOrderOnline = response.data;
            //console.log("check order online: ",$scope.listOrderOnline);
            $scope.totalPages = Math.ceil($scope.listOrderOnline.length / $scope.pageSize); // Tổng số trang
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
        })
    }
    var idDonHangShow = null;
    $scope.getOrderOnlineByID = function (orderID) {
        $scope.startAutoCheck();
        $scope.idDonHang = orderID;
        item = $scope.listOrderOnline.find(item=>item.idDonHang === orderID);
        console.log("item: ",item)
        $http.get("/don-hang-online/"+orderID).then(function (response) {
            $scope.listOrderDetail = response.data;
            console.log("check order online details: ",$scope.listOrderDetail);
            console.log("check order online : ",response.data);
            //$scope.showStep(response);
            var itemOrder = null
            var index =0;
            response.data.forEach((item, index) => {
                itemOrder = item.donHang;
            });

            // $scope.idTrangThai = itemOrder.trangThai.tenTrangThai;
            idDonHangShow = itemOrder.idDonHang;
            if (response.data && itemOrder) {
                if(itemOrder.trangThai.idTrangThai ===1){
                    $('#cancel-order').show();
                }else {
                    $('#cancel-order').hide();
                }
                $('#trang-thai').text(itemOrder.trangThai.tenTrangThai);
                $('#phi-van-chuyen').text(itemOrder.phiVanChuyen);
                $('#ma-don-hang').text(itemOrder.maDonHang);
                $('#tong-tien').text(itemOrder.tongTien);
                $('#loai-don-hang').text(itemOrder.loaiDonHang === 2 ? "Đơn hàng online" : "Đơn hàng tại quầy");
                // if (itemOrder.trangThaiThanhToan) {
                //     $('#tong-tien-thanh-toan').text(itemOrder.tongTienThanhToan);
                // } else {
                //     $('#tong-tien-thanh-toan').text(0 + itemOrder.phiVanChuyen);
                // }
                $('#tong-tien-thanh-toan').text(itemOrder.tongTienThanhToan);
                $('#trang-thai-thanh-toan').text(itemOrder.trangThaiThanhToan ? "Đã thanh toán" : "Chưa thanh toán");

                //khách hàng
                $('#ten-khach-hang').text(itemOrder.tenKhachNhan);
                $('#email-khach-hang').text(itemOrder.emailKhachNhan);
                $('#sdt-khach-hang').text(itemOrder.soDienThoaiKhachNhan);
                $('#dia-chi-khach-hang').text(itemOrder.diaChiNhan);
            }

        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
        })
    }

    $scope.showModalUpdateStatus = function (){
        $('#modal-status').modal('show');
    }

    $scope.updateStatuOrder = function (idTrangThai){
        var ghichu = $('#ghi-chu').val();
        $scope.dataStatus ={
            idDonHang: $scope.idDonHang,
            idTrangThai: idTrangThai,
            ghiChu: ghichu
        }
        var statusData = angular.copy($scope.dataStatus);
        $http({
            method: 'PUT',
            url: '/don-hang-online/cap-nhat-trang-thai',
            data: statusData,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }) .then(function(response) {
            console.log("check order after update: ",response.data);

            $scope.getAllOrderOnline();
            $('#trang-thai').text(response.data.trangThai.tenTrangThai);
            // $('#modal-status').modal('hide');
            $scope.showNotification('Cập Nhật trạng Thái Thành công!','success');
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
            $scope.showNotification('Cập Nhật trạng Thái Thất Bại!','error');
        });
    }

    //huỷ đon hàng
    $scope.cancelOrderStatus = function (){
        var ghichu = $('#ghi-chu').val();
        $scope.calcel ={
            idDonHang: $scope.idDonHang,
            idTrangThai: item.trangThai.idTrangThai,
            ghiChu: ghichu
        }
        var calcelData = angular.copy($scope.calcel);
        $http({
            method: 'PUT',
            url: '/don-hang-online/huy-don',
            data: calcelData,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }) .then(function(response) {
            console.log("check order after cancel: ",response.data);
            $scope.getAllOrderOnline();
            $('#modal-status').modal('hide');
            $scope.showNotification('Cập Nhật Thành công!','success');
            //$scope.showStepUpdate(response);
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
            $scope.showNotification('Cập Nhật trạng Thái!','error');
        });
    }

    //tạo hoá đơn và hoá đơn chi tiết
    $scope.createInvoice = function (){
        $scope.dataInvoice ={
            idDonHang: $scope.idDonHang,
            maHoaDon: $scope.generateRandomString(8)
        }

        var invoiceData = angular.copy($scope.dataInvoice);
        $http({
            method: 'POST',
            url: '/don-hang-online/tao-hoa-don',
            data: invoiceData,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }) .then(function(response) {
            console.log("check invoice create: ",response.data);

        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
        });

    }

    //test send email
    $scope.sendEmailOrder = function(){
        $http.get("").then(function (){

        }).catch(function (error){
            console.error('Có lỗi xảy ra:', error);
        })
    }

    //show step
    $scope.showStep = function (response){
        var itemOrderStatus = null
        var index =0;
        response.data.forEach((item, index) => {
            console.log(`Order ${index} Detail:`, item.donHang);
            itemOrderStatus = item.donHang;
            console.log(`Order ${index+1} Detail:`, itemOrderStatus);
        });
        if(itemOrderStatus.trangThai.idTrangThai ===1){
            $('#step-1').show();
            $('#step-2').hide();
            $('#step-3').hide();
            $('#step-4').hide();
            $('#step-5').hide();
        }
        if(itemOrderStatus.trangThai.idTrangThai ===7){
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').hide();
            $('#step-4').hide();
            $('#step-5').hide();
        }
        if(itemOrderStatus.trangThai.idTrangThai ===2){
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').hide();
            $('#step-5').hide();
        }
        if(itemOrderStatus.trangThai.idTrangThai ===3){
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').show();
            $('#step-5').hide();
            $scope.isCancelDisabled = true;
        }
        if(itemOrderStatus.trangThai.idTrangThai ===5){
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').show();
            $('#step-5').show();
            $scope.isCancelDisabled = true;
        }

        if(itemOrderStatus.trangThai.idTrangThai ===6){
            if ($('#step-1').is(':visible')) {
                console.log('Step 1 đang hiển thị');
            } else {
                console.log('Step 1 không hiển thị');
                $('#step-1').show();
            }
            if ($('#step-2').is(':visible')) {
                console.log('Step 1 đang hiển thị');
            } else {
                console.log('Step 1 không hiển thị');
                $('#step-2').show();
            }
            $('#step-6').show();
            $scope.isCancelDisabled = true;
        }
    }

    $scope.showStepUpdate = function (response){

        if(response.data.trangThai.idTrangThai ===1){
            $('#step-1').show();
            $('#step-2').hide();
            $('#step-3').hide();
            $('#step-4').hide();
            $('#step-5').hide();
        }
        if(response.data.trangThai.idTrangThai ===7){
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').hide();
            $('#step-4').hide();
            $('#step-5').hide();
        }
        if(response.data.trangThai.idTrangThai ===2){
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').hide();
            $('#step-5').hide();
        }
        if(response.data.trangThai.idTrangThai ===3){
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').show();
            $('#step-5').hide();
            $scope.isCancelDisabled = true;
        }
        if(response.data.trangThai.idTrangThai ===5){
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').show();
            $('#step-5').show();
            $scope.isCancelDisabled = true;
        }

        if(response.data.trangThai.idTrangThai ===6){
            if ($('#step-1').is(':visible')) {
                console.log('Step 1 đang hiển thị');
            } else {
                console.log('Step 1 không hiển thị');
                $('#step-1').show();
            }
            if ($('#step-2').is(':visible')) {
                console.log('Step 1 đang hiển thị');
            } else {
                console.log('Step 1 không hiển thị');
                $('#step-2').show();
            }
            $('#step-6').show();
            $scope.isCancelDisabled = true;
        }
    }

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

    $scope.hideStep = function (){
        $('#step-6').hide();
        $('#cancel-order').hide();
    }

    // var intervalPromise = $interval(checkTrangThai, 3000); // Lưu tham chiếu interval
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
                            $('#cancel-order').hide();
                            $scope.stopAutoCheck ();  // Dừng interval
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
                });
        }
    }

    // Lưu giữ tham chiếu đến interval
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

    //in hoa đon online
    $scope.checkInvoice = function (){
        console.log('hoa dơn get id:', idHoaDoncheck);
        $http.get("/hoa-don/get-invoice/"+idHoaDoncheck).then(function (response) {
            console.log('response.data has data',response.data);
            if (response.data && response.data.idHoaDon) {
                console.log('response.data has data',response.data);
                $scope.printerInvoice(response.data.idHoaDon);
            }
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
        })
    }
    //in hoá đơn
    $scope.printerInvoice = function (idHoaDon){
        console.log('check in hoá đơn:');
        $http.get("/hoa-don/invoice/"+idHoaDon).then(function (response) {
            console.log('thanh cong:', response);
            $scope.showNotification("In hoá đơn thành công!","success");
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
            $scope.showNotification("In hoá đơn thất bại!","error");
        })
    }

    //phân trang
    $scope.getPagedProducts = function () {
        const start = ($scope.currentPage - 1) * $scope.pageSize;
        const end = start + $scope.pageSize;
        return $scope.listOrderOnline.slice(start, end); // Lấy danh sách đơn hàng cho trang hiện tại
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
        return $scope.listOrderOnline.length > $scope.pageSize;
    };

    $scope.showModalCancel = function (){
        $('#modal-status').modal('show');
    }
    $scope.showModalConfirm = function (){
        if(idDonHangShow === null){
            $scope.showNotification('Chưa chọn đơn hàng!','error');
        }else {
            $('#confirmModal').modal('show');
        }

    }


    //load data
    $scope.getAllOrderOnline();
    $scope.hideStep();

    var intervalPromiseDH; // Biến quản lý $interval
    var controlTimeout;    // Biến quản lý $timeout
    var intervalTime = 1500; // Thời gian lặp lại $interval
    var pauseTime = 3000;   // Thời gian tạm dừng $interval
    var resumeTime = 2000;  // Thời gian để khởi động lại
    $scope.startAutoCheckOrder = function() {
        // Hàm quản lý chu kỳ chạy và dừng
        function manageInterval() {
            // Khởi động $interval nếu chưa có
            if (!intervalPromiseDH) {
                intervalPromiseDH = $interval(function() {
                    $scope.getAllOrderOnline();
                    console.log("Đang kiểm tra danh sách đơn hàng...");
                }, intervalTime); // Sử dụng thời gian lặp lại được cấu hình
                console.log("Đã bắt đầu tự động kiểm tra danh sách đơn hàng.");
            }

            // Tạm dừng sau pauseTime
            $timeout(function() {
                if (intervalPromiseDH) {
                    $interval.cancel(intervalPromiseDH);
                    intervalPromiseDH = null;
                    console.log("Tạm dừng tự động kiểm tra sau " + pauseTime + "ms.");
                }

                // Khởi động lại sau resumeTime
                controlTimeout = $timeout(manageInterval, resumeTime); // Sử dụng thời gian khởi động lại được cấu hình
            }, pauseTime); // Sử dụng thời gian tạm dừng được cấu hình
        }

        // Bắt đầu chu kỳ
        manageInterval();
    };
    $scope.startAutoCheckOrder();

    // Hủy $interval và $timeout khi controller bị hủy
    $scope.$on('$destroy', function() {
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
    });

})