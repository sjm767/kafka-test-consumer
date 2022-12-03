package com.jaeshim.test.consumer.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class BasicProducerTemplateConfiguration {
  @Value("${spring.kafka.producer.bootstrap-servers}")
  public String servers;

  @Value("${spring.kafka.producer.acks}")
  public String acks;

  private Map<String,Object> basicProducerFactoryConfig(){
    Map<String,Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,servers);
    configProps.put(ProducerConfig.ACKS_CONFIG,acks);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

    return configProps;
  }

  @Bean
  public ProducerFactory<String, String> basicProducerFactory(){
    return new DefaultKafkaProducerFactory<>(basicProducerFactoryConfig());
  }

  @Bean
  public KafkaTemplate<String,String> kafkaTemplate(){
    return new KafkaTemplate<>(basicProducerFactory());
  }
}
