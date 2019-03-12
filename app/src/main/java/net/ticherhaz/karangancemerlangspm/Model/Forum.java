package net.ticherhaz.karangancemerlangspm.Model;

public class Forum {
    private String forumUid;
    private String forumTitle;
    private long forumUserViewing;
    private String forumDescription;
    private long forumViews;
    private long threads;
    private long postThreadsCount;
    private String lastThreadPost;
    private String lastThreadByUser;

    public Forum() {
    }

    public Forum(String forumUid, String forumTitle, long forumUserViewing, String forumDescription, long forumViews, long threads, long postThreadsCount, String lastThreadPost, String lastThreadByUser) {
        this.forumUid = forumUid;
        this.forumTitle = forumTitle;
        this.forumUserViewing = forumUserViewing;
        this.forumDescription = forumDescription;
        this.forumViews = forumViews;
        this.threads = threads;
        this.postThreadsCount = postThreadsCount;
        this.lastThreadPost = lastThreadPost;
        this.lastThreadByUser = lastThreadByUser;
    }


    public String getForumUid() {
        return forumUid;
    }

    public void setForumUid(String forumUid) {
        this.forumUid = forumUid;
    }

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public long getForumUserViewing() {
        return forumUserViewing;
    }

    public void setForumUserViewing(long forumUserViewing) {
        this.forumUserViewing = forumUserViewing;
    }

    public String getForumDescription() {
        return forumDescription;
    }

    public void setForumDescription(String forumDescription) {
        this.forumDescription = forumDescription;
    }

    public long getForumViews() {
        return forumViews;
    }

    public void setForumViews(long forumViews) {
        this.forumViews = forumViews;
    }

    public long getThreads() {
        return threads;
    }

    public void setThreads(long threads) {
        this.threads = threads;
    }

    public long getPostThreadsCount() {
        return postThreadsCount;
    }

    public void setPostThreadsCount(long postThreadsCount) {
        this.postThreadsCount = postThreadsCount;
    }

    public String getLastThreadPost() {
        return lastThreadPost;
    }

    public void setLastThreadPost(String lastThreadPost) {
        this.lastThreadPost = lastThreadPost;
    }

    public String getLastThreadByUser() {
        return lastThreadByUser;
    }

    public void setLastThreadByUser(String lastThreadByUser) {
        this.lastThreadByUser = lastThreadByUser;
    }

}
