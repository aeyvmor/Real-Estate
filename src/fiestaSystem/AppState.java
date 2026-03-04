package fiestaSystem;

import fiestaSystem.users.Admin;
import fiestaSystem.users.Agent;
import fiestaSystem.users.Customer;

public class AppState {
    public static Admin admin = new Admin("A01", "Admin");
    public static Agent agent = new Agent("AG01", "Agent");
    public static Customer currentCustomer = null;
}