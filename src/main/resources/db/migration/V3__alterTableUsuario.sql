CREATE TYPE tipo_permissao AS ENUM ('ADM_GERAL','ADM_POLO','FUNCIONARIO');

ALTER TABLE usuario ADD COLUMN permissao tipo_permissao NOT NULL DEFAULT 'FUNCIONARIO';

UPDATE usuario SET permissao='ADM_GERAL';