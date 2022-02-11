package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class  ExecuteExecutor<T extends BackgroundTask> {
    public void execute(T task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
