package test;

import org.junit.Assert;
import org.junit.Test;

public class TestEquals {

    @Test
    public void test() {
        Transport transport1 = new Transport();
        Transport transport2 = transport1;
        Assert.assertTrue(transport1.equals(transport2));
    }
}
