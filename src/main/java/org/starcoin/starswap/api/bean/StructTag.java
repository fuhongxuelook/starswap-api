package org.starcoin.starswap.api.bean;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StructTag {

    @Column
    private String address;

    @Column
    private String module;

    @Column
    private String name;
    // private java.util.List<TypeTag> type_params;

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

    @Override
    public String toString() {
        return "StructTag{" +
                "address='" + address + '\'' +
                ", module='" + module + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
