angular.module("todoApp")
	.constant("userListActiveClass", "btn-primary")
	.constant("userListPageCount", 3)
	.controller("AdminController", function($scope, $http, userListActiveClass, userListPageCount){
	
		$http.get("/v1/admin/" )
			.then( function( response ){
						$scope.todos = response.data;
				   }, 
				   function( response ) {
					   console.log("Failed to create new Todo reason: " + response.data.error );
				   }
				);
		
		$scope.userNameSorter = function( todo ) {
			return todo.userName;
		};
		
		var selectedUser = null;
		
		$scope.selectedPage = 1;
		$scope.pageSize = userListPageCount;
		
		$scope.selectUser = function(newUser) {
			selectedUser = newUser;
			$scope.selectedPage = 1;
		};
		
		$scope.selectPage = function (newPage) {
			$scope.selectedPage = newPage;
		};
		
		$scope.userFilterFn = function(todo) {
			return selectedUser == null || todo.userName == selectedUser;
		};
		
		$scope.getUserClass = function (user) {
			console.log("selected user = " + selectedUser + "user = " + user);
			return selectedUser == user ? userListActiveClass : "";
		};
		
		$scope.getCompletedClass = function( completed ) {
			return completed ? "btn btn-success" : "btn btn-danger";
		}
		
		$scope.getPageClass = function (page) {
			return $scope.selectedPage == page ? userListActiveClass : "";
		};
		
		$scope.deleteTodo = function( userName, id ) {
			$http.delete("/v1/admin/user?username=" + userName + "&id=" + id )
				.then( function( response ){
							$scope.todos = $scope.todos.filter( function( td ) {
								return td.id !== id;
							});
				   		}, 
				   		function( response ) {
				   			console.log("Failed to create new Todo reason: " + response.data.error );
				   		}
					 );
		};
		
		
	});