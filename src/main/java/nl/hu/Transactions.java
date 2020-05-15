package nl.hu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by roelant on 19/12/2019.
 */
public class Transactions {
    private static Logger log = LoggerFactory.getLogger("Transactions");

    private Map<String, Set<Integer>> transactionsByCompany = new HashMap<String, Set<Integer>>();

    public Transactions() {
        this.transactionsByCompany = new HashMap<String, Set<Integer>>();
    }

    protected void add(Transaction t) {
        log.info("Adding transaction %s"+ t);
        if (!transactionsByCompany.containsKey(t.getCustomerId())) {
            transactionsByCompany.put(t.getCustomerId(), new HashSet<Integer>());
        }
        ((Set) transactionsByCompany.get(t.getCustomerId())).add(t.userId);

    }

    /**
     * returns all users from a company (customer).
     * @param customerId
     * @return set of users.
     */
    protected Set<Integer> usersFromCompany(String customerId) {
        // log.info("Looking up customers that have bought " + userId);
        return  transactionsByCompany.get(customerId);
    }
}
