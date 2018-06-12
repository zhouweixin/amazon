$(document).ready(function () {
    /*默认设置*/
    document.getElementById("getDataDate").valueAsDate = new Date()
    defaultSetImgUrl()

    var sub2 = $('#sub2')
    var activeRow
    var activeMenu

    var timer
    var mouseInSub = false

    $('#sub2')
        .on("mouseenter", function (e) {
            mouseInSub =true
        })
        .on("mouseleave", function () {
            mouseInSub = false
        })

    var mouseTrack = []

    var moveHandler = function(e){
        mouseTrack.push({
            x:e.pageX,
            y:e.pageY
        })
        if(mouseTrack.length > 3){
            mouseTrack.shift()
        }
    }
    $('#sub1-ul li')
        .on("mouseenter", function (e) {
            sub2.removeClass('none')

            $(document).bind("mousemove", moveHandler)
        })
        .on("mouseleave", function (e) {
            $(document).unbind("mousemove", moveHandler)
        })
    $('#sub1')
        .on("mouseleave", function (e) {
            sub2.addClass('none')
            if(activeRow){
                activeRow.removeClass('active')
                activeRow = null
            }
            if(activeMenu){
                activeMenu.addClass('none')
                activeMenu = null
            }

            $(document).unbind("mousemove", moveHandler)
        })
        .on("mouseenter", "#sub1-ul li", function (e) {
            if(!activeRow){
                activeRow = $(e.target)
                activeRow.addClass('active')
                getSubMenu(1,'sub2-a-ul');
                activeMenu = $("#" + activeRow.data('id'))
                activeMenu.removeClass('none')
                return
            }
            if(timer){
                clearTimeout(timer)
            }
            var currMousePos = mouseTrack[mouseTrack.length - 1]
            var leftCorner = mouseTrack[mouseTrack.length - 2]
            var delay = needDelay(sub2, leftCorner, currMousePos)

            if(delay){
                timer = setTimeout(function () {
                    if(mouseInSub){
                        return
                    }
                    activeRow.removeClass('active')
                    activeMenu.addClass('none')
                    activeRow = $(e.target)
                    activeRow.addClass('active')
                    activeMenu = $('#' + activeRow.data('id'))
                    activeMenu.removeClass('none')
                    timer = null
                }, 300)
            }else{
                var prevActiveRow = activeRow
                var prevActiveMenu = activeMenu

                activeRow = $(e.target)
                activeMenu = $('#' + activeRow.data('id'))
                prevActiveRow.removeClass('active')
                prevActiveMenu.addClass('none')
                activeRow.addClass('active')
                activeMenu.removeClass('none')
            }
        })



    var sub3 = $('#sub3')
    var activeRow2
    var activeMenu2

    var timer2
    var mouseInSub2 = false

    $('#sub3')
        .on("mouseenter", function (e) {
            mouseInSub2 =true
        })
        .on("mouseleave", function () {
            mouseInSub2 = false
        })

    var mouseTrack2 = []

    var moveHandler2 = function(e){
        mouseTrack2.push({
            x:e.pageX,
            y:e.pageY
        })
        if(mouseTrack2.length > 3){
            mouseTrack2.shift()
        }
    }
    $('#sub2-a-ul')
        .on("mouseenter", function (e) {
            sub3.removeClass('none')

            $(document).bind("mousemove", moveHandler2)
        })
        .on("mouseleave", function (e) {
            $(document).unbind("mousemove", moveHandler2)
        })
    $('#sub2')
        .on("mouseleave", function (e) {
            // sub3.addClass('none');
            document.getElementById("sub3").setAttribute("class", "none")
            if(activeRow2){
                activeRow2.removeClass('active')
                activeRow2 = null
            }
            if(activeMenu2){
                // activeMenu2.addClass('none')
                document.getElementById("sub3a").setAttribute("class", "none")
                activeMenu2 = null
            }

            $(document).unbind("mousemove", moveHandler2)
        })
        .on("mouseenter", "#sub2-a-ul li", function (e) {
            if(!activeRow2){
                activeRow2 = $(e.target)
                // var top = activeRow2.offsetTop
                // console.log(top)
                activeRow2.addClass('active')
                // console.log("12987248724189")
                var id = activeRow2.data('id')
                getSubMenu2(id, "sub2-a-ul", "sub3-a-ul",3, activeRow,activeRow2)
                // console.log(activeRow2.data('id'))
                activeMenu2 = $("#sub3a" )
                   // + activeRow2.data('id')
                activeMenu2.removeClass('none')
                return
            }
            if(timer2){
                clearTimeout(timer2)
            }
            var currMousePos2 = mouseTrack2[mouseTrack2.length - 1]
            var leftCorner2 = mouseTrack2[mouseTrack2.length - 2]
            // var delay2 = needDelay(sub3, leftCorner2, currMousePos2)

            if(0){
                timer2 = setTimeout(function () {
                    if(mouseInSub2){
                        return
                    }
                    activeRow2.removeClass('active')
                    activeMenu2.addClass('none')
                    activeRow2 = $(e.target)
                    activeRow2.addClass('active')
                    activeMenu2 = $('#' + activeRow2.data('id'))
                    activeMenu2.removeClass('none')
                    timer2 = null
                }, 300)
            }else{
                var prevActiveRow2 = activeRow2
                var prevActiveMenu2 = activeMenu2

                activeRow2 = $(e.target)
                var id = activeRow2.data('id')
                getSubMenu2(id, "sub2-a-ul", "sub3-a-ul", 3,  activeRow,activeRow2)
                activeMenu2 = $('#sub3a')
                    //+ activeRow2.data('id')
                prevActiveRow2.removeClass('active')
                prevActiveMenu2.addClass('none')
                activeRow2.addClass('active')
                activeMenu2.removeClass('none')
            }
        })




    var sub4 = $('#sub4')
    var activeRow3
    var activeMenu3

    var timer3
    var mouseInSub3 = false

    $('#sub4')
        .on("mouseenter", function (e) {
            mouseInSub3 =true
        })
        .on("mouseleave", function () {
            mouseInSub3 = false
        })

    var mouseTrack3 = []

    var moveHandler3 = function(e){
        mouseTrack3.push({
            x:e.pageX,
            y:e.pageY
        })
        if(mouseTrack3.length > 3){
            mouseTrack3.shift()
        }
    }
    $('#sub3-a-ul')
        .on("mouseenter", function (e) {
            sub4.removeClass('none')

            $(document).bind("mousemove", moveHandler3)
        })
        .on("mouseleave", function (e) {
            $(document).unbind("mousemove", moveHandler3)
        })
    $('#sub3')
        .on("mouseleave", function (e) {
            // sub3.addClass('none');
            document.getElementById("sub4").setAttribute("class", "none")
            if(activeRow3){
                activeRow3.removeClass('active')
                activeRow3 = null
            }
            if(activeMenu3){
                // activeMenu2.addClass('none')
                document.getElementById("sub4a").setAttribute("class", "none")
                activeMenu3 = null
            }

            $(document).unbind("mousemove", moveHandler3)
        })
        .on("mouseenter", "#sub3-a-ul li", function (e) {
            if(!activeRow3){
                activeRow3 = $(e.target)
                // var top = activeRow2.offsetTop
                // console.log(top)
                activeRow3.addClass('active')

                var id = activeRow3.data('id')
                getSubMenu2(id, "sub3-a-ul", "sub4-a-ul", 4,  activeRow2,activeRow3)
                // console.log(activeRow2.data('id'))
                activeMenu3 = $("#sub4a")
                activeMenu3.removeClass('none')
                return
            }
            if(timer3){
                clearTimeout(timer3)
            }
            var currMousePos3 = mouseTrack3[mouseTrack3.length - 1]
            var leftCorner3 = mouseTrack3[mouseTrack3.length - 2]
            // var delay2 = needDelay(sub3, leftCorner2, currMousePos2)

            if(0){
                timer3 = setTimeout(function () {
                    if(mouseInSub3){
                        return
                    }
                    activeRow3.removeClass('active')
                    activeMenu3.addClass('none')
                    activeRow3 = $(e.target)
                    activeRow3.addClass('active')
                    activeMenu3 = $('#' + activeRow3.data('id'))
                    activeMenu3.removeClass('none')
                    timer3 = null
                }, 300)
            }else{
                var prevActiveRow3 = activeRow3
                var prevActiveMenu3 = activeMenu3

                activeRow3 = $(e.target)
                var id = activeRow3.data('id')
                getSubMenu2(id, "sub3-a-ul", "sub4-a-ul", 4,  activeRow2,activeRow3)
                activeMenu3 = $('#sub4a')
                prevActiveRow3.removeClass('active')
                prevActiveMenu3.addClass('none')
                activeRow3.addClass('active')
                activeMenu3.removeClass('none')
            }
        })

    var sub5 = $('#sub5')
    var activeRow4
    var activeMenu4

    var timer4
    var mouseInSub4 = false

    $('#sub5')
        .on("mouseenter", function (e) {
            mouseInSub4 =true
        })
        .on("mouseleave", function () {
            mouseInSub4 = false
        })

    var mouseTrack4 = []

    var moveHandler4 = function(e){
        mouseTrack4.push({
            x:e.pageX,
            y:e.pageY
        })
        if(mouseTrack4.length > 3){
            mouseTrack4.shift()
        }
    }
    $('#sub4-a-ul')
        .on("mouseenter", function (e) {
            sub5.removeClass('none')

            $(document).bind("mousemove", moveHandler4)
        })
        .on("mouseleave", function (e) {
            $(document).unbind("mousemove", moveHandler4)
        })
    $('#sub4')
        .on("mouseleave", function (e) {
            // sub3.addClass('none');
            document.getElementById("sub5").setAttribute("class", "none")
            if(activeRow4){
                activeRow4.removeClass('active')
                activeRow4 = null
            }
            if(activeMenu4){
                // activeMenu2.addClass('none')
                document.getElementById("sub5a").setAttribute("class", "none")
                activeMenu4 = null
            }

            $(document).unbind("mousemove", moveHandler4)
        })
        .on("mouseenter", "#sub4-a-ul li", function (e) {
            if(!activeRow4){
                activeRow4 = $(e.target)
                // var top = activeRow2.offsetTop
                // console.log(top)
                activeRow4.addClass('active')

                var id = activeRow4.data('id')
                getSubMenu2(id, "sub4-a-ul", "sub5-a-ul", 5,  activeRow3,activeRow4)
                // console.log(activeRow2.data('id'))
                activeMenu4 = $("#sub5a")
                activeMenu4.removeClass('none')
                return
            }
            if(timer4){
                clearTimeout(timer4)
            }
            var currMousePos4 = mouseTrack4[mouseTrack4.length - 1]
            var leftCorner4 = mouseTrack4[mouseTrack4.length - 2]
            // var delay2 = needDelay(sub3, leftCorner2, currMousePos2)

            if(0){
                timer4 = setTimeout(function () {
                    if(mouseInSub4){
                        return
                    }
                    activeRow4.removeClass('active')
                    activeMenu4.addClass('none')
                    activeRow4 = $(e.target)
                    activeRow4.addClass('active')
                    activeMenu4 = $('#' + activeRow4.data('id'))
                    activeMenu4.removeClass('none')
                    timer4 = null
                }, 300)
            }else{
                var prevActiveRow4 = activeRow4
                var prevActiveMenu4 = activeMenu4

                activeRow4 = $(e.target)
                var id = activeRow4.data('id')
                getSubMenu2(id, "sub4-a-ul", "sub5-a-ul", 5,  activeRow3,activeRow4)
                activeMenu4 = $('#sub5a')
                prevActiveRow4.removeClass('active')
                prevActiveMenu4.addClass('none')
                activeRow4.addClass('active')
                activeMenu4.removeClass('none')
            }
        })




    var sub6 = $('#sub6')
    var activeRow5
    var activeMenu5

    var timer5
    var mouseInSub5 = false

    $('#sub6')
        .on("mouseenter", function (e) {
            mouseInSub5 =true
        })
        .on("mouseleave", function () {
            mouseInSub5 = false
        })

    var mouseTrack5 = []

    var moveHandler5 = function(e){
        mouseTrack5.push({
            x:e.pageX,
            y:e.pageY
        })
        if(mouseTrack5.length > 3){
            mouseTrack5.shift()
        }
    }
    $('#sub5-a-ul')
        .on("mouseenter", function (e) {
            sub6.removeClass('none')

            $(document).bind("mousemove", moveHandler5)
        })
        .on("mouseleave", function (e) {
            $(document).unbind("mousemove", moveHandler5)
        })
    $('#sub5')
        .on("mouseleave", function (e) {
            // sub3.addClass('none');
            document.getElementById("sub6").setAttribute("class", "none")
            if(activeRow5){
                activeRow5.removeClass('active')
                activeRow5 = null
            }
            if(activeMenu5){
                // activeMenu2.addClass('none')
                document.getElementById("sub6a").setAttribute("class", "none")
                activeMenu5 = null
            }

            $(document).unbind("mousemove", moveHandler5)
        })
        .on("mouseenter", "#sub5-a-ul li", function (e) {
            if(!activeRow5){
                activeRow5 = $(e.target)
                // var top = activeRow2.offsetTop
                // console.log(top)
                activeRow5.addClass('active')

                var id = activeRow5.data('id')
                getSubMenu2(id, "sub5-a-ul", "sub6-a-ul", 6,  activeRow4,activeRow5)
                // console.log(activeRow2.data('id'))
                activeMenu5 = $("#sub6a")
                activeMenu5.removeClass('none')
                return
            }
            if(timer5){
                clearTimeout(timer5)
            }
            var currMousePos5 = mouseTrack5[mouseTrack5.length - 1]
            var leftCorner5 = mouseTrack5[mouseTrack5.length - 2]
            // var delay2 = needDelay(sub3, leftCorner2, currMousePos2)

            if(0){
                timer5 = setTimeout(function () {
                    if(mouseInSub5){
                        return
                    }
                    activeRow5.removeClass('active')
                    activeMenu5.addClass('none')
                    activeRow5 = $(e.target)
                    activeRow5.addClass('active')
                    activeMenu5 = $('#' + activeRow5.data('id'))
                    activeMenu5.removeClass('none')
                    timer5 = null
                }, 300)
            }else{
                var prevActiveRow5 = activeRow5
                var prevActiveMenu5 = activeMenu5

                activeRow5 = $(e.target)
                var id = activeRow5.data('id')
                getSubMenu2(id, "sub5-a-ul", "sub6-a-ul", 6,  activeRow4,activeRow5)
                activeMenu5 = $('#sub6a')
                prevActiveRow5.removeClass('active')
                prevActiveMenu5.addClass('none')
                activeRow5.addClass('active')
                activeMenu5.removeClass('none')
            }
        })




    var activeRow6
    var activeMenu6

    var timer6
    var mouseInSub6 = false

    var mouseTrack6 = []

    var moveHandler6 = function(e){
        mouseTrack6.push({
            x:e.pageX,
            y:e.pageY
        })
        if(mouseTrack6.length > 3){
            mouseTrack6.shift()
        }
    }
    $('#sub6-a-ul')
        .on("mouseenter", function (e) {
            // sub6.removeClass('none')

            $(document).bind("mousemove", moveHandler6)
        })
        .on("mouseleave", function (e) {
            $(document).unbind("mousemove", moveHandler6)
        })
    $('#sub6')
        .on("mouseleave", function (e) {
            // sub3.addClass('none');
            // document.getElementById("sub6").setAttribute("class", "none")
            if(activeRow6){
                activeRow6.removeClass('active')
                activeRow6 = null
            }
            // if(activeMenu5){
            //     // activeMenu2.addClass('none')
            //     document.getElementById("sub6a").setAttribute("class", "none")
            //     activeMenu5 = null
            // }

            $(document).unbind("mousemove", moveHandler6)
        })
        .on("mouseenter", "#sub6-a-ul li", function (e) {
            if(!activeRow6){
                activeRow6 = $(e.target)
                // var top = activeRow2.offsetTop
                // console.log(top)
                activeRow6.addClass('active')

                var id = activeRow6.data('id')
                // getSubMenu2(id, "sub5-a-ul", "sub6-a-ul", 6,  activeRow4,activeRow5)
                // console.log(activeRow2.data('id'))
                // activeMenu6 = $("#sub6a")
                // activeMenu6.removeClass('none')
                return
            }
            if(timer5){
                clearTimeout(timer5)
            }
            var currMousePos5 = mouseTrack5[mouseTrack5.length - 1]
            var leftCorner5 = mouseTrack5[mouseTrack5.length - 2]
            // var delay2 = needDelay(sub3, leftCorner2, currMousePos2)

            if(0){
                timer5 = setTimeout(function () {
                    if(mouseInSub5){
                        return
                    }
                    activeRow5.removeClass('active')
                    activeMenu5.addClass('none')
                    activeRow5 = $(e.target)
                    activeRow5.addClass('active')
                    activeMenu5 = $('#' + activeRow5.data('id'))
                    activeMenu5.removeClass('none')
                    timer5 = null
                }, 300)
            }else{
                var prevActiveRow6 = activeRow6
                var prevActiveMenu6 = activeMenu6

                activeRow6 = $(e.target)
                var id = activeRow6.data('id')
                // getSubMenu2(id, "sub5-a-ul", "sub6-a-ul", 6,  activeRow4,activeRow5)
                // activeMenu5 = $('#sub6a')
                prevActiveRow6.removeClass('active')
                // prevActiveMenu6.addClass('none')
                activeRow6.addClass('active')
                // activeMenu6.removeClass('none')
            }
        })
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
function getRootMenu(str) {
    str = ipPort + "/menu/getById?id=" + str;
    runAjax(str, function () {
        if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
            var resultString = xmlhttp.responseText;
            var obj = JSON.parse(resultString);
            console.log(obj)
            document.getElementById("sub1-ul-li").innerHTML = obj.data.name;
            var width_ = document.getElementById("sub1-ul").offsetWidth;
            document.getElementById("sub2").style.left = width_ + "px";
            document.getElementById("sub2").style.top = (33) + "px";
        }
    });
}
function getSubMenu(id, foresubStr) {
    id = ipPort + "/menu/getSonMenuById?id=" + id;
    runAjax(id, function () {
        if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
            var resultString = xmlhttp.responseText;
            var obj = JSON.parse(resultString);
            var i;
            createMenuElements(foresubStr, obj);
        }
    });
}

function createMenuElements(foresubStr, obj) {
    var i;
    var parentElement = document.getElementById(foresubStr);
    $("li").remove(".subMenu-ul-li");
    for(i in obj.data){
        //console.log(i)
        var liElement = document.createElement("li");
        liElement.setAttribute("class", "subMenu-ul-li");
        liElement.setAttribute("data-id", obj.data[i].id);
        var liOnclick = "selectMenu(" + obj.data[i].id + ")"
        liElement.setAttribute("onclick", liOnclick)
        var node = document.createTextNode(obj.data[i].name);
        liElement.appendChild(node);
        parentElement.appendChild(liElement);
    }
}

function getSubMenu2(id,parentElement_id, str, n,  PactiveRow, activeRow) {
    id = ipPort + "/menu/getSonMenuById?id=" + id;
    runAjax(id, function () {
        if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
            var resultString = xmlhttp.responseText;
            var obj = JSON.parse(resultString);
            createMenuElements2(n, str, obj);
            var parent = "#sub" + (n - 1)
            var top1 = $(parent).offset().top
            var width_ = document.getElementById(parentElement_id).offsetWidth;
            var top_ =  activeRow.offset().top - top1 - 2
            var divId = "sub" + n
            document.getElementById(divId).style.left = width_ + "px";
            document.getElementById(divId).style.top = top_ + "px";
        }
    });
}

function createMenuElements2(n, str, obj) {
    var i;
    var parentElement = document.getElementById(str);
    str = str + "-li"
    $("li").remove("." + str);
    for(i in obj.data){
        //console.log(i)
        var liElement = document.createElement("li");
        liElement.setAttribute("class", str);
        liElement.setAttribute("data-id", obj.data[i].id);
        var liOnclick = "selectMenu(" + obj.data[i].id + ")"
        liElement.setAttribute("onclick", liOnclick)
        var node = document.createTextNode(obj.data[i].name);
        liElement.appendChild(node);
        parentElement.appendChild(liElement);
    }
}
function changeImgUrl() {
    var menuId = document.getElementById("imgs-div").getAttribute("dataId")
    setImgUrl(menuId)
}
function defaultSetImgUrl() {
    var menuId = document.getElementById("imgs-div").getAttribute("dataId")
    var downloadDate = getDate()
    if(downloadDate == 0)return
    var page = document.getElementById("imgs-div").getAttribute("value") - 1
    var size = 20
    var url = ipPort + "/closing/getByMenuIdAndDownloadDateByPage?menuId=" + menuId + "&downloadDate=" + downloadDate + "&page=" + page + "&size=" + size
    $.get(url).done(function (obj) {
        if(obj.data.content.length == 0){
            return
        }
        var img_a_class = document.getElementsByClassName("img-a-class")
        var imgs_class = document.getElementsByClassName("showImg-class")
        var name_price_class = document.getElementsByClassName("name-price")
        for(var i = 0, j = 0; i < obj.data.content.length; i++, j++){
            var name = obj.data.content[i].name
            var src = obj.data.content[i].mainImageUrl
            var id = obj.data.content[i].id
            var clickFunctionName = "setDetails(" + id + ")"
            img_a_class[j].setAttribute("onclick",clickFunctionName)
            imgs_class[j].setAttribute("src",src)
            name_price_class[j].innerHTML = name
        }
    })
}
function setImgUrl(menuId) {
    var downloadDate = getDate()
    if(downloadDate == 0)return
    var page = document.getElementById("imgs-div").getAttribute("value") - 1
    var size = 20
    var url = ipPort + "/closing/getByMenuIdAndDownloadDateByPage?menuId=" + menuId + "&downloadDate=" + downloadDate + "&page=" + page + "&size=" + size
    $.get(url).done(function (obj) {
        if(obj.data.content.length == 0){
            alert("没有查询到相关内容！")
            return
        }
        var img_a_class = document.getElementsByClassName("img-a-class")
        var imgs_class = document.getElementsByClassName("showImg-class")
        var name_price_class = document.getElementsByClassName("name-price")
        for(var i = 0, j = 0; i < obj.data.content.length; i++, j++){
            var name = obj.data.content[i].name
            var src = obj.data.content[i].mainImageUrl
            var id = obj.data.content[i].id
            var clickFunctionName = "setDetails(" + id + ")"
            img_a_class[j].setAttribute("onclick",clickFunctionName)
            imgs_class[j].setAttribute("src",src)
            name_price_class[j].innerHTML = name
        }
    })
}

function clickPageButton(pageId) {
    document.getElementById("imgs-div").setAttribute("value",pageId)
    var menuId = document.getElementById("imgs-div").getAttribute("dataId")
    var elements = document.getElementsByClassName("pageIndex-class")
    for(var i = 0; i < 5; i++){
        if(pageId == i + 1)elements[i].setAttribute("class","pageIndex-class active")
        else{
            elements[i].setAttribute("class", "pageIndex-class")
        }
    }
    setImgUrl(menuId)
}

function setDetails(goodsId) {
    window.location.href = "showDetails.html?goodsId=" + goodsId
}

function selectMenu(menuId) {
    document.getElementById("imgs-div").setAttribute("dataId",menuId)
    var str_menu = ipPort + "/menu/getById?id=" + menuId
    getMenuPath(str_menu)
}
function getDate() {
    var d = new Date(($('#getDataDate').val()))
    if(d == "Invalid Date"){
        alert("请输入日期！")
        return 0
    }
    var n = d.getTime()
    return n
}