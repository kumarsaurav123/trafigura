package org.example.events;

import org.example.model.Transaction;

import java.util.Comparator;
import java.util.Objects;

public class VersionComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction o1, Transaction o2) {
        if(!Objects.equals(o1.getTradeID(), o2.getTradeID()))
        {
            return  o1.getTradeID()-o2.getTradeID();
        }
        return o1.getVersion()- o2.getVersion();

    }
}
