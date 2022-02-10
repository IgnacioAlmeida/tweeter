package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.ExecuteExecutor;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticatedNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedObserver;

public class RegisterService extends ExecuteExecutor {

    public void gerRegistering(String firstName, String lastName, String alias, String password, String imageBytesBase64, AuthenticatedObserver getRegisterObserver) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new AuthenticatedNotificationHandler(getRegisterObserver));
        execute(registerTask);
    }
}
