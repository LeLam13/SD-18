var app = angular.module("donhang-app", [])
app.controller("donhang-ctrl", function ($scope, $http,$sce,$timeout) {
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
    $scope.idTrangThai = null;
    var item = null;

    $scope.notification = {
        show: false,
        message: '',
        type: '',
        icon: ''
    };
    $scope.isCancelDisabled = false;
    //idDonHang
    $scope.getAllOrderOnline = function (){
        $http.get("/don-hang-online").then(function (response) {
            $scope.listOrderOnline = response.data;
            //console.log("check order online: ",$scope.listOrderOnline);
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
        })
    }

    $scope.getOrderOnlineByID = function (orderID) {
        $scope.idDonHang = orderID;
        item = $scope.listOrderOnline.find(item=>item.idDonHang === orderID);
        console.log("item: ",item)
        $http.get("/don-hang-online/"+orderID).then(function (response) {
            $scope.listOrderDetail = response.data;
            console.log("check order online details: ",$scope.listOrderDetail);
            //console.log("check order online details: ",response.data.donHang.trangThai.idTrangThai);
            $scope.showStep(response);
            var itemOrder = null
            var index =0;
            response.data.forEach((item, index) => {
                itemOrder = item.donHang;
            });
            if (response.data && itemOrder) {
                $('#trang-thai').text(itemOrder.trangThai.tenTrangThai);
                $('#phi-van-chuyen').text(itemOrder.phiVanChuyen);
                $('#ma-don-hang').text(itemOrder.maDonHang);
                $('#tong-tien').text(itemOrder.tongTien);
                $('#loai-don-hang').text(itemOrder.loaiDonHang === 2 ? "Đơn hàng online" : "Đơn hàng tại quầy");
                if (itemOrder.trangThaiThanhToan) {
                    $('#tong-tien-thanh-toan').text(itemOrder.tongTienThanhToan);
                } else {
                    $('#tong-tien-thanh-toan').text(0 + itemOrder.phiVanChuyen);
                }
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

    $scope.updateStatuOrder = function (){
        var ghichu = $('#ghi-chu').val();
        $scope.dataStatus ={
            idDonHang: $scope.idDonHang,
            idTrangThai: item.trangThai.idTrangThai,
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
            $('#modal-status').modal('hide');
            $scope.showNotification('Cập Nhật trạng Thái Thành công!','success');
            $scope.showStepUpdate(response);
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
            $scope.showStepUpdate(response);
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

    //ẩn các step
    $scope.hideStep = function (){
        $('#step-1, #step-2, #step-3, #step-4, #step-5,#step-6').hide();
    }



    //load data
    $scope.getAllOrderOnline();
    $scope.hideStep();
})
