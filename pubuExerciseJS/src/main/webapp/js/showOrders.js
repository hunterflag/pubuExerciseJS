$(document).ready(function(){
	console.log( document.visibilityState );
	
});

//
//function showOrderDetails(od_id){
//	var urlString = "showOrderDetails.jsp?od_id=" + od_id;
//	$.get(urlString, function(data, status){
//		if(status == "success"){
//			$("#areaShowOrderDetails").html(data);
//		}
//	});
//}

function getOrderDetails(od_id){
	var urlString = "GetOrderDetailsByMaps.do?od_id=" + od_id;
	$.get(urlString, function(data, status){
		if(status == "success"){
			$("#areaShowOrderDetails").html(data);
		}
	});
}
	