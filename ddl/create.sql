create table member (
	id varchar(20) not null,
	name varchar(40),
	age integer,
	password varchar(60),
	primary key(id)
);

create table board (
	board_no int(10) not null auto_increment ,
	writer_id varchar(20) not null,
	write_date Date,
	last_modified_date Date,
	description varchar(1000),
	board_type int(1),
	primary key (board_no),
	FOREIGN KEY (writer_id) REFERENCES member(id)
);
