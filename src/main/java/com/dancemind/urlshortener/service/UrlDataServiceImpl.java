package com.dancemind.urlshortener.service;

import com.dancemind.urlshortener.entity.UrlData;
import com.dancemind.urlshortener.repository.UrlDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Provides methods for {@link UrlData} management
 */
@Service
public class UrlDataServiceImpl implements UrlDataService {

    public UrlDataRepository urlDataRepository;

    public UrlDataServiceImpl(UrlDataRepository urlDataRepository) {
        this.urlDataRepository = urlDataRepository;
    }

    /**
     * Gets data for all urls
     * @return  list of UrlData
     */
    @Override
    @Transactional(readOnly = true)
    public List<UrlData> findAllUrlsData() {
        return urlDataRepository.findAllByDeletedFalse();
    }
}