# Ejemplo de mapeo JPA Básico  (DAGSS-2024, semana 1)

* Ejemplo de proyecto con JPA (dependencias, configuración persistence.xml)
* Ejemplo de mapeo JPA (uso de anotaciones)


## 1. PREVIO
### Requisitos previos

* Servidor de BD MySQL (version > 8.x)
* Java SDK (version > 11)
* Maven (versión > 3.5.x)
* (opcional) GIT
* (opcional) IDE Java (Eclipse, Netbeans, IntelliJ, VS Code)

**Nota:** En los equipos de laboratorio, puede ser conveniente establecer la variable de entorno JAVA_PATH, para que el comando `mvn` (Maven) compile y ejecute los proyectos siempre con el mismo JDK.

 ```sh
 export JAVA_HOME=/usr/lib/jvm/openjdk-20
 
 export PATH=$JAVA_HOME/bin:$PATH
 ```


### Crear BD para los ejemplos

* Crear la BD "pruebas_dagss" en MySQL y crear un usuario "dagss/dagss"

    ```sh
    $ mysql -u root -p    #[pedirá la contraseña de MySQL]
    
    mysql> create database pruebas_dagss;
    mysql> create user dagss@localhost identified by "dagss";
    mysql> grant all privileges on pruebas_dagss.* to dagss@localhost;
    ```

**Nota:** También puede crearse la BD y el usuario desde un interfaz gráfico (MySQLWorkbench, PHPMyAdmin, ...) usando directamente los comandos indicados.

Adicionalmente, puede ser necesario establecer un formato de fecha compatible
```
mysql> set @@global.time_zone = '+00:00';
mysql> set @@session.time_zone = '+00:00';
```



## 2. Descarga y puesta en marcha del proyecto
### Descarga desde github
URL del proyecto: [https://github.com/esei-si-dagss/mapeo-jpa-24](https://github.com/esei-si-dagss/mapeo-jpa-24)

- Descarga con el comando `git`
	```sh
	$ git clone https://github.com/esei-si-dagss/mapeo-jpa-24.git
	```

- Descarga directa del proyecto comprimido en .ZIP 
	```sh
	$ wget https://github.com/esei-si-dagss/mapeo-jpa-24/archive/refs/heads/main.zip
	$ unzip main.zip
	$ mv mapeo-jpa-24-main mapeo-jpa-24
	```

### Creación de tablas y carga de datos iniciales

Desde la raíz del proyecto
```sh
$ cd mapeo-jpa-24

$ mysql -u dagss -p -D pruebas_dagss < sql/script_de_creacion.sql 
$ mysql -u dagss -p -D pruebas_dagss < sql/datos_iniciales.sql  
  
       #[en ambos casos pedirá la contraseña del usuario 'dagss']
```

**Nota:** Pueden crearse las tablas y cargar los datos desde un interfaz gráfico (MySQLWorkbench, PHPMyAdmin, ...) cargando/pegando los respectivos archivos .sql

## 3.  Revisión del código de proyecto

Comprobar la estructura del proyecto con el comando `tree` ó `ls -lR`:

```
.
├── pom.xml
├── README.md
├── sql
│   ├── datos_iniciales.sql
│   ├── modelo_ER.png
│   ├── modelo_ER.puml
│   └── script_de_creacion.sql
└── src
    └── main
        ├── java
        │   └── es
        │       └── uvigo
        │           └── dagss
        │               └── mapeo
        │                   ├── Auxiliar.java
        │                   ├── entidades
        │                   │   ├── CategoriaLaboral.java
        │                   │   ├── Departamento.java
        │                   │   ├── Empleado.java
        │                   │   ├── EstadoActividad.java
        │                   │   ├── Proyecto.java
        │                   │   └── Tarea.java
        │                   └── Main.java
        └── resources
            └── META-INF
                └── persistence.xml
```



### Directorio `sql`

- Script SQL de creación de las tablas de la Base de Datos (`script_de_creacion.sql`)

- Script SQL para la inserción de los datos iniciales (`datos_iniciales.sql`)

- Modelo entidad-relación: en formato PNG ([modelo_ER.png ](sql/modelo_ER.png)) y código fuente [PlantUML](https://plantuml.com/es/) ([modelo_ER.puml](sql/modelo_ER.puml))

  

### Fichero `pom.xml`

Configuración del proyecto [Maven](https://maven.apache.org/).

- Declara los metadatos y _properties_ del proyecto (versión de Java en este caso), junto con las dependencias de liberías externas.

**Librerías externas** utilizadas:

- **Hibernate ORM** (`hibernate-core`, versión 6.6.2-Final): Implementación de la especificación JPA del proyecto [Hibernate](https://hibernate.org/) 

  - Proporciona la implementación de los componentes de JPA (anotaciones para el mapeo OR, _EntityManager_, consultas)
  - Detalles en [https://hibernate.org/orm/](https://hibernate.org/orm/)

- **Lombok** (`lombok`, versión 1.18.34): Librería que genera implementaciones "automáticas" de notaciones de código de uso común (getters, setters, constructores,`toString()`, `hashCode()`, `equals()`, etc)

  - Se utiliza para proporcionar implementaciones de esos métodos en las entidades JPA.
  - Se hace uso de la anotación [`@Buider`](https://projectlombok.org/features/Builder) para dotar a las entidades de una _builder API_ que simplifique la creación de nuevos instancias (permite evitar el uso de múltiples constructores complejos).
  - Detalles en [Lombok features](https://projectlombok.org/features/).

- **MySQL connector** (`mysql-connector-j`, version 9.1.0): Implementación del driver [JDBC](https://es.wikipedia.org/wiki/Java_Database_Connectivity) que ofrece acceso a bajo nivel a servidores MySQL Server y MariaDB.

  - Utilizado por la implementación de JPA para el acceso a la BD.

    

### Paquete `es.uvigo.dagss.mapeo.entidades`

Paquete con las clases java que se encargarán de mapear las tablas y relaciones de la base de datos. Se trata de un modelo entidad-relación para una aplicación de gestión de proyectos muy simplificada.

- Se definen dos `enum` de Java: `CategoriaLaboral` y `EstadoActividad` [no nesitan anotaciones JPA]

- Se definen cuatro clases Java (`Departamento`, `Empleado`, `Proyecto`, `Tarea`) que habrá que anotar para mapear las tablas, atributos y relaciones de la base de datos.

  

### Clase principal `es.uvigo.dagss.mapeo.Main.java`

Incluye una función `main()` de ejemplo que:

- Instancia un `EntityManagerFactory` vinculado a la _Persistence Unit_ `mapeo_PU`, que se utlizará para crear los `EntityManager` necesario para realiazar operaciones sobre las entidades. 
- Invoca a las funciones `pruebaConsultaEmpleados()` y `pruebaConsultaProyectos()` que reciben un `EntityManager` con el que consultan la Base de Datos y muestran su contenido en pantalla.

**Nota:** Se incluye también una clase de utilidad `Auxiliar.java` con un método `crearEntidadesIniciales()` que muestra cómo se realizaría la inserción de las tuplas presentes en la Base de Datos (no es necesario utilizarlo).



### Configuración de la Persistence Unit (`persistence.xml`)

En el fichero `src/resources/META_INF/persistence.xml`se define la **Persistence Unit** de la aplicación [[más detalles en espeficicación JPA 3.1](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1#persistence-unit)]

- Declara una _Persistence Unit_ de nombre `mapeo-PU`, indicando que la gestión de transacción será "manual" (`transaction-type="RESOURCE_LOCAL"`).

- Indica que se utilizará la implementación de JPA proporcionada por Hibernate (`org.hibernate.jpa.HibernatePersistenceProvider`) y enumera la lista de tipos de entidades a gestionar.

- Contiene la información necesaria para establecer la conexión con la Base de Datos utilizada  (parámetros`jakarta.persistence.jdbc.{url, user, password}`) e indica que se usará el Driver JDBC para MySQL/MariaDB (`com.mysql.cj.jdbc.Driver`).

- La _property_ `jakarta.persistence.schema-generation.database.action` tiene el valor `none` que indica que no se actuará sobre la Base de Datos 

  - Puede cambiarse a `create` para forzar la creación desde cero las tablas de la Base de Datos conforme a lo indicado en las anotaciones JPA (fallará si la BDya existe) o a `drop-and-create`, que elimina las tablas (junto con los datos almacenados) y las crea de nuevo automáticamente [[más detalles en JEE  Tutorial](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-intro/persistence-intro.html#_configuring_an_application_to_create_or_drop_database_tables)]

- Opcionalmente, pueden añadirse _properties_ específicas de Hibernate ORM (`hibernate.*`) para ajustar detalles del mapeo JPA o para habilitar el log por pantalla de las consultas SQL generadas por Hibernate (`hibernate.show_sql`, `hibernate.format_sql`).

    

## 4. TAREA: Completar el mapeo JPA (en paquete `es.uvigo.dagss.mapeo.entidades`)



Si se compila y ejecuta el proyecto con los siguientes comandos MAVEN:

```sh
$ mvn package                                                     # compila y empaqueta el proyecto
$ mvn exec:java -Dexec.mainClass="es.uvigo.dagss.mapeo.Main"      # ejecuta la clase indicada
```

se obtiene una excepción `UnknownEntityException` indicando que no están definidas las entidades JPA. 

Es necesario añadir las anotaciones de JPA para definir las entidades del modelo y mapear sus atributos y relaciones de forma que sea posible mapear las tablas de la Base de Datos a los objetos Java definidos en el paquete `es.uvigo.dagss.mapeo.entidades`.

- En su versión actual, las entidades de ese paquete (`Departamento, Empleado, Proyecto, Tarea`) **sólo** tienen las **anotaciones de Lombok** necesarias para hacerlas funcionales como _Java Beans_ (constructor vacio, _getters_ y _setters_) y para ofrece una _Builder API_.

**Pasos a seguir**

1. Revisar el modelo Entidad-Relación disponible en [modelo_ER.png](sql/modelo_ER.png) y las sentencias DDL de creación de las tablas de [script_de_creacion.sql](sql/script_de_creacion.sql)
   - En **1** `Departamento`  trabajan **N** `Empleados` y cada `Empleado` trabaja en un único `Departamento`
   - En **1** `Departamento` hay **1** `Empleado` que actúa como jefe de departamento y cada jefe sólo puede dirigir **1** `Departamento`
   - En **1** `Proyecto` se definen **N** `Tareas` y cada `Tarea` es exclusiva del `Proyecto` al que pertenece
   - En **1** `Proyecto` hay **1** `Empleado` que actúa como responsable, pero un `Empleado` puede ser responsable de **N** `Proyectos` 
   - Cada `Proyecto` está asignado a **1** `Departamento` y un `Departamento` puede tener asignados **N** `Proyectos`
   -  En una `Tarea` participan **N** `Empleados` y cada `Empleado` puede participar en **M** `Tareas`
   - En **1** `Tarea` hay **1** `Empleado` que actúa como responsable, pero un `Empleado` puede ser responsable de **N** `Tareas` 
   
   **Nota:** Para facilitar la comprensión del ejemplo, en el modelo E-R se utilizan los nombres de las tablas y de las columnas de la Base de Datos (incluidas las claves foráneas), no los nombres y atributos de las entidades Java 
   
2. Añadir las anotaciones `@Entity` en las clases que correspondan y ajustar con `@Table` la vinculación con los nombres de las tablas de la Base de Datos

3. Añadir las anotacicones `@Id` en los atributos clave de cada entidad. 
   - En este ejemplo, todas las entidades, excepto `Empleado`, usan como clave primaria atributos autoincrementales de MySQL, por lo que se debe utilizar la anotación `@GeneratedValue(strategy = GenerationType.IDENTITY)`

4. Ajustar con `@Column` la vinculación los atributos de las entidades con los correspondientes nombres de columna de la Base de Datos. También puede ser necesario ajustar otros parámetros de `@Column`

5. Especificar el mapeo de los atributos de los tipos _enum_ utilizados y lel de os atributos de tipo `Date`

6. Mapear las relaciones del [modelo entidad-relación](sql/modelo_ER.png) con `@OneToOne`,  `@ManyToOne`, `@OneToMany` y `@ManyToMany` , según corresponda
   - Todas las relaciones está definidas de forma unidireccional, excepto la relación 1:N entre `Proyecto` y  `Tarea` que sí se mapea en ambos sentidos.
      - Detalles [sobre mapeo de relaciones](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1#a516) en la Especificación JPA 3.1: [unidireccionales](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1#a758), [bidireccionales](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1#bidirectional-manytoone-onetomany-relationships) 
   - El nombre de las _columnas de join_ en la definición de las tablas no se corresponde con la convención que se sigue en JPA por defecto y puede ser necesario configurarlo con anotaciones`@JoinColumn`
   -  La relación N:M entre`Tarea` y `Empleado` se mapea únicamente desde la entidad `Tarea`. En la Base de Datos está implementada por la tabla `T_TAREA_EMPLEADO`, para detallar este mapeo puede ser necesario hacerlo con la anotación `@JoinTable`, especificando los nombres de las columnas que correspondan.
      - Detalles y parámetros de la [anotación `@JoinTable`](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1#a15022)  

**Ejecución**

Una vez completado el mapeo JPA, la aplicación debería de poder compilarse y ejecutarse con:

```sh
$ mvn package 
$ mvn exec:java -Dexec.mainClass="es.uvigo.dagss.mapeo.Main" 
```

Obteniendo una salida similar a:

```
[PRUEBA JPA]: --------------------------------------
[PRUEBA JPA]: Listado de departamentos con empleados
[PRUEBA JPA]: --------------------------------------
[PRUEBA JPA]: DEPARTAMENTO (<departamento.id>) <departamento.nombre>, Jefe: <departamento.jefeDepartamento.nombre>
[PRUEBA JPA]:   <empleado.nombre> [<empleado.categoria]  (empleado.dni)
[PRUEBA JPA]:   <empleado.nombre> [<empleado.categoria]  (empleado.dni)
[PRUEBA JPA]:   <empleado.nombre> [<empleado.categoria]  (empleado.dni)
[PRUEBA JPA]:
[PRUEBA JPA]: --------------------------------------

[PRUEBA JPA]: --------------------------------------
[PRUEBA JPA]: Listado de proyectos con tareas
[PRUEBA JPA]: --------------------------------------
[PRUEBA JPA]: PROYECTO <proyecto.titulo> (<proyecto.id>) -> Estado: <proyecto.estado>, Responsable: <proyecto.responsable.nombre>
[PRUEBA JPA]:   Tarea: <tarea.nombre> (<tarea.id>) -> Estado: <tarea.estado>
[PRUEBA JPA]:      - <empleado.nombre> [<empleado.departamento.nombre>]
[PRUEBA JPA]:      - <empleado.nombre> [<empleado.departamento.nombre>]
[PRUEBA JPA]:      - <empleado.nombre> [<empleado.departamento.nombre>]
[PRUEBA JPA]:   Tarea: <tarea.nombre> (<tarea.id>) -> Estado: <tarea.estado>
[PRUEBA JPA]:      - <empleado.nombre> [<empleado.departamento.nombre>]
[PRUEBA JPA]:      - <empleado.nombre> [<empleado.departamento.nombre>]
[PRUEBA JPA]:
[PRUEBA JPA]: --------------------------------------
```

**NOTA:** Si se quiere comprobar cómo son las sentencias SQL que va generando Hibernate, se pueden descomentar las siguientes lineas en el fichero `persistence.xml`:

```xml
<property name="hibernate.show_sql" value="true"/>
<property name="hibernate.format_sql" value="true"/>
```

