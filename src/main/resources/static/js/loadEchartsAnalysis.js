$(document).ready(function () {
    document.getElementById("getDataDate").valueAsDate = new Date()
    defaultSetEchartsBrand()
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
function getEchartsBrands() {
    var obj = document.getElementById("echartsTitle-strong")
    obj.innerText = "品牌统计图"
    document.getElementById("page-1").setAttribute("class", "active")
    document.getElementById("page-2").setAttribute("class", "")
    document.getElementById("page-3").setAttribute("class", "")
    document.getElementById("echartsTitle").setAttribute("value", "品牌统计图")
    //图1
    var downloadDate = getDate()
    if(downloadDate == 0)return
    var menuId = document.getElementById("echartsTitle").getAttribute("dataId")
    menuId = ipPort + "/closing/getBrandNumByMenuIdAndDownloadDate?menuId=" + menuId + "&downloadDate=" + downloadDate;
    setEchartsBrand(menuId)
}
function getEchartsStars() {
    var obj = document.getElementById("echartsTitle-strong")
    obj.innerText = "商品星级统计图"
    document.getElementById("page-1").setAttribute("class", "")
    document.getElementById("page-2").setAttribute("class", "active")
    document.getElementById("page-3").setAttribute("class", "")
    document.getElementById("echartsTitle").setAttribute("value", "商品星级统计图")
    //图1
    var downloadDate = getDate()
    if(downloadDate == 0)return
    var menuId = document.getElementById("echartsTitle").getAttribute("dataId")
    menuId = ipPort + "/closing/getStarNumByMenuIdAndDownloadDate?menuId=" + menuId + "&downloadDate=" + downloadDate;
    setEchartsStars(menuId)
}

function getEchartsPrice() {
    var obj = document.getElementById("echartsTitle-strong")
    obj.innerText = "价格区间统计图"
    document.getElementById("page-1").setAttribute("class", "")
    document.getElementById("page-2").setAttribute("class", "")
    document.getElementById("page-3").setAttribute("class", "active")
    document.getElementById("echartsTitle").setAttribute("value", "价格区间统计图")
    //图2
    var downloadDate = getDate()
    if(downloadDate == 0)return
    var menuId = document.getElementById("echartsTitle").getAttribute("dataId")
    menuId = ipPort + "/closing/getPriceNumByMenuIdAndDownloadDate?menuId=" + menuId + "&downloadDate=" + downloadDate;
    setEchartsPrice(menuId)
}

function changeEcharts() {
    var downloadDate = getDate()
    if(downloadDate == 0)return
    var menuId = document.getElementById("echartsTitle").getAttribute("dataId")
    //图1
    var tableName = document.getElementById("echartsTitle").getAttribute("value")
    if(tableName == "品牌统计图"){
        menuId = ipPort + "/closing/getBrandNumByMenuIdAndDownloadDate?menuId=" + menuId + "&downloadDate=" + downloadDate;
        setEchartsBrand(menuId)
    }else if(tableName == "商品星级统计图"){
        menuId = ipPort + "/closing/getStarNumByMenuIdAndDownloadDate?menuId=" + menuId + "&downloadDate=" + downloadDate;
        setEchartsStars(menuId)
    }else if(tableName == "价格区间统计图"){
        menuId = ipPort + "/closing/getPriceNumByMenuIdAndDownloadDate?menuId=" + menuId + "&downloadDate=" + downloadDate;
        setEchartsPrice(menuId)
    }
}
function defaultSetEchartsBrand(){
    var downloadDate = getDate()
    if(downloadDate == 0)return
    var menuId = document.getElementById("echartsTitle").getAttribute("dataId")
    menuId = ipPort + "/closing/getBrandNumByMenuIdAndDownloadDate?menuId=" + menuId + "&downloadDate=" + downloadDate;
    $.get(menuId).done(function (data_obj) {
        if(data_obj.data.length == 0){
            return
        }
        document.getElementById('echartsTable').removeAttribute("_echarts_instance_")
        var myChartBrand = echarts.init(document.getElementById('echartsTable'));
        // 显示标题，图例和空的坐标轴
        myChartBrand.setOption({
            title: {
                text: ''
            },
            tooltip: {},
            legend: {
                data:[]
            },
            xAxis: {
                name : '商标',
                data: []
            },
            yAxis: {
                name : '数量(个)'
            },
            series: [{
                name: '统计数量',
                type: 'bar',
                data: []
            }]
        });
        var x = new Array()
        for(var i in data_obj.data){
            x[i] = data_obj.data[i].brand
        }
        var y = new Array()
        for(var i in data_obj.data){
            y[i] = data_obj.data[i].num
        }
        myChartBrand.setOption({
            xAxis: {
                data: x
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '统计数量',
                data: y
            }]
        })
    })
}
function setEchartsStars(menuId) {
    $.get(menuId).done(function (data_obj) {
        if(data_obj.data.length == 0){
            alert("没有查询到相关信息！")
            return
        }
        document.getElementById('echartsTable').removeAttribute("_echarts_instance_")
        var myChartStars = echarts.init(document.getElementById('echartsTable'));
        // 显示标题，图例和空的坐标轴
        myChartStars.setOption({
            title: {
                text: ''
            },
            tooltip: {},
            legend: {
                data:[]
            },
            xAxis: {
                name : '星级',
                data: []
            },
            yAxis: {
                name : '数量(个)'
            },
            series: [{
                name: '统计数量',
                type: 'bar',
                data: []
            }]
        });
        var x = new Array()
        for(var i in data_obj.data){
            x[i] = data_obj.data[i].star
        }

        var y = new Array()
        for(var i in data_obj.data){
            y[i] = data_obj.data[i].num
        }

        myChartStars.setOption({
            xAxis: {
                data: x
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '统计数量',
                data: y
            }]
        })
    })
}
function setEchartsBrand(menuId) {
    $.get(menuId).done(function (data_obj) {
        if(data_obj.data.length == 0){
            alert("没有查询到相关信息！")
            return
        }
        document.getElementById('echartsTable').removeAttribute("_echarts_instance_")
        var myChartBrand = echarts.init(document.getElementById('echartsTable'));
        // 显示标题，图例和空的坐标轴
        myChartBrand.setOption({
            title: {
                text: ''
            },
            tooltip: {},
            legend: {
                data:[]
            },
            xAxis: {
                name: '商标',
                data: []
            },
            yAxis: {
                name: '数量(个)'
            },
            series: [{
                name: '统计数量',
                type: 'bar',
                data: []
            }]
        });
        var x = new Array()
        for(var i in data_obj.data){
            x[i] = data_obj.data[i].brand
        }
        var y = new Array()
        for(var i in data_obj.data){
            y[i] = data_obj.data[i].num
        }
        myChartBrand.setOption({
            xAxis: {
                data: x
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '统计数量',
                data: y
            }]
        })
    })
}
function setEchartsPrice(menuId) {
    $.get(menuId).done(function (data_obj) {
        if(data_obj.data.length == 0){
            alert("没有查询到相关信息！")
            return
        }
        document.getElementById('echartsTable').removeAttribute("_echarts_instance_")
        var myChartPrice = echarts.init(document.getElementById('echartsTable'));
        // 显示标题，图例和空的坐标轴
        myChartPrice.setOption({
            title : {
                text: '',
                subtext: ''
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['最低价格的数量','最高价格的数量']
            },
            calculable : true,
            xAxis : [
                {
                    name : '价格区间',
                    type : 'category',
                    data : []
                }
            ],
            yAxis : [
                {
                    name: '数量(个)',
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'最低价格的数量',
                    type:'bar',
                    data:[],
                },
                {
                    name:'最高价格的数量',
                    type:'bar',
                    data:[],
                }
            ]
        });
        var x = new Array()
        for(var i in data_obj.data){
            x[i] = data_obj.data[i].interval
        }
        var y = new Array()
        for(var i in data_obj.data){
            y[i] = data_obj.data[i].lowerNum
        }
        var z = new Array()
        for(var i in data_obj.data){
            z[i] = data_obj.data[i].higherNum
        }
        myChartPrice.setOption({
            xAxis: {
                data: x
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '最低价格的数量',
                data: y
                },
                {
                    name:'最高价格的数量',
                    data:z
                }
            ]
        })
    })
}
function selectMenu(menuId) {
    document.getElementById("echartsTitle").setAttribute("dataId",menuId)
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