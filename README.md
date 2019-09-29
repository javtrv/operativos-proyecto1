# operativos-proyecto1
Repositorio dedicado al proyecto 1 de la materia Operativos 2


Para crear archivos .json de prueba con la informacion de prueba usamos la pagina: https://next.json-generator.com

Con la siguiente estructura (donde N es el numero de veces que se va a repetir):
[
  {
    'repeat( N )': {
      id: '{{objectId()}}',
      tiempo_llegada: '{{integer(0, 100)}}',
      tiempo_CPU: '{{integer(0, 100)}}',
      tiempo_IO: '{{integer(0, 100)}}',
      prioridad: '{{integer(0, 10)}}'

    }
  }
]

Para correr el proyecto se deben usar los siguientes comandos:

javac -cp .:json-simple-1.1.1.jar proyecto1.java
java -cp .:json-simple-1.1.1.jar Planificador <nombre archivo prueba>
