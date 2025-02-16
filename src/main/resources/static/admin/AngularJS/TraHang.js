var app = angular.module("trahang-app", [])
app.controller("trahang-ctrl", function ($scope, $http,$interval,$sce, $timeout) {
    $scope.listDonHang = [];
    $scope.donHangChiTiet = [];
    $scope.idDonHang = null;
    $scope.idTrangThai = null;
    $scope.notification = {
        show: false,
        message: '',
        type: '',
        icon: ''
    };
    //phân trang
    $scope.currentPage = 1; // Trang hiện tại
    $scope.pageSize = 5;

    $scope.getAllOrder = function (){
        $http.get("/don-hang-tai-quay").then(function (response){
            $scope.listDonHang = response.data;
            console.log("get all order: ",response.data);
            $scope.totalPages = Math.ceil($scope.listDonHang.length / $scope.pageSize); // Tổng số trang
        }).catch(function (error){
            console.error("Có lỗi xảy ra: ",error);
            if (error.status === -1 || error.status === 500) { // Lỗi kết nối server
                console.log("Server không phản hồi. Dừng tự động kiểm tra.");
                $scope.stopAutoCheck();
            }
        })
    }
    var idDonHangShow = null;
    $scope.getOrderByID = function (id){
        $scope.startAutoCheck();
        $scope.idDonHang = id;
        $http.get("/don-hang-tai-quay/"+id).then(function (response) {
            $scope.donHangChiTiet = response.data;
            console.log("get all order Detail: ",response.data);
            var itemOrder = null
            var index =0;
            response.data.forEach((item, index) => {
                //console.log(`Order ${index} Detail:`, item.donHang);
                itemOrder = item.donHang;
                //console.log(`Order ${index+1} Detail:`, itemOrder);
            });
            idDonHangShow = itemOrder.idDonHang;
            if (response.data && itemOrder) {
                // $scope.idTrangThai = itemOrder.trangThai.idTrangThai;
                $('#tongHoaDon').val(itemOrder.tongTien);
                $('#tongTienKhuyenMai').val(itemOrder.tongTienKhuyenMai);
                if(itemOrder.trangThaiThanhToan){
                    $('#tienKhachThanhToan').val(itemOrder.tongTienSauKhuyenMai);
                }else {
                    $('#tienKhachThanhToan').val(0);
                }


                if (itemOrder.phuongThucNhan === 2) {
                    $('#phuongThucNhan').val("Vận Chuyển(Ship)");
                }

                if (itemOrder.phuongThucNhan === 1) {
                    $('#phuongThucNhan').val("Nhận Hàng Tại Quầy");
                }

                $('#trangThaiThanhToan').val(itemOrder.trangThaiThanhToan ? "Đã thanh toán" : "Chưa thanh toán");

                //khach mua
                $('#ten-khach-mua').val(itemOrder.khachHang.hoTen);
                $('#sdt-khach-mua').val(itemOrder.khachHang.soDienThoai);
                $('#emal-khach-mua').val(itemOrder.khachHang.email);
                $('#dia-chi-khach-mua').val(itemOrder.khachHang.diaChi);
                //khách nhân
                if (itemOrder.phuongThucNhan === 2) {
                    $('#ten-khach-nhan').val(itemOrder.tenKhachNhan);
                    $('#sdt-khach-nhan').val(itemOrder.soDienThoaiKhachNhan);
                    //$('#email-khach-nhan').val(itemOrder.khachHang.email);
                    $('#dia-chi-nhan').val(itemOrder.diaChiNhan);
                }
            } else {
                console.error("Dữ liệu donHang không tồn tại trong response.");
            }
            //$scope.showStatusOrder(response);

        }).catch(function (errors) {
            console.error("Có lỗi xảy ra: ",errors);
        })
    }

    //showModal
    $scope.showModalStatus = function (){
        $('#modal-status').modal('show');
    }

    $scope.statusOrder = [1, 7, 2, 3, 5];
    $scope.currentStatus = 1;

    $scope.updateOrderStatus = function (idTrangThai){
        const currentIndex = $scope.statusOrder.indexOf($scope.currentStatus);
        const newIndex = $scope.statusOrder.indexOf(idTrangThai);
        // Kiểm tra nếu trạng thái mới nằm trước trạng thái hiện tại
        if (newIndex <= currentIndex) {
            $scope.showNotification('Không thể quay lại trạng thái trước hoặc cập nhật trạng thái hiện tại!', 'error');
            return;
        }

        // Nếu trạng thái hiện tại đã là 5, không cho phép cập nhật
        if ($scope.currentStatus === 5) {
            $scope.showNotification('Không thể cập nhật vì đơn hàng đã hoàn thành!', 'error');
            return;
        }

        var chichu = $('#ghi-chu').val();
        $scope.dataStatus ={
            idDonHang: $scope.idDonHang,
            idTrangThai: idTrangThai,
            ghiChu: chichu
        }
        console.log("status after update $scope.dataStatus: ",$scope.dataStatus);
        var orderStatus = angular.copy($scope.dataStatus);
        $http({
            method: 'PUT',
            url: '/don-hang-tai-quay/cap-nhat-trang-thai',
            data: orderStatus,
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: function(data) {
                return JSON.stringify(data);  // Chuyển đối tượng thành chuỗi JSON
            }
        }) .then(function(response) {
            console.log("status after update: ",response.data);
            //$scope.showStatusOrderAfterUpdate(response);
            $scope.currentStatus = idTrangThai;
            $scope.getAllOrder();
            $scope.showNotification('Cập Nhật trạng Thái Thành công!','success');
            $('#modal-status').modal('hide');
        }).catch(function(errors) {
            console.error("Có lỗi xảy ra: ",errors);
            $scope.showNotification('Cập Nhật trạng Thái Thất Bại!','error');
        });
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

    // var intervalPromise = $interval(checkTrangThai, 3000); // Lưu tham chiếu interval

    var intervalPromise;
    $scope.startAutoCheck = function() {
        // Khởi động interval khi nhấn nút
        if (!intervalPromise) {
            intervalPromise = $interval(checkTrangThai, 2000); // Lưu tham chiếu interval
            console.log("Đã bắt đầu tự động kiểm tra trạng thái.");
        }
    };
    $scope.stopAutoCheck = function() {
        // Dừng interval khi trạng thái đạt 5
        if (intervalPromise) {
            $interval.cancel(intervalPromise);
            intervalPromise = null; // Đặt lại tham chiếu interval
            console.log("Đã dừng tự động kiểm tra trạng thái.");
        }
    };

    function checkTrangThai() {
        if(idDonHangShow ===null){
            $scope.showActive(1);
        }else {
            $http.get('/api/getTrangThai/' +idDonHangShow)  // Gọi API để lấy trạng thái mới
                .then(function(response) {
                    // Cập nhật idTrangThai từ phản hồi server
                    const newTrangThai = response.data.trangThai.idTrangThai;
                    $scope.currentStatus = response.data.trangThai.idTrangThai ;
                    // console.log("newTrangThai",newTrangThai);
                    // console.log("newTrangThai",$scope.idTrangThai);
                    // Chỉ cập nhật giao diện nếu trạng thái thay đổi
                    if ($scope.idTrangThai !== newTrangThai) {
                        console.log("check.....")
                        if(newTrangThai ===1){
                            console.log(newTrangThai)
                            $scope.showActive(1);
                        }
                        if(newTrangThai ===7){
                            console.log(newTrangThai)
                            $scope.showActive(2);
                        }
                        if(newTrangThai ===2){
                            console.log(newTrangThai)
                            $scope.showActive(3);
                        }
                        if(newTrangThai ===3){
                            console.log(newTrangThai)
                            $scope.showActive(4);
                        }
                        if(newTrangThai ===5){
                            console.log("Trạng thái đạt 5, dừng tự động!");
                            $scope.showActive(5);
                            $scope.stopAutoCheck ();  // Dừng interval
                        }
                    }
                })
                .catch(function(error) {
                    console.error("Có lỗi khi lấy trạng thái", error);
                });
        }
    }

    $scope.showActive = function(idTrangThai) {
        $(".step").removeClass("active");
        for (let i = 1; i <= idTrangThai; i++) {
            $("#" + "step-" + i).addClass("active");
            // console.log("check:....","#" + "step-" + i)// Thêm class active cho các bước từ 1 đến idTrangThai
        }
    };


    //ẩn trạng thái
    $scope.hideStatusOrder = function (){
        $('#step-6').hide();
    }

    //phân trang
    $scope.getPagedProducts = function () {
        const start = ($scope.currentPage - 1) * $scope.pageSize;
        const end = start + $scope.pageSize;
        return $scope.listDonHang.slice(start, end); // Lấy danh sách đơn hàng cho trang hiện tại
    };

    // Chuyển đến trang khác
    $scope.setPage = function (page) {
        if (page >= 1 && page <= $scope.totalPages) {
            $scope.currentPage = page;
        }
    };

    $scope.getPaginationRange = function () {
        const rangeSize = 5; // Số lượng trang muốn hiển thị (mặc định là 5)
        let start = Math.max($scope.currentPage - Math.floor(rangeSize / 2), 1);
        const end = Math.min(start + rangeSize - 1, $scope.totalPages);

        // Điều chỉnh lại nếu các trang bị vượt giới hạn
        start = Math.max(Math.min(start, $scope.totalPages - rangeSize + 1), 1);

        const pages = [];
        for (let i = start; i <= end; i++) {
            pages.push(i);
        }
        return pages;
    };

    $scope.isPaginationVisible = function () {
        return $scope.listDonHang.length > $scope.pageSize;
    };

    //filter
    var inputData = null;
    $scope.searchMa = function (){
        $scope.stopAutoCheck();
        var maHD = $('#maHoaDon').val().trim();
        var regex = /^[a-zA-Z0-9]+$/; // Chỉ cho phép chữ cái và số
        if (!regex.test(maHD)) {
            $scope.showNotification('Mã hóa đơn không hợp lệ! Vui lòng nhập chỉ chữ và số.','error');
            return; // Dừng lại nếu phát hiện ký tự đặc biệt
        }
        $http({
            method: 'GET',
            url: '/don-hang/tim-kiem-ma-don-hang', // URL cơ bản
            params: { maHD: maHD } // Truyền trực tiếp tham số
        }).then(function (response) {
            if(!response || !response.data || response.data.length === 0){
                $scope.getAllOrder();
            }else {
                $scope.listDonHang = response.data;
                console.log("get all order search: ",response.data);
                $scope.totalPages = Math.ceil($scope.listDonHang.length / $scope.pageSize); // Tổng số trang
            }
        }).catch(function (error) {
            console.error("Có lỗi khi lấy trạng thái", error);
        })
    }
    $scope.selectedLoaiDon = null;
    $scope.searchLoaiDonHang = function (){
        $scope.stopAutoCheck();
        var loaiDonHang = $scope.selectedLoaiDon; // Lấy giá trị từ ng-model
        if (!loaiDonHang || loaiDonHang.length === 0) {
            $scope.getAllOrder();
        }
        // if (!loaiDonHang) {
        //     $scope.showNotification("Vui lòng chọn loại đơn", "error");
        //     return;
        // }
        $http({
            method: 'GET',
            url: '/don-hang/tim-kiem-loai-don-hang', // URL cơ bản
            params: { loaiDonHang: loaiDonHang } // Truyền trực tiếp tham số
        }).then(function (response) {
            // if(response && response.data === null){
            //     $scope.getAllOrder();
            // }else {
            //
            // }
            $scope.listDonHang = response.data;
            console.log("get all order search: ",response.data);
            $scope.totalPages = Math.ceil($scope.listDonHang.length / $scope.pageSize); // Tổng số trang
        }).catch(function (error) {
            console.error("Có lỗi khi lấy trạng thái", error);
        })
    }

    $scope.ngayBatDau = null;
    $scope.ngayKetThuc  =null;
    flatpickr("#ngayBatDau", {
        dateFormat: "d/m/Y",
        // minDate: "today", // Chỉ cho phép chọn ngày từ hôm nay trở đi
        // disableMobile: true, // Vô hiệu hóa datepicker trên thiết bị di động (nếu bạn muốn sử dụng giao diện riêng cho di động)
    });
    // Khởi tạo Flatpickr cho trường Ngày kết thúc
    flatpickr("#ngayKetThuc", {
        dateFormat: "d/m/Y",  // Định dạng ngày hiển thị
    });
    $scope.searchByDateRange = function () {
        $scope.stopAutoCheck();
        // Lấy giá trị từ các trường nhập liệu
        const ngayBatDau = document.getElementById("ngayBatDau").value.trim();
        const ngayKetThuc = document.getElementById("ngayKetThuc").value.trim();

        // Biểu thức kiểm tra định dạng ngày dd/MM/yyyy
        const dateRegex = /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[012])\/\d{4}$/;

        // Kiểm tra định dạng ngày
        if (ngayBatDau && !dateRegex.test(ngayBatDau)) {
            $scope.showNotification("Ngày bắt đầu không đúng định dạng dd/MM/yyyy", "error");
            return;
        }
        if (ngayKetThuc && !dateRegex.test(ngayKetThuc)) {
            $scope.showNotification("Ngày kết thúc không đúng định dạng dd/MM/yyyy", "error");
            return;
        }

        // Kiểm tra thứ tự ngày bắt đầu và ngày kết thúc
        if (ngayBatDau && ngayKetThuc) {
            const startDate = new Date(ngayBatDau.split("/").reverse().join("-"));
            const endDate = new Date(ngayKetThuc.split("/").reverse().join("-"));
            if (startDate > endDate) {
                $scope.showNotification("Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc", "error");
                return;
            }
        }

        // Chuyển đổi ngày thành định dạng phù hợp với API
        const params = {
            ngayBatDau: ngayBatDau || null,
            ngayKetThuc: ngayKetThuc || null,
        };

        // Gửi yêu cầu tìm kiếm
        $http({
            method: "GET",
            url: "/don-hang/tim-kiem-theo-ngay",
            params: params,
        }).then(function (response) {
            if (response.data && response.data.length > 0) {
                $scope.listDonHang = response.data;
                console.log("Kết quả tìm kiếm:", response.data);

                // Tính tổng số trang dựa trên dữ liệu trả về
                $scope.totalPages = Math.ceil($scope.listDonHang.length / $scope.pageSize);
            } else {
                $scope.showNotification("Không tìm thấy đơn hàng trong khoảng thời gian này", "info");
                $scope.listDonHang = [];  // Nếu không có kết quả, đảm bảo mảng danh sách trống
                $scope.totalPages = 0;  // Cập nhật lại số trang
            }
        }).catch(function (error) {
            console.error("Lỗi trong quá trình tìm kiếm:", error);
            $scope.showNotification("Có lỗi xảy ra trong quá trình tìm kiếm", "error");
        });
    };

    //lấy tất cả trạng thái
    $scope.listStatus = [];
    $scope.getAllStatus = function (){
        $http.get("/don-hang/lay-trang-thai").then(function (response) {
            $scope.listStatus = response.data;
            console.log("check $scope.listStatus: ",$scope.listStatus);
        }).catch(function (errors) {
            console.error("Có lỗi xảy ra trong quá trình ",errors);
            $scope.showNotification("Có lỗi xảy ra trong quá trình ", "error");
        })
    }

    $scope.selectedTrangThai = null;
    $scope.searchTrangThai = function (){
        $scope.stopAutoCheck();
        var idTrangThai = $scope.selectedTrangThai; // Lấy giá trị từ ng-model
        if (!idTrangThai || idTrangThai.length === 0) {
            $scope.getAllOrder();
        }
        $http({
            method: 'GET',
            url: '/don-hang/tim-kiem-trang-thai-don-hang', // URL cơ bản
            params: { idTrangThai: idTrangThai } // Truyền trực tiếp tham số
        }).then(function (response) {
            if(response && response.data === null){
                $scope.getAllOrder();
            }else {
                $scope.listDonHang = response.data;
                console.log("get all order search: ",response.data);
                $scope.totalPages = Math.ceil($scope.listDonHang.length / $scope.pageSize); // Tổng số trang
            }
        }).catch(function (error) {
            console.error("Có lỗi khi lấy trạng thái", error);
        })
    }

    //load data
    $scope.hideStatusOrder();
    $scope.getAllOrder();
    $scope.getAllStatus();

    var intervalPromiseDH; // Biến quản lý $interval
    var controlTimeout;    // Biến quản lý $timeout
    var intervalTime = 1500; // Thời gian lặp lại $interval
    var pauseTime = 3000;   // Thời gian tạm dừng $interval
    var resumeTime = 2000;  // Thời gian để khởi động lại
    // intervalPromiseDH = $interval(function() {
    //     $scope.getAllOrder();
    // }, 1500);
    $scope.isAutoCheckRunning = false;
    $scope.startAutoCheckOrder = function() {
        $scope.isAutoCheckRunning = true;
        // Hàm quản lý chu kỳ chạy và dừng
        function manageInterval() {
            if (!$scope.isAutoCheckRunning) {
                console.log("Tự động kiểm tra đã bị dừng.");
                return;
            }
            // Khởi động $interval nếu chưa có
            if (!intervalPromiseDH) {
                intervalPromiseDH = $interval(function() {
                    $scope.getAllOrder();
                    console.log("Đang kiểm tra danh sách đơn hàng...");
                }, intervalTime); // Sử dụng thời gian lặp lại được cấu hình
                console.log("Đã bắt đầu tự động kiểm tra danh sách đơn hàng.");
            }

            // Tạm dừng sau pauseTime
            $timeout(function() {
                if (intervalPromiseDH) {
                    $interval.cancel(intervalPromiseDH);
                    intervalPromiseDH = null;
                    console.log("Tạm dừng tự động kiểm tra sau " + pauseTime + "ms.");
                }
                // Khởi động lại sau resumeTime nếu cần
                if ($scope.isAutoCheckRunning) {
                    controlTimeout = $timeout(manageInterval, resumeTime);
                }
                // Khởi động lại sau resumeTime
                //controlTimeout = $timeout(manageInterval, resumeTime); // Sử dụng thời gian khởi động lại được cấu hình
            }, pauseTime); // Sử dụng thời gian tạm dừng được cấu hình
        }

        // Bắt đầu chu kỳ
        manageInterval();
    };
    $scope.startAutoCheckOrder();

    $scope.stopAutoCheck = function (){
        console.log("Hàm stopAutoCheck được gọi.");
        $scope.isAutoCheckRunning = false;
        if (intervalPromiseDH) {
            $interval.cancel(intervalPromiseDH);
            intervalPromiseDH = null;
            console.log("Đã dừng $interval khi chuyển trang.");
        }
        if (controlTimeout) {
            $timeout.cancel(controlTimeout);
            controlTimeout = null;
            console.log("Đã dừng $timeout khi chuyển trang.");
        }
    }
    // Hủy $interval và $timeout khi controller bị hủy
    $scope.$on('$destroy', function() {
        if (intervalPromiseDH) {
            $interval.cancel(intervalPromiseDH);
            intervalPromiseDH = null;
            console.log("Đã dừng $interval khi chuyển trang.");
        }
        if (controlTimeout) {
            $timeout.cancel(controlTimeout);
            controlTimeout = null;
            console.log("Đã dừng $timeout khi chuyển trang.");
        }
    });




});
