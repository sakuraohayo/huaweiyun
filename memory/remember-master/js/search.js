



$(function(){

     initial();
  
});

$(document).ready(function()
{

    //填充导航栏用户名
    $("span.ml-1").text($.cookie('username'));

   
      $("#manager").click(function()
      {
        $("[name='checkbox']").css("display","inline")
        $("#delete").css("display","inline")
         $("#cancel").css("display","inline")
         $("#manager").css("display","none")
      })
       $("#delete").click(function()
      {
         var array = new Array(); //用于保存 选中的那一条数据的ID   
        var flag=false; //判断是否一个未选   
        console.log("hhh")
        $("[name='checkbox']").each(function() { //遍历所有的name为selectFlag的 checkbox  
                    if ($(this).prop("checked")) { //判断是否选中    
                        flag = true; //只要有一个被选择 设置为 true  
                        var a=parseInt($(this).val())
                        array.push(a); 
                        console.log(a)
                         $("div").remove("#me"+$(this).val());

                    }  
                })  
     
     console.log("hhh")
        if(flag==false)
        {
            alert("请至少选择一个记忆");  
        }else{
           console.log(array)
            $.ajax({                     
   　　　
          url:"http://remember.icube.fun:8080/deleteMemory",            //引号内为处理数据的代码地址，如果是php就是test.php
          type:"post",
           headers: {
              'token':$.cookie('token')
          },
          // contentType: "multipart/form-data",
          traditional:true,
          data:{
            "memoryID":array
          },
          success: function(res){
             if(res.code==-2)
            {
              alert("请先登录！")
              window.location.href = "../page/auth-login.html";
            }
            console.log(res)
            
            if(res.code==33)
            {
               alert("批量删除成功")
               initial();
            }
            else{
              alert("删除失败")
            }
          }
        })
           
        }
        

      })
        $("#cancel").click(function()
      {
        $("[name='checkbox']").css("display","none")
        $("#delete").css("display","none")
         $("#cancel").css("display","none")
         $("#manager").css("display","inline")
      })

})


function initial()
{


    var list=[]

console.log($.cookie('token'))
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
          console.log(res)
           console.log("hhh")
           list=res.data.memoryList
           console.log(list)
           if(list.length==0)
           {
           	 $("#memory").append("<div class='col-md-12 col-sm-12 mb'>暂无记忆哦！快去发表自己的记忆吧！</div>")
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
                               " <div class='memo-show centered' onclick='detail(id)' id='"+list[i].id+"'>                            <p>查看记忆详情</p>                   </div>          <div class='centered'>"+
                                  " <div class='memo-show1 centered' onclick='dele(id)' id='"+list[i].id+"'>                            <p>删除记忆</p>                   </div>          <div class='centered'>"+
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

           

    //载入我的记忆
   
 
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
function detail(e)
{
  console.log(e)
    window.location.href = "../page/memory_show.html?id="+e;
}

function dele(e)
{
    console.log(e)
    var array=[]
    array.push(parseInt(e))
         $.ajax({                     
   　　　
          url:"http://remember.icube.fun:8080/deleteMemory",            //引号内为处理数据的代码地址，如果是php就是test.php
          type:"post",
           headers: {
              'token':$.cookie('token')
          },
          // contentType: "multipart/form-data",
          traditional:true,
          data:{
            "memoryID":array
          },
          success: function(res){
             if(res.code==-2)
            {
              alert("请先登录！")
              window.location.href = "../page/auth-login.html";
            }
            console.log(res)

            if(res.code==33)
            {
                 $("div").remove("#me"+e);
               alert("删除成功")
               initial();
            }
            else{
              alert("删除失败")
            }
          }
        })
        //删除记忆接口
}
