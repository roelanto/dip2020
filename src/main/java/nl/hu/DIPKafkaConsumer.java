package nl.hu;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Hello world!
 *
 */
public class DIPKafkaConsumer extends Thread {
    private static Logger log = LoggerFactory.getLogger("DIPKafkaConsumer");
    private final String topic;
    private final Boolean isAsync;
    private final KafkaConsumer<String, String> kafkaConsumer;
    private static final String KAFKA_SERVER_URL = "localhost";
    private static final int KAFKA_SERVER_PORT = 9092;
    private static final String CLIENT_ID = "DIPKafkaProducer";
    private static final Transactions transactions = new Transactions();

    public DIPKafkaConsumer(String topic, boolean isAsync) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", KAFKA_SERVER_URL + ":" + KAFKA_SERVER_PORT);
        properties.put("client.id", CLIENT_ID);
        properties.put("group.id", "mygroup");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer = new KafkaConsumer <String, String>(properties);
        this.topic = topic;
        this.isAsync = isAsync;
    }

    /**
     * This method will be run, because this Java file extends the Thread class.
     */
    public void run() {
        Set topics = new HashSet<String>();
        /* first subscribe to the topic */
        topics.add(topic);
        kafkaConsumer.subscribe(topics);

        pollForNewRecords(kafkaConsumer);

    }


    private int resolve_query(Transaction transaction) {
        String customerId = transaction.getCustomerId();

        Set<Integer> users = transactions.usersFromCompany(customerId);
        return users.size();
    }

    /**
     * This method runs a poll job. It continuously asks for new data from Kafka.
     * @param consumer
     */
    private void pollForNewRecords(KafkaConsumer consumer) {
        int threshold = 4;
        try {
            while (true) {
                // for Java versions 1.8 and higher, use Duration.
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records)
                {
                    log.info("topic = "+record.topic() + " partition = "+record.partition()+", offset = %d, userid = "+record.key()+", companyid = "+record.value()+"\n");
                    Transaction t = new Transaction(record.value(), Integer.parseInt(record.key()));
                    // add transaction to the list of known transactions
                    transactions.add(t);
                    // run query
                    log.info("# of users for company" + t.getCustomerId() + ": " + resolve_query(t));

                    int updatedCount = 1;
                }
            }
        } finally {
            consumer.close();
        }
    }


}
