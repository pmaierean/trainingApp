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
create table if not exists public.user_file
(
    id            uuid        not null
    constraint user_file_pk
    primary key,
    name          varchar(64) not null,
    creation_date date        not null
    );

alter table public.user_file
    owner to postgres;
