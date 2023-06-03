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
public class TestDriver {
    @SneakyThrows
    public static void main(String[] args) {
        String input = Files.lines(Paths.get(args[0])).
                collect(Collectors.joining("\n"));
        //array contain all test cases, it assumes test are separated by blank line
       String output =Arrays.stream(input.split("\n\n"))
                .map(TestDriver::runEachTestCase)
                .collect(Collectors.joining("\n"));
        Path outputPath = Paths.get(Paths.get(args[0]).getParent().toFile().getPath(), "output.txt");
        System.out.println(output);
        System.out.println("Output Path is " + outputPath);
        Files.write(outputPath, output.getBytes(StandardCharsets.UTF_8)
                , StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }

    private static String  runEachTestCase( String testCaseString) {
        StringBuilder sb = new StringBuilder();
        Queue<DataPoint> changedDataPointQueue = new LinkedBlockingDeque<>();
        //extract each test case and their input output node
        String[] testCase = testCaseString.split("\n");
        String testNumber = testCase[0];
        Map<DataPoint, Task> inputDataPointToTaskMap = new HashMap<>();
        int noOfTasks = Integer.parseInt(testCase[1]);
        boolean[] allExecutedTasks = new boolean[noOfTasks];
        sb.append(testNumber);

        for (int j = 0; j < noOfTasks; j++) {
            //create a task with input output data nodes
            String[] inputOutputDataPoints = testCase[j + 2].split(";");
            List<DataPoint> inputDataNode = Arrays.stream(inputOutputDataPoints[0].split(",")).
                    map(DataPoint::new)
                    .collect(Collectors.toList());
            List<DataPoint> outPutDataNode = Arrays.stream(inputOutputDataPoints[1].split(","))
                    .map(DataPoint::new)
                    .collect(Collectors.toList());
            Task task = new Task(j, inputDataNode, outPutDataNode, changedDataPointQueue, allExecutedTasks);
            inputDataPointToTaskMap.putAll(
                    inputDataNode.stream()
                            .collect(Collectors.toMap(Function.identity(), a -> task)));
        }
        //initial trigger condition
        Arrays.stream(testCase[testCase.length - 1].split(","))
                .map(DataPoint::new)
                .forEach(changedDataPointQueue::add);
        Set<Integer> allExecutedTask = new TreeSet<>();
        //use BFS to calculate all visited nodes
        while (!changedDataPointQueue.isEmpty()) {
            Task executingTask = inputDataPointToTaskMap.get(changedDataPointQueue.poll());
            if (Objects.nonNull(executingTask) && !allExecutedTasks[executingTask.getId()]) {
                allExecutedTask.add(executingTask.getId());
                allExecutedTasks[executingTask.getId()] = true;
                executingTask.trigger();
            }
        }
        sb.append(allExecutedTask.stream().map(Object::toString).collect(Collectors.joining(",")));
        return sb.toString();
    }
}