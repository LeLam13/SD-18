var app = angular.module("trahang-app", [])
app.controller("trahang-ctrl", function ($scope, $http) {
    $scope.listDonHang = [];
    $scope.donHangChiTiet = [];

    $scope.getAllOrder = function (){
        $http.get("/don-hang-tai-quay").then(function (response){
            $scope.listDonHang = response.data;
            console.log("get all order: ",response.data);
        }).catch(function (errors){
            console.error("Có lỗi xảy ra: ",errors);
        })
    }
    
    $scope.getOrderByID = function (id){
        $http.get("/don-hang-tai-quay/"+id).then(function (response) {
            $scope.donHangChiTiet = response.data;
            console.log("get all order Detail: ",response.data);
        }).catch(function (errors) {
            console.error("Có lỗi xảy ra: ",errors);
        })
    }

    //ẩn trạng thái
    $scope.hideStatusOrder = function (){
        $('#order-tracking').hide();
    }
    //hiển thị trạng thái
    $scope.showStatusOrder = function (){
        $('#order-tracking').show();
    }

    //load data
    $scope.hideStatusOrder();
    $scope.getAllOrder();
});
