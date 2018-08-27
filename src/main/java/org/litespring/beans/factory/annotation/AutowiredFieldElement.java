package org.litespring.beans.factory.annotation;

import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.AutowireCapableBeanFactory;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @Author: wangjunjie 2018/7/23 22:25
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2018/7/23 22:25
 */
public class AutowiredFieldElement extends InjectionElement {

    boolean required;

    public AutowiredFieldElement(Field f, boolean required, AutowireCapableBeanFactory factory) {
        super(f, factory);
        this.required=required;
    }

    public Field getField(){
        return (Field)this.member;
    }

    @Override
    public void inject(Object target) {
        Field field = this.getField();
        try {
            DependencyDescriptor dese = new DependencyDescriptor(field, this.required);
            Object value = factory.resolveDependency(dese);
            if (value!=null) {
                ReflectionUtils.makeAccessible(field);
                field.set(target,value);
            }
        }catch (Exception e) {
            throw new BeanCreationException("Could not autowire field: " + field, e);
        }
    }
}
