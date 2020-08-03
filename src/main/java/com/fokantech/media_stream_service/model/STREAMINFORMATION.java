package com.fokantech.media_stream_service.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "source_stream")
@Data
public class STREAMINFORMATION {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer device_id;
    private Integer type;
    private String protocol;
    private String url;
    private Integer status;
    private String session_id;

}
