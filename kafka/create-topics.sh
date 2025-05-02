#!/bin/bash

# Attesa per lo startup di kafka
sleep 20

echo "Creazione topics... "

#Creazione topics pipeline 
TOPICS=("service-exchange-messages-out" "service-exchange-messages-in")

#Ciclo per la creazione dei topic kafka (sono stati configurati per avere tutti la stessa retations e partizioni)
for TOPIC in "${TOPICS[@]}"; do
  kafka-topics --create --if-not-exists \
    --bootstrap-server kafka:9093 \
    --replication-factor 1 \
    --partitions 3 \
    --topic "$TOPIC" \
	--command-config /etc/kafka/secrets/client-ssl.properties

  kafka-configs --bootstrap-server kafka:9093 \
    --entity-type topics \
    --entity-name "$TOPIC" \
    --alter \
    --add-config retention.ms=604800000,retention.bytes=1073741824 \
	--command-config /etc/kafka/secrets/client-ssl.properties
done

# Topic per i messaggi invalidi
kafka-topics --create --if-not-exists --topic service-exchange-messages-invalid \
  --bootstrap-server kafka:9093 \
  --replication-factor 1 \
  --partitions 1 \
  --command-config /etc/kafka/secrets/client-ssl.properties

kafka-configs --bootstrap-server kafka:9093 \
--entity-type topics \
--entity-name "$TOPIC" \
--alter \
--add-config retention.ms=604800000,retention.bytes=1073741824 \
--command-config /etc/kafka/secrets/client-ssl.properties

echo "Topic kafkaa creati."