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
    // Biến lỗi
    $scope.errors = {};

    // 1. Xác thực dữ liệu trước khi gọi API
    let isValid = true;

    // Kiểm tra rỗng và độ dài của mã khách hàng
    if (!$scope.newKhachHang.ma_khach_hang || $scope.newKhachHang.ma_khach_hang.trim() === '') {
      $scope.errors.ma_khach_hang = "Mã khách hàng không được để trống.";
      isValid = false;
    } else if ($scope.newKhachHang.ma_khach_hang.length < 3 || $scope.newKhachHang.ma_khach_hang.length > 10) {
      $scope.errors.ma_khach_hang = "Mã khách hàng phải từ 3 đến 10 ký tự.";
      isValid = false;
    }

    // Kiểm tra rỗng và định dạng họ tên
    if (!$scope.newKhachHang.ho_ten || $scope.newKhachHang.ho_ten.trim() === '') {
      $scope.errors.ho_ten = "Họ tên không được để trống.";
      isValid = false;
    }

    if (!$scope.newKhachHang.ngay_sinh) {
      $scope.errors.ngay_sinh = "Ngày sinh không được để trống.";
      isValid = false;
    } else {
      let birthDate;

      if (typeof $scope.newKhachHang.ngay_sinh === 'string') {
        // Nếu ngay_sinh là chuỗi định dạng MM/dd/yyyy
        const parts = $scope.newKhachHang.ngay_sinh.split('/');
        if (parts.length !== 3) {
          $scope.errors.ngay_sinh = "Định dạng ngày sinh không hợp lệ (MM/dd/yyyy).";
          isValid = false;
        } else {
          const month = parseInt(parts[0], 10) - 1; // Tháng trong JavaScript bắt đầu từ 0
          const day = parseInt(parts[1], 10);
          const year = parseInt(parts[2], 10);
          birthDate = new Date(year, month, day);
        }
      } else if ($scope.newKhachHang.ngay_sinh instanceof Date) {
        // Nếu ngay_sinh là đối tượng Date
        birthDate = $scope.newKhachHang.ngay_sinh;
      } else {
        $scope.errors.ngay_sinh = "Ngày sinh không hợp lệ.";
        isValid = false;
      }

      if (birthDate) {
        const currentDate = new Date();
        if (isNaN(birthDate.getTime())) {
          $scope.errors.ngay_sinh = "Ngày sinh không hợp lệ.";
          isValid = false;
        } else if (birthDate > currentDate) {
          $scope.errors.ngay_sinh = "Ngày sinh không được lớn hơn ngày hiện tại.";
          isValid = false;
        } else {
          // Chuyển ngày sinh về định dạng yyyy-MM-dd
          const formattedDate = `${birthDate.getFullYear()}-${('0' + (birthDate.getMonth() + 1)).slice(-2)}-${('0' + birthDate.getDate()).slice(-2)}`;
          $scope.newKhachHang.ngay_sinh = formattedDate;
        }
      }
    }


    // Kiểm tra định dạng số điện thoại
    const phonePattern = /^(\+84|0)\d{9,10}$/;
    if (!$scope.newKhachHang.so_dien_thoai || $scope.newKhachHang.so_dien_thoai.trim() === '') {
      $scope.errors.so_dien_thoai = "Số điện thoại không được để trống.";
      isValid = false;
    }
    if (!phonePattern.test($scope.newKhachHang.so_dien_thoai)) {
      $scope.errors.so_dien_thoai = "Số điện thoại không đúng định dạng (VD: +84123456789 hoặc 0123456789).";
      isValid = false;
    }

    // Kiểm tra định dạng email
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if (!$scope.newKhachHang.email || $scope.newKhachHang.email.trim() === '') {
      $scope.errors.email = "Email không được để trống.";
      isValid = false;
    } else if (!emailPattern.test($scope.newKhachHang.email)) {
      $scope.errors.email = "Email không đúng định dạng.";
      isValid = false;
    }

    // Kiểm tra giới tính
    if ($scope.newKhachHang.gioi_tinh === undefined || $scope.newKhachHang.gioi_tinh === '') {
      $scope.errors.gioi_tinh = "Vui lòng chọn giới tính.";
      isValid = false;
    }

    // Kiểm tra rỗng địa chỉ
    if (!$scope.newKhachHang.dia_chi || $scope.newKhachHang.dia_chi.trim() === '') {
      $scope.errors.dia_chi = "Địa chỉ không được để trống.";
      isValid = false;
    }

    // Kiểm tra tên tài khoản
    if (!$scope.newKhachHang.username_tai_khoan || $scope.newKhachHang.username_tai_khoan.trim() === '') {
      $scope.errors.username_tai_khoan = "Tên tài khoản không được để trống.";
      isValid = false;
    }

    // Nếu có lỗi, ngừng thực hiện API và hiển thị lỗi
    if (!isValid) {
      console.warn("Có lỗi xác thực, không gửi yêu cầu API:", $scope.errors);
      return;
    }

    // 3. Gửi dữ liệu đến API
    $http.post("/admin/khach-hang/api/add", $scope.newKhachHang).then(
      function (response) {
        console.log("Customer added successfully:", response.data);
        $scope.khachhangList.push(response.data);
        $scope.paginate(); // Làm mới phân trang nếu có
        $scope.closeAddForm(); // Đóng biểu mẫu
      },
      function (error) {
        console.error("Error adding customer:", error);
        $scope.errors.api_error = "Có lỗi xảy ra trong quá trình thêm khách hàng.";
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
    // Biến lỗi
    $scope.errors = {};

    // 1. Xác thực dữ liệu trước khi gọi API
    let isValid = true;

    // Kiểm tra rỗng và độ dài của mã khách hàng
    if (!$scope.selectedKhachHang.ma_khach_hang || $scope.selectedKhachHang.ma_khach_hang.trim() === '') {
      $scope.errors.ma_khach_hang = "Mã khách hàng không được để trống.";
      isValid = false;
    } else if ($scope.selectedKhachHang.ma_khach_hang.length < 3 || $scope.selectedKhachHang.ma_khach_hang.length > 10) {
      $scope.errors.ma_khach_hang = "Mã khách hàng phải từ 3 đến 10 ký tự.";
      isValid = false;
    }

    // Kiểm tra rỗng và định dạng họ tên
    if (!$scope.selectedKhachHang.ho_ten || $scope.selectedKhachHang.ho_ten.trim() === '') {
      $scope.errors.ho_ten = "Họ tên không được để trống.";
      isValid = false;
    }

    // Kiểm tra ngày sinh
    if (!$scope.selectedKhachHang.ngay_sinh) {
      $scope.errors.ngay_sinh = "Ngày sinh không được để trống.";
      isValid = false;
    } else {
      let birthDate;
      if (typeof $scope.selectedKhachHang.ngay_sinh === 'string') {
        const parts = $scope.selectedKhachHang.ngay_sinh.split('/');
        if (parts.length !== 3) {
          $scope.errors.ngay_sinh = "Định dạng ngày sinh không hợp lệ (MM/dd/yyyy).";
          isValid = false;
        } else {
          const month = parseInt(parts[0], 10) - 1;
          const day = parseInt(parts[1], 10);
          const year = parseInt(parts[2], 10);
          birthDate = new Date(year, month, day);
        }
      } else if ($scope.selectedKhachHang.ngay_sinh instanceof Date) {
        birthDate = $scope.selectedKhachHang.ngay_sinh;
      } else {
        $scope.errors.ngay_sinh = "Ngày sinh không hợp lệ.";
        isValid = false;
      }

      if (birthDate) {
        const currentDate = new Date();
        if (isNaN(birthDate.getTime())) {
          $scope.errors.ngay_sinh = "Ngày sinh không hợp lệ.";
          isValid = false;
        } else if (birthDate > currentDate) {
          $scope.errors.ngay_sinh = "Ngày sinh không được lớn hơn ngày hiện tại.";
          isValid = false;
        } else {
          const formattedDate = `${birthDate.getFullYear()}-${('0' + (birthDate.getMonth() + 1)).slice(-2)}-${('0' + birthDate.getDate()).slice(-2)}`;
          $scope.selectedKhachHang.ngay_sinh = formattedDate;
        }
      }
    }

    // Kiểm tra định dạng số điện thoại
    const phonePattern = /^(\+84|0)\d{9,10}$/;
    if (!$scope.selectedKhachHang.so_dien_thoai || $scope.selectedKhachHang.so_dien_thoai.trim() === '') {
      $scope.errors.so_dien_thoai = "Số điện thoại không được để trống.";
      isValid = false;
    } else if (!phonePattern.test($scope.selectedKhachHang.so_dien_thoai)) {
      $scope.errors.so_dien_thoai = "Số điện thoại không đúng định dạng (VD: +84123456789 hoặc 0123456789).";
      isValid = false;
    }

    // Kiểm tra định dạng email
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if (!$scope.selectedKhachHang.email || $scope.selectedKhachHang.email.trim() === '') {
      $scope.errors.email = "Email không được để trống.";
      isValid = false;
    } else if (!emailPattern.test($scope.selectedKhachHang.email)) {
      $scope.errors.email = "Email không đúng định dạng.";
      isValid = false;
    }

    // Kiểm tra rỗng địa chỉ
    if (!$scope.selectedKhachHang.dia_chi || $scope.selectedKhachHang.dia_chi.trim() === '') {
      $scope.errors.dia_chi = "Địa chỉ không được để trống.";
      isValid = false;
    }

    // Kiểm tra tên tài khoản
    if (!$scope.selectedKhachHang.username_tai_khoan || $scope.selectedKhachHang.username_tai_khoan.trim() === '') {
      $scope.errors.username_tai_khoan = "Tên tài khoản không được để trống.";
      isValid = false;
    }

    // Nếu có lỗi, ngừng thực hiện API và hiển thị lỗi
    if (!isValid) {
      console.warn("Có lỗi xác thực, không gửi yêu cầu API:", $scope.errors);
      return;
    }

    // 3. Gửi dữ liệu đến API
    $http.put(`/admin/khach-hang/api/update/${$scope.selectedKhachHang.id_khach_hang}`, $scope.selectedKhachHang)
      .then(
        function (response) {
          const index = $scope.khachhangList.findIndex(
            (kh) => kh.id_khach_hang === response.data.id_khach_hang
          );
          if (index !== -1) {
            $scope.khachhangList[index] = response.data;
            $scope.paginate();
          }
          $scope.closeEditForm();
          $scope.fetchKhachHang();
        },
        function (error) {
          console.error("Error updating customer:", error);
          $scope.errors.api_error = "Có lỗi xảy ra trong quá trình cập nhật khách hàng.";
        }
      );
  };


  // Initialize data fetch
  $scope.fetchKhachHang();
});
