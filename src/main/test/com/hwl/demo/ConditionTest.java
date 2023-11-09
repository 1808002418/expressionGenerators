package com.hwl.demo;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ConditionTest {

    public static void main(String[] args) {
        String source1 = "{\"logic\":\"MUST\",\"expressionEntities\":[{\"f1\":\"User_Type\",\"f2\":\"TERM\",\"f3\":\"00\"},{\"f1\":\"User_ID\",\"f2\":\"TERM\",\"f3\":\"123456\"}]}";
        String source2 = "{\"logic\":\"MUST\",\"expressionEntities\":[{\"logic\":\"SHOULD\",\"expressionEntities\":[{\"f1\":\"User_Type\",\"f2\":\"WILDCARD\",\"f3\":\"00\"},{\"f1\":\"User_ID\",\"f2\":\"TERM\",\"f3\":\"350321199612220717\"}]},{\"f1\":\"User_ID\",\"f2\":\"TERM\",\"f3\":\"350321199612220717\"}]}";
        JSONObject sourceObject = JSON.parseObject(source2);

        ConditionDTO dto = new ConditionDTO(
                null,
                null,
                null,
                0,
                10,
                DateUtil.parseDate("2020-10-01 00:00:00"),
                DateUtil.parseDate("2020-10-28 00:00:00"),
                sourceObject
        );
        System.out.println(dto.getSearchSourceBuilder());
//        System.out.println(dto.getQueryBuilder());
    }
}
