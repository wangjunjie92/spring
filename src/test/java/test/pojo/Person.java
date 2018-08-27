package test.pojo;

/**
 * @Author: wangjunjie 2018/6/30 19:44
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2018/6/30 19:44
 */
public class Person {

    private int age;

    private String name;

    private int sex;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String show() {
        return "my name is wangjunjie,you know";
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(int age, String name, int sex) {
        this.age = age;
        this.name = name;
        this.sex = sex;
    }

    public Person() {
    }

    public Person(int age) {
        this.age=age;
    }
}
