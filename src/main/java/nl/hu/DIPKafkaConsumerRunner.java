package nl.hu;

/**
 * Created by roelant on 19/12/2019.
 * Updated on 14/5/2020 for DIP.  
 */
public class DIPKafkaConsumerRunner {
    public static final String TOPIC = "transactie";

    public static void main(String[] args) {
        boolean isAsync = false;
        DIPKafkaConsumer consumerThread = new DIPKafkaConsumer(TOPIC, isAsync);
        // start the producer
        consumerThread.start();
    }
}
