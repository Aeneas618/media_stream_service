package com.fokantech.media_stream_service.controller;

import com.fokantech.media_stream_service.configuration.CONFIGPARAM;
import com.fokantech.media_stream_service.server.GLOBALCONFIGUTATION;
import com.fokantech.media_stream_service.server.RECVMSGTASK;
import com.fokantech.media_stream_service.server.SYSTEM_THREAD;
import com.fokantech.media_stream_service.service.UNIVERSAL_SERVICE;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SYSTEMINFO_CONTROLLER {

    @Autowired private GLOBALCONFIGUTATION GLOBALCONFIGUTATION;
               private SYSTEM_THREAD THREAD = new SYSTEM_THREAD();
               private static final File file = new File("/opt/sms/webserver/loginfo/log.tar.gz");
    @Autowired private UNIVERSAL_SERVICE UNIVERSAL_SERVICE;
    @ApiOperation(value = "获取服务器基本信息")
    @GetMapping(value = "/system/computerinfo")
    public Map<String, Object> GET_COMPUTER_INFO()
    {
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION( "success","100252"," Access to basic computer information", THREAD.GET_COMPUTER_INFO_LIST() );
    }

    @ApiOperation(value = "获取CPU信息")
    @GetMapping(value = "/system/cpuinfo")
    public Map<String, Object> GET_CPU_INFO()
    {
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION( "success","100250","Access to computer cpu information",THREAD.GET_CPU_INFO_LIST());
    }

    @ApiOperation(value = "获取内存信息")
    @GetMapping(value = "/system/meminfo")
    public Map<String, Object> GET_MEMORY_INFO()
    {
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION( "success","100251","Get Computer Memory Information", THREAD.GET_MEMORY_INFO_LIST());
    }

    @ApiOperation(value = "获取磁盘信息")
    @GetMapping(value = "/system/disklistinfo")
    public Map<String, Object> GET_DISKLIST_INFO()
    {
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION( "success","100253","Access to computer disk information",THREAD.GET_DISK_INFO_LIST());
    }

    @ApiOperation(value = "获取网络信息")
    @GetMapping(value = "/system/network")
    public Map<String,Object> GET_NET_WORK() throws Exception {
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION( "success","100254","Collection of current network card data",THREAD.GET_NETWORK_INFO_LIST());
    }

    @GetMapping(value = "/system/version")
    public Map<String,Object> FIND_VIEW_VERSION()
    {
        List<Object> objects = new RECVMSGTASK().getObjects();
        List<Object> objs = new ArrayList<>();
        objs.add("1.0.1");
        Map<String,Object> map = new HashMap<>();
        map.put("controller",objects);
        map.put("webservice",objs);
        map.put("pageversion",new CONFIGPARAM().GET_PACKAGE_VERSION());
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100013,"The query successfully",map);
    }

    @GetMapping(value = "/system/logs/clear")
    public Map<String,Object> GET_CLEAR_LOGS()
    {
        File[] files = new File("../logs").listFiles();
        for (File file1 : files) {
            String file_update_time = UNIVERSAL_SERVICE.GET_FILE_LAST_UPDATE_TIME(file1);
            if (!file_update_time.equals(UNIVERSAL_SERVICE.GET_DATE_TIME())){
                file1.delete();
            }
        }
        return GLOBALCONFIGUTATION.GLOBALCONFIGUTATION("success",100016,"The deleted successfully","" );
    }

    @GetMapping(value = "/system/download")
    public void USE_DOWNLOAD_PACKAGE(HttpServletResponse response)
    {
        UNIVERSAL_SERVICE.EXECUTE_LINUX_CMD("tar -zcf loginfo/log.tar.gz ../logs/");
        if (file.exists()) {
            response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());
            byte[] buffer = new byte[1024*1024*1000];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    System.out.println( i);
                    i = bis.read(buffer);
                    System.out.println(i);
                }
                return ;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
