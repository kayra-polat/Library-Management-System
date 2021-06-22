
/*-------------------CREATING TABLES------------------*/
--Staff Table--
CREATE TABLE public.staff (
	staff_id int4 NOT NULL,
	staff_name varchar NOT NULL,
	staff_tel varchar(15) NOT NULL,
	salary int4 NULL,
	gender varchar(20) NULL,
	CONSTRAINT staff_pk PRIMARY KEY (staff_id)
);

--Users Table--
CREATE TABLE public.users(
	user_id varchar(20) NOT NULL,
	user_name varchar NOT NULL,
	user_surname varchar NOT NULL,
	user_tel varchar(15) NOT NULL,
	address varchar NOT NULL,
	email varchar NOT NULL,
	staff_id int4 NOT NULL,
	gender varchar(20) NOT NULL,
	CONSTRAINT users_pk1 PRIMARY KEY (user_id),
	CONSTRAINT users_fk2 FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);

--Publishers Table--
CREATE TABLE public.publishers(
	publisher_id varchar(20) NOT NULL,
	publisher_name varchar NOT NULL,
	city varchar NOT NULL,
	CONSTRAINT publisher_pk1 PRIMARY KEY (publisher_id)
);

--Authors Table--
CREATE TABLE public.authors(
	author_id varchar(20) NOT NULL,
	author_name varchar(50) NOT NULL,
	author_surname varchar(50) NOT NULL,
	gender varchar(20) NOT NULL,
	CONSTRAINT author_pk PRIMARY KEY (author_id)
);

--Books Table--
CREATE TABLE public.books(
	book_id varchar NOT NULL,
	author_id varchar(20) NOT NULL,
	book_name varchar NOT NULL,
	book_language varchar(20) NOT NULL,
	book_edition varchar(40) NOT NULL,
	book_genre varchar NOT NULL,
	isbn varchar(13) NOT NULL,
	page_count int4 NOT NULL,
	copies int4 NULL,
	publisher_id varchar(20) NOT NULL,
	CONSTRAINT book_pk PRIMARY KEY (book_id),
	CONSTRAINT book_fk1 FOREIGN KEY (author_id) REFERENCES authors(author_id),
	CONSTRAINT book_fk2 FOREIGN KEY (publisher_id) REFERENCES publishers(publisher_id)
);

--Borrow Table--
CREATE TABLE public.borrow (
	user_id varchar(20) NOT NULL,
	book_id varchar NOT NULL,
	borrow_date date NULL,
	due_date date NULL,
	reservation_status bool NULL,
	CONSTRAINT borrow_fk1 FOREIGN KEY (user_id) REFERENCES users(user_id),
	CONSTRAINT borrow_fk2 FOREIGN KEY (book_id) REFERENCES books(book_id)
);

--Department Table--
CREATE TABLE public.department (
	dept_id int4 NOT NULL,
	dept_name varchar(20) NOT NULL,
	staff_id int4 NOT NULL,
	CONSTRAINT dept_fk FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);

--Study Rooms Table--
CREATE TABLE public.study_rooms (
	room_id varchar(10) NOT NULL,
	room_type varchar(20) NOT NULL,
	CONSTRAINT study_room_pk PRIMARY KEY (room_id)
);

--Room Reservation Table--
CREATE TABLE public.room_reservation (
	user_id varchar(20) NOT NULL,
	room_id varchar(10) NOT NULL,
	reserve_date varchar NOT NULL,
	time_interval varchar NOT NULL,
	CONSTRAINT room_reservation_fk1 FOREIGN KEY (user_id) REFERENCES users(user_id),
	CONSTRAINT room_reservation_fk2 FOREIGN KEY (room_id) REFERENCES study_rooms(room_id)
);



