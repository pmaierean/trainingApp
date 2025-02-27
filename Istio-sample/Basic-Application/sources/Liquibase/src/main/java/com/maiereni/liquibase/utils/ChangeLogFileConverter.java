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
package com.maiereni.liquibase.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.Assert;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;


/**
 * Utility class to convert the changelog.xml file into a changelog.sql by piecing together all referred sql files
 * into one large one maintaining their grouping by baseline in the former definition file.
 *
 * @author Petre Maierean
 * @date 2/25/2025 5:27 AM
 **/
@Log4j2
public class ChangeLogFileConverter {
    public static final String SOURCE_FILE = "/changelog.xml";
    public static final String DEFAULT_DESTINATION = "changelogs.sql";
    private static final String HEADER = "--liquibase formatted sql\r\n";
    private static final String CHANGE_SET_TEMPLATE = "--changeset %s:%s%s\r\n";
    private static final String ID_ATTR = "id";
    private static final String AUTHOR_ATTR = "author";
    private static final String SQL_FILE = "sqlFile";
    private static final String PATH_ATTR = "path";
    private static final List<String> IGNORABLE = Arrays.asList(new String[] {
        "endDelimiter", "path", "relativeToChangelogFile"
    });
    private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    /**
     * Method to convert the changelog.xml file among resources into the destination file
     *
     * @param destination a file path
     * @throws Exception thrown for a failure to process
     */
    public void processConversion(String destination) throws Exception {
        Assert.requireNonEmpty(destination, "Destination cannot be empty");
        File file = new File(destination);
        File parentFolder = file.getParentFile();
        if (!(parentFolder== null || parentFolder.exists())) {
            if (!parentFolder.mkdirs()) {
                throw new Exception("Cannot create folder " + parentFolder.getAbsolutePath());
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HEADER);
        try (InputStream is = getClass().getResourceAsStream(SOURCE_FILE);) {
            Assert.requireNonEmpty(is, "Source file not found");
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            Element root = document.getDocumentElement();
            NodeList nl = root.getElementsByTagName("changeSet");
            if (nl.getLength() == 0) {
                throw new Exception("No changeSet elements found");
            }
            for (int i = 0; i < nl.getLength(); i++) {
                Element element = (Element) nl.item(i);
                stringBuilder.append(convertChangeSet(element));
            }
        }
        FileUtils.writeStringToFile(file, stringBuilder.toString(), Charset.defaultCharset());
        log.info("Finished processing the changelog.xml. The result file is {}", file.getAbsolutePath());
    }

    private String convertChangeSet(Element changeSet) throws Exception {
        StringBuilder result = new StringBuilder();
        result.append(getCommentLine(changeSet));
        NodeList nl = changeSet.getElementsByTagName(SQL_FILE);
        for (int i = 0; i < nl.getLength(); i++) {
            Element element = (Element) nl.item(i);
            String content = getSourceFileContent(element);
            if (StringUtils.isNotBlank(content)) {
                result.append("\r\n");
                result.append(content);
            }
        }
        return result.toString();
    }

    protected String getSourceFileContent(Element sqlFile) throws Exception {
        String relativePath = sqlFile.getAttribute(PATH_ATTR);
        if (StringUtils.isNotBlank(relativePath)) {
            try (InputStream is = getClass().getResourceAsStream("/" + relativePath);
                 InputStreamReader isr = new InputStreamReader(is);
                 LineNumberReader lineNumberReader = new LineNumberReader(isr)) {
                Assert.requireNonEmpty(is, "Source file " + relativePath + " not found");
                log.info("Add file content to sql {}", relativePath);
                StringBuilder result = new StringBuilder();
                for (String line = null; (line = lineNumberReader.readLine()) != null; ) {
                    String s = line.trim();
                    if (!(s.startsWith("/*") || s.startsWith("*") || s.startsWith("-"))) {
                        result.append(line).append("\r\n");
                    }
                }
                return result.toString();
            }
        }
        throw new Exception("Source file not found");
    }

    protected String getCommentLine(Element changeSet) {
        StringBuilder processingAttributes = new StringBuilder();
        NamedNodeMap attributes= changeSet.getAttributes();
        String id = "Sample";
        String author = "Mr. John";
        for(int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            String name = attribute.getNodeName();
            String value = attribute.getTextContent();
            if (name.equals(ID_ATTR)) {
                id = value;
            } else if (name.equals(AUTHOR_ATTR)) {
                author = value;
            } else if (!IGNORABLE.contains(name)) {
                processingAttributes.append(" ");
                processingAttributes.append(name).append(":").append(value);
            }
        }
        return String.format(CHANGE_SET_TEMPLATE, author, id, processingAttributes.toString());
    }

    public static void main(String[] args) {
        try {
            String destinationFile = args.length > 0 ? args[0] : DEFAULT_DESTINATION;
            new ChangeLogFileConverter().processConversion(destinationFile);
        }
        catch (Exception e) {
            log.error("Failed to process", e);
        }
    }
}
