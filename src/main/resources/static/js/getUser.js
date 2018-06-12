$(document).ready(function () {
    var userName = $.session.get('user');
    if(!userName){
        alert("请登录！")
        document.location = "login.html"
    }
    var userJson = JSON.parse(userName)
    document.getElementById("userName-span").innerHTML = "当前用户：" + userJson.name
})