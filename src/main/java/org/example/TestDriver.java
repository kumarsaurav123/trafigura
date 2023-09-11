package org.example;

import lombok.SneakyThrows;
import org.example.events.*;
import org.example.model.BuySell;
import org.example.model.Transaction;
import org.example.model.Type;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestDriver {
    @SneakyThrows
    public static void main(String[] args) {

        EventProcessor eventProcessor = new EventProcessor();
        EventRepository eventRepository = new EventRepository();
        TransactionHistory transactionHistory = new TransactionHistory();
        StateProcessor stateProcessor = new StateProcessor(eventProcessor);
        Trigger trigger = new Trigger(eventRepository, transactionHistory, stateProcessor);

        Files.lines(Paths.get(args[0])).
                skip(1)
                .map(l -> l.split(","))
                .map(l -> Transaction.builder().transactionID(Integer.parseInt(l[0]))
                        .type(Type.valueOf(l[5].toUpperCase()))
                        .buySell(BuySell.valueOf(l[6].toUpperCase()))
                        .tradeID(Integer.parseInt(l[1]))
                        .version(Integer.parseInt(l[2]))
                        .quantity(Integer.parseInt(l[4]))
                        .securityCode(l[3])
                        .build())
                .peek(t -> trigger.trigger(t))
                .peek(p -> System.out.println(stateProcessor.getAllPositions()))
                .collect(Collectors.toList());

    }

}