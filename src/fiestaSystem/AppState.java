package fiestaSystem;

import fiestaSystem.users.Admin;
import fiestaSystem.users.Agent;
import fiestaSystem.users.Customer;
import java.util.HashMap;

public class AppState {
    public static Admin    admin           = new Admin("A01", "Admin");
    public static Agent    agent           = new Agent("AG01", "Agent");
    public static Customer currentCustomer = null;

    /** Persistent customer registry keyed by lowercased name. */
    public static HashMap<String, Customer> customers = new HashMap<>();

    /**
     * Returns the existing Customer with this name, or creates and registers a new one.
     * Match is case-insensitive so "Juan" and "juan" resolve to the same account.
     */
    public static Customer getOrCreateCustomer(String name) {
        String key = name.trim().toLowerCase();
        Customer c = customers.get(key);
        if (c == null) {
            c = new Customer("C-" + (customers.size() + 1), name.trim());
            customers.put(key, c);
        }
        return c;
    }
}