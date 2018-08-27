package org.litespring.beans.factory.annotation;

import java.util.List;

/**
 * @Author: wangjunjie 2018/7/24 16:57
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2018/7/24 16:57
 */
public class InjectionMetadata {

    private final Class<?>  targetClass;
    private List<InjectionElement> injectionElements;

    public InjectionMetadata(Class<?> targetClass, List<InjectionElement> injectionElements) {
        this.targetClass = targetClass;
        this.injectionElements = injectionElements;
    }

    public List<InjectionElement> getInjectionElements() {
        return injectionElements;
    }

    public void inject(Object target) {
        if (injectionElements == null || injectionElements.isEmpty()) {
            return;
        }

        for (InjectionElement ele: injectionElements) {
            ele.inject(target);
        }
    }
}
