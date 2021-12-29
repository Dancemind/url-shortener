package com.dancemind.urlshortener.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "url_data")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UrlData extends BaseEntity {

    @Column(name = "long_url")
    @NotEmpty
    private String longUrl;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "comment")
    private String comment;

    public UrlData() {
    }

    public UrlData(String longUrl, String shortUrl, String comment) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.comment = comment;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}