package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;
import edu.byu.cs.tweeter.client.model.service.RegisterService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

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

    public String convertImage(Bitmap image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public void validateRegistration(EditText firstName, EditText lastName, EditText alias, EditText password, ImageView imageToUpload) {
        if (firstName.getText().length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.getText().length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.getText().length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageToUpload.getDrawable() == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    public void registerUser(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        registerService.gerRegistering(firstName, lastName, alias, password, imageBytesBase64, new GetRegisterObserver());
    }

    public class GetRegisterObserver implements AuthenticatedObserver {

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
