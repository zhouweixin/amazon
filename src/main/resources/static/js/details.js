$(document).ready(function () {
    var goodsId = getParm()
    $.get(ipPort + "/closing/getById?id=" + goodsId).done(function (obj) {
        var imgUrl = obj.data.mainImageUrl
        var goodsName = obj.data.name
        var goodsPrice = "$" + obj.data.priceLower + "-" + obj.data.priceHigher
        var goodsAsin = obj.data.asin
        var goodsBrand = obj.data.brand
        var goodsNowRanking = obj.data.rank
        var goodsStar = obj.data.star
        var goodsComments = obj.data.answerNum
        var goodsMaterial = obj.data.materialQuality
        var goodsColor =  obj.data.color
        var goodsPoints =  obj.data.bulletPointss
        var goodsModelNumber =  obj.data.itemModelNumber
        var goodsFirstDate = new Date(obj.data.firstAvailAmazonDate)
        goodsFirstDate = goodsFirstDate.toLocaleDateString()
        var goodsDownloadDate =  new Date(obj.data.downloadDate)
        goodsDownloadDate = goodsDownloadDate.toLocaleDateString()
        var goodsUrlA =  obj.data.url
        var goodsUrl =  obj.data.url
        var goodsDescription = obj.data.description
        document.getElementById("goods-name").innerHTML = goodsName
        document.getElementById("goods-img").setAttribute("src",imgUrl)
        document.getElementById("goods-price").innerHTML = goodsPrice
        document.getElementById("goods-asin").innerHTML = goodsAsin
        document.getElementById("goods-brand").innerHTML = goodsBrand
        document.getElementById("goods-nowRanking").innerHTML = goodsNowRanking
        document.getElementById("goods-star").innerHTML = goodsStar
        document.getElementById("goods-comments").innerHTML = goodsComments
        document.getElementById("goods-material").innerHTML = goodsMaterial
        document.getElementById("goods-color").innerHTML = goodsColor
        document.getElementById("goods-points").innerHTML = goodsPoints
        document.getElementById("goods-modelNumber").innerHTML = goodsModelNumber
        document.getElementById("goods-firstDate").innerHTML = goodsFirstDate
        document.getElementById("goods-downloadDate").innerHTML = goodsDownloadDate
        document.getElementById("goods-url-a").setAttribute("href",goodsUrlA)
        document.getElementById("goods-url").innerHTML = goodsUrl
        document.getElementById("goods-description").innerHTML = goodsDescription
    })

    /*设置默认echarts*/
    var endDate = new Date()
    endDate.setDate(endDate.getDate() - 7)
    document.getElementById("getStartDate").valueAsDate = endDate
    document.getElementById("getEndDate").valueAsDate = new Date()
    defaultSearching()
})
function getParm() {
    var url = location.href
    var tmp1 = url.split("?")[1]
    var tmp2 = tmp1.split("=")[1]
    return tmp2
}

function defaultSearching() {
    var goodsId = getParm()
    var startDate = getStartDate()
    var endDate = getEndDate()
    if(startDate == 0 || endDate == 0)return
    var url = ipPort + "/closing/getClosingsByIdAndStartDateToEndDate?id=" +  goodsId + "&startDate=" + startDate + "&endDate=" + endDate
    $.get(url).done(function (obj) {
        if(obj.data.length == 0){
            return
        }
        setGoodsEchartsRanking(obj)
        setGoodsEchartsStars(obj)
        setGoodsEchartsPrice(obj)
    })
}
function searching() {
    var goodsId = getParm()
    var startDate = getStartDate()
    var endDate = getEndDate()
    if(startDate == 0 || endDate == 0)return
    var url = ipPort + "/closing/getClosingsByIdAndStartDateToEndDate?id=" +  goodsId + "&startDate=" + startDate + "&endDate=" + endDate
    $.get(url).done(function (obj) {
        if(obj.data.length == 0){
            alert("没有查询到相关内容！")
            return
        }
        setGoodsEchartsRanking(obj)
        setGoodsEchartsStars(obj)
        setGoodsEchartsPrice(obj)
    })
}
function setGoodsEchartsRanking(obj) {
    var dom = document.getElementById("echartsRanking");
    var myChart = echarts.init(dom);
    myChart.setOption({

        tooltip: {
            trigger: 'none',
            axisPointer: {
                type: 'cross'
            }
        },
        legend: {
            data:['商品排名']
        },
        grid: {
            top: 70,
            bottom: 50
        },
        xAxis: [
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        //color: colors[0]
                    }
                },
                axisPointer: {
                    label: {
                        formatter: function (params) {
                            return '排名  ' + params.value
                                + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
                        }
                    }
                },
                data: []//排名时间
            },

        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [

            {
                name:'',
                type:'line',
                smooth: true,
                data: []//排名
            }
        ]
    })
    var x = new Array()
    for(var i in obj.data){
        x[i] = obj.data[i].downloadDate
    }
    var y = new Array()
    for(var i in obj.data){
        y[i] = obj.data[i].rank
    }
    myChart.setOption({
        xAxis: {
            data: x
        },
        series: [{
            // 根据名字对应到相应的系列
            name: '统计数量',
            data: y
        }]
    })
}
function setGoodsEchartsStars(obj) {
    var dom = document.getElementById("echartsStars");
    var myChart= echarts.init(dom);
    myChart.setOption({
        tooltip: {
            trigger: 'none',
            axisPointer: {
                type: 'cross'
            }
        },
        legend: {
            data:['星级']
        },
        grid: {
            top: 70,
            bottom: 50
        },
        xAxis: [
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        //color: colors[0]
                    }
                },
                axisPointer: {
                    label: {
                        formatter: function (params) {
                            return '星级  ' + params.value
                                + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
                        }
                    }
                },
                data: []//排名时间
            },

        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [

            {
                name:'',
                type:'line',
                smooth: true,
                data: []//排名
            }
        ]
    })
    var x = new Array()
    for(var i in obj.data){
        x[i] = obj.data[i].downloadDate
    }
    var y = new Array()
    for(var i in obj.data){
        y[i] = obj.data[i].star
    }
    myChart.setOption({
        xAxis: {
            data: x
        },
        series: [{
            // 根据名字对应到相应的系列
            name: '统计数量',
            data: y
        }]
    })
}
function setGoodsEchartsPrice(obj) {
    var dom = document.getElementById("echartsPrice");
    var myChart= echarts.init(dom);
    var colors = ['#5793f3', '#d14a61', '#675bba'];
    myChart.setOption({
        color: colors,

        tooltip: {
            trigger: 'none',
            axisPointer: {
                type: 'cross'
            }
        },
        legend: {
            data:['最高价格', '最低价格']
        },
        grid: {
            top: 70,
            bottom: 50
        },
        xAxis: [
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: colors[1]
                    }
                },
                axisPointer: {
                    label: {
                        formatter: function (params) {
                            return '最低价格  ' + params.value
                                + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
                        }
                    }
                },
                data: []
            },
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: colors[0]
                    }
                },
                axisPointer: {
                    label: {
                        formatter: function (params) {
                            return '最高价格  ' + params.value
                                + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
                        }
                    }
                },
                data: []
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name:'最高价格',
                type:'line',
                xAxisIndex: 1,
                smooth: true,
                data: []
            },
            {
                name:'最低价格',
                type:'line',
                smooth: true,
                data: []
            }
        ]
    })
    var x = new Array()
    for(var i in obj.data){
        x[i] = obj.data[i].downloadDate
    }
    var y = new Array()
    for(var i in obj.data){
        y[i] = obj.data[i].priceLower
    }
    var z = new Array()
    for(var i in obj.data){
        z[i] = obj.data[i].priceHigher
    }
    myChart.setOption({
        xAxis: [{
            data: x
        },
            {
            data: x
        }],
        series: [{
            // 根据名字对应到相应的系列
            name: '最低价格',
            data: y
        },
            {
            // 根据名字对应到相应的系列
            name: '最高价格',
            data: z
        }]
    })

}
function getStartDate() {
    var d = new Date(($('#getStartDate').val()))
    if(d == "Invalid Date"){
        alert("请输入开始日期！")
        return 0
    }
    var n = d.getTime()
    return n
}
function getEndDate() {
    var d = new Date(($('#getEndDate').val()))
    if(d == "Invalid Date"){
        alert("请输入截止日期！")
        return 0
    }
    var n = d.getTime()
    return n
}