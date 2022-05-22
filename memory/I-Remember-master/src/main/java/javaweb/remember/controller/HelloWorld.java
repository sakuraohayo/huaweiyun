package javaweb.remember.controller;
import javaweb.remember.service.PhotoService;
import javaweb.remember.utils.GetIpAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Remarks  : get和post测试
 * File     : HelloWorld.java
 * Project  : I-Remember
 * Software : IntelliJ IDEA
 */


@RestController
public class HelloWorld {
    @Autowired
    PhotoService photoService;

    //@GetMapping(value = "/get-photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getPhotoTest(@RequestParam("photo_name") String photo_name) throws Exception {
        String photo_path = "K:\\Java Projects\\I-Remember\\image\\" + photo_name;
        // 访问http://localhost:8080/get-photo?photo_name=晚安.jpg可以访问图片
        // 访问http://localhost:8080/get-photo?photo_name=随便名字，只要不存在会抛出异常，http状态码是400
        byte[] photo = photoService.getPhoto(photo_path);
        if (null == photo){
            throw new Exception("文件读取出错");
        }
        else {
            return photo;
        }
    }

    //@GetMapping(value = "/delete-photo")
    public String deletePhotoTest(@RequestParam("photo_name") String photo_name) throws Exception {
        String photo_path = "K:\\Java Projects\\I-Remember\\image\\" + photo_name;
        // 访问http://localhost:8080/delete-photo?photo_name=测试.jpg可以删除图片
        // 访问http://localhost:8080/delete-photo?photo_name=随便名字，只要不存在会抛出异常，http状态码是400
        int delete_result = photoService.deletePhoto(photo_path);
        if (delete_result != 0){
            throw new Exception("文件读取出错");
        }
        else {
            return "成功删除";
        }
    }

    //@PostMapping("/upload-photo")
    public String uploadPhotoTest(@RequestParam(value = "photo") MultipartFile photoFile) {
        String photo_path = "K:\\Java Projects\\I-Remember\\image\\" + photoFile.getOriginalFilename();
        int i = photoService.uploadPhoto(photo_path, photoFile);
        if ( i != 0){
            return "上传失败";
        }
        return "上传成功";
    }

    @GetMapping("/")
    public String hello(
            HttpServletResponse httpServletResponse,
            HttpServletRequest request,
            @RequestParam(value = "name", defaultValue = "World") String name
    ) {
        String  browserDetails  =   request.getHeader("User-Agent");
        System.out.println(browserDetails);
        String ip_info = GetIpAddress.getIp(request);
        System.out.println(ip_info);
        HttpSession session = request.getSession();
          System.out.println(session);
        Cookie[] cookies = request.getCookies();

        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    System.out.println("准备进数据库");
//                    User user = userMapper.findByToken(token); //去数据库寻找该token值的用户信息
//                    System.out.println(user.toString());
//                    if(user != null){ //若找到了这个用户信息
//                        //写进session，让页面去展示
//                        request.getSession().setAttribute("user",user);
//                    }
                    break;
                }
            }
        }

//        return "index";
        Cookie cookie = new Cookie("token", "token");
        httpServletResponse.addCookie(cookie);
        return String.format("Hello %s!", name);
    }

    @PostMapping("/")
    public String hello2(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }


}

