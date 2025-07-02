//用于前端 POST 请求时传递的产品参数
package com.example.xiaohongshu_ai_helper.model;

import lombok.Data;

@Data
public class NoteRequest {
    private String name;
    private String description;// 产品描述
    private String category;// 产品分类
    private double price;// 产品价格
    private String targetUser;// 目标用户（可选）
    private String scenario;// 使用场景（可选）
}
