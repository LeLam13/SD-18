var app = angular.module("trahang-app", [])
app.controller("trahang-ctrl", function ($scope, $http) {
    $scope.listDonHang = [];
    $scope.donHangChiTiet = [];
    $scope.idDonHang = null;
    $scope.idTrangThai = null;

    $scope.getAllOrder = function (){
        $http.get("/don-hang-tai-quay").then(function (response){
            $scope.listDonHang = response.data;
            console.log("get all order: ",response.data);
        }).catch(function (errors){
            console.error("Có lỗi xảy ra: ",errors);
        })
    }
    
    $scope.getOrderByID = function (id){
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
            if (response.data && itemOrder) {
                $scope.idTrangThai = itemOrder.trangThai.idTrangThai;
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
            $scope.showStatusOrder(response);

        }).catch(function (errors) {
            console.error("Có lỗi xảy ra: ",errors);
        })
    }

    //showModal
    $scope.showModalStatus = function (){
        $('#modal-status').modal('show');
    }

    $scope.updateOrderStatus = function (){
        var chichu = $('#ghi-chu').val();
        $scope.dataStatus ={
            idDonHang: $scope.idDonHang,
            idTrangThai: $scope.idTrangThai,
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
            $scope.showStatusOrderAfterUpdate(response);
            $scope.getAllOrder();
            alert("Cập Nhật Thành công!");
            $('#modal-status').modal('hide');
        }).catch(function(errors) {
            console.error("Có lỗi xảy ra: ",errors);
            alert("Cập Nhật Thất Bại!")
        });
    }


    //ẩn trạng thái
    $scope.hideStatusOrder = function (){
        $('#order-tracking').hide();
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

    //load data
    $scope.hideStatusOrder();
    $scope.getAllOrder();
});
