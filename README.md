# 📅 Sistema de Agendamento Inteligente com Previsão de No-Show

> Uma API RESTful que une Spring Boot e Inteligência Artificial para otimizar agendas e prever faltas de clientes, mitigando prejuízos financeiros.

## 🎯 Sobre o projeto

O "no-show" (quando um cliente agenda um horário e não comparece) é uma das maiores dores em negócios baseados em serviços, gerando ociosidade na equipe e impacto direto no faturamento. Solucionar esse problema exige mais do que um simples CRUD de agendamentos; exige previsibilidade.

Este sistema foi projetado para ser uma solução completa de gestão de agendas com um diferencial estratégico: um módulo integrado de Machine Learning. Ao analisar o histórico de marcações, o sistema calcula a probabilidade de um cliente faltar, permitindo que o estabelecimento adote medidas preventivas, como o envio de notificações de confirmação reforçadas, e libere automaticamente o horário quando o cliente não comparece dentro da tolerância de atraso.

Atualmente, o projeto encontra-se em desenvolvimento ativo, com a API core (Spring Boot) estruturada em arquitetura em camadas e a suíte de testes unitários da camada de serviço em expansão a cada entidade nova.

## ✨ Funcionalidades

- [x] **CRUD de Serviços, Profissionais e Expedientes:** gestão administrativa com regras de unicidade garantidas no banco e tratamento centralizado de exceções.
- [x] **Cadastro e Perfil de Clientes:** perfil vinculado 1:1 ao usuário, com mascaramento do CPF (LGPD), e com acesso restrito aos próprios dados.
- [x] **Autenticação e Autorização:** segurança baseada em JWT com controle de perfis (Admin vs. Cliente) e verificação de propriedade dos recursos, prevenindo falhas de IDOR.
- [ ] **Agendamento Inteligente:** validação de conflitos de horários em tempo real.
- [ ] **Previsão de Falta (IA):** integração com motor Python/Scikit-learn para score de probabilidade de no-show.
- [ ] **Notificações Preventivas:** alertas automáticos baseados na probabilidade de falta.
- [ ] **Job de Liberação de Horário:** rotina automatizada para liberar o horário após o limite de tolerância de atraso.

## 🏗️ Arquitetura e Decisões Técnicas

**Separação de Contextos (Spring Boot + Python)**
A arquitetura foi projetada com dois serviços: o de gestão (Spring Boot) e o motor de previsão (Python, em desenvolvimento). A decisão foi motivada por dois fatores:
1. **Ecossistema:** o Java/Spring é uma escolha consolidada para transações robustas e segurança. Já o ecossistema Python é o mais maduro para treino e inferência de modelos preditivos.
2. **Escalabilidade independente:** o processamento do modelo consome recursos de forma diferente do tráfego web, e separar os serviços permite evoluir cada um sem afetar o outro.

O Spring Boot é o único componente com acesso ao banco de dados: ele envia ao serviço de IA os dados de histórico necessários e recebe de volta a probabilidade de falta. Isso reduz a superfície de ataque e mantém uma única fonte de verdade para os dados.

**Segurança e Gestão de Sessão (JWT com HMAC)**
Optou-se por JWT com assinatura simétrica (HMAC). Como o serviço que emite o token é o mesmo que o valida, o HMAC oferece boa performance e menor complexidade de configuração em comparação a chaves assimétricas (RSA). A migração para RSA seria o caminho natural caso a autenticação venha a ser separada em um serviço próprio.

**Integridade de dados no banco**
Regras como unicidade de nome e de expediente por profissional e dia da semana são garantidas por constraints no PostgreSQL (versionadas com Flyway), e a aplicação apenas traduz a violação em uma resposta 409 amigável.

**Padrões de Qualidade e Testes**
Os testes unitários da camada de serviço usam JUnit 5 e Mockito, com o padrão *Object Mother* (factories) para gerar dados de teste de forma consistente. Cada fluxo de sucesso e de exceção de regra de negócio é coberto sem depender do banco de dados.

### Arquitetura e Diagramas

**Arquitetura e Fluxo de Negócio**

![Fluxo de Arquitetura](docs/diagramas/fluxo-arquitetura-sistema.svg)

**Fluxo de Autenticação**

![Fluxo de Autenticação](docs/diagramas/fluxo-autenticacao.drawio.png)

**Modelo Entidade-Relacionamento (MER)**

![MER](docs/diagramas/mer.drawio.png)

## 💻 Tecnologias Utilizadas

- **Backend:** Java 21, Spring Boot 3, Spring Security, Spring Data JPA, Hibernate, MapStruct, Lombok, Maven.
- **Banco de Dados:** PostgreSQL, migrations com Flyway.
- **Inteligência Artificial (em desenvolvimento):** Python, Pandas, Scikit-learn.
- **Infraestrutura:** Docker, Docker Compose, GitHub Actions (CI).
- **Testes:** JUnit 5, Mockito.

## 🚀 Como rodar localmente

### Pré-requisitos
- Java 21
- Docker e Docker Compose
- Maven

### Passo a passo

1. Clone o repositório
```bash
git clone https://github.com/lucianosantos-dev/sistema-agendamento.git
cd sistema-agendamento
```

2. Configure as variáveis de ambiente
```bash
cp .env.example .env
# Preencha os valores necessários (credenciais, JWT secret, etc.) no arquivo .env
```

3. Suba a infraestrutura de dados com Docker
```bash
docker compose up -d
```

4. Execute a aplicação Spring Boot
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

## 📚 Documentação Adicional

- [Documento de Requisitos](docs/requisitos/requisitos.md)
- [Diretório de Diagramas de Arquitetura](docs/diagramas/)

## 👨‍💻 Autor

**Luciano Santos**
Desenvolvedor Backend Java | Foco em arquitetura de software e IA aplicada

[LinkedIn](https://www.linkedin.com/in/lucianosantos-dev) · [GitHub](https://github.com/lucianosantos-dev)