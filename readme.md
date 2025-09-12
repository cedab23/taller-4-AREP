# Entrega inicial taller 3 AREP-101

El siguiente readme tiene la finalidad de ilustrar los cambios agregados a la tercera edición
de este taller, con la finalidad de demostrar el manejo de POJO's e implementación de componentes

# Pre-requisitos
- maven (esto debido a una libreria utilizada para manejo de objetos json)
- navegador web (con la finalidad de ver el servidor web)
- un IDE de preferencia (para poder inicializar el servidor)

## Diagrama de clases

Este taller se realizo teniendo en cuenta el siguiente diseño:

![Diagrama de clases](/src/resources/Diagrama%20de%20clases.png)

y utilizando las siguientes Tecnologias:

![Componentes](/src/resources/tech%20stack.png)

## Pruebas Realizadas

En esta ocasion se manejaron tres pruebas iniciales para confirmar el correcto funcionamiento del servidor web

### 1) Registrar Caballo

La funcionalidad de este test es comprobar que se puede registrar un caballo de forma exitosa en el 
servidor web, en este se le envia mediante POST un string con la informacion de un participante del
arima kinen (jinete, nombre del caballo y estrategia).

### 2) Obtener Datos Para la Tabla

Mediante GET se obtienen los datos guardados en ese momento de los participantes, en este caso
estaria vacia y por ende, deberia generar una tabla sin datos.

### 3) Comprobar Existencia de endpoints

Con esta prueba se verifica que en caso de que se busque por medio de un endpoint un archivo que no 
exista, este devuelva el mensaje de error 404.

### Resultados
Como podemos comprobar, las tres pruebas han sido exitosas.

![pruebas](/src/resources/pruebas.png)

## Instalacion

1) Primero que todo, hay que clonar el repositorio en el que se encuentra actualmente, es decir, este repositorio a una ubicacion de su elección, ya sea en descargas, escritorio o junto a demas documentos.

![archivo descomprimido](/src/resources/carpeta.png)

2) Una vez descargada la carpeta, esta se necesita abrir con un entorno de desarrollo integrado o 'IDE', para este ejemplo se utilizo 'visual studio code', dando el siguiente resultado 

![taller en ide](/src/resources/ide.png)

3) Continuando dentro del IDE elegido, procedemos a inicializar el archivo, en este caso se puede realizar con click derecho y eligiendo la opción 'run java' como se muestra en la imagen. Tambien es posible iniciar el servidor con un boton ubicado en la zona superior izquierda que se asemeja a un triangulo.

![iniciar servidor](/src/resources/correr%20servidor.png)

![boton](/src/resources/boton.png)

4) Una vez inicializado el servidor web, deberia soltar en la consola el siguiente mensaje:

![inicio correcto](/src/resources/inicio.png)

5) Ahora nos dirigimos al navegador web de nuestra preferencia (para este ejemplo se uso microsoft edge) y digitamos en la barra de url el siguiente url:

```
http://localhost:5000/index.html
```
y le damos al boton enter.

![link](/src/resources/enlace.png)

6) Si se han seguido los anteriores pasos de forma correcta, deberia verse en la pantalla la siguiente pagina, haciendo una imitiación de un formulario de inscripción para el arima kinen, una de las carreras de caballos mas prestigiosas de Japon 

![pagina arima kinen](/src/resources/pagina%20funcionando.png)

7) Al final de esta, hay un pequeño formulario para inscribir el caballo junto a otros dos datos (con la finalidad de tener mas semejanza a un formulario de verdad) , se diligencian a gusto propio del siguiente modo:

![inscripcion](/src/resources/inscripcion.png)

y una vez dado al boton 'Registrar caballo' deberia salir el siguiente mensaje:

```
(nombre del caballo) ha sido registrado exitosamente
```

![mensaje](/src/resources/mensaje.png)

mostrando la funcionalidad del servidor web para manejar servicios REST

## Cambios en la segunda edición

ahora en la seccion final de la aplicación web, podemos apreciar que hay un titulo
que dice "Caballos Inscritos" junto a un boton y una tabla. Esta tabla tiene como objetivo mostrarle al usuario cuales fueron los datos ingresados al servidor web recientemente, mientras que el boton es aquel que "refresca" la tabla para que los datos se muestren en la
misma.

![tabla](/src/resources/cambios.png)

por lo cual, si agregamos multiples caballos, en este caso cuatro caballos distintos, a la hora  de oprimir 
el boton "Ver tabla de caballos" estos apareceran por orden de inscripción.

### Caballos inscritos

para este ejemplo se registraron los siguientes participantes:

![Maruzensky](/src/resources/maru.png)
![Tokai Teio](/src/resources/teio.png)
![Oguri Cap](/src/resources/oguri.png)
![Gold Ship](/src/resources/golshi.png)

### Actualizar tabla

finalmente cuando utilizamos el nuevo boton, este traera todos los datos
guardados en el servidor web y los añadira a la tabla, dando el siguiente resultado:

![participantes](/src/resources/participantes.png)

## Cambios en la tercera edición

en esta ocasion se añadio un nuevo endpoint de "saludo.java", el cual tendra la peculiaridad
de tener componentes añadidos, los cuales afectan el comportamiento que tendra a la hora de recibir y enviar
datos, no obstante, esto se realizara a futuro en la entrega final, mientras tanto se realizo su implementación.

![nuevos cambios](/src/resources/nuevos%20cambios.png)

ahora contiene un nuevo endpoint llamado "/greeting", el cual posee la cualidad de devolver un saludo, siendo el 
tipico "hola mundo", no obstante, en caso de devolverle un parametro de la siguiente forma:

```
http://localhost:5000/greeting?name=<parametro>
```

este devolvera un "hola" junto al parametro enviado, mostrado de la siguiente forma:

### Forma Predeterminada

![predeterminado](/src/resources/predeterminado.png)

### Forma Personalizada

![personalizado](/src/resources/personalizado.png)

## Referencias

La imagen y video utilizados no son de autoria propia, si no que se tomaron de 'netkeiba', una pagina especializada en las carreras de caballos de japon.

#### Imagen
- https://en.netkeiba.com/race/shutuba.html?race_id=202510020811&rf=race_toggle_menu
#### Video
- https://en.netkeiba.com/library/detail.html?no=147

Icono utilizado para el header del servidor, este es de uso libre pero requiere su debida retribucion al autor.
#### Icono
- https://www.flaticon.es/icono-gratis/silueta-de-caballo-saltando-hacia-la-vista-lateral-izquierda_33348?term=caballo&page=1&position=12&origin=tag&related_id=33348

### Autor:
Nicolás Pachón Unibio


