package org.example.events;

import org.example.model.Transaction;
import org.example.model.Type;

import java.util.Optional;

public class Trigger {
    private final EventRepository eventRepository;
    private final TransactionHistory transactionHistory;
    private final StateProcessor stateProcessor;

    public Trigger(EventRepository eventRepository,TransactionHistory transactionHistory,StateProcessor stateProcessor)
    {
        this.eventRepository=eventRepository;
        this.transactionHistory=transactionHistory;
        this.stateProcessor=stateProcessor;
    }
    public int trigger(Transaction transaction)
    {
        eventRepository.addEvent(transaction);
        return process(transaction);
    }

    private int process(Transaction transaction) {
        Transaction previous=transactionHistory.getLastEvent(transaction.getSecurityCode(), transaction.getTradeID());
        if(transaction.getType()!= Type.CANCEL) {
            transactionHistory.addEvent(transaction);
        }
        int lastVersion= Optional.ofNullable(previous)
                .map(Transaction::getVersion).orElse(Integer.MIN_VALUE);
        if(transaction.getVersion()> lastVersion)
        {
            stateProcessor.triggerStateChange(transaction,previous);
        }
        else {
            transactionHistory.clear(transaction.getSecurityCode());
            stateProcessor.clear(transaction.getSecurityCode());
            for(Transaction t :eventRepository.getEvents(transaction.getSecurityCode()))
            {
                process(t);
            }
        }
        return stateProcessor.getCurrentPositions(transaction.getSecurityCode());
    }
}
