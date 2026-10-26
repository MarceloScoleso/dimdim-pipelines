-- =====================================================================
-- Consultas de evidencia do CRUD (rodar no Editor de consultas do Portal
-- depois de CADA operacao feita pela API)
-- =====================================================================

-- 1) Clientes
SELECT * FROM dbo.cliente ORDER BY id;

-- 2) Contas
SELECT * FROM dbo.conta ORDER BY id;

-- 3) Relacionamento (JOIN cliente x conta)
SELECT cl.id   AS cliente_id,
       cl.nome AS cliente,
       cl.cpf,
       co.id   AS conta_id,
       co.agencia,
       co.numero,
       co.tipo,
       co.saldo
FROM dbo.cliente cl
LEFT JOIN dbo.conta co ON co.cliente_id = cl.id
ORDER BY cl.id, co.id;

-- 4) Estrutura das tabelas (mostra FK e colunas)
SELECT fk.name AS fk, OBJECT_NAME(fk.parent_object_id) AS tabela_filha,
       OBJECT_NAME(fk.referenced_object_id) AS tabela_pai
FROM sys.foreign_keys fk;
