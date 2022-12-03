package com.jaeshim.test.consumer.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class TestListenerContainerConfiguration {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String servers;

    @Value("${spring.kafka.consumer.max-poll-records}")
    private Integer maxPollRecords;

    @Value("${spring.kafka.consumer.max-poll-interval-ms}")
    private Integer maxPollIntervalMs;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.client-id}")
    private String clientId;

    @Value("${spring.kafka.consumer.session-timeout-ms}")
    private Integer sessionTimeoutMs;

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> customContainerFactoryWithBatchBatch() {
        Map<String, Object> prop = new HashMap<>();

        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        prop.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        prop.put(ConsumerConfig.CLIENT_ID_CONFIG,clientId);
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,autoOffsetReset);
        prop.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,sessionTimeoutMs);
        prop.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());
//        prop.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "instance1");

        DefaultKafkaConsumerFactory cf = new DefaultKafkaConsumerFactory<>(prop);

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        factory.setConsumerFactory(cf);

        return factory;

    }
}
