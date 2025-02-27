--liquibase formatted sql
--changeset Petre:1

CREATE TABLE public.user_file
(
    id uuid NOT NULL,
    name character varying(64) NOT NULL,
    creation_date date NOT NULL,
    PRIMARY KEY (id)
)
TABLESPACE pg_default;

ALTER TABLE public.user_file OWNER to ${USER_NAME};



CREATE TABLE public.file_line
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

ALTER TABLE IF EXISTS public.file_line OWNER to ${USER_NAME};


--changeset Petre:2 runInTransaction:true

do $$
begin
        insert into public.user_file (id, name, creation_date) values ('0d33477c-c912-4b5a-8c5e-1a0ad661e984', 'default', now());
        insert into public.file_line (id, "key", "value", "position", user_file_id) values ('3aba3703-c310-4253-979d-acc37c2a87ac', 'one', 'the value of one', 0,'0d33477c-c912-4b5a-8c5e-1a0ad661e984');
end$$;
