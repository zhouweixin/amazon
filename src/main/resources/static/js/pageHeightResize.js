window.addEventListener("resize", function (ev) {
    var body_height = document.body.offsetHeight
    if(body_height < 769){
        document.getElementById("left-sub-nav").style.marginTop = "50px"
        var left_button = document.getElementsByClassName("left-button")
        for(var i = 0; i < 4; i++){
            left_button[i].style.marginTop = "30px"
        }
    }
    else if(body_height >= 769){
        document.getElementById("left-sub-nav").style.marginTop = "100px"
        var left_button = document.getElementsByClassName("left-button")
        for(var i = 0; i < 4; i++){
            left_button[i].style.marginTop = "70px"
        }
    }
})
$(document).ready(function () {
    var body_height = document.body.offsetHeight
    if(body_height < 769){
        document.getElementById("left-sub-nav").style.marginTop = "50px"
        var left_button = document.getElementsByClassName("left-button")
        for(var i = 0; i < 4; i++){
            left_button[i].style.marginTop = "30px"
        }
    }
    else if(body_height >= 769){
        document.getElementById("left-sub-nav").style.marginTop = "100px"
        var left_button = document.getElementsByClassName("left-button")
        for(var i = 0; i < 4; i++){
            left_button[i].style.marginTop = "70px"
        }
    }
})