# Event Producer and Consumer with RabbitMQ and PostgreSQL

This project includes an **Event Producer** and **Event Consumer** that use **RabbitMQ** for messaging and **PostgreSQL** for data storage. The producer sends events to a RabbitMQ queue, while the consumer listens for and processes those events.

## Services

- **Event Producer**: A service that generates and sends events to a RabbitMQ queue.
- **Event Consumer**: A service that listens to the RabbitMQ queue and processes the events.
- **RabbitMQ**: A message broker that facilitates communication between the producer and consumer.
- **PostgreSQL**: A relational database for storing processed event data.

## Setup

1. **Clone the Repository**:
   
   git clone 
   cd bosch_project

2.install docker and then navigate to this folder where docker compose file is located and then run docker compose up commandi


3.Access rabbitMQ using this url
http://localhost:15672/

