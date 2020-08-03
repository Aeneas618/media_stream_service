package com.fokantech.media_stream_service.server;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@Component
@RestControllerAdvice
public class GLOBALCONFIGUTATION {

    public Map<String, Object> GLOBALCONFIGUTATION(Object status, Object code, Object message, Object data) {
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        map.put( "status",status);
        map.put( "code",code );
        map.put( "message",message);
        map.put( "data", data);
        result.put( "result",map );
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Map<String,Object> handler(Exception ex) {
        return GLOBALCONFIGUTATION("error",110086,ex.getMessage(),""  );
    }
}
