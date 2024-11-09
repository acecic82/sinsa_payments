CREATE TABLE point_policy (
    id  BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    max_accumulated_point BIGINT not null,
    max_held_point BIGINT not null,
    day_of_expired_date BIGINT
) engine=InnoDB default character set = utf8;

CREATE TABLE free_point (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id bigint,
    point bigint,
    manual boolean default false,
    expired_date DATETIME DEFAULT CURRENT_TIMESTAMP
) engine=InnoDB default character set = utf8;

create INDEX idx_free_point_expired on free_point(expired_date);
create INDEX idx_free_point_member_expired on free_point(member_id, expired_date);

CREATE TABLE free_point_snapshot (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id bigint,
    point_id bigint,
    order_id varchar(255),
    point numeric(38,0) not null,
    approval_key bigint,
    free_point_snapshot_status enum('ACCUMULATED', 'ACCUMULATED_CANCEL', 'APPROVAL', 'CANCEL', 'EXPIRE') NOT NULL
) engine=InnoDB default character set = utf8;

create INDEX idx_free_point_snapshot_1 on free_point_snapshot(point_id);
create INDEX idx_free_point_snapshot_2 on free_point_snapshot(point_id, free_point_snapshot_status);
create INDEX idx_free_point_snapshot_3 on free_point_snapshot(member_id, order_id, free_point_snapshot_status);