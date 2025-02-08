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
package com.maiereni.webapp.sample.model.dao.impl;

import com.maiereni.webapp.sample.model.dao.PersistentLinesDao;
import com.maiereni.webapp.sample.model.pojo.Line;
import com.maiereni.webapp.sample.model.pojo.Lines;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * @author Petre Maierean
 * @date 1/29/2025 10:13 AM
 **/
@Repository
@Slf4j
public class PersistentLinesDaoImpl implements PersistentLinesDao {
    private static final String DEFAULT_LINES_NAME = "default";
    @PersistenceContext
    EntityManager entityManager;

    /**
     * Get the file record by id
     * @param id null or the id of a file record
     * @return the record object
     */
    @Transactional
    @Override
    public Lines getLines(String id) {
        Lines lines = null;
        TypedQuery<Lines> query = null;
        if (StringUtils.isNotBlank(id)) {
            query = entityManager.createNamedQuery(Lines.GET_LINES_BY_ID, Lines.class);
            query.setParameter(Lines.ID, UUID.fromString(id));
        }
        else {
            query = entityManager.createNamedQuery(Lines.GET_LINES_FOR_NAME, Lines.class);
            query.setParameter(Lines.LINES_NAME, DEFAULT_LINES_NAME);
        }
        lines = query.getSingleResult();
        return lines;
    }
    /**
     * Create a new file record
     * @param name the name of the new file record
     * @return the new record
     */
    @Transactional
    @Override
    public Lines createFile(String name) {
        Assert.notNull(name, "Name must not be null");
        Lines lines = getLinesByName(name);
        if (lines == null) {
            lines = new Lines();
            lines.setName(name);
            lines.setLine(new HashSet<>());
            entityManager.persist(lines);
        }
        return lines;
    }
    /**
     * Get all files names
     * @return a list of file name
     */
    @Transactional
    @Override
    public List<String> getFiles() {
        TypedQuery<String> query = entityManager.createNamedQuery(Lines.GET_LINES_NAMES, String.class);
        return query.getResultList();
    }
    /**
     * Get the lines that are available in the file
     * @param fileName the file name
     * @param keyPattern a pattern to match keys with. If null or .* then all lines are matched
     * @return a list of lines
     */
    @Transactional
    @Override
    public List<Line> getLines(String fileName, String keyPattern) {
        String s = StringUtils.isBlank(fileName) ? DEFAULT_LINES_NAME : fileName;
        TypedQuery<Line> r = null;
        if (StringUtils.isBlank(keyPattern) || keyPattern.equals(".*")) {
            r = entityManager.createNamedQuery(Line.LINE_BY_FILE_NAME, Line.class);
            r.setParameter(Lines.LINES_NAME, s);
        }
        else {
            r = entityManager.createNamedQuery(Line.LINE_BY_FILE_NAME_AND_NAME, Line.class);
            r.setParameter(Lines.LINES_NAME, s);
            r.setParameter(Line.LINE_NAME, "%" + keyPattern + "%");
        }
        return r.getResultList();
    }

    /**
     * Add or update a line record
     * @param line the line
     */
    @Transactional
    @Override
    public void addOrUpdateLine(Line line) {
        Assert.notNull(line, "line must not be null");
        Assert.notNull(line.getLines(), "Lines must not be null");
        if (line.getId() == null) {
            entityManager.persist(line);
        }
        else {
            entityManager.merge(line);
        }
    }
    /**
     * Get a line by id
     * @param id the id of a line record
     * @return the line object
     */
    @Transactional
    @Override
    public Line getLine(String id) {
        Assert.notNull(id, "id must not be null");
        TypedQuery<Line> query = entityManager.createNamedQuery(Line.LINE_BY_ID, Line.class);
        query.setParameter(Line.LINE_ID, UUID.fromString(id));
        return query.getSingleResult();
    }
    /**
     * Delete a line record
     * @param line the line record to delete
     */
    @Transactional
    @Override
    public void deleteLine(Line line) {
        Assert.notNull(line, "line must not be null");
        entityManager.remove(line);
    }

    /**
     * Get a position at the end of the file
     * @param linesId identifies the lines object
     * @return a number
     */
    @Transactional
    @Override
    public Integer getEndPosition(String linesId) {
        UUID id = null;
        if (StringUtils.isBlank(linesId)) {
            Lines lines = getLinesByName(DEFAULT_LINES_NAME);
            id = lines.getId();
        }
        else {
            id = UUID.fromString(linesId);
        }
        TypedQuery<Integer> query = entityManager.createNamedQuery(Line.GET_MAX_POSITION_IN_LINES, Integer.class);
        query.setParameter(Lines.ID, id);
        return query.getSingleResult();
    }

    private Lines getLinesByName(String name) {
        TypedQuery<Lines> query = entityManager.createNamedQuery(Lines.GET_LINES_FOR_NAME, Lines.class);
        query.setParameter(Lines.LINES_NAME, name);
        return query.getSingleResult();
    }
}
