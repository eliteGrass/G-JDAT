import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/24 13:11 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class Lesson026Test {
    private void log(String s) {
        System.out.println(Thread.currentThread().getName() + ":" + s);
    }

    /**
     * 1、测试 ThreadLocal 遇到线程池会怎么样？
     *
     * @throws InterruptedException
     */
    @Test
    public void threadLocalTest() throws InterruptedException {
        //1、创建一个ThreadLocal，用来存放用户名
        ThreadLocal<String> userNameTl = new ThreadLocal<>();
        //2、当前线程，即主线程中，放入用户名:路人
        userNameTl.set("路人");
        //3、在当前线程中从ThreadLocal获取用户名
        this.log(userNameTl.get());

        //4、创建大小为2的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //5、循环5次，通过线程池去执行任务，任务中去从 userNameTl 获取用户名，看看是否可以获取到？
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                String userName = userNameTl.get();
                this.log(userName);
            });
        }

        //关闭线程池，并等待结束
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }


    /**
     * 2、测试 InheritableThreadLocal 遇到线程池会怎么样？
     *
     * @throws InterruptedException
     */
    @Test
    public void InheritableThreadLocalTest() throws InterruptedException {
        //1、创建一个InheritableThreadLocal，用来存放用户名
        InheritableThreadLocal<String> userNameItl = new InheritableThreadLocal<>();

        //2、创建大小为2的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        //3、循环5次，通过线程池去执行任务，执行任务之前会生成一个用户名放入 userNameItl，然后在线程池中的任务中，将用户再取出来，看看和外面丢进去的是不是一样的？
        for (int i = 0; i < 5; i++) {
            //主线程中用户名，丢到userNameItl中
            String mainThreadUserName = "路人-" + i;
            userNameItl.set(mainThreadUserName);

            executorService.execute(() -> {
                //线程池中获取用户名
                String threadPoolThreadUserName = userNameItl.get();
                this.log(String.format("mainThreadUserName:" + mainThreadUserName + ",threadPoolThreadUserName:" + threadPoolThreadUserName));
            });

            TimeUnit.SECONDS.sleep(1);
        }

        //关闭线程池，并等待结束
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    /**
     * 3、自定义线程池解决这个问题
     */
    @Test
    public void threadLocalTestNew() throws InterruptedException {
        //1、创建一个ThreadLocal，用来存放用户名
        ThreadLocal<String> userNameTl = new ThreadLocal<>();

        //2、创建大小为2的线程池，大家先不用过度关注这块代码，稍后会解释
        ExecutorService executorService = new MyThreadPoolExecutor(new MyThreadPoolExecutor.ThreadLocalContext<String>() {
            @Override
            public String getContext() {
                return userNameTl.get();
            }

            @Override
            public void setContext(String userName) {
                userNameTl.set(userName);
            }
        }, 2, 2, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<>(100));

        //3、循环5次，通过线程池去执行任务，执行任务之前会生成一个用户名放入 userNameTl，然后在线程池中的任务中，将用户再取出来，看看和外面丢进去的是不是一样的？
        for (int i = 0; i < 5; i++) {
            //主线程中用户名，丢到userNameTl中
            String mainThreadUserName = "路人-" + i;
            userNameTl.set(mainThreadUserName);

            executorService.execute(() -> {
                //线程池中获取用户名
                String threadPoolThreadUserName = userNameTl.get();
                this.log(String.format("mainThreadUserName:" + mainThreadUserName + ",threadPoolThreadUserName:" + threadPoolThreadUserName));
            });

            TimeUnit.SECONDS.sleep(1);
        }

        //关闭线程池，并等待结束
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

}
