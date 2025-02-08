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
create table if not exists public.file_line
(
    id           uuid              not null
    constraint file_line_pk
    primary key,
    key          varchar(256)      not null,
    value        varchar(256)      not null,
    position     integer default 0 not null,
    user_file_id uuid              not null
);

comment on column public.file_line.position is 'position';

alter table public.file_line
    owner to postgres;

