# operativos-proyecto1: Simulador 
Repositorio dedicado al proyecto 1 de la materia Operativos 2

# Generador y Parser de Procesos
Para crear archivos .json de prueba con la informacion de prueba usamos la pagina: https://next.json-generator.com

Con la siguiente estructura (donde N es el numero de veces que se va a repetir):


[
  {
    'repeat(N)': {
      tiempo_llegada: '{{integer(1, 100)}}',
      tiempo_CPU: '{{integer(1, 100)}}',
      tiempo_IO: '{{integer(0, 100)}}',
      prioridad: '{{integer(1, 10)}}',
    }
  }
]

Para correr el proyecto se deben usar los siguientes comandos:


make
make run file="<nombre del archivo>"

# Estructuras

Para pruebas: main.java

Estructuras:

    * Process

    * RedBlackBST 

    * BlockingQueue (Thread safe queue) 


Original Sources: 

    RedBlackBST:  Red Black Tree implementation in Java
                  Author: Algorithm Tutor (Cormen)
                  Tutorial URL: https://algorithmtutor.com/Data-Structures/Tree/Red-Black-Trees/

    Queue: Java Utils

  Dependencies:

    JavaFX

    json-simple
