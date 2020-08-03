package com.fokantech.media_stream_service.service;

import com.fokantech.media_stream_service.configuration.PageResult;
import com.fokantech.media_stream_service.mapper.DEVICEINFORMATION_MAPPER;
import com.fokantech.media_stream_service.model.DEVICEINFORMATION;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Slf4j
@Service
public class DEVICEINFORMATION_SERVICE {

    @Autowired private DEVICEINFORMATION_MAPPER MAPPER;


    public PageResult<DEVICEINFORMATION> GET_DEVICES_ALL(Integer perPage, Integer page, String searchKeyword)
    {
        PageHelper.startPage(page,perPage);
        Example example = new Example(DEVICEINFORMATION.class);
        if (!searchKeyword.equals( "" )){
            example.createCriteria().orLike("device_name", "%"+searchKeyword+"%");
        }
        List<DEVICEINFORMATION> DEVICES = MAPPER.selectByExample(example);
        if (CollectionUtils.isEmpty(DEVICES)) {
            return null;
        }
        PageInfo<DEVICEINFORMATION> info = new PageInfo<>(DEVICES);
        PageResult<DEVICEINFORMATION> pageResult = new PageResult<>( info.getTotal(), info.getPages(), info.getPageNum(), info.getPageSize(), DEVICES );
        return pageResult;

    }

    public DEVICEINFORMATION GET_VIEW_DEVICE(String dveice_name)
    {
        DEVICEINFORMATION DEVICEINFORMATION = new DEVICEINFORMATION();
        DEVICEINFORMATION.setDevice_name(dveice_name);
        return MAPPER.selectOne(DEVICEINFORMATION);
    }

    public DEVICEINFORMATION ADD_DEVICE_INFORMATION(String dveice_name) {
        DEVICEINFORMATION DEVICEINFORMATION = new DEVICEINFORMATION();
        DEVICEINFORMATION.setDevice_name(dveice_name);
        MAPPER.insert(DEVICEINFORMATION);
        return DEVICEINFORMATION;
    }

    public DEVICEINFORMATION UPDATE_VIEW_DEVICE_BYID(Integer id, String device_name) {
        DEVICEINFORMATION deviceinformation = MAPPER.selectByPrimaryKey(id);
        deviceinformation.setDevice_name(device_name);
        MAPPER.updateByPrimaryKey(deviceinformation);
        return deviceinformation;
    }

    public boolean DELETE_VIEW_DEVICE_BYID(Integer id) {
        DEVICEINFORMATION deviceinformation = MAPPER.selectByPrimaryKey(id);
        if (deviceinformation !=null){
            MAPPER.deleteByPrimaryKey(id);
            return true;
        }else {
            log.info("The device currently intended to be deleted does not exist and is being verified");
            return false;
        }

    }

    public DEVICEINFORMATION GET_VIEW_DEVICE_BYID(Integer device_id) {
        return MAPPER.selectByPrimaryKey(device_id);
    }
}
