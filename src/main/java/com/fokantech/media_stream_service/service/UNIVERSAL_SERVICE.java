package com.fokantech.media_stream_service.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class UNIVERSAL_SERVICE {

    public Integer SOURCES_STREAM_STATUS;
    public Integer CONVERTER_STREAM_STATUS;

    public String GET_SOURCE_SESSION_ID() {
        String s = UUID.randomUUID().toString();
        return s.replace("-", "").substring( 0, 16 );
    }

    public String GET_CONVERTER_SESSION_ID(){
        return String.valueOf( new Date(  ).getTime() );
    }
    @SneakyThrows
    public boolean CHECK_SOURCE_PROTOCOL(String url){
        String[] INDEX_FIRST = url.split("//",2);
        String[] SECOUND = INDEX_FIRST[1].split("/", 2);
        String[] IPADDRS = SECOUND[0].split("@", 2);
        String addr = IPADDRS[1].substring(0, IPADDRS[1].indexOf(":"));
        String ip = InetAddress.getLocalHost().getHostAddress();
        if (ip.equals(addr)){
            return true;
        }else {
            return false;
        }
    }
    @SneakyThrows
    public boolean CHECK_CONVERTER_or_SOURCE_PROTOCOL(String url){
        String substring = url.substring(0, url.indexOf("//"));
        String[] split = substring.split("/");
        if (split[0].equals(InetAddress.getLocalHost().getHostAddress())){
            return true;
        }
        return false;
    }

    public String GET_FILE_LAST_UPDATE_TIME(File f){
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }
    public String GET_DATE_TIME() {
        Date date = new Date();
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    public String EXECUTE_LINUX_CMD(String cmd) {
        System.out.println("执行命令[ " + cmd + "]");
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(cmd);
            String line;
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer out = new StringBuffer();
            while ((line = stdoutReader.readLine()) != null ) {
                out.append(line);
            }
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            process.destroy();
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
