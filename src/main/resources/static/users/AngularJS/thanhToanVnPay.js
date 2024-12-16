
var app = angular.module("thanh-toan-app", [])
app.controller("thanh-toan-controller",function ($scope,$http) {
    $scope.retryPayment = function() {
        var idDonHang = $('#idDonHang').text();
        console.log("$scope.donHang:", idDonHang);
        const requestData = {
            idDonHang: idDonHang // Dùng mã đơn hàng từ nút bấm
        };
        $http.post('/create-payment', requestData)
            .then(function(response) {
                const paymentUrl = response.data.paymentUrl;
                console.log("Redirecting to VNPay:", paymentUrl);
                window.location.href = paymentUrl; // Điều hướng lại đến VNPay
            })
            .catch(function(error) {
                console.error('Có lỗi xảy ra khi thanh toán lại:', error);
            });
    };


})