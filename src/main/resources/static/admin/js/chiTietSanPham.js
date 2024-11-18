var app = angular.module('chiTietSP-app', []);
app.controller('chiTietSP-ctrl', function ($scope, $http) {

    $scope.items = [];
    $scope.page = 0;  // Trang hiện tại
    $scope.size = 4; // Số lượng bản ghi trên mỗi trang
    $scope.totalPages = 0; // Tổng số trang
    $scope.pageInput = 1; // Giá trị nhập từ ô input
    $scope.filterData = {};

    const pathName = window.location.pathname.split('/');
    var idSanPham = pathName[pathName.length - 1];

    $scope.findAll = function () {
        var url = `/admin/san-pham/` + idSanPham + `/find-all?page=${$scope.page}&size=${$scope.size}`;
        $http.get(url).then(resp => {
            $scope.items = resp.data.content;
            $scope.totalPages = resp.data.totalPages; // Cập nhật tổng số trang
        }).catch(error => {
            console.log(error);
        });
    };

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

    $scope.getAll = function () {
        var url = `/admin/san-pham/{idSanPham}/get-all`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;
        }).catch(error => {
            console.log(error);
        });
    };

    $scope.findAll();
    // Hàm chuyển tới trang trước
    $scope.previousPage = function () {
        if ($scope.page > 0) {
            $scope.page--;
            // Kiểm tra nếu có bộ lọc, gọi lại filter, nếu không gọi findAll
            if (Object.keys($scope.filterData).length > 0) {
                $scope.filter($scope.filterData); // Lọc với dữ liệu hiện tại
            } else {
                $scope.findAll(); // Nếu không lọc, lấy tất cả sản phẩm
            }
        }
    };

// Hàm chuyển tới trang sau
    $scope.nextPage = function () {
        if ($scope.page < $scope.totalPages - 1) {
            $scope.page++;
            // Kiểm tra nếu có bộ lọc, gọi lại filter, nếu không gọi findAll
            if (Object.keys($scope.filterData).length > 0) {
                $scope.filter($scope.filterData); // Lọc với dữ liệu hiện tại
            } else {
                $scope.findAll(); // Nếu không lọc, lấy tất cả sản phẩm
            }
        }
    };

    $scope.getSanPhamChiTiet = function (ma) {
        var url = "/admin/san-pham/chi-tiet/view" + "/" + ma;
        console.log(url)
        $http.get(url).then(function (r) {
            console.log(r.data)
            $scope.spct = r.data;
            // console.log("check:",$scope.spct.idMauSac);
        })
    }

    $scope.update = function (ma) {
        var url = "/admin/san-pham/chi-tiet/update" + "/" + ma;
        var updateSPCT = {
            ma: ma,
            soLuong: $scope.spct.soLuong,
            giaBan: $scope.spct.giaBan,
            idMauSac: $scope.spct.idMauSac.idMauSac,
            idThuongHieu: $scope.spct.idThuongHieu.idThuongHieu,
            idKieuDang: $scope.spct.idKieuDang.idKieuDang,
            idChatLieu: $scope.spct.idChatLieu.idChatLieu,
            idKichCo: $scope.spct.idKichCo.idKichCo,
            idXuatXu: $scope.spct.idXuatXu.idXuatXu,
        }
        $http.post(url, updateSPCT).then(function (r) {
            alert("Update thành công");
            console.log($scope.spct)
            $scope.findAll();
        }).catch(function (err) {
            console.log("Update khong thanh cong", err);
        })
    }

    $scope.updateTT = function (idSanPhamChiTiet) {
        if (confirm("Xác nhận đổi?")) {
            var url = "/admin/san-pham/chi-tiet/updateTT" + "/" + idSanPhamChiTiet;
            $http.post(url).then(function (r) {
                alert("Doi thành công!!!")
                $scope.findAll();
            }).catch(function (err) {
                console.log("Loi: ", err);
            })
        }
    }


    $scope.navigateToForm = function () {
        window.location.href = '/admin/san-pham/formAdd/' + idSanPham;
    };


    // Hàm lọc sản phẩm
    $scope.filter = function (filterData) {
        // Loại bỏ các thuộc tính không hợp lệ (rỗng/null)
        for (const [key, value] of Object.entries(filterData)) {
            if (!value || value.length === 0) {
                delete filterData[key];
            }
        }

        filterData.idSanPham = idSanPham;
        console.log("Dữ liệu lọc: ", filterData);

        // Gửi yêu cầu lọc đến server với phân trang
        $http.post(`/admin/san-pham/chi-tiet/filter?page=${$scope.page}&size=${$scope.size}`, filterData).then(function (response) {
            $scope.items = response.data.content; // Gán danh sách sản phẩm sau khi lọc
            $scope.totalPages = response.data.totalPages; // Tổng số trang
            $scope.pageNumber = 0; // Reset lại trang hiện tại sau khi lọc
            console.log("Dữ liệu lọc: ", $scope.items);

            // Hiển thị số bộ lọc đang được áp dụng
            if (Object.keys(filterData).length > 1) {
                document.getElementById('lengthFilter').innerText = Object.keys(filterData).length-1;
            } else {
                document.getElementById('lengthFilter').innerText = "";
            }

        }).catch(function (error) {
            console.error("Lỗi khi lọc sản phẩm:", error);
            alertify.error("Không thể lọc sản phẩm. Vui lòng thử lại sau!");
        });
    };


    // Hàm xóa bộ lọc
    $scope.clearFilter = function () {
        $scope.filterData = {}; // Reset dữ liệu lọc
        document.getElementById('lengthFilter').innerText = "";
        // $scope.filter($scope.filterData); // Gọi lại hàm lọc để làm mới danh sách
        $scope.findAll();
    };

    // Hàm lấy danh sách số trang (nếu muốn phân trang)
    $scope.getPageNumbers = function (totalPages) {
        $scope.pageNumbers = Array.from({length: totalPages}, (_, i) => i + 1);
    };

    // Hàm chuyển trang (phân trang)
    $scope.changePage = function (page) {
        $scope.pageNumber = page;
        const filterDataWithPage = {...$scope.filterData, page};
        $scope.filter(filterDataWithPage);
    };


//     const pathName = window.location.pathname.split('/');
//     const idSP = pathName[pathName.length - 1]
//
//     $scope.items =[];
//     $scope.form ={
//         sanPham : idSP
//     };
//     $scope.sizes = [];
//     $scope.itemUpdate = {};
//
//     $scope.getAll = function (){
//         $http.get("/admin/san-pham/"+idSP+"/get-all").then(r => {
//             $scope.items = r.data;
//         }).catch(e => console.log(e))
//     }
//     $scope.getSizes = function (){
//         $http.get("/admin/san-pham/"+idSP+"/test").then(r => {
//             $scope.sizes = r.data;
//         }).catch(e => console.log(e))
//     }
//     $scope.getAll();
//     $scope.getSizes();
//
//
//     // $scope.delete = function (ma){
//     //     $http.delete("/admin/san-pham/delete/"+ma).then(r => {
//     //         var index = $scope.items.findIndex(i => i.ma == ma);
//     //         console.log(index)
//     //         $scope.items.splice(index,1);
//     //         // $scope.getAll();
//     //     }).catch(e => console.log(e));
//     // }
//
//     $scope.getChiTietSP = function (ma){
//         location.href = "/admin/san-pham/"+ma;
//     }
//
//     //Thêm
//     $scope.add = function (){
//
//         let data =[];
//         let sizesInForm = $scope.form.sizes
//         if(sizesInForm == undefined) sizesInForm = [];
//
//             $http.post("/admin/san-pham/"+idSP+"/add?sizes="+sizesInForm,{
//                 soLuong : $scope.form.soLuong
//             }).then(r =>{
//                 $scope.removeSizeInForm(sizesInForm);
//                 $scope.items = $scope.items.concat(r.data);
//                 $scope.form.soLuong = ""
//                 alertify.success("Thêm thành công "+sizesInForm.length+" chi tiết sản phẩm")
//             }).catch(e => {
//                 document.getElementById("eSize").innerText = e.data.eSize == undefined ? "" : e.data.eSize
//                 document.getElementById("eSoLuong").innerText =  e.data.soLuong == undefined ? "" : e.data.soLuong
//                 console.log(e)
//                 alertify.error("Thêm thất bại")
//             })
//     }
//
//     //Xóa
//     $scope.delete = function (item){
//         console.log(item)
//         alertify.confirm("Xóa chi tiết sản phẩm size "+item.size, function () {
//             $http.delete("/admin/san-pham/"+idSP+"/delete/"+item.id).then(r => {
//                 let index = $scope.items.findIndex(i => i.id == item.id);
//                 $scope.items.splice(index,1);
//                 $scope.getSizes();
//                 alertify.success("Xóa thành công chi tiết sản phẩm size "+item.size);
//             }).catch(e => {
//                 alertify.error("Xóa thất bại")
//                 console.log(e);
//             })
//
//         }, function () {
//             alertify.error("Xóa thất bại")
//         })
//     }
//
//     //Cập nhật
//     $scope.viewUpdate = function (item){
//         $scope.itemUpdate = angular.copy(item)
//         console.log($scope.itemUpdate)
//     }
//     $scope.update = function (){
//             alertify.confirm("Cập nhật chi tiết sản phẩm size "+$scope.itemUpdate.size+" ?", function () {
//                 // $scope.itemUpdate.sanPham = idSP
//                 $http.put("/admin/san-pham/"+idSP+"/update",$scope.itemUpdate).then(r =>{
//                     let index = $scope.items.findIndex(i => i.id == $scope.itemUpdate.id)
//                     $scope.items[index] = r.data
//                     console.log("cl", $("#cancelModal").click())
//                     alertify.success("Cập nhật thành công số lượng size "+$scope.itemUpdate.size)
//                 }).catch(e => {
//                     document.getElementById("eSoLuongUpdate").innerText = e.data.soLuong
//                     console.log(e)
//                     alertify.error("Cập nhật thất bại")
//                 })
//             }, function () {
//                 alertify.error("Cập nhật thất bại")
//             })
//     }
//     $scope.updateSlInTable = function (soLuong,id){
//         console.log(id)
//         $scope.itemUpdate.id = id
//         $scope.itemUpdate.soLuong = parseInt(soLuong);
//         console.log($scope.itemUpdate)
//         $http.put("/admin/san-pham/"+idSP+"/update",$scope.itemUpdate).then(r =>{
//             alertify.success("Cập nhật số lượng thành công")
//         }).catch(e => {
//             $http.get("/admin/san-pham/"+idSP+"/getSoLuong/"+id).then(r => {
//                 document.getElementById(id+"").value = r.data.soLuong
//             })
//             alertify.error(e.data.soLuong)
//         })
//     }
//
//     $scope.removeSizeInForm = function (size){
//         for (let i = 0 ;i< size.length;i++){
//             let index = $scope.sizes.findIndex(s => s.ma == size[i])
//             $scope.sizes.splice(index,1);
//         }
//     }
//     $scope.removeER = function (id){
//         document.getElementById(id).innerText = "";
//     }
//
//     $scope.selectAllSize = function (){
//         let elm = document.getElementById("sizeSL")
//         if(elm.selectedIndex == 0) {
//             elm.options[0].selected = false;
//             console.log(elm.options[0].selected)
//             for (let i = 0; i < elm.options.length; i++) {
//                 elm.options[i].selected = true;
//             }
//         }
//     }
// });
// $( '#sizeSL' ).select2( {
//     theme: "bootstrap-5",
//     width: $( this ).data( 'width' ) ? $( this ).data( 'width' ) : $( this ).hasClass( 'w-100' ) ? '100%' : 'style',
//     placeholder: $( this ).data( 'placeholder' ),
//     closeOnSelect: false,
//     allowClear: true,
});





