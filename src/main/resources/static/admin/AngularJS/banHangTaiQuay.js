var app = angular.module("banhang-app", [])
app.controller("banhang-ctrl", function ($scope, $http) {
    //tao random ma don hang
    $scope.generateRandomString = function(length) {
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
        let result = '';

        for (let i = 0; i < length; i++) {
            const randomIndex = Math.floor(Math.random() * characters.length);
            result += characters[randomIndex];
        }

        return result;
    };


    //$scope.donHang = {}
    $scope.donHangAdd = {
        maDonHang: $scope.generateRandomString(8),
        idTrangThai : 1,
        tongTien: 1,
        tongTienKhuyenMai: 1,
        tongTienSauKhuyenMai: 1,
        ghiChu: "khong",
        trangThaiThanhToan: false,
        idNhanVien: 1,
        idKhachHang: 1,
        idPhuongThucThanhToan: 1,
        idKhuyenMai: 1
    };

    $scope.chiTietDonHang = []
    $scope.donHang = [];
    $scope.products = [];
    $scope.productDetails = [];

    $scope.selectedId = null;
    //lấy don hàng chi tiết khi click đơn hàng
    $scope.selectOrder = function(id) {
        $scope.selectedId = id;
        console.log('Selected Order ID:', id);
        $http.get(`/don-hang/don-hang-chi-tiet/${$scope.selectedId}`).then(function (response){
            console.log("check don hàng chi tiết: ",response.data);
            $scope.productDetails = response.data;
        }).catch(function (err){
            console.log("err: ", err);
        })
    };

    //lấy sản phẩm
    $scope.getProducts = function (){
        $http.get("/don-hang/san-pham-chi-tiet").then(function (response){
            console.log("check log: ",response)
            $scope.products = response.data;
        }).catch(function (errors){
            console.log(errors)
        });
    }

    //lấy đợn hàng chờ xử lý
    $scope.getDonHang = function (){
        $http.get("/don-hang/get-don-hang").then(function (response){
            console.log("check log: ",response)
            $scope.donHang = response.data;
            console.log("check log donhang: ",$scope.donHang)
        }).catch(function (errors){
            console.log(errors)
        });
    }

    $scope.addDonHang = function() {
        var donHangData = angular.copy($scope.donHangAdd);

        $http({
            method: 'POST',
            url: '/don-hang/them-moi',
            data: donHangData,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }) .then(function(response) {
                console.log('Đơn hàng đã được thêm:', response.data);
                $scope.getDonHang();
        }).catch(function(error) {
                console.error('Có lỗi xảy ra:', error);
        });
    };

    //thêm sản phẩm vào đơn hàng chi tiết
    $scope.addProductsDetail = function (idSanPhamChiTiet){
        $scope.dataProduct ={
            maDonHangChiTiet: $scope.generateRandomString(8),
            idDonHang: $scope.selectedId,
            idSanPhamChiTiet: idSanPhamChiTiet,
            soLuong: '1'
        }
        var dataProductsDetails = angular.copy($scope.dataProduct);
        //kiểm tra đã tồn tại sản phẩm chưa
        var productWithId = $scope.productDetails.find(item => item.idSanPham === idSanPhamChiTiet);
        if(productWithId){
            $http({
                method: 'PUT',
                url: '/don-hang/them-moi',
                data: dataProductsDetails,
                headers: {
                    'Content-Type': 'application/json'
                },
                transformRequest: function(data) {
                    return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
                }
            }).then(function(response) {
                console.log('Sản phẩm thêm thành công');
                $scope.getDonHang();
            }).catch(function(error) {
                    console.error('Có lỗi xảy ra:', error);
            });
        }else {
            console.log("check sản phẩm chưa tồn tạo ");
        }
        console.log("check Data spct",$scope.dataProduct);
    }

    //show-modal-khach
    $scope.openModal = function() {
        $('#show-modal-khach').modal('show');
    };

    //load data product when run
    $scope.getProducts();
    $scope.getDonHang();

})