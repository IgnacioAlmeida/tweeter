package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticatedNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedObserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterService {

    public void gerRegistering(String firstName, String lastName, String alias, String password, String imageBytesBase64, AuthenticatedObserver getRegisterObserver) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new AuthenticatedNotificationHandler(getRegisterObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }
}
