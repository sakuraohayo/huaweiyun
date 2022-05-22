package javaweb.remember.controller;

import javaweb.remember.entity.Memory;
import javaweb.remember.enumeration.MemoryTagsEnum;
import javaweb.remember.enumeration.ResultEnum;
import javaweb.remember.service.MemoryService;
import javaweb.remember.service.PhotoService;
import javaweb.remember.service.RedisService;
import javaweb.remember.utils.DataBaseArrayUtils;
import javaweb.remember.utils.ImageNameUtils;
import javaweb.remember.vo.MemoryVo;
import javaweb.remember.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MemoryController {

    @Autowired
    private MemoryService memoryService;

    @Autowired
    private PhotoService photoService;

    @PostMapping("/saveMemory")
    public ResultVo save(@RequestParam("memoryName") String title,
                         @RequestParam("labelList") String[] tags,
                         @RequestParam("content") String content,
                         @RequestParam("images") MultipartFile[] images,
                         HttpServletRequest request){

        ResultVo resultVo;
        Map<String, Object> memoryId = new HashMap<>();
        String[] allImages = new String[images.length];
        Long userId = (Long)request.getAttribute("id");
        String path = System.getProperty("user.dir") + "/image/";
        int num = 0;

        //图片储存
        for(MultipartFile image:images){
            String newImageName = ImageNameUtils.reFileName(image.getOriginalFilename(), userId);
            File dest = new File(path + "/" + newImageName);
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            try {
                image.transferTo(dest); //保存文件
                allImages[num] = newImageName;
                num++;
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                return new ResultVo(-100, e.getMessage(), null);
            }
        }

        //数据存入数据库
        Memory m = new Memory();
        Memory m2;

        m.setTags(DataBaseArrayUtils.ArrayToString(tags));
        m.setTitle(title);
        m.setContent(content);
        m.setCreateTime(new Date(new java.util.Date().getTime()));
        m.setCreator(userId);
        m.setImages(DataBaseArrayUtils.ArrayToString(allImages));
        m2 = memoryService.save(m);

        if(m2 != null){
            resultVo = new ResultVo(ResultEnum.REMEMBER_PUBLISH_SUCCESS);
            memoryId.put("memoryID",m2.getId());
            resultVo.setData(memoryId);
        }
        else{
            resultVo = new ResultVo(ResultEnum.REMEMBER_PUBLISH_FAIL);
        }
        return resultVo;
    }


    @PostMapping("/memoryShow")
    public ResultVo memoryShow(@RequestParam("memoryID") Long memoryId){

        Map<String,Object> memory = new HashMap<>();
        ResultVo resultVo;
        Memory m = memoryService.findById(memoryId);
        if(m == null){
            resultVo = new ResultVo(ResultEnum.MEMORY_SHOW_FAIL);
            return resultVo;
        }

        memory.put("name",m.getTitle());
        memory.put("labelList",DataBaseArrayUtils.StringToArray(m.getTags()));
        memory.put("content",m.getContent());
        memory.put("imgUrls",DataBaseArrayUtils.StringToArray(m.getImages()));
        memory.put("time",m.getCreateTime());

        resultVo = new ResultVo(ResultEnum.MEMORY_SHOW_SUCCESS);
        resultVo.setData(memory);
        return resultVo;
    }

    @GetMapping(value = "/get-photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getPhotoTest(@RequestParam("photo_name") String photo_name) throws Exception {
        //String photo_path = "K:\\Java Projects\\I-Remember\\image\\" + photo_name;
        String photo_path = System.getProperty("user.dir") + "/image/" + photo_name;
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

    @PostMapping("/getTags")
    public ResultVo getTags(){
        ResultVo resultVo;
        Map<String, Object> map = new HashMap<>();
        String[] tagsArray = new String[MemoryTagsEnum.values().length];
        int num = 0;

        for (MemoryTagsEnum e : MemoryTagsEnum.values()) {
            tagsArray[num] = e.getTag();
            num++;
        }

        map.put("tabs",tagsArray);
        resultVo = new ResultVo(ResultEnum.TAGS_GET_SUCCESS);
        resultVo.setData(map);

        return resultVo;
    }

    @PostMapping("/getCreatorAllMemory")
    public ResultVo getCreatorAllMemory(HttpServletRequest request){
        ResultVo resultVo = new ResultVo();
        List<Map<String,Object>> mapList = new ArrayList<>();

        Long id = (Long)request.getAttribute("id");
        List<Memory> memoryList = memoryService.findAllByCreator(id);

        for(Memory m: memoryList){
            Map<String,Object> map = new HashMap<>();
            map.put("id",m.getId());
            map.put("tags",DataBaseArrayUtils.StringToArray(m.getTags()));
            map.put("title",m.getTitle());
            map.put("content",m.getContent());
            map.put("images",DataBaseArrayUtils.StringToArray(m.getImages()));
            map.put("creator",m.getCreator());
            map.put("createTime",m.getCreateTime());
            mapList.add(map);
        }

        Map<String,Object> temp = new HashMap<>();
        temp.put("memoryList",mapList);

        resultVo.setCode(30);
        resultVo.setMessage("获取用户全部记忆成功");
        resultVo.setData(temp);

        return resultVo;
    }

    @PostMapping("/randomMemory")
    public ResultVo randomMemory(){
        ResultVo resultVo;
        Map<String,Object> map1 = new HashMap<>();
        Map<String,Object> map2 = new HashMap<>();

        Memory m = memoryService.randomMemory();
        map1.put("photo",DataBaseArrayUtils.StringToArray(m.getImages()));
        map1.put("content",m.getContent());
        map2.put("Memory",map1);

        resultVo = new ResultVo(ResultEnum.RANDOM_MEMORY_SUCCESS);
        resultVo.setData(map2);

        return resultVo;
    }

    @PostMapping("/searchMemory")
    public ResultVo searchMemory(@RequestParam("searchStr") String searchStr,
                                     HttpServletRequest request){
        ResultVo resultVo = new ResultVo();
        List<Map<String,Object>> mapList = new ArrayList<>();

        Long id = (Long)request.getAttribute("id");
        List<Memory> memoryList = memoryService.searchMemory(searchStr);

        for(Memory m: memoryList){
            Map<String,Object> map = new HashMap<>();
            map.put("id",m.getId());
            map.put("tags",DataBaseArrayUtils.StringToArray(m.getTags()));
            map.put("title",m.getTitle());
            map.put("content",m.getContent());
            map.put("images",DataBaseArrayUtils.StringToArray(m.getImages()));
            map.put("creator",m.getCreator());
            map.put("createTime",m.getCreateTime());
            mapList.add(map);
        }

        Map<String,Object> temp = new HashMap<>();
        temp.put("memoryList",mapList);

        resultVo.setCode(40);
        resultVo.setMessage("搜索记忆成功");
        resultVo.setData(temp);

        return resultVo;
    }

    @PostMapping("/deleteMemory")
    public ResultVo deleteMemory(HttpServletRequest request, @RequestParam("memoryID") Long[] memoryIds){
        // 待返回结果
        ResultVo resultVo = new ResultVo();
        // 获取用户id
        Long userID = (Long)request.getAttribute("id");
        for (Long memoryId:memoryIds){
            // 获取记忆详情
            Memory memory;
            try {
                memory = memoryService.findById(memoryId);
            }
            catch (Exception e){
                e.printStackTrace();
                resultVo.setCode(31);
                resultVo.setMessage("记忆不存在");
                resultVo.setData("");
                return resultVo;
            }
            // 核对记忆拥有者
            if (!memory.getCreator().equals(userID)){
                resultVo.setCode(32);
                resultVo.setMessage("权限不足");
                resultVo.setData("");
                return resultVo;
            }
            // 删除记忆
            if (memoryService.deleteMemoryByID(memoryId)){
                resultVo.setCode(33);
                resultVo.setMessage("删除成功");
            }
            else {
                resultVo.setCode(31);
                resultVo.setMessage("权限不足");
            }
            resultVo.setData("");
        }
        return resultVo;
    }

    @PostMapping("/findAllMemory")
    public ResultVo findAllMemory(){
        ResultVo resultVo;
        List<MemoryVo> memoryVoList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        List<Memory> memoryList = memoryService.findAll();
        for(Memory m: memoryList){
            MemoryVo memoryVo = new MemoryVo();
            memoryVo.setId(m.getId());
            memoryVo.setContent(m.getContent());
            memoryVo.setTitle(m.getTitle());
            memoryVo.setCreator(m.getCreator());
            memoryVo.setCreateTime(m.getCreateTime());
            memoryVo.setImages(DataBaseArrayUtils.StringToArray(m.getImages()));
            memoryVo.setTags(DataBaseArrayUtils.StringToArray(m.getTags()));

            memoryVoList.add(memoryVo);
        }

        map.put("length",memoryList.size());
        map.put("Memory",memoryVoList);
        resultVo = new ResultVo(ResultEnum.GET_ALL_MEMORY_SUCCESS);
        resultVo.setData(map);

        return resultVo;
    }
}
