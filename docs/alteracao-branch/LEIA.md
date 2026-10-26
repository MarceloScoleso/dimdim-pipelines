# Alteração de código do passo 06 (feita NA BRANCH da Task)

Nova regra de negócio: **rendimento da poupança** (`Conta.aplicarRendimento`).
Ela aparece nos testes: o CI passa de **11 para 13 testes**.

No Azure Repos, com a branch da Task selecionada:

1. `src/main/java/br/com/fiap/dimdim/model/Conta.java` > Edit > apague tudo > cole o conteúdo de `docs/alteracao-branch/Conta.java` > Commit (marque a Task no commit).
2. `src/test/java/br/com/fiap/dimdim/model/ContaTest.java` > Edit > apague tudo > cole `docs/alteracao-branch/ContaTest.java` > Commit.
3. `src/main/resources/application.properties` > troque `app.versao=1.0.0` por `app.versao=1.1.0` > Commit.
   (Assim o `GET /` da API no Azure mostra a versão nova depois do deploy.)

Depois abra o Pull Request da branch para a `main`.
