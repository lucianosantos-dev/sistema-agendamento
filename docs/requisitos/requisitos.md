# Sistema de Agendamentos com Integração de IA

## Visão Geral e Fluxo do Cliente

Como dono de um negócio preciso de um sistema no qual meus clientes consigam agendar um horário comigo de forma simples e segura.

* **Sugestão Automática de Horários:** Se no momento do agendamento o horário desejado estiver indisponível, o sistema deve sugerir o horário disponível mais próximo na grade de horários disponível no dia selecionado pelo cliente. O limite de tolerância por atraso é de apenas 15 minutos, evitando constrangimento.  
* **Notificações Instantâneas:** Assim que o cliente realizar o agendamento, deve receber uma confirmação via WhatsApp ou e-mail. Em caso de falha o sistema deve informar ao cliente e dar a ele o direito de fazer uma nova tentativa, se por algum motivo ele tentar 2 vezes deve fazer um agendamento manual direto com estabelecimento, garantindo uma boa relação.  
* **Análise Preditiva (IA):** Ao agendar, o sistema deve reunir as informações dos últimos agendamentos e calcular a probabilidade de atraso. Com base no histórico, o sistema deve prever se o cliente pode faltar e enviar uma notificação preventiva com uma média entre 30 e 40 minutos de antecedência, dando ao cliente uma margem de tempo.

## Cadastro e Área do Cliente

O sistema deve ser intuitivo para o cliente:

* **Agendamento Rápido:** Como **cliente** preciso preencher os dados para agendamento como nome, e-mail, telefone, horário e o serviço. Ao clicar em "Agendar", o cliente recebe a confirmação por e-mail ou WhatsApp.   
* **Cadastro e Login:** Como **cliente** preciso de um cadastro  simples e acesso via e-mail ou username e senha.  
* **Histórico e Gestão de Agendamentos:** Como **cliente** preciso de um painel interativo para acompanhar os meus últimos agendamentos filtrando por *cancelados*, *remarcados* e *pendentes*.  
* **Reagendamento:** Como **cliente** devo ter a possibilidade de remarcar um outro horário seguindo as regras de disponibilidade de horários e profissionais para o serviço escolhido. ex: horário das 17:00 \- profissional Marcos, só posso agendar se esse profissional estiver disponível, caso contrário devo escolher outro profissional no horário escolhido e com profissional livre para atendimento naquele horário.

#
# Parte Administrativa do Sistema

## Painel Geral e Clientes

* **Acesso e Dashboard:** Como **admin** preciso de um cadastro único para gestão e administração do sistema. Após o login, exibe a tela inicial com os agendamentos filtrados pelas últimas 8 horas, e do dia e da semana.  
* **Gestão de Clientes:** Como **admin** preciso de uma tela dedicada com informações e dados de todos os clientes, contendo um botão para envio de alertas para prevenir faltas e esperas desnecessárias.

## Gestão de Profissionais, Serviços e Horários

* **Profissionais e Serviços:** Como **admin** preciso de uma tela onde eu possa fazer o cadastro de profissionais e vínculos aos seus respectivos serviços. Permitindo ao cliente escolher o profissional e o serviço desejado.  
* **Regras de Agendamento por Serviço:** Um profissional pode ter 1 ou mais serviços associados, mas só pode estar disponível para um serviço por vez. Exemplo: se um cliente agendar "corte e barba" às 10h com João, outro cliente só poderá agendar com João em outro horário, considerando a média de tempo metrificada para os serviços.  
* **Horários e Dias de Funcionamento:** Como **admin** preciso de uma tela onde eu possa fazer um cadastro completo de horários e dias de funcionamento do estabelecimento.  
* **Filtros de Busca:** Como **admin** preciso de uma tela na qual eu consiga filtrar por profissionais, por serviços, além de filtros para clientes agendados nas últimas 8 horas, cancelados, remarcados, etc.


**Requisitos não Funcionais**

1. A senha do cliente e admin devem ser ser salvas no Banco por meio de alguma ferramenta de criptografia ex: BCript  
2. O sistema deve expor somente dados que não forem sensíveis ex: nome, email, telefone  
3. Os endpoints devem ser acessados com autenticação via token  
4. usuários comuns não podem ter acesso a rotas pertencentes a um usuário admin  
5. O  token deve ser expirado após 30 minutos de inatividade  
6. O sistema deve permitir até 100 requisições simultâneas para MVP  
7. O usuário comum deve ter um limite de até 5 requisições para endpoints como cadastro e login  
8. Dados sensíveis trafegam somente por HTTPS  
9. Clientes que não retornaram nos últimos 3 meses devem ser inativados  
10. O sistema só deve ficar fora do ar no caso de manutenções e isso somente nos fins de semana. Somente em caso extremo deve parar para manutenção.  
    

**Funcionalidades Futuras**

* **Avaliações e Incentivos:** Como  
*  **admin** preciso de um módulo  para que meus  clientes avaliem o funcionário prestador do serviço, servindo como incentivo, acompanhamento de metas batidas, premiações individuais, etc.

## Resumo do Administrador

Em resumo, o administrador pode gerenciar de forma centralizada todos os horários, clientes e profissionais do sistema.

    
