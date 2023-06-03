package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Queue;


@Data
@AllArgsConstructor
public class Task {
    private final int id;
    private final List<DataPoint> inputDataNode;
    private final List<DataPoint> outPutDataNode;
    private final Queue<DataPoint> taskQueue;
    private final boolean[] allExecutedTasks;

    public void trigger() {
        outPutDataNode.stream().filter(i->!taskQueue.contains(i))
                        .forEach(taskQueue::add);
//        taskQueue.addAll(outPutDataNode);
    }
}
