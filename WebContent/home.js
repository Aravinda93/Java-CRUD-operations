var app	 	= 	angular.module('myApp', ['datatables']);

app.controller('AppController', function($scope, $timeout, $compile,DTOptionsBuilder,DTColumnBuilder, $http) {
	$scope.companies	=	[];
	$scope.ModifyingCompany;
	$scope.dtOptions = DTOptionsBuilder.newOptions().withOption('order', [0, 'asc']).withOption('lengthMenu', [5, 10, 15, 20]);
	
	//Get the data from the Database on click of the button
	$scope.getData	=	function()
	{	
		angular.element("#CompanyTable").hide();
		angular.element("#Loader_Logo1").show();	
		$http({
			method 		: 	"GET",
			url 		: 	'GetData'
        }).success(function(data) {
        	angular.element("#Loader_Logo1").hide();
        	angular.element("#CompanyTable").show();
            $scope.companies	=	data;
        }).error(function(data) {
        	console.log(data);
            console.log("Something Went Wrong in GET FUNCTION");
        });
	}
	
	//On load of the page call the get function to obtain the data
	$scope.init		=	function(){
		$scope.getData();
	}
	
	//Display Modal for adding the new Data
	$scope.AddNew		=	function(){
		angular.element('#AddNewCompany').modal('show');
	}
	
	//After the submit call the function to write the data
	$scope.NewCompanyAddition	=	function(){
	
		//Check if the company is already present		
		for(var com=0; com<$scope.companies.length; com++)
		{
			if($scope.NewCompanyAdditionDetails.FirmName == $scope.companies[com].COMPANY_NAME)
			{
				alertify.alert("Trail Project","The organization "+ $scope.NewCompanyAdditionDetails.FirmName +" details already exist."); 
				return;
			}
		}
		
		var data	=	JSON.stringify({CompanyDetails : $scope.NewCompanyAdditionDetails});
		$http({
			method 		: 	"POST",
			url 		: 	'AddData',
			data		:	data,
			headers		: 	{'Content-Type': 'application/json'},
        }).success(function(data) {
        	angular.element('#AddNewCompany').modal('hide');
        	alertify.alert("Trail Project","The new organization <b>" + $scope.NewCompanyAdditionDetails.FirmName + "</b> details has been added"); 
            $scope.getData();
           	$scope.NewCompanyAdditionDetails	=	{};
        }).error(function(data) {
            console.log("Something Went Wrong in GET FUNCTION");
        });
	}
	
	//Show the Edit Modal for the Data onclick
	$scope.EditCompanyfn	=	function(Edit_Id){
		$scope.EditCompanyDetails	=	{};
		data	=	{Edit_Id:Edit_Id, Type: 'Edit'};
		
		$http({
			method 		: 	"POST",
			url 		: 	'GetData',
			params 		:	data
        }).success(function(data) {
        	$scope.EditCompanyDetails = 	{
		        								FirmName		:	data.COMPANY_NAME,	
		        								LegalEntity		:	data.LEGAL_ENTITY,
		        								Employees		:	data.EMPLOYEES,
		        								ShareCapital	:	data.CAPITAL,
		        								EditIDSave		:	data.ID
        									};
        	angular.element('#EditCompany').modal('show');
        	$scope.ModifyingCompany	= data;
        }).error(function(data) {
        	console.log(data);
            console.log("Something Went Wrong in GET FUNCTION");
        });
	} 
	
	//Edit insert on save of the modal information
	$scope.EditExisingCompany	=	function(){
	
		if($scope.EditCompanyDetails.FirmName != $scope.ModifyingCompany.COMPANY_NAME || $scope.EditCompanyDetails.LegalEntity != $scope.ModifyingCompany.LEGAL_ENTITY || $scope.EditCompanyDetails.Employees != $scope.ModifyingCompany.EMPLOYEES || $scope.EditCompanyDetails.ShareCapital != $scope.ModifyingCompany.CAPITAL)
		{
			var data		=	JSON.stringify({CompanyDetails : $scope.EditCompanyDetails});
		
			$http({
				method 		: 	"POST",
				url 		: 	'UpDate',
				data		:	data,
				headers		: 	{'Content-Type': 'application/json'},
	        }).success(function(data) {
	        	angular.element('#EditCompany').modal('hide');
	            $scope.EditCompanyDetails	=	{};
	            alertify.alert("Trail Project","The organization <b>"+ $scope.ModifyingCompany.COMPANY_NAME +"</b> details has been updated"); 
	            $scope.getData();
	        }).error(function(data) {
	            console.log("Something Went Wrong in GET FUNCTION");
	        });
		}
		else
		{
			 alertify.alert("Trail Project","Details provided for <b>"+ $scope.ModifyingCompany.COMPANY_NAME +"</b> already matches"); 
		}
		
	}
	
	//Delete the company
	$scope.DeleteCompany	=	function(Delete_ID)
	{
		var CompanyName		=	"";
		
		for(var com=0; com<$scope.companies.length; com++)
		{
			if($scope.companies[com].ID == Delete_ID)
			{
				CompanyName	= $scope.companies[com].COMPANY_NAME;
				break;
			}
		}
		
		data	=	{Delete_ID:Delete_ID, Type: 'Delete'};
				
		$http({
			method 		: 	"POST",
			url 		: 	'GetData',
			params 		:	data
        }).success(function(data) {
			alertify.alert("Trail Project","The organization <b>"+ CompanyName +"</b> has been deleted"); 
			$scope.getData();      	
        }).error(function(data) {
        	console.log(data);
            console.log("Something Went Wrong in GET FUNCTION");
        });
	}
	
	//function to refresh the table periodically
	$scope.RefreshPeriodic	=	function(){
		var RefreshTime		=	$scope.RefreshTime * 1000;
		if(RefreshTime > 0)
		{
			setInterval(function() {
   				$scope.getData();
			}, RefreshTime);
		}
	}
	
});