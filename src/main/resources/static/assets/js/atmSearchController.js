angular.module('atmFinder', [])
.controller('atmSearchController', function($scope, $http) {
    $http.get('/atms/locations').
        then(function(response) {
            $scope.locations = response.data;
        });
});

    