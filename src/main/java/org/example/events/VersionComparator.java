package org.example.events;

import org.example.model.Transaction;

import java.util.Comparator;
import java.util.Objects;

public class VersionComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction o1, Transaction o2) {

        return o1.getTransactionID()- o2.getTransactionID();

    }
}
