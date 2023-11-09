package com.hwl.demo;

public interface Visitor<R,T> {
    R visit(T arg);
}
