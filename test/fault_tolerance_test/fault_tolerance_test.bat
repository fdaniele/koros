@echo off
echo Stop del consumer
docker stop koros-consumer-1

echo Provo ad inviare un messaggio al producer
curl -s -X POST "http://localhost:8081/api/secure/send?payload=MessaggioTolleranzaGuasto"

echo Wait di 10 secondi...
timeout /t 10 /nobreak > NUL

echo Riavvio il consumer
docker start koros-consumer-1

echo Attendo ricezione messaggio..
timeout /t 10 /nobreak > NUL

echo Verifica log del consumer per il corretto funzionamento
docker logs koros-consumer-1 | findstr "MessaggioTolleranzaGuasto" > nul

if %errorlevel%==0 (
    echo Test PASSED: il messaggio e' stato ricevuto dal consumer.
) else (
    echo Test FAILED: il consumer non ha ricevuto il messaggio.
)
