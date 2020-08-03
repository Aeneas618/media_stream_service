package com.fokantech.media_stream_service.service;

import com.fokantech.media_stream_service.configuration.PageResult;
import com.fokantech.media_stream_service.mapper.STREAMINFORMATION_MAPPER;
import com.fokantech.media_stream_service.model.STREAMINFORMATION;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import java.util.List;

@Service
public class STREAMINFORMATION_SERVICE {

    @Autowired private STREAMINFORMATION_MAPPER MAPPER;
    @Autowired private UNIVERSAL_SERVICE UNIVERSAL_SERVICE;
    public PageResult<STREAMINFORMATION> GET_VIEW_ALL(Integer perPage, Integer page, String searchKeyword) {
        PageHelper.startPage( page,perPage);
        Example example = new Example(STREAMINFORMATION.class);
        if (!searchKeyword.equals( "" ) && searchKeyword != null){
            example.createCriteria().orLike("protocol", "%"+searchKeyword+"%").
                    orLike( "url", "%"+searchKeyword+"%" );
        }
        List<STREAMINFORMATION> streaminformations = MAPPER.selectByExample(example);
        if (CollectionUtils.isEmpty(streaminformations)) {
            return null;
        }
        PageInfo<STREAMINFORMATION> info = new PageInfo<>(streaminformations);
        PageResult<STREAMINFORMATION> pageResult = new PageResult<>( info.getTotal(), info.getPages(), info.getPageNum(), info.getPageSize(),streaminformations );
        return pageResult;
    }

    public STREAMINFORMATION GET_STREAMS_ADD(Integer device_id, String protocol, Integer type, String url) {
        STREAMINFORMATION streaminformation = new STREAMINFORMATION();
        streaminformation.setDevice_id(device_id);
        streaminformation.setProtocol(protocol);
        streaminformation.setUrl(url);
        streaminformation.setType(type);
        streaminformation.setStatus(UNIVERSAL_SERVICE.SOURCES_STREAM_STATUS);
        streaminformation.setSession_id(UNIVERSAL_SERVICE.GET_SOURCE_SESSION_ID());
        MAPPER.insert(streaminformation);
        return streaminformation;
    }

    public List<STREAMINFORMATION> GET_VIEW_STREAM_ONLY(String session_id){
        Example example = new Example(STREAMINFORMATION.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("session_id", session_id);
        List<STREAMINFORMATION> sTREAMINFORMATION = MAPPER.selectByExample(example);
        return sTREAMINFORMATION;

    }

    public STREAMINFORMATION UPDATE_VIEW_STREAM(String session_id, Integer type, String protocol, String url) {
        List<STREAMINFORMATION> streaminformations = GET_VIEW_STREAM_ONLY(session_id);
        STREAMINFORMATION streaminfo = null;
        for (STREAMINFORMATION streaminformation : streaminformations) {
            if (streaminformation.getStatus() == 2){
                return null;
            }
            streaminformation.setType(type);
            streaminformation.setProtocol(protocol);
            streaminformation.setUrl(url);
            MAPPER.updateByPrimaryKey(streaminformation);
            streaminfo = streaminformation;
            break;
        }
        return streaminfo;
    }

    public void GET_STREAMS_DELETE_ONLY(String session_id) {
        List<STREAMINFORMATION> streaminformations = GET_VIEW_STREAM_ONLY(session_id);
        for (STREAMINFORMATION streaminformation : streaminformations) {
            MAPPER.deleteByPrimaryKey(streaminformation.getId());
            break;
        }
    }
}
