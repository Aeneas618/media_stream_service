package com.fokantech.media_stream_service.configuration;

import lombok.SneakyThrows;
import org.dtools.ini.*;

import java.io.File;

public class CONFIGPARAM {

    public static final Integer SOURCES_STREAM_STATUS = 1;
    public static final Integer CONVERTER_STREAM_STATUS = 0;
    private static String SETTINGCONF = "/opt/sms/config/media_service_setting.ini";

    public String defaultSetting()
    {
        if (checkFile() == false){
            creatTxtFile();
            return readContent();
        }else {
            return readContent();
        }
    }

    @SneakyThrows
    private String readContent()
    {
        IniFile iniFile=new BasicIniFile();
        File file=new File(SETTINGCONF);
        IniFileReader rad=new IniFileReader(iniFile, file);
        IniFileWriter wir=new IniFileWriter(iniFile, file);
        rad.read();
        IniSection iniSection=iniFile.getSection(0);
        IniItem iniItem=iniSection.getItem("networkcard");
        String name=iniItem.getValue();
        iniSection.addItem(iniItem);
        iniFile.addSection(iniSection);
        wir.write();
        return name;
    }

    @SneakyThrows
    private void creatTxtFile()
    {
        IniFile iniFile=new BasicIniFile();
        IniSection dataSection=new BasicIniSection("media_stream_service  Configuration");
        iniFile.addSection(dataSection);
        IniItem nameIniItem=new IniItem("networkcard");
        nameIniItem.setValue("em1");
        dataSection.addItem(nameIniItem);
        IniItem nameIniItem1=new IniItem("pageversion");
        nameIniItem1.setValue("1.0.0");
        dataSection.addItem(nameIniItem1);
        File file=new File(SETTINGCONF);
        IniFileWriter niFileWriter=new IniFileWriter(iniFile, file);
        niFileWriter.write();
    }

    private boolean checkFile()
    {
        boolean flag = false;
        File filename = new File(SETTINGCONF);
        if (!filename.exists()) {
            return flag;
        }
        flag = true;
        return flag;
    }

    @SneakyThrows
    public String GET_PACKAGE_VERSION()
    {
        IniFile iniFile=new BasicIniFile();
        File file=new File(SETTINGCONF);
        IniFileReader rad=new IniFileReader(iniFile, file);
        IniFileWriter wir=new IniFileWriter(iniFile, file);
        rad.read();
        IniSection iniSection=iniFile.getSection(0);
        IniItem iniItem=iniSection.getItem("pageversion");
        String name=iniItem.getValue();
        iniSection.addItem(iniItem);
        iniFile.addSection(iniSection);
        wir.write();
        return name;
    }
}
