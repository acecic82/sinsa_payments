DROP TABLE IF EXISTS point_policy;

CREATE TABLE point_policy (
                         id bigint generated by default as identity,
                         max_accumulated_point numeric(38,2) not null,
                         max_held_point numeric(38,2) not null,
                         primary key (id)
);

DROP TABLE IF EXISTS free_point;

CREATE TABLE free_point (
                        id bigint generated by default as identity,
                        member_id bigint,
                        point numeric(38,2) not null,
                        manual boolean default false,
                        expired_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                        primary key (id)
);

-- create INDEX idx_free_point_expired on free_point(expired_date);

DROP TABLE IF EXISTS free_point_snapshot;

CREATE TABLE free_point_snapshot (
                        id bigint generated by default as identity,
                        pointId bigint,
                        orderId varchar(255) not null,
                        point numeric(38,2) not null,
                        primary key (id)
);