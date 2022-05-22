//Dropzone.autoDiscover = false;
var imgFile = []; //文件流
var imgSrc = []; //图片路径
var imgName = []; //图片名字
var imgNum=0;

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

    //填充导航栏信息
    $("span.ml-1").text($.cookie('username'));

    //console.log($.cookie('token'));
    //此部分交互时取消注释
    //获取标签列表
    $.ajax({
        type: "POST",
        beforeSend: function (request) {
           //将cookie中的token信息放于请求头中
            request.setRequestHeader("token", $.cookie('token'));
        },
        url: "http://remember.icube.fun:8080/getTags", // 
        dataType: "json",
        success: function (rtmsg) {
            //data是返回的hash,key之类的值，key是定义的文件名
            console.log(JSON.stringify(rtmsg));
            if (rtmsg.code === -2) {
                alert(rtmsg.message);
            } else if (rtmsg.code == 15) {
                console.log(rtmsg.message);
                //填充可选记忆标签表
                initLabel(rtmsg.data.tabs);
            } else if(rtmsg.code==-2){
                alert(rtmsg.message);
                window.location.href="auth-login.html";
            }
            else {
                alert("获取标签集合失败");
            }
        },
        error: function () {
            alert("记忆标签获取失败");
        }
    });
    // var testlabel = ["快乐", "忧郁", "悲伤", "痛苦", "明朗", "平淡", "幸福", "梦想"];
    // initLabel(testlabel);

    //这里处理文件部分
    // 鼠标经过显示删除按钮
    $('.content-img-list').on('mouseover', '.content-img-list-item', function () {
        $(this).children('a').removeClass('hide');
    });
    // 鼠标离开隐藏删除按钮
    $('.content-img-list').on('mouseleave', '.content-img-list-item', function () {
        $(this).children('a').addClass('hide');
    });
    // 单个图片删除
    $(".content-img-list").on("click", '.content-img-list-item a', function () {
        var index = $(this).attr("index");
        imgSrc.splice(index, 1);
        imgFile.splice(index, 1);
        imgName.splice(index, 1);
        var boxId = ".content-img-list";
        addNewContent(boxId);
        if (imgSrc.length < 20) {//显示上传按钮
            $('.content-img .file').show();
        }
    });
    //图片上传
    $('#upload').on('change', function () {

        var initNum=imgNum;
        console.log("历史添加图片个数"+initNum);

        if(imgSrc.length>=20){
        	return alert("最多只能上传20张图片");
        }
        
        var imgSize = this.files[0].size;  //b
        if (imgSize > 1024 * 1024 * 5) {//5M
            return alert("上传图片不能超过5M");
        }
        console.log(this.files[0].type)
        if (this.files[0].type != 'image/png' && this.files[0].type != 'image/jpeg' && this.files[0].type != 'image/gif') {
            return alert("图片上传格式不正确");
        }
        var imgBox = '.content-img-list';
        var fileList = this.files;
        console.log(imgSrc.length);
        console.log(fileList.length);
        if(imgSrc.length+fileList.length>20){
            return alert("最多只能上传20张图片");
        }
        for (var i = 0; i < fileList.length; i++) {
            var imgSrcI = getObjectURL(fileList[i]);
            imgName.push(fileList[i].name);
            imgSrc.push(imgSrcI);
            imgFile.push(fileList[i]);
            imgNum++;
        } 
        if(imgSrc.length>=20){//隐藏上传按钮
        	$('.content-img .file').hide();
        }
        addNewContent(imgBox);
        this.value = null;//解决无法上传相同图片的问题
    })


    //提交表单
    $("#btnSubmit").click(function () {
        /*检查表单内容*/
        // var data = $("#inputMem").serialize();
        // var decodeData = decodeURIComponent(data, true);
        // //将FormData转成JSON
        // var postdata = {};
        // for (var attr in decodeData) {
        //     var str = decodeData[attr];
        //     postdata[attr.toString()] = str;
        // }
        // console.log("postdata是" + postdata);
        // alert("postdata是" + JSON.stringify(postdata));
        //获取文本框的值 标题和内容
        var memoryName = $("#memoryName").val();
        if(memoryName==""){
            alert("记忆标题不能为空");
            return;
        }
        
        //获取标签列表
        var jsonArrayStr = '[]';
        var jsonArray = eval('(' + jsonArrayStr + ')');
        var obj = document.getElementById('chooseLabel');
        //获取所有的option
        var options = obj.options;
        for (var i = 0; i < options.length; i++) {
            //判断optin是否被选中了
            var ss = options[i].selected;
            if (options[i].selected) {
                var jsonstr = '{"value":"' + options[i].value + '","text":"' + options[i].text + '"}';
                jsonArray.push(jsonstr);
            }
        }
        console.log(jsonArray);//["{"value":"","text":""}",{},{}]
        //转换格式
        var chooseLabel = [];
        for (var i = 0; i < jsonArray.length; i++) {
            var o = JSON.parse(jsonArray[i]);
            chooseLabel[i] = o.value;
        }
        if(chooseLabel.length==0){
            alert("记忆标签不能为空");
            return;
        }
        console.log(JSON.stringify(chooseLabel));
        var content = $("#content").val();
        if(content==""){
            alert("记忆内容不能为空");
            return;
        }
        content.replace("\n", "<br\>");
        console.log(content);
        //alert(JSON.stringify(chooseLabel));
        //构成初始data
        var data = {
            "memoryName": memoryName,
            "labelList": chooseLabel,
            "content": content
        }
        console.log("提交的信息：" + JSON.stringify(data));
        /*添加imageUrls*/
        // FormData上传图片
        var formFile = new FormData();
        formFile.append("memoryName", memoryName);
        formFile.append("labelList", chooseLabel);
        formFile.append("content", content);

        // 遍历图片imgFile添加到formFile里面
        $.each(imgFile, function (i, file) {
            formFile.append('images', file);
        });
        if(imgFile.length==0){
            alert("记忆影集不能为空");
            return;
        }
        console.log(imgFile);
        // for (var [a, b] of formFile.entries()) {
        //     console.log(a, b);
        // }
        console.log("hello");
        /*提交*/
        $.ajax({
            type: "POST",
            url: "http://remember.icube.fun:8080/saveMemory", //  
            beforeSend: function (request) {
                //将cookie中的token信息放于请求头中
                 request.setRequestHeader("token", $.cookie('token'));
             }, 
            //ContentType: "multipart/form-data",
            processData: false,
            contentType: false,
            cache: false,  
            data: formFile,
            dataType:'json',
            success: function (rtmsg) {
                console.log(JSON.stringify(rtmsg));
                if (rtmsg.code === 11) {
                    alert(rtmsg.message);
                    //跳转页面处理
                    window.location.href="index_login.html";
                } else if(rtmsg.code==-20){
                    alert(rtmsg.message);
                } else if(rtmsg.code==-2){
                    alert(rtmsg.message);
                    window.location.href="auth-login.html";
                }else{
                    alert("发布失败");
                }
            },
            error: function (request,textStatus,errorThrown) {
                alert("记忆提交错误");
                // console.log(textStatus);
                // console.log(errorThrown);
            }
        });
        console.log("end hand-in");
    });

})

function initLabel(labels) {
    var num = labels.length;
    var str = "";
    for (var i = 0; i < num; i++) {
        str += "<option value='" + labels[i] + "'>" + labels[i] + "</option>";
    }
    $("#chooseLabel").html(str);
}

//删除
function removeImg(obj, index) {
    imgSrc.splice(index, 1);
    imgFile.splice(index, 1);
    imgName.splice(index, 1);
    var boxId = ".content-img-list";
    addNewContent(boxId);
    imgNum--;
}

//图片展示
function addNewContent(obj) {
    // console.log(imgSrc)
    $(obj).html("");
    for (var a = 0; a < imgSrc.length; a++) {
        var oldBox = $(obj).html();
        $(obj).html(oldBox + '<li class="content-img-list-item"><img src="' + imgSrc[a] + '" alt=""><a index="' + a + '" class="hide delete-btn"><i class="mdi mdi-delete"></i>取消上传</a></li>');
    }
}

//建立一個可存取到該file的url
function getObjectURL(file) {
    var url = null;
    if (window.createObjectURL != undefined) { // basic
        url = window.createObjectURL(file);
    } else if (window.URL != undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file);
    } else if (window.webkitURL != undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file);
    }
    return url;
}

// $("#myDropzone").dropzone({
//     url: "uploadImages",
//     method: "post",
//     maxFilesize: "5",
//     autoDiscover: false,
//     autoProcessQueue: true,
//     addRemoveLinks: true,
//     addViewLinks: true,
//     acceptedFiles: ".jpg,.png",
//     dictRemoveFile: '点击移除',
//     init: function () {
//         this.on("removedfile", function (file) {
//             //通过file找到图片url
//             var url = findFile(file);
//             // 手动在服务器上删除图片
//             $.ajax({
//                 type: "POST",
//                 url: "/xx/deleteImage", //  
//                 data: { "imageUrl": url },
//                 dataType: "json",
//                 success: function (data) {
//                     console.log(JSON.stringify(data));
//                     if (data.status === 1) {
//                         alert("移除成功");
//                         //跳转页面处理

//                     } else {
//                         alert("移除失败");
//                     }
//                 },
//                 error: function () {
//                     alert("移除失败了");
//                 }
//             });
//             //在图片列表中删除
//             removeFile(file);
//         });
//     },
//     success: function (file, response) {
//         //alert(response);
//         console.log(response);
//         if (response.status === 1) {
//             console.log("上传图片成功");
//             addFile(file, response.url);
//         }
//     },
//     error: function () {
//        // alert("上传图片失败");
//        //仅做展示用
//         alert("图片上传成功");
//     }
// });



// //imageUrls和fileList对应
// function addFile(file, url) {
//     var i;
//     //在imageUrl找到一处空闲
//     for (i = 0; i < fileList.length; i++) {
//         if (fileList[i] == null)//找到一处为空
//         {
//             fileList[i] = file;
//             //将url添加到该处
//             imageUrls[i] = url;
//             break;
//         }
//     }
//     return i;
// }

// function removeFile(file) {
//     //在fileList中找到匹配的文件
//     var index = findFile(file);
//     //在fileList中删除
//     fileList[i] = null;
//     //在imageUrls对应处删除
//     imageUrls[i] = null;
// }

// function findFile(file) {
//     var index = 0;
//     //根据file匹配fileList中的文件
//     for (index = 0; index < fileList.length; index++) {
//         if (fileList[index] == file) {
//             return index;
//             break;
//         }
//     }
//     //返回i值
//     return -1;
// }

