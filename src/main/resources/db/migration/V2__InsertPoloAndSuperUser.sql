-- 1) Criar o usuário principal
INSERT INTO users (name, cpf, email, phone, password, active, role)
VALUES ('Master Admin Test',
        '12345678901',
        'teste@example.com',
        '11999999999',
        '$2a$12$BEubfFRRsx7mu7w.fdxArO2aqHB78XmAG9sLdpZr0FjwaRBdt1B/W',
        true,
        'ADMIN_MASTER')
ON CONFLICT (email) DO NOTHING;

-- 2) Criar o endereço da unidade
INSERT INTO address_unit (number, street, district, city, state, zip_code, complement)
VALUES ('110',
        'R. São Geraldo',
        'Santo Amaro',
        'Recife',
        'PE',
        '50040020',
        'Prédio A')
ON CONFLICT (street, number, zip_code) DO NOTHING;


-- 3) Criar a unidade usando o user e o address criados
INSERT INTO unit (name, phone, email, responsible_id, address_id, created_at, active)
VALUES ('Unidade Central',
        '8131815420',
        'unidadecentral@defesacivil.gov.pe.br',
        (SELECT id FROM users WHERE email = 'teste@example.com'),
        (SELECT id FROM address_unit WHERE street = 'R. São Geraldo'),
        NOW(),
        true)
ON CONFLICT (email) DO NOTHING;

-- 1) REF PRODUCTS (cadastro de referências)
INSERT INTO ref_product (gtin, description, avg_price, brand, image)
SELECT '7891234560001', 'Arroz 5kg - Tipo 1', 25.50, 'ArrozBom', 'arroz-5kg.jpg'
WHERE NOT EXISTS (SELECT 1 FROM ref_product WHERE gtin = '7891234560001');

INSERT INTO ref_product (gtin, description, avg_price, brand, image)
SELECT '7891234560002', 'Água mineral 500ml - pack 6', 18.00, 'ÁguaPura', 'agua-500ml-pack6.jpg'
WHERE NOT EXISTS (SELECT 1 FROM ref_product WHERE gtin = '7891234560002');

INSERT INTO ref_product (gtin, description, avg_price, brand, image)
SELECT '7891234560003', 'Sabão em barra 90g', 2.80, 'Limpo', 'sabao-90g.jpg'
WHERE NOT EXISTS (SELECT 1 FROM ref_product WHERE gtin = '7891234560003');

INSERT INTO ref_product (gtin, description, avg_price, brand, image)
SELECT '7891234560004', 'Kit curativo (band-aid, gaze, ataduras)', 12.00, 'SaudeJá', 'kit-curativo.jpg'
WHERE NOT EXISTS (SELECT 1 FROM ref_product WHERE gtin = '7891234560004');


-- 2) ORIGINS (cada produto tem uma origem - aqui usamos DOAÇÃO como exemplo)
-- usamos a unidade criada anteriormente buscando pelo e-mail
WITH unit_ref AS (
    SELECT id AS unit_id FROM unit WHERE email = 'unidadecentral@defesacivil.gov.pe.br' LIMIT 1
)
INSERT INTO origin (cpf_cnpj_origin, document, date, origin, SEI_process, unit_id)
SELECT '11122233344', 'CPF', '2025-11-20', 'DONATION', 123, unit_id
FROM unit_ref
WHERE NOT EXISTS (
    SELECT 1 FROM origin o WHERE o.cpf_cnpj_origin = '11122233344' AND o.date = '2025-11-20' AND o.origin = 'DONATION' AND o.unit_id = unit_ref.unit_id
);

WITH unit_ref AS (
    SELECT id AS unit_id FROM unit WHERE email = 'unidadecentral@defesacivil.gov.pe.br' LIMIT 1
)
INSERT INTO origin (cpf_cnpj_origin, document, date, origin, SEI_process, unit_id)
SELECT '22233344455566', 'CNPJ', '2025-11-20', 'DONATION', 344, unit_id
FROM unit_ref
WHERE NOT EXISTS (
    SELECT 1 FROM origin o WHERE o.cpf_cnpj_origin = '22233344455566' AND o.date = '2025-11-20' AND o.origin = 'DONATION' AND o.unit_id = unit_ref.unit_id
);


-- 3) PRODUCTS (associando ref_product + origin)
-- Arroz
INSERT INTO product (validity, quantity, ref_product_id, origin_id)
SELECT DATE '2026-06-30', 200,
       (SELECT id FROM ref_product WHERE gtin = '7891234560001' LIMIT 1),
       (SELECT id FROM origin WHERE cpf_cnpj_origin = '11122233344' AND date = '2025-11-20' LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM product p
                      JOIN ref_product r ON p.ref_product_id = r.id
                      JOIN origin o ON p.origin_id = o.id
    WHERE r.gtin = '7891234560001' AND o.cpf_cnpj_origin = '11122233344' AND p.validity = DATE '2026-06-30'
);

-- Água
INSERT INTO product (validity, quantity, ref_product_id, origin_id)
SELECT DATE '2025-12-31', 120,
       (SELECT id FROM ref_product WHERE gtin = '7891234560002' LIMIT 1),
       (SELECT id FROM origin WHERE cpf_cnpj_origin = '11122233344' AND date = '2025-11-20' LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM product p
                      JOIN ref_product r ON p.ref_product_id = r.id
                      JOIN origin o ON p.origin_id = o.id
    WHERE r.gtin = '7891234560002' AND o.cpf_cnpj_origin = '11122233344' AND p.validity = DATE '2025-12-31'
);

-- Sabão
INSERT INTO product (validity, quantity, ref_product_id, origin_id)
SELECT DATE '2026-03-31', 300,
       (SELECT id FROM ref_product WHERE gtin = '7891234560003' LIMIT 1),
       (SELECT id FROM origin WHERE cpf_cnpj_origin = '22233344455566' AND date = '2025-11-20' LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM product p
                      JOIN ref_product r ON p.ref_product_id = r.id
                      JOIN origin o ON p.origin_id = o.id
    WHERE r.gtin = '7891234560003' AND o.cpf_cnpj_origin = '22233344455566' AND p.validity = DATE '2026-03-31'
);

-- Kit curativo
INSERT INTO product (validity, quantity, ref_product_id, origin_id)
SELECT DATE '2027-01-01', 50,
       (SELECT id FROM ref_product WHERE gtin = '7891234560004' LIMIT 1),
       (SELECT id FROM origin WHERE cpf_cnpj_origin = '22233344455566' AND date = '2025-11-20' LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM product p
                      JOIN ref_product r ON p.ref_product_id = r.id
                      JOIN origin o ON p.origin_id = o.id
    WHERE r.gtin = '7891234560004' AND o.cpf_cnpj_origin = '22233344455566' AND p.validity = DATE '2027-01-01'
);

-- 4) RESOURCES (registro físico no estoque da unidade)
WITH unit_ref AS (SELECT id AS unit_id FROM unit WHERE email = 'unidadecentral@defesacivil.gov.pe.br' LIMIT 1)
INSERT INTO resource (description, status, quantity, category, unit_id, qrcode)
SELECT 'Estoque Arroz 5kg', 'STABLE', 200, 'FOOD', unit_id, 'QR-ARROZ-01' FROM unit_ref
WHERE NOT EXISTS (SELECT 1 FROM resource WHERE qrcode = 'QR-ARROZ-01' AND unit_id = unit_ref.unit_id);

WITH unit_ref AS (SELECT id AS unit_id FROM unit WHERE email = 'unidadecentral@defesacivil.gov.pe.br' LIMIT 1)
INSERT INTO resource (description, status, quantity, category, unit_id, qrcode)
SELECT 'Estoque Água 500ml (pack 6)', 'STABLE', 120, 'FOOD', unit_id, 'QR-AGUA-01' FROM unit_ref
WHERE NOT EXISTS (SELECT 1 FROM resource WHERE qrcode = 'QR-AGUA-01' AND unit_id = unit_ref.unit_id);

WITH unit_ref AS (SELECT id AS unit_id FROM unit WHERE email = 'unidadecentral@defesacivil.gov.pe.br' LIMIT 1)
INSERT INTO resource (description, status, quantity, category, unit_id, qrcode)
SELECT 'Estoque Sabão em barra', 'STABLE', 300, 'HYGIENIC', unit_id, 'QR-SABAO-01' FROM unit_ref
WHERE NOT EXISTS (SELECT 1 FROM resource WHERE qrcode = 'QR-SABAO-01' AND unit_id = unit_ref.unit_id);

WITH unit_ref AS (SELECT id AS unit_id FROM unit WHERE email = 'unidadecentral@defesacivil.gov.pe.br' LIMIT 1)
INSERT INTO resource (description, status, quantity, category, unit_id, qrcode)
SELECT 'Estoque Kit Curativo', 'STABLE', 50, 'HEALTH', unit_id, 'QR-KITCUR-01' FROM unit_ref
WHERE NOT EXISTS (SELECT 1 FROM resource WHERE qrcode = 'QR-KITCUR-01' AND unit_id = unit_ref.unit_id);


-- 5) VINCULAR PRODUCTS <-> RESOURCES (resource_product)
-- Arroz
INSERT INTO resource_product (product_id, resource_id)
SELECT p.id, r.id
FROM product p
         JOIN ref_product rp ON p.ref_product_id = rp.id AND rp.gtin = '7891234560001'
         JOIN resource r ON r.qrcode = 'QR-ARROZ-01'
WHERE NOT EXISTS (
    SELECT 1 FROM resource_product rp2 WHERE rp2.product_id = p.id AND rp2.resource_id = r.id
);

-- Água
INSERT INTO resource_product (product_id, resource_id)
SELECT p.id, r.id
FROM product p
         JOIN ref_product rp ON p.ref_product_id = rp.id AND rp.gtin = '7891234560002'
         JOIN resource r ON r.qrcode = 'QR-AGUA-01'
WHERE NOT EXISTS (
    SELECT 1 FROM resource_product rp2 WHERE rp2.product_id = p.id AND rp2.resource_id = r.id
);

-- Sabão
INSERT INTO resource_product (product_id, resource_id)
SELECT p.id, r.id
FROM product p
         JOIN ref_product rp ON p.ref_product_id = rp.id AND rp.gtin = '7891234560003'
         JOIN resource r ON r.qrcode = 'QR-SABAO-01'
WHERE NOT EXISTS (
    SELECT 1 FROM resource_product rp2 WHERE rp2.product_id = p.id AND rp2.resource_id = r.id
);

-- Kit curativo
INSERT INTO resource_product (product_id, resource_id)
SELECT p.id, r.id
FROM product p
         JOIN ref_product rp ON p.ref_product_id = rp.id AND rp.gtin = '7891234560004'
         JOIN resource r ON r.qrcode = 'QR-KITCUR-01'
WHERE NOT EXISTS (
    SELECT 1 FROM resource_product rp2 WHERE rp2.product_id = p.id AND rp2.resource_id = r.id
);