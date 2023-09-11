package org.example.events;

import lombok.AllArgsConstructor;
import org.example.model.Transaction;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public class StateProcessor {
    private final EventProcessor eventProcessor;
    private Map<String, Integer> currentState= new ConcurrentHashMap<>();
    public StateProcessor(EventProcessor eventProcessor)
    {
        this.eventProcessor=eventProcessor;
    }

    public Integer getCurrentPositions(String securityCode)
    {
        return currentState.getOrDefault(securityCode,0);
    }
    public Map<String, Integer> getAllPositions()
    {
        return currentState;
    }
    public Integer addState(String securityCode,Integer val)
    {
        return currentState.put(securityCode,val);
    }
    public void triggerStateChange(Transaction t,Transaction previous)
    {

            int prevChange=eventProcessor.processIndiviudalTransaction(previous,Integer.MIN_VALUE);
            int change=eventProcessor.processIndiviudalTransaction(t,prevChange);
            int val=getCurrentPositions(t.getSecurityCode())+change;
            addState(t.getSecurityCode(),val);

    }

    public void clear(String securityCode) {
        currentState.put(securityCode,0);
    }
}
