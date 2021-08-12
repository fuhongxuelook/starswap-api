create table pulling_event_task
    (from_block_number decimal(21,0) not null,
    created_at bigint not null,
    created_by varchar(70) not null,
    status varchar(20) not null,
    to_block_number decimal(21,0) not null,
    updated_at bigint not null,
    updated_by varchar(70) not null,
    primary key (from_block_number))
    engine=InnoDB;

