package com.dancemind.urlshortener.controller;

import com.dancemind.urlshortener.entity.UrlData;
import com.dancemind.urlshortener.service.UrlDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UrlDataController {

    public UrlDataService urlDataService;

    public UrlDataController(UrlDataService urlDataService) {
        this.urlDataService = urlDataService;
    }

    @ApiOperation(value = "Returns all urls data")
    @GetMapping("/urls")
    public List<UrlData> getAllExpenseCategories() {
        return urlDataService.findAllUrlsData();
    }

    @ApiOperation(value = "Creates new short url for long url")
    @PostMapping("/urls")
    public UrlData createUrlData(@Valid @RequestBody UrlData urlData) {
        return urlDataService.createUrlData(urlData);
    }

    @ApiOperation(value = "Gets long url by short url")
    @GetMapping("/{shortUrl}")
    public UrlData getUrlData(@PathVariable(name = "shortUrl") String shortUrl) {
        return urlDataService.getUrlData(shortUrl);
    }
}