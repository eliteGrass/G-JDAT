/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/1 19:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class DeadlockDemo {
    // 创建两个共享资源
    private static final String resource1 = new String();
    private static final Object resource2 = new Object();

    public static void main(String[] args) {
        /**
         * 两个线程 thread1 thread2
         * thread1对resource1上锁，然后尝试获取resource2的锁；
         * thread2对resource2上锁，然后尝试获取resource1的锁；
         * 2个线程会相互等待，出现死锁
         */
        Thread thread1 = new Thread(() -> {
            synchronized (resource1) {
                System.out.println("Thread 1: Locked resource 1");

                try {
                    Thread.sleep(100); // 假设这里有其他操作，需要一些时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Thread 1: Attempting to lock resource 2");

                //尝试索取resource2的锁
                synchronized (resource2) {
                    System.out.println("Thread 1: Locked resource 2");
                }
            }
        }, "thread1");

        Thread thread2 = new Thread(() -> {
            synchronized (resource2) {
                System.out.println("Thread 2: Locked resource 2");

                try {
                    Thread.sleep(100); // 假设这里有其他操作，需要一些时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //尝试索取resource1的锁
                System.out.println("Thread 2: Attempting to lock resource 1");
                synchronized (resource1) {
                    System.out.println("Thread 2: Locked resource 1");
                }
            }
        }, "thread2");

        // 启动线程
        thread1.start();
        thread2.start();
    }
}