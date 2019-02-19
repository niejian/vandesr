package cn.com.vandesr.backend.config.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author: nj
 * @date: 2019/2/19:下午3:06
 */
public class ThreadTest {
    static class CallableThread implements Callable<String> {
        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public String call() throws Exception {
            System.out.println("call able Running....");
            return "success";
        }
    }


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> futureTask = executorService.submit(new CallableThread());

        try {
            // 执行线程
            String result = futureTask.get();
            System.out.println("---->执行结果：" + result);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }

    }
}
