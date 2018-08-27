package org.litespring.test.v5;

import org.junit.Test;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * @Author: wangjunjie 2018/8/8 15:54
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2018/8/8 15:54
 */
public class CGLibTest {

    @Test
    public void testCallBack() {

        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(PetStoreService.class);
        enhancer.setCallback(new TransactionInterceptor());
        PetStoreService petStore=(PetStoreService)enhancer.create();
        petStore.placeOrder();

    }

    public static class TransactionInterceptor implements MethodInterceptor {

        TransactionManager txManager=new TransactionManager();

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            txManager.start();
            Object result = methodProxy.invokeSuper(o, objects);
            txManager.commit();

            return result;
        }
    }

    @Test
    public void  testFilter() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PetStoreService.class);

        enhancer.setInterceptDuringConstruction(false);

        Callback[] callbacks = {new TransactionInterceptor(),NoOp.INSTANCE};

        Class<?>[] types = new Class<?>[callbacks.length];

        for (int x = 0; x < types.length; x++) {
            types[x] = callbacks[x].getClass();
        }

        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackTypes(types);
        enhancer.setCallbackFilter(new ProxyCallbackFilter());

        PetStoreService petStore = (PetStoreService)enhancer.create();
        petStore.placeOrder();
        System.out.println(petStore.toString());
    }

    private static class ProxyCallbackFilter implements CallbackFilter {

        public ProxyCallbackFilter() {

        }

        public int accept(Method method) {
            if(method.getName().startsWith("place")){
                return 0;
            } else{
                return 1;
            }

        }

    }
}
