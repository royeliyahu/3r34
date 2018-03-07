package formsubmission.controller;


import formsubmission.QueueProducer;
import formsubmission.QueueReciveer;
import formsubmission.model.MyTweet;
import formsubmission.model.TweetCount;
import formsubmission.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


@Controller
public class SimpleWebController {
//    @Autowired
//    Twitter twitter;
    @Autowired
    QueueProducer queueProducer;
    @Autowired
    BeeperControl beeperControl;


    Logger log = LoggerFactory.getLogger(this.getClass());
//    public static
    ArrayList<User> users = new ArrayList<>();

    @RequestMapping(value="/form", method= RequestMethod.GET)
    public String userForm(Model model) {
        model.addAttribute("user", new User());
        return "form";
    }
 
    @RequestMapping(value="/form", method= RequestMethod.POST)
    public String userSubmit(@ModelAttribute User user, Model model) {

        model.addAttribute("user", user);
        String info = String.format("User Submission: userName = %s, firsHashtag = %s, secondHashtag = %s, thirdHashtag = %s",
                                        user.getUserName(), user.getFirstHashtag(), user.getSecondHashtag(), user.getThirdHashtag());
        log.info(info);

        users.add(user);


//        beeperControl.beepForAnHour();
//        queueProducer.collectTweets(user);

//        new Thread(new HelloRunnable())).start()

//        Runnable user1 = new QueueProducer(user);
//        ((QueueProducer)user1).setTwitter(twitter);
//        new Thread(user1).start();
//        user1.run();


        try {
            queueProducer.openChanelAndPost(user);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

//        Runnable thread = () -> {
//            try {
//                queueProducer.openChanelAndPost(user);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (TimeoutException e) {
//                e.printStackTrace();
//            }
//        };
//        new Thread(thread).start();


        return "result";
    }

//    /* */
//    public void handlePostTrade(PostTradeTO postTradeTO, SystemTradeOrder tradeMessage) {
//        logger.info("Create new Thread For Regular PostTrade logic.");
//        Runnable thread = () -> handlePostTradeLogic(postTradeTO, tradeMessage);
//        new Thread(thread).start();
//    }


    @RequestMapping(value="/display", method= RequestMethod.GET)
    public String userDisplay(Model model) {
        model.addAttribute("user", new User());
        return "display";
    }

    @RequestMapping(value="/display", method= RequestMethod.POST)
    public String userDisplayScreen(@ModelAttribute User user, Model model) {
        if(! users.contains(user)){
            return "noSuchUser";
        }

        Runnable user1 = new QueueReciveer(users.get(users.indexOf(user)));
        user1.run();

        user = users.get(users.indexOf(user));
        model.addAttribute("user", user);
        List<MyTweet> tweetList = user.getLast10Twits().stream().collect(Collectors.toList());
        model.addAttribute("last10Twits", tweetList);
        model.addAttribute("message", "last 10 Twits for user: " + user.getUserName());

        String info = String.format("User display: userName = %s, firsHashtag = %s, secondHashtag = %s, thirdHashtag = %s",
                user.getUserName(), user.getFirstHashtag(), user.getSecondHashtag(), user.getThirdHashtag());
        log.info(info);

        return "displayUser";
    }

    @RequestMapping(value="/statistics", method= RequestMethod.GET)
    public String userStatistics(Model model) {
        model.addAttribute("user", new User());
        return "statistics";
    }

    @RequestMapping(value="/statistics", method= RequestMethod.POST)
    public String userStatisticsScreen(@ModelAttribute User user, Model model) {

        if(! users.contains(user)){
            return "noSuchUser";
        }

        user = users.get(users.indexOf(user));

        model.addAttribute("user", user);
        model.addAttribute("message", "hash Tag Tweet Count statistics for user: " + user.getUserName());

        List<TweetCount> tweetCountList =  user.getHashTagTweetCount().entrySet().stream().map(
                data -> new TweetCount(data.getValue(), data.getKey())).collect(Collectors.toList());
        model.addAttribute("hashTagTweetCount", tweetCountList);

        String info = String.format("User display: userName = %s, firsHashtag = %s, secondHashtag = %s, thirdHashtag = %s",
                user.getUserName(), user.getFirstHashtag(), user.getSecondHashtag(), user.getThirdHashtag());
        log.info(info);


        return "statisticsUser";
    }

}




