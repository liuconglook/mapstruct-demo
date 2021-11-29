package com.belean.entity;

/**
 * 地址
 * @author belean
 * @date 2021/11/27
 */
public class Address {
    private String name;

    public Address(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
