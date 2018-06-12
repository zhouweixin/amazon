var str = "/closing/startCrawl?"
var startScheduleStr = "/closing/enableSchedule?"
var intervalID
$(document).ready(function () {
    str = getParm(str)
    startScheduleStr = getParm(startScheduleStr)
    activeMenu()
})
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
function activeMenu() {
    getSpiderSubMenu(1, 1)
    // console.log("2")
}

$(document).ready(function () {
    var menu1 = $("#ul-1 #1-li a")
    var menu2 = $("#ul-2 li")
    var activeRow
    menu1.on("click",  function (e) {
        getSpiderSubMenu(1, 1)
    })
})
function activeMenu2(parentId) {
    getSpiderSubMenu(parentId,  2)
}
function activeMenu3(parentId) {
    getSpiderSubMenu(parentId,  3)
}
function activeMenu4(parentId) {
    getSpiderSubMenu(parentId,  4)
}
function activeMenu5(parentId) {
    getSpiderSubMenu(parentId,  5)
}
function activeMenu6(parentId) {
    getSpiderSubMenu(parentId,  6)
}
function activeMenu7(parentId) {
    getSpiderSubMenu(parentId,  7)
}
function checkboxOnclick(checkbox) {
    document.getElementById("StatusPanel").setAttribute("class", "panel panel-info hidden")
    document.getElementById("selectedMenuPanel").setAttribute("class", "panel panel-info")
    if(checkbox.checked == true){
        createPMenuName(checkbox)
    }else {
        removePMenuName(checkbox)
    }
}

function selectAllCheckbox() {
    document.getElementById("StatusPanel").setAttribute("class", "panel panel-info hidden")
    document.getElementById("selectedMenuPanel").setAttribute("class", "panel panel-info")
    $(".checkboxMenu").prop("checked", true)
    var checkboxs = document.getElementsByClassName("checkboxMenu")
    var i
    for(i in checkboxs){
        if(i == "length")break
        createPMenuName(checkboxs[i])
    }
}
function cancelAllCheckbox() {
    $(".checkboxMenu").prop("checked", false)
    var checkboxs = document.getElementsByClassName("checkboxMenu")
    var i
    for(i in checkboxs){
        if(i == "length")break
        removePMenuName(checkboxs[i])
    }
}
function emptyPanel() {
    $("p").remove(".selectedMenuName");
    $("p").remove(".p-status");
    $(".checkboxMenu").prop("checked", false)
    var checkboxs = document.getElementsByClassName("checkboxMenu")
    var i
    for(i in checkboxs){
        if(i == "length")break
        removePMenuName(checkboxs[i])
    }
}
function emptyStatusPanel() {
    $("p").remove(".p-status");
}
function startSpider() {
    emptyStatusPanel()
    $.get(ipPort + "/closing/getSpiderStatus").done(function (obj_) {
        if(obj_.data.code == 11){
            alert("爬虫正在运行！")
            return
        }
        var spiderMenu = document.getElementsByClassName("selectedMenuName")
        if(spiderMenu.length == 0){
            alert("请选择菜单！")
            return
        }

        var menu = []
        for(var i in spiderMenu){
            if(i == "length")break
            menu.push(spiderMenu[i].getAttribute("dataid"))
        }
        var startStr
        startStr = ipPort + str + "&menus=" + menu
        $.get(startStr).done(function () {
            document.getElementById("selectedMenuPanel").setAttribute("class", "panel panel-info hidden")
            document.getElementById("StatusPanel").setAttribute("class", "panel panel-info")
            alert("爬虫开始！")
            queryState()
        })
    })

}
function endSpider() {
    var endStr = ipPort + "/closing/stopCrawl"
    $.get(endStr).done(function () {
        alert("停止爬取！")

        clearInterval(intervalID)
        // emptyStatusPanel()
    })
}
function startSchedule() {
    emptyStatusPanel()
    $.get(ipPort + "/closing/getSpiderStatus").done(function (obj_) {
        if(obj_.data.code == 11){
            alert("爬虫正在运行！")
            return
        }
        var spiderMenu = document.getElementsByClassName("selectedMenuName")
        if(spiderMenu.length == 0){
            alert("请选择菜单！")
            return
        }

        var menu = []
        for(var i in spiderMenu){
            if(i == "length")break
            menu.push(spiderMenu[i].getAttribute("dataid"))
            // var id = spiderMenu[i].getAttribute("dataid")
        }
        var startStr = ""
        startStr = ipPort + startScheduleStr + "&menus=" + menu
        $.get(startStr).done(function () {
            document.getElementById("selectedMenuPanel").setAttribute("class", "panel panel-info hidden")
            document.getElementById("StatusPanel").setAttribute("class", "panel panel-info")
            alert("启动定时器，每天0点开始爬取！")
            queryStateSchedule()
        })
    })
}
function stopSchedule() {
    var endStr = ipPort + "/closing/stopSchedule"
    $.get(endStr).done(function () {
        alert("结束定时器！")
        // document.getElementById("StatusPanel").setAttribute("class", "panel panel-info hidden")
        // document.getElementById("selectedMenuPanel").setAttribute("class", "panel panel-info")
        clearInterval(intervalID)
        // emptyStatusPanel()
    })
}
function startSpiderAll() {
    emptyStatusPanel()
    $.get(ipPort + "/closing/getSpiderStatus").done(function (obj_) {
        if(obj_.data.code != 9){
            alert("爬虫正在运行！")
            return
        }
        $.get(ipPort + "/menu/getAll").done(function (obj_menu) {
            var menu = []
            for(var i = 0; i < obj_menu.data.length; i++){
                menu.push(obj_menu.data[i].id)
            }
            var startStr = ipPort + "/closing/startCrawlAllMenu"
            // console.log(startStr)
            $.get(startStr).done(function () {
                document.getElementById("selectedMenuPanel").setAttribute("class", "panel panel-info hidden")
                document.getElementById("StatusPanel").setAttribute("class", "panel panel-info")
                alert("爬虫开始！")
                queryState()
            })
        })
    })
}
function startScheduleAll() {
    emptyStatusPanel()
    $.get(ipPort + "/closing/getSpiderStatus").done(function (obj_) {
        if(obj_.data.code == 11){
            alert("爬虫正在运行！")
            return
        }
        var startStr = ""
        startStr = ipPort + "/closing/enableScheduleAllMenu"
        $.get(startStr).done(function () {
            document.getElementById("selectedMenuPanel").setAttribute("class", "panel panel-info hidden")
            document.getElementById("StatusPanel").setAttribute("class", "panel panel-info")
            alert("启动定时器，每天0点开始爬取！")
            queryStateSchedule()
        })
    })
}
function queryState() {
    var strStatus = ipPort + "/closing/getSpiderStatus"
    var div_scroll = document.getElementById("Status")
    var getState = {
        url:strStatus,
        dataType:'json',
        success:function (obj) {
            createPStatus(obj)
            div_scroll.scrollTop = div_scroll.scrollHeight
            if(obj.data.code != 11) {
                var sumTime = obj.data.endTime - obj.data.startTime
                var hour = (sumTime / 3600000).toFixed(0)
                sumTime = sumTime % 3600000
                var min = (sumTime / 60000).toFixed(0)
                sumTime = sumTime % 60000
                var second = (sumTime / 1000).toFixed(0)
                alert("爬取结束！爬取总时间为 " + hour + "小时 " + min + "分钟 " + second + "秒")
                clearInterval(intervalID)
                return
            }
        }
    }
    intervalID = window.setInterval(function () {
        $.ajax(getState)
    }, 5000)
}
function queryStateSchedule() {
    var strStatus = ipPort + "/closing/getSpiderStatus"
    var div_scroll = document.getElementById("Status")
    var flag = 0
    var getState = {
        url:strStatus,
        dataType:'json',
        success:function (obj) {
            if(obj.data.code == 11){
                createPStatus(obj)
                div_scroll.scrollTop = div_scroll.scrollHeight
                flag = 1
            }else if(flag == 1 && obj.data.code != 11){
                var sumTime = obj.data.endTime - obj.data.startTime
                var hour = (sumTime / 3600000).toFixed(0)
                sumTime = sumTime % 3600000
                var min = (sumTime / 60000).toFixed(0)
                sumTime = sumTime % 60000
                var second = (sumTime / 1000).toFixed(0)
                alert("爬取结束！爬取总时间为 " + hour + "小时 " + min + "分钟 " + second + "秒")
                clearInterval(intervalID)
                return
            }
        }
    }
    intervalID = window.setInterval(function () {
        $.ajax(getState)
    }, 5000)
}
function getParm(str) {
    var url = location.href
    var tmp1 = url.split("?")[1]
    if(tmp1){
        var tmp = tmp1.split("&")
        var flag = 0
        for(var i in tmp){
            if((tmp[i].split("="))[1] == "")continue
            if(flag == 0){
                flag = 1
                str = str + (tmp[i].split("="))[0] + "=" + (tmp[i].split("="))[1]
            }
            else {
                str = str + "&" + (tmp[i].split("="))[0] + "=" + (tmp[i].split("="))[1]
            }
        }
    }
    return str
}
