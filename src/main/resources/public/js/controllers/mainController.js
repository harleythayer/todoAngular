(function() {

  var app = angular.module( "mainModule", [] );

  var MainController = function($scope, $http) {
  
  	var onTodoComplete = function( response ) {
  		$scope.todos = response.data;
  		$scope.incompleteCount( );
  	};
  	
  	$scope.user = "";
  	
  	$scope.enterUser = function( userName ) {
  		$scope.user = userName;
  		$scope.getTodos( userName.toLowerCase() );
  	}
  	
  	$scope.incompleteCount = function () {
		var count = 0;
		angular.forEach($scope.todos, function (todo) {
			if (!todo.completed) { count++ }
		});
		return count;
	}
  	
  	$scope.warningLevel = function () {
		return $scope.incompleteCount() < 3 ? "label-success" : "label-warning";
	}
  	
  	$scope.getTodos = function( userName ) {
  		$http.get("/todos/" + userName )
  			.then( onTodoComplete );
  	}
  };
  
  app.controller("MainController", MainController);

}());