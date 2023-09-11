package org.example.events;

import org.example.model.Transaction;
import org.example.model.Type;

import java.util.Optional;
import java.util.Set;

public class Trigger {
    private final EventRepository eventRepository;
    private final StateProcessor stateProcessor;

    public Trigger(EventRepository eventRepository, StateProcessor stateProcessor) {
        this.eventRepository = eventRepository;
        this.stateProcessor = stateProcessor;
    }

    public int trigger(Transaction transaction) {
        return process(transaction, true);
    }

    private int process(Transaction transaction, boolean add) {
        Transaction previous = eventRepository.getLastEvent(transaction.getSecurityCode(), transaction.getTradeID());
        //if got update or cancel wait for previous transactions
        int lastVersion = Optional.ofNullable(previous)
                .map(Transaction::getVersion).orElse(0);
        if (lastVersion!=transaction.getVersion()-1 && transaction.getType()!=Type.INSERT) {

            addEvent(transaction, add);
            return stateProcessor.getCurrentPositions(transaction.getSecurityCode());
        }
        if (transaction.getVersion() > lastVersion) {

            stateProcessor.triggerStateChange(transaction, previous);
            addEvent(transaction, add);
        } else {
            addEvent(transaction, add);
            stateProcessor.clear(transaction.getSecurityCode());
            Set<Transaction> previousTransaction = eventRepository.getEvents(transaction.getSecurityCode());
            eventRepository.clear(transaction.getSecurityCode(),transaction.getTradeID());
            for (Transaction t : previousTransaction) {
                process(t, true);
            }
        }
        return stateProcessor.getCurrentPositions(transaction.getSecurityCode());
    }

    public void addEvent(Transaction transaction, boolean add) {
        if (add) {
            eventRepository.addEvent(transaction);
        }
    }
}
