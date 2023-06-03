package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Queue;
import java.util.Set;


@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Task implements Comparable<Task> {
    private final Integer id;
    private final List<DataPoint> inputDataNode;
    private final List<DataPoint> outPutDataNode;
    private final Queue<DataPoint> changedDataPointQueue;
    private final Set<Task> allExecutedTasks;

    public void trigger() {

        changedDataPointQueue.addAll(outPutDataNode);
    }

    @Override
    public int compareTo(Task o) {
        return this.getId().compareTo(o.getId());
    }
}
