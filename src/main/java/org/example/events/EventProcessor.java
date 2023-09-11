package org.example.events;

import lombok.AllArgsConstructor;
import org.example.model.BuySell;
import org.example.model.Transaction;
import org.example.model.Type;

import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
public class EventProcessor {

    public int processIndiviudalTransaction(Transaction transaction,int oldChange) {
        int change=0;
        if(Objects.nonNull(transaction)) {
            if (transaction.getType() != Type.CANCEL) {

                    if (transaction.getType() == Type.INSERT) {
                        if (transaction.getBuySell() == BuySell.BUY) {
                            change = transaction.getQuantity();
                        } else {
                            change = transaction.getQuantity() * -1;
                        }
                    }
                 else if (transaction.getType() == Type.UPDATE) {
                             oldChange=oldChange *-1;
                        if (transaction.getBuySell() == BuySell.BUY) {
                            change = oldChange+transaction.getQuantity();
                        } else {
                            change = oldChange+transaction.getQuantity() * -1;
                        }
                }
            } else {
                change = oldChange * -1;
            }
        }
        return change;

    }
}
