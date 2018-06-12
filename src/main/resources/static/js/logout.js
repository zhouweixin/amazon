function logout() {
    var str = ipPort + "/user/logout"
    $.ajax({
        url:str,
        success:function (obj) {
            // console.log(obj)
            if(obj.code == 0){
                $.session.clear()
                document.location = 'login.html';
            }
        },
        error: function(err) {
            console.log(err)
        }

    })
}