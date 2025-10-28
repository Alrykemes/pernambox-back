-- Extensão para gerar UUIDs automaticamente
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE address
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

CREATE TABLE unit
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name       VARCHAR(150) NOT NULL,
    address_id UUID         NOT NULL,
    CONSTRAINT fk_unit_address_id FOREIGN KEY (address_id)
        REFERENCES address (id) ON DELETE CASCADE
);

CREATE TYPE role_type AS ENUM ('MASTER_ADM','UNIT_ADM','USER');

CREATE TABLE users
(
    id       UUID PRIMARY KEY      DEFAULT uuid_generate_v4(),
    name     VARCHAR(255) NOT NULL,
    cpf      CHAR(11)     NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL UNIQUE,
    phone    VARCHAR(14) UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     role_type    NOT NULL DEFAULT 'USER',
    unit_id  UUID         NOT NULL,
    CONSTRAINT fk_users_unit_id FOREIGN KEY (unit_id)
        REFERENCES unit (id) ON DELETE CASCADE
);

CREATE TYPE status_type AS ENUM ('STABLE','UNSTABLE','CRITICAL');

CREATE TYPE categories_tools AS ENUM ('EPI', 'TOOLS', 'OTHERS');

CREATE TABLE tools
(
    id          UUID PRIMARY KEY          DEFAULT uuid_generate_v4(),
    quantity    BIGINT           NOT NULL,
    status      status_type      NOT NULL,
    category    categories_tools NOT NULL DEFAULT 'OTHERS',
    in_use      BIGINT           NOT NULL DEFAULT 0,
    description VARCHAR(255)     NOT NULL,
    unit_id     UUID             NOT NULL,
    CONSTRAINT fk_tools_unit_id FOREIGN KEY (unit_id)
        REFERENCES unit (id) ON DELETE CASCADE
);

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
CREATE TYPE operation_target AS ENUM ('ADDRESS','UNIT','USER','PRODUCT','RESOURCE','TOOLS','RESOURCE_PRODUCT','ORIGIN');

CREATE TABLE operation
(
    id               SERIAL PRIMARY KEY,
    operation        operation_type   NOT NULL,
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
CREATE TYPE origin_type AS ENUM ('TOOLS', 'PRODUCT', 'RESOURCE');

CREATE TABLE origin
(
    id              SERIAL PRIMARY KEY,
    receipt         VARCHAR(255)  NOT NULL,
    cpf_cnpj_origin VARCHAR(14)   NOT NULL,
    document        document_type NOT NULL,
    date            DATE          NOT NULL,
    origin          origin_type   NOT NULL,
    target_id       UUID          NOT NULL
);