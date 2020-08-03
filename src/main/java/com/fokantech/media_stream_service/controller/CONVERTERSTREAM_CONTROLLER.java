package com.fokantech.media_stream_service.controller;

import com.fokantech.media_stream_service.configuration.CONFIGPARAM;
import com.fokantech.media_stream_service.model.CONVERTERSTREAM;
import com.fokantech.media_stream_service.server.GLOBALCONFIGUTATION;
import com.fokantech.media_stream_service.service.CONVERTERSTREAM_SERVICE;
import com.fokantech.media_stream_service.service.UNIVERSAL_SERVICE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
public class CONVERTERSTREAM_CONTROLLER {

    @Autowired private GLOBALCONFIGUTATION GLOBALCONFIGUTATION;
    @Autowired private CONVERTERSTREAM_SERVICE SERVICE;
    @Autowired private UNIVERSAL_SERVICE UNIVERSAL_SERVICE;

    @PostMapping("/streams/converter")
    public Map<String,Object> GET_CONVERTER_STREAM_INFO(
            @RequestParam("session_id") String source_session_id ,
            @RequestParam("protocol")String protocol,
            @RequestParam("url")String url){
        if (UNIVERSAL_SERVICE.CHECK_CONVERTER_or_SOURCE_PROTOCOL(url)){
            CONVERTERSTREAM converterstream = SERVICE.GET_CONVERTER_STREAM_ADD(source_session_id, protocol, url);
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100007,"The converter stream successfully",converterstream);
        }else {
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("error",100007,"IP address does not conform to this machine","");
        }
    }
    @PutMapping("/streams/converter/update")
    public Map<String,Object> updateDate(
            @RequestParam("session_id")String session_id,
            @RequestParam("url") String url)
    {
        CONVERTERSTREAM converterstream = SERVICE.UPDATE_CONVERTER_STREAM(session_id, url);
        if (converterstream!=null){
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION(
                    "success",100010,"The update successfully",converterstream
            );
        }
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION(
                "error",100009,"The update failed",converterstream
        );
    }

    @GetMapping("/streams/converter/view")
    public Map<String,Object> queryView(@RequestParam(value = "session_id") String session_id)
    {
        List<CONVERTERSTREAM> converterstreams = SERVICE.GET_CONVERTER_STREAM_VIEW_ONLY(session_id);
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100014,"The query successfully",converterstreams);
    }

    @GetMapping("/streams/converter/all")
    public Map<String,Object> queryAll() {
        List<CONVERTERSTREAM> converterstreams = SERVICE.GET_CONVERTER_STREAM_ALL_INFO();
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100014,"The query successfully",converterstreams);
    }

    @DeleteMapping("/streams/converter/delete")
    public Map<String,Object> delConverterStream(@RequestParam(value = "session_id")String session_id){
        SERVICE.GET_CONVERTER_SESSION_ID_DELETE(session_id);
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100016,"The deleted successfully","");
    }

    @PostMapping("/streams/converter/stop")
    public Map<String,Object> stopConverterStream(@RequestParam(value = "session_id")String session_id,
                                                  @RequestParam(value = "source_session_id")String source_session_id ){
        SERVICE.GET_CONVERTER_STREAM_UPDATE_STATUS(session_id, CONFIGPARAM.CONVERTER_STREAM_STATUS);
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("successs",100121,"Current conversion stream stopped","");
    }

    @PostMapping("/streams/converter/start")
    public Map<String,Object> start(@RequestParam(value = "session_id")String session_id,
                                    @RequestParam(value = "source_session_id")String source_session_id ){
        SERVICE.GET_CONVERTER_STREAM_UPDATE_STATUS(session_id, CONFIGPARAM.SOURCES_STREAM_STATUS);
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("successs",100122,"Current conversion stream start","");
    }


}
