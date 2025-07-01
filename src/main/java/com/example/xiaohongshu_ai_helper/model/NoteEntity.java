package com.example.xiaohongshu_ai_helper.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "note")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String content;

    // 标签用逗号分隔保存数据库
    private String tags;

    // 关联产品名称，方便查询
    private String productName;

    private String category;

    private double price;
}
