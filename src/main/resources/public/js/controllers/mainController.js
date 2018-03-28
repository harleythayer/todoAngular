(function() {

  var app = angular.module( "mainModule", [] );

  var MainController = function($scope, $http) {
  
  	var onTodoComplete = function( response ) {
  		$scope.todos = response.data;
  		$scope.incompleteCount( );
  	};
  	
  	var onError = function( failType ) {
  		console.log( failType + " failed!");
  	}
  	
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
  			.then( onTodoComplete, onError );
  	}
  	
  	$scope.updateTodo = function( todo ) {
  		var url = "/todos/" + $scope.user +"/" + todo.id;
  		$http.put(url, todo, [] )
  			.then( function successCallback( ) {
  				   },
  				   onError
  				);
  	}
  	
  	$scope.addNewTodo = function( wToDo ) {
  		var url = "/todos/" + $scope.user;
  		var newTodo = { 
  				userName: $scope.user, 
  				completed: false, 
  				whatToDo: wToDo 
  		};
  		$http.post( url, newTodo, [] )
  			.then( function successCallback( response ) {
  						$scope.todos.push( response.data );
  				   },
  				   onError
  			 	); 
  	}
  	
  	var deleteSuccess = function( id ) {
  		$scope.todos = $scope.todos.filter( function( td ) {
			return td.id !== id;
		});
  	}
  	
  	$scope.deleteTodo = function( id ) {
  		var url = "/todos/" + $scope.user + "/" + id;
  		$http.delete(url, null, [] )
			.then( deleteSuccess(id), onError );
  	}
  };
  
  app.controller("MainController", MainController);

}());