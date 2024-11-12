#!/bin/bash

set -e

# Generar una contraseña segura aleatoria (20 caracteres, mayúsculas, minúsculas, números y símbolos)
PASS=$(tr -dc 'A-Za-z0-9@#$%^&*()_+=' </dev/urandom | head -c 20)

# Guardar la contraseña en una variable de entorno (opcional)
export KEYSTORE_PASSWORD="$PASS"

# Crear archivos temporales para la clave privada y el certificado
echo "$PRIVATE_KEY" > key.pem
echo "$CERTIFICATE" > cert.pem

# Combinar la clave y el certificado en un keystore PKCS12
openssl pkcs12 -export \
  -inkey key.pem \
  -in cert.pem \
  -out keystore.p12 \
  -name mykey \
  -passout pass:"$PASS"

# Eliminar los archivos temporales
rm key.pem cert.pem

# Ejecutar la aplicación Java con los parámetros especificados
java \
  -Dterminal.jline=false \
  -Dterminal.ansi=true \
  -Dserver.ssl.enabled=true \
  -Dserver.ssl.protocol=TLS \
  -Dserver.ssl.key-store=keystore.p12 \
  -Dserver.ssl.key-store-type=PKCS12 \
  -Dserver.ssl.key-store-password="$PASS" \
  -jar app.jar
