var app = angular.module("mau-sac", [])
app.controller("mau-sac-ctrl", function ($scope, $http) {

    $scope.form = {}
    $scope.items = []

    $scope.findAll = function () {
        $http.get("/admin/mau-sac/find-all").then(resp => {
            console.log(resp.data)
            $scope.items = resp.data;
        }).catch(error => {
            console.log(error)
        })
    }

    $scope.findAll();

    $scope.create = function () {
        var mauSac = {
            ten: $scope.ten
        }
        if ($scope.ten == undefined || $scope.ten.length == 0) {
            document.getElementById("eTenMau").innerText = "Vui lòng nhập tên!!!";
            return
        }
        if ($scope.ten.length > 100) {
            document.getElementById("eTenMau").innerText = "Tên tối đa 100 ký tự!!!";
            return
        } else {
            $http.post("/admin/mau-sac/add", mauSac).then(r => {
                location.reload();
                alert("Thêm thành công")
            }).cath(function (err){
                console.log("Them khong thanh cong", err);
            })
        }
    }


    $scope.getMauSac = function (idMauSac) {
        var url = "/admin/mau-sac/chiTiet" + "/" + idMauSac;
        console.log(url)
        $http.get(url).then(function (r) {
            console.log(r.data)
            let mauSac = r.data;
            $scope.idMauSac = mauSac.idMauSac;
            $scope.ten = mauSac.ten;
            $scope.updateDate = mauSac.updateDate;
        })
    }


    $scope.update = function (idMauSac) {
        if ($scope.ten == undefined || $scope.ten.length == 0) {
            document.getElementById("eTenMauUd").innerText = "Vui lòng nhập tên!!!";
            return
        }
        if ($scope.ten.length > 100) {
            document.getElementById("eTenMauUd").innerText = "Tên tối đa 100 ký tự!!!";
            return
        }
        var url = "/admin/mau-sac/update" + "/" + idMauSac;
        var updateMau = {
            idMauSac: idMauSac,
            ten: $scope.ten
        }

        $http.post(url, updateMau).then(function (r) {
            location.reload();
            alert("Update thành công")
        }).cath(function(err){
            console.log("Update khong thanh cong",err);
        })
    }

    $scope.delete = function (idMauSac) {
        if(confirm("Xác nhận xóa?")){
            var url = "/admin/mau-sac/delete" + "/" + idMauSac;
            $http.delete(url).then(function (r) {
                alert("Delete thành công!!!")
                location.reload();
            })
        }
    }
})