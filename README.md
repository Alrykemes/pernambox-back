# Pernambox Backend 📦

### Este projeto trata-se de todo o backend e infraestrura do pernambox, aplicação de gerenciamento de estoque inteligente, desenvolvido para sanar as dores de gerenciamento de recursos da defesa civil do estado de Pernambuco.

## Tecnologias Utilizadas 🖥️:
 
- Java 21
- SpringBoot
- Flyway Migrations
- PostgreSql
- Redis
- MinIO Local Storage
- Loki
- Prometheus
- Grafana
- Docker

## Deploy 🚀

### Para realizar o deploy do projeto em containers docker é necessário alguns requisitos sendo eles:

- Docker (Desktop ou Linux)
- Email com AppPassword ou SMTP Password(em caso de servidores smtp)

### Para que o sistema funcione corretamente é necessário criar um arquivo `.env` com as seguintes variáveis de ambiente para que sejam setadas no container docker e utilizadas pela aplicação:

- SPRING_PROFILES_ACTIVE=prod
- POSTGRES_USER
- POSTGRES_PASSWORD 
- POSTGRES_DB
- POSTGRES_URL
- JWT_SECRET=infra.security.jwt={jwt_secret_value}
- MAIL_HOST=smtp.gmail.com
- MAIL_PORT=587
- MAIL_USERNAME
- MAIL_PASSWORD
- MAIL_AUTH=true
- MAIL_TTLS=true
- REDIS_HOST=redis
- REDIS_PORT=6379
- REDIS_PASSWORD
- MINIO_ENDPOINT=http://host.docker.internal:9100
- MINIO_ACCESS_KEY
- MINIO_SECRET_KEY
- MINIO_BUCKET=arquivos-privados
- GRAFANA_USER
- GRAFANA_PASS

### `Considere os valores que já estão setados como default`

## Instruções de Deploy 🚀:

- ### Inicialmente clone o repositório do pernambox, com o comando `git clone`, após isso navegue até a pasta raiz do repositório `pernambox-back`, ao entrar na pasta raíz sete as variáveis de ambiente mostradas no tópico anterior.

- ### Realizando as etapas anteriores com sucesso, ainda na pasta raíz do repositório execute o comando `docker compose up -d`.

- ### O backend do pernambox estará rodando na porta local `8080` de sua máquina.