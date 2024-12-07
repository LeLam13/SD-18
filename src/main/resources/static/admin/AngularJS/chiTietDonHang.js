var app = angular.module("chiTiet-app", [])
app.controller("chiTiet-ctrl",function ($scope,$location, $http,$interval,$sce, $timeout){
    var fullUrl = $location.absUrl();
    console.log("Full URL:", fullUrl);
    var id = fullUrl.split('/').pop();
    console.log("ID từ URL:", id);

    //khai báo scope
    $scope.donHangChiTiet = [];
    $scope.idDonHang = null;
    $scope.notification = {
        show: false,
        message: '',
        type: '',
        icon: ''
    };

    var idDonHangShow = null;

    $scope.getOrderByID = function (){
        //$scope.idDonHang = id;
        $http.get("/don-hang-tai-quay/"+id).then(function (response) {
            $scope.donHangChiTiet = response.data;
            console.log("getorder Detail: ",response.data);
            var itemOrder = null
            var index =0;
            response.data.forEach((item, index) => {
                itemOrder = item.donHang;
            });
            idDonHangShow = itemOrder.idDonHang;
            if (response.data && itemOrder) {
                //console.log("check Detail: ",itemOrder);
                $('#trang-thai').text(itemOrder.trangThai.tenTrangThai);
                $('#ma-don-hang').text(itemOrder.maDonHang);
                if (itemOrder.loaiDonHang === 2) {
                    $('#loai-don-hang').text("Đơn hàng online");
                }
                if (itemOrder.phuongThucNhan === 1) {
                    $('#loai-don-hang').text("Đơn hàng tại quầy");
                }

                $('#tong-tien').text(itemOrder.tongTien);
                $('#tong-tien-thanh-toan').text(itemOrder.tongTienThanhToan);
                $('#phi-van-chuyen').text(itemOrder.phiVanChuyen);

                //khach mua
                $('#ten-khach-mua').val(itemOrder.khachHang.hoTen);
                $('#sdt-khach-mua').val(itemOrder.khachHang.soDienThoai);
                $('#emal-khach-mua').val(itemOrder.khachHang.taikhoan.email);
                $('#dia-chi-khach-mua').val(itemOrder.khachHang.diaChi);
                //khách nhân
                if (itemOrder.phuongThucNhan === 2) {
                    $('#ten-khach-nhan').val(itemOrder.tenKhachNhan);
                    $('#sdt-khach-nhan').val(itemOrder.soDienThoaiKhachNhan);
                    $('#email-khach-nhan').val(itemOrder.emailKhachNhan);
                    $('#dia-chi-nhan').val(itemOrder.diaChiNhan);
                }
            } else {
                console.error("Dữ liệu donHang không tồn tại trong response.");
            }

        }).catch(function (errors) {
            console.error("Có lỗi xảy ra: ",errors);
        })
    }

    $scope.statusOrder = [1, 7, 2, 3, 5];
    $scope.currentStatus = 1;
    $scope.updateOrderStatus = function (idTrangThai){

        const currentIndex = $scope.statusOrder.indexOf($scope.currentStatus);
        const newIndex = $scope.statusOrder.indexOf(idTrangThai);
        // Kiểm tra nếu trạng thái mới nằm trước trạng thái hiện tại
        if (newIndex <= currentIndex) {
            $scope.showNotification('Không thể quay lại trạng thái trước hoặc cập nhật trạng thái hiện tại!', 'error');
            return;
        }

        // Nếu trạng thái hiện tại đã là 5, không cho phép cập nhật
        if ($scope.currentStatus === 5) {
            $scope.showNotification('Không thể cập nhật vì đơn hàng đã hoàn thành!', 'error');
            return;
        }

        var chichu = $('#ghi-chu').val();
        $scope.dataStatus ={
            idDonHang: id,
            idTrangThai: idTrangThai,
            ghiChu: chichu
        }
        console.log("status after update $scope.dataStatus: ",$scope.dataStatus);
        var orderStatus = angular.copy($scope.dataStatus);
        $http({
            method: 'PUT',
            url: '/don-hang-tai-quay/cap-nhat-trang-thai',
            data: orderStatus,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);
            }
        }) .then(function(response) {
            console.log("status after update: ",response.data);
            //$scope.showStatusOrderAfterUpdate(response);
            $scope.currentStatus = idTrangThai;
            //$scope.getAllOrder();
            $scope.showNotification('Cập Nhật trạng Thái Thành công!','success');
            //$('#modal-status').modal('hide');
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
        }, 2000);
    };

    //ẩn trạng thái
    $scope.hideStatusOrder = function (){
        $('#step-6').hide();
    }

    //load dữ liệu mặc định
    $scope.getOrderByID();
    $scope.hideStatusOrder();



    // tự động đồng bộ dữ liệu
    var intervalPromise;
    $scope.startAutoCheck = function() {
        // Khởi động interval khi nhấn nút
        if (!intervalPromise) {
            intervalPromise = $interval(checkTrangThai, 1500);
            console.log("Đã bắt đầu tự động kiểm tra trạng thái.");
        }
    };
    $scope.stopAutoCheck = function() {
        // Dừng interval khi trạng thái đạt 5
        if (intervalPromise) {
            $interval.cancel(intervalPromise);
            intervalPromise = null;
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
                            $scope.stopAutoCheck();  // Dừng interval
                        }
                        $('#trang-thai').text(response.data.trangThai.tenTrangThai);
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
            $("#" + "step-" + i).addClass("active");
        }
    };

    $scope.startAutoCheck();
});