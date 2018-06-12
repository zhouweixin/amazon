var xmlhttp;
function runAjax(str, cfunc) {
    if(str==""){
        document.getElementById("a-span").innerHTML="";
        return;
    }
    if(window.XMLHttpRequest){
        xmlhttp=new XMLHttpRequest();
    }
    else{
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = cfunc;
    xmlhttp.open("GET", str, true);
    xmlhttp.send();
}

function login() {
    var username = $("#inputEmail").val()
    var password = $("#inputPassword").val()
    var str = ipPort + "/user/login?id=" + username + "&password=" + password
    $.ajax({
        url:str,
        dataType:'json',
        success:function (obj) {
            console.log(obj)
            if(obj.code == 0){
                $.session.set('user',JSON.stringify(obj.data));
                document.location = 'index.html';
            }
            else if(obj.code == 15){
                alert("请输入用户名！")
            }
            else if(obj.code == 16){
                alert("请输入密码！")
            }
            else if(obj.code == 17){
                alert("用户名或者密码不正确！")
            }
        },
        error: function(err) {
            console.log(err)
        }

    })
}