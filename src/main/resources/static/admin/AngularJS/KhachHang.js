var app = angular.module("khachhangApp", []);

app.controller("KhachHangController", function ($scope, $http, $filter) {
  // Khai báo các biến
  $scope.khachhangList = [];
  $scope.paginatedKhachHangList = []; // Danh sách khách hàng đã phân trang
  $scope.searchQuery = "";
  $scope.newKhachHang = {};
  $scope.selectedKhachHang = {}; // Dữ liệu cho khách hàng được chọn để xem hoặc sửa

  // Pagination variables
  $scope.currentPage = 1; // Trang hiện tại
  $scope.pageSize = 5; // Số khách hàng mỗi trang
  $scope.totalPages = 0; // Tổng số trang

  // Fetch all customers
  // Fetch all customers with pagination
  $scope.fetchKhachHang = function () {
    $http
        .get("/admin/khach-hang/api/page", {
          params: {
            page: $scope.currentPage - 1,
            size: $scope.pageSize,
            searchQuery: $scope.searchQuery, // Gửi từ khóa tìm kiếm
          },
        })
        .then(function (response) {
          const data = response.data;
          $scope.khachhangList = data.data;
          $scope.totalItems = data.totalItems;
          $scope.totalPages = data.totalPages;
          $scope.currentPage = data.currentPage + 1;
        })
        .catch(function (error) {
          console.error("Error fetching data:", error);
        });
  };

  // Kích hoạt tìm kiếm
  $scope.searchKhachHang = function () {
    $scope.currentPage = 1; // Đặt lại về trang đầu tiên khi tìm kiếm
    $scope.fetchKhachHang(); // Lấy danh sách khách hàng với từ khóa mới
  };

  // Pagination logic
  $scope.paginate = function () {
    $scope.fetchKhachHang();
  };

  $scope.previousPage = function () {
    if ($scope.currentPage > 1) {
      $scope.currentPage--;
      $scope.fetchKhachHang(); // Gọi lại API với trang mới
    }
  };

  $scope.nextPage = function () {
    if ($scope.currentPage < $scope.totalPages) {
      $scope.currentPage++;
      $scope.fetchKhachHang(); // Gọi lại API với trang mới
    }
  };

  $scope.setPage = function (page) {
    if (page >= 1 && page <= $scope.totalPages) {
      $scope.currentPage = page;
      $scope.fetchKhachHang();
    }
  };

  // Show Add Customer Form
  $scope.showAddForm = function () {
    $scope.newKhachHang = {}; // Reset form data
    $("#viewAdd").modal("show"); // Hiển thị modal thêm khách hàng
  };

  // Close Add Customer Form
  $scope.closeAddForm = function () {
    $("#viewAdd").modal("hide"); // Đóng modal thêm khách hàng
  };

  // Add new customer
  $scope.addKhachHang = function () {
    // Format ngay_sinh to yyyy-MM-dd
    if ($scope.newKhachHang.ngay_sinh) {
      $scope.newKhachHang.ngay_sinh = $filter("date")(
          $scope.newKhachHang.ngay_sinh,
          "yyyy-MM-dd"
      );
    }

    $http.post("/admin/khach-hang/api/add", $scope.newKhachHang).then(
        function (response) {
          console.log("Customer added successfully:", response.data);
          $scope.khachhangList.push(response.data);
          $scope.paginate(); // Refresh pagination if applicable
          $scope.closeAddForm(); // Close the modal form
        },
        function (error) {
          console.error("Error adding customer:", error);
        }
    );
  };
  // View purchase history of a customer
  $scope.viewPurchaseHistory = function (idKhachHang) {
    $http
        .get("/admin/khach-hang/api/purchase-history/" + idKhachHang)
        .then(function (response) {
          $scope.purchaseHistory = response.data; // Gán dữ liệu trả về vào biến purchaseHistory
          $("#viewPurchaseHistory").modal("show"); // Hiển thị modal lịch sử mua hàng
        })
        .catch(function (error) {
          console.error("Error fetching purchase history:", error);
          $scope.purchaseHistory = []; // Reset danh sách lịch sử mua hàng nếu có lỗi
          alert("Không thể lấy lịch sử mua hàng của khách hàng.");
        });
  };

  // Close purchase history modal
  $scope.closePurchaseHistory = function () {
    $("#viewPurchaseHistory").modal("hide");
  };

  // View customer details
  $scope.viewDetails = function (khach) {
    $scope.selectedKhachHang = khach; // Gán dữ liệu khách hàng vào selectedKhachHang
    $("#viewDetails").modal("show"); // Hiển thị modal chi tiết
  };

  $scope.closeViewDetails = function () {
    $("#viewDetails").modal("hide");
  };

  // Edit customer
  $scope.editCustomer = function (khach) {
    $scope.selectedKhachHang = angular.copy(khach); // Copy dữ liệu để sửa
    $("#viewEdit").modal("show"); // Hiển thị modal sửa
  };

  $scope.closeEditForm = function () {
    $("#viewEdit").modal("hide");
  };
  $scope.applyDiscountCode = function () {
    // Kiểm tra nếu mã giảm giá không được nhập
    if (!$scope.discountCode || $scope.discountCode.trim() === "") {
      $scope.discountError = "Vui lòng nhập mã giảm giá!";
      $scope.discountSuccess = null;
      $scope.tienGiam = 0; // Reset tiền giảm nếu không có mã
      return; // Dừng lại ở đây nếu không có mã giảm giá
    }

    // Tiến hành gọi API nếu có mã giảm giá
    $http
        .get("/api/khuyen-mai/kiem-tra", {
          params: { maKhuyenMai: $scope.discountCode },
        })
        .then(function (response) {
          const discountRate = parseFloat(response.data); // Tỉ lệ giảm giá từ server (ví dụ: 10%)
          $scope.tienGiam = $scope.getSum() * (discountRate / 100);
          $scope.discountSuccess = "Áp dụng mã giảm giá thành công!";
          $scope.discountError = null;
          $scope.formatMoney(); // Cập nhật số tiền sau khi áp dụng mã giảm giá
        })
        .catch(function (error) {
          $scope.discountError = error.data || "Mã giảm giá không hợp lệ!";
          $scope.discountSuccess = null;
          $scope.tienGiam = 0;
          $scope.formatMoney(); // Cập nhật lại tiền khi không có mã giảm giá
        });
  };
  // Update customer data
  $scope.updateKhachHang = function () {
    if ($scope.selectedKhachHang.ngay_sinh) {
      $scope.selectedKhachHang.ngay_sinh = $filter("date")(
          $scope.selectedKhachHang.ngay_sinh,
          "yyyy-MM-dd"
      );
    }
    $http
        .put(
            "/admin/khach-hang/api/update/" +
            $scope.selectedKhachHang.id_khach_hang,
            $scope.selectedKhachHang
        )
        .then(
            function (response) {
              const index = $scope.khachhangList.findIndex(
                  (kh) => kh.id_khach_hang === response.data.id_khach_hang
              );
              if (index !== -1) {
                $scope.khachhangList[index] = response.data; // Cập nhật danh sách khách hàng
                $scope.paginate(); // Cập nhật dữ liệu phân trang
              }
              $scope.closeEditForm(); // Đóng modal sửa
              $scope.fetchKhachHang();
            },
            function (error) {
              console.error("Error updating customer:", error);
            }
        );
  };

  // Initialize data fetch
  $scope.fetchKhachHang();
});
