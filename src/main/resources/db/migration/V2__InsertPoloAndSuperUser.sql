-- 1) Criar o usuário principal
INSERT INTO users (name, cpf, email, phone, password, role)
VALUES (
    'João da Silva',
    '12345678901',
    'teste@example.com',
    '(11)99999-9999',
    '$2a$12$BEubfFRRsx7mu7w.fdxArO2aqHB78XmAG9sLdpZr0FjwaRBdt1B/W',
    'MASTER_ADM'
)
ON CONFLICT (email) DO NOTHING;

-- 2) Criar o endereço da unidade
INSERT INTO address_unit (number, street, district, city, state, zip_code, complement)
VALUES (
    '123',
    'Av. Paulista',
    'Bela Vista',
    'São Paulo',
    'SP',
    '01311000',
    'Prédio A'
)
ON CONFLICT (street, number, zip_code) DO NOTHING;


-- 3) Criar a unidade usando o user e o address criados
INSERT INTO unit (name, phone, email, responsible_id, address_id)
VALUES (
    'Unidade Central',
    '36796543',
    'unidadecentral@defesacivil.gov.br',
    (SELECT id FROM users WHERE email = 'teste@example.com'),
    (SELECT id FROM address_unit WHERE street = 'Av. Paulista')
)
ON CONFLICT (email) DO NOTHING;
