package com.hwl.demo;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ComplexEntity implements Visitor<QueryBuilder, Void> {
    private final LogicVO logic;

    private final List<ExpressionEntity> expressionEntities;

    protected <T extends ComplexEntity> ComplexEntity(T ignore) {
        this.logic = null;
        this.expressionEntities = null;
    }

    public ComplexEntity(LogicVO logic, List<ExpressionEntity> expressionEntities) {
        Assert.notNull(logic, "逻辑关系不能为空.");
        Assert.notEmpty(expressionEntities, "表达式列表不能为空");
        this.logic = logic;
        this.expressionEntities = expressionEntities;
    }

    @Override
    public QueryBuilder visit(Void arg) {
        return logic.visit(this);
    }

    public static ComplexEntity instance(JSONObject original) {
        Assert.notNull(original, "构造数据不能为空");
        if (original.containsKey("logic")) {
            LogicVO logic = LogicVO.instance(original.getString("logic"));
            List<ExpressionEntity> expressionEntities = new ArrayList<>();
            JSONArray jsonArray = original.getJSONArray("expressionEntities");
            for (int i = 0; i < jsonArray.size(); i++) {
                expressionEntities.add(
                        ExpressionEntity.instance(jsonArray.getJSONObject(i))
                );
            }
            return new ComplexEntity(logic, expressionEntities);
        }

        throw new RuntimeException("未知类型错误");
    }
}
