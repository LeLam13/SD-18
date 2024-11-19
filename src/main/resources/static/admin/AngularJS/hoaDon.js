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
        $http.get("/admin/hoa-don/search?maHoaDon=" + $scope.searchMaHoaDon).then(
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
        $scope.invoiceData = {
            idHoaDon: idHoaDon,
            customerName: "",
            companyName: "",
            taxCode: "",
            address: "",
            paymentMethod: "Tiền mặt",
        };
        $("#exportInvoiceModal").modal("show");
    };

    $scope.confirmExport = function () {
        const invoiceData = $scope.invoiceData;

        // Gửi API để tạo file PDF hóa đơn
        $http
            .post(`/admin/hoa-don/export`, invoiceData, {
                responseType: "arraybuffer",
            })
            .then(
                function (response) {
                    const blob = new Blob([response.data], { type: "application/pdf" });
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement("a");
                    a.href = url;
                    a.download = `Invoice_${invoiceData.idHoaDon}.pdf`;
                    a.click();
                    window.URL.revokeObjectURL(url);
                    $("#exportInvoiceModal").modal("hide");
                    alert("Xuất hóa đơn thành công!");
                },
                function (error) {
                    console.error("Error exporting invoice:", error);
                    alert("Đã xảy ra lỗi khi xuất hóa đơn!");
                }
            );
    };

    $scope.closeModal = function () {
        $("#detailsModal").modal("hide"); // Đóng modal
    };

    // Call the function to fetch Hoa Dons on load
    $scope.getHoaDons();
});
