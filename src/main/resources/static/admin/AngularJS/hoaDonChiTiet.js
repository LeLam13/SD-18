$scope.viewDetails = function(idHoaDon) {
    $http.get('/admin/hoa-don/detail/' + idHoaDon).then(function(response) {
        $scope.detail = response.data;
        $('#detailsModal').modal('show');
    }, function(error) {
        console.error("Error fetching detail:", error);
    });
};

