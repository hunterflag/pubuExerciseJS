$(document).ready(function(){
	$("#btnHome").click(function(){
		todo("btnHome");
	});

	$("#btnLogin").click(function(){
		window.open('login.jsp');
		todo("btnLogin");
	});
	
	$("#btnLogout").click(function(){
		todo('btnLogout');
		window.open('Logout.do');
	});
	
	$("#btnShowProductList").click(function(){
		todo("btnShowProductList");
		window.open("showProductList.jsp");
	});
	
	$("#btnShowShoppingCart").click(function(){
		todo("btnShowShoppingCart");
		window.open("showShoppingCart.jsp");
	});
	
	$("#btnShowOrder").click(function(){
		todo("btnShowOrder");
		window.open("showOrders.jsp");
	});
	
	todo("index.js Loaded...");
});