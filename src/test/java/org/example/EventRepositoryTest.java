package org.example;

import com.google.common.collect.Sets;
import org.example.events.EventProcessor;
import org.example.events.EventRepository;
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
public class EventRepositoryTest {
    @InjectMocks
    EventRepository eventRepository;

    @Before
    public void setUp()
    {
        eventRepository= new EventRepository();
    }
    @Test
    public void testGetEvents()
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
        eventRepository.addEvent(t2);
        eventRepository.addEvent(t1);
        Assert.assertEquals(Sets.newHashSet(t1,t2),eventRepository.getEvents("INF"));
    }
}
