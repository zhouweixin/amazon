function openSpiderPage() {
    var domain = $("#domain-input").val()
    var startUrl = $("#startUrl-input").val()
    var retryTimes = $("#retryTimes-input").val()
    var cycleRetryTimes = $("#cycleRetryTimes-input").val()
    var retrySleepTime = $("#retrySleepTime-input").val()
    var threadNum = $("#threadNum-input").val()
    var sleepTime = $("#sleepTime-input").val()
    var timeOut = $("#timeOut-input").val()
    var chromeDriverPath = $("#chromeDriverPath-input").val()
    var proxyPort = $("#proxyPort-input").val()
    var proxyIp = $("#proxyIp-input").val()

    var url = "spider.html?domain=" + domain +  "&retryTimes=" + retryTimes + "&cycleRetryTimes=" + cycleRetryTimes
        + "&retrySleepTime=" + retrySleepTime + "&threadNum=" + threadNum + "&sleepTime=" + sleepTime + "&timeOut=" + timeOut
        + "&chromeDriverPath=" + chromeDriverPath
    window.location.href = url

}