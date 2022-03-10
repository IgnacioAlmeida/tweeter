package edu.byu.cs.tweeter.client.presenter;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainActivityPresenterUnitTest {

  private MainActivityPresenter.View mockView;
  private FeedService mockFeedService;
  private Status testStatus;
  private AuthToken testAuthToken;
  private MainActivityPresenter mainActivityPresenterSpy;

  @Before
  public void setup() {
    mockView = Mockito.mock(MainActivityPresenter.View.class);
    mockFeedService = Mockito.mock(FeedService.class);

    testStatus = new Status();
    testStatus.datetime = "test";

    testAuthToken = new AuthToken("123");
    Cache.getInstance().setCurrUserAuthToken(testAuthToken);

    mainActivityPresenterSpy = Mockito.spy(new MainActivityPresenter(mockView));
    when(mainActivityPresenterSpy.getFeedService()).thenReturn(mockFeedService);
  }

  public void setupAndCallPostingStatusAndVerifyCommonResult(Answer<Void> answer){
    Mockito.doAnswer(answer).when(mockFeedService).postStatus(Mockito.any(),Mockito.any(),Mockito.any(MainActivityPresenter.GetPostStatusObserver.class));
    mainActivityPresenterSpy.postStatus(testStatus);
    Mockito.verify(mockView).displayInfoMessage("Posting Status...");
  }

  public abstract class HandleObserverAnswer {
    Answer<Void> answer;
    MainActivityPresenter.GetPostStatusObserver observer;
    AuthToken authToken;
    Status status;
    abstract void observerHandler();
    public HandleObserverAnswer() {
      answer = new Answer<Void>() {
        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
          authToken = invocation.getArgument(0, AuthToken.class);
          assert(authToken.getClass() == AuthToken.class);
          assert(authToken.token == "123");
          status = invocation.getArgument(1, Status.class);
          assert(status.getClass() == Status.class);
          assert(status.datetime == "test");
          observer = invocation.getArgument(2, MainActivityPresenter.GetPostStatusObserver.class);
          assert(observer.getClass() == MainActivityPresenter.GetPostStatusObserver.class);
          observerHandler();
          return null;
        }
      };
    }
  }

  @Test
  public void testPostStatus_postSuccessful() {
    HandleObserverAnswer handleObserverAnswer = new HandleObserverAnswer() {
      @Override
      void observerHandler() {
        observer.handleSuccess(true);
      }
    };

    setupAndCallPostingStatusAndVerifyCommonResult(handleObserverAnswer.answer);
    Mockito.verify(mockView).handlePostSuccess();
  }

  @Test
  public void testPostStatus_postFailedWithMessage() {
    HandleObserverAnswer handleObserverAnswer = new HandleObserverAnswer() {
      @Override
      void observerHandler() {
        observer.handleFailure("the error message");
      }
    };

    setupAndCallPostingStatusAndVerifyCommonResult(handleObserverAnswer.answer);

    Mockito.verify(mockView).displayErrorMessage("Failed to post status: the error message");

  }

  @Test
  public void testPostStatus_postFailedWithException() {
    HandleObserverAnswer handleObserverAnswer = new HandleObserverAnswer() {
      @Override
      void observerHandler() {
        observer.handleException(new Exception("the exception message"));
      }
    };

    setupAndCallPostingStatusAndVerifyCommonResult(handleObserverAnswer.answer);


    Mockito.verify(mockView).displayErrorMessage("Failed to post status because of exception: the exception message");


  }

}
