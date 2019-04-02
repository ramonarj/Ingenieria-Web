-- 
-- SQL script that will be used to initialize the database
-- Note that the syntax is very picky. A reference is available at
--   http://www.hsqldb.org/doc/guide/sqlgeneral-chapt.html
-- 
-- When writing INSERT statements, the order must be exactly as found in
-- the server logs (search for "create table user"), or as 
-- specified within (creation-compatible) parenthesis:
--     INSERT INTO user(id,enabled,login,password,roles) values (...)
-- vs
--     INSERT INTO user VALUES (...)
-- You can find the expected order by inspecting the output of Hibernate
-- in your server logs (assuming you use Spring + JPA)
--

-- On passwords:
--
-- valid bcrypt-iterated, salted-and-hashed passwords can be generated via
-- https://www.dailycred.com/article/bcrypt-calculator
-- (or any other implementation) and prepending the text '{bcrypt}'
--
-- Note that every time that you generate a bcrypt password with a given 
-- password you will get a different result, because the first characters
-- after the third $ correspond to a random salt that will be different each time.
--
-- a few simple examples:
-- {bcrypt}$2a$04$2ao4NQnJbq3Z6UeGGv24a.wRRX0FGq2l5gcy2Pjd/83ps7YaBXk9C == 'a'
-- {bcrypt}$2a$04$5v02dQ.kxt7B5tJIA4gh3u/JFQlxmoCadSnk76PnvoN35Oz.ge3GK == 'p'
-- {bcrypt}$2a$04$9rrSETFYL/gqiBxBCy3DMOIZ6qmLigzjqnOGbsNji/bt65q.YBfjK == 'q'


INSERT INTO turno(id, date) VALUES
(1, 'Fire6729');


-- NUUESTROS USUARIOS	
INSERT INTO user(id,enabled,login,name,idfire,phone,mail,address,password,roles, turno_id) VALUES 
(4, 1, 'ale', 'Alejandro Villar Rubio', 'Fire6729', '696969696', 'xXxXCuchilloNegroXxXx@yahoo.com', 'Er puerto de Santa María, Barca 17',
	'{bcrypt}$2a$04$eiPCDkX7i.89wQl7DOEaMO7BYVQIDWLtZzAm1bLJpZ8l58/IE1mmi', 	'USER', 1);
INSERT INTO user(id,enabled,login,name,idfire,phone,mail,address,password,roles, turno_id) VALUES 
(5, 1, 'ramon', 'Ramón Arjona Quijones', 'Fire8293', '695274369', 'ramonarku@gmail.com', 'C/San Jacinto nº64, 3ºE', 
	'{bcrypt}$2a$04$/92dKh3H3UJ1xizYfMpQMuxXVri60Xod6lMpMgIwSkV4mYX0pQ.Se', 	
	'USER,ADMIN', 1);
INSERT INTO user(id,enabled,login,name,idfire,phone,mail,address,password,roles, turno_id) VALUES 
(6, 1, 'kelvin', 'Kelvin Comper Díaz', 'Fire9120', '123456789', 'erKelvinReshulon@hotmail.com', 'Avda. Ballecas nº404',
	'{bcrypt}$2a$04$OudKz64nPFOcNC.UMgMYy.L45HCYb52AenVYqC1.X6X.oEXIR6vyG', 	
	'USER', 1);
INSERT INTO user(id,enabled,login,name,idfire,phone,mail,address,password,roles, turno_id) VALUES 
(7, 1, 'alberto', 'Alberto Córdoba Ortiz', 'Fire1705', '444666888', 'alberto360@gmail.net', 'Legaspi',
	'{bcrypt}$2a$04$512BG9TBSwDBxdG8J7f.uOJ2OKkwwIxgk7dNYy0BwXrLgMQgz2TEG', 	
	'USER', 1);
INSERT INTO user(id,enabled,login,name,idfire,phone,mail,address,password,roles, turno_id) VALUES
 (8, 1, 'javi', 'Javier Cordero Calvo', 'Fire01227', '000123456', 'ElCorderitoCalvo@gmail.com', 'ExtremoDuro',
	'{bcrypt}$2a$04$9AkKzXf94ipILs.3ZmcBi.f64auKWrycYLLPRwczR/I/MAJzF1pli', 	
	'USER', 1);

 --BASE DE DATOS PARA LAS HERRAMIENTAS

 INSERT INTO Herramienta(id, type, user_id) VALUES
 (1, 'Lantern', 8);
 
  INSERT INTO Herramienta(id, type, user_id) VALUES
 (2, 'HACHA', 7);


