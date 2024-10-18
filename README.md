# Práctica de Kafka: Sistema de Gestión de Inventario para E-Commerce

## Descripción General

Este proyecto implementa un sistema de gestión de inventario en tiempo real para una plataforma de e-commerce utilizando Apache Kafka. El sistema maneja ventas, actualización de inventario y alertas de stock.

## Componentes del Sistema

1. **Producers (SpringBoot Kafka)**
    - Genera eventos de ventas y devoluciones.
    - Publica mensajes en el tópico SALES.

2. **Tópico SALES (Apache Kafka)**
    - Almacena eventos de ventas del e-commerce.

3. **Consumers (SpringBoot Kafka)**
    - Consume y procesa eventos de ventas y devoluciones.
    - Actualiza la base de datos STORE BD.
    - Detecta alertas de stock.

4. **Producers (para alertas)**
    - Genera alertas basadas en el nivel de stock.
    - Publica en los tópicos INSUFFICIENT_STOCK y LOW_STOCK.

5. **Tópicos de Alertas**
    - INSUFFICIENT_STOCK: Para alertas de stock insuficiente.
    - LOW_STOCK: Para alertas de stock bajo.

6. **STORE BD (PostgreSQL)**
    - Base de datos para manejar el inventario de la tienda.

## Requisitos Previos

- Java JDK 21+
- Docker y Docker Compose
- Gradle

## Configuración y Ejecución

1. **Clonar el Repositorio**
   git clone https://github.com/AZapata27/kafka-training.git
   cd kafka-training

2. **Iniciar los Servicios con Docker Compose**

   docker-compose up -d

    Esto iniciará Zookeeper, Kafka, PostgreSQL y Redpanda Console.

3. **Configurar la Aplicación**
   - Edita `src/main/resources/application.properties` para configurar la conexión a Kafka y PostgreSQL:
     ```
     spring.kafka.bootstrap-servers=localhost:9093
     spring.datasource.url=jdbc:postgresql://localhost:5432/develop
     spring.datasource.username=admin
     spring.datasource.password=admin123
     ```

4. **Compilar y Ejecutar la Aplicación**

   ./gradlew bootRun

## Estructura del Proyecto

- `src/main/java/co/example/kafkatraining`
- `producers/`: Implementación de los productores Kafka.
- `consumers/`: Implementación de los consumidores Kafka.
- `schemas/`: Schemas de transferencia de datos.
- `jpa/`: Entidades Interfaces de repositorio para PostgreSQL.
- `handler/`: Lógica de negocio.


## Monitoreo

- Accede a Redpanda Console en `http://localhost:8080` para monitorear los tópicos de Kafka.
- Revisa los logs de la aplicación para el seguimiento de eventos y alertas.

## Detener los Servicios

Para detener y eliminar los contenedores, volúmenes y redes creados por docker-compose:

docker-compose down -v