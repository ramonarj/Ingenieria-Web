

INSERT INTO turno(id, start, end, name) VALUES
(1, '2019-04-28T09:30:00', '2019-04-28T19:30:00', 'Turno A');
INSERT INTO turno(id, start, end, name) VALUES
(2, '2019-04-28T09:30:00', '2019-04-29T09:30:00', 'Turno B');
INSERT INTO turno(id, start, end, name) VALUES
(3, '2019-04-28T09:30:00', '2019-04-29T09:30:00', 'Turno C');
INSERT INTO turno(id, start, end, name) VALUES
(4, '2019-04-28T09:30:00', '2019-04-29T09:30:00', 'Turno D');

-- NUUESTROS USUARIOS	
INSERT INTO user(id,enabled,login,name,idfire,rankfire,driver,phone,mail,address,password,roles, turno_id) VALUES 
(4, 1, 'ale', 'Alejandro Villar Rubio', 'Fire6729', 'Bombero', 'SI', '696969696', 'xXxXCuchilloNegroXxXx@yahoo.com', 'Er puerto de Santa María, Barca 17',
	'{bcrypt}$2a$04$eiPCDkX7i.89wQl7DOEaMO7BYVQIDWLtZzAm1bLJpZ8l58/IE1mmi', 	'USER', 1);
INSERT INTO user(id,enabled,login,name,idfire,rankfire,driver,phone,mail,address,password,roles, turno_id) VALUES 
(5, 1, 'ramon', 'Ramón Arjona Quijones', 'Fire8293', 'Jefe de bomberos', 'NO','695274369', 'ramonarku@gmail.com', 'C/San Jacinto nº64, 3ºE', 
	'{bcrypt}$2a$04$/92dKh3H3UJ1xizYfMpQMuxXVri60Xod6lMpMgIwSkV4mYX0pQ.Se', 	
	'USER,ADMIN', 1);
INSERT INTO user(id,enabled,login,name,idfire,rankfire,driver,phone,mail,address,password,roles, turno_id) VALUES 
(6, 1, 'kelvin', 'Kelvin Comper Díaz', 'Fire9120', 'Cabo', 'NO', '123456789', 'erKelvinReshulon@hotmail.com', 'Avda. Ballecas nº404',
	'{bcrypt}$2a$04$OudKz64nPFOcNC.UMgMYy.L45HCYb52AenVYqC1.X6X.oEXIR6vyG', 	
	'USER', 1);
INSERT INTO user(id,enabled,login,name,idfire,rankfire,driver,phone,mail,address,password,roles, turno_id) VALUES 
(7, 1, 'alberto', 'Alberto Córdoba Ortiz', 'Fire1705', 'Bombero', 'SI', '444666888', 'alberto360@gmail.net', 'Legaspi',
	'{bcrypt}$2a$04$512BG9TBSwDBxdG8J7f.uOJ2OKkwwIxgk7dNYy0BwXrLgMQgz2TEG', 	
	'USER', 1);
INSERT INTO user(id,enabled,login,name,idfire,rankfire,driver,phone,mail,address,password,roles, turno_id) VALUES
 (8, 1, 'javi', 'Javier Cordero Calvo', 'Fire01227', 'Bombero', 'NO', '000123456', 'ElCorderitoCalvo@gmail.com', 'ExtremoDuro',
	'{bcrypt}$2a$04$9AkKzXf94ipILs.3ZmcBi.f64auKWrycYLLPRwczR/I/MAJzF1pli', 	
	'USER', 1);

 --BASE DE DATOS PARA LAS HERRAMIENTAS

 INSERT INTO Herramienta(id, type, user_id) VALUES
 (3, 'Barrita de chocolate', 6);

 INSERT INTO Herramienta(id, type, user_id) VALUES
 (1, 'Lantern', 8);
 
  INSERT INTO Herramienta(id, type, user_id) VALUES
 (2, 'Walkie', 7);


