package com.hwl.demo;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
//import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ConditionDTO {

    private static final int PAGE_NO = 0;
    private static final int PAGE_SIZE = 10;
    private static final SortOrder ASC = SortOrder.ASC;
    private static final SortOrder DESC = SortOrder.DESC;
    private static final String ORDER_FIELD = "Operate_Time";
    private static final String INDEX_PREFIX = "log_audit_";

    private String indexPrefix;
    private String orderField;
    private SortOrder orderDirection;
    private int pageNo;
    private int pageSize;
    // 搜索时间范围
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    // 搜索时间范围
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private JSONObject original;
    private ConditionEntity conditionEntity;

    public ConditionDTO(String indexPrefix, String orderField, SortOrder orderDirection, int pageNo, int pageSize, Date startDate, Date endDate, JSONObject original) {
        Assert.notNull(original, "条件构造数据不能为空");
        Assert.notNull(startDate, "开始时间不能为空");
        Assert.notNull(endDate, "结束时间不能为空");
        this.indexPrefix=indexPrefix;
        this.orderField = orderField;
        this.orderDirection = orderDirection;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.startDate = startDate;
        this.endDate = endDate;
        this.original = original;
    }

    public String getIndexPrefix() {
        if (StringUtils.isBlank(indexPrefix)){
            indexPrefix=INDEX_PREFIX;
        }
        return indexPrefix;
    }

    public String getOrderField() {
        if (StringUtils.isBlank(orderField)) {
            orderField = ORDER_FIELD;
        }
        return orderField;
    }

    public SortOrder getOrderDirection() {
        if (orderDirection == null) {
            orderDirection = DESC;
        }
        return orderDirection;
    }

    public int getPageNo() {
        if (pageNo < 0) {
            pageNo = PAGE_NO;
        }
        return pageNo;
    }

    public int getPageSize() {
        if (pageSize <= 0) {
            pageSize = PAGE_SIZE;
        }
        return pageSize;
    }

    public ConditionEntity getConditionEntity() {
        if (conditionEntity == null) {
            conditionEntity = new ConditionEntity(
                    getStartDate(),
                    getEndDate(),
                    ComplexEntity.instance(original)
            );
        }
        return conditionEntity;
    }

    public SearchSourceBuilder getSearchSourceBuilder() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder
                .query(getQueryBuilder())
                .from(getPageNo())
                .size(getPageSize())
                .sort(SortBuilders.fieldSort(getOrderField()).order(getOrderDirection()));
        return sourceBuilder;
    }

    public QueryBuilder getQueryBuilder() {
        return getConditionEntity().visit(null);
    }

}
