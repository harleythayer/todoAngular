(function() {

  var app = angular.module( "mainModule", [] );

  var MainController = function($scope, $http) {
  	
  	var onError = function( failType ) {
  		console.log( failType + " failed!");
  	}
  	
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
  			.then( function( response ){
  						$scope.todos = response.data;
  						$scope.incompleteCount( );
  				   }, 
  				   function( response ) {
					   console.log("Failed to create new Todo reason: " + response.data.error );
				   }
  				 );
  	}
  	
  	$scope.updateTodo = function( todo ) {
  		var url = "/todos/" + $scope.user +"/" + todo.id;
  		$http.put(url, todo, [] )
  			.then( function( response ) {
  				   },
  				   function( response ) {
  					   console.log("Failed to update Todo with id: " + todo.id + " reason: " + response.data.error );
  				   }
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
  			.then( function( response ) {
  						$scope.todos.push( response.data );
  				   },
  				   function( response ) {
  					   console.log("Failed to create new Todo reason: " + response.data.error );
  				   }
  			 	); 
  	}
  	
  	$scope.deleteTodo = function( id ) {
  		var url = "/todos/" + $scope.user + "/" + id;
  		$http.delete(url, null, [] )
			.then( function( ) {
						$scope.todos = $scope.todos.filter( function( td ) {
							return td.id !== id;
						});
					}, 
					function(response) {
						console.log("could not delete Id: " + id + " reason: " + response.data.error);
					} 
			);
  	}
  };
  
  app.controller("MainController", MainController);

}());