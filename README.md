# WeCodeFest Workshop

Taller de introducción al Big Data (concretamente, al concepto **MapReduce**) e introducción a Apache Beam.

## Requisitos

Para este taller vamos a necesitar los siguientes requisitos:

- Un editor de código: No hace falta ningún IDE en especial. Puedes usar *IntelliJ IDEA*, *Eclipse*, *vim*... En mi caso, uso *Visual Studio Code*, pero como he dicho, no importa.
- **No tener miedo a usar la consola.**
- Tener descargado este proyecto en alguna carpeta, que va a ser nuestro *workdir*. Podemos usar git (*git clone https://github.com/urbandataanalytics/uda-WeCodeFest.git*) o bien descargarlo comprimido desde GitHub, que te da la opción.
- Alguna forma de ejecutar la aplicación. Podemos hacero **con Docker** o **sin Docker**.

### Con Docker

- Por supuesto, hace falta tener instalado Docker. En su web tenemos un magnífico tutorial según el caso:
    - Para Mac: https://docs.docker.com/docker-for-mac/install/
    - Para Windows: https://docs.docker.com/docker-for-windows/install/
    - Para Linux según distro:
        - CentOS: https://docs.docker.com/install/linux/docker-ce/centos/
        - Debian: https://docs.docker.com/install/linux/docker-ce/debian/
        - Fedora: https://docs.docker.com/install/linux/docker-ce/fedora/
        - Ubuntu: https://docs.docker.com/install/linux/docker-ce/ubuntu/
- Un detalle en Mac (y probablemente Windows): El tutorial no lo dice directamente, pero hace falta abrir la aplicación "desktop" (y esperar que se configure) para que los comandos estén disponibles.

### Sin Docker

Si optamos por esta vía, aviso que puede ser un camino de dolor si dejamos algún cabo suelto :-). Por lo tanto, verificar que todo está correctamente configurado para evitar dolores de cabeza:

- Tener instalado "Java 1.8" (también conocido como "java 8").
- Tener instalado Maven (en mi caso, he utilizado la última versión actual, **la 3.6.3**).

Parece fácil, pero puede llegar a dar muchos problemas si no se configura correctamente:

**¿Qué versión está usando Maven?**

Verificar que Maven está usando java 1.8. Para ello, podemos ejecutar el siguiente comando que nos mostrará por consola dicha información.
```bash
mvn --version
```
Si aparece otra versión, probablemente nos os funcionará la compilación del programa. En *Linux* y en *MacOS* podemos verificar si la variable de entorno apunta a la versión adecuada:
```
echo $SPARK_HOME
```
Si aparece otra versión o, simplemente, no aparece nada, tendremos que apuntar dicha variable al directorio donde se haya instalado java 1.8 (el lugar dependerá de como lo hayáis instalado):
```
export SPARK_HOME=directorio_de_instalación
```
En *Windows*, sinceramente, no tengo información de como hacer esto.

**¿Cómo instalo todo esto en Ubuntu (y probablemente Debian)?**

Para instalar el jdk-1.8 basta con tirar de los repos:

```
sudo apt-get install openjdk-8-jdk
```

Y de manera similar instalamos Maven:
```
sudo apt-get install maven
```

Probablemente, ya tuvieramos instalado un jdk (no estoy seguro si ya Ubuntu trae uno por defecto), así que cuando ejecutemos el siguiente comando,
veremos que maven no está usando la versión 1.8 de jdk.
```
mvn --version
```
Si es así, habrá que indicar que use la "1.8". Esto lo hacemos haciendo que la variable de entorno **JAVA_HOME** apunte al directorio donde se encuentre el jdk deseado. En ubuntu, probablemente, sea la ruta "/usr/lib/jvm/java-8-openjdk-amd64".

```
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
```

Y comprobamos que Maven usa la versión deseada. **IMPORTANTE:** debemos exportar la variable de entorno anterior cada vez que abramos una consola para ejecutar el proyecto.


**¿Cómo instalo todo esto en MacOS?**

En una distribución Linux, instalar todo esto es harto sencillo usando los sistemas de paqueterías. En Windows y MacOS puede volverse más complicado. Llevo sin usar Windows desde hace años así que no sé como facilitar el proceso pero en MacOS, afortunadamente, existen herramientas que intentan imitar un sistema paquetería propia del mundo Linux y que hace la vida a los desarrolladores mucho más sencilla.

Una de dichas herramientas es "homebrew", la cual podéis instalar siguiendo las instrucciones de la siguiente web https://brew.sh/index_es (que, realmente, es simplemente ejecutar un comando en consola).

Una vez instalado homebrew, podemos instalar las dependencias con lo siguiente:

Java 1.8
```
brew tap adoptopenjdk/openjdk
brew cask install adoptopenjdk8
```

Lo anterior, por explicar un poco, agrega el repositorio de "adoptopenjdk" y, a continuación, instala la versión deseada.

Hecho esto, instalamos Maven.

```
brew install maven
```

## Como ejecutarlo

He intentado simplificar la ejecución a través de scripts a los cuales tenemos que pasar como parámetro la *Clase* que queremos ejecutar. Por ejemplo, si fuera la clase *WordsCount* dentro del paquete *exercices*, dicho parámetro sería **exercices.WordsCount**.

Existen dos scripts, cada uno para un método de ejecución (*Con Docker* o *Sin Docker*):

- Con Docker: Si hemos escogido la vía de utilizar Docker, utilizaremos el script **docker_execute.sh**. En Linux, por requisitos de docker, que el script será ejecutado como *superusuario*, por lo que habría que utilizar el comando "sudo" o bien ser el usuario "root".
Por ejemplo, si queremos ejecutar la clase WordsCount, sería:
````bash
bash docker_execute.sh resolvedExercice.WordsCount
````
- Sin Docker: Si hemos optado por la vía de no usar Docker e instalar manualmente todas las dependencias, utilizaremos el script **maven_execute.sh**.
Por ejemplo, si queremos ejecutar la clase WordsCount, sería:
````bash
bash maven_execute.sh resolvedExercice.WordsCount
````