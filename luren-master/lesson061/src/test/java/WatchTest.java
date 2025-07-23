import java.util.concurrent.TimeUnit;

public class WatchTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("程序已启动");
        for (int i = 1; i < 1000000; i++) {
            add(i, i + 1);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public static int add(int a, int b) {
        return a + b;
    }
}
