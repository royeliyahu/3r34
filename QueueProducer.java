package formsubmission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import formsubmission.model.MyTweet;
import formsubmission.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service("QueueProducer")
public class QueueProducer {//implements Runnable{
        @Autowired
    Twitter twitter;


    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(10);

    public void collectTweets(User user) {
        final Runnable collector = new Runnable() {
            public void run() {
                try {
                    openChanelAndPost(user);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        };
        final ScheduledFuture<?> tweetsCollector =
                scheduler.scheduleAtFixedRate(collector, 0, 100, SECONDS);
//        scheduler.schedule(new Runnable() {
//            public void run() { tweetsCollector.cancel(true); }
//        }, 60 * 60, SECONDS);
    }
//    private User user;

//    public QueueProducer(User user) {
//        this.user = user;
//    }

//    @Override
//    public void run() {
//      while (true) {
//          try {
//              openChanelAndPost();
//          } catch (IOException e) {
//              e.printStackTrace();
//          } catch (TimeoutException e) {
//              e.printStackTrace();
//          }
//          catch (Exception e){
//              System.err.println("exeption: " );
//              e.printStackTrace();
//          }
//
//          try {
//              Thread.sleep(5000);
//          } catch (InterruptedException e) {
//              e.printStackTrace();
//          }
//      }
//    }

//    @Bean
//    @Async
  public void openChanelAndPost(User user) throws IOException, TimeoutException {
      System.err.println("openChanelAndPost");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(user.getFirstHashtag(), false, false, false, null);
        channel.queueDeclare(user.getSecondHashtag(), false, false, false, null);
        channel.queueDeclare(user.getThirdHashtag(), false, false, false, null);

        List<Tweet> first = getTweets(user.getFirstHashtag());
        List<Tweet> second = getTweets(user.getSecondHashtag());
        List<Tweet> third = getTweets(user.getThirdHashtag());


        publishTweets(channel, first,  user.getFirstHashtag());
        publishTweets(channel, second,  user.getSecondHashtag());
        publishTweets(channel, third,  user.getThirdHashtag());



//        third.forEach(tweet -> {
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                String jsonInString = mapper.writeValueAsString(new MyTweet(tweet));
//                System.err.println(jsonInString);
//
//                channel.basicPublish("", user.getThirdHashtag(), null, jsonInString.getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });


        channel.close();
        connection.close();
    }

    private void publishTweets(Channel channel, List<Tweet> first, String queueName) {
        first.forEach(tweet -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String jsonInString = mapper.writeValueAsString(new MyTweet(tweet, queueName));
                System.err.println(jsonInString);

                channel.basicPublish("", queueName, null, jsonInString.getBytes());
                 } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<Tweet> getTweets(String hashTag){
        return twitter.searchOperations().search(hashTag).getTweets();
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }


}
