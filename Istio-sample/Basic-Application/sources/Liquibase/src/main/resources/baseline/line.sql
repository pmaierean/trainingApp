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
-- DROP TABLE IF EXISTS public.file_line;

CREATE TABLE IF NOT EXISTS public.file_line
(
    id uuid NOT NULL,
    key character varying(256) COLLATE pg_catalog."default" NOT NULL,
    value character varying(256) COLLATE pg_catalog."default" NOT NULL,
    position integer default 0,
    user_file_id uuid NOT NULL,
    CONSTRAINT file_line_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user_file_id FOREIGN KEY (user_file_id)
        REFERENCES public.user_file (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.file_line
    OWNER to ${USER_NAME};

--alter table public.file_line owner to postgres;

