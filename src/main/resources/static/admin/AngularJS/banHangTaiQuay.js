var app = angular.module("banhang-app", []);
app.controller("banhang-ctrl", function ($scope, $http) {
  //tao random ma don hang
  $scope.generateRandomString = function (length) {
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    let result = "";

    for (let i = 0; i < length; i++) {
      const randomIndex = Math.floor(Math.random() * characters.length);
      result += characters[randomIndex];
    }

    return result;
  };

  //$scope.donHang = {}
  $scope.donHangAdd = {
    maDonHang: $scope.generateRandomString(8),
    idTrangThai: 1,
    tongTien: 1,
    tongTienKhuyenMai: 1,
    tongTienSauKhuyenMai: 1,
    ghiChu: "khong",
    trangThaiThanhToan: false,
    idNhanVien: 1,
    idKhachHang: 1,
    idPhuongThucThanhToan: 1,
    idKhuyenMai: 1,
  };

  $scope.khachHangById = {};
  $scope.chiTietDonHang = [];
  $scope.donHang = [];
  $scope.products = [];
  $scope.productDetails = [];
  $scope.khachHang = [];

  var selectedId = null;
  $scope.selectedId = null;
  $scope.khachThanhToan = 0;
  //hiển thị vận chuyển
  $scope.shippingMethod = "1";

  //lấy don hàng chi tiết khi click đơn hàng
  $scope.selectOrder = function (id) {
    $scope.selectedId = id;
    selectedId = id;
    console.log("Selected Order ID:", id);
    $http
        .get(`/don-hang/don-hang-chi-tiet/${$scope.selectedId}`)
        .then(function (response) {
          console.log("check don hàng chi tiết: ", response.data);
          $scope.productDetails = response.data;
        })
        .catch(function (err) {
          console.log("err: ", err);
        });

    $http
        .get(`/don-hang/get-don-hang/${$scope.selectedId}`)
        .then(function (response) {
          console.log("check don hàng sau khi update kh: ", response.data);
          if (response.data.oldKhachHang) {
            $("#nameKH").val(response.data.oldKhachHang.hoTen || "");
            $("#sdtKH").val(response.data.oldKhachHang.soDienThoai || "");
          } else {
            $("#nameKH").val("");
            $("#sdtKH").val("");
          }
          $scope.khachHangById = response.data;
          // $scope.productDetails = response.data;
          console.log("check don hàng kh: ", $scope.khachHangById);
        })
        .catch(function (err) {
          console.log("err: ", err);
        });
  };

  //lấy sản phẩm
  $scope.getProducts = function () {
    $http
        .get("/don-hang/san-pham-chi-tiet")
        .then(function (response) {
          console.log("check log get products: ", response);
          $scope.products = response.data;
        })
        .catch(function (errors) {
          console.log(errors);
        });
  };

  //lấy đợn hàng chờ xử lý
  $scope.getDonHang = function () {
    $http
        .get("/don-hang/get-don-hang")
        .then(function (response) {
          console.log("check log: ", response);
          $scope.donHang = response.data;
          console.log("check log donhang: ", $scope.donHang);
        })
        .catch(function (errors) {
          console.log(errors);
        });
  };

  //lấy all khách hàng
  $scope.getKhachHang = function () {
    $http
        .get("/don-hang/get-khach-hang")
        .then(function (response) {
          $scope.khachHang = response.data;
          console.log("check get All khach hàng: ", $scope.khachHang);
        })
        .catch(function (errors) {
          console.error("Có lỗi xảy ra:", errors);
        });
  };

  //lấy khách hàng theo id
  $scope.getKhachHangByID = function (id) {
    var idDH = $scope.selectedId;
    $http
        .get("/don-hang/get-khach-hang/" + id)
        .then(function (response) {
          $scope.khachHangById = response.data;
          $("#nameKH").val(response.data.ho_ten);
          $("#sdtKH").val(response.data.so_dien_thoai);
          $("#show-modal-khach").modal("hide");
          console.log("$scope.khachHangById: ", $scope.khachHangById);
        })
        .catch(function (errors) {
          console.error("Có lỗi xảy ra:", errors);
        });

    $http
        .put("/don-hang/update-don-hang/" + idDH + "/" + id)
        .then(function (response) {
          console.log("update DH: ", response);
        })
        .catch(function (errors) {
          console.error("Có lỗi xảy ra:", errors);
        });
  };

  //tạo đon hàng
  $scope.addDonHang = function () {
    //console.log("$scope.khachHangById: ",$scope.khachHangById);
    if ($scope.donHang.length >= 8) {
      console.error("Đã có 8 đơn hàng, không thể thêm mới.");
      //alert('Đã đạt đến giới hạn 8 đơn hàng, không thể thêm đơn hàng mới.');
      return; // Dừng function nếu đã có 8 item
    }
    var donHangData = angular.copy($scope.donHangAdd);
    $http({
      method: "POST",
      url: "/don-hang/them-moi",
      data: donHangData,
      headers: {
        "Content-Type": "application/json",
      },
      transformRequest: function (data) {
        return JSON.stringify(data); // Chuyển đối tượng thành chuỗi JSON
      },
    })
        .then(function (response) {
          console.log("Đơn hàng đã được thêm:", response.data);
          $scope.getDonHang();
        })
        .catch(function (error) {
          console.error("Có lỗi xảy ra:", error);
        });
  };

  //thêm/cập nhật sản phẩm vào đơn hàng chi tiết
  $scope.addProductsDetail = function (idSanPhamChiTiet) {
    $scope.dataProduct = {
      maDonHangChiTiet: $scope.generateRandomString(8),
      idĐonHang: selectedId,
      idSanPhamChiTiet: idSanPhamChiTiet,
      soLuong: "1",
    };
    var dataProductsDetails = angular.copy($scope.dataProduct);
    if ($scope.dataProduct.idDonHang === null) {
      console.log("idDonHang is null, action blocked.");
      alert("Bạn Chưa Chọn Đơn Hàng!");
      return; // Chặn không cho thực hiện nếu idDonHang là null
    }
    var name = $("#nameKH").val();
    var sdt = $("#sdtKH").val();
    if (name === "" || sdt === "") {
      alert("Bạn Chưa Chọn Khách Hàng!");
      return;
    }

    if ($scope.selectedId === null || $scope.selectedId === "") {
      alert("Chưa Chọn Đơn Hàng!");
      return;
    }

    //kiểm tra đã tồn tại sản phẩm chưa
    var productWithId = $scope.productDetails.find(
        (item) => item.idSanPham === idSanPhamChiTiet
    );
    if (productWithId) {
      //console.log("productWithId",productWithId);
      $http({
        method: "PUT",
        url: "/don-hang/don-hang-chi-tiet/cap-nhat",
        data: dataProductsDetails,
        headers: {
          "Content-Type": "application/json",
        },
        transformRequest: function (data) {
          return JSON.stringify(data); // Chuyển đối tượng thành chuỗi JSON
        },
      })
          .then(function (response) {
            //console.log('Sản phẩm thêm thành công');
            console.log("Sản phẩm thêm: ", response.data);
            productWithId.soLuong++;
            //$scope.getProducts();
            var productInScope = $scope.products.find(
                (item) => item.idSanPhamChiTiet === idSanPhamChiTiet
            );
            if (productInScope && productInScope.soLuong > 1) {
              productInScope.soLuong--;
            }
          })
          .catch(function (error) {
            console.error("Có lỗi xảy ra:", error);
          });
    } else {
      //console.log("check sản phẩm chưa tồn tạo ");
      //thêm sản phẩm vào giỏ hàng
      $http({
        method: "POST",
        url: "/don-hang/don-hang-chi-tiet/them-moi",
        data: dataProductsDetails,
        headers: {
          "Content-Type": "application/json",
        },
        transformRequest: function (data) {
          return JSON.stringify(data); // Chuyển đối tượng thành chuỗi JSON
        },
      })
          .then(function (response) {
            console.log("Sp dơn hàng chi tiết đã được thêm:", response.data);
            $scope.productDetails.push(response.data);
            var productInScope = $scope.products.find(
                (item) => item.idSanPhamChiTiet === idSanPhamChiTiet
            );
            if (productInScope && productInScope.soLuong > 1) {
              productInScope.soLuong--;
            }
          })
          .catch(function (error) {
            console.error("Có lỗi xảy ra:", error);
          });
    }
    console.log("check Data spct", $scope.dataProduct);
  };

  $scope.createHoaDon = function () {
    var nameKH = $("#nameKH").val();
    var tongTien = $(".total-amount").text();
    var phieuGiamGia = $scope.selectedKhuyenMai; // Lấy mã khuyến mãi được chọn
    var khachThanhToan = $("#id-khach-thanh-toan").val();
    var phuongThucThanhToan = $("#paymentMethodSelect").val();
    var ghiChu = $("textarea").val();
    var tenKhachNhan = $("#nameKHNhan").val();
    var sdtKhachNhan = $("#sdtKHNhan").val();
    var diaChiKhachNhan =
        $("#soNha").val() +
        "-" +
        $("#phuong option:selected").text() +
        "-" +
        $("#quan option:selected").text() +
        "-" +
        $("#tinh option:selected").text();

    if ($scope.selectedId === null) {
      alert("Chưa chọn đơn hàng!");
      return;
    }

    if (khachThanhToan === null || khachThanhToan == 0) {
      alert("Chưa nhập tiền khách thanh toán!");
      return;
    }

    khachThanhToan = parseFloat(khachThanhToan);
    if (khachThanhToan < $scope.getTienKhachPTra()) {
      alert("Khách chưa thanh toán đủ tiền!");
      return;
    }

    if ($scope.productDetails && $scope.productDetails.length === 0) {
      alert("Chưa chọn sản phẩm!");
      return;
    }

    if ($scope.shippingMethod === "2") {
      if (!tenKhachNhan || !sdtKhachNhan || !diaChiKhachNhan) {
        alert("Thông tin nhận hàng không đầy đủ!");
        return;
      }
    }

    // Sử dụng voucher nếu có
    if (phieuGiamGia) {
      $http
          .post("/api/khuyen-mai/su-dung", null, {
            params: { maKhuyenMai: phieuGiamGia },
          })
          .then(function (response) {
            proceedCreateHoaDon(); // Gọi hàm tiếp tục tạo hóa đơn
          })
          .catch(function (error) {
            console.error("Sử dụng voucher thất bại:", error);
            alert(
                "Sử dụng mã khuyến mãi không thành công: " + (error.data || "")
            );
          });
    } else {
      proceedCreateHoaDon(); // Gọi hàm tiếp tục tạo hóa đơn
    }

    // Hàm tạo hóa đơn sau khi sử dụng voucher thành công
    function proceedCreateHoaDon() {
      var hoaDonData = {
        maHoaDon: $scope.generateRandomString(8),
        idKhuyenMai: phieuGiamGia || null,
        idTrangThai: 5,
        idPhuongThucThanhToan: phuongThucThanhToan,
        idDonHang: $scope.selectedId,
        idKhachHang: $scope.khachHangById.id_khach_hang,
        tenKhachHang: nameKH,
        tongTien: tongTien,
        tongTienKhuyenMai: $scope.getTienGiam(),
        tongTienSauKhuyenMai: $scope.getTienKhachPTra(),
        ghiChu: ghiChu,
        tenKhachNhan: tenKhachNhan,
        soDienThoaiKhachNhan: sdtKhachNhan,
        diaChiKhachNhan: diaChiKhachNhan,
        phuongThucNhan: $scope.shippingMethod,
        loaiDonHang: "1",
      };

      $http({
        method: "POST",
        url: "/hoa-don/them-moi",
        data: hoaDonData,
        headers: { "Content-Type": "application/json" },
      })
          .then(function (response) {
            console.log("Hóa đơn đã được tạo:", response.data);
            alert("Lưu hóa đơn thành công!");
            $scope.getKhachHang();
          })
          .catch(function (error) {
            console.error("Lưu hóa đơn thất bại:", error);
            alert("Lưu hóa đơn thất bại!");
          });
    }
  };

  //thêm khách hàng
  $scope.addKhachHang = function () {
    var sdt = $("#sdt-khach-hang").val();

    if (sdt === null || sdt === "") {
      $scope.showErrrorsMes("Chưa nhập số điện thoại");
      return;
    }

    $scope.dataKhachHang = {
      maKhachHang: $scope.generateRandomString(8),
      soDienThoai: sdt,
    };
    var khachHangData = angular.copy($scope.dataKhachHang);
    $http({
      method: "POST",
      url: "/don-hang/them-khach-hang",
      data: khachHangData,
      headers: {
        "Content-Type": "application/json",
      },
      transformRequest: function (data) {
        return JSON.stringify(data); // Chuyển đối tượng thành chuỗi JSON
      },
    })
        .then(function (response) {
          console.log("Khách Hàng DATA:", response.data);
          $scope.getKhachHang();
        })
        .catch(function (error) {
          console.error("Có lỗi xảy ra Khách Hàng DATA:", error);
          if (error.data) {
            $scope.showErrrorsMes(error.data); // Sử dụng thông báo lỗi từ server
          } else {
            $scope.showErrrorsMes("Có lỗi xảy ra khi thêm khách hàng.");
          }
        });

    $("#show-modal-khach").modal("show");
  };

  //xoá đơn hàng
  $scope.deleteDonHang = function (id) {
    if (confirm("Bạn có muôna xoá đơn hàng?")) {
      $http
          .delete("/don-hang/don-hang/xoa/" + id)
          .then(function (response) {
            console.log("Đã xoá thành công đơn hang: ", response);
            //$scope.selectOrder(selectedId);
            $scope.getDonHang();
            $scope.productDetails = [];
          })
          .catch(function (errors) {
            console.error("Có lỗi xảy ra:", errors);
          });
    }
  };

  //xoá đơn hàng chi tiết
  $scope.deleteDonHangChiTiet = function (id, idSP) {
    // alert("Show: "+id);
    if (confirm("Bạn Có muốn Xoá sản phẩm khỏi đơn hàng không?")) {
      $http
          .delete("/don-hang/don-hang-chi-tiet/xoa/" + id)
          .then(function (response) {
            console.log("Đã xóa đơn hàng chi tiết thành công:", response);
            $scope.selectOrder(selectedId);
            var productInScope = $scope.products.find(
                (item) => item.idSanPhamChiTiet === idSP
            );
            if (productInScope) {
              productInScope.soLuong++;
            } else {
              console.error("Không tìm thấy sản phẩm với id:", id);
            }
          })
          .catch(function (errors) {
            console.error("Có lỗi xảy ra:", errors);
          });
    }
  };

  //tìm kiếm khách hàng
  $scope.searchKhachHang = function () {
    var sdt = $("#sdt-khach-hang").val();
    if (sdt === null || sdt === "") {
      $scope.getKhachHang();
    } else {
      $http
          .get("/don-hang/khach-hang/tim-kiem", {
            params: { sdt: sdt }, // Truyền `sdt` dưới dạng query parameter
          })
          .then(function (response) {
            $scope.khachHang = [];
            console.log("Check kH search: ", response.data);
            $scope.khachHang = response.data;
          })
          .catch(function (errors) {
            console.error("Có lỗi xảy ra:", errors);
          });
    }
  };

  //tìm kiếm sản phẩm
  $scope.inputData = "";
  $scope.searchSanPham = function () {
    var searchData = $("#search-product").val();
    if (searchData === null || searchData === "") {
      $scope.getProducts();
    } else {
      $http
          .get("/don-hang/san-pham-chi-tiet/tim-kiem", {
            params: { tenSanPham: searchData }, // Truyền `sdt` dưới dạng query parameter
          })
          .then(function (response) {
            $scope.products = [];
            console.log("Check PD search: ", response.data);
            $scope.products = response.data;
          })
          .catch(function (errors) {
            console.error("Có lỗi xảy ra:", errors);
          });
    }
  };

  //show-modal-khach
  $scope.openModal = function () {
    $("#show-modal-khach").modal("show");
  };

  //tang so luong
  $scope.soLuongPlus = function (details) {
    details.soLuong += 1;
    $scope.getProducts();
    console.log("Số Lượng Reduce: ", details);
    console.log("Số Lượng Reduce: ", details.soLuong);
    console.log("Số Lượng Reduce: ", details.soLuong * details.giaBan);
  };
  //Giảm Số Lượng
  $scope.soLuongReduce = function (details) {
    if (details.soLuong > 1) {
      details.soLuong -= 1;
      $scope.getProducts();
      console.log("Số Lượng plus: ", details.soLuong);
      console.log("Số Lượng plus: ", details.soLuong * details.giaBan);
    }
  };
  //lưu só lượng ban đầu
  $scope.saveOriginalQuantity = function (details) {
    details.saveOriginalQuantity = details.soLuong;
  };
  //kiểm tra số lượng được nhập
  $scope.checkQuantity = function (details) {
    if (details.soLuong > 100) {
      alert("Số lượng không được lớn hơn 100.");
      details.soLuong = details.originalQuantity;
    } else {
      details.originalQuantity = details.soLuong;
    }
  };

  //tính tổng tiền tất cả sản phẩm
  $scope.getSum = function () {
    if (!$scope.productDetails || $scope.productDetails.length === 0) {
      return 0;
    }
    let sumMoney = 0;
    $scope.productDetails.forEach(function (details) {
      sumMoney += details.soLuong * details.giaBan;
    });
    return sumMoney;
  };
  $scope.getTienGiam = function () {
    // Kiểm tra nếu chưa chọn khuyến mãi
    if (!$scope.selectedKhuyenMai) {
      return 0; // Không áp dụng giảm giá
    }

    // Tìm khuyến mãi trong danh sách dựa trên mã khuyến mãi
    const selectedKhuyenMai = $scope.khuyenMaiList.find(
        (km) => km.maKhuyenMai === $scope.selectedKhuyenMai
    );

    // Nếu không tìm thấy mã khuyến mãi hợp lệ
    if (!selectedKhuyenMai) {
      $scope.discountError = "Mã khuyến mãi không hợp lệ!";
      $scope.discountMessage = null; // Xóa thông báo thành công nếu có
      return 0;
    }

    // Kiểm tra điều kiện áp dụng khuyến mãi (nếu có điều kiện tối thiểu)
    if (selectedKhuyenMai.soTienToiThieu && $scope.getSum() < selectedKhuyenMai.soTienToiThieu) {
      $scope.discountError = "Không đủ điều kiện áp dụng mã khuyến mãi!";
      $scope.discountMessage = null;
      return 0;
    }

    // Xóa thông báo lỗi nếu mã hợp lệ
    $scope.discountError = null;

    // Tính tiền giảm
    let tienGiam = 0;
    if (selectedKhuyenMai.mucGiamGia <= 100) {
      // Nếu là phần trăm
      tienGiam = $scope.getSum() * (selectedKhuyenMai.mucGiamGia / 100);
    } else {
      // Nếu là số tiền cố định
      tienGiam = selectedKhuyenMai.mucGiamGia;
    }

    // Đảm bảo tiền giảm không vượt quá tổng tiền
    tienGiam = Math.min(tienGiam, $scope.getSum());

    // Cập nhật thông báo thành công
    $scope.discountMessage = `Mã khuyến mãi hợp lệ! Được giảm: ${tienGiam} VND`;

    return tienGiam; // Trả về số tiền được giảm
  };


  $scope.getTienKhachPTra = function () {
    const khachPhaiTra = $scope.getSum() - $scope.getTienGiam();
    return khachPhaiTra > 0 ? khachPhaiTra : 0; // Không trả về giá trị âm
  };

  //tính tiền thừa
  $scope.tinhTienThua = function () {
    if ($scope.khachThanhToan === 0) {
      return 0;
    }
    let tongTien = $scope.getTienKhachPTra();
    let khachThanhToan = parseFloat($scope.khachThanhToan) || 0; // Đảm bảo giá trị là số
    return khachThanhToan - tongTien;
  };

  //in hoá đơn
  $scope.printer = function () {
    if ($scope.selectedId === null)
      $http
          .get("/hoa-don/invoice")
          .then(function (response) {
            console.error("thanh cong:", response);
          })
          .catch(function (errors) {
            console.error("Có lỗi xảy ra:", errors);
          });
  };
  //ẩn thông báo
  $scope.hideErrrorsMes = function () {
    $("#erroresMessage").hide();
  };
  //hiện thông báo
  $scope.showErrrorsMes = function (message) {
    $("#showMessage").text(message);
    $("#erroresMessage").show();

    setTimeout(() => {
      $("#erroresMessage").hide();
    }, 2000);
  };

  //test get tỉnh/thành phố
  function loadProvinces() {
    $http
        .get("https://esgoo.net/api-tinhthanh/1/0.htm")
        .then(function (response) {
          if (response.data.error == 0) {
            $.each(response.data.data, function (key_tinh, val_tinh) {
              $("#tinh").append(
                  '<option value="' +
                  val_tinh.id +
                  '">' +
                  val_tinh.full_name +
                  "</option>"
              );
              //$("#tinh").append('<option value="' + val_tinh.full_name + '">' + val_tinh.full_name + '</option>');
            });
          }
        });
  }

  // Function to load districts based on selected province
  $scope.loadDistricts = function () {
    var idtinh = $("#tinh").val();
    $http
        .get("https://esgoo.net/api-tinhthanh/2/" + idtinh + ".htm")
        .then(function (response) {
          if (response.data.error == 0) {
            $("#quan").html('<option value="0">Quận Huyện</option>');
            $("#phuong").html('<option value="0">Phường Xã</option>');
            $.each(response.data.data, function (key_quan, val_quan) {
              $("#quan").append(
                  '<option value="' +
                  val_quan.id +
                  '">' +
                  val_quan.full_name +
                  "</option>"
              );
              //$("#quan").append('<option value="' + val_quan.full_name + '">' + val_quan.full_name + '</option>');
            });
          }
        });
  };

  // Function to load wards based on selected district
  $scope.loadWards = function () {
    var idquan = $("#quan").val();
    $http
        .get("https://esgoo.net/api-tinhthanh/3/" + idquan + ".htm")
        .then(function (response) {
          if (response.data.error == 0) {
            $("#phuong").html('<option value="0">Phường Xã</option>');
            $.each(response.data.data, function (key_phuong, val_phuong) {
              $("#phuong").append(
                  '<option value="' +
                  val_phuong.id +
                  '">' +
                  val_phuong.full_name +
                  "</option>"
              );
              //$("#phuong").append('<option value="' + val_phuong.full_name + '">' + val_phuong.full_name + '</option>');
            });
          }
        });
  };
  // Call loadProvinces when the controller initializes
  loadProvinces();
  // Watch for changes in the province dropdown to load districts
  $("#tinh").change($scope.loadDistricts);

  // Watch for changes in the district dropdown to load wards
  $("#quan").change($scope.loadWards);

  $scope.khuyenMaiList = []; // Danh sách khuyến mãi
  $scope.selectedKhuyenMai = null; // Mã khuyến mãi được chọn
  $scope.discountMessage = null; // Thông báo kết quả
  $scope.discountError = null;
  $scope.getAllKhuyenMai = function () {
    $http
        .get("/api/khuyen-mai")
        .then(function (response) {
          $scope.khuyenMaiList = response.data; // Lưu danh sách khuyến mãi
        })
        .catch(function (error) {
          console.error("Lỗi khi tải danh sách khuyến mãi", error);
        });
  };

  // Kiểm tra mã khuyến mãi khi chọn
  $scope.checkKhuyenMai = function () {
    if (!$scope.selectedKhuyenMai) {
      $scope.discountError = "Vui lòng chọn mã khuyến mãi!";
      $scope.discountMessage = null;
      return;
    }

    $http
        .get("/api/khuyen-mai/kiem-tra", {
          params: {
            maKhuyenMai: $scope.selectedKhuyenMai,
            tongTien: $scope.getSum(), // Truyền tổng tiền của đơn hàng
          },
        })
        .then(function (response) {
          const discount = response.data; // Giá trị trả về từ server

          let discountMessage = ""; // Biến chứa thông báo
          if (discount <= 100) {
            // Nếu giá trị giảm <= 100, coi như % giảm giá
            const tienGiam = ($scope.getSum() * discount) / 100;
            discountMessage =
                "Mã khuyến mãi hợp lệ! Giảm " +
                discount +
                "% (Tương đương: " +
                tienGiam +
                " VND)";
          } else {
            // Nếu giá trị giảm > 100, coi như số tiền giảm cố định
            discountMessage =
                "Mã khuyến mãi hợp lệ! Giảm trực tiếp: " + discount + " VND";
          }

          // Cập nhật thông báo
          $scope.discountMessage = discountMessage;
          $scope.discountError = null;
        })
        .catch(function (error) {
          $scope.discountError = error.data || "Mã khuyến mãi không hợp lệ!";
          $scope.discountMessage = null;
          $scope.tienGiam = 0; // Đảm bảo tiền giảm được reset
        });
  };
  $scope.clearVoucher = function () {
    $scope.selectedKhuyenMai = null;
    $scope.discountMessage = null;
    $scope.discountError = null;
    $scope.tienGiam = 0; // Reset tiền giảm
  };
  //load data product when run
  $scope.getProducts();
  $scope.getDonHang();
  $scope.getKhachHang();
  $scope.hideErrrorsMes();
  $scope.getAllKhuyenMai();
});
