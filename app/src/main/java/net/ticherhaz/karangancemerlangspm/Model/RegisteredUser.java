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
    private String state;
    private String birthday;
    private String mode;
    private int postCount;
    private int reputation;
    private int reputationPower;
    private String onlineStatus;
    private long lastOnline;
    private String lastCreatedThread;
    private String onDateCreated;
    //BehaviorLog
    private String onlineStatusLogUid;
    private String lastSeenLogUid;
    private String onClickedLogUid;
    private String onCreatedThreadLogUid;
    //UserProfileLog
    private String profileUrlLogUid;
    private String emailLogUid;
    private String usernameLogUid;
    private String titleTypeLogUid;
    private String customTitleLogUid;
    private String bioLogUid;
    private String stateLogUid;
    private String birthdayLogUid;
    private String modeLogUid;
    //UpperCase
    private String emailUpperCase;
    private String usernameUpperCase;
    private String bioUpperCase;
    private String stateUpperCase;

    public RegisteredUser(String registeredUserUid, String userUid, String typeUser, String profileUrl, String email, String username, String sekolah, String titleType, String customTitle, String bio, String state, String birthday, String mode, int postCount, int reputation, int reputationPower, String onlineStatus, long lastOnline, String lastCreatedThread, String onDateCreated, String onlineStatusLogUid, String lastSeenLogUid, String onClickedLogUid, String onCreatedThreadLogUid, String profileUrlLogUid, String emailLogUid, String usernameLogUid, String titleTypeLogUid, String customTitleLogUid, String bioLogUid, String stateLogUid, String birthdayLogUid, String modeLogUid, String emailUpperCase, String usernameUpperCase, String bioUpperCase, String stateUpperCase) {
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
        this.onlineStatusLogUid = onlineStatusLogUid;
        this.lastSeenLogUid = lastSeenLogUid;
        this.onClickedLogUid = onClickedLogUid;
        this.onCreatedThreadLogUid = onCreatedThreadLogUid;
        this.profileUrlLogUid = profileUrlLogUid;
        this.emailLogUid = emailLogUid;
        this.usernameLogUid = usernameLogUid;
        this.titleTypeLogUid = titleTypeLogUid;
        this.customTitleLogUid = customTitleLogUid;
        this.bioLogUid = bioLogUid;
        this.stateLogUid = stateLogUid;
        this.birthdayLogUid = birthdayLogUid;
        this.modeLogUid = modeLogUid;
        this.emailUpperCase = emailUpperCase;
        this.usernameUpperCase = usernameUpperCase;
        this.bioUpperCase = bioUpperCase;
        this.stateUpperCase = stateUpperCase;
    }

    public RegisteredUser() {
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

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public int getReputationPower() {
        return reputationPower;
    }

    public void setReputationPower(int reputationPower) {
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

    public String getOnlineStatusLogUid() {
        return onlineStatusLogUid;
    }

    public void setOnlineStatusLogUid(String onlineStatusLogUid) {
        this.onlineStatusLogUid = onlineStatusLogUid;
    }

    public String getLastSeenLogUid() {
        return lastSeenLogUid;
    }

    public void setLastSeenLogUid(String lastSeenLogUid) {
        this.lastSeenLogUid = lastSeenLogUid;
    }

    public String getOnClickedLogUid() {
        return onClickedLogUid;
    }

    public void setOnClickedLogUid(String onClickedLogUid) {
        this.onClickedLogUid = onClickedLogUid;
    }

    public String getOnCreatedThreadLogUid() {
        return onCreatedThreadLogUid;
    }

    public void setOnCreatedThreadLogUid(String onCreatedThreadLogUid) {
        this.onCreatedThreadLogUid = onCreatedThreadLogUid;
    }

    public String getProfileUrlLogUid() {
        return profileUrlLogUid;
    }

    public void setProfileUrlLogUid(String profileUrlLogUid) {
        this.profileUrlLogUid = profileUrlLogUid;
    }

    public String getEmailLogUid() {
        return emailLogUid;
    }

    public void setEmailLogUid(String emailLogUid) {
        this.emailLogUid = emailLogUid;
    }

    public String getUsernameLogUid() {
        return usernameLogUid;
    }

    public void setUsernameLogUid(String usernameLogUid) {
        this.usernameLogUid = usernameLogUid;
    }

    public String getTitleTypeLogUid() {
        return titleTypeLogUid;
    }

    public void setTitleTypeLogUid(String titleTypeLogUid) {
        this.titleTypeLogUid = titleTypeLogUid;
    }

    public String getCustomTitleLogUid() {
        return customTitleLogUid;
    }

    public void setCustomTitleLogUid(String customTitleLogUid) {
        this.customTitleLogUid = customTitleLogUid;
    }

    public String getBioLogUid() {
        return bioLogUid;
    }

    public void setBioLogUid(String bioLogUid) {
        this.bioLogUid = bioLogUid;
    }

    public String getStateLogUid() {
        return stateLogUid;
    }

    public void setStateLogUid(String stateLogUid) {
        this.stateLogUid = stateLogUid;
    }

    public String getBirthdayLogUid() {
        return birthdayLogUid;
    }

    public void setBirthdayLogUid(String birthdayLogUid) {
        this.birthdayLogUid = birthdayLogUid;
    }

    public String getModeLogUid() {
        return modeLogUid;
    }

    public void setModeLogUid(String modeLogUid) {
        this.modeLogUid = modeLogUid;
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
