var app = angular.module("donhang-app", [])
app.controller("donhang-ctrl", function ($scope, $http) {
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
            $scope.showStep(response)
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
            $scope.showStepUpdate(response);
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
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
            //console.log(`Order ${index} Detail:`, item.donHang);
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
        }
        if(itemOrderStatus.trangThai.idTrangThai ===5){
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').show();
            $('#step-5').show();
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
        }
        if(response.data.trangThai.idTrangThai ===5){
            $('#step-1').show();
            $('#step-2').show();
            $('#step-3').show();
            $('#step-4').show();
            $('#step-5').show();
        }
    }

    //ẩn các step
    $scope.hideStep = function (){
        $('#step-1, #step-2, #step-3, #step-4, #step-5').hide();
    }



    //load data
    $scope.getAllOrderOnline();
    $scope.hideStep();
})
