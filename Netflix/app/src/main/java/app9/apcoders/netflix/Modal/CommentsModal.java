package app9.apcoders.netflix.Modal;

import java.util.Date;

public class CommentsModal {
    String username;
    String commenttext;
    Date commentDate;



    public CommentsModal(String username, String commenttext, Date commentDate) {
        this.username = username;
        this.commenttext = commenttext;
        this.commentDate = commentDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommenttext() {
        return commenttext;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public void setCommenttext(String commenttext) {
        this.commenttext = commenttext;
    }
}
