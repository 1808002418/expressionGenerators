package com.hwl.demo;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RelationVO implements Visitor<QueryBuilder, ExpressionEntity> {
    private final RelationType value;

    public RelationVO(RelationType value) {
        this.value = value;
    }


    private static final Map<String, RelationVO> instances = new HashMap<>();

    public static RelationVO instance(String relation) {
        Assert.notNull(relation, "关系运算符不能为空.");
        if (instances.containsKey(relation)) {
            return instances.get(relation);
        }
        try {
            RelationType relationType = RelationType.valueOf(relation);
            RelationVO vo = new RelationVO(relationType);
            instances.put(relation, vo);
            return vo;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("关系运算符不存在.");
        }
    }

    @Override
    public QueryBuilder visit(ExpressionEntity expressionEntity) {
        return value.visit(expressionEntity);
    }


    public enum RelationType implements Visitor<QueryBuilder, ExpressionEntity> {
        TERM() {
            @Override
            public QueryBuilder visit(ExpressionEntity expressionEntity) {
                return QueryBuilders.termQuery(expressionEntity.getF1().getValue(), expressionEntity.getF3().getValue());
            }
        },
        WILDCARD() {
            @Override
            public QueryBuilder visit(ExpressionEntity expressionEntity) {
                return QueryBuilders.wildcardQuery(expressionEntity.getF1().getValue(), expressionEntity.getF3().getValue());
            }
        };

    }
}
