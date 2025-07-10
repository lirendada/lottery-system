$(document).ajaxSend(function (e, xhr, opt) {
    var user_token = localStorage.getItem("user_token");
    if (user_token) {
        xhr.setRequestHeader("user_token", user_token);
    }
});

function errFunc(xhr) {
    console.log(xhr);
    if(xhr!=null && xhr.status==401){
        alert("用户未登录, 即将跳转到登录页!");
        location.href ="/blogin.html";
    }
}