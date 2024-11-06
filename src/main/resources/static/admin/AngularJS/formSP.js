var app = angular.module("formSP-app", [])
app.controller("san-pham-ctrl", function ($scope, $http) {

    $scope.mauSac=[];
    $scope.chatLieu=[];
    $scope.thuongHieu=[];
    $scope.xuatXu=[];
    $scope.kieuDang=[];
    $scope.kichCo=[];
    $scope.selectedMauSac = [];
    $scope.selectedKichCo = [];

    $scope.getThuocTinh = function () {
        $http.get("/admin/mau-sac/get-all").then(r => {
            $scope.mauSac = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/chat-lieu/get-all").then(r => {
            $scope.chatLieu = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/thuong-hieu/get-all").then(r => {
            $scope.thuongHieu = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/xuat-xu/get-all").then(r => {
            $scope.xuatXu = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/kieu-dang/get-all").then(r => {
            $scope.kieuDang = r.data;
        }).catch(e => console.log(e))

        $http.get("/admin/size/get-all").then(r => {
            $scope.kichCo = r.data;
        }).catch(e => console.log(e))
    }

    $scope.getThuocTinh();



    $scope.tables = []; // Mảng chứa các bảng màu với các kích thước tương ứng

// Khi chọn màu
    $scope.selectColor = function () {
        $scope.selectedMauSac.forEach(function(mau) {
            // Kiểm tra xem màu đã có trong tables chưa
            let existingTable = $scope.tables.find(t => t.mau.ten === mau.ten);

            // Nếu chưa tồn tại, thêm màu mới với tất cả kích cỡ hiện có từ các bảng trong tables
            if (!existingTable) {
                // Tạo một danh sách kích cỡ mới bằng cách hợp nhất tất cả các kích cỡ hiện có trong các bảng
                let allSizes = [];
                $scope.tables.forEach(table => {
                    table.size.forEach(size => {
                        if (!allSizes.some(s => s.ten === size.ten)) {
                            allSizes.push(size); // Thêm kích cỡ nếu chưa tồn tại trong allSizes
                        }
                    });
                });

                // Thêm màu mới với tất cả kích cỡ hiện có vào tables
                $scope.tables.push({ mau: mau, size: allSizes });
            }
        });
    };


// Khi chọn kích cỡ
    $scope.selectSize = function () {
        $scope.selectedKichCo.forEach(function(size) {
            // Duyệt qua từng bảng màu hiện có và thêm kích cỡ vào từng bảng
            $scope.tables.forEach(function(table) {
                // Kiểm tra kích cỡ đã tồn tại trong bảng màu hiện tại chưa
                if (!table.size.some(s => s.ten === size.ten)) {
                    table.size.push(size); // Thêm kích cỡ nếu chưa có
                }
            });
        });
    };



    // var httpThuocTinh = "";
    // var thuocTinhSL = undefined;
    // var filesTransfer = new DataTransfer();
    // var checkViewModal = false;
    // $scope.tenThuocTinh = ""
    // $scope.er = {}
    // document.getElementById("pro-image").files = filesTransfer.files
    //
    // $("#viewAdd").on('hide.bs.modal', function () {
    //     if (checkViewModal == false) thuocTinhSL.selectedIndex = 0;
    //     $scope.tenThuocTinh = "";
    //     document.getElementById("viewAddThuongHieu").style.display = "none"
    // });
    //
    // $scope.viewAddMauSac = function () {
    //     httpThuocTinh = "/admin/mau-sac/add"
    //     thuocTinhSL = document.getElementById("mauSac");
    //
    //     if (thuocTinhSL.selectedIndex == thuocTinhSL.length - 1) {
    //         $('#viewAdd').modal('show');
    //         checkViewModal = false
    //     }
    // }
    // $scope.viewAddXuatXu = function () {
    //     httpThuocTinh = "/admin/xuat-xu/add"
    //     thuocTinhSL = document.getElementById("xuatXu");
    //
    //     if (thuocTinhSL.selectedIndex == thuocTinhSL.length - 1) {
    //         $('#viewAdd').modal('show');
    //         checkViewModal = false
    //     }
    // }
    // $scope.viewAddDongSP = function () {
    //     httpThuocTinh = "/admin/dong-san-pham/add"
    //     thuocTinhSL = document.getElementById("dongSP");
    //
    //     if (thuocTinhSL.selectedIndex == thuocTinhSL.length - 1) {
    //         document.getElementById("viewAddThuongHieu").style.display = "inline-block"
    //         $('#viewAdd').modal('show');
    //         checkViewModal = false
    //     }
    // }
    //
    // $scope.viewAddKieuDang = function () {
    //     httpThuocTinh = "/admin/kieu-dang"
    //     thuocTinhSL = document.getElementById("kieuDang");
    //
    //     if (thuocTinhSL.selectedIndex == thuocTinhSL.length - 1) {
    //         $('#viewAdd').modal('show');
    //         checkViewModal = false
    //     }
    // }
    //
    // $scope.viewAddChatLieu = function () {
    //     httpThuocTinh = "/admin/chat-lieu/add"
    //     thuocTinhSL = document.getElementById("chatLieu");
    //
    //     if (thuocTinhSL.selectedIndex == thuocTinhSL.length - 1) {
    //         $('#viewAdd').modal('show');
    //         checkViewModal = false
    //     }
    // }
    // $scope.addThuocTinh = function () {
    //     if (thuocTinhSL.id == "dongSP"){
    //         if($scope.thuongHieu == undefined){
    //             document.getElementById('erAddDongSP').innerText = "Vui lòng chọn thương hiệu"
    //             return;
    //         }
    //     }
    //     if($scope.tenThuocTinh==undefined || $scope.tenThuocTinh.length==0){
    //         document.getElementById('etenThuocTinh').innerText = "Vui lòng nhập tên"
    //         return
    //     }
    //     if($scope.tenThuocTinh.length>100){
    //         document.getElementById('etenThuocTinh').innerText = "Tên tối đa 100 ký tự"
    //         return
    //     }
    //
    //     $http.post(httpThuocTinh, {thuongHieu: $scope.thuongHieu, ten: $scope.tenThuocTinh}).then(r => {
    //         // document.getElementById("viewAddThuongHieu").style.display = "none"
    //         // if (thuocTinhSL.id == "dongSP") $scope.addOtpInDongSP(r.data)
    //         // else {
    //             var option = document.createElement("option");
    //             option.text = r.data.ten;
    //             option.value = r.data.id == undefined ? r.data.ma : r.data.id
    //             thuocTinhSL.add(option, thuocTinhSL[thuocTinhSL.length - 1]);
    //             thuocTinhSL.value = option.value;
    //         // }
    //
    //         $scope.tenThuocTinh = "";
    //         checkViewModal = true
    //         $('#viewAdd').modal('hide');
    //         alertify.success("Thêm thành công")
    //     }).catch(e => {
    //         alertify.error("Thêm thất bại")
    //         console.log(e)
    //     })
    //
    // }
    // $scope.addOtpInDongSP = function (data) {
    //     let otpGroup = document.getElementById($scope.thuongHieu+"");
    //     console.log(otpGroup)
    //     console.log(data)
    //     let option = document.createElement("option");
    //     option.setAttribute("value",data.id);
    //     option.innerHTML=data.ten;
    //     // option.text = data.ten;
    //     // option.value = data.id;
    //     otpGroup.append(option)
    //     thuocTinhSL.value = option.value;
    //
    // }
    //
    //
    // $scope.removeER = function (id) {
    //     document.getElementById(id).innerText = "";
    // }
    // $scope.closeModal = function () {
    //     $('#viewAdd').modal('hide');
    // }
    //
    // $scope.appendFile = function () {
    //     $scope.removeER('erImg')
    //     let files = document.getElementById("pro-image").files
    //     console.log(files.length + filesTransfer.files.length)
    //     if(files.length + filesTransfer.files.length > 5){
    //         document.getElementById("erImg").innerText = "Sản phẩm chỉ tối đa 5 ảnh"
    //         return
    //     }
    //
    //     files.forEach(f => {
    //         if(f.size > 1 * 1024 * 1024){
    //             document.getElementById("erImg").innerText = "Kích thước tối đa của ảnh là 1mb"
    //             return
    //         }
    //     })
    //
    //     document.getElementById("erImg").innerText = ""
    //     files.forEach(f => filesTransfer.items.add(f))
    //     // document.getElementById("pro-image").files = filesTransfer.files
    // }
    // $scope.removeFile = function (key) {
    //     $scope.removeER('erImg')
    //     let index;
    //     let files1 = new DataTransfer();
    //     filesTransfer.files.forEach(f => {
    //         if (f.lastModified != key) {
    //             files1.items.add(f);
    //         }
    //     })
    //
    //     filesTransfer = files1
    //     // document.getElementById("pro-image").files = filesTransfer.files
    // }
    // $scope.loadImgProduct = function (fileName) {
    //     const image = new File([fileName], fileName, {
    //         lastModified: new Date(),
    //     });
    //     let buttonCancel = document.getElementById(fileName).getElementsByClassName('image-cancel')
    //     buttonCancel[0].setAttribute("id", image.lastModified);
    //     console.log(buttonCancel)
    //
    //     filesTransfer.items.add(image);
    //     document.getElementById("pro-image").files = filesTransfer.files
    // }
    // $scope.sortFiles = function () {
    //     console.log(document.getElementsByClassName("image-cancel"))
    //
    //     let indexs = []
    //
    //     let files1 = new DataTransfer();
    //     document.getElementsByClassName("image-cancel").forEach(item => {
    //         console.log(item)
    //         for(let i = 0;i<filesTransfer.files.length ;i++)
    //             if (filesTransfer.files[i].lastModified == item.id && indexs.includes(i)==false) {
    //                 indexs.push(i)
    //                 files1.items.add(filesTransfer.files[i]);
    //             }
    //     })
    //     filesTransfer = files1
    //     document.getElementById("pro-image").files = filesTransfer.files
    //     $scope.check()
    //
    //     // filesTransfer = new DataTransfer();
    //     // document.getElementById("pro-image").files = filesTransfer.files
    //
    // }
    // $scope.check = function (){
    //     console.log(filesTransfer.files, document.getElementById("pro-image").files)
    // }

})

