var app = angular.module('khachhangApp', []);

app.controller('KhachHangController', function($scope, $http) {
    $scope.khachhangList = [];
    $scope.searchQuery = "";
    $scope.newKhachHang = {};

    // Fetch all customers
    $scope.fetchKhachHang = function() {
        $http.get('/admin/khach-hang/api')
            .then(function(response) {
                console.log("Data fetched:", response.data);
                $scope.khachhangList = response.data;
            }, function(error) {
                console.error("Error fetching khach hang data:", error);
            });
    };

    // Show Add Customer Form
    $scope.showAddForm = function() {
        $scope.newKhachHang = {}; // Reset form data
        document.getElementById('addKhachHangModal').style.display = 'block';
    };

    // Close Add Customer Form
    $scope.closeAddForm = function() {
        document.getElementById('addKhachHangModal').style.display = 'none';
    };

    // Add new customer
    $scope.addKhachHang = function() {
        console.log("Dữ liệu khách hàng mới:", $scope.newKhachHang); // Log để kiểm tra dữ liệu
        $http.post('/admin/khach-hang/api/add', $scope.newKhachHang)
            .then(function(response) {
                console.log("Khách hàng đã được thêm:", response.data);
                $scope.khachhangList.push(response.data); // Thêm khách hàng vào danh sách
                $scope.closeAddForm(); // Đóng form
            }, function(error) {
                console.error("Lỗi khi thêm khách hàng:", error);
            });
    };

    // Initial call to fetch data
    $scope.fetchKhachHang();
    // Hiển thị Form Thêm Khách Hàng
    $scope.showAddForm = function() {
        $scope.newKhachHang = {}; // Reset dữ liệu form
        $('#viewAdd').modal('show'); // Hiển thị modal bằng Bootstrap
    };

// Đóng Form Thêm Khách Hàng
    $scope.closeAddForm = function() {
        $('#viewAdd').modal('hide'); // Ẩn modal bằng Bootstrap
    };
    // Các biến phân trang
    $scope.currentPage = 1;          // Trang hiện tại
    $scope.pageSize = 5;             // Số khách hàng mỗi trang
    $scope.totalPages = 0;           // Tổng số trang

    // Lấy danh sách khách hàng và tính tổng số trang
    $scope.fetchKhachHang = function() {
        $http.get('/admin/khach-hang/api')
            .then(function(response) {
                $scope.khachhangList = response.data;
                $scope.totalPages = Math.ceil($scope.khachhangList.length / $scope.pageSize);
            }, function(error) {
                console.error("Lỗi khi lấy dữ liệu khách hàng:", error);
            });
    };

    // Hàm để chuyển đến trang trước
    $scope.previousPage = function() {
        if ($scope.currentPage > 1) {
            $scope.currentPage--;
        }
    };

    // Hàm để chuyển đến trang kế tiếp
    $scope.nextPage = function() {
        if ($scope.currentPage < $scope.totalPages) {
            $scope.currentPage++;
        }
    };

    // Hàm để thay đổi trang khi người dùng chọn trang
    $scope.setPage = function(page) {
        if (page >= 1 && page <= $scope.totalPages) {
            $scope.currentPage = page;
        }
    };

    // Hàm khởi tạo để lấy dữ liệu ban đầu
    $scope.fetchKhachHang();

});
