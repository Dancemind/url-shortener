package com.dancemind.urlshortener.controller;

import com.dancemind.urlshortener.entity.UrlData;
import com.dancemind.urlshortener.service.UrlDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/urls")
public class UrlDataController {

    public UrlDataService urlDataService;

    public UrlDataController(UrlDataService urlDataService) {
        this.urlDataService = urlDataService;
    }

    @ApiOperation(value = "Returns all urls data")
    @GetMapping
    public List<UrlData> getAllExpenseCategories() {
        return urlDataService.findAllUrlsData();
    }

    @ApiOperation(value = "Creates new short url for long url")
    @PostMapping
    public UrlData createUrlData(@Valid @RequestBody UrlData urlData) {
        return urlDataService.createUrlData(urlData);
    }
}