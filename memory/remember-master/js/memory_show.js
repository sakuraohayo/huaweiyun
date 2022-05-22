$(document).ready(function () {


    //重定向
    $.ajaxSetup({
        complete:function(xhr,status){
            xhr.getAllResponseHeaders();
            console.log(xhr)
            let REDIRECT = xhr.getResponseHeader("REDIRECT");
            if(REDIRECT === "redirect"){
                window.location.href = "auth-login.html";
            }
        }
    })
    //填充导航栏用户名
    $("span.ml-1").text($.cookie('username'));

    //解析记忆ID
    function GetUrlPara(){
        var url = document.location.toString();//获取当前URL
        if (url.indexOf("?") != -1) {  //判断URL？后面不为空
        var arrUrl = url.split("?");//分割？
        var para = arrUrl[1];//获取参数部分
        if(para)//判断传入的参数部分不为空
        {
            var arr = para.split("=");//分割=
            var res = arr[1];//获取参数的值
            return res;
        }   
        }
        return null;
    }
    var memory_id=GetUrlPara();
    console.log(memory_id);

    var postdata = { "memoryID": memory_id};
    //var postdata = { "memoryID": "23" };
    $.ajax({
        data: postdata,
        beforeSend: function (request) {
            //将cookie中的token信息放于请求头中
            request.setRequestHeader("token", $.cookie('token'));
        },
        url: "http://remember.icube.fun:8080/memoryShow", // 
        type: "POST",
        asyn:false,
        dataType: "json",
        success: function (backdata) {
            //data是返回的hash,key之类的值，key是定义的文件名
            console.log(JSON.stringify(backdata));
            if (backdata.code == 12) {
                console.log(backdata.message);
                initialPage(backdata.data);
            }
            else if (backdata.code == -21) {
                console.log(backdata.message);//获取失败
            }else if(backdata.code==-2){
                alert(backdata.message);
                window.location.href="auth-login.html";
            }
            else {
                alert("获取记忆失败");
            }
        },
        error: function () {
            alert("展示失败");
        }
    });
    //initialPage(testData.Memory);

    // $('.image-show').slick({
    //     dots: true,
    //     infinite: true,
    //     speed: 500,
    //     cssEase: 'linear',
    //     arrorws: true,
    //     autoplay: true,
    //     autoplaySpeed: 5000,
    //     //centerMode: true,
    //     //slidesToShow: 2,
    // });
    
})

function initialPage(memory){
    $(".card-header").html("<h4>"+memory.name+"</h4>");
    var label="";
    for(var i=0;i<memory.labelList.length;i++)
    {
        label+="<span class='badge badge-primary'>"+memory.labelList[i]+"</span>"
    }
    $(".badgeList").html(label);
    //var replaceStr="";
    //console.log(memory.content);
    var result = memory.content.split('\n').join('<br/>');
    //replaceStr=memory.content.replace('/↵/g',"<br/>");
    // memory.content.replace("\\n","<br/>");
    // memory.content.replace("\\r\\n","<br/>");
    // memory.content.replace("\\\n","<br/>");
    // memory.content.replace("\\\r\\\n","<br/>");
    console.log(result);
    $(".memory-text").html("<p class='card-text'>"+result+"</p>");
    $(".card-footer").html(memory.time);
    
    var str="";
    for(var i=0;i<memory.imgUrls.length;i++)
    {
        // $.ajax({
        //     type: "GET",
        //     url: "http://憨憨.club:8080/get-photo?photo_name="+memory.imgUrls[i], 
        //     //dataType:'blob',
        //     asyn:false,
        //     beforeSend: function(request) {
        //         //将cookie中的token信息放于请求头中
        //         request.setRequestHeader("token", $.cookie('token'));
        //     },
        //     success: function (backdata) {
        //         console.log(backdata);
        //         // var img=document.createElement("img");
        //         // img.onload = function(e) {
		// 	    //     window.URL.revokeObjectURL(img.src);
        //         // };
        //         // img.src = window.URL.createObjectURL(new Blob([backdata], {type: "image/jpeg"}));
        //         // img.setAttribute("class","myimage")
        //         // //document.body.appendChild(img);
        //         // var div=document.createElement("div");
        //         // div.setAttribute("class","one-image");
        //         // div.appendChild(img);
        //         // $(".image-show").append(div);
        //         var src=window.URL.createObjectURL(new Blob([backdata], {type: "image/jpeg"}));
        //         str+="<div class=\"one-image\">"+
        //         "<img class=\"myimage\" src=\""+"http://憨憨.club:8080/get-photo?photo_name="+memory.imgUrls[i]+"\">"+
        //         "</div>";
        //     },
        //     error: function (XMLHttpRequest, textStatus, errorThrown) {
        //         console.log(textStatus);
        //         console.log(errorThrown.toString());
        //         alert("获取图片失败");
        //     }
        // });
        // $(".image-show").slick("slickAdd","<div class=\"one-image\">"+
        // "<img class=\"myimage\" src=\""+memory.imgUrls[i]+"\">"+
        // "</div>");
        // $('.image-show').append("<div class=\"one-image\">"+
        // "<img class=\"myimage\" src=\""+testData.Memory.imgUrls[i]+"\">"+
        // "</div>");
        // str+="<div class=\"one-image\">"+
        // "<img class=\"myimage\" src=\"http://2547m30z96.wicp.vip/get-photo?photo_name="+memory.imgUrls[i]+"\">"+
        // "</div>";
        str+="<div class=\"one-image\">"+
        "<img class=\"myimage\" src=\"http://remember.icube.fun:8080/get-photo?photo_name="+memory.imgUrls[i]+"\">"+
        "</div>";
    }
    $(".image-show").html(str);
    $('.image-show').slick({
        dots: true,
        infinite: true,
        speed: 500,
        cssEase: 'linear',
        arrorws: true,
        autoplay: true,
        autoplaySpeed: 5000,
        //centerMode: true,
        //slidesToShow: 2,
    });
}

var testData={
    "Memory": {
                "name": "偶遇",
                "labelList": ["快乐","难忘","日常","幸福"],
                "content":"我是天空里的一片云，\r\n偶然投映在你的波心--\n你不必讶异，\n更无需欢喜--\n转瞬间消灭了踪影。\n你我相逢在黑夜的海上，\n你有你的，\n我有我的方向，\n你记得也好，\n最好你忘掉，\n在这交汇时互放的光亮。",
                "imgUrls":["../images/beijing2.jpg","../images/beijing3.png","../images/night.jpg"],
                "time":"2020-5-13 13:00"
            }
}