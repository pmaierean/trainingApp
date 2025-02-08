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

import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

/**
 * @author Petre Maierean
 * @date 1/29/2025 8:47 AM
 **/
@Entity
@Table(name="user_file")
@NamedQueries({
        @NamedQuery(name = Lines.GET_LINES_NAMES, query = "SELECT name FROM Lines ORDER BY name"),
        @NamedQuery(name = Lines.GET_LINES_BY_ID, query = "SELECT l FROM Lines l WHERE l.id = :" + Lines.ID),
        @NamedQuery(name = Lines.GET_LINES_FOR_NAME, query = "SELECT l FROM Lines l WHERE l.name = :" + Lines.LINES_NAME)
})
@Data
public class Lines {
    public static final String GET_LINES_NAMES = "Lines.names";
    public static final String GET_LINES_FOR_NAME = "Lines.getLineForName";
    public static final String GET_LINES_BY_ID = "Lines.getLineByID";
    public static final String LINES_NAME = "name";
    public static final String ID = "id";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name="name")
    private String name;
    @Column(name="creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creationDate;
    @OneToMany(mappedBy = "lines")
    private Set<Line> line;
}
