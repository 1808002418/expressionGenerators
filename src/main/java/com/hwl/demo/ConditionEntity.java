package com.hwl.demo;

import cn.hutool.core.lang.Assert;
import lombok.Getter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Date;

@Getter
public class ConditionEntity implements Visitor<QueryBuilder, Void> {
    private static final String DATE_FILTER_FIELD = "Operate_Time";

    private final Date startDate;
    private final Date endDate;
    private final ComplexEntity complexEntity;

    public ConditionEntity(Date startDate, Date endDate, ComplexEntity complexEntity) {
        Assert.notNull(startDate, "查询开始时间不能为空");
        Assert.notNull(endDate, "查询结束时间不能为空");

        this.startDate = startDate;
        this.endDate = endDate;

        this.complexEntity = complexEntity;
    }

    @Override
    public QueryBuilder visit(Void arg) {
        QueryBuilder queryBuilder;
        if (complexEntity != null) {
            queryBuilder = complexEntity.visit(null);
        } else {
            queryBuilder = null;
        }
        return combinedDate(queryBuilder);
    }

    private QueryBuilder combinedDate(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            return QueryBuilders.rangeQuery(DATE_FILTER_FIELD)
                    .gte(startDate.getTime())
                    .lte(endDate.getTime());
        } else {
            // 减少嵌套层级
            if (queryBuilder instanceof BoolQueryBuilder && ((BoolQueryBuilder) queryBuilder).should().isEmpty()){
                return ((BoolQueryBuilder) queryBuilder).must(combinedDate(null));
            }else {
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                return boolQuery
                        .must(queryBuilder)
                        .must(combinedDate(null));
            }
        }
    }

}
