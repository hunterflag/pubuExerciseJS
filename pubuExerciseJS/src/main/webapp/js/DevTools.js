// https://blog.wu-boy.com/2009/11/javascript-%E5%9C%A8%E5%87%BD%E6%95%B8%E8%A3%A1%E8%A8%AD%E5%AE%9A%E5%8F%83%E6%95%B8%E9%A0%90%E8%A8%AD%E5%80%BC/
function todo(id, message){
	id = id || '無資料';
	if (typeof message == 'undefined') { message = '無資料'; }
	
//	alert("id: " + id + ", message: " + message);
	console.log("id: " + id + ", message: " + message);
}