package com.simple.server.service;

import java.util.List;

import com.simple.server.domain.contract.BusClassificator;
import com.simple.server.domain.contract.BusTagTemplate;

public interface IReaderService {
	List<BusTagTemplate> getAllTags() throws Exception;
	List<BusClassificator> getAllClassificator() throws Exception;
	List<BusClassificator> getClassificatorBySqlCriteria(String sql) throws Exception;
}
