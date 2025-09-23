DO
$$
DECLARE
v_polo_id uuid;
BEGIN

INSERT INTO polo (nome, numero, rua, bairro, municipio, cep)
VALUES ('Estado de Pernambuco', '413', 'Rua do Forte', 'São José', 'Recife', '50020-490');

SELECT id
INTO v_polo_id
FROM polo
WHERE municipio = 'Recife';

INSERT INTO usuario (nome, cpf, email, telefone, senha, polo_id)
VALUES ('Init User', '111.222.333-44', 'teste@example.com', '(81)97777-6666', '$2a$12$6VfUPb3ziIEeat9GV5.O7u0vWO7HeAZa/VGTsM0pUyOjfZOmcpK4K', v_polo_id);
END $$;
