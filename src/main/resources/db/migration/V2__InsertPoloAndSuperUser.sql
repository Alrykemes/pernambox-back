-- Criar um endereço, unidade e usuário padrão no sistema

-- 1) Endereço
INSERT INTO address_unit (id, number, street, district, city, state, zip_code, complement)
VALUES (uuid_generate_v4(),
        '123',
        'Av. Paulista',
        'Bela Vista',
        'São Paulo',
        'SP',
        '01311000',
        'Prédio A')
ON CONFLICT DO NOTHING;
-- evita erro se rodar mais de uma vez

-- 2) Unidade (usando o mesmo address_id criado acima)
INSERT INTO unit (id, name, address_id)
VALUES (uuid_generate_v4(),
        'Unidade Central',
        (SELECT id FROM address_unit WHERE street = 'Av. Paulista' AND number = '123'))
ON CONFLICT DO NOTHING;

-- 3) Usuário (vinculado à unidade criada)
INSERT INTO users (id, name, cpf, email, phone, password, role)
VALUES (uuid_generate_v4(),
        'João da Silva',
        '12345678901',
        'teste@example.com',
        '(11)99999-9999',
        '$2a$12$BEubfFRRsx7mu7w.fdxArO2aqHB78XmAG9sLdpZr0FjwaRBdt1B/W',
        'MASTER_ADM')
ON CONFLICT DO NOTHING;
