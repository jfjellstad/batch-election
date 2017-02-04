create table BATCH (
    NAME varchar(255) not null,
    SCHEDULE timestamp not null
);

alter table BATCH add constraint BA_PK primary key (NAME, SCHEDULE);
