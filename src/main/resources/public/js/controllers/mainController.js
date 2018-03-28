(function() {

  var app = angular.module( "mainModule", [] );

  var MainController = function($scope, $http) {
  
  	var onTodoComplete = function( response ) {
  		$scope.todos = response.data;
  		$scope.incompleteCount( );
  	};
  	
  	$scope.user = "";
  	$scope.todoUser = "";
  	
  	$scope.enterUser = function( userName ) {
  		$scope.user = userName;
  		$scope.todoUser = userName.toLowerCase( );
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
  	
  	$scope.updateTodo = function( todo ) {
  		$http.put("/todos/" + $scope.user +"/" +todo.id, todo, [] )
  			.then( function successCallback( ) {
  				console.log( "put success" );
  			},
  			function errorCallback( ) {
  				console.log( "put Error" );
  			}  );
  	}
  	
  	$scope.addNewTodo = function( wToDo ) {
  		var url = "/todos/" + $scope.user;
  		var newTodo = { userName: $scope.user, completed: false, whatToDo: wToDo };
  		$http.post( url, newTodo, [] )
  			.then( function successCallback( response ) {
  				$scope.todos.push( response.data );
  				console.log( "post success" );
  			},
  			function errorCallback( ) {
  				console.log( "post Error" );
  			}  ); 
  	}
  };
  
  app.controller("MainController", MainController);

}());