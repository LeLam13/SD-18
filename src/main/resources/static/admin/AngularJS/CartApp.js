var app = angular.module("cart-app1", [])
app.controller("cart-ctrl1", function ($scope, $http) {
    $scope.listProducts =[];

    $scope.getAllProduct = function (){
        $http.get("/danh-sach-san-pham").then(function (response){
            $scope.listProducts = response.data;
            console.log("check log: ",response.data)
        }).catch(function (errors){
            console.error("có lỗi xảy ra: ",errors)
        })
    }



});