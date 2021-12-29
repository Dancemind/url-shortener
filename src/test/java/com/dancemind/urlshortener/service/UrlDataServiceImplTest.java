package com.dancemind.urlshortener.service;

import com.dancemind.urlshortener.entity.UrlData;
import com.dancemind.urlshortener.repository.UrlDataRepository;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlDataServiceImplTest {

    private static final int STRING_LENGTH = 10;

    @Mock
    private UrlDataRepository urlDataRepository;

    private UrlDataService urlDataService;

    private final List<UrlData> urlDataList = new ArrayList<>();

    @BeforeEach
    public void init() {
        urlDataService = new UrlDataServiceImpl(urlDataRepository);
        Collections.addAll(urlDataList, createUrlDataInstance(), createUrlDataInstance());
    }

    @Test
    public void findAllUrlsData_Success() {
        when(urlDataRepository.findAllByDeletedFalse()).thenReturn(urlDataList);

        List<UrlData> gotUrlDataList = urlDataService.findAllUrlsData();

        verify(urlDataRepository).findAllByDeletedFalse();
        assertEquals(gotUrlDataList, urlDataList);
    }

    private UrlData createUrlDataInstance() {
        return new UrlData(generateString(), generateString(), generateString());
    }

    protected String generateString() {
        return RandomStringUtils.randomAlphabetic(STRING_LENGTH);
    }
}