package com.example.xiaohongshu_ai_helper.controller;

import com.example.xiaohongshu_ai_helper.ai.StreamingResponse;
import com.example.xiaohongshu_ai_helper.model.NoteRequest;
import com.example.xiaohongshu_ai_helper.model.NoteResponse;
import com.example.xiaohongshu_ai_helper.service.NoteService;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/stream-generate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public StreamingResponse streamGenerateNote(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false, defaultValue = "") String category,
            @RequestParam(required = false, defaultValue = "0") double price,
            @RequestParam(required = false, defaultValue = "") String targetUser,
            @RequestParam(required = false, defaultValue = "") String scenario) {
        return noteService.streamGenerateNote(name, description, category, price, targetUser, scenario);
    }

    @GetMapping(value = "/stream-analysis", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public StreamingResponse streamGenerateAnalysis(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false, defaultValue = "") String category,
            @RequestParam(required = false, defaultValue = "0") double price,
            @RequestParam(required = false, defaultValue = "") String targetUser,
            @RequestParam(required = false, defaultValue = "") String scenario) {
        return noteService.streamGenerateAnalysis(name, description, category, price, targetUser, scenario);
    }

    @GetMapping(value = "/stream-titles", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public StreamingResponse streamGenerateTitles(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false, defaultValue = "") String category) {
        return noteService.streamGenerateTitles(name, description, category);
    }

    @GetMapping(value = "/stream-tags", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public StreamingResponse streamGenerateTags(
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "") String category) {
        return noteService.streamGenerateTags(name, category);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getNoteHistory() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }
}