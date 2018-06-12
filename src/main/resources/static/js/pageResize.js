window.addEventListener("resize", function (ev) {
    var height = document.getElementById("main-nav").offsetHeight
    document.getElementById("nav-hidden").style.height = height + "px"
    var body_height = document.body.offsetHeight
    document.getElementById("content-panel").style.height = (body_height - height) + "px"
})
$(document).ready(function () {
    var height = document.getElementById("main-nav").offsetHeight
    document.getElementById("nav-hidden").style.height = height + "px"
    var body_height = document.body.offsetHeight
    document.getElementById("content-panel").style.height = (body_height - height) + "px"
})