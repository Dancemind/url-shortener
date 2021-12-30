package com.dancemind.urlshortener.service;

import com.dancemind.urlshortener.entity.UrlData;

import java.util.List;

public interface UrlDataService {

    List<UrlData> findAllUrlsData();
    UrlData createUrlData(UrlData urlData);

    UrlData getUrlData(String shortUrl);
}