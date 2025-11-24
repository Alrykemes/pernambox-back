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
