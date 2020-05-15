package nl.hu;

/**
 * Created by roelant on 19/12/2019.
 */
public class Transaction {
    public String getCustomerId() {
        return customerId;
    }

    String customerId;
    int userId;


    public Transaction(String customerId, int userId) {
        this.customerId = customerId;
        this.userId = userId;
    }


    public int getUserId() {
        return userId;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "customerId=" + customerId +
                ", userId=" + userId +
                '}';
    }
}
