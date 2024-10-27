var app = angular.module('khachhangApp', []);

app.controller('KhachHangController', function($scope, $http) {
    $scope.khachhangList = [];

    // Function to fetch all customers
    $scope.fetchKhachHang = function() {
        $http.get('/admin/khach-hang/api')
            .then(function(response) {
                console.log("Data fetched:", response.data);
                $scope.khachhangList = response.data; // Store the fetched data
            }, function(error) {
                console.error("Error fetching khach hang data:", error);
            });
    };

    // Initial call to fetch data when the controller is loaded
    $scope.fetchKhachHang();
});
