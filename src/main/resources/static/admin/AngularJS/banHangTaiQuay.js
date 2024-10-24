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

    $scope.khachHangById={};
    $scope.chiTietDonHang = []
    $scope.donHang = [];
    $scope.products = [];
    $scope.productDetails = [];
    $scope.khachHang =[];

    var selectedId = null;
    $scope.selectedId = null;
    $scope.khachThanhToan = 0;


    //lấy don hàng chi tiết khi click đơn hàng
    $scope.selectOrder = function(id) {
        $scope.selectedId = id;
        selectedId = id;
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

    $scope.getKhachHang = function (){
        $http.get("/don-hang/get-khach-hang").then(function (response) {
            $scope.khachHang = response.data;
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
        })
    }

    $scope.getKhachHangByID = function (id){
        $http.get("/don-hang/get-khach-hang/" +id).then(function (response) {
            $scope.khachHangById = response.data;
            $('#nameKH').val(response.data.hoTen);
            $('#sdtKH').val(response.data.soDienThoai);
            $('#show-modal-khach').modal('hide');
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
        })
    }

    $scope.addDonHang = function() {
        console.log("$scope.khachHangById: ",$scope.khachHangById);
        // var donHangData = angular.copy($scope.donHangAdd);
        // $http({
        //     method: 'POST',
        //     url: '/don-hang/them-moi',
        //     data: donHangData,
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     transformRequest: function(data) {
        //         return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
        //     }
        // }) .then(function(response) {
        //         console.log('Đơn hàng đã được thêm:', response.data);
        //         $scope.getDonHang();
        // }).catch(function(error) {
        //         console.error('Có lỗi xảy ra:', error);
        // });
    };

    //thêm/cập nhật sản phẩm vào đơn hàng chi tiết
    $scope.addProductsDetail = function (idSanPhamChiTiet){
        $scope.dataProduct ={
            maDonHangChiTiet: $scope.generateRandomString(8),
            idĐonHang: selectedId,
            idSanPhamChiTiet: idSanPhamChiTiet,
            soLuong: '1'
        }
        var dataProductsDetails = angular.copy($scope.dataProduct);
        if ($scope.dataProduct.idDonHang === null) {
            console.log("idDonHang is null, action blocked.");
            alert("Bạn Chưa Chọn Đơn Hàng!");
            return;  // Chặn không cho thực hiện nếu idDonHang là null
        }
        var name = $('#nameKH').val();
        var sdt = $('#sdtKH').val();
        if(name === "" || sdt === "") {
            alert("Bạn Chưa Chọn Khách Hàng!");
            return;
        }

        //kiểm tra đã tồn tại sản phẩm chưa
        var productWithId = $scope.productDetails.find(item => item.idSanPham === idSanPhamChiTiet);
        if(productWithId){
            //console.log("productWithId",productWithId);
            $http({
                method: 'PUT',
                url: '/don-hang/don-hang-chi-tiet/cap-nhat',
                data: dataProductsDetails,
                headers: {
                    'Content-Type': 'application/json'
                },
                transformRequest: function(data) {
                    return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
                }
            }).then(function(response) {
                //console.log('Sản phẩm thêm thành công');
                console.log('Sản phẩm thêm: ',response.data);
                productWithId.soLuong++;
                //$scope.getProducts();
                var productInScope = $scope.products.find(item => item.idSanPhamChiTiet === idSanPhamChiTiet);
                if (productInScope && productInScope.soLuong > 1) {
                    productInScope.soLuong--;
                }
            }).catch(function(error) {
                    console.error('Có lỗi xảy ra:', error);
            });
        }else {
            //console.log("check sản phẩm chưa tồn tạo ");
            //thêm sản phẩm vào giỏ hàng
            $http({
                method: 'POST',
                url: '/don-hang/don-hang-chi-tiet/them-moi',
                data: dataProductsDetails,
                headers: {
                    'Content-Type': 'application/json'
                },
                transformRequest: function(data) {
                    return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
                }
            }) .then(function(response) {
                console.log('Sp dơn hàng chi tiết đã được thêm:', response.data);
                $scope.productDetails.push(response.data);
                var productInScope = $scope.products.find(item => item.idSanPhamChiTiet === idSanPhamChiTiet);
                if (productInScope && productInScope.soLuong > 1) {
                    productInScope.soLuong--;
                }
            }).catch(function(error) {
                console.error('Có lỗi xảy ra:', error);
            });
        }
        console.log("check Data spct",$scope.dataProduct);
    }

    //tạo hoá đơn
    $scope.createHoaDon = function (){
        var nameKH = $('#nameKH').val();
        var tongTien = $('.total-amount').text();
        var phieuGiamGia = $('#discountSelect').val();
        //var khachThanhToan = $('#exampleFormControlInput1').val();
        var phuongThucThanhToan = $('#paymentMethodSelect').val();
        var ghiChu = $('textarea').val();

        $scope.hoaDonData ={
            maHoaDon: $scope.generateRandomString(8),
            idKhuyenMai: phieuGiamGia,
            idTrangThai: 2,//trạng thái của hoá đơn hoàn thành
            idPhuongThucThanhToan: phuongThucThanhToan,
            idDonHang: $scope.selectedId,
            idKhachHang: $scope.khachHangById.idKhachHang,
            tenKhachHang: nameKH,
            tongTien: tongTien,
            tongTienKhuyenMai: $scope.getTienGiam(),
            tongTienSauKhuyenMai: $scope.getTienKhachPTra(),
            ghiChu: ghiChu,
        }

        var hoaDonData = angular.copy($scope.hoaDonData);

        $http({
            method: 'POST',
            url: '/hoa-don/them-moi',
            data: hoaDonData,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }) .then(function(response) {
                console.log('Hoá Đơn DATA:', response.data);
                //$scope.getDonHang();
        }).catch(function(error) {
                console.error('Có lỗi xảy ra Hoá Đơn DATA:', error);
        });
    }

    //xoá đơn hàng
    $scope.deleteDonHang = function (id){
        if(confirm("Bạn có muôna xoá đơn hàng?")){
            $http.delete('/don-hang/don-hang/xoa/'+id).then(function (response){
                console.log('Đã xoá thành công đơn hang: ',response);
                //$scope.selectOrder(selectedId);
                $scope.getDonHang();
                $scope.productDetails =[];
            }).catch(function (errors){
                console.error('Có lỗi xảy ra:', errors);
            })

        }
    }

    $scope.deleteDonHangChiTiet = function (id){
        // alert("Show: "+id);
        if(confirm("Bạn Có muốn Xoá sản phẩm khỏi dơn hàng không?")){
            $http.delete('/don-hang/don-hang-chi-tiet/xoa/' + id).then(function (response){
                console.log('Đã xóa đơn hàng chi tiết thành công:', response);
                $scope.selectOrder(selectedId);
                var productInScope = $scope.products.find(item => item.idSanPhamChiTiet === id);
                productInScope.soLuong++;
                // if (productInScope && productInScope.soLuong > 1) {
                //     productInScope.soLuong++;
                // }
            }).catch(function (errors){
                console.error('Có lỗi xảy ra:', errors);
            })
        }
    }


    //show-modal-khach
    $scope.openModal = function() {
        $('#show-modal-khach').modal('show');
    };

    //tang so luong
    $scope.soLuongPlus = function (details){
        details.soLuong +=1;
        console.log("Số Lượng plus: ",details.soLuong);
        console.log("Số Lượng plus: ",details.soLuong * details.donGia);
    }
    //Giảm Số Lượng
    $scope.soLuongReduce = function(details){
        if(details.soLuong >1){
            details.soLuong -=1;
            console.log("Số Lượng plus: ",details.soLuong);
            console.log("Số Lượng plus: ",details.soLuong * details.donGia);
        }
    }
    //lưu só lượng ban đầu
    $scope.saveOriginalQuantity = function (details){
        details.saveOriginalQuantity = details.soLuong;
    }
    //kiểm tra số lượng được nhập
    $scope.checkQuantity = function (details){
        if (details.soLuong > 100) {
            alert("Số lượng không được lớn hơn 100.");
            details.soLuong = details.originalQuantity;
        } else {
            details.originalQuantity = details.soLuong;
        }
    }

    //tính tổng tiền tất cả sản phẩm
    $scope.getSum = function (){
        if (!$scope.productDetails || $scope.productDetails.length === 0) {
            return 0;
        }
        let sumMoney =0;
        $scope.productDetails.forEach(function (details){
            sumMoney += details.soLuong * details.donGia;
        });
        return sumMoney;
    }
    $scope.getTienGiam = function (){
        $scope.discountRate = "10%";
        let tienGiam = parseFloat($scope.discountRate.replace('%',''))/100;
        let tongTienGiam = $scope.getSum() * tienGiam;
        return tongTienGiam;
    }
    $scope.getTienKhachPTra = function (){
        let khachPhaiTra = $scope.getSum() - $scope.getTienGiam();
        return khachPhaiTra;
    }
    //tính tiền thừa
    $scope.tinhTienThua = function () {
        if($scope.khachThanhToan ===0){
            return 0;
        }
        let tongTien = $scope.getTienKhachPTra();
        let khachThanhToan = parseFloat($scope.khachThanhToan) || 0; // Đảm bảo giá trị là số
        return khachThanhToan - tongTien;
    };


    //load data product when run
    $scope.getProducts();
    $scope.getDonHang();
    $scope.getKhachHang();
})