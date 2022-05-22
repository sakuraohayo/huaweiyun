package javaweb.remember.vo;

import lombok.Data;

import java.sql.Date;

@Data
public class MemoryVo {

    private Long id;
    private String[] tags;
    private String title;
    private String content;
    private String[] images;
    private Long creator;
    private Date createTime;
}
