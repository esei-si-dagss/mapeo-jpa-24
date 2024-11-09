use pruebas_dagss;

drop table if exists T_DEPARTAMENTO;
create table T_DEPARTAMENTO (
    NUM_DEPTO bigint not null auto_increment, 
    JEFE_DNI_FK varchar(9), 
    NOMBRE varchar(50), 
    DESCRIPCION varchar(150), 
    UBICACION varchar(150), 
    primary key (NUM_DEPTO)
) engine=InnoDB;

drop table if exists T_EMPLEADO;
create table T_EMPLEADO (
    NUM_DEPTO_FK bigint, 
    DNI varchar(9) not null, 
    NOMBRE varchar(50), 
    CATEGORIA enum ('BECARIO','JEFE_AREA','JEFE_DEPARTAMENTO','JUNIOR','SENIOR'), 
    primary key (DNI)
) engine=InnoDB;

drop table if exists T_PROYECTO;
create table T_PROYECTO (
    FECHA_FIN date, 
    FECHA_INICIO date, 
    DEPTO_NUM_FK bigint, 
    ID_PROYECTO bigint not null auto_increment, 
    RESPONSABLE_DNI_FK varchar(9), 
    TITULO varchar(50), 
    DESCRIPCION varchar(150), 
    ESTADO_PROY enum ('ANULADO','EN_CURSO','FINALIZADO','PLANIFICADO'), 
    primary key (ID_PROYECTO)
) engine=InnoDB;

drop table if exists T_TAREA;
create table T_TAREA (
    FECHA_FIN date, 
    FECHA_INICIO date, 
    ID_TAREA bigint not null auto_increment, 
    PROYECTO_ID_FK bigint, 
    RESPONSABLE_DNI_FK varchar(9), 
    DESCRIPCION varchar(50), 
    NOMBRE varchar(50), 
    ESTADO_TAREA enum ('ANULADO','EN_CURSO','FINALIZADO','PLANIFICADO'), 
    primary key (ID_TAREA)
) engine=InnoDB;

drop table if exists T_TAREA_EMPLEADO;
create table T_TAREA_EMPLEADO (
    TAREA_ID_FK bigint not null, 
    EMPLEADO_DNI_FK varchar(9) not null
) engine=InnoDB;

alter table T_DEPARTAMENTO add constraint departamento_jefe_unique 
                                unique (JEFE_DNI_FK);

alter table T_DEPARTAMENTO add constraint departamento_jefe_fk 
                                foreign key (JEFE_DNI_FK) 
                                references T_EMPLEADO (DNI);

alter table T_EMPLEADO add constraint empleado_departamento_fk 
                                foreign key (NUM_DEPTO_FK) 
                                references T_DEPARTAMENTO (NUM_DEPTO);

alter table T_PROYECTO add constraint proyecto_departamento_fk 
                                foreign key (DEPTO_NUM_FK) 
                                references T_DEPARTAMENTO (NUM_DEPTO);

alter table T_PROYECTO add constraint proyecto_responsable_fk 
                                foreign key (RESPONSABLE_DNI_FK) 
                                references T_EMPLEADO (DNI);

alter table T_TAREA add constraint tarea_proyecto_fk 
                                foreign key (PROYECTO_ID_FK) 
                                references T_PROYECTO (ID_PROYECTO);

alter table T_TAREA add constraint tarea_responsable_fk 
                                foreign key (RESPONSABLE_DNI_FK) 
                                references T_EMPLEADO (DNI);

alter table T_TAREA_EMPLEADO add constraint tarea_empleado_fk_empleado 
                                foreign key (EMPLEADO_DNI_FK) 
                                references T_EMPLEADO (DNI);

alter table T_TAREA_EMPLEADO add constraint tarea_empleado_fk_tarea 
                                foreign key (TAREA_ID_FK) 
                                references T_TAREA (ID_TAREA);
