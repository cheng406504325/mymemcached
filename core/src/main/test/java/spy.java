import net.spy.memcached.MemcachedClient;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * @author suncheng
 * @version 0.0.1
 * @since 15/12/1
 */
public class spy {

    @Test
    public void testConn() {
        try {
            MemcachedClient mc = new MemcachedClient(
                    new InetSocketAddress("192.168.1.106", 11211));
            mc.set("user", 0, "suncheng");
            System.out.println(mc.get("user"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnMy() {
        try {
            MemcachedClient mc = new MemcachedClient(
                    new InetSocketAddress("localhost", 12121));
            mc.set("user", 0, "suncheng");
            System.out.println(mc.get("user"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnMy2() {
        try {
            Module module = new Module();
            module.setName("suncheng");
            module.setAge(25);
            List<String> stus = new LinkedList<>();
            stus.add("1");
            stus.add("2");
            module.setStus(stus);
            MemcachedClient mc = new MemcachedClient(
                    new InetSocketAddress("localhost", 12121));
            mc.set("user", 0, module);
            System.out.println(mc.get("user"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConn2() {
        try {
            Module module = new Module();
            module.setName("suncheng");
            module.setAge(25);
            List<String> stus = new LinkedList<>();
            stus.add("1");
            stus.add("2");
            module.setStus(stus);
            MemcachedClient mc = new MemcachedClient(
                    new InetSocketAddress("192.168.1.106", 11211));
            mc.set("user", 0, module);
            System.out.println(mc.get("user"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
