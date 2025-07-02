//这个类专门用来封装接口的返回数据
package com.example.xiaohongshu_ai_helper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {
    private String note;// 小红书笔记内容
    private List<String> titles;// 推荐标题列表
    private List<String> tags;// 标签列表
    private String analysis;// 产品分析
}
