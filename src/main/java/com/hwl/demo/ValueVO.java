package com.hwl.demo;

import cn.hutool.core.lang.Assert;
import lombok.Getter;

@Getter
public class ValueVO {
    private final String value;

    public ValueVO(String value) {
        Assert.notBlank(value, "值不能为空.");
        this.value = value;
    }

}
