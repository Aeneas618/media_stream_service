package com.fokantech.media_stream_service.service;

import com.fokantech.media_stream_service.configuration.CONFIGPARAM;
import com.fokantech.media_stream_service.mapper.CONVERTERSTREAM_MAPPER;
import com.fokantech.media_stream_service.model.CONVERTERSTREAM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class CONVERTERSTREAM_SERVICE {
    @Autowired
    private CONVERTERSTREAM_MAPPER MAPPER;
    @Autowired
    private UNIVERSAL_SERVICE UNIVERSAL_SERVICE;

    public CONVERTERSTREAM GET_CONVERTER_STREAM_ADD(String source_session_id, String protocol, String url) {
        CONVERTERSTREAM converterstream = new CONVERTERSTREAM();
        converterstream.setSource_session_id(source_session_id);
        converterstream.setProtocol(protocol);
        converterstream.setUrl(url);
        converterstream.setStatus(UNIVERSAL_SERVICE.CONVERTER_STREAM_STATUS);
        converterstream.setSession_id(UNIVERSAL_SERVICE.GET_CONVERTER_SESSION_ID());
        MAPPER.insert(converterstream);
        return converterstream;
    }

    public CONVERTERSTREAM UPDATE_CONVERTER_STREAM(String session_id, String url) {
        List<CONVERTERSTREAM> converterstreams = GET_CONVERTER_STREAM_VIEW_ONLY(session_id);
        CONVERTERSTREAM converter = null;
        for (CONVERTERSTREAM converterstream : converterstreams) {
            converterstream.setUrl(url);
            MAPPER.updateByPrimaryKey(converterstream);
            converter = converterstream;
        }
        return converter;
    }

    public List<CONVERTERSTREAM> GET_CONVERTER_STREAM_VIEW_ONLY(String session_id) {
        Example example = new Example(CONVERTERSTREAM.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("session_id", session_id);
        List<CONVERTERSTREAM> converters = MAPPER .selectByExample(example);
        return converters;
    }

    public List<CONVERTERSTREAM> GET_CONVERTER_STREAM_ALL_INFO() {
        return MAPPER.selectAll();
    }

    /**
     *  狀態爲0可忽略
     * @param session_id
     */
    public void GET_CONVERTER_SESSION_ID_DELETE(String session_id) {
        Example example = new Example(CONVERTERSTREAM.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("session_id", session_id);
        List<CONVERTERSTREAM> lists = MAPPER.selectByExample( example );
        for (CONVERTERSTREAM list : lists) {
            MAPPER.deleteByPrimaryKey(list.getId());
        }
    }

    public void GET_CONVERTER_STREAM_UPDATE_STATUS(String session_id, Integer index) {
        Example example = new Example(CONVERTERSTREAM.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("session_id", session_id);
        List<CONVERTERSTREAM> converters = MAPPER .selectByExample(example);
        for (CONVERTERSTREAM converter : converters) {
            converter.setStatus(index);
            MAPPER.updateByPrimaryKey( converter );
        }

    }
}
