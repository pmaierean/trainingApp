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
package com.maiereni.webapp.sample;

/**
 * @author Petre Maierean
 * @date 1/29/2025 6:44 AM
 **/
public class DatabaseConnectionException extends Exception {

    public DatabaseConnectionException(String text) {
        super(text);
    }

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
