create table courses
(
    id serial primary key,
    title varchar(50) not null
);

create table students
(
    id serial primary key,
    name varchar(20) not null,
    surname varchar(20) not null
);

create table students_courses
(
    student_id serial not null,
    course_id serial not null,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    UNIQUE (student_id, course_id)
);