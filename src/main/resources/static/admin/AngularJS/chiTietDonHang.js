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
    $scope.idCheckTrangThai = null;

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
            idCheckTrangThai = itemOrder.trangThai.idTrangThai;
            console.log("idCheckTrangThai: ",idCheckTrangThai);
            if (response.data && itemOrder) {
                //console.log("check Detail: ",itemOrder);
                $('#trang-thai').text(itemOrder.trangThai.tenTrangThai);
                $('#ma-don-hang').text(itemOrder.maDonHang);
                if (itemOrder.loaiDonHang === 2) {
                    $('#loai-don-hang').text("Đơn hàng online");
                    $('#phuong-thuc-thanh-toan').text(
                        itemOrder.phuongThucThanhToan.idPhuongThucThanhToan ===2 ? "Thanh Toán Online(ví VNPay)" : "Thanh Toán Sau Khi Nhận");
                }
                if(itemOrder.loaiDonHang === 1){
                    $('#loai-don-hang').text("Đơn hàng tại quầy");
                    $('#phuong-thuc-thanh-toan').text(
                        itemOrder.phuongThucThanhToan.idPhuongThucThanhToan ===2 ? "Thanh Toán Online" : "Tiền mặt");
                }
                // if (itemOrder.phuongThucNhan === 1) {
                //     $('#loai-don-hang').text("Đơn hàng tại quầy");
                // }

                $('#tong-tien').text(itemOrder.tongTien);
                $('#tong-tien-thanh-toan').text(itemOrder.tongTienThanhToan);
                $('#phi-van-chuyen').text(itemOrder.phiVanChuyen);
                $('#trang-thai-thanh-toan').text(itemOrder.trangThaiThanhToan ? "Đã thanh toán" : "Chưa thanh toán");
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
        if (newIndex !== currentIndex + 1) {
            $scope.showNotification('Trạng thái phải được cập nhật theo thứ tự đúng!', 'error');
            return;
        }
        // Kiểm tra nếu trạng thái mới nằm trước trạng thái hiện tại
        if (newIndex <= currentIndex) {
            $scope.showNotification('Không thể quay lại trạng thái trước hoặc cập nhật trạng thái hiện tại!', 'error');
            return;
        }

        // Nếu trạng thái hiện tại đã là 5, không cho phép cập nhật
        if ($scope.currentStatus === 5 || $scope.currentStatus === 6) {
            $scope.showNotification('Không thể cập nhật vì đơn hàng đã hoàn thành hoặc huỷ đơn!', 'error');
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

    $scope.cancelOrderStatus = function (){
        var ghichu = $('#ghi-chu').val();
        $scope.calcel ={
            idDonHang: id,
            idTrangThai: 6,
            ghiChu: ghichu
        }
        if(ghichu === null || ghichu ===""){
            $scope.showNotification("Chưa điền lý do huỷ đơn!",'error')
        }
        var calcelData = angular.copy($scope.calcel);
        console.log("calcelData: ",calcelData);
        $http.put('/don-hang-tai-quay/huy-don-hang', calcelData, {
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            console.log("check order after cancel: ", response.data);
            //$scope.getOrderOfUser();
            $scope.currentStatus = 6;
            $('#modal-status').modal('hide');
            $('#btn-huy').hide();
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

    $scope.checkInvoice = function (){
        console.log('hoa dơn get id:', id);//lấy hoá đơn theo id của đơn hàng
        $http.get("/don-hang/get-invoice/"+id).then(function (response) {
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
        $http({
            method: 'GET',
            url: "/don-hang/invoice/" + idHoaDon,
            headers: {
                'Accept': 'text/plain' // Đảm bảo server trả về plain text (URL của file PDF)
            },
            responseType: 'text' // Đảm bảo AngularJS xử lý đúng dữ liệu (URL file PDF)
        }).then(function (response) {
            console.log('Phản hồi thành công:', response.data);

            // Nếu có URL file PDF
            if (response.data) {
                console.log('URL file PDF:', response.data);

                // Kiểm tra xem URL có hợp lệ không trước khi mở
                if (response.data.startsWith('/assets/pdf/')) {
                    // Mở file PDF trong tab mới
                    window.open(response.data, '_blank');
                } else {
                    console.warn('URL không hợp lệ:', response.data);
                    $scope.showNotification("Không tìm thấy file PDF!", "error");
                }
            } else {
                console.warn('Phản hồi không chứa URL file PDF.');
                $scope.showNotification("Không tìm thấy file PDF!", "error");
            }
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
            $scope.showNotification("Có lỗi xảy ra khi tạo file PDF.", "error");
        });
        // $http.get("/don-hang/invoice/"+idHoaDon).then(function (response) {
        //     console.log('thanh cong:', response);
        //     $scope.showNotification("In hoá đơn thành công!","success");
        // }).catch(function (errors) {
        //     console.error('Có lỗi xảy ra:', errors);
        //     $scope.showNotification("In hoá đơn thất bại!","error");
        // })
    }

    $scope.showCancelOrder = function() {
        $(".step").removeClass("active");
        $("#step-6").show();
        $("#step-1").addClass("active");
        $("#step-6").addClass("active");
    };

    $scope.showModalCancel = function (){
        $('#confirmModal').modal('hide');
        $('#modal-status').modal('show');
    }

    $scope.showModalConfirm = function (){
        // if(idDonHangShow === null){
        //     $scope.showNotification('Chưa chọn đơn hàng!','error');
        // }else {
        //     $('#confirmModal').modal('show');
        // }
        $('#confirmModal').modal('show');
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
        if(idCheckTrangThai !==1){
            $('#btn-huy').hide();
        }else {
            $('#btn-huy').show();
        }
        $('#step-6').hide();
    }

    $scope.hideInvoiceOrder = function (){
        if(idCheckTrangThai === 7){
            $('#btn-in-hoa-don').show();
        }else {
            $('#btn-in-hoa-don').hide();
        }

    }

    //load dữ liệu mặc định
    $scope.getOrderByID();
    $('#step-6').hide();
    // $scope.hideStatusOrder();
    // $scope.hideInvoiceOrder();


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
                    $scope.currentStatus = newTrangThai;
                    // Chỉ cập nhật giao diện nếu trạng thái thay đổi
                    if ($scope.idTrangThai !== newTrangThai) {
                        $scope.idCheckTrangThai = newTrangThai;
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
                    if (error.status === -1 || error.status === 500) { // Lỗi kết nối server
                        console.log("Server không phản hồi. Dừng tự động kiểm tra.");
                        $scope.stopAutoCheck();
                    }
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

    $scope.$on('$destroy', function() {
        $scope.stopAutoCheck();
        if (intervalPromise) {
            $interval.cancel(intervalPromise);
            intervalPromise = null;
            console.log("Đã dừng tự động kiểm tra trạng thái.");
        }
    });
});