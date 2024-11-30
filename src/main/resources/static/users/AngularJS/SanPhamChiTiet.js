app.controller('ctsp-ctrl', function ($scope, $http) {
    $scope.items = [];
    $scope.page = 0;  // Trang hiện tại
    $scope.size = 4; // Số lượng bản ghi trên mỗi trang
    $scope.totalPages = 0; // Tổng số trang
    $scope.pageInput = 1; // Giá trị nhập từ ô input
    $scope.filterData = {};

    const pathName = window.location.pathname.split('/');
    var idSanPham = pathName[pathName.length - 1];

    console.log("check ID:idSanPham/ - ",idSanPham);

    // $scope.listChiTietProducts = function () {
    //     $http.get("/san-pham/chi-tiet/get-all-by/" + idSanPham).then(function (response) {
    //         $scope.items=response.data;
    //     }).catch(function (err) {
    //         console.error("Error fetching data:", err);
    //     });
    // };


    $scope.listChiTietProducts = function (idSanPham) {
        $http.get("/san-pham/chi-tiet/get-all-by/" + idSanPham).then(function (response) {
            // Lấy tất cả các chi tiết sản phẩm
            $scope.items = response.data;

            // Lọc danh sách màu sắc và kích cỡ duy nhất
            $scope.uniqueColors = [];
            $scope.uniqueSizes = [];
            const seenColors = new Set();
            const seenSizes = new Set();

            // Duyệt qua tất cả các chi tiết sản phẩm để lọc các màu sắc và kích cỡ duy nhất
            $scope.items.forEach(item => {
                // Lọc màu sắc duy nhất
                if (!seenColors.has(item.idMauSac.idMauSac)) {
                    $scope.uniqueColors.push(item.idMauSac);
                    seenColors.add(item.idMauSac.idMauSac);
                }

                // Lọc kích cỡ duy nhất
                if (!seenSizes.has(item.idKichCo.idKichCo)) {
                    $scope.uniqueSizes.push(item.idKichCo);
                    seenSizes.add(item.idKichCo.idKichCo);
                }
            });

            // Chọn màu sắc và kích cỡ mặc định
            $scope.selectedColor = $scope.uniqueColors[0]; // Chọn màu sắc đầu tiên

            // Lọc theo màu sắc và kích cỡ mặc định
            $scope.filterByColor($scope.selectedColor);
            console.log("$scope.filterData: --- /",$scope.filterData);
        }).catch(function (err) {
            console.error("Error fetching data:", err);
        });
    };

// Hàm lọc theo màu sắc
    $scope.filterByColor = function (selectedColor) {
        $scope.selectedColor = selectedColor; // Cập nhật màu sắc đã chọn

        // Lọc các sản phẩm chi tiết theo màu sắc
        const filteredItems = $scope.items.filter(item =>
            !selectedColor || item.idMauSac.idMauSac === selectedColor.idMauSac
        );

        // Cập nhật filteredItems
        $scope.filteredItems = filteredItems;

        // Lọc kích cỡ theo màu sắc đã chọn
        const seenSizes = new Set();
        $scope.filteredSizes = [];
        filteredItems.forEach(item => {
            if (!seenSizes.has(item.idKichCo.idKichCo)) {
                $scope.filteredSizes.push(item.idKichCo);
                seenSizes.add(item.idKichCo.idKichCo);
            }
        });

        // Chọn kích cỡ đầu tiên nếu có
        if ($scope.filteredSizes.length > 0) {
            $scope.selectedSize = $scope.filteredSizes[0]; // Chọn kích cỡ đầu tiên
            $scope.filterBySize($scope.selectedSize); // Lọc theo kích cỡ đầu tiên
        } else {
            // Nếu không có kích cỡ, hãy đặt lại giá và số lượng
            $scope.price = null;
            $scope.soLuong = null;
        }

        // Cập nhật giá và số lượng cho sản phẩm lọc đầu tiên
        if (filteredItems.length > 0) {
            const firstItem = filteredItems[0];
            $scope.price = firstItem.giaBan;
            $scope.soLuong = firstItem.soLuong;
        }
    };

    $scope.idSanPhamChiTiet1 = null;
// Hàm lọc theo kích cỡ
    $scope.filterBySize = function (size) {
        $scope.selectedSize = size; // Cập nhật kích cỡ đã chọn

        // Lọc sản phẩm chi tiết theo cả màu sắc và kích cỡ
        const filteredItem = $scope.items.find(item =>
            item.idMauSac.idMauSac === $scope.selectedColor.idMauSac &&
            item.idKichCo.idKichCo === size.idKichCo
        );

        if (filteredItem) {
            $scope.price = filteredItem.giaBan;
            $scope.soLuong = filteredItem.soLuong;
        }
        console.log("filteredItem size",filteredItem)
        $scope.idSanPhamChiTiet1 = filteredItem.idSanPhamChiTiet;
    };


    $scope.listChiTietProducts(idSanPham);
})