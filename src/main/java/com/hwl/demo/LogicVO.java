package com.hwl.demo;

import cn.hutool.core.lang.Assert;
import lombok.Getter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class LogicVO implements Visitor<QueryBuilder, ComplexEntity> {
    private static final Map<String, LogicVO> instances = new HashMap<>();

    private final LogicType value;


    private LogicVO(LogicType value) {
        this.value = value;
    }

    public static LogicVO instance(String logic) {
        Assert.notNull(logic, "逻辑运算符不能为空.");
        if (instances.containsKey(logic)) {
            return instances.get(logic);
        }
        try {
            LogicType logicType = LogicType.valueOf(logic);
            LogicVO vo = new LogicVO(logicType);
            instances.put(logic, vo);
            return vo;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("逻辑运算符不存在.");
        }
    }

    @Override
    public QueryBuilder visit(ComplexEntity complexEntity) {
        return value.visit(complexEntity);
    }


    public enum LogicType implements Visitor<QueryBuilder, ComplexEntity> {
        MUST() {
            @Override
            public QueryBuilder visit(ComplexEntity complexEntity) {
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                List<ExpressionEntity> expressionEntities = complexEntity.getExpressionEntities();
                for (ExpressionEntity expressionEntity : expressionEntities) {
                    boolQuery.must(
                            expressionEntity.visit(null)
                    );
                }
                return boolQuery;
            }
        },
        SHOULD() {
            @Override
            public QueryBuilder visit(ComplexEntity complexEntity) {
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                List<ExpressionEntity> expressionEntities = complexEntity.getExpressionEntities();
                for (ExpressionEntity expressionEntity : expressionEntities) {
                    boolQuery.should(
                            expressionEntity.visit(null)
                    );
                }
                return boolQuery;
            }
        },
        NULL() {
            @Override
            public QueryBuilder visit(ComplexEntity complexEntity) {
                return complexEntity.getExpressionEntities().get(0).visit(null);
            }
        };

    }
}
