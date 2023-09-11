
package org.example;

import org.example.events.EventProcessor;
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
public class EventProcessorTest {

    @InjectMocks
    EventProcessor eventProcessor;

    @Before
    public void setUp()
    {
        eventProcessor= new EventProcessor();
    }
    @Test
    public void testProcessIndiviudalTransaction()
    {
       Assert.assertEquals(100,eventProcessor.processIndiviudalTransaction(Transaction.builder().buySell(BuySell.BUY).type(Type.INSERT)
                        .quantity(100)
                .build(),Integer.MIN_VALUE));
        Assert.assertEquals(-100,eventProcessor.processIndiviudalTransaction(Transaction.builder().buySell(BuySell.SELL).type(Type.INSERT)
                .quantity(100)
                .build(),Integer.MIN_VALUE));
        Assert.assertEquals(20,eventProcessor.processIndiviudalTransaction(Transaction.builder().buySell(BuySell.SELL).type(Type.UPDATE)
                .quantity(100)
                .build(),-120));
        Assert.assertEquals(-20,eventProcessor.processIndiviudalTransaction(Transaction.builder().buySell(BuySell.BUY).type(Type.UPDATE)
                .quantity(100)
                .build(),120));
    }
}
