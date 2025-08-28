package net.ticherhaz.karangancemerlangspm.huawei.model;


import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.annotations.DefaultValue;
import com.huawei.agconnect.cloud.database.annotations.Indexes;
import com.huawei.agconnect.cloud.database.annotations.PrimaryKeys;

import java.util.Date;

@PrimaryKeys({"id"})
@Indexes({"bookName:bookName"})
public final class BookInfo extends CloudDBZoneObject {
    private Integer id;

    private String bookName;

    private String author;

    private Double price;

    private String publisher;

    private Date publishTime;

    @DefaultValue(booleanValue = true)
    private Boolean shadowFlag;

    public BookInfo() {
        super(BookInfo.class);
        this.shadowFlag = true;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Boolean getShadowFlag() {
        return shadowFlag;
    }

    public void setShadowFlag(Boolean shadowFlag) {
        this.shadowFlag = shadowFlag;
    }

}
