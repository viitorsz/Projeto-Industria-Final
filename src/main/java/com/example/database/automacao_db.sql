CREATE DATABASE automacao_db;
USE automacao_db;

CREATE TABLE automacaorh (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_automacao VARCHAR(100) NOT NULL,
    responsavel varchar(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    descricao varchar(500) not null,
    operacao varchar(100) not null,
    setor varchar(100) not null,
    localizacao varchar(100) not null,
    situacao varchar(100) not null,
    prioridade varchar(100) not null,
    cpf varchar(100) not null
    );

CREATE TABLE automacaoEst (
    id int not null auto_increment PRIMARY KEY,  
    material VARCHAR(100) NOT NULL,
    descricao VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL CHECK (quantidade >= 0),
    estado VARCHAR(50) NOT NULL
);

CREATE TABLE automacaoQA (
    id int not null auto_increment PRIMARY KEY,
    produto INT NOT NULL CHECK (produto >= 0),
    selo VARCHAR(100) NOT NULL,
    descricao VARCHAR(100) NOT NULL,
    caso VARCHAR(100) NOT NULL,
    chegada VARCHAR(50) NOT NULL,
    saida VARCHAR(50) NOT NULL,
    porcentagem VARCHAR(100) NOT NULL
);

CREATE TABLE automacaoProducao (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
    nome_produto VARCHAR(100) NOT NULL,
    preco VARCHAR(100) NOT NULL,
    lote INT NOT NULL CHECK (lote >= 0),
    codigo INT NOT NULL CHECK (codigo >= 0)
);

CREATE TABLE automacaoFinanceiro(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome_automacaoFin varchar(100) not null,
    descricaoFin VARCHAR(100) not null, 
    setorFin varchar(100) not null, 
    categoriaFin VARCHAR(100) not null,
    estadoFin varchar(100) not null
);



-- Automação RH
INSERT INTO automacaorh (
    nome_automacao, responsavel, categoria, descricao, operacao, setor, localizacao, situacao, prioridade
) VALUES (
    'Integração Automática de Novos Funcionários',
    'Ana Lima',
    'Onboarding',
    'Automatiza o processo de integração de novos colaboradores, incluindo geração de e-mails, envio de documentos e configuração de acessos.',
    'Cadastro e comunicação',
    'Recursos Humanos',
    'Bloco Administrativo 1',
    'Ativo',
    'Alta'
);

-- Automação Estoque (Est)
INSERT INTO automacaoEst (
    material, descricao, quantidade, estado
) VALUES 
(
    'Sensor de Proximidade',
    'Controle de entrada e saída automatizada de sensores de proximidade no estoque técnico.',
    120,
    'Disponível'
),
(
    'Placa Controladora',
    'Reposição automatizada de placas controladoras utilizadas na produção.',
    45,
    'Em uso'
);

-- Automação Qualidade (QA)
INSERT INTO automacaoQA (
    produto, selo, descricao, caso, chegada, saida, porcentagem
) VALUES (
    1023,
    'Selo Verde',
    'Inspeção automatizada de qualidade em produtos eletrônicos.',
    'Teste de funcionamento completo',
    '2025-05-10',
    '2025-05-11',
    '98.5%'
);

-- Automação Produção
INSERT INTO automacaoProducao (
    nome_produto, preco, lote, codigo
) VALUES 
(
    'Painel Solar Inteligente',
    '1349.90',
    500,
    874562
),
(
    'Controlador de Energia',
    '359.50',
    1200,
    587912
);

-- Automação Financeiro
INSERT INTO automacaoFinanceiro (
    nome_automacaoFin, descricaoFin, setorFin, categoriaFin, estadoFin
) VALUES 
(
    'Controle de Pagamentos Recorrentes',
    'Automação de lançamentos e vencimentos de contas fixas e variáveis.',
    'Financeiro',
    'Contas a Pagar',
    'Ativo'
),
(
    'Geração Automática de Relatórios Mensais',
    'Geração de relatórios financeiros e envio automático para diretoria.',
    'Financeiro',
    'Relatórios',
    'Ativo'
);