package org.starcoin.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StructTag {
    /*
          "type_tag": {
          "Struct": {
            "address": "0x00000000000000000000000000000001",
            "module": "Account",
            "name": "DepositEvent",
            "type_params": []
          }
        },
     */

    String address;
    String module;
    String name;

    @JsonProperty("type_params")
    List<Object> typeParams; // Is ok???

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getTypeParams() {
        return typeParams;
    }

    public void setTypeParams(List<Object> typeParams) {
        this.typeParams = typeParams;
    }

    @Override
    public String toString() {
        return "StructTag{" +
                "address='" + address + '\'' +
                ", module='" + module + '\'' +
                ", name='" + name + '\'' +
                ", typeParams=" + typeParams +
                '}';
    }
}
