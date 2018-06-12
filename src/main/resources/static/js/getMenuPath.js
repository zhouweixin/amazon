function getMenuPath(menuId) {
    $.get(menuId).done(function (obj) {
        setMenuPath(obj)
    })
}
function setMenuPath(obj) {
    var menuPath = ""
    var data = obj.data
    var i = 0
    while(data.parentMenu){
        menuPath = data.parentMenu.name + " > " + menuPath
        data = data.parentMenu
    }
    menuPath = menuPath + obj.data.name
    document.getElementById("menuPath").setAttribute("class", "active")
    document.getElementById("menuPath").innerHTML = menuPath
}