var xmlhttp;
var tableSize = 200
$(document).ready(function () {
    var secondDate = new Date()
    secondDate.setDate(secondDate.getDate() - 1)
    document.getElementById("getFirstDate").valueAsDate = secondDate
    document.getElementById("getSecondDate").valueAsDate = new Date()
    defaultSearchAll()
})
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
        var liOnclick = "getMenuId(" + obj.data[i].id + ")"
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
        var liOnclick = "getMenuId(" + obj.data[i].id + ")"
        liElement.setAttribute("onclick", liOnclick)
        var node = document.createTextNode(obj.data[i].name);
        liElement.appendChild(node);
        parentElement.appendChild(liElement);
    }
}

function defaultSearchAll() {
    var firstDate = getFirstDate()
    if(firstDate == 0)return
    var secondDate = getSecondDate()
    if(secondDate == 0)return
    var menuId = document.getElementById("table-over").getAttribute("dataId")
    var page = document.getElementById("table-over").getAttribute("value") - 1
    var size = 10
    var str = ipPort + "/closing/getByMenuIdAndStartEndDateAndRankDetaByPage?menuId=" + menuId +
        "&startDate=" + firstDate + "&endDate=" + secondDate
    runAjax(str, function () {
        if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
            document.getElementById("table-over").setAttribute("whichWay", "searchAll")
            var resultString = xmlhttp.responseText;
            var obj = JSON.parse(resultString);
            if(obj.code == 20){
                return
            }
            setTable(obj)
        }
    });
}
function searchAll() {
    var firstDate = getFirstDate()
    if(firstDate == 0)return
    var secondDate = getSecondDate()
    if(secondDate == 0)return
    var menuId = document.getElementById("table-over").getAttribute("dataId")
    if(menuId == ""){
        alert("请选择菜单！")
        return
    }
    var page = document.getElementById("table-over").getAttribute("value") - 1
    var size = 10
    var str = ipPort + "/closing/getByMenuIdAndStartEndDateAndRankDetaByPage?menuId=" + menuId +
        "&startDate=" + firstDate + "&endDate=" + secondDate
    runAjax(str, function () {
        if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
            document.getElementById("table-over").setAttribute("whichWay", "searchAll")
            var resultString = xmlhttp.responseText;
            var obj = JSON.parse(resultString);
            if(obj.code == 20){
                var tableTr = document.getElementsByClassName("table-tr")
                for(var i = 0; i < tableSize; i++){
                    tableTr[i].setAttribute("class", "table-tr hidden")
                }
                alert(obj.message)
                return
            }
            setTable(obj)
        }
    });
}
function searching() {
    var firstDate = getFirstDate()
    if(firstDate == 0)return
    var secondDate = getSecondDate()
    if(secondDate == 0)return
    var menuId = document.getElementById("table-over").getAttribute("dataId")
    if(menuId == ""){
        alert("请选择菜单！")
        return
    }
    var rankDeta = ($('#rankChange-input').val())
    if(rankDeta == ""){
        alert("请输入排名变化！")
        return
    }
    var page = document.getElementById("table-over").getAttribute("value") - 1
    var size = 10
    var str = ipPort + "/closing/getByMenuIdAndStartEndDateAndRankDetaByPage?menuId=" + menuId +
        "&startDate=" + firstDate + "&endDate=" + secondDate + "&rankDeta=" + rankDeta
    runAjax(str, function () {
        if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
            document.getElementById("table-over").setAttribute("whichWay", "searching")
            var resultString = xmlhttp.responseText;
            var obj = JSON.parse(resultString);
            if(obj.code == 20){
                var tableTr = document.getElementsByClassName("table-tr")
                for(var i = 0; i < tableSize; i++){
                    tableTr[i].setAttribute("class", "table-tr hidden")
                }
                alert(obj.message)
                return
            }
            setTable(obj)
        }
    });
}
function rankIns() {
    var firstDate = getFirstDate()
    if(firstDate == 0)return
    var secondDate = getSecondDate()
    if(secondDate == 0)return
    var menuId = document.getElementById("table-over").getAttribute("dataId")
    if(menuId == ""){
        alert("请选择菜单！")
        return
    }
    var page = document.getElementById("table-over").getAttribute("value") - 1
    var size = 10
    var str = ipPort + "/closing/getByMenuIdAndStartEndDateAndRankDetaByPage?menuId=" + menuId +
        "&startDate=" + firstDate + "&endDate=" + secondDate + "&rankDeta=" + 1
    runAjax(str, function () {
        if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
            document.getElementById("table-over").setAttribute("whichWay", "rankIns")
            var resultString = xmlhttp.responseText;
            var obj = JSON.parse(resultString);
            if(obj.code == 20){
                var tableTr = document.getElementsByClassName("table-tr")
                for(var i = 0; i < tableSize; i++){
                    tableTr[i].setAttribute("class", "table-tr hidden")
                }
                alert(obj.message)
                return
            }
            setTable(obj)
        }
    });
}
function rankDes() {
    var firstDate = getFirstDate()
    if(firstDate == 0)return
    var secondDate = getSecondDate()
    if(secondDate == 0)return
    var menuId = document.getElementById("table-over").getAttribute("dataId")
    if(menuId == ""){
        alert("请选择菜单！")
        return
    }
    var page = document.getElementById("table-over").getAttribute("value") - 1
    var size = 10
    var str = ipPort + "/closing/getByMenuIdAndStartEndDateAndRankDetaByPage?menuId=" + menuId +
        "&startDate=" + firstDate + "&endDate=" + secondDate + "&rankDeta=" + (-1)
    runAjax(str, function () {
        if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
            document.getElementById("table-over").setAttribute("whichWay", "rankDes")
            var resultString = xmlhttp.responseText;
            var obj = JSON.parse(resultString);
            if(obj.code == 20){
                var tableTr = document.getElementsByClassName("table-tr")
                for(var i = 0; i < tableSize; i++){
                    tableTr[i].setAttribute("class", "table-tr hidden")
                }
                alert(obj.message)
                return
            }
            setTable(obj)
        }
    });
}
function getMenuId(menuId) {
    document.getElementById("table-over").setAttribute("dataId", menuId)
    var str_menu = ipPort + "/menu/getById?id=" + menuId
    getMenuPath(str_menu)
}
function setTable(obj) {
    var goodsRanking = document.getElementsByClassName("goods-ranking")
    var goodsNameA = document.getElementsByClassName("goods-name-a")
    var goodsBrand = document.getElementsByClassName("goods-brand")
    var goodsPrice = document.getElementsByClassName("goods-price")
    var goodsStar = document.getElementsByClassName("goods-star")
    var goodsRankChange = document.getElementsByClassName("goods-rankChange")
    var tableTr = document.getElementsByClassName("table-tr")
    for(var i = 0; i < obj.data.length; i++){
        tableTr[i].setAttribute("class", "table-tr")
        var ranking = obj.data[i].rank
        var name = obj.data[i].name
        var brand = obj.data[i].brand
        var price = "$" + obj.data[i].priceLower + " - " + obj.data[i].priceHigher
        var star = obj.data[i].star
        var rankChange = obj.data[i].rankDeta
        var id = obj.data[i].id
        var clickFunctionName = "setDetails(" + id + ")"
        goodsRanking[i].innerHTML = ranking
        goodsNameA[i].setAttribute("onclick", clickFunctionName)
        goodsNameA[i].innerHTML = name
        goodsBrand[i].innerHTML = brand
        goodsPrice[i].innerHTML = price
        goodsStar[i].innerHTML = star
        goodsRankChange[i].innerHTML = rankChange
    }
    for(var i = obj.data.length; i < tableSize; i++){
        tableTr[i].setAttribute("class", "table-tr hidden")
    }
}
function clickPageButton(pageId) {
    document.getElementById("table-over").setAttribute("value",pageId)
    var elements = document.getElementsByClassName("pageIndex-class")
    for(var i = 0; i < 10; i++){
        if(pageId == i + 1)elements[i].setAttribute("class","pageIndex-class active")
        else{
            elements[i].setAttribute("class", "pageIndex-class")
        }
    }
    var whichWay = document.getElementById("table-over").getAttribute("whichWay")
    switch (whichWay){
        case "searchAll":searchAll();break;
        case "searching":searching();break;
        case "rankIns":rankIns();break;
        case "rankDes":rankDes();break;
    }
}
function setDetails(goodsId) {
    window.location.href = "showDetails.html?goodsId=" + goodsId
}
function getFirstDate() {
    var d = new Date(($('#getFirstDate').val()))
    if(d == "Invalid Date"){
        alert("请输入第一个日期！")
        return 0
    }
    var n = d.getTime()
    return n
}
function getSecondDate() {
    var d = new Date(($('#getSecondDate').val()))
    if(d == "Invalid Date"){
        alert("请输入第二个日期！")
        return 0
    }
    var n = d.getTime()
    return n
}