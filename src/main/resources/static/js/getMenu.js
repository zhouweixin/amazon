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
        var liOnclick = "changeEcharts(" + obj.data[i].id + ")"
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
        var liOnclick = "changeEcharts(" + obj.data[i].id + ")"
        liElement.setAttribute("onclick", liOnclick)
        var node = document.createTextNode(obj.data[i].name);
        liElement.appendChild(node);
        parentElement.appendChild(liElement);
    }
}

//爬虫界面
function getSpiderSubMenu(parentId, number) {
    // console.log(parentId)
    var parentLiId = parentId
    parentId = ipPort + "/menu/getSonMenuById?id=" + parentId;
    runAjax(parentId, function () {
        if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
            var resultString = xmlhttp.responseText;
            var obj = JSON.parse(resultString);
            // console.log(obj)
            var htmlParentId = parentLiId + "-li"
            // console.log(htmlParentId)
            createSpiderSubMenu(number, obj, htmlParentId);
        }
    });
}


function createSpiderSubMenu(number, obj, htmlParentId) {
    var i;
    var parentElement = document.getElementById(htmlParentId);
    var ulId = "ul-" + (number + 1)
    $("ul").remove("#" + ulId);
    var UlElement = document.createElement("ul")
    UlElement.setAttribute("class", "nav");
    // var sonId = htmlParentId + "-ul"
    UlElement.setAttribute("id", ulId)
    var liClass = "ul-" + (number + 1) + "-li"
    for(i in obj.data){
        var liElement = document.createElement("li");
        liElement.setAttribute("class", liClass);
        var liId = obj.data[i].id + "-li"
        liElement.setAttribute("id", liId);
        var dataId = obj.data[i].id
        liElement.setAttribute("data-id",dataId );
        UlElement.appendChild(liElement);

        var aElement = document.createElement("a")
        // aElement.addEventListener("click", function () { activeMenu2(dataId) })
        var onclickFunction = "activeMenu" + (number + 1) + "(" + dataId + ")"
        aElement.setAttribute("onclick", onclickFunction)
        aElement.setAttribute("href", "#")
        var node = document.createTextNode(obj.data[i].name);
        aElement.appendChild(node)
        liElement.appendChild(aElement);
    }
    parentElement.appendChild(UlElement)
    var setbackColor = "ul-" + (number + 1)
    var setbackColorP = "ul-" + number

    document.getElementById(setbackColorP).style.background = "#FFFFFF"
    document.getElementById(setbackColor).style.background = "#F8F8F8"

    document.getElementById(setbackColorP).style.fontWeight = "bold"
    document.getElementById(setbackColor).style.fontWeight = "normal"
    createCheckbox(obj)

}

function createCheckbox(obj) {
    var i
    var parentElement = document.getElementById("checkbox-form");
    $("div").remove("#checkbox-parent-div");
    var parentDivElement = document.createElement("div")
    parentDivElement.setAttribute("id", "checkbox-parent-div")
    parentDivElement.setAttribute("class", "")
    var parentDivContainerElement = document.createElement("div")
    parentDivContainerElement.setAttribute("class", "")
    parentDivElement.appendChild(parentDivContainerElement)
    for(i in obj.data){
        var checkOutDivElement = document.createElement("div")
        checkOutDivElement.setAttribute("class", "col-xs-12")
        var checkDivElement = document.createElement("div")
        checkDivElement.setAttribute("class", "checkbox")
        checkOutDivElement.appendChild(checkDivElement)
        var labelElement = document.createElement("label")
        checkDivElement.appendChild(labelElement)
        var inputElement = document.createElement("input")
        inputElement.setAttribute("class","checkboxMenu");
        inputElement.setAttribute("type", "checkbox")
        var dataId = obj.data[i].id
        inputElement.setAttribute("iddata",dataId );
        inputElement.setAttribute("url",obj.data[i].url );
        inputElement.setAttribute("value",obj.data[i].name);
        inputElement.setAttribute("onclick", "checkboxOnclick(this)");
        labelElement.appendChild(inputElement)
        var node = document.createTextNode(obj.data[i].name);
        labelElement.appendChild(node)
        parentDivContainerElement.appendChild(checkOutDivElement)
    }
    parentElement.appendChild(parentDivElement)
}

function createPMenuName(checkbox) {
    var parentElement = document.getElementById("selectedMenu");
    var pElement = document.createElement("p")
    var selectedMenuId = checkbox.getAttribute("iddata") + "-p"
    pElement.setAttribute("id",selectedMenuId)
    pElement.setAttribute("class","selectedMenuName")
    pElement.setAttribute("url",checkbox.getAttribute("url"))
    pElement.setAttribute("dataid",checkbox.getAttribute("iddata"))
    var pValue = checkbox.getAttribute("iddata") + "-" + checkbox.getAttribute("value")
    var node = document.createTextNode(pValue)
    pElement.appendChild(node)
    parentElement.appendChild(pElement)
}

function createPStatus(obj) {
    var parentElement = document.getElementById("Status");
    var pElement = document.createElement("p")
    pElement.setAttribute("class","p-status")
    var downloadTime = getDownloadTime(obj)
    var node = document.createTextNode("已下载:" + (obj.data.endId - obj.data.startId)+ "/" + obj.data.sum + "  下载时间:" + downloadTime)
    pElement.appendChild(node)
    parentElement.appendChild(pElement)
}
function removePMenuName(checkbox) {
    var pMenuName = "#" + checkbox.getAttribute("iddata") + "-p"
    $("p").remove(pMenuName);
}
function getDownloadTime(obj) {
    var sumTime = obj.data.endTime - obj.data.startTime
    var hour = (sumTime / 3600000).toFixed(0)
    sumTime = sumTime % 3600000
    var min = (sumTime / 60000).toFixed(0)
    sumTime = sumTime % 60000
    var second = (sumTime / 1000).toFixed(0)
    return  hour + "小时" + min + "分钟" + second + "秒"
}