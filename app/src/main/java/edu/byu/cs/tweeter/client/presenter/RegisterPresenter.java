package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.RegisterService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {

    public interface View {
        void displayErrorMessage(String message);
        void handleSuccess(User registeredUser, AuthToken authToken);
    }
    private View view;
    private RegisterService registerService;

    public RegisterPresenter(View view) {
        this.view = view;
        registerService = new RegisterService();
    }

    public void registerUser(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        registerService.gerRegistering(firstName, lastName, alias, password, imageBytesBase64, new GetRegisterObserver());
    }

    public class GetRegisterObserver implements RegisterService.GetRegisterObserver {

        @Override
        public void handleSuccess(User registeredUser, AuthToken authToken) {
            view.handleSuccess(registeredUser, authToken);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to register: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage( "Failed to register because of exception: " + exception.getMessage());
        }
    }
}
