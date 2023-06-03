package org.example;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;
import java.util.stream.Collectors;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class TestDriver {
    @SneakyThrows
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        String input = Files.lines(Paths.get(args[0])).
                collect(Collectors.joining("\n"));
        //array contain all test cases it assumes test are seperated by blank line
        String[] inputArr = input.split("\n\n");
        StringBuilder sb = new StringBuilder();
        //run for each cases
        for (int i = 0; i < inputArr.length; i++) {
            Queue<DataPoint> dataPointQueue = new LinkedBlockingDeque<>(1024);
            String[] singleTestcase = inputArr[i].split("\n");
            String testNumber = singleTestcase[0];
            sb.append(testNumber);
            System.out.println("Test number Executing--> " + testNumber);
            Map<DataPoint, Task> inputDataPointToTaskMap = new HashMap<>();
            Integer noOfTasks = Integer.parseInt(singleTestcase[1]);
            boolean[] allExecutedTasks = new boolean[noOfTasks];

            for (int j = 0; j < noOfTasks; j++) {
                String[] inputOutputDataPoints = singleTestcase[j + 2].split(";");
                List<DataPoint> inputDataNode = Arrays.stream(inputOutputDataPoints[0].split(",")).
                        map(DataPoint::new)
                        .collect(Collectors.toList());
                List<DataPoint> outPutDataNode = Arrays.stream(inputOutputDataPoints[1].split(","))
                        .map(DataPoint::new)
                        .collect(Collectors.toList());
                System.out.println(inputDataNode + "-->" + outPutDataNode);
                Task task = new Task(j, inputDataNode, outPutDataNode, dataPointQueue, allExecutedTasks);
                inputDataPointToTaskMap.putAll(
                        inputDataNode.stream()
                                .collect(Collectors.toMap(Function.identity(), a -> task)));
            }
            Arrays.stream(singleTestcase[singleTestcase.length - 1].split(","))
                    .map(DataPoint::new)
                    .forEach(dataPointQueue::add);
            Set<Integer> allExecutedTask = new TreeSet<>();
            while (!dataPointQueue.isEmpty()) {
                Task executingTask = inputDataPointToTaskMap.get(dataPointQueue.poll());
                if (Objects.nonNull(executingTask) && !allExecutedTasks[executingTask.getId()]) {
                    allExecutedTask.add(executingTask.getId());
                    allExecutedTasks[executingTask.getId()] = true;
                    executingTask.trigger();
                }
            }
            sb.append(allExecutedTask.stream().map(Object::toString).collect(Collectors.joining(",")));
            sb.append("\n");
        }
        System.out.println(sb.toString());
        Path outputPath = Paths.get(Paths.get(args[0]).getParent().toFile().getPath(), "output.txt");
        System.out.println("Output Path is" + outputPath.toString());
        Files.write(outputPath, sb.toString().getBytes(StandardCharsets.UTF_8)
                , StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }
}