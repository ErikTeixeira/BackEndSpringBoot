# Projeto Cadastro Usuário

Usuários e Tarefas (cada usuário pode ter uma lista de tarefas)

- Usuário
  - id
  - Nome
  - Sobrenome
  - Idade
  - Email
  - Senha
  - Telefone
  - Dia do aniversário
  - Imagem de perfil

- Tarefas
  - id
  - título
  - descrição
  - status (por exemplo: “pendente”, “em andamento”, “concluída”)
  - prioridade (por exemplo: “baixa”, “média”, “alta”)
  - categoria (ex: “trabalho”, “pessoal”, “estudo”; opcional)
  - usuário_id (chave estrangeira para associar à entidade Usuário)

