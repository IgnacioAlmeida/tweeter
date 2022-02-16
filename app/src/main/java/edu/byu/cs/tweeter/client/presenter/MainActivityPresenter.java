package edu.byu.cs.tweeter.client.presenter;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.CounterObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainActivityPresenter {
    public interface View {
        void displayErrorMessage(String message);
        void handleSuccess(boolean follow);
        void handleSuccessFollowees(int count);
        void handleSuccessFollowers(int count);
        void handlePostSuccess();
        void handleLogoutSuccess();
        void handleIsFollowerSuccess(boolean isFollower);
        void displayInfoMessage(String message);

    }
    private View view;
    private FollowService followService;
    private FeedService feedService;
    private LoginService loginService;

    public MainActivityPresenter(View view) {
        this.view = view;
        followService = new FollowService();
//        feedService = getFeedService();
        loginService = new LoginService();

    }

    protected FeedService getFeedService() {
        if(feedService == null){
            feedService = new FeedService();
        }
        return feedService;
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public List<String> parseURLs(String post) throws MalformedURLException {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public void unfollow(User selectedUser) {
        followService.unfollow(Cache.getInstance().getCurrUserAuthToken(),selectedUser, new GetUnfollowObserver());
    }

    public class GetUnfollowObserver implements SimpleNotificationObserver {
        @Override
        public void handleSuccess(Boolean status) {
            view.handleSuccess(false);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to unfollow: " + message);

        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to unfollow because of exception: " + exception.getMessage());

        }


    }

    public void follow(User selectedUser) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowObserver());
    }

    public class GetFollowObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess(Boolean status) {
            view.handleSuccess(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to follow: " + message);

        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to follow because of exception: " + exception.getMessage());

        }

    }


    public void getFollowingCounter(User selectedUser, Executor executor) {
        followService.getFollowingCounter(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowingCounterObserver(), executor);
    }

    public class GetFollowingCounterObserver implements CounterObserver {

        @Override
        public void handleSuccess(int count) {
            view.handleSuccessFollowees(count);

        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get following count: " + message);

        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get following count because of exception: " + exception.getMessage());

        }
    }


    public void getFollowersCounter(User selectedUser, Executor executor) {
        followService.getFollowersCounter(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowersCounterObserver(), executor);
    }

    public class GetFollowersCounterObserver implements CounterObserver {

        @Override
        public void handleSuccess(int count) {
            view.handleSuccessFollowers(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get followers count: " + message);

        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get followers count because of exception: "+ exception.getMessage());

        }
    }

    //TODO create tests  for post status
    public void postStatus(Status newStatus) {
        view.displayInfoMessage("Posting Status...");
        getFeedService().postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus, new GetPostStatusObserver());

    }

    public class GetPostStatusObserver implements SimpleNotificationObserver {

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to post status because of exception: " + exception.getMessage());

        }

        @Override
        public void handleSuccess(Boolean status) {
            view.handlePostSuccess();
        }
    }

    public void logout() {
        loginService.logout(Cache.getInstance().getCurrUserAuthToken(), new GetLogoutObserver());
    }

    public class GetLogoutObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess(Boolean status) {
            Cache.getInstance().clearCache();
            view.handleLogoutSuccess();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to logout: " + message);

        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to logout because of exception: " + ex.getMessage());

        }

    }


    public void isFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new GetIsFollowerObserver());
    }

    public class GetIsFollowerObserver implements FollowService.GetIsFollowerObserver {
        @Override
        public void handleSuccess(boolean isFollower) {
            if (isFollower) {
                view.handleIsFollowerSuccess(true);
            } else {
                view.handleIsFollowerSuccess(false);
            }
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to determine following relationship because of exception: " + exception.getMessage());
        }
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);
                containedMentions.add(word);
            }
        }
        return containedMentions;
    }


}
