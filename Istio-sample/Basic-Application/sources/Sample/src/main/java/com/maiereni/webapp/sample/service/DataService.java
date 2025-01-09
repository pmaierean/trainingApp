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
package com.maiereni.webapp.sample.service;

import com.maiereni.webapp.sample.bo.BaseData;
import com.maiereni.webapp.sample.bo.request.DataRequest;
import com.maiereni.webapp.sample.exception.DataServiceException;

import java.util.List;

/**
 * @author Petre Maierean
 * @date 1/7/2025 11:34 AM
 **/
public interface DataService {
    /**
     * Data to modify
     * @param dataRequest operation to perform
     * @throws DataServiceException a failure
     */
    void saveData(DataRequest dataRequest) throws DataServiceException;

    /**
     * Get all data
     * @return a list of data records
     * @throws DataServiceException a failure to list
     */
    List<BaseData> getAllData() throws DataServiceException;

    /**
     * Find data by key pattern
     * @param keyPattern the key pattern
     * @return a list of data records
     * @throws DataServiceException a failure to retrieve
     */
    List<BaseData> findData(String keyPattern) throws DataServiceException;
}
