create table organization_registration(
    org_id_php int not null,
    org_id_rest int default -1,
    referral_code varchar(64) not null,
    primary key (org_id_php, org_id_rest, referral_code)
);

create table scholarship_acceptance(
    user_id_student int not null,
    user_id_scholarship int not null,
    scholarship_id int not null,
    status enum('accepted', 'rejected'),
    primary key (user_id_student, user_id_scholarship, scholarship_id)
);

create table university(
    rest_uni_id int not null,
    php_uni_id int default -1,
    primary key (rest_uni_id, php_uni_id)
);

create table students(
    user_id int not null,
    rest_uni_id int not null,
    php_uni_id int,
    primary key (user_id, rest_uni_id, php_uni_id),
    foreign key (rest_uni_id) references university(rest_uni_id)
);

create table logging(
    logging_id int not null auto_increment,
    description varchar(255) not null,
    ip_address varchar(16) not null,
    timestamp TIMESTAMP not null default CURRENT_TIMESTAMP,
    primary key (logging_id)
);
