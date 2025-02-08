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
package com.maiereni.webapp.sample.model.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * @author Petre Maierean
 * @date 1/29/2025 8:32 AM
 **/
@Entity
@Table(name="file_line")
@NamedQueries({
        @NamedQuery(name = Line.LINE_BY_FILE_NAME, query = "SELECT l FROM Line l WHERE l.lines.name = :" + Lines.LINES_NAME),
        @NamedQuery(name = Line.LINE_BY_FILE_NAME_AND_NAME, query = "SELECT l FROM Line l WHERE l.lines.name = :" + Lines.LINES_NAME + " AND l.key like :" + Line.LINE_NAME),
        @NamedQuery(name = Line.LINE_BY_ID, query = "SELECT l FROM Line l WHERE l.id = :" + Line.LINE_ID),
        @NamedQuery(name = Line.GET_MAX_POSITION_IN_LINES, query = "SELECT max(l.position) FROM Line l WHERE l.lines.id = :" + Lines.ID),
})
@Data
public class Line {
    public static final String LINE_BY_FILE_NAME = "Line.byFileName";
    public static final String LINE_BY_FILE_NAME_AND_NAME = "Line.byFileNameAandName";
    public static final String GET_MAX_POSITION_IN_LINES = "Line.getMaxPositionInLines";
    public static final String LINE_BY_ID = "Line.byId";
    public static final String LINE_NAME = "LineName";
    public static final String LINE_ID = "LineId";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name="key")
    private String key;
    @Column(name="value")
    private String value;
    @Column(name="position")
    private Integer position;
    @ManyToOne
    @JoinColumn(name="user_file_id")
    private Lines lines;
}
