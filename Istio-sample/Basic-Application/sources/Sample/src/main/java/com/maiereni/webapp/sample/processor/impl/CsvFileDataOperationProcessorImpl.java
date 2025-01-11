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
import com.maiereni.webapp.sample.processor.DataOperationProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An implementation of the DataOperationProcessor which uses a CSV file as a persistent storage for data
 * @author Petre Maierean
 * @date 1/7/2025 12:56 PM
 **/
@Component
@Slf4j
public class CsvFileDataOperationProcessorImpl implements DataOperationProcessor {
    public static final String FILE_DATA_PATH = "file.data.path";
    private static final String MISSING_PERSISTENT_FILE_PATH = "Missing persistent file path";
    private static final String CANNOT_MAKE_FOLDER = "Cannot make folder";
    private static final String DATA_IS_NULL = "data is null";
    private static final String DATA_IS_INVALID = "data missing key or value";
    private static final String DATA_ID_NOT_NULL = "data id must be null";
    private static final String DATA_ID_IS_NULL = "data id must not be null";
    private static final String TEMPLATE = "%s,\"%s\",\"%s\"\r\n";
    private File csvFile;

    public CsvFileDataOperationProcessorImpl() throws DataServiceException {
        String filePath = System.getenv(FILE_DATA_PATH);
        if (StringUtils.isBlank(filePath)) {
            throw new DataServiceException(MISSING_PERSISTENT_FILE_PATH);
        }
        csvFile = new File(filePath);
        if (!csvFile.exists()) {
            if (!csvFile.getParentFile().exists()) {
                if (!csvFile.getParentFile().mkdirs()) {
                    throw new DataServiceException(CANNOT_MAKE_FOLDER);
                }
            }
        }
    }

    /**
     * Add a new record
     * @param data the record to add
     * @throws DataServiceException failure to add
     */
    @Override
    public String addData(BaseData data) throws DataServiceException {
        if (data == null) {
            throw new DataServiceException(DATA_IS_NULL);
        }
        if (data.getKey() == null || data.getValue() == null) {
            throw new DataServiceException(DATA_IS_INVALID);
        }
        if (StringUtils.isNotBlank(data.getId())) {
            throw new DataServiceException(DATA_ID_NOT_NULL);
        }
        try(StringWriter writer = new StringWriter()) {
            if (csvFile.exists()) {
                writer.write(FileUtils.readFileToString(csvFile, Charset.defaultCharset()));
            }
            String id = UUID.randomUUID().toString();
            String line = toLine(id, data);
            writer.write(line);
            log.debug("Write '{}' to the file", line);
            FileUtils.writeStringToFile(csvFile, writer.toString(), Charset.defaultCharset());
            return id;
        }
        catch (Exception e) {
            log.error("Failed to persist", e);
            throw new DataServiceException(DATA_IS_INVALID);
        }
    }

    /**
     * Update a record
     * @param data the data to update
     * @throws DataServiceException failed to update
     */
    @Override
    public String updateData(BaseData data) throws DataServiceException {
        if (data == null) {
            throw new DataServiceException(DATA_IS_NULL);
        }
        if (StringUtils.isAllBlank(data.getKey(), data.getValue())) {
            throw new DataServiceException(DATA_IS_INVALID);
        }
        if (data.getId() == null) {
            throw new DataServiceException(DATA_ID_IS_NULL);
        }
        change(data, false);
        return data.getId();
    }

    /**
     * Delete a record
     * @param data the data to delete
     * @throws DataServiceException failed to delete data
     */
    @Override
    public String deleteData(BaseData data) throws DataServiceException {
        if (data == null) {
            throw new DataServiceException(DATA_IS_NULL);
        }
        if (data.getId() == null) {
            throw new DataServiceException(DATA_ID_IS_NULL);
        }
        change(data, true);
        return data.getId();
    }

    /**
     * Find values in file
     * @param keyPattern
     * @return
     * @throws DataServiceException
     */
    @Override
    public List<BaseData> findData(String keyPattern) throws DataServiceException {
        boolean omit = StringUtils.isBlank(keyPattern);
        List<BaseData> dataList = new ArrayList<>();
        try(FileReader reader = new FileReader(csvFile);
            LineNumberReader lnr = new LineNumberReader(reader)) {
            String line = null;
            while ((line = lnr.readLine()) != null) {
                String[] split = line.split(",");
                if (split.length == 3) {
                    String key = trimQuotes(split[1]);
                    if (omit || key.matches(keyPattern)) {
                        BaseData data = new BaseData();
                        data.setId(split[0]);
                        data.setKey(key);
                        data.setValue(trimQuotes(split[2]));
                        dataList.add(data);
                    }
                }
            }
            log.debug("Found {} data", dataList.size());
        }
        catch (Exception e) {
            log.error("Failed to persist", e);
        }
        return dataList;
    }

    private String trimQuotes(String s) {
        return s.startsWith("\"")? s.substring(1, s.length() - 1) : s;
    }

    private void change(BaseData data, boolean omit) throws DataServiceException {
        try(StringWriter writer = new StringWriter();
            FileReader reader = new FileReader(csvFile);
            LineNumberReader lnr = new LineNumberReader(reader)) {
            String line = null;
            int count = 0;
            while ((line = lnr.readLine()) != null) {
                count++;
                String[] split = line.split(",");
                if (split.length > 0 && split[0].equals(data.getId())) {
                    if (omit) {
                        log.debug("Data was omitted in line {}", count);
                    }
                    else {
                        String s = toLine(data);
                        writer.write(s);
                        log.debug("Data was updated in line {} with {}", count, s);
                    }
                    continue;
                }
                writer.write(line);
                writer.write(System.lineSeparator());
            }
            FileUtils.writeStringToFile(csvFile, writer.toString(), Charset.defaultCharset());
        }
        catch (Exception e) {
            log.error("Failed to persist", e);
        }
    }

    private String toLine(BaseData data) {
        return toLine(data.getId(), data);
    }

    private String toLine(String id, BaseData data) {
        if (StringUtils.isBlank(id)) {
            id = UUID.randomUUID().toString();
        }
        return String.format(TEMPLATE, id, data.getKey(), data.getValue());
    }
}
