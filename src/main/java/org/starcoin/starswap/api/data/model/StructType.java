package org.starcoin.starswap.api.data.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StructType {

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String module;

    @Column(nullable = false)
    private String name;

    // private java.util.List<TypeTag> type_params;

    public StructType() {
    }

    public StructType(String address, String module, String name) {
        this.address = address;
        this.module = module;
        this.name = name;
    }

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
        return "StructType{" +
                "address='" + address + '\'' +
                ", module='" + module + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
