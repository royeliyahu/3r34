package formsubmission.model;

import org.springframework.social.twitter.api.Tweet;

import java.util.Date;

public class MyTweet{
    private String text;
    private Date createdAt;
    private String fromUser;
    private String hashTag;

    public MyTweet(String text, Date createdAt, String fromUser, String hashTag) {
        this.text = text;
        this.createdAt = createdAt;
        this.fromUser = fromUser;
        this.hashTag = hashTag;
    }

    public MyTweet(){

    }

    public MyTweet(Tweet original, String hashTag){
        this.createdAt = original.getCreatedAt();
        this.fromUser = original.getFromUser();
        this.text = original.getText();
        this.hashTag = hashTag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    @Override
    public String toString() {
        return "MyTweet{" +
                "text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", fromUser='" + fromUser + '\'' +
                ", hashTag='" + hashTag + '\'' +
                '}';
    }
}
