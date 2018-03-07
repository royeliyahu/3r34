package formsubmission.model;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class User {
    public static final Integer NUM_OF_TWITS_TO_DISPLAY = 10;

    private String userName;
    private String firstHashtag;
    private String secondHashtag;
    private String thirdHashtag;
    private Queue<MyTweet> last10Twits = new ArrayBlockingQueue<MyTweet>(NUM_OF_TWITS_TO_DISPLAY);
    private HashMap<String, Integer> hashTagTweetCount = new HashMap<>();

 
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
 
    public String getFirstHashtag() {
        return firstHashtag;
    }
 
    public void setFirstHashtag(String firstHashtag) {
        this.firstHashtag = firstHashtag;
    }
 
    public String getSecondHashtag() {
        return secondHashtag;
    }
 
    public void setSecondHashtag(String secondHashtag) {
        this.secondHashtag = secondHashtag;
    }

    public String getThirdHashtag() {
        return thirdHashtag;
    }

    public void setThirdHashtag(String thirdHashtag) {
        this.thirdHashtag = thirdHashtag;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", firstHashtag='" + firstHashtag + '\'' +
                ", secondHashtag='" + secondHashtag + '\'' +
                ", thirdHashtag='" + thirdHashtag + '\'' +
                '}';
    }

    public int addTweet(MyTweet tweet){
        if(last10Twits.size() == NUM_OF_TWITS_TO_DISPLAY){
            last10Twits.remove();
        }
        last10Twits.add(tweet);
        return last10Twits.size();
    }

    public void addHashTagTweetCount(String hashTag, Integer count){
        if(hashTagTweetCount.containsKey(hashTag)){
            hashTagTweetCount.put(hashTag, hashTagTweetCount.get(hashTag) + count);
        }
        else{
            hashTagTweetCount.put(hashTag, count);
        }
    }

    public Queue<MyTweet> getLast10Twits() {
        return last10Twits;
    }

    public HashMap<String, Integer> getHashTagTweetCount() {
        return hashTagTweetCount;
    }

    public void setLast10Twits(Queue<MyTweet> last10Twits) {
        this.last10Twits = last10Twits;
    }

    public void setHashTagTweetCount(HashMap<String, Integer> hashTagTweetCount) {
        this.hashTagTweetCount = hashTagTweetCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return userName.equals(user.userName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * ((userName == null) ? 0 : userName.hashCode());


        return result;
    }
}