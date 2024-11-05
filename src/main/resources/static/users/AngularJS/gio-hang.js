var app = angular.module("gio-hang", [])
app.controller("gio-hang-ctrl", function ($scope, $http) {
    $scope.message = "Hello 1234456787!";
    $scope.listProducts = [];
    $scope.items = [];//lưu dữ liệu vào localStore
    $scope.username = null;

    $scope.getUserName = function (){
        $http.get("/don-hang-online/username").then(function (response){
            $scope.username = response.data;
        }).catch(function (errors){
            console.error("có lỗi xảy ra: ",errors);
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

    $scope.addProductIntocart= function (id){
        //alert("add OK");
        var item = $scope.items.find(item=>item.id == id);
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
    }

    $scope.count = function (){
    }

    $scope.clearLocalStorage = function (){
        $scope.items=[];
        this.saveToLocalStorage();
    }

    $scope.removeLocalStorage = function (id){
        var index = this.items.findIndex(item => item.id == id);
        $scope.items.splice(index,1);
        this.saveToLocalStorage();
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
    $scope.loadFromLocalStorage();
});

app.controller("don-hang-online-ctrl", function ($scope, $http) {
    $scope.mess = "hello";
    $scope.itemsOrder = [];
    $scope.loadFromLocalStorage = function (){
        var json = localStorage.getItem("cart");
        $scope.itemsOrder = json ? JSON.parse(json) : [];
    }

    //load dữ liệu
    $scope.loadFromLocalStorage();
})