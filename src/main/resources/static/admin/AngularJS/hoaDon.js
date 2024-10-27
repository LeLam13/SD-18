var app = angular.module("hoaDon-app", [])
app.controller("hoaDon-ctrl", function ($scope, $http) {
    $scope.hoaDons = []; // Array to hold Hoa Don data

    // Function to get Hoa Don data from the API
    $scope.getHoaDons = function() {
        $http.get('/admin/hoa-don/api').then(function(response) {
            $scope.hoaDons = response.data; // Assign the data to the scope variable
        }, function(error) {
            console.error("Error fetching Hoa Dons:", error);
        });
    };
    // Call the function to fetch Hoa Dons on load
    $scope.getHoaDons();
});