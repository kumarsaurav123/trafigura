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
        sb.append(testNumber);
        Set<Task> allExecutedTask = new TreeSet<>();

        for (int j = 2; j < noOfTasks; j++) {
            //create a task with input output data nodes
            String[] inputOutputDataPoints = testCase[j].split(";");
            List<DataPoint> inputDataNode = getInputOutPutDataPoints(inputOutputDataPoints, 0);
            List<DataPoint> outPutDataNode = getInputOutPutDataPoints(inputOutputDataPoints, 1);
            Task task = new Task(j, inputDataNode, outPutDataNode, changedDataPointQueue, allExecutedTask);
            inputDataPointToTaskMap.putAll(inputDataNode.stream().collect(Collectors.toMap(Function.identity(), a -> task)));
        }
        //initial trigger condition
        Arrays.stream(testCase[testCase.length - 1].split(","))
                .map(DataPoint::new)
                .forEach(changedDataPointQueue::add);
        //use BFS to calculate all visited nodes
        while (!changedDataPointQueue.isEmpty()) {
            Task executingTask = inputDataPointToTaskMap.get(changedDataPointQueue.poll());
            if (Objects.nonNull(executingTask) && !allExecutedTask.contains(executingTask)) {
                allExecutedTask.add(executingTask);
                executingTask.trigger();
            }
        }
        sb.append(allExecutedTask.stream().map(Task::getId).map(Objects::toString).collect(Collectors.joining(",")));
        return sb.toString();
    }

    private static List<DataPoint> getInputOutPutDataPoints(String[] inputOutputDataPoints, int x) {
        return Arrays.stream(inputOutputDataPoints[x].split(",")).
                map(DataPoint::new)
                .collect(Collectors.toList());
    }
}