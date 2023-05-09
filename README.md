# Resumen de Actas | DEW-3TI11-L1

## Acta de Reunión nº1

### Preámbulo
---
La reunión se realizó en persona el día miércoles **26/04/2023** en referencia a la sesión 0 de laboratorio.

El grupo 3ti11_g06 está compuesto por:
- Pablo Cerdá Puche
- Diego Córdoba Serra
- Jorge Gandara Sanchis
- Francisco Lozano Mellado
- José Andrés Penadés Cervelló

El alumno firmante es:
- Francisco Lozano Mellado

### Resumen
---
Exposición de los temas reapartidos en la **reunión 1** y posterior evaluación a cada miembro.

### Desarrollo
---
- Cerdá Puche Pablo: *Expectativas, Brainstorming*
    > Extrae las ideas principales y las comunica de manera eficaz

- Diego Córdoba Serra: *Conflictos, Gestión de Conflictos*
    > Se apoya en un lenguaje claro y sencillo para conseguir transmitir las ideas.

- Jorge Gandara Sanchis: *Comunicación, Objetivos, Resolución de Problemas*
    > Se apoya en ejemplos que ayudan a visualizar los conceptos y demuestra gran capacidad de síntesis.

- Francisco Lozano Mellado: *Conflictos, Gestión de Conflictos*
    > Se preocupa de que todos entiendan las ideas que expone y usa un tono más suave para facilitar la comprensión.

- Jose Andrés Penadés Cervelló: *Comunicación, Objetivos, Resolución de Problemas*
   > Demuestra habilidad para resumir la información y exponerla.

-> Para la próxima sesión el equipo acuerda practicar con Eclipse la publicación en Servidor de proyectos web dinámicos


## Acta de Reunión nº2

### Preámbulo
---
La reunión se realizó en persona el día miércoles **19/04/2023** en referencia a la sesión 1 de laboratorio.

El grupo 3ti11_g06 está compuesto por:
- Pablo Cerdá Puche
- Diego Córdoba Serra
- Jorge Gandara Sanchis
- Francisco Lozano Mellado
- José Andrés Penadés Cervelló

El alumno firmante es:
- José Andrés Penadés Cervelló

### Resumen
---
Tras la presentación del trabajo por parte del profesor, se realizó una reunión al salir del aula en la que los miembros del grupo especificamos la forma de trabajar
para las próximas reuniones y el desarrollo del trabajo de prácticas. 

### Desarrollo
---
Para definir la dinámica de trabajo se creó un grupo de WhatsApp para facilitar la comunicación entre reuniones, mientras que durante las reuniones se usará Discord 
como método de comunicación y, finalmente, para facilitar la compartición de código se inicializó un GitHub (git) como herramienta común. 
Diego Córdoba creó un Trello con el objetivo de mantener un registro de las actividades y tareas realizadas, además de contener enlaces y documentos (como tablas, 
actas...) derivados del desarrollo del trabajo. 
Se ha definido los jueves entre las 12:30 y las 14:00 al acabar las clases como horario para realizar las reuniones. 
Tras definir el dinámica, se comentó el contenido de la próxima sesión de laboratorio (Sesión 0) y se definieron tareas para prepararla:
- Lectura del documento "Organización Labo 2", disponible en apartado Recursos del PoliformaT de la asignatura

### En referencia al profesor
----
Durante el desarrollo de la reunión no se ha producido ningún incidente que necesite de la intervención del profesor


## Acta de Reunión nº3

### Preámbulo
----
Esta reunión se realizo el día jueves 27/04/2023 en referencia a la sesión 2 de laboratorio.

El grupo 3ti11_g06 está compuesto por:
- Pablo Cerdá Puche
- Diego Córdoba Serra
- Jorge Gandara Sanchis
- Francisco Lozano Mellado
- José Andrés Penadés Cervelló

El alumno firmante es:
- José Andrés Penadés Cervelló

### Resumen
---
Tras la explicación de la sesión el día 26/04/2023 por parte del profesor, se inició esta reunión con el objetivo de realizar las tareas correspondientes a la 
sesión número 1 de la segunda parte de las prácticas. En dicha reunión, se desarrollaron mediante Eclipse los primeros **servlets** del proyecto (prototipos 
de log) que se  pusieron a prueba mediante formularios. Además, se realizaromn las primeras interacciones con la aplicación CentroEducativo mediante el uso 
de **curl**. 

### Desarrollo
---
**Formularios**
Para el desarrollo de los diferentes tipos de servlets, se desarrollaron una serie de formularios relativos a cada servlet. Para facilitar poder acceder a cada 
formulario relacionado con un servlet determinado sin afectar a los demás, se definió un html llamado "index.html", el cual contiene enlaces a los tres formularios, 
cada uno relacionado con un servlet (log) concreto. 
*Código de index.html

Seguidamente, se exxplicará el funcionamiento de los formularios desarrollados
El formulario adjuntado a continuación hace referencia a la ejecución del servlet log0. Se han desarrollado tres formularios diferentes, cada uno relacionado con 
su servlet. Cada formulario se llama "logn.html", siendo n el número de log. Como se puede observar, distinguimos cuatro inputs. 
El primer input es de tipo text y hace referencia al DNI del usuario. 
El segundo input, también de tipo text, hace referencia a los apellidos del usuario.
El tercer input, último de tipo ext, hace referencia al nombre del usuario.
El cuarto y último input, de tipo submit, actua de botón para realizar el envío del formulario. 

Es también observable que el formato del formulario descrito se aplica tanto para el método GET como para el método POST. 
*Código de log0.html

**Especificaciones de servlets, logger1 y logger2**
Los nombres de los servlets siguen la misma forma que los nombres de los formularios, loggern, siendo n el numero de log al que hacen referencia. Los servlets 
relativos a log1 y log2 (logger1 y logger2 respectivamente), pese a tener una estructura similar, tienen cambios significativos respecto al log0. Estos cambios
se definirán a continuación.
Los servlets incorporan dos métodos, doGet y doPost. Es aquí donde entran las diferencias entre servlets.  

En primer lugar, nos fijremos en logger1, el servlet relativo a log2. Si nos fijamos en el método doGet, podemos ver que se especifica
(mediante el uso de la clase File) una ubicación, que será donde los logs se generen. 
PrintWriter pw2 = new PrintWriter(new FileOutputStream(new File("/home/user/Escritorio/log-NOL-dew.log"),true));´

Por otro lado, en el servlet relativo a log2, también se especifica una ubicación pero esta vez se define desde el ficherp web.xml (facilitado en las 
transparencias del tema 7 de Desarrollo Web). Esto provoca un cambio en el código respecto del anterior. 
*Código de logger2.java

**Salidas **
La salida debe proporcionar datos del formulario,información del cliente, fecha actual, URI, método. Las diferencias notables entre los tres loggers se hacen 
visibles a la hora de observar la salida de cada uno de ellos.
En cuanto al logger0, la salida de este se mostrará en la pantalla del navegador una vez enviado el respectivo formulario. 
*salida de logger0
Por su parte, la salida de logger1 y logger2 quedarán grabadas en el fichero relativo a la ubicación definida anteriormente. 
*salida de logger1
*salida de logger2

**Curl**

### En referencia al profesor
---
La reunión ha transcurrido sin incidentes que precisaran de la presencia o ayuda del profesor.
