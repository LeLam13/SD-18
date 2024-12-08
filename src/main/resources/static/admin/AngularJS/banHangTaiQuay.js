var app = angular.module("banhang-app", [])
app.controller("banhang-ctrl", function ($scope, $http,$sce,$timeout) {
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

    $scope.notification = {
        show: false,
        message: '',
        type: '',
        icon: ''
    };

    $scope.provinces = [];
    $scope.selectedProvince = null;
    //quận-huyện
    $scope.selectedDistricts = null;
    $scope.districts = [];
    //xã
    $scope.selectedWards = null;
    $scope.wards = [];

    //fee-shipping
    $scope.fromDistrictId = 3440;
    $scope.fromWardId = 13010;
    $scope.feeShipping =[];

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
        idKhuyenMai: 1,
        loaiDonHang: 3
    };

    $scope.khachHangById={};
    $scope.chiTietDonHang = []
    $scope.donHang = [];
    $scope.products = [];
    $scope.productDetails = [];
    $scope.khachHang =[];
    $scope.khuyenMai =[];
    $scope.khuyenMaiById={};

    var selectedId = null;
    var idHoaDoncheck = null;
    $scope.selectedId = null;
    $scope.khachThanhToan = 0;
    //hiển thị vận chuyển
    $scope.shippingMethod = '1';
    //phân trang
    $scope.currentPage = 1; // Trang hiện tại
    $scope.pageSize = 5;


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

        $http.get(`/don-hang/get-don-hang/${$scope.selectedId}`).then(function (response){
            console.log("check don hàng sau khi update kh: ",response.data);
            if (response.data.oldKhachHang) {
                $('#nameKH').val(response.data.oldKhachHang.hoTen || '');
                $('#sdtKH').val(response.data.oldKhachHang.soDienThoai || '');
                $('#nameKHNhan').val(response.data.oldKhachHang.hoTen || '');
                $('#sdtKHNhan').val(response.data.oldKhachHang.soDienThoai || '');
            } else {
                $('#nameKH').val('');
                $('#sdtKH').val('');
                $('#nameKHNhan').val('');
                $('#sdtKHNhan').val('');
            }
            $scope.khachHangById = response.data;
            // $scope.productDetails = response.data;
            //console.log("check don hàng kh: ",$scope.khachHangById);
        }).catch(function (err){
            console.log("err: ", err);
        })
    };

    //lấy sản phẩm
    $scope.getProducts = function (){
        $http.get("/don-hang/san-pham-chi-tiet").then(function (response){
            console.log("check log get products: ",response)
            $scope.products = response.data;
            $scope.totalPages = Math.ceil($scope.products.length / $scope.pageSize); // Tổng số trang
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

    //lấy all khách hàng
    $scope.getKhachHang = function (){
        $http.get("/don-hang/get-khach-hang").then(function (response) {
            $scope.khachHang = response.data;
            //console.log("check get All khach hàng: ",$scope.khachHang);
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
        })
    }

    //lấy khách hàng theo id
    $scope.getKhachHangByID = function (id){
        var idDH = $scope.selectedId;
        $http.get("/don-hang/get-khach-hang/" +id).then(function (response) {
            $scope.khachHangById = response.data;
            $('#nameKH').val(response.data.ho_ten);
            $('#sdtKH').val(response.data.so_dien_thoai);
            $('#nameKHNhan').val(response.data.ho_ten );
            $('#sdtKHNhan').val(response.data.so_dien_thoai);
            $('#show-modal-khach').modal('hide');
            //console.log("$scope.khachHangById: ",$scope.khachHangById);
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
        })

        $http.put("/don-hang/update-don-hang/" +idDH+"/"+ id ).then(function (response) {
            console.log("update DH: ",response);
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
        })
    }

    //tạo đon hàng
    $scope.addDonHang = function() {
        //console.log("$scope.khachHangById: ",$scope.khachHangById);
        if ($scope.donHang.length >= 8) {
            console.error('Đã có 8 đơn hàng, không thể thêm mới.');
            //alert('Đã đạt đến giới hạn 8 đơn hàng, không thể thêm đơn hàng mới.');
            return;  // Dừng function nếu đã có 8 item
        }
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
            $scope.showNotification('Tạo Đơn Hàng Thành Công!','success');
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
            $scope.showNotification('Tạo Đơn Hàng Thất Bại!','error');
        });
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

        if($scope.selectedId === null || $scope.selectedId ===''){
            alert("Chưa Chọn Đơn Hàng!");
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
        // var phieuGiamGia = $('#discountSelect').val();
        var phieuGiamGia = $scope.khuyenMaiById.idKhuyenMai;
        var khachThanhToan = $('#id-khach-thanh-toan').val();
        var phuongThucThanhToan = $('#paymentMethodSelect').val();
        var ghiChu = $('textarea').val();
        var tenKhachNhan = $('#nameKHNhan').val();
        var sdtKhachNhan = $('#sdtKHNhan').val();

        var selectedProvinceName = $scope.selectedProvince ? $scope.selectedProvince.ProvinceName : '';

        // Tìm quận/huyện đã chọn
        var selectedDistrict = $scope.districts.find(d => d.DistrictID == $scope.selectedDistricts); // Sử dụng == thay vì ===
        var selectedDistrictName = selectedDistrict ? selectedDistrict.DistrictName : '';

        // Tìm xã/phường đã chọn
        var selectedWard = $scope.wards.find(w => w.WardCode === $scope.selectedWards);
        var selectedWardName = selectedWard ? selectedWard.WardName : '';

        var diaChiKhachNhan =$('#soNha').val()+ "-" + selectedWardName+ "-" + selectedDistrictName  + "-" + selectedProvinceName ;


        console.log("check id khach hàng1: ",$scope.khachHangById);
        var trangThai = 5;
        if($scope.shippingMethod===2){
            trangThai=1;
        }
        $scope.hoaDonData ={
            maHoaDon: $scope.generateRandomString(8),
            idKhuyenMai: phieuGiamGia,
            idTrangThai: trangThai,//trạng thái của hoá đơn hoàn thành
            idPhuongThucThanhToan: phuongThucThanhToan,
            idDonHang: $scope.selectedId,
            idKhachHang: $scope.khachHangById.id_khach_hang,
            tenKhachHang: nameKH,
            tongTien: tongTien,
            tongTienKhuyenMai: $scope.getTienGiam(),
            tongTienSauKhuyenMai: $scope.getTienKhachPTra(),
            phiVanChuyen: $scope.getFeeShip(),
            tongTienThanhToan: $scope.tongTienTHanhToan(),
            ghiChu: ghiChu,
            tenKhachNhan: tenKhachNhan,
            soDienThoaiKhachNhan: sdtKhachNhan,
            diaChiKhachNhan: diaChiKhachNhan,
            phuongThucNhan: $scope.shippingMethod,
            loaiDonHang: '1'
        }
        console.log("check data hoa đon: ",$scope.hoaDonData);
        if($scope.hoaDonData.idDonHang === null){
            $scope.showNotification('Chưa Chọn đơn Hàng!','error');
            return;
        }

        if(khachThanhToan === null || khachThanhToan ==0){
            $scope.showNotification('Chưa Nhập tiền khách thanh toán!','error');
            return;
        }

        if (isNaN(khachThanhToan) || khachThanhToan === "") {
            $scope.showNotification('Giá trị thanh toán không hợp lệ. Vui lòng nhập số!','error');
            return;
        }

        khachThanhToan = parseFloat(khachThanhToan);

        if (khachThanhToan < ($scope.getTienKhachPTra()+$scope.getFeeShip())) {
            $scope.showNotification('Khách chưa thanh toán đủ tiền!','error');
            return;
        }

        var sdt = $('#sdtKH').val();
        if(nameKH === "" || sdt === "") {
            $scope.showNotification('Chưa Chọn Khách Hàng!','error');
            return;
        }

        if ($scope.productDetails && $scope.productDetails.length === 0) {
            $scope.showNotification('Chưa chọn sản phẩm!','error');
            return;
        }

        if($scope.shippingMethod ==='2'){
            if(tenKhachNhan === null || tenKhachNhan ===""){
                $scope.showNotification('Chưa nhập tên khách nhận!','error');
                return;
            }
            var regex = /^[a-zA-ZÀ-ỹ\s]+$/;
            if (regex.test(tenKhachNhan)) {
                console.log("Tên khách nhận hợp lệ!");
            } else {
                console.log("Tên khách nhận không hợp lệ!");
                $scope.showNotification("Tên khách nhận không hợp lệ!", "error");
            }

            if(tenKhachNhan.length <2 || tenKhachNhan.length > 20){
                $scope.showNotification('Độ dài tên khách nhận không hợp lệ!','error');
                return;
            }

            if(sdtKhachNhan === null || sdtKhachNhan ===""){
                $scope.showNotification('Chưa nhập số diện thoại khách nhận!','error');
                return;
            }
            var regex1 = /^[0-9]+$/;
            if (regex1.test(sdtKhachNhan)) {
                console.log("Số điện thoại hợp lệ!");
            } else {
                console.log("Số điện thoại không hợp lệ!");
                $scope.showNotification("Số diện thoại khách nhận không hợp lệ!", "error");
            }
            if(sdtKhachNhan.length <10 || sdtKhachNhan.length > 11){
                $scope.showNotification('Độ dài số diện thoại khách nhận hợp lệ!','error');
                return;
            }

            if($('#soNha').val() === null || $('#soNha').val()===""){
                $('#messSoNha').text('Chưa nhập địa chỉ số nhà!');
                $('#messSoNha').show();
                return;
            }

            var regex2 = /^[a-zA-Z0-9À-ỹ\s]+$/;
            if (regex2.test($('#soNha').val())) {
                console.log("Số nhà hợp lệ!");
            } else {
                console.log("Số nhà không hợp lệ!");
                $('#messSoNha').text('Số nhà không hợp lệ!');
                $('#messSoNha').show();
            }

            if(selectedProvinceName === null || selectedProvinceName===""){
                $('#messThanhPho').text('Chưa chọn tỉnh - thành phố!');
                $('#messThanhPho').show();
                return;
            }
            if(selectedDistrictName === null || selectedDistrictName===""){
                $('#messQuan').text('Chưa chon quận - huyện!');
                $('#messQuan').show();
                return;
            }
            if(selectedWardName === null || selectedWardName===""){
                $('#messPhuong').text('Chưa chọn phường - xã !');
                $('#messPhuong').show();
                return;
            }
        }
        //console.log("check log upadteghg: ",$scope.hoaDonData);
        var hoaDonData = angular.copy($scope.hoaDonData);

        $http({
            method: 'POST',
            url: '/hoa-don/them-moi',
            data: hoaDonData,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);
            }
        }) .then(function(response) {
            console.log('Hoá Đơn DATA:', response.data);
            idHoaDoncheck = response.data.idHoaDon;
            $scope.getKhachHang();
            // alert("Lưu Hoá Đơn Thành Công!");
            $('#printer').show();
            $scope.showNotification('Lưu Hoá Đơn Thành Công!','success');
        }).catch(function(error) {
            console.error('Có lỗi xảy ra khách hàng DATA:', error);
            $scope.showNotification('Lưu Hoá Đơn Thất Bại!','error');
        });
    }

    //thêm khách hàng
    $scope.addKhachHang = function (){
        var sdt = $('#sdt-khach-hang').val();

        if(sdt === null || sdt ===""){
            $scope.showErrrorsMes("Không để trống số điện thoại!");
            return;
        }

        if(sdt.length <10 || sdt >11){
            $scope.showErrrorsMes("Số điện thoại Không đúng định dạng!");
            return;
        }

        if (/^\d+$/.test(sdt)) {
            // Nếu chỉ chứa số
            console.log("Số điện thoại hợp lệ.");
        } else {
            // Nếu có ký tự không phải số
            $scope.showErrrorsMes("Số điện thoại Không hợp lệ!");
            return;
        }

        $scope.dataKhachHang ={
            maKhachHang: $scope.generateRandomString(8),
            soDienThoai: sdt
        }
        var khachHangData = angular.copy($scope.dataKhachHang);
        $http({
            method: 'POST',
            url: '/don-hang/them-khach-hang',
            data: khachHangData,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }) .then(function(response) {
            console.log('Khách Hàng DATA:', response.data);
            $scope.getKhachHang();
            $scope.showNotification('Thêm Khách Hàng Thành Công!','success');
        }).catch(function(error) {
            console.error('Có lỗi xảy ra Khách Hàng DATA:', error);
            if (error.data) {
                $scope.showErrrorsMes(error.data);  // Sử dụng thông báo lỗi từ server
            } else {
                $scope.showErrrorsMes("Có lỗi xảy ra khi thêm khách hàng.");
            }
        });

        $('#show-modal-khach').modal('show');
    }

    //xoá đơn hàng
    $scope.deleteDonHang = function (id){
        if(confirm("Bạn có muôna xoá đơn hàng?")){
            $http.delete('/don-hang/don-hang/xoa/'+id).then(function (response){
                console.log('Đã xoá thành công đơn hang: ',response);
                //$scope.selectOrder(selectedId);
                $scope.getDonHang();
                $scope.productDetails =[];
                $scope.getProducts();
                $scope.showNotification('Xoá Đơn Hàng Thành Công!','success');
            }).catch(function (errors){
                console.error('Có lỗi xảy ra:', errors);
                $scope.showNotification('Xoá Đơn Hàng Thất BẠi!','error');
            })

        }
    }

    //xoá đơn hàng chi tiết
    $scope.deleteDonHangChiTiet = function (id, idSP){
        // alert("Show: "+id);
        if(confirm("Bạn Có muốn Xoá sản phẩm khỏi đơn hàng không?")){
            $http.delete('/don-hang/don-hang-chi-tiet/xoa/' + id).then(function (response){
                console.log('Đã xóa đơn hàng chi tiết thành công:', response);
                $scope.selectOrder(selectedId);
                var productInScope = $scope.products.find(item => item.idSanPhamChiTiet === idSP);
                if (productInScope) {
                    productInScope.soLuong++;
                } else {
                    console.error('Không tìm thấy sản phẩm với id:', id);
                }
            }).catch(function (errors){
                console.error('Có lỗi xảy ra:', errors);
            })
        }
    }

    //tìm kiếm khách hàng
    $scope.searchKhachHang = function (){
        var sdt = $('#sdt-khach-hang').val();
        if(sdt === null || sdt ===""){
            $scope.getKhachHang();
        }else {
            $http.get("/don-hang/khach-hang/tim-kiem", {
                params: { sdt: sdt }  // Truyền `sdt` dưới dạng query parameter
            }).then(function (response) {
                $scope.khachHang = [];
                console.log("Check kH search: ",response.data);
                $scope.khachHang = response.data;
            }).catch(function (errors) {
                console.error('Có lỗi xảy ra:', errors);
            });
        }
    }

    //tìm kiếm sản phẩm
    $scope.inputData = "";
    $scope.searchSanPham = function (){
        var searchData = $('#search-product').val();
        if(searchData === null || searchData ===""){
            $scope.getProducts();
        }else {
            $http.get("/don-hang/san-pham-chi-tiet/tim-kiem", {
                params: { tenSanPham: searchData }  // Truyền `sdt` dưới dạng query parameter
            }).then(function (response) {
                $scope.products = [];
                console.log("Check PD search: ",response.data);
                $scope.products = response.data;
            }).catch(function (errors) {
                console.error('Có lỗi xảy ra:', errors);
            });
        }
    }


    //show-modal-khach
    $scope.openModal = function() {
        if(selectedId === null){
            $scope.showNotification("Chưa chọn đơn hàng!","error");
            return;
        }
        $('#show-modal-khach').modal('show');
    };

    $scope.openModalKhuyenMai = function() {
        $scope.getKhuyenmai();
        $('#show-modal-khuyen-mai').modal('show');
    };

    //tang so luong
    $scope.soLuongPlus = function (details){
        details.soLuong +=1;
        //$scope.getProducts();
        console.log("Số Lượng Reduce: ",details);
        console.log("Số Lượng Reduce: ",details.soLuong);
        console.log("Số Lượng Reduce: ",details.soLuong * details.giaBan)
        $scope.updateQuantityPlus(details);
    }
    //Giảm Số Lượng
    $scope.soLuongReduce = function(details){
        if(details.soLuong >1){
            details.soLuong -=1;
            //$scope.getProducts();
            console.log("Số Lượng plus: ",details.soLuong);
            console.log("Số Lượng plus: ",details.soLuong * details.giaBan);
            $scope.updateQuantityReduce(details);
        }
    }

    //cập nhật số lượng khi reduce hoặc plus
    $scope.updateQuantityPlus = function (details){
        console.log("check Quantity: ",details);
        $scope.dataUpdateProduct ={
            maDonHangChiTiet: $scope.generateRandomString(8),
            idĐonHangChiTiet: details.idDonHangChiTiet,
            idSanPhamChiTiet: details.idSanPham,
            idĐonHang:selectedId,
            soLuong: '1'
        }
        console.log("check Quantity: ",$scope.dataUpdateProduct);
        var updateProduct = angular.copy($scope.dataUpdateProduct);
        $http({
            method: 'PUT',
            url: '/don-hang/don-hang-chi-tiet/cap-nhat-so-luong-tang',
            data: updateProduct,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }).then(function(response) {
            //console.log('Sản phẩm thêm thành công');
            console.log('Sản phẩm thêm: ',response.data);
            // $scope.getProducts();
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
        });
    }

    $scope.updateQuantityReduce = function (details){
        $scope.dataUpdateProductReduce ={
            maDonHangChiTiet: $scope.generateRandomString(8),
            idĐonHangChiTiet: details.idDonHangChiTiet,
            idSanPhamChiTiet: details.idSanPham,
            idĐonHang:selectedId,
            soLuong: '1'
        }
        //console.log("check Quantity: ",$scope.dataUpdateProduct);
        var updateProductReduce = angular.copy($scope.dataUpdateProductReduce);
        $http({
            method: 'PUT',
            url: '/don-hang/don-hang-chi-tiet/cap-nhat-so-luong-giam',
            data: updateProductReduce,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }).then(function(response) {
            //console.log('Sản phẩm thêm thành công');
            console.log('Sản phẩm thêm: ',response.data);
            $scope.getProducts();
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
        });
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
            sumMoney += details.soLuong * details.giaBan;
        });
        return sumMoney;
    }
    $scope.getTienGiam = function (){
        $scope.discountRate = $('#muc-giam-gia').val();
        if ($scope.discountRate === null || $scope.discountRate.length === 0) {
            return 0;
        }
        let tongTienGiam =0;
        if ($scope.discountRate.includes('%')) {
            let tienGiam = parseFloat($scope.discountRate.replace('%', '')) / 100;
            tongTienGiam = $scope.getSum() * tienGiam;
        } else {
            let tienGiam = parseFloat($scope.discountRate);
            tongTienGiam = $scope.getSum() - tienGiam;
        }
        return tongTienGiam;
    }
    $scope.getTienKhachPTra = function (){
        let khachPhaiTra = $scope.getSum() - $scope.getTienGiam();
        return khachPhaiTra;
    }
    $scope.tongTienTHanhToan = function (){
        let tong = $scope.getTienKhachPTra() +$scope.getFeeShip();
        return tong;
    }
    //tính tiền thừa
    $scope.tinhTienThua = function () {
        if($scope.khachThanhToan ===0){
            return 0;
        }
        let tongTien = $scope.tongTienTHanhToan();
        let khachThanhToan = parseFloat($scope.khachThanhToan) || 0; // Đảm bảo giá trị là số
        return khachThanhToan - tongTien;
    };

    //khuyen mai
    $scope.getKhuyenmai = function (){
        if ($scope.productDetails && $scope.productDetails.length === 0) {
            alert("Chưa chọn sản phẩm!");
            return;
        }
        $http.get("/don-hang/khuyen-mai").then(function (response) {
            console.log('khuyen mai:', response.data);
            $scope.khuyenMai = response.data;
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
        })
    }
    $scope.getKhuyenmaiById = function (id){
        $http.get("/don-hang/khuyen-mai/"+id).then(function (response) {
            console.log('khuyen mai by id:', response.data);
            $scope.khuyenMaiById = response.data;
            $('#ma-khuyen-mai').val(response.data.maKhuyenMai);
            if(response.data.mucGiamGia < 100){
                $('#muc-giam-gia').val(response.data.mucGiamGia + '%');
            }else {
                $('#muc-giam-gia').val(response.data.mucGiamGia);
            }

            // $('#show-modal-khach').modal('hide');
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
        })
    }


    //in hoá đơn
    $scope.printer = function (){
        // if($scope.selectedId === null){
        //     return ;
        // }
        console.log('hoa dơn get id:', idHoaDoncheck);
        $http.get("/hoa-don/get-invoice/"+idHoaDoncheck).then(function (response) {
            console.log('response.data has data',response.data);
            if (response.data && response.data.idHoaDon) {
                console.log('response.data has data',response.data);
                $scope.printerInvoice(response.data.idHoaDon);
            }
            $('#printer').hide();
            $scope.showNotification("In hoá đơn thành công!","success");
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);
            $scope.showNotification("In hoá đơn thất bại!","error");
        })

    }
    $scope.printerInvoice = function (idHoaDon){
        console.log('check in hoá đơn:');
        $http.get("/hoa-don/invoice/"+idHoaDon).then(function (response) {
            console.log('thanh cong:', response);
        }).catch(function (errors) {
            console.error('Có lỗi xảy ra:', errors);

        })
    }

    //ẩn thông báo
    $scope.hideErrrorsMes = function (){
        $('#erroresMessage').hide();
    }
    //ẩn thông báo
    $scope.hideErrrorsMes1 = function (){
        $('#erroresMessage1').hide();
    }
    //hiện thông báo
    $scope.showErrrorsMes = function (message){
        $('#showMessage').text(message);
        $('#erroresMessage').show();

        setTimeout(() => {
            $('#erroresMessage').hide();
        }, 2000);
    }
    //hiện thông báo
    $scope.showErrrorsMes1 = function (message){
        $('#showMessage1').text(message);
        $('#erroresMessage1').show();

        setTimeout(() => {
            $('#erroresMessage1').hide();
        }, 2000);
    }



    $scope.getProvinces = function (){
        $http.get("/api/provinces").then(function (response){
            console.log("check res: ",response);
            $scope.provinces = response.data;
        }).catch(function (errors) {
            console.error("có lỗi xảy ra: ",errors)
        })
    }

    $scope.getDisTricts = function (){
        console.log($scope.selectedProvince)
        if($scope.selectedProvince === null || $scope.selectedProvince ===""){
            alert("Chưa chọn tỉnh Thành Phố")
            return;
        }

        var url = "/api/districts/" + $scope.selectedProvince.ProvinceID;
        console.log(url)
        $http.get(url).then(function (response){
            console.log("check res: ",response);
            $scope.districts = response.data;
        }).catch(function (errors) {
            console.error("có lỗi xảy ra: ",errors)
        })
    }

    $scope.getWard = function (){
        console.log($scope.selectedDistrict)
        if($scope.selectedDistricts === null || $scope.selectedDistricts ===""){
            alert("Chưa chọn tỉnh Thành Phố")
            return;
        }

        var url = "/api/ward/" + $scope.selectedDistricts;
        console.log(url)
        $http.get(url).then(function (response){
            console.log("check res: ",response);
            $scope.wards = response.data;
        }).catch(function (errors) {
            console.error("có lỗi xảy ra: ",errors)
        })
    }

    $scope.feeShippingApi = function (){
        console.log($scope.selectedProvince)
        if($scope.selectedProvince === null || $scope.selectedProvince ===""){
            alert("Chưa chọn tỉnh Thành Phố")
            return;
        }
        if($scope.selectedDistricts === null || $scope.selectedDistricts ===""){
            alert("Chưa chọn Quận-Huyện")
            return;
        }
        if($scope.selectedWards === null || $scope.selectedWards ===""){
            alert("Chưa chọn Phường-Xã")
            return;
        }

        $scope.shippingData = {
            service_type_id: 2,
            from_district_id: $scope.fromDistrictId,
            from_ward_code: "13010",
            // to_province_id: $scope.selectedProvince.ProvinceID,
            to_district_id: $scope.selectedDistricts,
            to_ward_code: $scope.selectedWards,
            weight: 2000
        };
        var dataShipping = angular.copy($scope.shippingData);
        $http({
            method: 'POST',
            url: '/api/fee',
            data: dataShipping,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }) .then(function(response) {
            console.log("check fee shipping: ",response);
            $scope.feeShipping = response.data;
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
        });
    }

    //phí vận chuyển
    $scope.getFeeShip = function (){
        if($scope.feeShipping === null || $scope.feeShipping.length === 0){
            return 0;
        }
        let feeShipping = $scope.feeShipping.total
        return feeShipping;
    }

    $scope.validateQuantity = function(details) {
        // console.log("Products:", $scope.products);
        // console.log("Details:", details);
        // Tìm sản phẩm tương ứng trong listProducts để lấy số lượng có sẵn
        let availableProduct = $scope.products.find(product => product?.idSanPhamChiTiet === details.idSanPham);
        console.log("availableProduct: ",availableProduct);
        console.log("availableProduct: ",$scope.products);
        // Kiểm tra nếu số lượng yêu cầu lớn hơn số lượng có sẵn
        if (availableProduct && details.soLuong > availableProduct.soLuong) {
            details.invalidQuantity = true;
        } else {
            details.invalidQuantity = false;
            console.log("check2:")
            $scope.updateQuantityChange(details);
        }
    };
    $scope.updateQuantityChange = function (details){
        console.log("details change: ",details);
        $scope.dataUpdateProduct ={
            maDonHangChiTiet: $scope.generateRandomString(8),
            idĐonHangChiTiet: details.idDonHangChiTiet,
            idSanPhamChiTiet: details.idSanPham,
            idĐonHang:selectedId,
            soLuong: details.soLuong,
            giaBan:details.giaBan
        }
        console.log("check Quantity: ",$scope.dataUpdateProduct);
        var updateProductReduce = angular.copy($scope.dataUpdateProduct);
        $http({
            method: 'PUT',
            url: '/don-hang/don-hang-chi-tiet/so-luong-thay-doi',
            data: updateProductReduce,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }).then(function(response) {
            //console.log('Sản phẩm thêm thành công');
            console.log('Sản phẩm thay đổi số lượng: ',response.data);
            $scope.getProducts();
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
        });
    }

    //phân trang
    $scope.getPagedProducts = function () {
        const start = ($scope.currentPage - 1) * $scope.pageSize;
        const end = start + $scope.pageSize;
        return $scope.products.slice(start, end); // Lấy danh sách sản phẩm cho trang hiện tại
    };

    // Chuyển đến trang khác
    $scope.setPage = function (page) {
        if (page >= 1 && page <= $scope.totalPages) {
            $scope.currentPage = page;
        }
    };

    //thông báo
    $scope.showNotification = function(message, type) {
        $scope.notification.message = message;
        $scope.notification.type = type;

        // Chọn icon dựa trên loại thông báo
        if (type === 'success') {
            $scope.notification.icon = $sce.trustAsHtml('✔️');
        } else if (type === 'error') {
            $scope.notification.icon = $sce.trustAsHtml('❌');
        } else {
            $scope.notification.icon = $sce.trustAsHtml('ℹ️');
        }

        $scope.notification.show = true;

        // Sử dụng $timeout để tự động ẩn sau 5 giây
        $timeout(function() {
            $scope.notification.show = false;
        }, 3000);
    };


    //load data product when run
    $scope.getProducts();
    $scope.getDonHang();
    $scope.getKhachHang();
    $scope.hideErrrorsMes();
    $scope.hideErrrorsMes1();
    $scope.getProvinces();
    $('#messSoNha').hide();
    $('#messThanhPho').hide();
    $('#messQuan').hide();
    $('#messPhuong').hide();
    $('#printer').hide();
})