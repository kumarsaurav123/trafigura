package org.example;

import com.google.common.collect.Sets;
import org.example.events.EventProcessor;
import org.example.events.EventRepository;
import org.example.events.StateProcessor;
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
public class StateProcessorTest {
    @Mock
    EventProcessor eventProcessor;
    @InjectMocks
    StateProcessor stateProcessor;

    @Before
    public void setUp()
    {
        stateProcessor= new StateProcessor(new EventProcessor());
    }
    @Test
    public void testTriggerStateChange()
    {
       Transaction t1 = Transaction.builder().buySell(BuySell.BUY).type(Type.INSERT)
                .quantity(100)
                .tradeID(1)
                .transactionID(1)
                .securityCode("INF")
                .version(1)
                .build();
        Transaction t2=Transaction.builder().buySell(BuySell.BUY).type(Type.CANCEL)
                .quantity(100)
                .tradeID(1)
                .transactionID(1)
                .version(2)
                .securityCode("INF")

                .build();

        stateProcessor.triggerStateChange(t1,null);
        stateProcessor.triggerStateChange(t2,t1);
       Assert.assertEquals(new Integer(0), stateProcessor.getCurrentPositions("INF"));
    }
}
