package test;

import org.junit.Test;
import test.pojo.Person;

import java.lang.reflect.Constructor;

/**
 * @Author: wangjunjie 2018/6/30 19:42
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2018/6/30 19:42
 */
public class ClassTest {

    @Test
    public void test1() throws Exception {
        Class<?> cl = Class.forName("test.pojo.Person");
        Constructor<?>[] cos = cl.getConstructors();
        for (Constructor<?> co : cos) {
            try {
                Person person = (Person) co.newInstance(25);
                System.out.println(person.getAge());
            }catch (Exception e) {
                continue;
            }


        }

        //Assert.assertTrue(bean instanceof Person);
    }
}
