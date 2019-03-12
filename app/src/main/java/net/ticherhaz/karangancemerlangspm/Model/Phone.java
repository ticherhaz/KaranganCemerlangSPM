package net.ticherhaz.karangancemerlangspm.Model;

public class Phone {

    private String phoneUid;
    private String board;
    private String brand;
    private String device;
    private String display;
    private String fingerprint;
    private String hardware;
    private String host;
    private String id;
    private String manufacturer;
    private String model;
    private String product;
    private String tags;
    private String time;
    private String type;
    private String unknown;

    private String user;

    public Phone() {
    }

    public Phone(String phoneUid, String board, String brand, String device, String display, String fingerprint, String hardware, String host, String id, String manufacturer, String model, String product, String tags, String time, String type, String unknown, String user) {
        this.phoneUid = phoneUid;
        this.board = board;
        this.brand = brand;
        this.device = device;
        this.display = display;
        this.fingerprint = fingerprint;
        this.hardware = hardware;
        this.host = host;
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.product = product;
        this.tags = tags;
        this.time = time;
        this.type = type;
        this.unknown = unknown;
        this.user = user;
    }

    public String getPhoneUid() {
        return phoneUid;
    }

    public void setPhoneUid(String phoneUid) {
        this.phoneUid = phoneUid;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnknown() {
        return unknown;
    }

    public void setUnknown(String unknown) {
        this.unknown = unknown;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
