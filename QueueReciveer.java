package formsubmission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import formsubmission.model.MyTweet;
import formsubmission.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;


/**




@Service
class BeeperControl {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void beepForAnHour() {
        final Runnable beeper = new Runnable() {
            public void run() { System.out.println("beep"); }
        };
        final ScheduledFuture<?> beeperHandle =
                scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);
        scheduler.schedule(new Runnable() {
            public void run() { beeperHandle.cancel(true); }
        }, 60 * 60, SECONDS);
    }
}



/**/


public class QueueReciveer implements Runnable{

    private User user;

    public QueueReciveer(User user) {
        this.user = user;
    }

    @Override
    public void run() {

        try {
            openChannelAndListen();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void openChannelAndListen() throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.queueDeclare(user.getFirstHashtag(), false, false, false, null);
        channel.queueDeclare(user.getSecondHashtag(), false, false, false, null);
        channel.queueDeclare(user.getThirdHashtag(), false, false, false, null);

        List<MyTweet> q = new ArrayList<>();
        List<MyTweet> qa = new ArrayList<>();


        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                List<MyTweet> qb = new ArrayList<>();
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");

                ObjectMapper mapper = new ObjectMapper();
                MyTweet tweet = mapper.readValue(message, MyTweet.class);

                System.err.println("obj: " + tweet.getCreatedAt() + "   " + tweet.getText() + "  Text: " + tweet.getFromUser() + " HashTag:  " + tweet.getHashTag());
                System.err.println("done!! size: " + qb.size() + "  all size: " + q.size());
                q.add(tweet);
                qb.add(tweet);
                System.err.println("adding: " + tweet.toString());
                user.addTweet(tweet);
                user.addHashTagTweetCount(tweet.getHashTag(), 1);

            }
//            HashMap<String, Integer> hashTagTweetCount = user.getHashTagTweetCount();
//            hashTagTweetCount.put(user.getFirstHashtag(), qb.size());

        };
        String s = channel.basicConsume(user.getFirstHashtag(), true, consumer);
        channel.basicConsume(user.getSecondHashtag(), true, consumer);
        channel.basicConsume(user.getThirdHashtag(), true, consumer);










//        List<Tweet> first = getTweets(user.getFirstHashtag());
//        List<Tweet> second = getTweets(user.getSecondHashtag());
//        List<Tweet> third = getTweets(user.getThirdHashtag());
//        System.err.println("twits for: " + user.getFirstHashtag() + " : " + first.toString());
//        System.err.println("twits for: " + user.getSecondHashtag() + " : " + second.toString());
//        System.err.println("twits for: " + user.getThirdHashtag() + " : " + third);

//        HashMap<String, Integer> hashTagTweetCount = user.getHashTagTweetCount();
//        hashTagTweetCount.put(user.getFirstHashtag(), first.size());
//        hashTagTweetCount.put(user.getSecondHashtag(), second.size());
//        hashTagTweetCount.put(user.getThirdHashtag(), third.size());


//        List<Tweet> q = new ArrayList<>();
//        q.addAll(first.subList(0,Math.min(NUM_OF_TWITS_TO_DISPLAY, first.size())));
//        q.addAll(second.subList(0,Math.min(NUM_OF_TWITS_TO_DISPLAY, second.size())));
//        q.addAll(third.subList(0,Math.min(NUM_OF_TWITS_TO_DISPLAY, third.size())));
//        q.sort(Comparator.comparing(MyTweet::getCreatedAt));
//        Queue userQ = user.getLast10Twits();
//        q.subList(Math.max(0,q.size() - NUM_OF_TWITS_TO_DISPLAY),
//                q.size())
//                .stream().forEach(tweet -> userQ.add(tweet));
    }
}
