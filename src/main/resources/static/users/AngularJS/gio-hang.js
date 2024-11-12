var app = angular.module("gio-hang", [])
app.controller("gio-hang-ctrl", function ($scope, $http) {
    $scope.generateRandomString = function(length) {
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
        let result = '';

        for (let i = 0; i < length; i++) {
            const randomIndex = Math.floor(Math.random() * characters.length);
            result += characters[randomIndex];
        }

        return result;
    };

    $scope.message = "Hello 1234456787!";
    $scope.listProducts = [];
    $scope.items = [];//lưu dữ liệu vào localStore
    $scope.username = [];
    $scope.cartDetaiPro = [];
    $scope.cartDetai =[];
    $scope.cart = [];

    $scope.getUserName = function (){
        $http.get("/lay-tai-khoan").then(function (response){
            console.log("check user: ",response);
            $scope.username = response.data;
            if (!$scope.username) {
                $scope.loadFromLocalStorage();
                console.log("username null");
            } else {
                console.log("username not null",$scope.username);
                $scope.getCart();
            }
        }).catch(function (errors) {
            console.error("có lỗi xảy ra: ",errors)
        })
    }

    $scope.getCart = function (){
        $http.get("/gio-hang/lay-theo-user").then(function (response){
            console.log("check gio hang: ",response.data);
            $scope.cart = response.data;
            $scope.getDetailCart($scope.cart.idGioHang);
        }).catch(function (errors) {
            console.error("có lỗi xảy ra: ",errors)
        })
    }

    $scope.getAllProduct = function (){
        $http.get("/danh-sach-san-pham").then(function (response){
            $scope.listProducts = response.data;
            console.log("check log: ",response.data)
        }).catch(function (errors){
            console.error("có lỗi xảy ra: ",errors)
        })
    }
    var idSanPhamChiTiet = null;
    $scope.createCartWithUsername = function (id){

        $scope.cartData={
            maGioHang: $scope.generateRandomString(8),
            userName: $scope.username
        }

        if(!$scope.cart || $scope.cart.length ===0){
            //tạo giỏ hàng
            var dataCart = angular.copy($scope.cartData);
            $http({
                method: 'POST',
                url: '/gio-hang/them-moi',
                data: dataCart,
                headers: {
                    'Content-Type': 'application/json'
                },
                transformRequest: function(data) {
                    return JSON.stringify(data);
                }
            }) .then(function(response) {
                console.log("check cart when create: ",response);

                $scope.cart = response.data;
            }).catch(function(error) {
                console.error('Có lỗi xảy ra:', error);
            });
        }
        //tạo giỏ hàng chi tiết
        //kiểm tra xem có sản phẩm chưa
        if(!$scope.items || $scope.items.length === 0){
            //lấy sản phẩm
            $http.get(`/danh-sach-san-pham/${id}`).then(response =>{
                // response.data.qty = 1;
                // response.data.soLuong = 1;
                // console.log("check pro get id: ",response.data)
                $scope.cartDetaiPro  = response.data;
                idSanPhamChiTiet = response.data.idSanPhamChiTiet;
                console.log("check cart getPro: ", $scope.cartDetaiPro);
                $scope.createCartDetail();
            }).catch(function(error) {
                console.error('Có lỗi xảy ra:', error);
            });
            //thêm sản phẩm vào gio hàng chi tiết

        }else {
            //items đã có sản phẩm
            //console.log("items:", $scope.items);
            var itemCartDetail = $scope.items.find(item=>item.sanPhamChiTiet.idSanPhamChiTiet === id);
            //console.log("itemCartDetail: ",itemCartDetail);
            if(itemCartDetail){
                //đã có cập nhật số lượng
                console.log("update so luong.......");
                $http.get(`/danh-sach-san-pham/${id}`).then(response =>{
                    idSanPhamChiTiet = response.data.idSanPhamChiTiet;
                    console.log("check idSanPhamChiTiet : ", idSanPhamChiTiet);
                    $scope.updateQuantityCartDetail();
                }).catch(function(error) {
                    console.error('Có lỗi xảy ra:', error);
                });

            }else {
                //thêm mới nếu chưa có
                $http.get(`/danh-sach-san-pham/${id}`).then(response =>{
                    $scope.cartDetaiPro  = response.data;
                    idSanPhamChiTiet = response.data.idSanPhamChiTiet;
                    //console.log("check cart getPro: ", $scope.cartDetaiPro);
                    $scope.createCartDetail();
                }).catch(function(error) {
                    console.error('Có lỗi xảy ra:', error);
                });
            }

        }
    }

    $scope.createCartDetail = function (){
        $scope.cartDetailData={
            idGioHang: $scope.cart.idGioHang,
            maGioHangChiTiet: $scope.generateRandomString(8),
            idSanPhamChiTiet: $scope.cartDetaiPro.idSanPhamChiTiet
        }
        console.log("checkoi: ",$scope.cartDetailData)
        var dataCartDetail = angular.copy($scope.cartDetailData);
        $http({
            method: 'POST',
            url: '/gio-hang-chi-tiet/them-moi',
            data: dataCartDetail,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);
            }
        }) .then(function(response) {
            console.log("check cartDetail when create: ",response.data);
            $scope.cartDetai = response.data;
            console.log("check cartDetail when create1: ",$scope.cartDetai);
            $scope.getDetailCart($scope.cartDetai.gioHang.idGioHang)
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
        });
    }

    $scope.updateQuantityCartDetail = function (){
        $scope.cartDetailData={
            idGioHang: $scope.cart.idGioHang,
            maGioHangChiTiet: $scope.generateRandomString(8),
            idSanPhamChiTiet: idSanPhamChiTiet,
            soLuong: 1,
            giaBan:1
            // donGia:1
        }

        var dataCartDetail = angular.copy($scope.cartDetailData);
        console.log("Data gửi đi:", dataCartDetail);
        $http({
            method: 'PUT',
            url: '/gio-hang-chi-tiet/cap-nhat-so-luong',
            data: dataCartDetail,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);
            }
        }) .then(function(response) {
            console.log("check cartDetail when update so lượng: ",response.data);
             $scope.getDetailCart(response.data.gioHang.idGioHang);

        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
            if (error.data) {

            } else {
                // $scope.showErrrorsMes("Có lỗi xảy ra khi thêm khách hàng.");
            }
        });
    }

    $scope.getDetailCart = function(idGioHang){
        $http.get(`/gio-hang-chi-tiet/${idGioHang}`).then(response =>{
            response.data.qty = response.data.soLuong;
            // response.data.soLuong = 1;
            console.log("check cart detail get id: ",response.data)
            $scope.items  = response.data;
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
        });
    }

    $scope.addProductIntocart= function (id){
        //alert("add OK");
        if (!$scope.username){
            var item = $scope.items.find(item=>item.idSanPhamChiTiet == id);
            if(item){
                item.qty++;
                item.soLuong++;
                this.saveToLocalStorage();
            }else{
                $http.get(`/danh-sach-san-pham/${id}`).then(response =>{
                    response.data.qty = 1;
                    response.data.soLuong = 1;
                    console.log("check log get id: ",response.data)
                    $scope.items.push(response.data);
                    this.saveToLocalStorage();
                });
            }
        }else {
            $scope.createCartWithUsername(id);
        }

    }


    $scope.count = function (){
        return $scope.items.map(item => item.soLuong).reduce((total, soLuong) => total + soLuong, 0);
    }

    $scope.clearLocalStorage = function (){
        $scope.items=[];
        this.saveToLocalStorage();
    }

    $scope.removeLocalStorage = function (id){
        console.log('check delete: ',id)
        console.log('check delete: ',$scope.items)
        if (!$scope.username){
            var index = this.items.findIndex(item => item.sanPhamChiTiet.idSanPhamChiTiet == id);
            $scope.items.splice(index,1);
            this.saveToLocalStorage();
        }else {
            var item = this.items.find(item=>item.sanPhamChiTiet.idSanPhamChiTiet === id);
            console.log('check delete index: ',item)
            $http({
                method: 'DELETE',
                url: '/gio-hang-chi-tiet/xoa-theo-id-san-pham/' + item.sanPhamChiTiet.idSanPhamChiTiet
            }).then(function(response) {
                console.log("Đã xóa sản phẩm khỏi giỏ hàng:", response.data);
                $scope.getDetailCart(response.data.gioHang.idGioHang);
            }, function(error) {
                // Xử lý lỗi
                console.error("Lỗi khi xóa sản phẩm:", error.data);
                alert("Không tìm thấy sản phẩm hoặc có lỗi khi xóa!");
            });
        }
    }

    $scope.saveToLocalStorage = function (){
        var json = JSON.stringify(angular.copy(this.items));
        localStorage.setItem("cart",json);
    }

    $scope.loadFromLocalStorage = function (){
        var json = localStorage.getItem("cart");
        $scope.items = json ? JSON.parse(json) : [];
    }


    //load dữ liệu
    $scope.getAllProduct();
    //$scope.loadFromLocalStorage();

    $scope.getUserName();
});




app.controller("don-hang-online-ctrl", function ($scope, $http,$sce,$timeout) {
    $scope.generateRandomString = function(length) {
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
        let result = '';

        for (let i = 0; i < length; i++) {
            const randomIndex = Math.floor(Math.random() * characters.length);
            result += characters[randomIndex];
        }

        return result;
    };

    $scope.mess = "hello";
    $scope.itemsOrder = [];
    //tỉnh thành phố
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

    //lấy dữ liệu
    $scope.paymentMethod = 1;

    //thông báo
    $scope.notification = {
        show: false,
        message: '',
        type: '',
        icon: ''
    };

    $scope.username = null;
    $scope.cart = [];

    //user
    $scope.getUser = function (){
        $http.get("/lay-tai-khoan").then(function (response){
            //console.log("check user: ",response);
            // $scope.username = response.data;
            $scope.username = response.data;
            console.log("check user after setting: ", $scope.username);
            if(!$scope.username){
                console.log("check user: null");
                $('#hoVaTen').val('');
                $('#email').val('');
                $('#soDienThoai').val('');
                $scope.loadFromLocalStorage();
            }else {
                console.log("check user view gio hang: not null",$scope.username);
                //hiển thị thông tin khách hàng khi đăng nhập
                $('#hoVaTen').val(response.data.hoTen);
                $('#email').val(response.data.email);
                $('#soDienThoai').val(response.data.soDienThoai);

                //lấy giỏ hàng chi tiết
                $scope.itemsOrder =[];
                $scope.getCart();
            }
        }).catch(function (errors) {
            console.error("có lỗi xảy ra: ",errors)
        })
    }
    //lấy giỏ hàng
    $scope.getCart = function (){
        $http.get("/gio-hang/lay-theo-user").then(function (response){
            console.log("check gio hang: ",response.data);
            $scope.cart = response.data;
            $scope.getDetailCart($scope.cart.idGioHang);
        }).catch(function (errors) {
            console.error("có lỗi xảy ra: ",errors)
        })
    }
    //lấy giỏ hàng chi tiết
    $scope.getDetailCart = function(idGioHang){
        $http.get(`/gio-hang-chi-tiet/${idGioHang}`).then(response =>{
            response.data.qty = response.data.soLuong;
            // response.data.soLuong = 1;
            console.log("check cart detail get id: ",response.data)
            $scope.itemsOrder  = response.data;
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
        });
    }

    $scope.hideNotification = function (){
        $('#messHoTen').hide();
        $('#messEmail').hide();
        $('#messSDT').hide();
        $('#messSoNha').hide();
        $('#messThanhPho').hide();
        $('#messQuan').hide();
        $('#messPhuong').hide();
    }

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

    $scope.saveToLocalStorage = function (){
        var json = JSON.stringify(angular.copy(this.itemsOrder));
        localStorage.setItem("cart",json);
    }

    $scope.loadFromLocalStorage = function (){
        var json = localStorage.getItem("cart");
        $scope.itemsOrder = json ? JSON.parse(json) : [];
        console.log("check itemOrder: ",$scope.itemsOrder);
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


    //format VND
    $scope.formatCurrency = function (amount, currencyCode = 'VND') {
        return amount.toLocaleString('vi-VN', { style: 'currency', currency: currencyCode });
    };
    //tổng tiền
    $scope.getSum = function (){
        if($scope.itemsOrder === null || $scope.itemsOrder.length === 0){
            return 0;
        }
        let sumMoney = 0;
        $scope.itemsOrder.forEach(function (details){
            sumMoney += details.soLuong * details.giaBan;
        });
        return sumMoney;
        //return  sumMoney;
    }
    $scope.promotionAmount = function (){
        $scope.discountRate = "10%";
        let reduceMoney = parseFloat($scope.discountRate.replace('%',''))/100;
        let sumPromotionAmount = $scope.getSum() * reduceMoney;
        return sumPromotionAmount;
    }
    //tính tiền thừa
    $scope.totalPromotionAmountAfter = function () {
        // if($scope.itemsOrder === null || $scope.itemsOrder.length === 0){
        //     return 0;
        // }
        let sumAfterAmount = $scope.getSum() - $scope.promotionAmount();
        return sumAfterAmount;
    };
    //phí vận chuyển
    $scope.getFeeShip = function (){
        if($scope.feeShipping === null || $scope.feeShipping.length === 0){
            return 0;
        }
        let feeShipping = $scope.feeShipping.total
        return feeShipping;
    }
    //tính tổng thanh toán
    $scope.sumAmount = function (){
        let sumAmount =$scope.totalPromotionAmountAfter() + $scope.getFeeShip();
        return sumAmount;
    }
    //load table gio hàng
    $scope.loadFromLocalStorage();

    //tạo đơn hàng
    $scope.createOrderOnline = function (){
        // Lấy tên tỉnh/thành phố đã chọn
        var selectedProvinceName = $scope.selectedProvince ? $scope.selectedProvince.ProvinceName : '';

        // Tìm quận/huyện đã chọn
        var selectedDistrict = $scope.districts.find(d => d.DistrictID == $scope.selectedDistricts); // Sử dụng == thay vì ===
        var selectedDistrictName = selectedDistrict ? selectedDistrict.DistrictName : '';

        // Tìm xã/phường đã chọn
        var selectedWard = $scope.wards.find(w => w.WardCode === $scope.selectedWards);
        var selectedWardName = selectedWard ? selectedWard.WardName : '';

        var diaChiNhan =$('#soNha').val()+ "-" + selectedWardName+ "-" + selectedDistrictName  + "-" + selectedProvinceName ;

        if(selectedDistrictName === null && selectedProvinceName == null && selectedWardName === null
           && $('#hoVaTen').val() === null && $('#soDienThoai').val() == null && $('#email').val() == null
        ){
            $scope.showNotification('Chưa nhập thông tin khách hàng!','error');
            return;
        }
        if($('#hoVaTen').val() === null || $('#hoVaTen').val() === ""){
            // $scope.showNotification('Chưa nhập họ tên khách hàng!','error');
            $('#messHoTen').text('Chưa nhập họ tên !');
            $('#messHoTen').show();
            return;
        }
        if ($('#email').val() === null || $('#email').val() === "") {
            //$scope.showNotification('Chưa nhập Email khách hàng!','error');
            $('#messEmail').text('Chưa nhập email !');
            $('#messEmail').show();
            return;
        } else {
            // Kiểm tra email có hợp lệ không
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if ( emailRegex.test($('#email').val()) ) {
                $('#messEmail').text('');
                $('#messEmail').hide();
            } else {
                //$scope.showNotification('Email Không hợp lệ!','error');
                $('#messEmail').text('Email không hợp lệ!');
                $('#messEmail').show();
                return;
            }
        }
        if($('#soDienThoai').val() === null || $('#soDienThoai').val() === ""){
            //$scope.showNotification('Chưa nhập số điện thoại khách hàng!','error');
            $('#messSDT').text('Chưa nhập số điện thoại !');
            $('#messSDT').show();
            return;
        }else {
            const phoneRegex = /^(0[0-9]{8,10})$/;
            if ( phoneRegex.test($('#soDienThoai').val()) ) {
                $('#messSDT').text('');
                $('#messSDT').hide();
            } else {
                //$scope.showNotification('Số điện thoại không hợp lệ!','error');
                $('#messSDT').text('Số điện thoại không hợp lệ!');
                $('#messSDT').show();
                return;
            }
        }
        if($('#soNha').val() === null || $('#soNha').val()===""){
            $('#messSoNha').text('Chưa nhập địa chỉ nhà!');
            $('#messSoNha').show();
            return;
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


        $scope.orderData ={
            maDonHang: $scope.generateRandomString(8),
            tenKhachHang: $('#hoVaTen').val(),
            soDienThoaiKhachHang: $('#soDienThoai').val(),
            diaChiKhachHang: diaChiNhan,
            emailKhachHang: $('#email').val(),
            tongTien: $scope.getSum(),
            tongTienKhuyenMai: $scope.promotionAmount(),
            tongTienSauKhuyenMai: $scope.totalPromotionAmountAfter(),
            tongTienThanhToan: $scope.sumAmount(),
            phiVanChuyen: $scope.getFeeShip(),
            ghiChu: $('#ghi-chu').val(),
            trangThaiThanhToan: false,
            idTrangThai: 1,
            idPhuongThucThanhToan: $scope.paymentMethod,
            idKhuyenMai: 1,
            orderDetail: $scope.itemsOrder
        };
        //console.log("check createOrederOnline: ",$scope.orderData);
        var dataOrder = angular.copy($scope.orderData);
        $http({
            method: 'POST',
            url: '/don-hang-online/them-moi',
            data: dataOrder,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);
            }
        }) .then(function(response) {
            console.log("check fee order when create: ",response);
            $scope.showNotification('Đặt hàng Thành công!','success')
        }).catch(function(error) {
            $scope.showNotification('Đặt Hàng Thất Bại!','error')
            console.error('Có lỗi xảy ra:', error);
        });
    }
    //tăng số lượng
    $scope.soLuongPlus = function (orderProduct){
        // var cart = JSON.parse(localStorage.getItem("cart")) || [];
        var product = $scope.itemsOrder.find(item=>item.idSanPhamChiTiet === orderProduct.idSanPhamChiTiet);
        if(product){
            // product.qty++;
            // product.soLuong++;
            product.soLuong = parseInt(product.soLuong) + 1;  // Chuyển thành số nếu cần thiết
            product.qty = parseInt(product.qty) + 1;  // Chuyển thành số nếu cần thiết
            this.saveToLocalStorage();
        }
    }
    //Giảm Số Lượng
    $scope.soLuongReduce = function(orderProduct){
        var product = $scope.itemsOrder.find(item=>item.idSanPhamChiTiet === orderProduct.idSanPhamChiTiet);
        if(product.soLuong >1){
            product.soLuong = parseInt(product.soLuong) - 1;
            product.qty = parseInt(product.qty) - 1;
            this.saveToLocalStorage();
        }
    }

    //giỏ hàng chi tiết
    $scope.removeLocalStorage = function (id){
        console.log('check delete: ',id)
        console.log('check delete: ',$scope.items)
        if (!$scope.username){
            var index = this.items.findIndex(item => item.sanPhamChiTiet.idSanPhamChiTiet == id);
            $scope.items.splice(index,1);
            this.saveToLocalStorage();
        }else {
            var item = this.items.find(item=>item.sanPhamChiTiet.idSanPhamChiTiet === id);
            console.log('check delete index: ',item)
            $http({
                method: 'DELETE',
                url: '/gio-hang-chi-tiet/xoa-theo-id-san-pham/' + item.sanPhamChiTiet.idSanPhamChiTiet
            }).then(function(response) {
                console.log("Đã xóa sản phẩm khỏi giỏ hàng:", response.data);
                $scope.getDetailCart(response.data.gioHang.idGioHang);
            }, function(error) {
                // Xử lý lỗi
                console.error("Lỗi khi xóa sản phẩm:", error.data);
                alert("Không tìm thấy sản phẩm hoặc có lỗi khi xóa!");
            });
        }
    }



    //load dữ liệu
    $scope.hideNotification();
    $scope.getProvinces();
    $scope.getUser();
})