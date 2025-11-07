-- Extensão para gerar UUIDs automaticamente
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TYPE role_type AS ENUM ('ADMIN','USER');

CREATE TABLE users
(
    id       UUID PRIMARY KEY      DEFAULT uuid_generate_v4(),
    name     VARCHAR(255) NOT NULL,
    cpf      CHAR(11)     NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL UNIQUE,
    phone    VARCHAR(14)  UNIQUE,
    password VARCHAR(255) NOT NULL,
    active   BOOLEAN      NOT NULL,
    role     role_type    NOT NULL DEFAULT 'USER'
);

CREATE TABLE address_unit
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    number     VARCHAR(255) NOT NULL,
    street     VARCHAR(255) NOT NULL,
    district   VARCHAR(255) NOT NULL,
    city       VARCHAR(255) NOT NULL,
    state      VARCHAR(255) NOT NULL,
    zip_code   CHAR(8)      NOT NULL,
    complement VARCHAR(255),
    CONSTRAINT unique_address UNIQUE (street, number, zip_code)
);

CREATE TABLE unit
(
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name            VARCHAR(150) NOT NULL,
    phone           VARCHAR(11)  NOT NULL,
    email           VARCHAR(255) NOT NULL,
    responsible_id  UUID         NOT NULL,
--     active BOOLEAN NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    description     VARCHAR(255),
    address_id      UUID         NOT NULL UNIQUE,
    CONSTRAINT fk_unit_responsible FOREIGN KEY (responsible_id)
        REFERENCES users (id) ON DELETE RESTRICT,
    CONSTRAINT fk_unit_address_id FOREIGN KEY (address_id)
        REFERENCES address_unit (id) ON DELETE CASCADE,
    CONSTRAINT unique_unit_email UNIQUE (email)
);

CREATE TYPE status_type AS ENUM ('STABLE','UNSTABLE','CRITICAL');

CREATE TYPE categories_resources AS ENUM ('HYGIENIC','HEALTH','FOOD','OTHERS');

CREATE TABLE resource
(
    id          UUID PRIMARY KEY              DEFAULT uuid_generate_v4(),
    description VARCHAR(255)         NOT NULL,
    status      status_type          NOT NULL,
    quantity    BIGINT               NOT NULL,
    validity    DATE                 NOT NULL,
    category    categories_resources NOT NULL DEFAULT 'OTHERS',
    unit_id     UUID                 NOT NULL,
    qrcode      VARCHAR(255)         NOT NULL,
    CONSTRAINT fk_resource_unit_id FOREIGN KEY (unit_id)
        REFERENCES unit (id) ON DELETE CASCADE
);

CREATE TABLE product
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    validity    DATE         NOT NULL,
    quantity    BIGINT       NOT NULL,
    gtin        VARCHAR(14)  NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE resource_product
(
    product_id  UUID NOT NULL,
    resource_id UUID NOT NULL,
    CONSTRAINT fk_resource_product_product_id FOREIGN KEY (product_id)
        REFERENCES product (id) ON DELETE CASCADE,
    CONSTRAINT fk_resource_product_resource_id FOREIGN KEY (resource_id)
        REFERENCES resource (ID) ON DELETE CASCADE
);

CREATE TYPE operation_type AS ENUM ('CREATE','UPDATE','DELETE');
CREATE TYPE operation_target AS ENUM ('UNIT','USER','PRODUCT','RESOURCE','RESOURCE_PRODUCT','ORIGIN', 'PRODUCT_RESOURCE_ORIGIN', 'DESTINATION');

CREATE TABLE operation
(
    id               SERIAL PRIMARY KEY,
    operation_type   operation_type   NOT NULL,
    operation_date   TIMESTAMP        NOT NULL,
    operation_target operation_target NOT NULL,
    description      VARCHAR(255)     NOT NULL,
    target_id        UUID             NOT NULL,
    unit_id          UUID             NOT NULL,
    users_id         UUID             NOT NULL,
    CONSTRAINT fk_operation_unit_id FOREIGN KEY (unit_id)
        REFERENCES unit (id) ON DELETE CASCADE,
    CONSTRAINT fk_operation_users_id FOREIGN KEY (users_id)
        REFERENCES users (id) ON DELETE CASCADE
);

CREATE TYPE document_type AS ENUM ('CPF','CNPJ');
CREATE TYPE origin_type AS ENUM ('DONATION', 'BUY','OTHERS');

CREATE TABLE origin
(
    id              SERIAL PRIMARY KEY,
    receipt         VARCHAR(255)  NOT NULL,
    cpf_cnpj_origin VARCHAR(14)   NOT NULL,
    document        document_type NOT NULL,
    date            DATE          NOT NULL,
    origin          origin_type   NOT NULL,
    SEI_process     INTEGER,
    "order"         VARCHAR(255),
    documents_name  VARCHAR(255)  NOT NULL
);

CREATE TABLE product_resource_origin(
    created_at          TIMESTAMP        NOT NULL,
    product_id          UUID,
    resource_id         UUID,
    origin_id           UUID             NOT NULL,
    CONSTRAINT fk_product_resource_origin_product_id FOREIGN KEY (product_id)
        REFERENCES product (id) ON DELETE CASCADE,
    CONSTRAINT fk_product_resource_origin_resource_id FOREIGN KEY (resource_id)
        REFERENCES resource (id) ON DELETE CASCADE,
    CONSTRAINT fk_product_resource_origin_origin_id FOREIGN KEY (origin_id)
        REFERENCES origin (id) ON DELETE CASCADE
);

CREATE TABLE refresh_token
(
    token           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID      NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_refresh_token_users FOREIGN KEY (user_id)
        REFERENCES users (id)
);

CREATE TABLE address_destination
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    number     VARCHAR(255) NOT NULL,
    street     VARCHAR(255) NOT NULL,
    district   VARCHAR(255) NOT NULL,
    city       VARCHAR(255) NOT NULL,
    state      VARCHAR(255) NOT NULL,
    zip_code   CHAR(8)      NOT NULL,
    complement VARCHAR(255)
);

CREATE TABLE destination
(
    id          SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    date_time   TIMESTAMP    NOT NULL,
    unit_id     UUID         NOT NULL,
    address_id  UUID         NOT NULL,
    resource_id UUID         NOT NULL,
    quantity    BIGINT       NOT NULL,
    CONSTRAINT fk_destination_unit_id FOREIGN KEY (unit_id)
        REFERENCES unit (id),
    CONSTRAINT fk_destination_address_id FOREIGN KEY (address_id)
        REFERENCES address_destination (id) ON DELETE CASCADE,
    CONSTRAINT fk_destination_resource_id FOREIGN KEY (resource_id)
        REFERENCES resource (id)
);