var app = angular.module("hoaDon-app", [])
app.controller("hoaDon-ctrl", function ($scope, $http) {
    $scope.hoaDons = []; // Array to hold Hoa Don data

    // Function to get Hoa Don data from the API
    $scope.getHoaDons = function(page = 0) {
        $http.get('/admin/hoa-don/api?page=' + page).then(function(response) {
            $scope.hoaDons = response.data.content; // Lấy danh sách hoaDons
            $scope.currentPage = response.data.number; // Lưu trang hiện tại
            $scope.totalPages = response.data.totalPages; // Tổng số trang
        }, function(error) {
            console.error("Error fetching Hoa Dons:", error);
        });
    };
    $scope.searchHoaDon = function() {
        $http.get('/admin/hoa-don/search?maHoaDon=' + $scope.searchMaHoaDon).then(function(response) {
            $scope.hoaDons = response.data; // Cập nhật danh sách hóa đơn với kết quả tìm kiếm
        }, function(error) {
            console.error("Error searching Hoa Dons:", error);
        });
    };
    $scope.viewDetails = function(idHoaDon) {
        $http.get('/admin/hoa-don/detail/' + idHoaDon).then(function(response) {
            $scope.detail = response.data;
            console.log(response.data);
            $('#detailsModal').modal('show');
        }, function(error) {
            console.error("Error fetching detail:", error);
        });
    };
    $scope.closeModal = function () {
        $('#detailsModal').modal('hide'); // Đóng modal
    };

    // Call the function to fetch Hoa Dons on load
    $scope.getHoaDons();



});