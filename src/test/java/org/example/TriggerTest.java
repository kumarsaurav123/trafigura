package org.example;

import org.example.events.*;
import org.example.model.BuySell;
import org.example.model.Transaction;
import org.example.model.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TriggerTest {

    @InjectMocks
    Trigger trigger;


    @Before
    public void setUp()
    {

        EventProcessor eventProcessor = new EventProcessor();
        EventRepository eventRepository = new EventRepository();
        StateProcessor stateProcessor = new StateProcessor(eventProcessor);
        trigger = new Trigger(eventRepository, stateProcessor);


    }
    @Test
    public void testTrigger()
    {
        Transaction t1 = Transaction.builder().buySell(BuySell.BUY).type(Type.INSERT)
                .quantity(100)
                .tradeID(1)
                .transactionID(1)
                .securityCode("INF")
                .version(1)
                .build();
        Transaction t2=Transaction.builder().buySell(BuySell.BUY).type(Type.UPDATE)
                .quantity(90)
                .tradeID(1)
                .transactionID(2)
                .version(2)
                .securityCode("INF")
                .build();

       ;
        Assert.assertEquals(100, trigger.trigger(t1));
        Assert.assertEquals(90,trigger.trigger(t2));

    }
    @Test
    public void testTriggerWithLateArrival()
    {
        Transaction t1 = Transaction.builder().buySell(BuySell.BUY).type(Type.INSERT)
                .quantity(100)
                .tradeID(1)
                .transactionID(1)
                .securityCode("INF")
                .version(1)
                .build();
        Transaction t2=Transaction.builder().buySell(BuySell.BUY).type(Type.UPDATE)
                .quantity(80)
                .tradeID(1)
                .transactionID(2)
                .version(2)
                .securityCode("INF")
                .build();

        ;

        Assert.assertEquals(0,trigger.trigger(t2));
        Assert.assertEquals(80, trigger.trigger(t1));

    }
}
