$(function(){
    initial();
    
});

$(document).ready(function()
{

//填充导航栏用户名
    $("span.ml-1").text($.cookie('username'));
    
 


    $("form").submit(function()
    {
        console.log( $("#searchContent").val())
         $("#memory").empty()
        //$("#resultNum").text("10");





        $.ajax({                     
   　　　
          url:"http://remember.icube.fun:8080/searchMemory",            //引号内为处理数据的代码地址，如果是php就是test.php
          type:"post",
           headers: {
              'token':$.cookie('token')
          },
          data:{
            searchStr:$("#searchContent").val()
          },
          success: function(res){
             if(res.code==-2)
            {
              alert("请先登录！")
              window.location.href = "../page/auth-login.html";
            }
           console.log(res)
           console.log("hhh")
           var list=res.data.memoryList
           console.log(list)
           if(res.code==40){
           	  if(list.length==0)
           {
           	 $("#memory").append("<div class='col-md-12 col-sm-12 mb'>暂无记忆哦！换个关键词搜索吧！</div>")
           }

            for(var i=0;i<list.length;i++)
    {
        console.log("hhh")
        // if(i%3==0)
        // {
        //      $("#memory").append("<div class='row'>")
        // }

        $("#memory").append(" <div class='col-md-4 col-sm-4 mb' id='me"+list[i].id+"'><input type='checkbox'  name='checkbox' value='"+list[i].id+"' style='transform: scale(1.2,1.2);display:none'><div class='content-panel pn' >"+
                                "<div id='memo-show' style='background:url(http://remember.icube.fun:8080/get-photo?photo_name="+list[i].images[0]+") no-repeat center top;'>                              <h3>"+list[i].title+"</h3>                    <h6>"+list[i].createTime+"</h6>                   </div>"+
                               " <div class='memo-show centered' onclick='on(id)' id='"+list[i].id+"'>                            <p>查看记忆详情</p>                   </div>          <div class='centered'>"+
                                  "          <div class='centered'>"+
                                "</div>"+
                          "  </div>"+
                            "<!-- /content-panel -->"+
                       " </div>                    ");
          }
        }
        },
          error:function(XMLHttpRequest, textStatus, errorThrown){
              alert("网络请求错误！");
            // alert("请求对象XMLHttpRequest: "+XMLHttpRequest);
            // alert("错误类型textStatus: "+textStatus);
            // alert("异常对象errorThrown: "+errorThrown);
          }
        });
    
     return false;
        
    })

})

function initial()
{
  var username=$.cookie('username')

    $("#username").text(username);
     
           	 $("#memory").append("<div class='col-md-12 col-sm-12 mb'>暂无记忆哦！输入关键词搜索吧！</div>")
           

    $.ajax({                     
   　　　
          url:"http://remember.icube.fun:8080/getCreatorAllMemory",            //引号内为处理数据的代码地址，如果是php就是test.php
          type:"post",
           headers: {
              'token':$.cookie('token')
          },
       
          success: function(res){
            if(res.code==-2)
            {
              alert("请先登录！")
              window.location.href = "../page/auth-login.html";
            }
           

        },
         
          error:function(XMLHttpRequest, textStatus, errorThrown){
              alert("网络请求错误！");
            // alert("请求对象XMLHttpRequest: "+XMLHttpRequest);
            // alert("错误类型textStatus: "+textStatus);
            // alert("异常对象errorThrown: "+errorThrown);
          }
        });
    /*
    $("#resultNum").text("0");
    $.ajax({
        type:"GET",
        url:"resources\test.json",
        dataType:"json",
        success:function(data){
            var listdata=data.list;
            if(listdata.length>0){
                var listdata="";
                $.each(listdata, function () { 
                     listInfo+="<div class='options-frame'>"+
                     "<h3>"+this.theme+"</h3>"+
                     "<p>"+this.content+"</p>"+
                     "<h6>"+this.date+"</h6>";
                });
                $("#resultMemories")[0].innerHTML=listInfo;
            }
        },
        error:function(){
            alert("请求数据失败");
        }
        
    })
    */
}
function on(e)
{
    console.log(e)
       window.location.href = "../page/memory_show.html?id="+e;
}
