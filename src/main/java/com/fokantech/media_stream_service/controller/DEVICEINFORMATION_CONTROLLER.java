package com.fokantech.media_stream_service.controller;

import com.fokantech.media_stream_service.configuration.PageResult;
import com.fokantech.media_stream_service.model.DEVICEINFORMATION;
import com.fokantech.media_stream_service.server.GLOBALCONFIGUTATION;
import com.fokantech.media_stream_service.service.DEVICEINFORMATION_SERVICE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class DEVICEINFORMATION_CONTROLLER {
    @Autowired private DEVICEINFORMATION_SERVICE SERVICE;
    @Autowired private GLOBALCONFIGUTATION GLOBALCONFIGUTATION;
    @GetMapping("/devices")
    public Map<String, Object> VIEW_DEVICES(@RequestParam(value = "page", defaultValue = "1") Integer page ,
                                            @RequestParam(value = "per-page",defaultValue = "5") Integer perPage ,
                                            @RequestParam(value = "search-keyword",required = false) String searchKeyword)
    {
        PageResult<DEVICEINFORMATION> deviceinformationPageResult = SERVICE.GET_DEVICES_ALL(perPage, page, searchKeyword);
        System.out.println(deviceinformationPageResult);
        if (deviceinformationPageResult == null){
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("error",100013,"The query failed","");
        }
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100014,"The query successfully",deviceinformationPageResult);

    }

    @PostMapping("/devices/create")
    public Map<String, Object> GET_VIEW_DEVICE(@RequestParam(value = "device_name") String dveice_name)
    {
        if (SERVICE.GET_VIEW_DEVICE(dveice_name) == null){
            DEVICEINFORMATION DEVICEINFORMATION = SERVICE.ADD_DEVICE_INFORMATION(dveice_name);
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100001,"The add device successfully" ,DEVICEINFORMATION);
        }else {
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION( "error",100120,
                    "The current device already exists and can not be repeated" ,"");
        }
    }

    @PutMapping("/devices/update")
    public Map<String, Object> updateDeviceDatum(@RequestParam(value = "id") Integer id,
                                                 @RequestParam(value = "device_name") String device_name)
    {
        DEVICEINFORMATION deviceinformation = SERVICE.UPDATE_VIEW_DEVICE_BYID(id, device_name);
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100010,"The update successfully",deviceinformation);
    }

    @DeleteMapping("/devices/delete")
    public Map<String, Object> deleteByIdDevice(@RequestParam(value = "id") Integer id){
        boolean b = SERVICE.DELETE_VIEW_DEVICE_BYID(id);
        if (b) {
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success", 100016, "The deleted successfully", "");
        }else {//The device currently intended to be deleted does not exist and is being verified
            return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("error", 100016, "The device currently intended to be deleted does not exist and is being verified", "");
        }
    }
}
