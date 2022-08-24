package model.database;

import java.sql.Connection;

public class CounterStore {
    private Connection c;

    public CounterStore(Connection c) {
        this.c = c;
    }


}
