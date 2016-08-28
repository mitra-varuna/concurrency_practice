import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class ProducerConsumerTest {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedTransferQueue<>();
        Thread producerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        queue.put(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        producerThread.setName("Producer");
        producerThread.start();


        Thread consumerThread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        consumerThread.setName("Consumer");
        consumerThread.start();

    }

}
