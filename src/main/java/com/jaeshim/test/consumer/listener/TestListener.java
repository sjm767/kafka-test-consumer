package com.jaeshim.test.consumer.listener;

import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TestListener {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  private static final String SOURCE_TOPIC = "jaeshim-source-20220822";
  private static final String SINK_TOPIC="jaeshim-sink-20220822";

  static final Logger log = LoggerFactory.getLogger(TestListener.class);

  @KafkaListener
      (
          topics = SOURCE_TOPIC,
          groupId = "${spring.kafka.consumer.group-id}",
          concurrency = "1",
          containerFactory = "customContainerFactoryWithBatchBatch"
      )
  public void consume(ConsumerRecords<String, String> records) {

    String key;
    try {
      log.info("recordsCount = " + records.count());
      for (ConsumerRecord record : records) {
        key = record.key().toString();

        log.info("message info || partition: " + record.partition() + ", key: " +key +", offset: " + record.offset());
        send(SINK_TOPIC, record.partition(), String.valueOf(key),LocalDateTime.now().toString());

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void send(String topic, int partition, String key, String message) {
    kafkaTemplate.send(topic, partition,key,message);
  }
}
