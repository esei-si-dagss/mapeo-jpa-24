
USE pruebas_dagss;
SET FOREIGN_KEY_CHECKS = 0;


LOCK TABLES `T_DEPARTAMENTO` WRITE;
INSERT INTO `T_DEPARTAMENTO` VALUES 
      (1,'11111111A','Mortífagos','Descripcion del departamento Mortífagos','Ubicacion del departamento Mortífagos'),
      (2,'22222222A','Ejercito de Dumbledore','Descripcion del departamento Ejercito de Dumbledore','Ubicacion del departamento Ejercito de Dumbledore'),(3,'33333333A','Claustro de Hogwarts','Descripcion del departamento Claustro de Hogwarts','Ubicacion del departamento Claustro de Hogwarts');
UNLOCK TABLES;

LOCK TABLES `T_EMPLEADO` WRITE;
INSERT INTO `T_EMPLEADO` VALUES 
      (1,'11111111A','Voldemort','JEFE_DEPARTAMENTO'),
      (1,'11111111B','Bellatrix Lestrange','SENIOR'),
      (1,'11111111C','Lucius Malfoy','SENIOR'),
      (1,'11111111D','Barty Crouch Jr.','SENIOR'),
      (2,'22222222A','Harry Potter','JEFE_DEPARTAMENTO'),
      (2,'22222222B','Hermione Granger','BECARIO'),
      (2,'22222222C','Ron Weasley','BECARIO'),
      (2,'22222222D','Luna Lovegood','BECARIO'),
      (3,'33333333A','Albus Dumbledore','JEFE_DEPARTAMENTO'),
      (3,'33333333B','Minerva McGonagall','SENIOR'),
      (3,'33333333C','Rubius Hagrid','SENIOR');
UNLOCK TABLES;


LOCK TABLES `T_PROYECTO` WRITE;
INSERT INTO `T_PROYECTO` VALUES 
      ('2024-11-09','2024-05-13',1,1,'11111111A','Conquista del mundo','Descripción de Conquista del mundo','EN_CURSO'),
      ('2024-11-09','2024-05-13',3,2,'33333333A','Defensa del mundo mágico','Descripción de Defensa del mundo mágico','EN_CURSO');
UNLOCK TABLES;


LOCK TABLES `T_TAREA` WRITE;
INSERT INTO `T_TAREA` VALUES 
      ('2024-11-09','2024-05-13',1,1,'11111111B','Descripción de la tarea Reclutar adeptos','Reclutar adeptos','EN_CURSO'),
      ('2024-11-09','2024-05-13',2,1,'11111111A','Descripción de la tarea Matar a Harry Potter','Matar a Harry Potter','EN_CURSO'),
      ('2024-11-09','2024-05-13',3,1,'11111111D','Descripción de la tarea Revivir a Voldemort','Revivir a Voldemort','EN_CURSO'),
      ('2024-11-09','2024-05-13',4,1,'11111111A','Descripción de la tarea Atacar el castillo','Atacar el castillo','EN_CURSO'),
      ('2024-11-09','2024-05-13',5,2,'22222222A','Descripción de la tarea Búsqueda de horrocruxes','Búsqueda de horrocruxes','EN_CURSO'),
      ('2024-11-09','2024-05-13',6,2,'33333333B','Descripción de la tarea Defensa del castillo','Defensa del castillo','EN_CURSO'),
      ('2024-11-09','2024-05-13',7,2,'22222222A','Descripción de la tarea Matar a Voldemort','Matar a Voldemort','EN_CURSO');
UNLOCK TABLES;


LOCK TABLES `T_TAREA_EMPLEADO` WRITE;
INSERT INTO `T_TAREA_EMPLEADO` VALUES 
      (1,'11111111B'),
      (1,'11111111C'),
      (1,'11111111D'),
      (2,'11111111A'),
      (3,'11111111D'),
      (4,'11111111A'),
      (4,'11111111B'),
      (4,'11111111C'),
      (4,'11111111D'),
      (5,'22222222A'),
      (5,'22222222B'),
      (5,'22222222C'),
      (6,'33333333B'),
      (6,'33333333C'),
      (6,'22222222D'),
      (7,'22222222A'),
      (7,'33333333A');
UNLOCK TABLES;

SET FOREIGN_KEY_CHECKS = 1;
