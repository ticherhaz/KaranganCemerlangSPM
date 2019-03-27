package net.ticherhaz.karangancemerlangspm.Model;

public class RegisteredUser {

    private String registeredUserUid;
    private String userUid;
    private String typeUser;
    private String profileUrl;
    private String email;
    private String username;
    private String sekolah;
    private String titleType;
    private String customTitle;
    private String bio;
    private String gender;
    private String state;
    private String birthday;
    private String mode;
    private long postCount;
    private long reputation;
    private long reputationPower;
    private String onlineStatus;
    private long lastOnline;
    private String lastCreatedThread;
    private String onDateCreated;
    private String onDateCreatedMonthYear;
    //BehaviorLog
//    private String onlineStatusLogUid;
//    private String lastSeenLogUid;
//    private String onClickedLogUid;
//    private String onCreatedThreadLogUid;
    //UserProfileLog
//    private String profileUrlLogUid;
//    private String emailLogUid;
//    private String usernameLogUid;
//    private String titleTypeLogUid;
//    private String customTitleLogUid;
//    private String bioLogUid;
//    private String stateLogUid;
//    private String birthdayLogUid;
//    private String modeLogUid;
    //UpperCase
    private String emailUpperCase;
    private String usernameUpperCase;
    private String bioUpperCase;
    private String stateUpperCase;

    public RegisteredUser() {
    }

    public RegisteredUser(String registeredUserUid, String userUid, String typeUser, String profileUrl, String email, String username
            , String sekolah, String titleType, String customTitle, String bio, String gender, String state, String birthday, String mode
            , long postCount, long reputation, long reputationPower, String onlineStatus, long lastOnline, String lastCreatedThread
            , String onDateCreated, String onDateCreatedMonthYear, String emailUpperCase, String usernameUpperCase, String bioUpperCase, String stateUpperCase) {
        this.registeredUserUid = registeredUserUid;
        this.userUid = userUid;
        this.typeUser = typeUser;
        this.profileUrl = profileUrl;
        this.email = email;
        this.username = username;
        this.sekolah = sekolah;
        this.titleType = titleType;
        this.customTitle = customTitle;
        this.bio = bio;
        this.gender = gender;
        this.state = state;
        this.birthday = birthday;
        this.mode = mode;
        this.postCount = postCount;
        this.reputation = reputation;
        this.reputationPower = reputationPower;
        this.onlineStatus = onlineStatus;
        this.lastOnline = lastOnline;
        this.lastCreatedThread = lastCreatedThread;
        this.onDateCreated = onDateCreated;
        this.onDateCreatedMonthYear = onDateCreatedMonthYear;
        this.emailUpperCase = emailUpperCase;
        this.usernameUpperCase = usernameUpperCase;
        this.bioUpperCase = bioUpperCase;
        this.stateUpperCase = stateUpperCase;
    }

    public String getRegisteredUserUid() {
        return registeredUserUid;
    }

    public void setRegisteredUserUid(String registeredUserUid) {
        this.registeredUserUid = registeredUserUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSekolah() {
        return sekolah;
    }

    public void setSekolah(String sekolah) {
        this.sekolah = sekolah;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getCustomTitle() {
        return customTitle;
    }

    public void setCustomTitle(String customTitle) {
        this.customTitle = customTitle;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public long getPostCount() {
        return postCount;
    }

    public void setPostCount(long postCount) {
        this.postCount = postCount;
    }

    public long getReputation() {
        return reputation;
    }

    public void setReputation(long reputation) {
        this.reputation = reputation;
    }

    public long getReputationPower() {
        return reputationPower;
    }

    public void setReputationPower(long reputationPower) {
        this.reputationPower = reputationPower;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getLastCreatedThread() {
        return lastCreatedThread;
    }

    public void setLastCreatedThread(String lastCreatedThread) {
        this.lastCreatedThread = lastCreatedThread;
    }

    public String getOnDateCreated() {
        return onDateCreated;
    }

    public void setOnDateCreated(String onDateCreated) {
        this.onDateCreated = onDateCreated;
    }

    public String getOnDateCreatedMonthYear() {
        return onDateCreatedMonthYear;
    }

    public void setOnDateCreatedMonthYear(String onDateCreatedMonthYear) {
        this.onDateCreatedMonthYear = onDateCreatedMonthYear;
    }

    public String getEmailUpperCase() {
        return emailUpperCase;
    }

    public void setEmailUpperCase(String emailUpperCase) {
        this.emailUpperCase = emailUpperCase;
    }

    public String getUsernameUpperCase() {
        return usernameUpperCase;
    }

    public void setUsernameUpperCase(String usernameUpperCase) {
        this.usernameUpperCase = usernameUpperCase;
    }

    public String getBioUpperCase() {
        return bioUpperCase;
    }

    public void setBioUpperCase(String bioUpperCase) {
        this.bioUpperCase = bioUpperCase;
    }

    public String getStateUpperCase() {
        return stateUpperCase;
    }

    public void setStateUpperCase(String stateUpperCase) {
        this.stateUpperCase = stateUpperCase;
    }
}
