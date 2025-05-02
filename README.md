# Koros – Un middleware orientato allo scambio di messaggi asincroni a supporto della sicurezza nel settore secondario

Koros è un progetto accademico basato su **Spring Boot**, che implementa una pipeline asincrona a microservizi utilizzando **Apache Kafka**, **MongoDB**, e meccanismi di **sicurezza a livello di messaggio** tramite **JWT** e **HMAC**.

---

## Funzionalità principali

- Microservizi Producer / Middleware / Consumer
- Kafka con SSL + autenticazione HMAC + JWT
- Validazione dei messaggi e routing su topic validi o invalidi
- Persistenza su MongoDB
- Monitoraggio delle latenze tramite headers Kafka
- Pipeline testata con Apache JMeter
- Contenitore Docker per ogni servizio

---

## Prerequisiti

Assicurati di avere installato:

- Docker e Docker Compose
- Java 17+
- Maven 3.8+
- Git

---

## Avvio del progetto

1. **Clona il repository:**

```bash
git clone https://github.com/fdaniele/koros.git
cd koros
```

2. **Avvia tutti i servizi:**

```bash
docker-compose up --build
```

⏳ Attendi 1–2 minuti che Kafka, Mongo e i microservizi siano operativi.

3. **Esegui una curl per effettuare un giro end-to-end della pipeline:**
```bash
curl -X POST "http://localhost:8081/api/secure/send" --data-urlencode "payload={\"messageId\":\"ID-20250205-001\",\"timestamp\":\"2025-05-02T15:30:00Z\",\"sender\":{\"id\":\"service-producer\",\"name\":\"Koros Producer Project Work Unipegaso\"},\"receiver\":{\"id\":\"service-consumer\",\"name\":\"Koros Consumer PJ Work Unipegaso\"},\"content\":{\"type\":\"notification\",\"payload\":{\"event\":\"NEW_CUSTOMER_MACHINE\",\"userId\":\"USR-14592\",\"email\":\"test@pharmaceutical.com\",\"status\":\"PENDING_CONFIRMATION\"}},\"security\":{\"level\":\"HIGH\",\"encrypted\":true}}"
```
---

## Struttura del progetto

```
koros/
├── docker-compose.yml
├── kafka/                 # Configurazioni Kafka, certs e script
├── secrets/               # Keystore/truststore e credenziali
├── koros-producer-service/
├── koros-middleware/
├── koros-consumer-service/
├── koros.jmx              # Test JMeter
└── README.md
```

---

## Sicurezza

I messaggi scambiati includono:

- `x-jwt` → Token JWT firmato (authenticity)
- `x-hmac` → HMAC SHA-256 sul payload (integrity)
- `x-sent-producer-timestamp`, `x-middleware-timestamp` → per misurare latenza

Le chiavi per kafka si trovano nella directory `kafka/secrets`. Si trovano inoltre all'interno della cartella anche il *.bat per genereare delle nuove chiavi

---

## Test automatizzati

### JUnit 5

```bash
cd koros-producer-service
mvn test
```

Test presenti su:
- `SecurityUtil`
- `KafkaProducerController`
- `KafkaMessageListener` (mock Kafka/Mongo)

### Apache JMeter

Test di carico definiti nel file:

```
koros.jmx
```

Esecuzione CLI:

```bash
jmeter -n -t koros.jmx -l report.jtl -e -o report/
```

---

## Monitoraggio & Troubleshooting

- Log real-time:

```bash
docker-compose logs -f producer
docker-compose logs -f middleware
docker-compose logs -f consumer
```

---

## Credenziali (default)

| Componente       | File                               | Password                                         |
|------------------|------------------------------------|--------------------------------------------------|
| Keystore         | `kafka.server.keystore.jks`        | `k0r0s2025!`                                     |
| Truststore       | `kafka.server.truststore.jks`      | `trustk0r0s2025!`                                |
| HMAC secret key  | hardcoded in `SecurityUtil.java`   | `hmacsecretunipegasfiorio`                       |
| JWT secret key   | hardcoded in `SecurityUtil.java`   | `projectworkunipegasokorosdidanielefiorio2025`   |

---

## Note finali

- I messaggi non validi vengono instradati su `service-exchange-messages-invalid`
- La sicurezza è gestita end-to-end su header Kafka
- La pipeline è estendibile con alert, metrica Prometheus o log centralizzati tramite ELK

---

## Autore

Daniele — Università Pegaso – Corso di Laurea in Informatica  