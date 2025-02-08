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
do
$$
    declare
        fileId uuid := gen_random_uuid();
        recordId uuid := gen_random_uuid();
    begin
        -- DELETE FROM file_line;
        -- DELETE FROM user_file;

        insert into user_file (id, name, creation_date) values (fileId, 'default', now());
        insert into file_line (id, "key", "value", "position", user_file_id) values (recordId, 'one', 'the value of one', 0,fileId);

    end;
$$ language PLPGSQL;
