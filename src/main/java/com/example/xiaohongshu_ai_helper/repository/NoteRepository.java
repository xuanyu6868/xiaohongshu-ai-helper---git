package com.example.xiaohongshu_ai_helper.repository;

import com.example.xiaohongshu_ai_helper.model.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
    // 可根据需要扩展自定义查询
}
