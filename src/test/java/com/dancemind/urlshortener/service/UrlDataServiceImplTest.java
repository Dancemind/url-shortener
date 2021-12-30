package com.dancemind.urlshortener.service;

import com.dancemind.urlshortener.entity.UrlData;
import com.dancemind.urlshortener.repository.UrlDataRepository;
import com.dancemind.urlshortener.service.exceptions.NoAvailableLettersException;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlDataServiceImplTest {

    private static final int STRING_LENGTH = 10;

    private static final String INITIAL_SHORT_URL = "aaaaaa";
    private static final String SHORT_URL = "aazZZZ";
    private static final String NEXT_SHORT_URL = "aaAaaa";
    private static final String LAST_SHORT_URL = "ZZZZZZ";

    private static final int NO_INVOCATIONS = 0;

    @Mock
    private UrlDataRepository urlDataRepository;

    private UrlDataService urlDataService;

    private final List<UrlData> urlDataList = new ArrayList<>();
    private UrlData urlData;

    @BeforeEach
    public void init() {
        urlDataService = new UrlDataServiceImpl(urlDataRepository);
        urlData = createUrlDataInstance();
        Collections.addAll(urlDataList, urlData, createUrlDataInstance());
    }

    @Test
    public void findAllUrlsData_Success() {
        when(urlDataRepository.findAllByDeletedFalse()).thenReturn(urlDataList);

        List<UrlData> gotUrlDataList = urlDataService.findAllUrlsData();

        verify(urlDataRepository).findAllByDeletedFalse();
        assertEquals(gotUrlDataList, urlDataList);
    }

    @Test
    public void createUrlData_Success() {
        urlDataService.createUrlData(urlData);
        verify(urlDataRepository).save(urlData);
    }

    @Test
    public void createUrlData_Capture() {
        ArgumentCaptor<UrlData> captor = ArgumentCaptor.forClass(UrlData.class);

        urlDataService.createUrlData(urlData);

        verify(urlDataRepository).save(captor.capture());

        UrlData gotUrlData = captor.getValue();
        assertEquals(urlData.getComment(), gotUrlData.getComment());
    }

    @Test
    public void createUrlData_null_aaaaaa() {
        urlDataService.createUrlData(urlData);
        assertEquals(INITIAL_SHORT_URL, urlData.getShortUrl());
    }

    @Test
    public void createUrlData_aazZZZ_aaAaaa() {
        urlData.setShortUrl(SHORT_URL);
        UrlData urlData2 = createUrlDataInstance();

        when(urlDataRepository.findFirstByDeletedFalseOrderByIdDesc()).thenReturn(urlData);

        urlDataService.createUrlData(urlData2);

        assertEquals(NEXT_SHORT_URL, urlData2.getShortUrl());
    }

    @Test
    public void createUrlData_ZZZZZZ_lastShortUrl() {
        urlData.setShortUrl(LAST_SHORT_URL);
        UrlData urlData2 = createUrlDataInstance();

        when(urlDataRepository.findFirstByDeletedFalseOrderByIdDesc()).thenReturn(urlData);

        assertThatThrownBy(() -> urlDataService.createUrlData(urlData2))
                .isInstanceOf(NoAvailableLettersException.class);
        verify(urlDataRepository, times(NO_INVOCATIONS)).save(any());
    }

    private UrlData createUrlDataInstance() {
        return new UrlData(generateString(), generateString(), generateString());
    }

    protected String generateString() {
        return RandomStringUtils.randomAlphabetic(STRING_LENGTH);
    }
}