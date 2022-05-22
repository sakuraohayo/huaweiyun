package javaweb.remember.utils;

import java.util.UUID;

public class ImageNameUtils {

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String reFileName(String fileOriginName, Long userId){
        return userId + "-image" + UUID.randomUUID() + ImageNameUtils.getSuffix(fileOriginName);
    }


}
