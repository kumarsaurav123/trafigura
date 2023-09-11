package org.example;

import org.example.events.VersionComparator;
import org.example.model.BuySell;
import org.example.model.Transaction;
import org.example.model.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

public class VersionComparatorTest {
    @InjectMocks
    VersionComparator versionComparator;

    @Before
    public void setUp()
    {
        versionComparator= new VersionComparator();
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
        Assert.assertEquals(-1,versionComparator.compare(t1,t2));

    }
}
