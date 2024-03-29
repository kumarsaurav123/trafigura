package org.example.events;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.example.model.Transaction;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
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
    public Transaction getLastEvent(String securityCode,Integer tradeID)
    {
        if(Objects.nonNull(eventTable.get(securityCode,tradeID)) && eventTable.get(securityCode,tradeID).size()>0) {
            return eventTable.row(securityCode).get(tradeID).last();
        }
        return null;
    }
    public void clear(String securityCode,Integer tradeID)
    {

            eventTable.get(securityCode,tradeID).clear();
    }
}
