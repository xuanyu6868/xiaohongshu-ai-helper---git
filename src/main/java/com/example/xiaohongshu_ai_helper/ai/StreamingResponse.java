package com.example.xiaohongshu_ai_helper.ai;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
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
}