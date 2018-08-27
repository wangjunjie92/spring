package org.litespring.beans.factory.config;

import java.lang.reflect.Field;

/**
 * @Author: wangjunjie 2018/7/23 07:42
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2018/7/23 07:42
 */
public class DependencyDescriptor {
    private Field field;
    private boolean required;

    public DependencyDescriptor(Field field, boolean required) {
        this.field = field;
        this.required = required;
    }

    public Class<?> getDependencyType() {
        if (this.field!=null) {
            return field.getType();
        }
        throw new RuntimeException("only support field dependency");
    }

    public boolean isRequired() {
        return this.required;
    }
}
