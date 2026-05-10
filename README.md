<h1>Sistema de Gestão de Pedidos - Raízes do Nordeste</h1>

Este repositório contém a implementação do Back-end para a rede de lanchonetes Raízes do Nordeste. A API foi desenvolvida utilizando Java e Spring Boot, focando em segurança (JWT), escalabilidade e conformidade com a LGPD.
<h2>Requisitos e Tecnologias</h2>

<ul>
  <li>Linguagem: Java 17</li>
  <li>Framework: Spring Boot 3.x</li>
  <li>Banco de Dados: MySQL 8.0</li>
  <li>Gerenciador de Dependências: Maven</li>
  <li>Segurança: Spring Security + JWT</li>
</ul>

<h2>Configuração das Variáveis de Ambiente</h2>

Crie um arquivo .env na raiz do projeto com as seguintes configurações:

MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=raizesdb

SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/raizesdb?createDatabaseIfNotExist=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root

SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true

SERVER_PORT=8080

<h2>Como Instalar e Rodar</h2>

<ol>
  <li>git clone https://github.com/LucasEduardoCampolino/raizesdonordeste.git</li>
  Na raíz do projeto:
  <li>Crie o arquivo .env e adicione o código como no exemplo acima</li>
  <li>docker compose up --build -d</li>
</ol>

<h2>Documentação da API (Swagger/OpenAPI)</h2>

A documentação interativa com todos os endpoints, modelos de dados e requisitos de autenticação pode ser acessada em:
Swagger UI: http://localhost:8080/swagger-ui/index.html

<h2>Testando com Postman</h2>

Para facilitar os testes de integração e o fluxo de pedidos, disponibilizamos uma Collection completa com todos os cenários (positivos e negativos) configurados.
Como importar:
<ul>
  <li>Localize o arquivo raizes_do_nordeste_collection.json na raiz deste projeto</li>
  <li>No seu Postman, clique em Import</li>
  <li>Arraste o arquivo ou selecione-o em sua pasta</li>
</ul>

Fluxo Automático de Autenticação:

A collection foi configurada com scripts que automatizam o processo de segurança:

Ao executar a requisição Login (Gerar Token) com sucesso, o sistema extrai o Token JWT da resposta e o salva automaticamente em uma variável global chamada {{token}}.

Todas as requisições subsequentes (Cadastro de Produtos, Pedidos, etc.) já utilizam essa variável no cabeçalho Authorization, dispensando o trabalho de copiar e colar o código manualmente.

Cenários de Teste Implementados

A collection cobre os seguintes requisitos do projeto:

<ul>
  <li>Autenticação: Login e acesso protegido</li>
  <li>LGPD: Validação de consentimento no cadastro de usuários</li>
  <li>Gestão de Estoque: Entrada de produtos e trava de segurança para estoque insuficiente</li>
  <li>Fluxo de Pedidos: Checkout completo, desde a escolha dos itens até o pagamento mockado</li>
  <li>Regras de Negócio: Aplicação de descontos e soma de pontos no programa de fidelidade</li>
</ul>
