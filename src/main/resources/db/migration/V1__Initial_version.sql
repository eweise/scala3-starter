create table if not exists todo (
         id uuid not null,
         title text not null,
         description text,
         due_date timestamp without time zone,
         is_complete bool default false,
         created_date timestamp without time zone not null,
          CONSTRAINT todo_pkey PRIMARY KEY (id));