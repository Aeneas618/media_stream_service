package com.fokantech.media_stream_service.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@Table(name = "converter_stream")
public class CONVERTERSTREAM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String protocol;
    private String url;
    private String session_id;
    private String source_session_id;
    private Integer status;
}
