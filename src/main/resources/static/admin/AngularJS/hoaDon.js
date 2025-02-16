var app = angular.module("hoaDon-app", []);
app.controller("hoaDon-ctrl", function ($scope, $http) {
    $scope.hoaDons = []; // Array to hold Hoa Don data

    // Function to get Hoa Don data from the API
    $scope.getHoaDons = function (page = 0) {
        $http.get("/admin/hoa-don/api?page=" + page).then(
            function (response) {
                $scope.hoaDons = response.data.content; // Lấy danh sách hoaDons
                $scope.currentPage = response.data.number; // Lưu trang hiện tại
                $scope.totalPages = response.data.totalPages; // Tổng số trang
            },
            function (error) {
                console.error("Error fetching Hoa Dons:", error);
            }
        );
    };
    $scope.searchHoaDon = function () {
        const url = `/admin/hoa-don/search-filter?keyword=${
            $scope.searchMaHoaDon || ""
        }&filterLoaiDonHang=${$scope.filterLoaiDonHang || ""}&filterTrangThaiThanhToan=${
            $scope.filterTrangThaiThanhToan || ""
        }`;

        $http.get(url).then(
            function (response) {
                $scope.hoaDons = response.data; // Cập nhật danh sách hóa đơn với kết quả tìm kiếm
            },
            function (error) {
                console.error("Error searching Hoa Dons:", error);
            }
        );
    };

    $scope.viewDetails = function (idHoaDon) {
        $http.get("/admin/hoa-don/detail/" + idHoaDon).then(
            function (response) {
                $scope.detail = response.data;
                console.log(response.data);
                $("#detailsModal").modal("show");
            },
            function (error) {
                console.error("Error fetching detail:", error);
            }
        );
    };
    $scope.invoiceData = {};

    $scope.exportHoaDon = function (idHoaDon) {
        // Gửi request đến API export hóa đơn
        $http({
            method: "POST",
            url: "/admin/hoa-don/export",
            params: { idHoaDon: idHoaDon }, // Gửi idHoaDon qua query params
            responseType: "arraybuffer", // Đảm bảo nhận dữ liệu nhị phân (PDF)
        })
            .then(function (response) {
                // Tạo Blob từ response data
                const blob = new Blob([response.data], { type: "application/pdf" });

                // Tạo URL từ Blob
                const url = window.URL.createObjectURL(blob);

                // Tạo thẻ a để tải file
                const a = document.createElement("a");
                a.href = url;
                a.download = `HoaDon_${idHoaDon}.pdf`; // Tên file PDF
                document.body.appendChild(a);
                a.click();

                // Xóa URL sau khi tải
                window.URL.revokeObjectURL(url);
                document.body.removeChild(a);
            })
            .catch(function (error) {
                console.error("Lỗi khi xuất hóa đơn:", error);
                alert("Không thể xuất hóa đơn. Vui lòng thử lại sau.");
            });
    };

    $scope.closeModal = function () {
        $("#detailsModal").modal("hide"); // Đóng modal
    };

    // Call the function to fetch Hoa Dons on load
    $scope.getHoaDons();
});
