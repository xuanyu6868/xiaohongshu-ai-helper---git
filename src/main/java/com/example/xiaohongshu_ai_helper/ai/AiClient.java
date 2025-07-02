package com.example.xiaohongshu_ai_helper.ai;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * AI客户端服务，负责与DashScope API进行交互
 * 提供同步和流式生成功能
 * 
 * @author AI Assistant
 * @version 1.0
 */
@Slf4j
@Component
public class AiClient {

    private static final String DEFAULT_MODEL = "deepseek-v3";
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 1000L;
    
    private final Generation generation;
    private final String apiKey;
    private final String model;

    /**
     * 构造函数，通过Spring配置注入API密钥和模型
     * 
     * @param apiKey API密钥
     * @param model 模型名称
     */
    public AiClient(@Value("${spring.ai.openai.api-key}") String apiKey, 
                    @Value("${spring.ai.openai.chat.options.model:" + DEFAULT_MODEL + "}") String model) {
        this.generation = new Generation();
        this.apiKey = apiKey;
        this.model = model;
    }

    /**
     * 初始化后检查配置
     */
    @PostConstruct
    public void validateConfiguration() {
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalStateException("API密钥未配置，请检查application.yml中的spring.ai.openai.api-key配置");
        }
        if (!StringUtils.hasText(model)) {
            throw new IllegalStateException("模型名称未配置，请检查application.yml中的spring.ai.openai.chat.options.model配置");
        }
        log.info("AiClient初始化完成，使用模型: {}", model);
    }

    /**
     * 同步生成文本内容
     * 
     * @param prompt 输入提示词
     * @return 生成的文本内容
     * @throws AiClientException 当API调用失败时抛出
     */
    @Retryable(
        value = {ApiException.class, NoApiKeyException.class},
        maxAttempts = MAX_RETRY_ATTEMPTS,
        backoff = @Backoff(delay = RETRY_DELAY_MS, multiplier = 2.0)
    )
    public String generate(String prompt) {
        validatePrompt(prompt);
        
        long startTime = System.currentTimeMillis();
        log.debug("开始生成文本，提示词长度: {}", prompt.length());
        
        try {
            Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(prompt)
                .build();
                
            GenerationParam param = GenerationParam.builder()
                .apiKey(this.apiKey)
                .model(this.model)
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
                
            GenerationResult result = generation.call(param);
            String content = extractContent(result);
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("文本生成完成，耗时: {}ms，输出长度: {}", duration, content.length());
            
            return content;
            
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            log.error("API调用失败: {}", e.getMessage(), e);
            throw new AiClientException("生成文本失败: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("未知错误: {}", e.getMessage(), e);
            throw new AiClientException("生成文本时发生未知错误", e);
        }
    }

    /**
     * 流式生成文本内容
     * 
     * @param prompt 输入提示词
     * @return 生成结果的流
     * @throws AiClientException 当API调用失败时抛出
     */
    @Retryable(
        value = {ApiException.class, NoApiKeyException.class},
        maxAttempts = MAX_RETRY_ATTEMPTS,
        backoff = @Backoff(delay = RETRY_DELAY_MS, multiplier = 2.0)
    )
    public Flowable<GenerationResult> streamGenerate(String prompt) {
        validatePrompt(prompt);
        
        log.debug("开始流式生成文本，提示词长度: {}", prompt.length());
        
        try {
            Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(prompt)
                .build();
                
            GenerationParam param = GenerationParam.builder()
                .apiKey(this.apiKey)
                .model(this.model)
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .incrementalOutput(true)
                .build();
                
            return generation.streamCall(param)
                .doOnSubscribe(subscription -> log.info("开始流式生成"))
                .doOnNext(result -> log.debug("收到流式响应块"))
                .doOnComplete(() -> log.info("流式生成完成"))
                .doOnError(error -> log.error("流式生成出错: {}", error.getMessage()))
                .timeout(30, TimeUnit.SECONDS); // 30秒超时
                
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            log.error("流式API调用失败: {}", e.getMessage(), e);
            throw new AiClientException("流式生成文本失败: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("流式生成未知错误: {}", e.getMessage(), e);
            throw new AiClientException("流式生成文本时发生未知错误", e);
        }
    }

    /**
     * 验证输入提示词
     * 
     * @param prompt 提示词
     * @throws IllegalArgumentException 当提示词无效时抛出
     */
    private void validatePrompt(String prompt) {
        if (!StringUtils.hasText(prompt)) {
            throw new IllegalArgumentException("提示词不能为空");
        }
        if (prompt.length() > 10000) {
            throw new IllegalArgumentException("提示词长度不能超过10000个字符");
        }
    }

    /**
     * 从生成结果中提取内容
     * 
     * @param result 生成结果
     * @return 提取的文本内容
     * @throws AiClientException 当结果格式异常时抛出
     */
    private String extractContent(GenerationResult result) {
        try {
            if (result == null || result.getOutput() == null) {
                throw new AiClientException("API返回结果为空");
            }
            
            if (result.getOutput().getChoices() == null || result.getOutput().getChoices().isEmpty()) {
                throw new AiClientException("API返回的选择列表为空");
            }
            
            String content = result.getOutput().getChoices().get(0).getMessage().getContent();
            if (content == null) {
                throw new AiClientException("API返回的内容为空");
            }
            
            return content.trim();
            
        } catch (Exception e) {
            throw new AiClientException("解析API响应失败: " + e.getMessage(), e);
        }
    }

    /**
     * AI客户端异常类
     */
    public static class AiClientException extends RuntimeException {
        public AiClientException(String message) {
            super(message);
        }
        
        public AiClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}