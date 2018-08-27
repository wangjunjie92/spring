package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * @Author: wangjunjie 2018/8/4 15:51
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2018/8/4 15:51
 */
public class AspectJBeforeAdvice extends AbstractAspectJAdvice  {

    public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod, pointcut, adviceObjectFactory);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        this.invokeAdviceMethod();
        Object o = mi.proceed();
        return o;
    }
}
