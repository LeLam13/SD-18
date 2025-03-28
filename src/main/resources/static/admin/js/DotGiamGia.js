var app = angular.module('dot-giam-gia-admin', []);
app.controller('ctrl', function ($scope, $http) {
    $scope.selectedProductId = 0;
    $scope.items = [];
    $scope.selectedProducts = []; // Mảng chứa các sản phẩm đã chọn
    $scope.page = 0;
    $scope.size = 4;
    $scope.totalPages = 0;
    $scope.selectedProductIds = []; // Mảng để lưu các id chi tiết sản phẩm đã chọn

    // Hàm tải danh sách sản phẩm
    $scope.findAll = function () {
        const discountId = getDiscountIdFromUrl(); // Hàm lấy ID đợt giảm giá từ URL

        const url = `/admin/san-pham/find-all-not-in/${discountId}?page=${$scope.page}&size=${$scope.size}`;
        $http.get(url).then(resp => {
            $scope.items = resp.data.content;
            $scope.totalPages = resp.data.totalPages;

            $scope.syncCheckboxState();
            $scope.selectAll = $scope.items.every(item => item.selected);

        }).catch(error => console.error('Lỗi khi tải dữ liệu:', error));
    };

    $scope.syncCheckboxState = function () {
        $scope.items.forEach(item => {
            item.selected = $scope.selectedProducts.some(p => p.idSanPham.idSanPham === item.idSanPham);
        });
    };

    $scope.discountProducts = [];

    $scope.loadDiscountProducts = function () {
        const discountId = getDiscountIdFromUrl(); // Hàm lấy ID đợt giảm giá từ URL
        if (!discountId) {
            alert('Không tìm thấy ID đợt giảm giá!');
            return;
        }

        $http.get(`/admin/san-pham/dot-giam-gia/${discountId}`)
            .then(response => {
                $scope.discountProducts = response.data;
                console.log("Danh sách sản phẩm trong đợt giảm giá:", $scope.discountProducts);
            })
            .catch(error => {
                console.error("Lỗi khi tải sản phẩm trong đợt giảm giá:", error);
            });
    };

    $scope.removeFromDiscount = function (productId) {
        const discountId = getDiscountIdFromUrl(); // Hàm lấy ID đợt giảm giá từ URL
        if (!discountId) {
            alertify.error('Không tìm thấy ID đợt giảm giá!');
            return;
        }

        alertify.confirm(
            "Xác nhận xóa", // Tiêu đề
            "Bạn có chắc muốn xóa sản phẩm này khỏi đợt giảm giá?", // Nội dung
            function () {
                // Nếu người dùng chọn "Có"
                console.log(`/admin/delete/${productId}/dot-giam-gia/${discountId}`);
                $http.get(`/admin/delete/${productId}/dot-giam-gia/${discountId}`)
                    .then(() => {
                        alertify.success('Xóa sản phẩm khỏi đợt giảm giá thành công!');
                        $scope.loadDiscountProducts(); // Cập nhật danh sách sau khi xóa
                        $scope.findAll(); // Tải lại danh sách tổng quát
                    })
                    .catch(error => {
                        alertify.error("Lỗi khi xóa sản phẩm khỏi đợt giảm giá!");
                        console.error(error);
                    });
            },
            function () {
                // Nếu người dùng chọn "Không"
                alertify.warning("Hủy xóa sản phẩm khỏi đợt giảm giá.");
            }
        );
    };


// Hàm hiển thị danh sách chi tiết sản phẩm
    $scope.showProductDetails = function (productId) {
        $scope.selectedProductId = productId;
        const discountId = getDiscountIdFromUrl(); // Lấy ID đợt giảm giá
        if (!discountId) {
            alert('Không tìm thấy ID đợt giảm giá!');
            return;
        }

        $http.get(`/admin/san-pham/dot-giam-gia/${discountId}/chi-tiet/${productId}`)
            .then(response => {
                $scope.productDetails = response.data; // Lưu danh sách chi tiết sản phẩm
                $('#productDetailsModal').modal('show'); // Hiển thị modal
            })
            .catch(error => {
                console.error("Lỗi khi tải chi tiết sản phẩm:", error);
                alert("Không thể tải danh sách chi tiết sản phẩm!");
            });
    };

// Hàm xóa chi tiết sản phẩm
    $scope.removeProductDetail = function (productDetailId) {
        const discountId = getDiscountIdFromUrl(); // Lấy ID đợt giảm giá
        if (!discountId) {
            alertify.error('Không tìm thấy ID đợt giảm giá!');
            return;
        }

        alertify.confirm(
            "Xác nhận xóa", // Tiêu đề
            "Bạn có chắc muốn xóa chi tiết sản phẩm này khỏi đợt giảm giá?", // Nội dung
            function () {
                // Xử lý khi xác nhận "Có"
                $http.delete(`/admin/delete/chi-tiet/${productDetailId}/dot-giam-gia/${discountId}`)
                    .then(() => {
                        alertify.success("Xóa chi tiết sản phẩm thành công!");
                        // Tải lại danh sách chi tiết sản phẩm sau khi xóa
                        $scope.showProductDetails($scope.selectedProductId);
                        window.location.reload();
                    })
                    .catch(error => {
                        console.error("Lỗi khi xóa chi tiết sản phẩm:", error);
                        alertify.error("Không thể xóa chi tiết sản phẩm!");
                    });
            },
            function () {
                // Xử lý khi hủy "Không"
                alertify.warning("Hủy xóa chi tiết sản phẩm.");
            }
        );
    };





    // Lấy chi tiết sản phẩm và thêm vào mảng selectedProducts
    $scope.getProductDetail = function (productId) {
        const discountId = getDiscountIdFromUrl(); // Lấy ID đợt giảm giá
        if (!discountId) {
            alert('Không tìm thấy ID đợt giảm giá!');
            return;
        }

        if (!productId) {
            console.log("productId không hợp lệ:", productId);
            return;
        }
        $http.get(`/admin/san-pham/${productId}/find-all/${discountId}`).then(response => {
            const productDetails = response.data.content;

            // Thêm từng chi tiết sản phẩm nếu chưa tồn tại
            productDetails.forEach(productDetail => {
                const existingProduct = $scope.selectedProducts.some(p => p.idSanPhamChiTiet === productDetail.idSanPhamChiTiet);
                if (!existingProduct) {
                    $scope.selectedProducts.push(angular.copy(productDetail));
                }
            });

            console.log("Chi tiết sản phẩm đã tải:", productDetails);
        }).catch(error => {
            console.log("Lỗi khi lấy chi tiết sản phẩm:", error);
        });
    };


    // Thay đổi trạng thái chọn của một sản phẩm
    $scope.toggleSelection = function (product) {
        if (product.selected) {
            // Gọi chi tiết sản phẩm
            $scope.getProductDetail(product.idSanPham);
        } else {
            // Xóa chi tiết sản phẩm liên quan
            $scope.selectedProducts = $scope.selectedProducts.filter(p => p.idSanPham.idSanPham !== product.idSanPham);
            // Cập nhật trạng thái và giao diện
            // product.selected = false;
            // $scope.$apply(); // Buộc giao diện đồng bộ lại
        }
        $scope.selectAll = $scope.items.every(item => item.selected);
    };


    // Chọn tất cả sản phẩm từ tất cả các trang
    $scope.toggleSelectAll = function () {

        $scope.items.forEach(item => {
            item.selected = $scope.selectAll; // Cập nhật trạng thái của từng sản phẩm
            if ($scope.selectAll) {
                $scope.getProductDetail(item.idSanPham); // Lấy chi tiết sản phẩm nếu cần
            } else {
                $scope.items.forEach(item => {
                    item.selected = false;
                    $scope.selectedProducts = $scope.selectedProducts.filter(p => p.idSanPham.idSanPham !== item.idSanPham);

                });
            }
        });

    };


    // Chuyển trang trước
    $scope.previousPage = function () {
        if ($scope.page > 0) {
            $scope.page--;
            $scope.findAll();
        }
    };

    // Chuyển trang sau
    $scope.nextPage = function () {
        if ($scope.page < $scope.totalPages - 1) {
            $scope.page++;
            $scope.findAll();
        }
    };

    // Cập nhật danh sách ID chi tiết sản phẩm khi trạng thái checkbox thay đổi
    $scope.updateSelectedProducts = function (product) {
        if (product.selected) {
            $scope.selectedProductIds.push(product.idSanPhamChiTiet);
        } else {
            $scope.selectedProductIds = $scope.selectedProductIds.filter(id => id !== product.idSanPhamChiTiet);
        }
        console.log("Danh sách ID chi tiết sản phẩm đã chọn:", $scope.selectedProductIds);
    };

    // Chọn hoặc bỏ chọn tất cả chi tiết sản phẩm
    $scope.toggleSelectAll2 = function (selectAll2) {
        $scope.selectedProducts.forEach(product => {
            product.selected = selectAll2;
        });
    };

    $scope.removeSelectedProducts = function () {
        // Lọc danh sách chi tiết sản phẩm để chỉ giữ lại các sản phẩm chưa được chọn
        const removedProductIds = $scope.selectedProducts
            .filter(product => product.selected)
            .map(product => product.idSanPham.idSanPham);

        // Xóa các sản phẩm được chọn
        $scope.selectedProducts = $scope.selectedProducts.filter(product => !product.selected);

        // Cập nhật trạng thái của các sản phẩm trong items
        $scope.items.forEach(item => {
            if (removedProductIds.includes(item.idSanPham)) {
                const hasRemainingDetails = $scope.selectedProducts.some(p => p.idSanPham.idSanPham === item.idSanPham);
                item.selected = hasRemainingDetails; // Nếu không còn chi tiết nào, bỏ tích
            }
        });

        // Cập nhật trạng thái "Tích tất cả"
        $scope.selectAll2 = $scope.selectedProducts.every(product => product.selected);
        $scope.selectAll = $scope.items.every(item => item.selected);

        console.log("Danh sách sản phẩm sau khi xóa:", $scope.selectedProducts);
    };




    // Gọi API thêm sản phẩm vào đợt giảm giá
    $scope.addProductsToDiscount = function () {
        const discountId = getDiscountIdFromUrl();
        if (!discountId) return alert('Không tìm thấy ID đợt giảm giá!');

        const selectedIds = $scope.selectedProductIds;
        if (!selectedIds.length) return alert('Vui lòng chọn ít nhất một sản phẩm!');

        $http.post(`/admin/dot-giam-gia/${discountId}/add-san-pham`, selectedIds)
            .then(() => alert('Thêm sản phẩm thành công!'))
            .catch(err => alert(`Lỗi khi thêm sản phẩm: ${err.message}`));
    };

    // Lấy ID đợt giảm giá từ URL
    function getDiscountIdFromUrl() {
        const url = window.location.href;
        const match = url.match(/\/dot-giam-gia\/detail\/(\d+)/);
        return match ? match[1] : null;
    }


    // Khởi tạo
    $scope.findAll();
    $scope.loadDiscountProducts();
});