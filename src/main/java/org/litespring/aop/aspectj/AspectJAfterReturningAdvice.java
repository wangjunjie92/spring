package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice {
	
	public AspectJAfterReturningAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory){
		super(adviceMethod,pointcut,adviceObjectFactory);
	}
	
	public Object invoke(MethodInvocation mi) throws Throwable {
		Object o = mi.proceed();
		//例如：调用TransactionManager的commit方法
		this.invokeAdviceMethod();
		return o;
	}

}