# TRIANAFY-BASE
Trianafy de 2º DAM - Salesianos Triana

Esta API-REST permite gestionar información musical, tales como un artista, una canción y una playlist cada uno con sus respectivos datos

# Autor
  ## Arturo Céspedes Pedrazas  | Github : [Perfil de github](https://github.com/ArturoCes)
# Funcionalidades
Proyecto de desarrollo de una API-REST Salesianos Triana.

**Tecnología y lenguaje utilizado:**
Para el desarrollo de la aplicación, se han utilizado los siguientes elementos:

- **Spring Boot** como framework.
- **InteliJIdea** como IDE.
- **Java** para el desarrollo del código que atiende las peticiones a la API.
- **PostMan** para las pruebas de las diferentes peticiones.
- **Swagger UI** para la documentación.

## Pruebas:
### Para poder probar la API, tendremos dos vías principales:

**Documentación del proyecto en Swagger:** Accederemos a la documentación hecha con **Swagger UI** a la cual accederemos mediante la siguiente ruta:
[Documentación en Swagger](http://localhost:8080/swagger-ui.html) Aquí, podremos probar todas las peticiones disponibles dentro de la API, y ver tanto los valores como el cuerpo de la misma.

**Aplicación de Postman:** Dentro del proyecto, se encuentra el archivo Trianafy-APIREST.postman_collection.json. En este archivo se encuentran las peticiones de postman, deberemos importarlo a nuestro worskpace para poder usarlo, en el archivo se encuentran una serie de peticiones a todos los posibles métodos de la API. En las peticiones POST y PUT, cuando la seleccionemos, al pulsar en Body, podremos indicar el cuerpo que se envía en la petición para crear o modificar algún recurso.
Organización del proyecto:

## Elementos a tener en cuenta:

### Trianafy-APIREST.postman_collection.json:
Es una colección de Postman, que podremos importar en dicho programa, y que nos permitirá acceder a los distintos endpoints de la API para probar todas sus funcionalidades.
# Rutas disponibles:
## Artista: 
#### ({id} es el ID del artista)
- GET: http://localhost:8080/artist/: Obtiene el listado completo de artistas.
- GET: http://localhost:8080/artist/{id}: Obtiene la información de un artista si lo encuentra.
- POST: http://localhost:8080/artist/: Crea un nuevo artista.
- PUT: http://localhost:8080/artist/{id}: Modifica un artista si lo encuentra.
- DELETE: http://localhost:8080/artist/{id}: Borra un artista si lo encuentra y le quita el artista a las canciones que lo tenian vinculado.
## Canción: 
### ({id} es el ID de la canción)
- GET: http://localhost:8080/song/: Obtiene el listado completo de canciones.
- GET: http://localhost:8080/song/{id}: Obtiene la información de una canción si la encuentra.
- POST: http://localhost:8080/song/: Crea una nueva canción.
- PUT: http://localhost:8080/song/{id}: Modifica una canción si la encuentra.
- DELETE: http://localhost:8080/song/{id}: Borra una canción si la encuentra, y, si existe en alguna playlist, la borra de ahí.
## Lista:
## Playlist:
### ({id} es el ID de la playlist)
- GET: http://localhost:8080/list/: Obtiene el listado completo de playlists.
- GET: http://localhost:8080/list/{id}: Obtiene la información de una playlist si la encuentra.
- POST: http://localhost:8080/list/: Crea una nueva playlist.
- PUT: http://localhost:8080/list/{id}: Modifica una playlist si la encuentra.
- DELETE: http://localhost:8080/list/{id}: Borra una playlist si la encuentra.
## Canción-Playlist:
### ({id1} es el ID de la playlist, e {id2} es el ID de la canción)
- GET: http://localhost:8080/list/{id1}/song/: Obtiene el listado completo de canciones de una playlist si la encuentra.
- GET: http://localhost:8080/list/{id1}/song/{id2}: Obtiene la información de una canción existente en una playlist, y encuentra ambos.
- POST: http://localhost:8080/list/{id1}/song/{id2}: Añade una canción a una playlist si encuentra ambos.
- DELETE: http://localhost:8080/list/{id1}/song/{id2}: Borra una canción de una playlist si encuentra ambos.
