package sg.bigo.test;

import sg.bigo.test.utils.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author tangyun@bigo.sg
 * @date 11/4/19 10:24 AM
 */
public class QueryGenerator {

  static Random random = new Random(System.currentTimeMillis());
  public  static void main(String[] args) {

    ExecutorService executor = Executors.newCachedThreadPool();
    List<Future<String>> futures = new ArrayList<>();
    String sql = "select count(distinct uid) from algo.rec_like_event_orc where day='2019-10-10'";
    Properties properties = new Properties();

    for (int i = 0; i < 10; ++i) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      int finalI = i;
      Future<String> future = executor.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
          return new ConnectionUtil("presto_test" + random.nextInt(1000)).execute(sql, true, properties);
        }
      });
      futures.add(future);
    }
    for (Future<String> future: futures) {
      try {
        String re = future.get();
        System.out.println(re);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
  }
}
