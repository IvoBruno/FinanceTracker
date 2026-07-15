# FinanceTracker

FinanceTracker e um servico web desenvolvido em Java e Spring Boot para gerenciar transacoes financeiras e calcular estatisticas agregadas em tempo real. Os dados das transacoes sao mantidos em memoria de forma concorrente.

## Tecnologias Utilizadas

* Java 21
* Spring Boot 4.1.0
* Spring Web
* Spring Validation
* JUnit 5 e MockMvc (para testes automatizados)
* Maven (gerenciador de dependencias e build)

## Funcionalidades e Regras de Negocio

* Criacao de Transacoes: Permite o registro de uma transacao financeira contendo um valor (amount) e uma data/hora (dateTime).
* Validacoes na Criacao:
  * O valor da transacao e obrigatorio e deve ser estritamente maior que zero.
  * A data e hora sao obrigatorias e devem seguir o formato ISO-8601 (yyyy-MM-dd'T'HH:mm:ss).
* Exclusao de Transacoes: Permite limpar todas as transacoes registradas na memoria.
* Calculo de Estatisticas: Calcula em tempo real informacoes agregadas sobre as transacoes salvas, incluindo:
  * Quantidade total de transacoes
  * Soma de todos os valores
  * Media de todos os valores
  * Valor minimo registrado
  * Valor maximo registrado

## Estrutura do Projeto

O projeto segue a estrutura padrao do Maven para aplicacoes Spring Boot:

* src/main/java/project/financetracker/
  * FinanceTrackerApplication.java: Classe principal de inicializacao da aplicacao.
  * controllers/TransactionController.java: Endpoint REST que gerencia a entrada e saida de dados.
  * records/TransactionRecord.java: DTO para representacao de uma transacao e suas regras de validacao.
  * records/StatisticsRecord.java: DTO que contem as estatisticas calculadas.
* src/test/java/project/financetracker/
  * controllers/TransactionControllerTest.java: Testes unitarios e de integracao da API utilizando MockMvc.

## Documentacao da API REST

### 1. Criar Transacao
Registra uma nova transacao financeira em memoria.

* Metodo: POST
* URL: /api/transactions
* Headers: Content-Type: application/json
* Corpo da Requisicao:
```json
{
  "amount": 120.50,
  "dateTime": "2026-07-15T11:00:00"
}
```

* Respostas:
  * 200 OK: Transacao criada com sucesso. Retorna o texto "Transaction created".
  * 400 Bad Request: Ocorreu um erro de validacao (ex: valor nulo, valor menor ou igual a zero, data/hora nula ou com formato invalido).

### 2. Excluir Transacoes
Limpa a lista de transacoes mantida em memoria.

* Metodo: DELETE
* URL: /api/transactions
* Respostas:
  * 200 OK: Lista de transacoes esvaziada com sucesso. Retorna o texto "Transaction list has been deleted".

### 3. Obter Estatisticas
Retorna o calculo estatistico consolidado de todas as transacoes registradas em memoria.

* Metodo: GET
* URL: /api/transactions
* Respostas:
  * 200 OK: Retorna o JSON com as estatisticas calculadas.
    ```json
    {
      "quantidade": 2,
      "soma": 170.50,
      "media": 85.25,
      "minimo": 50.00,
      "maximo": 120.50
    }
    ```
  * 404 Not Found: Nenhuma transacao registrada na memoria no momento da consulta.

## Execucao da Aplicacao

### Pre-requisitos
* Java JDK 21 instalado e configurado nas variaveis de ambiente.

### Passos para Iniciar o Servidor
No diretorio raiz do projeto, execute o comando correspondente ao seu sistema operacional:

* Em sistemas Linux/macOS:
  ```bash
  ./mvnw spring-boot:run
  ```

* Em sistemas Windows (Command Prompt ou PowerShell):
  ```cmd
  mvnw.cmd spring-boot:run
  ```

O servidor sera iniciado por padrao na porta 8080. A API podera ser acessada em http://localhost:8080/api/transactions.

## Execucao dos Testes Automatizados

Os testes automatizados foram construidos com base em particionamento de equivalencia para cobrir casos validos, invalidos e de limite de dados.

Para rodar todos os testes do projeto, execute:

* Em sistemas Linux/macOS:
  ```bash
  ./mvnw test
  ```

* Em sistemas Windows (Command Prompt ou PowerShell):
  ```cmd
  mvnw.cmd test
  ```
