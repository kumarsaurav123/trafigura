package org.example.events;

import com.google.common.collect.Maps;
import org.example.model.BuySell;
import org.example.model.Transaction;
import org.example.model.Type;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EventRepository {

    private Map<String, Set<Transaction>> eventMap= new ConcurrentHashMap<>();

    public EventRepository()
    {
    }
    public void addEvent(Transaction t)
    {
        if(Objects.nonNull(eventMap.get(t.getSecurityCode())))
        {
            eventMap.get(t.getSecurityCode()).add(t);
        }
        else {
            eventMap.put(t.getSecurityCode(),new TreeSet<>(new VersionComparator()));
            eventMap.get(t.getSecurityCode()).add(t);
        }
    }
   public Set<Transaction> getEvents(String securityCode)
   {
       return eventMap.getOrDefault(securityCode,new TreeSet<>());
   }

}
