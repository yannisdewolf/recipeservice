DROP table if exists ingredient;
DROP TABLE IF EXISTS recipe;

create table recipe
(
    id               varchar(36) primary key not null,
    name             varchar(255)            not null,
    extra_info       varchar(255),
    deleted          boolean                          default false,
    url              varchar(255),
    created_on       timestamp               not null,
    last_modified_on timestamp               not null,
    version          integer                 not null default 0
);

create table ingredient
(
    id        varchar(36) primary key not null,
    name      varchar(255)            not null,
    recipe_id varchar(36)             not null,
    constraint fk_int_ree
        foreign key (recipe_id) references recipe (id)
);

create index ix_int_recipe_id on ingredient (recipe_id);