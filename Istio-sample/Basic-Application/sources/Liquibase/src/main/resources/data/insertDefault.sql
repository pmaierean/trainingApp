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
do $$
begin
        insert into user_file (id, name, creation_date) values ('0d33477c-c912-4b5a-8c5e-1a0ad661e984', 'default', now());
        insert into file_line (id, "key", "value", "position", user_file_id) values ('3aba3703-c310-4253-979d-acc37c2a87ac', 'one', 'the value of one', 0,'0d33477c-c912-4b5a-8c5e-1a0ad661e984');
end$$;
