/*
 * =========================================================================================
 * Copyright (c) 2024 - 2025 to Maiereni Software and Consulting Inc
 * =========================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.maiereni.webapp.sample.service.impl;

import com.maiereni.webapp.sample.bo.BaseData;
import com.maiereni.webapp.sample.bo.request.DataRequest;
import com.maiereni.webapp.sample.bo.request.DataRequestOperation;
import com.maiereni.webapp.sample.exception.DataServiceException;
import com.maiereni.webapp.sample.processor.DataOperationProcessor;
import com.maiereni.webapp.sample.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Petre Maierean
 * @date 1/7/2025 11:57 AM
 **/
@Service
@Slf4j
public class DataServiceImpl implements DataService {
    private final DataOperationProcessor dataOperationProcessor;

    public DataServiceImpl(DataOperationProcessor dataOperationProcessor) {
        this.dataOperationProcessor = dataOperationProcessor;
    }
    /**
     * Data to modify
     * @param dataRequest operation to perform
     * @throws DataServiceException a failure
     */
    @Override
    public void saveData(DataRequest dataRequest) throws DataServiceException {
        if (dataRequest == null) {
            throw new DataServiceException("dataRequest is null");
        }
        if (dataRequest.getOperation() == null || dataRequest.getOperation().equals(DataRequestOperation.ADD)) {
            dataOperationProcessor.addData(dataRequest.getData());
        }
        else if (dataRequest.getOperation().equals(DataRequestOperation.UPDATE)) {
            dataOperationProcessor.updateData(dataRequest.getData());
        }
        else {
            dataOperationProcessor.deleteData(dataRequest.getData());
        }
    }

    /**
     * Get all data
     * @return a list of data records
     * @throws DataServiceException a failure to list
     */
    @Override
    public List<BaseData> getAllData() throws DataServiceException {
        return dataOperationProcessor.findData(".*");
    }

    /**
     * Find data by key pattern
     * @param keyPattern the key pattern
     * @return a list of data records
     * @throws DataServiceException a failure to retrieve
     */
    @Override
    public List<BaseData> findData(String keyPattern) throws DataServiceException {
        log.debug("findData keyPattern: {}", keyPattern);
        return dataOperationProcessor.findData(keyPattern);
    }
}
