package javaweb.remember.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Remarks  : 封装图片上传和保存功能接口类
 * File     : PhotoService.java
 * Project  : I-Remember
 * Software : IntelliJ IDEA
 */
public interface PhotoService {
    /**
     * 上传图片
     * @param photo_path 图片保存路径
     * @param photo_file 图片文件
     * @return int。0：成功保存；1：保存失败
     */
    public int uploadPhoto(String photo_path, MultipartFile photo_file);

    /**
     * 删除图片，删除图片前要检测权限，检测这个图片是不是当前用户的
     * @param photo_path 图片路径
     * @return int。0：成功删除；1：删除失败
     */
    public int deletePhoto(String photo_path);

    /**
     * 获取图片
     * @param photo_path 图片路径
     * @return 返回图片的byte数组，配合HTTP header中设置produces = MediaType.IMAGE_JPEG_VALUE，可以直接在网页上显示图片
     */
    public byte[] getPhoto(String photo_path);
}
