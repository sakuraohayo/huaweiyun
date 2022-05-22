$(function(){
    initial();
});

$(document).ready(function()
{

    $("span.ml-1").text($.cookie('username'));



    $("#btnSearch").click(function()
    {
        $("#resultNum").text("7");

    })



})



function initial() {
    var list=[];
    //重定向
    // $.ajaxSetup({
    //     complete:function (xhr,status) {
    //         xhr.getAllResponseHeaders();
    //         console.log(xhr);
    //         let REDIRECT=xhr.getResponseHeader("REDIRECT");
    //         if(REDIRECT==="redirect"){
    //             window.location.href="index_login.html";
    //         }
    //     }
    // });
    $.ajax({

        url:"http://remember.icube.fun:8080/findAllMemory",
        type:"POST",
        headers: {
            'token':$.cookie('token')
        },
        success: function(res){
            if(res.code==-2)
            {
                alert("请先登录");
                window.location.href="auth-login.html";
            }
            console.log(res);
            list=res.data.Memory;
            console.log(list)
            for(var i=0;i<list.length;i++)
            {

                $("#memory").append(" <div class='col-md-4 col-sm-4 mb' id='me"+list[i].id+"'><div class='content-panel pn' >"+
                    "<div id='memo-show' style='background:url(http://remember.icube.fun:8080/get-photo?photo_name="+list[i].images[0]+") no-repeat center top;'>                              <h3>"+list[i].title+"</h3>                    <h6>"+list[i].createTime+"</h6>                   </div>"+
                    " <div class='memo-show centered' onclick='detail(id)' id='"+list[i].id+"'>                            <p>查看记忆详情</p>                   </div>          <div class='centered'>"+
                    " <div class='centered'>"+
                    "</div>"+
                    "  </div>"+
                    "<!-- /content-panel -->"+
                    " </div>                    ");

            }

        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            alert("网络请求错误！");
            // alert("请求对象XMLHttpRequest: "+XMLHttpRequest);
            // alert("错误类型textStatus: "+textStatus);
            // alert("异常对象errorThrown: "+errorThrown);
        }
    });
}

function detail(e)
{
    console.log(e)
    window.location.href = "../page/memory_show.html?id="+e;
}