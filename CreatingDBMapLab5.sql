#create database MapLab5;
use MapLab5;
create table Students( 
studentId bigint PRIMARY KEY, firstName varchar(30), lastName varchar(30) , totalCredits int);

create table Teachers(
teacherId bigint PRIMARY KEY, firstName varchar(30), lastName varchar(30));

create table Courses(
courseId bigint PRIMARY KEY, name varchar(30), credits int, teacherId bigint,
FOREIGN KEY( teacherId ) REFERENCES Teachers (teacherId));

alter table  Courses
add foreign key (teacherId) REFERENCES Teachers(teacherId);

alter table Courses 
add column maxEnrollment int;

update  Courses 
set Courses.maxEnrollment = 8
where Courses.courseId in (1,2);

select * from Students;


insert into Students values (1,'Ion', 'Ionescu', 0);
insert into Students values (2,'Pop', 'Popescu', 0);
insert into Students values (3,'Andrei', 'Andreescu', 0);

insert into Teachers values (1,'Ovidiu', 'Ovidiu');
insert into Teachers values (2,'Mariana', 'Mariana');

insert into Courses values (1,'FirstCourse', 5,1,15);
insert into Courses values (2,'SecondCourse', 5,2,10);
insert into Courses values (3,'ThirdCourse', 20,1,5);
insert into Courses values (4,'FourthCourse', 20,2,5);


create table Enrolled(
studentId bigint, courseId bigint, PRIMARY KEY(studentId, courseId),
FOREIGN KEY( studentId ) REFERENCES Students (studentId), 
FOREIGN KEY( courseId ) REFERENCES Courses (courseId));


select * from Students;
select * from Teachers;
select * from Courses;
select * from Enrolled;

