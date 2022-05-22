package javaweb.remember.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
public class Memory {
    /**
     * 记忆id，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 记忆标签（数组）
     */
    private String tags;
    /**
     * 记忆标题
     */
    private String title;
    /**
     * 记忆内容（映射数据库类型为TEXT）
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    /**
     * 图片数组
     */
    private String images;
    /**
     * 发布者id
     */
    private Long creator;
    /**
     * 创建时间
     */
    private Date createTime;
}
