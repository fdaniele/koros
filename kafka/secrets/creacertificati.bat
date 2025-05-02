@echo off
setlocal

REM === Impostazioni ===
set KEYSTORE_PASSWORD=k0r0s2025!
set TRUSTSTORE_PASSWORD=trustk0r0s2025!
set KEY_PASSWORD=k0r0s2025!
set ALIAS=kafka-server
set CN=kafka

REM === Crea keystore ===
echo Generazione keystore...
keytool -genkeypair ^
 -alias %ALIAS% ^
 -keyalg RSA ^
 -keysize 2048 ^
 -validity 365 ^
 -keystore kafka.server.keystore.jks ^
 -storepass %KEYSTORE_PASSWORD% ^
 -keypass %KEY_PASSWORD% ^
 -dname "CN=%CN%, OU=koros, O=koros, L=Roma, S=RM, C=IT"

REM === Esporta certificato pubblico ===
echo Esportazione certificato...
keytool -export ^
 -alias %ALIAS% ^
 -keystore kafka.server.keystore.jks ^
 -storepass %KEYSTORE_PASSWORD% ^
 -file kafka.server.cer

REM === Crea truststore ===
echo Importazione nel truststore...
keytool -import ^
 -alias %ALIAS% ^
 -file kafka.server.cer ^
 -keystore kafka.server.truststore.jks ^
 -storepass %TRUSTSTORE_PASSWORD% ^
 -noprompt

REM === Pulizia file intermedio ===
REM del kafka.server.cer

echo.
echo Certificati generati con successo!
echo Keystore: kafka.server.keystore.jks
echo Truststore: kafka.server.truststore.jks

endlocal
pause