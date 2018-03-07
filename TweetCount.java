package formsubmission.model;

public class TweetCount {
    private int count;
    private String hashtag;


    public TweetCount(int count, String hashtag) {
        this.count = count;
        this.hashtag = hashtag;
    }

    public TweetCount() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
}
