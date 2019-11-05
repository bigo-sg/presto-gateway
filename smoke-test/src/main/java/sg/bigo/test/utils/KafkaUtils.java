package sg.bigo.test.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tangyun@bigo.sg
 * @date 11/4/19 6:18 PM
 */
public class KafkaUtils implements Runnable {
  private final KafkaConsumer<String, String> consumer;
  private ConsumerRecords<String, String> msgList;
  private final String topic;
  private static final String GROUPID = "test-gateway";
  private ExecutorService executor = Executors.newCachedThreadPool();

  public KafkaUtils(String topicName) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", GROUPID);
    props.put("enable.auto.commit", "true");
    props.put("auto.commit.interval.ms", "1000");
    props.put("session.timeout.ms", "30000");
    props.put("auto.offset.reset", "earliest");
    props.put("key.deserializer", StringDeserializer.class.getName());
    props.put("value.deserializer", StringDeserializer.class.getName());
    this.consumer = new KafkaConsumer<>(props);
    this.topic = topicName;
    this.consumer.subscribe(Arrays.asList(topic));
  }

  @Override
  public void run() {
    int messageNo = 1;
    try {
      for (;;) {
        msgList = consumer.poll(1000);
        if(null!=msgList&&msgList.count()>0){
          for (ConsumerRecord<String, String> record : msgList) {

            System.out.println(messageNo+"=======receive: key = " + record.key() + "," +
                " value = " + record.value()+" offset==="+record.offset());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = null;
            try {
              jsonNode = mapper.readTree(record.value());
            } catch (IOException e) {
              e.printStackTrace();
            }
            String query = jsonNode.get("query").asText();
            if (!query.startsWith("select") && !query.startsWith("SELECT")) {
              continue;
            }
            String user = jsonNode.get("user").asText();
            String syntax = jsonNode.get("syntax").asText();
            Properties properties = new Properties();
            properties.setProperty("query_max_execution_time", "5m");
            executor.submit(new Runnable() {
              @Override
              public void run() {
                try {
                  new ConnectionUtil(user)
                      .execute(query, syntax.equals("hive")?true:false, properties);
                } catch (SQLException e) {
                  e.printStackTrace();
                } catch (ClassNotFoundException e) {
                  e.printStackTrace();
                }
              }
            });
            if(messageNo%1000==0){
              break;
            }
            messageNo++;
          }
        }else{
          Thread.sleep(1000);
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      consumer.close();
    }
  }
  public static void main(String args[]) {
    KafkaUtils test1 = new KafkaUtils("presto_job_audit");
    Thread thread1 = new Thread(test1);
    thread1.start();
  }
}
