# Tasks schema

# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
    id integer NOT NULL DEFAULT nextval('task_id_seq'),
    label varchar(255),
    email varchar(64)
);

create table user (
  email                     varchar(255) not null primary key,
  name                      varchar(255) not null,
  password                  varchar(255) not null
);

# --- !Downs

DROP TABLE task;
DROP SEQUENCE task_id_seq;
drop table user;