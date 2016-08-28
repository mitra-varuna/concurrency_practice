import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadersWritersTest {
    public static void main(String[] args) {
         class ProtectedResource {
             private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
             private Lock readLock = readWriteLock.readLock();
             private Lock writeLock = readWriteLock.writeLock();
             private Map<String, String> map = new HashMap<>();

             public String get(String key) {
                 try{
                     readLock.lock();
                     return map.get(key);
                 }finally {
                     readLock.unlock();
                 }

             }

             public void set(String key, String val) {
                 try{
                     writeLock.lock();
                     map.put(key, val);
                     System.out.printf("Set %s %s \n", key, val);

                 }finally {
                     writeLock.unlock();
                 }
             }

        }

        ProtectedResource resource = new ProtectedResource();

        for (int i = 0; i < 100; i++) {
            final int x = i;
            Thread setThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    resource.set(String.valueOf(x), String.valueOf(x));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            setThread.setName(String.format("Set Thread %s", i));
            setThread.start();
        }

        for (int i = 0; i < 100; i++) {
            final int x = i;
            Thread getThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String val = resource.get(String.valueOf(x));
                    System.out.println(val);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            getThread.setName(String.format("Get Thread %s", x));
            getThread.start();
        }
    }
}
