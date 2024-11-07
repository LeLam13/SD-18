var app = angular.module('dot-giam-gia-admin', []);
app.controller('ctrl', function ($scope, $http) {
    $scope.items = [];
    $scope.selectedProducts = []; // Mảng chứa các sản phẩm đã chọn
    $scope.page = 0;
    $scope.size = 4;
    $scope.totalPages = 0;

    // Hàm tải dữ liệu
    $scope.findAll = function () {
        var url = `/admin/san-pham/find-all?page=${$scope.page}&size=${$scope.size}`;
        $http.get(url).then(resp => {
            $scope.items = resp.data.content;
            $scope.totalPages = resp.data.totalPages;
            // Cập nhật lại trạng thái chọn sản phẩm khi chuyển trang
            $scope.items.forEach(item => {
                item.selected = $scope.selectedProducts.some(p => p.idSanPham === item.idSanPham);
            });
        }).catch(error => {
            console.log(error);
        });
    };

    $scope.getProductDetail = function (productId) {
        if (!productId) {
            console.log("productId không hợp lệ:", productId);
            return;
        }

        $http.get(`/admin/san-pham/${productId}/find-all`).then(response => {
            console.log("Chi tiết sản phẩm:", response.data);

            if (response.data.content && response.data.content.length > 0) {
                response.data.content.forEach(productDetail => {
                    // Kiểm tra và thêm sản phẩm nếu chưa có trong danh sách
                    if (!$scope.selectedProducts.some(p => p.idSanPham === productDetail.idSanPham && p.idCTSP === productDetail.idCTSP)) {
                        $scope.selectedProducts.push(angular.copy(productDetail));
                        console.log("Đã thêm sản phẩm chi tiết:", productDetail.idSanPham, "với CTSP:", productDetail.idCTSP);
                    } else {
                        console.log("Sản phẩm chi tiết đã có trong selectedProducts:", productDetail.idSanPham, "với CTSP:", productDetail.idCTSP);
                    }
                });
            } else {
                console.log("Không có chi tiết sản phẩm nào được tìm thấy.");
            }
        }).catch(error => {
            console.log("Lỗi khi lấy chi tiết sản phẩm: ", error);
        });
    };


    // Hàm chọn hoặc bỏ chọn một sản phẩm
    $scope.toggleSelection = function (product) {
        if (product.selected) {
            if (product.idSanPham) {
                console.log("Gọi chi tiết sản phẩm với ID:", product.idSanPham);
                $scope.getProductDetail(product.idSanPham);
            }
        } else {
            // Xóa tất cả các bản sao sản phẩm khỏi selectedProducts khi bỏ chọn
            $scope.selectedProducts = $scope.selectedProducts.filter(p => p.idSanPham !== product.idSanPham);
            console.log("Đã bỏ chọn sản phẩm ID:", product.idSanPham);

            // Log danh sách ID sản phẩm hiện tại trong selectedProducts
            console.log("Danh sách ID sản phẩm còn lại trong selectedProducts:",
                $scope.selectedProducts.map(p => p.idSanPham));

            // Cập nhật lại trạng thái của sản phẩm trong items
            const itemToUpdate = $scope.items.find(item => item.idSanPham === product.idSanPham);
            if (itemToUpdate) {
                itemToUpdate.selected = false;
            }
        }
    };

    // Hàm chọn tất cả sản phẩm
    $scope.toggleSelectAll = function () {
        $scope.selectedProducts = []; // Reset mảng đã chọn
        angular.forEach($scope.items, function (item) {
            item.selected = $scope.selectAll; // Đánh dấu tất cả là đã chọn hoặc bỏ chọn
            if ($scope.selectAll) {
                $scope.getProductDetail(item.idSanPham);
            }
        });

        if (!$scope.selectAll) {
            $scope.selectedProducts = []; // Reset mảng đã chọn nếu bỏ chọn tất cả
        }
    };

    // Hàm chuyển tới trang trước
    $scope.previousPage = function () {
        if ($scope.page > 0) {
            $scope.page--;
            $scope.findAll();
        }
    };

    // Hàm chuyển tới trang sau
    $scope.nextPage = function () {
        if ($scope.page < $scope.totalPages - 1) {
            $scope.page++;
            $scope.findAll();
        }
    };

    // Lấy dữ liệu khi trang được tải
    $scope.findAll();
});
