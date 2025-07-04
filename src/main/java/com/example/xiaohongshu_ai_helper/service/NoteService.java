package com.example.xiaohongshu_ai_helper.service;

import com.example.xiaohongshu_ai_helper.ai.AiClient;
import com.example.xiaohongshu_ai_helper.ai.PromptBuilder;
import com.example.xiaohongshu_ai_helper.ai.StreamingResponse;
import com.example.xiaohongshu_ai_helper.model.NoteEntity;
import com.example.xiaohongshu_ai_helper.model.NoteResponse;
import com.example.xiaohongshu_ai_helper.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class NoteService {

    private final AiClient aiClient;
    private final PromptBuilder promptBuilder;
    private final NoteRepository noteRepository;

    public NoteService(AiClient aiClient, PromptBuilder promptBuilder, NoteRepository noteRepository) {
        this.aiClient = aiClient;
        this.promptBuilder = promptBuilder;
        this.noteRepository = noteRepository;
    }

    public NoteResponse generateNote(String name, String description, String category, double price,
                                     String targetUser, String scenario) {
        // 生成小红书笔记内容
        String notePrompt = promptBuilder.buildNotePrompt(name, description, category, price, targetUser,
                scenario);
        String note = aiClient.generate(notePrompt).trim();

        // 生成多个推荐标题
        String titlesPrompt = promptBuilder.buildMultipleTitlesPrompt(name, description, category);
        String titlesStr = aiClient.generate(titlesPrompt).trim();
        List<String> titles = Arrays.stream(titlesStr.split("\\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .limit(5) // 限制最多5个标题
                .toList();

        // 生成标签
        String tagsPrompt = promptBuilder.buildTagsPrompt(name, category);
        String tagsStr = aiClient.generate(tagsPrompt).trim();
        List<String> tags = Arrays.stream(tagsStr.split("[,，]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        // 生成产品分析
        String analysisPrompt = promptBuilder.buildAnalysisPrompt(name, description, category, price,
                targetUser, scenario);
        String analysis = aiClient.generate(analysisPrompt).trim();

        // 保存到数据库，使用第一个标题作为主标题
        NoteEntity entity = new NoteEntity();
        entity.setTitle(titles.isEmpty() ? name : titles.get(0));
        entity.setContent(note);
        entity.setTags(String.join(",", tags));
        entity.setProductName(name);
        entity.setCategory(category);
        entity.setPrice(price);

        noteRepository.save(entity);

        return new NoteResponse(note, titles, tags, analysis);
    }

    public StreamingResponse streamGenerateNote(String name, String description, String category, double price,
                                                     String targetUser, String scenario) {
        String prompt = buildNotePrompt(name, description, category, price, targetUser, scenario);
        return new StreamingResponse(aiClient.streamGenerate(prompt), (com.alibaba.dashscope.aigc.generation.GenerationResult generationResult) -> {
            return generationResult.getOutput().getChoices().get(0).getMessage().getContent();
        }, (String completeContent) -> {
            // 流式生成完成后的回调，保存到数据库
            saveNoteToDatabase(name, completeContent, category, price);
        });
    }

    public StreamingResponse streamGenerateAnalysis(String name, String description, String category, double price,
                                                    String targetUser, String scenario) {
        String prompt = buildAnalysisPrompt(name, description, category, price, targetUser, scenario);
        return new StreamingResponse(aiClient.streamGenerate(prompt), (com.alibaba.dashscope.aigc.generation.GenerationResult generationResult) -> {
            return generationResult.getOutput().getChoices().get(0).getMessage().getContent();
        });
    }

    public StreamingResponse streamGenerateTitles(String name, String description, String category) {
        String prompt = buildTitlesPrompt(name, description, category);
        return new StreamingResponse(aiClient.streamGenerate(prompt), (com.alibaba.dashscope.aigc.generation.GenerationResult generationResult) -> {
            return generationResult.getOutput().getChoices().get(0).getMessage().getContent();
        });
    }

    public StreamingResponse streamGenerateTags(String name, String category) {
        String prompt = buildTagsPrompt(name, category);
        return new StreamingResponse(aiClient.streamGenerate(prompt), (com.alibaba.dashscope.aigc.generation.GenerationResult generationResult) -> {
            return generationResult.getOutput().getChoices().get(0).getMessage().getContent();
        });
    }
    
    private String buildNotePrompt(String name, String description, String category, double price, String targetUser, String scenario) {
        return promptBuilder.buildNotePrompt(name, description, category, price, targetUser, scenario);
    }

    private String buildAnalysisPrompt(String name, String description, String category, double price, String targetUser, String scenario) {
        return promptBuilder.buildAnalysisPrompt(name, description, category, price, targetUser, scenario);
    }

    private String buildTitlesPrompt(String name, String description, String category) {
        return promptBuilder.buildMultipleTitlesPrompt(name, description, category);
    }

    private String buildTagsPrompt(String name, String category) {
        return promptBuilder.buildTagsPrompt(name, category);
    }

    private void saveNoteToDatabase(String name, String content, String category, double price) {
        try {
            // 生成标题和标签用于保存
            String titlesPrompt = promptBuilder.buildMultipleTitlesPrompt(name, "", category);
            String titlesStr = aiClient.generate(titlesPrompt).trim();
            List<String> titles = Arrays.stream(titlesStr.split("\\n"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .limit(5)
                    .toList();

            String tagsPrompt = promptBuilder.buildTagsPrompt(name, category);
            String tagsStr = aiClient.generate(tagsPrompt).trim();
            List<String> tags = Arrays.stream(tagsStr.split("[,，]"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();

            // 保存到数据库
            NoteEntity entity = new NoteEntity();
            entity.setTitle(titles.isEmpty() ? name : titles.get(0));
            entity.setContent(content);
            entity.setTags(String.join(",", tags));
            entity.setProductName(name);
            entity.setCategory(category);
            entity.setPrice(price);

            noteRepository.save(entity);
        } catch (Exception e) {
            // 记录错误但不影响主流程
            System.err.println("保存到数据库失败: " + e.getMessage());
         }
     }

     public List<NoteEntity> getAllNotes() {
         return noteRepository.findAll();
     }
 }