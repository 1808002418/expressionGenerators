package com.hwl.demo;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ExpressionEntity extends ComplexEntity implements Visitor<QueryBuilder, Void>, Cloneable {
    //    左值
    private final ValueVO f1;
    //    关系运算符
    private final RelationVO f2;
    //    右值
    private final ValueVO f3;

    private ExpressionEntity(LogicVO logic, List<ExpressionEntity> expressionEntities) {
        super(logic, expressionEntities);
        this.f1 = null;
        this.f2 = null;
        this.f3 = null;
    }

    private ExpressionEntity(ValueVO f1, RelationVO f2, ValueVO f3) {
        super(null);
        Assert.notNull(f1, "左值不能为空");
        Assert.notNull(f2, "运算符开始时间不能为空");
        Assert.notNull(f3, "右值开始时间不能为空");
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
    }

    @Override
    public QueryBuilder visit(Void arg) {
        if (f2 != null) {
            return f2.visit(this);
        }

        if (getLogic() != null) {
            return getLogic().visit(this);
        }
        throw new RuntimeException("未知类型错误.");
    }

    public static ExpressionEntity instance(JSONObject original) {
        Assert.notNull(original, "构造数据不能为空");
        if (original.containsKey("logic")) {
            LogicVO logic = LogicVO.instance(original.getString("logic"));
            List<ExpressionEntity> expressionEntities = new ArrayList<>();
            JSONArray jsonArray = original.getJSONArray("expressionEntities");
            for (int i = 0; i < jsonArray.size(); i++) {
                expressionEntities.add(
                        instance(jsonArray.getJSONObject(i))
                );
            }
            return new ExpressionEntity(logic, expressionEntities);
        }

        if (original.containsKey("f1")) {
            return new ExpressionEntity(
                    new ValueVO(original.getString("f1")),
                    RelationVO.instance(original.getString("f2")),
                    new ValueVO(original.getString("f3"))
            );
        }
        throw new RuntimeException("未知类型错误");
    }
}
