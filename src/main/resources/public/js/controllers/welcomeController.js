(function() {

  var app = angular.module( "welcomeModule", [] );

  var WelcomeController = function($scope, $http) {
  
  	var onTodoComplete = function( response ) {
  		$scope.todos = response.data;
  	};
  	
  	$http.get("/todos/")
  		.then( onTodoComplete );
    $scope.message = "Harlexxxxy";
  };
  
  app.controller("WelcomeController", WelcomeController);

}());