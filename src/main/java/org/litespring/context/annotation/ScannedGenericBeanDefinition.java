package org.litespring.context.annotation;

import org.litespring.beans.factory.annotation.AnnotatedBeanDefinition;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.core.type.AnnotationMetadata;

/**
 * @Author: wangjunjie 2018/7/15 21:19
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2018/7/15 21:19
 */
public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {

    private final AnnotationMetadata metadata;

    public ScannedGenericBeanDefinition( AnnotationMetadata metadata) {
        super();
        this.metadata = metadata;

        setBeanClassName(this.metadata.getClassName());
    }

    @Override
    public final AnnotationMetadata getMetadata() {
        return this.metadata;
    }
}

