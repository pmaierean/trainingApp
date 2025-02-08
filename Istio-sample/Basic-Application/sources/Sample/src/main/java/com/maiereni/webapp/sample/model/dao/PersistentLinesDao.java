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
package com.maiereni.webapp.sample.model.dao;

import com.maiereni.webapp.sample.model.pojo.Line;
import com.maiereni.webapp.sample.model.pojo.Lines;

import java.util.List;

/**
 * @author Petre Maierean
 * @date 1/29/2025 10:06 AM
 **/
public interface PersistentLinesDao {
    /**
     * Get the file record by id
     * @param id null or the id of a file record
     * @return the record object
     */
    Lines getLines(String id);
    /**
     * Create a new file record
     * @param name the name
     * @return the record object newly create
     */
    Lines createFile(String name);
    /**
     * Get the available file names
     * @return a list of file names
     */
    List<String> getFiles();
    /**
     * Get the lines that are available in the file
     * @param fileName the file name. If null, than retrieve the DEFAULT
     * @param keyPattern a pattern to match keys with. If null or .* then all lines are matched
     * @return a list of lines
     */
    List<Line> getLines(String fileName, String keyPattern);
    /**
     * Add or update a line record
     * @param line the line
     */
    void addOrUpdateLine(Line line);
    /**
     * Get a line by id
     * @param id the id of a line record
     * @return the line object
     */
    Line getLine(String id);
    /**
     * Delete a line record
     * @param line the line record to delete
     */
    void deleteLine(Line line);
    /**
     * Get a position at the end of the file
     * @param linesId identifies the lines object
     * @return a number
     */
    Integer getEndPosition(String linesId);
}
