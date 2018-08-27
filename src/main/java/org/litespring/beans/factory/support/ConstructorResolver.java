package org.litespring.beans.factory.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @Author: wangjunjie 2018/6/30 20:47
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2018/6/30 20:47
 */
public class ConstructorResolver {

    protected final Log logger = LogFactory.getLog(getClass());

    private final AbstractBeanFactory beanFactory;

    public ConstructorResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object autowireConstructor(final BeanDefinition bd) {
        Constructor<?> constructorToUse=null;
        Object[] argsToUse = null;

        Class<?> beanClass = null;
        try {
            beanClass=this.beanFactory.getBeanClassLoader().loadClass(bd.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException( bd.getID(), "Instantiation of bean failed, can't resolve class", e);
        }

        Constructor<?>[] candidates = beanClass.getConstructors();
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory);
        ConstructorArgument cargs = bd.getConstructorArgument();
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();

        for (int i=0;i<candidates.length;++i) {
            Class<?>[] parameterTypes = candidates[i].getParameterTypes();
            if (parameterTypes.length!=cargs.getArgumentValues().size()) {
                continue;
            }
            argsToUse=new Object[parameterTypes.length];
            boolean result = valuesMatchTypes(parameterTypes, cargs.getArgumentValues(), argsToUse, valueResolver,
                    typeConverter);
            if (result) {
                constructorToUse=candidates[i];
                break;
            }
        }

        //找不到一个合适的构造函数
        if(constructorToUse == null){
            throw new BeanCreationException( bd.getID(), "can't find a apporiate constructor");
        }

        try {
            return constructorToUse.newInstance(argsToUse);
        }catch (Exception e) {
            throw new BeanCreationException( bd.getID(), "can't find a create instance using "+constructorToUse);
        }

    }

    private boolean valuesMatchTypes(Class<?> [] parameterTypes,
                                     List<ConstructorArgument.ValueHolder> valueHolders,
                                     Object[] argsToUse,
                                     BeanDefinitionValueResolver valueResolver,
                                     SimpleTypeConverter typeConverter ) {

        for (int i=0;i<parameterTypes.length;++i) {
            ConstructorArgument.ValueHolder valueHolder = valueHolders.get(i);
            Object value = valueHolder.getValue();
            
            try {
                Object resolveValue = valueResolver.resolveValueIfNecessary(value);
                Object convertedValue = typeConverter.convertIfNecessary(resolveValue, parameterTypes[i]);
                argsToUse[i] =convertedValue;
            }catch (Exception e) {
                return false;
            }
            
        }
        
        return true;

    }
}
