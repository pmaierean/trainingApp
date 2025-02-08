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
package com.maiereni.webapp.sample.processor.impl;

import com.maiereni.webapp.sample.bo.BaseData;
import com.maiereni.webapp.sample.exception.DataServiceException;
import com.maiereni.webapp.sample.model.dao.PersistentLinesDao;
import com.maiereni.webapp.sample.model.pojo.Line;
import com.maiereni.webapp.sample.model.pojo.Lines;
import com.maiereni.webapp.sample.processor.DataOperationProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * A processor implementation that uses the database persistence to store and retrieved data
 *
 * @author Petre Maierean
 * @date 1/29/2025 11:43 AM
 **/
@Component("dbProcessor")
@Slf4j
public class DataOperationProcessorImpl implements DataOperationProcessor {
    private final PersistentLinesDao linesDao;

    /**
     * Constructor
     * @param linesDao the dao object to use for to persist objects
     */
    public DataOperationProcessorImpl(PersistentLinesDao linesDao) {
        this.linesDao = linesDao;
    }

    /**
     * Persists a new data object
     * @param data the object to be stored
     * @return the id of the object persisted
     * @throws DataServiceException invalid parameters passed
     */
    @Override
    public String addData(BaseData data) throws DataServiceException {
        try {
            Assert.notNull(data, "date must not be null");
            if (data.getKey() == null || data.getValue() == null) {
                throw new IllegalArgumentException("key and value must not be null");
            }
            Line line = convertAsNew(data);
            linesDao.addOrUpdateLine(line);
            log.debug("A line has been added new {}", line.getId().toString());
            return line.getId().toString();
        }
        catch (Exception e) {
            throw new DataServiceException("Failed to add data", e);
        }
    }

    /**
     * Update a data object
     * @param data the data to be updated
     * @return the id of the persisted object
     * @throws DataServiceException invalid arguments or a failure to persist
     */
    @Override
    public String updateData(BaseData data) throws DataServiceException {
        try {
            Assert.notNull(data, "data object must not be null");
            Assert.notNull(data.getFId(), "the file id of the data must not be null");
            Assert.notNull(data.getId(), "the id of the data must not be null");
            if (data.getKey() == null || data.getValue() == null) {
                throw new IllegalArgumentException("key and value must not be null");
            }
            Line line = convertExisting(data);
            linesDao.addOrUpdateLine(line);
            log.debug("A line has been updated {}", line.getId().toString());
            return line.getId().toString();
        }
        catch (Exception e) {
            throw new DataServiceException("Failed to add data", e);
        }
    }

    /**
     * Deletes a record from the persistence
     * @param data the data to be removed
     * @return the id of the data removed
     * @throws DataServiceException wrong argument or a failure to process
     */
    @Override
    public String deleteData(BaseData data) throws DataServiceException {
        try {
            Assert.notNull(data, "data object must not be null");
            Assert.notNull(data.getFId(), "the file id of the data must not be null");
            Assert.notNull(data.getId(), "the id of the data must not be null");
            if (data.getKey() == null || data.getValue() == null) {
                throw new IllegalArgumentException("key and value must not be null");
            }
            Line line = linesDao.getLine(data.getId());
            linesDao.deleteLine(line);
            log.debug("A line has been deleted {}", line.getId().toString());
            return line.getId().toString();
        }
        catch (Exception e) {
            throw new DataServiceException("Failed to add data", e);
        }
    }

    /**
     * Find all lines by the key pattern. The argument can be null in which case the function returns all records for
     * the default file. Otherwise, if the argument contains two tokens separated by a column, the first is interpreted
     * as the name or the id of the file, and the second as the key pattern for the selection of data
     *
     * @param keyPattern the pattern for finding data
     * @return a list of objects
     * @throws DataServiceException a failure to process
     */
    @Override
    public List<BaseData> findData(String keyPattern) throws DataServiceException {
        try {
            String fileName = null, selector = null;
            if (StringUtils.isNotBlank(keyPattern)) {
                String[] tokens = keyPattern.split("\\:");
                if (tokens.length > 1) {
                    fileName = tokens[0];
                    selector = tokens[1];
                } else {
                    selector = tokens[0];
                }
            }
            final List<BaseData> result = new ArrayList<>();
            List<Line> lines = linesDao.getLines(fileName, selector);
            lines.forEach(line -> {
                result.add(convertExisting(line));
            });
            result.sort((c1, c2) -> c1.getPosition().compareTo(c2.getPosition()));
            log.debug("A number of {} have been found for the query {}", result.size(), keyPattern);
            return result;
        }
        catch (Exception e) {
            throw new DataServiceException("Failed to add data", e);
        }
    }

    private BaseData convertExisting(Line line) {
        BaseData data = new BaseData();
        data.setKey(line.getKey());
        data.setValue(line.getValue());
        data.setPosition(line.getPosition());
        data.setId(line.getId().toString());
        data.setFId(line.getLines().getId().toString());
        return data;
    }

    private Line convertExisting(BaseData data) {
        Line line = linesDao.getLine(data.getId());
        line.setKey(data.getKey());
        line.setValue(data.getValue());
        if (data.getPosition() != null) {
            line.setPosition(data.getPosition());
        }
        return line;
    }

    private Line convertAsNew(BaseData data) {
        Lines lines = null;
        if (StringUtils.isBlank(data.getFId())) {
            lines = linesDao.getLines(null);
        } else {
            lines = linesDao.getLines(data.getFId());
        }

        Line line = new Line();
        line.setKey(data.getKey());
        line.setValue(data.getValue());
        if (data.getPosition() == null) {
            Integer max = linesDao.getEndPosition(lines.getId().toString());
            line.setPosition(max + 1);
        }
        else {
            line.setPosition(data.getPosition());
        }
        line.setLines(lines);
        return line;
    }

}
