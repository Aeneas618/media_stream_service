package com.fokantech.media_stream_service.configuration;


import lombok.Data;

import java.util.List;
@Data
public class PageResult<T> {

    private Long totalCount;// 总条数
    private Integer pageCount;// 总页数
    private Integer currentPage;//当前页码
    private Integer perPage; //当前记录数
    private List<T> items;// 当前页数据

    public PageResult(Long totalCount, Integer pageCount, Integer currentPage, Integer perPage, List<T> items) {
        this.totalCount = totalCount;
        this.pageCount = pageCount;
        this.currentPage = currentPage;
        this.perPage = perPage;
        this.items = items;
    }

    public PageResult(){

    }
}