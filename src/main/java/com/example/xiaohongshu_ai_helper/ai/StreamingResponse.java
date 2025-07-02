package com.example.xiaohongshu_ai_helper.ai;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public class StreamingResponse extends SseEmitter {

    public <T> StreamingResponse(io.reactivex.Flowable<T> source, Function<T, String> onNext) {
        source.subscribe(
                data -> {
                    try {
                        send(SseEmitter.event().data(onNext.apply(data)));
                    } catch (IOException e) {
                        completeWithError(e);
                    }
                },
                this::completeWithError,
                this::complete
        );
    }

    public <T> StreamingResponse(io.reactivex.Flowable<T> source, Function<T, String> onNext, Consumer<String> onComplete) {
        StringBuilder completeContent = new StringBuilder();
        source.subscribe(
                data -> {
                    try {
                        String content = onNext.apply(data);
                        completeContent.append(content);
                        send(SseEmitter.event().data(content));
                    } catch (IOException e) {
                        completeWithError(e);
                    }
                },
                this::completeWithError,
                () -> {
                    try {
                        onComplete.accept(completeContent.toString());
                    } catch (Exception e) {
                        System.err.println("完成回调执行失败: " + e.getMessage());
                    }
                    this.complete();
                }
        );
    }
}