package com.fokantech.media_stream_service.controller;

import com.fokantech.media_stream_service.configuration.PageResult;
import com.fokantech.media_stream_service.model.DEVICEINFORMATION;
import com.fokantech.media_stream_service.model.STREAMINFORMATION;
import com.fokantech.media_stream_service.server.GLOBALCONFIGUTATION;
import com.fokantech.media_stream_service.service.DEVICEINFORMATION_SERVICE;
import com.fokantech.media_stream_service.service.STREAMINFORMATION_SERVICE;
import com.fokantech.media_stream_service.service.UNIVERSAL_SERVICE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class STREAMINFORMATION_CONTROLLER {

    @Autowired private DEVICEINFORMATION_SERVICE DEVICEINFORMATION_SERVICE;
    @Autowired private STREAMINFORMATION_SERVICE SERVICE;
    @Autowired private GLOBALCONFIGUTATION GLOBALCONFIGUTATION;

    @GetMapping("/streams")
    public Map<String, Object> GET_VIEW_STREAMS_ALL(@RequestParam(value = "page", defaultValue = "1") Integer page ,
                                                     @RequestParam(value = "per-page",defaultValue = "5") Integer perPage , @RequestParam(value = "search-keyword",required = false) String searchKeyword){
        PageResult<STREAMINFORMATION> pageResult = SERVICE.GET_VIEW_ALL(perPage, page, searchKeyword);
        if (pageResult == null){
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("error",100013,"The query failed","");
        }
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100014,"The query successfully",pageResult);
    }

    @PostMapping("/streams/create")
    public Map<String, Object> GET_VIEW_STREAMS_ADD(@RequestParam(value = "device_id")Integer device_id,
        @RequestParam(value = "type")Integer type,@RequestParam(value = "protocol")String protocol,
         @RequestParam(value = "url") String url){
        DEVICEINFORMATION deviceinformation = DEVICEINFORMATION_SERVICE.GET_VIEW_DEVICE_BYID(device_id);
        if (deviceinformation == null){
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("error", 100012, "The add failed,No equipment", "");
        }
        STREAMINFORMATION streaminformation = SERVICE.GET_STREAMS_ADD(device_id, protocol, type, url);
        if (streaminformation != null) {
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success", 100011, "The add successfully", streaminformation);
        }else {
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("error",100012,"The add failed","");
        }
    }

    @GetMapping("/streams/view")
    public Map<String, Object> GET_VIEW_STREAM_ONLY(@RequestParam(value = "session_id")String session_id){
        List<STREAMINFORMATION> streaminformations = SERVICE.GET_VIEW_STREAM_ONLY(session_id);
        if (streaminformations.size()>0 || streaminformations != null){
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100011,"The add successfully",streaminformations.get(0));
        }
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("error",100012,"The add failed","");
    }

    @PutMapping("/streams/update")
    public Map<String, Object> updateStream(@RequestParam(value = "session_id")String session_id,
                                            @RequestParam(value = "type")Integer type,
                                            @RequestParam(value = "protocol")String protocol,
                                            @RequestParam(value = "url")String url){
        STREAMINFORMATION streaminformation = SERVICE.UPDATE_VIEW_STREAM(session_id, type, protocol, url);
        if (streaminformation != null){
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100010,"The update successfully",streaminformation);
        }
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("error",100009,"The update failed","");
    }

    @DeleteMapping("/streams/delete")
    public Map<String, Object> GET_VIEW_STREAMS_DELETE(@RequestParam(value = "session_id")String session_id){
        SERVICE.GET_STREAMS_DELETE_ONLY(session_id);
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100016,"The deleted successfully","");
    }

}
