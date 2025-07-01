package com.example.xiaohongshu_ai_helper.controller;

import com.example.xiaohongshu_ai_helper.model.NoteRequest;
import com.example.xiaohongshu_ai_helper.model.NoteResponse;
import com.example.xiaohongshu_ai_helper.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") // 生产环境应指定具体前端地址
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/generate")
    public ResponseEntity<NoteResponse> generateNote(@RequestBody NoteRequest request) {
        NoteResponse response = noteService.generateNote(
                request.getName(),
                request.getDescription(),
                request.getCategory(),
                request.getPrice(),
                request.getTargetUser(),
                request.getScenario());
        // 确保返回的字段与前端匹配
        return ResponseEntity.ok()
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(response);
    }
}
