var app = angular.module("trahang-app", [])
app.controller("trahang-ctrl", function ($scope, $http,$interval) {
    $scope.listDonHang = [];
    $scope.donHangChiTiet = [];
    $scope.idDonHang = null;
    $scope.idTrangThai = null;
    $scope.notification = {
        show: false,
        message: '',
        type: '',
        icon: ''
    };
    //phân trang
    $scope.currentPage = 1; // Trang hiện tại
    $scope.pageSize = 5;

    $scope.getAllOrder = function (){
        $http.get("/don-hang-tai-quay").then(function (response){
            $scope.listDonHang = response.data;
            console.log("get all order: ",response.data);
            $scope.totalPages = Math.ceil($scope.listDonHang.length / $scope.pageSize); // Tổng số trang
        }).catch(function (errors){
            console.error("Có lỗi xảy ra: ",errors);
        })
    }
    var idDonHangShow = null;
    $scope.getOrderByID = function (id){
        $scope.startAutoCheck();
        $scope.idDonHang = id;
        $http.get("/don-hang-tai-quay/"+id).then(function (response) {
            $scope.donHangChiTiet = response.data;
            console.log("get all order Detail: ",response.data);
            var itemOrder = null
            var index =0;
            response.data.forEach((item, index) => {
                //console.log(`Order ${index} Detail:`, item.donHang);
                itemOrder = item.donHang;
                //console.log(`Order ${index+1} Detail:`, itemOrder);
            });
            idDonHangShow = itemOrder.idDonHang;
            if (response.data && itemOrder) {
                // $scope.idTrangThai = itemOrder.trangThai.idTrangThai;
                $('#tongHoaDon').val(itemOrder.tongTien);
                $('#tongTienKhuyenMai').val(itemOrder.tongTienKhuyenMai);
                if(itemOrder.trangThaiThanhToan){
                    $('#tienKhachThanhToan').val(itemOrder.tongTienSauKhuyenMai);
                }else {
                    $('#tienKhachThanhToan').val(0);
                }


                if (itemOrder.phuongThucNhan === 2) {
                    $('#phuongThucNhan').val("Vận Chuyển(Ship)");
                }

                if (itemOrder.phuongThucNhan === 1) {
                    $('#phuongThucNhan').val("Nhận Hàng Tại Quầy");
                }

                $('#trangThaiThanhToan').val(itemOrder.trangThaiThanhToan ? "Đã thanh toán" : "Chưa thanh toán");

                //khach mua
                $('#ten-khach-mua').val(itemOrder.khachHang.hoTen);
                $('#sdt-khach-mua').val(itemOrder.khachHang.soDienThoai);
                $('#emal-khach-mua').val(itemOrder.khachHang.email);
                $('#dia-chi-khach-mua').val(itemOrder.khachHang.diaChi);
                //khách nhân
                if (itemOrder.phuongThucNhan === 2) {
                    $('#ten-khach-nhan').val(itemOrder.tenKhachNhan);
                    $('#sdt-khach-nhan').val(itemOrder.soDienThoaiKhachNhan);
                    //$('#email-khach-nhan').val(itemOrder.khachHang.email);
                    $('#dia-chi-nhan').val(itemOrder.diaChiNhan);
                }
            } else {
                console.error("Dữ liệu donHang không tồn tại trong response.");
            }
            //$scope.showStatusOrder(response);

        }).catch(function (errors) {
            console.error("Có lỗi xảy ra: ",errors);
        })
    }

    //showModal
    $scope.showModalStatus = function (){
        $('#modal-status').modal('show');
    }

    $scope.updateOrderStatus = function (idTrangThai){
        var chichu = $('#ghi-chu').val();
        $scope.dataStatus ={
            idDonHang: $scope.idDonHang,
            idTrangThai: idTrangThai,
            ghiChu: chichu
        }
        var orderStatus = angular.copy($scope.dataStatus);
        $http({
            method: 'PUT',
            url: '/don-hang-tai-quay/cap-nhat-trang-thai',
            data: orderStatus,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }) .then(function(response) {
            console.log("status after update: ",response.data);
            //$scope.showStatusOrderAfterUpdate(response);
            $scope.getAllOrder();
            $scope.showNotification('Cập Nhật trạng Thái Thành công!','success');
            $('#modal-status').modal('hide');
        }).catch(function(errors) {
            console.error("Có lỗi xảy ra: ",errors);
            $scope.showNotification('Cập Nhật trạng Thái Thất Bại!','error');
        });
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

    // $scope.hideStep = function (){
    //     $('#step-6').hide();
    // }

    // var intervalPromise = $interval(checkTrangThai, 3000); // Lưu tham chiếu interval
    var intervalPromise;
    $scope.startAutoCheck = function() {
        // Khởi động interval khi nhấn nút
        if (!intervalPromise) {
            intervalPromise = $interval(checkTrangThai, 2000); // Lưu tham chiếu interval
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
                            $scope.stopAutoCheck ();  // Dừng interval

                        }
                    }
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


    //ẩn trạng thái
    $scope.hideStatusOrder = function (){
        $('#step-6').hide();
    }
    //hiển thị trạng thái
    $scope.showStatusOrder = function (response){
        var itemOrderStatus = null
        var index =0;
        response.data.forEach((item, index) => {
            //console.log(`Order ${index} Detail:`, item.donHang);
            itemOrderStatus = item.donHang;
            //console.log(`Order ${index+1} Detail:`, itemOrder);
        });
        $('#order-tracking').show();
        if(itemOrderStatus.phuongThucNhan ===1 && itemOrderStatus.trangThai.idTrangThai ===5){
            $('#step-1').hide();
            $('#step-2').hide();
            $('#step-3').hide();
            $('#step-4').show();
        }

        if(itemOrderStatus.phuongThucNhan ===2 && itemOrderStatus.trangThai.idTrangThai ===2){
            console.log("check log status")
            $('#step-1').show();
            $('#step-2').hide();
            $('#step-3').hide();
            $('#step-4').hide();
        }
        if(itemOrderStatus.phuongThucNhan ===2 && itemOrderStatus.trangThai.idTrangThai ===3){
            console.log("check log status")
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').hide();
            $('#step-4').hide();
        }
        if(itemOrderStatus.phuongThucNhan ===2 && itemOrderStatus.trangThai.idTrangThai ===4){
            console.log("check log status")
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').hide();
        }
        if(itemOrderStatus.phuongThucNhan ===2 && itemOrderStatus.trangThai.idTrangThai ===5){
            console.log("check log status")
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').show();
        }
    }

    $scope.showStatusOrderAfterUpdate = function (response){
        var itemOrderStatus = response.data;
        if(itemOrderStatus.phuongThucNhan ===1 && itemOrderStatus.trangThai.idTrangThai ===5){
            $('#step-1').hide();
            $('#step-2').hide();
            $('#step-3').hide();
            $('#step-4').show();
        }

        if(itemOrderStatus.phuongThucNhan ===2 && itemOrderStatus.trangThai.idTrangThai ===2){
            console.log("check log status")
            $('#step-1').show();
            $('#step-2').hide();
            $('#step-3').hide();
            $('#step-4').hide();
        }
        if(itemOrderStatus.phuongThucNhan ===2 && itemOrderStatus.trangThai.idTrangThai ===3){
            console.log("check log status")
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').hide();
            $('#step-4').hide();
        }
        if(itemOrderStatus.phuongThucNhan ===2 && itemOrderStatus.trangThai.idTrangThai ===4){
            console.log("check log status")
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').hide();
        }
        if(itemOrderStatus.phuongThucNhan ===2 && itemOrderStatus.trangThai.idTrangThai ===5){
            console.log("check log status")
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').show();
        }
    }

    //phân trang
    $scope.getPagedProducts = function () {
        const start = ($scope.currentPage - 1) * $scope.pageSize;
        const end = start + $scope.pageSize;
        return $scope.listDonHang.slice(start, end); // Lấy danh sách đơn hàng cho trang hiện tại
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
        return $scope.listDonHang.length > $scope.pageSize;
    };

    //load data
    $scope.hideStatusOrder();
    $scope.getAllOrder();
});
