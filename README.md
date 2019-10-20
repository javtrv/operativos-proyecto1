# operativos-proyecto1: Simulador 
Repositorio dedicado al proyecto 1 de la materia Operativos 2

# Generador y Parser de Procesos
Para crear archivos .json de prueba con la informacion de prueba usamos la pagina: https://next.json-generator.com

Con la siguiente estructura (donde N es el numero de veces que se va a repetir):


[
  {
    'repeat(N)': {
      tiempo_llegada: '{{integer(0, 100)}}',
      tiempo_CPU: '{{integer(0, 100)}}',
      tiempo_IO: '{{integer(0, 100)}}',
      prioridad: '{{integer(0, 10)}}',
    }
  }
]

Para correr el proyecto se deben usar los siguientes comandos:


make
make run

# Estructuras

Para pruebas: main.java

Estructuras:

    * Process

    * RedBlackBST 

    * BlockingQueue (Thread safe queue) 


Original Sources: 

    RedBlackBST: https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html

    Queue: Java Utils

  Dependencies:

    StdIn: https://introcs.cs.princeton.edu/java/stdlib/StdIn.java.html

    StdOut: https://introcs.cs.princeton.edu/java/stdlib/StdOut.java.html
