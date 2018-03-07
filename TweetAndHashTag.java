package formsubmission.model;


public class TweetAndHashTag {

    private MyTweet tweet;
    private String hashTag;

    public TweetAndHashTag(MyTweet tweet, String hashTag) {
        this.tweet = tweet;
        this.hashTag = hashTag;
    }

    public MyTweet getTweet() {
        return tweet;
    }

    public void setTweet(MyTweet tweet) {
        this.tweet = tweet;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }
}
