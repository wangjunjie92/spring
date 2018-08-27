package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.context.support.FileSystemXmlApplicationContext;
import org.litespring.service.v1.PetStoreService;

public class ApplicationContextTest {

    @Test
    public void testGetBean() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("petstore-v1.xml");
        PetStoreService petStore = (PetStoreService) applicationContext.getBean("petStore");
        Assert.assertNotNull(petStore);
    }

    @Test
    public void testGetBeanFromFileSystemContext() {
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("/Users/wangjunjie/Documents/java/code/student/lite_spring/src/test/resources/petstore-v1.xml");
        PetStoreService petStore = (PetStoreService) applicationContext.getBean("petStore");
        Assert.assertNotNull(petStore);
    }
}
