package org.example;

import com.google.common.collect.Sets;
import org.example.events.EventRepository;
import org.example.events.TransactionHistory;
import org.example.model.BuySell;
import org.example.model.Transaction;
import org.example.model.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionHistoryTest {
    @InjectMocks
    TransactionHistory transactionHistory;

    @Before
    public void setUp()
    {
        transactionHistory= new TransactionHistory();
    }
    @Test
    public void testAddEvent()
    {

        Transaction t1 = Transaction.builder().buySell(BuySell.BUY).type(Type.INSERT)
                .quantity(100)
                .tradeID(1)
                .transactionID(1)
                .securityCode("INF")
                .version(1)
                .build();
        Transaction t2=Transaction.builder().buySell(BuySell.SELL).type(Type.CANCEL)
                .quantity(100)
                .tradeID(1)
                .transactionID(1)
                .version(2)
                .securityCode("INF")
                .build();
        transactionHistory.addEvent(t1);
        transactionHistory.addEvent(t2);
        Assert.assertEquals(t2,transactionHistory.getLastEvent("INF",1));
        transactionHistory.clear("INF");
        Assert.assertNull(transactionHistory.getLastEvent("INF",1));

    }
}
