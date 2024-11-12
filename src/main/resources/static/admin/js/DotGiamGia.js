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
                const productDetails = response.data.content;  // Lấy tất cả chi tiết sản phẩm

                // Thêm từng chi tiết sản phẩm vào selectedProducts nếu chưa có
                productDetails.forEach(productDetail => {
                    const existingProduct = $scope.selectedProducts.some(p => p.idSanPham === productDetail.idSanPham);
                    if (!existingProduct) {
                        $scope.selectedProducts.push(angular.copy(productDetail));
                        console.log("Đã thêm sản phẩm chi tiết:", productDetail.idSanPham);
                    }
                });
            } else {
                console.log("Không có chi tiết sản phẩm nào được tìm thấy.");
            }
        }).catch(error => {
            console.log("Lỗi khi lấy chi tiết sản phẩm: ", error);
        });
    };



    $scope.toggleSelection = function (product) {
        if (product.selected) {
            // Khi chọn sản phẩm, gọi chi tiết sản phẩm
            if (product.idSanPham) {
                console.log("Gọi chi tiết sản phẩm với ID:", product.idSanPham);
                $scope.getProductDetail(product.idSanPham);
            }
        } else {
            // Khi bỏ chọn sản phẩm, xóa chi tiết sản phẩm khỏi selectedProducts
            const removedProduct = $scope.selectedProducts.find(p => p.idSanPham === product.idSanPham);

            // Nếu tìm thấy sản phẩm chi tiết trong selectedProducts thì xóa nó
            if (removedProduct) {
                $scope.selectedProducts = $scope.selectedProducts.filter(p => p.idSanPham !== product.idSanPham);
                console.log("Đã bỏ chọn và xóa chi tiết sản phẩm ID:", product.idSanPham);
            }

            // Cập nhật lại trạng thái của sản phẩm trong items
            const itemToUpdate = $scope.items.find(item => item.idSanPham === product.idSanPham);
            if (itemToUpdate) {
                itemToUpdate.selected = false;
            }

            // Cập nhật giao diện
            $scope.$apply();  // Đảm bảo đồng bộ hóa giao diện
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




    $scope.selectedProductIds = []; // Mảng để lưu các id chi tiết sản phẩm đã chọn

// Cập nhật mảng các ID chi tiết sản phẩm khi checkbox thay đổi
    $scope.updateSelectedProducts = function(product) {
        if (product.selected) {
            // Thêm ID vào mảng nếu checkbox được chọn
            $scope.selectedProductIds.push(product.idSanPhamChiTiet);
        } else {
            // Loại bỏ ID khỏi mảng nếu checkbox bị bỏ chọn
            const index = $scope.selectedProductIds.indexOf(product.idSanPhamChiTiet);
            if (index > -1) {
                $scope.selectedProductIds.splice(index, 1);
            }
        }

        // In mảng các ID chi tiết sản phẩm ra console để kiểm tra
        console.log("Danh sách ID chi tiết sản phẩm đã chọn: ", $scope.selectedProductIds);
    };

    $scope.toggleSelectAll2 = function(selectAll2) {
        // Nếu chọn tất cả, đánh dấu tất cả checkbox là true
        angular.forEach($scope.selectedProducts, function(product) {
            product.selected = selectAll2; // Cập nhật trạng thái chọn/tích cho mỗi sản phẩm
        });
    };

    $scope.updateSelectedProducts = function(product) {
        // Cập nhật trạng thái của sản phẩm khi checkbox được thay đổi
        // Thực hiện các thao tác bạn muốn với các sản phẩm đã chọn, ví dụ lưu vào mảng hoặc gọi API
        console.log("Cập nhật sản phẩm:", product);
    };








});
