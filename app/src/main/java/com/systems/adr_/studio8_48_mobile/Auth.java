package com.systems.adr_.studio8_48_mobile;

/**
 * Created by luis_ on 29/04/2017.
 */

class Auth {

    private Client client;

    private static final Auth ourInstance = new Auth();

    static Auth getInstance() {
        return ourInstance;
    }

    private Auth() {

    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
