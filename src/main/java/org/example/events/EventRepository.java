package org.example.events;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.example.model.BuySell;
import org.example.model.Transaction;
import org.example.model.Type;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EventRepository {

    private Table<String,Integer, TreeSet<Transaction>> eventTable= HashBasedTable.create();
    public EventRepository()
    {
    }
    public void addEvent(Transaction transaction)
    {
        if(Objects.isNull(eventTable.get(transaction.getSecurityCode(), transaction.getTradeID())))
        {
            eventTable.put(transaction.getSecurityCode(), transaction.getTradeID(), new TreeSet<>(new VersionComparator()));
        }
        eventTable.get(transaction.getSecurityCode(), transaction.getTradeID()).add(transaction);
    }
   public Set<Transaction> getEvents(String securityCode)
   {
       Set<Transaction> allEvents= new TreeSet(new VersionComparator());
       allEvents.addAll(eventTable.row(securityCode)
               .values()
               .stream().flatMap(Collection::stream)
               .collect(Collectors.toList()));
       return allEvents;
   }

}
