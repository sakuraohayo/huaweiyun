package javaweb.remember.serviceImpl;

import javaweb.remember.service.PhotoService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Remarks  : 封装图片上传和保存功能
 * File     : PhotoServiceImpl.java
 * Project  : I-Remember
 * Software : IntelliJ IDEA
 */
@Service
public class PhotoServiceImpl implements PhotoService {

    /**
     * 上传图片
     *
     * @param photo_path 图片保存绝对路径，从根路径到图片名
     * @param photo_file 图片文件
     * @return int。0：成功保存；1：保存失败，图片类型不符合，要求png或jpg或jpeg或bmp或gif图片；2：图片待保存文件夹不存在创建出错；3：文件保存出错
     */
    @Override
    public int uploadPhoto(String photo_path, MultipartFile photo_file) {
        // -------------------文件类型判断-------------------
        //获取照片文件名
        String photo_file_name = photo_file.getOriginalFilename();
        // 判断文件不是空
        assert photo_file_name != null;
        //获取文件类型，即后缀名
        String photo_type = photo_file_name.substring(photo_file_name.lastIndexOf(".")).toLowerCase();
        // 判断文件是不是图片
        if (!(
                photo_type.equals(".png")
                || photo_type.equals(".jpg")
                || photo_type.equals(".jpeg")
                || photo_type.equals(".bmp")
                || photo_type.equals(".gif")
        )){
            return 1;
        }
        // -------------------文件保存位置判断-------------------
        File photo_to_save = new File(photo_path);
        if (!photo_to_save.getParentFile().exists()){
            if (!photo_to_save.getParentFile().mkdir()){
                return 2;
            }
        }
        // -------------------保存文件-------------------
        try {
            photo_file.transferTo(photo_to_save);
        }
        catch (IOException e){
            e.printStackTrace();
            return 3;
        }
        return 0;
    }

    /**
     * 删除图片，删除图片前要检测权限，检测这个图片是不是当前用户的
     *
     * @param photo_path 图片绝对路径
     * @return int。0：成功删除；1：删除失败
     */
    @Override
    public int deletePhoto(String photo_path) {
        // -------------------文件是否存在判断-------------------
        File photo_to_delete = new File(photo_path);
        if (photo_to_delete.exists() && photo_to_delete.delete()){
            return 0;
        }
        else {
            return 1;
        }
    }

    /**
     * 获取图片
     *
     * @param photo_path 图片路径
     * @return 图片异常读取时返回null，图片正常读取时返回图片的byte数组，配合HTTP header中设置produces = MediaType.IMAGE_JPEG_VALUE，可以直接在网页上显示图片
     */
    @Override
    public byte[] getPhoto(String photo_path) {
        // -------------拼接用户头像保存位置并核对图片存在-------------
        // 注意路径拼接的时候使用'/'，不然会导致Linux系统下出问题
        File file = new File(photo_path);
        if (!file.exists()) {
            return null;
        }
        // -------------读取头像并返回-------------
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            int readResult = inputStream.read(bytes, 0, inputStream.available());
            if (readResult >= 0){
                return bytes;
            }
            else {
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
