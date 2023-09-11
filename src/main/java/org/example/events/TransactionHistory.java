package org.example.events;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.example.model.Transaction;

import java.util.*;

public class TransactionHistory {
    private Table<String,Integer, TreeSet<Transaction>> lastAppliedMap= HashBasedTable.create();

    public  void addEvent(Transaction transaction)
    {
        if(Objects.isNull(lastAppliedMap.get(transaction.getSecurityCode(), transaction.getTradeID())))
        {
            lastAppliedMap.put(transaction.getSecurityCode(), transaction.getTradeID(), new TreeSet<>(Comparator.comparing(Transaction::getTransactionID)));
        }
        lastAppliedMap.get(transaction.getSecurityCode(), transaction.getTradeID()).add(transaction);
    }
    public Transaction getLastEvent(String securityCode,Integer tradeID)
    {
        if(Objects.nonNull(lastAppliedMap.row(securityCode).get(tradeID))) {
            return lastAppliedMap.row(securityCode).get(tradeID).last();
        }
        return null;
    }

    public void clear(String securityCode) {
        lastAppliedMap.row(securityCode).clear();
    }
}
