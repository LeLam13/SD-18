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
            method: 'PUT',
            url: '/don-hang-online/cap-nhat-trang-thai',
            data: invoiceData,
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

    $scope.tables = [
        {
            mau: { idMauSac: 1, ten: 'Đỏ' },
            img: { imageSrc: '' }
        },
        {
            mau: { idMauSac: 2, ten: 'Xanh' },
            img: { imageSrc: '' }
        }
    ];

// Hàm xử lý thay đổi file
    $scope.handleFileChange = function(idMauSac, table) {
        // Tìm input file theo id
        var fileInput = document.getElementById('formFile-' + idMauSac);
        var file = fileInput.files[0];

        // Kiểm tra nếu file hợp lệ
        if (file) {
            table.img.imageSrc = file.name;  // Cập nhật tên file vào table.img.imageSrc
            console.log("Tên file đã chọn:", table.img.imageSrc);  // Debug: In tên file

            // In ra danh sách bảng sau khi cập nhật
            console.log("Danh sách bảng:", $scope.tables);

            // Cập nhật giao diện AngularJS
            $scope.$apply();  // Cập nhật UI nếu cần
        }
    };

    angular.element(document).ready(function () {
        angular.forEach($scope.tables, function (table) {
            var fileInput = document.getElementById('formFile-' + table.mau.idMauSac);
            fileInput.addEventListener('change', function () {
                $scope.handleFileChange(table.mau.idMauSac, table);
            });
        });
    });

    //load data
    $scope.getAllOrderOnline();
    $scope.hideStep();
})
