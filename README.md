# Territory Predictive Insights (TPI)

### Global Solution — FIAP

## Integrantes

| Nome | RM |
|---|---:|
| Pedro Miranda Campos Riato | 562117 |
| Israel Karacsony de Camargo Nunes | 563435 |

---

## 1. Sobre o projeto

O **Territory Predictive Insights (TPI)** é um sistema de monitoramento territorial desenvolvido em Java com Spring Boot, com o objetivo de acompanhar condições ambientais que podem indicar riscos de alagamentos e outros eventos relacionados ao clima.

A aplicação simula sensores responsáveis por coletar informações de pluviometria, nível da água e umidade do solo. As medições são armazenadas em um banco de dados Oracle e utilizadas para gerar previsões e alertas, auxiliando na identificação de possíveis situações de risco.

A proposta é transformar dados ambientais em informações úteis para apoiar a tomada de decisão e contribuir para a prevenção de desastres.

## 2. Funcionalidades

- Simulação de três tipos de sensores: pluviômetro, nível da água e umidade do solo.
- Verificação do status operacional dos sensores.
- Registro e validação de medições ambientais.
- Armazenamento persistente de medições e alertas no Oracle.
- Previsões utilizando média móvel e comparação de medições.
- Geração automática de alertas nos níveis ATENÇÃO, ALERTA e CRÍTICO.
- Consulta ao histórico de medições e aos alertas registrados.
- Visualização do status geral do sistema.
- Gerenciamento dos limites de alerta por meio de código de acesso.

## 3. Tecnologias utilizadas

- **Java 17:** linguagem de programação.
- **Spring Boot 4.1.2:** estrutura principal da aplicação.
- **Spring Data JPA:** persistência e comunicação com o banco de dados.
- **Oracle Database:** armazenamento das medições e dos alertas.
- **Maven:** gerenciamento de dependências e execução do projeto.
- **JUnit 5:** testes automatizados.
- **Mockito:** simulação de dependências durante os testes.

## 4. Estrutura do projeto

A aplicação foi organizada em pacotes para separar responsabilidades e facilitar a manutenção:

| Pacote | Responsabilidade |
|---|---|
| `config` | Configurações e limites do sistema. |
| `dominio` | Sensores, estratégias de previsão e regras do domínio. |
| `entidade` | Entidades persistidas no banco de dados. |
| `excecao` | Exceções personalizadas. |
| `factory` | Criação das estratégias de previsão. |
| `menu` | Interface de interação pelo terminal. |
| `repository` | Acesso e consulta aos dados persistidos. |
| `service` | Regras de negócio e monitoramento. |

## 5. Conceitos de orientação a objetos

O projeto aplica conceitos fundamentais da programação orientada a objetos:

- **Abstração:** utilização das classes abstratas `ComponenteTerritorial` e `EstrategiaPrevisao`.
- **Herança:** os sensores concretos herdam características de `ComponenteTerritorial`, enquanto as estratégias herdam de `EstrategiaPrevisao`.
- **Encapsulamento:** atributos privados e métodos de acesso controlam a manipulação dos dados.
- **Polimorfismo:** diferentes sensores implementam a interface `Sensor`, e as estratégias possuem suas próprias implementações do método `prever()`.

## 6. Padrões de projeto

Foram utilizados dois padrões criacionais:

**Factory:** a classe `FabricaPrevisao` centraliza a criação das estratégias de previsão. Dessa forma, a lógica de instanciação não fica espalhada pelo sistema, facilitando a inclusão de novas estratégias.

**Singleton:** a classe `ConfiguracaoSistema` mantém uma instância compartilhada das configurações e dos limites de alerta. Isso permite que os componentes utilizem os mesmos valores durante a execução da aplicação.

## 7. Banco de dados e persistência

O projeto utiliza Oracle Database com Spring Data JPA para persistência dos dados.

Foram implementadas duas entidades principais:

- **Medicao:** armazena o tipo de sensor, a região, o valor medido e a data e hora do registro.
- **Alerta:** armazena a região, o nível do alerta, a descrição e a data e hora de sua geração.

Os dados são persistidos no banco, permitindo consultar o histórico mesmo após encerrar e iniciar novamente a aplicação.

### Configuração da conexão

As informações de conexão devem ser configuradas no arquivo:

`src/main/resources/application.properties`

É necessário informar os dados de acesso ao Oracle disponibilizado pela FIAP. As credenciais devem permanecer em configuração local ou variáveis de ambiente, sem serem publicadas no repositório.

## 8. Como executar o projeto

### Pré-requisitos

- Java 17 ou superior.
- Banco de dados Oracle acessível.
- Maven ou Maven Wrapper.

### Passo a passo

1. Clone o repositório:

   ```bash
   git clone https://github.com/Israel-Nunes/GS---territory-predictive-insightsterritory-predictive-insights---Java.git
   ```

2. Acesse a pasta do projeto:

   ```bash
   cd territory-predictive-insights
   ```

3. Configure a conexão com o Oracle no arquivo `application.properties`.

4. Execute os testes automatizados:

   ```powershell
   .\mvnw.cmd test
   ```

5. Inicie a aplicação com o menu habilitado:

   ```powershell
   .\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--app.menu.enabled=true"
   ```

## 9. Menu do sistema

Ao iniciar a aplicação, o usuário encontrará as seguintes opções:

| Opção | Funcionalidade |
|---|---|
| 1 | Verificar sensores e registrar medições. |
| 2 | Executar previsões. |
| 3 | Consultar histórico de medições. |
| 4 | Consultar alertas. |
| 5 | Exibir status geral. |
| 6 | Gerenciar dados do território. |
| 0 | Encerrar a aplicação. |

## 10. Testes automatizados

O projeto possui testes desenvolvidos com JUnit 5 e Mockito, utilizando o padrão Arrange, Act e Assert (AAA).

Os testes verificam:

- Cálculo da média móvel.
- Previsões por comparação.
- Validação e alteração dos limites do território.
- Tratamento de sensores indisponíveis.
- Registro de medições.
- Geração de alertas críticos.
- Inicialização do contexto Spring Boot.

Na última execução, foram obtidos os seguintes resultados:

- **10 testes executados.**
- **0 falhas.**
- **0 erros.**
- **BUILD SUCCESS.**

## 11. Reflexão crítica

Um sistema de previsão e monitoramento ambiental precisa reconhecer que seus resultados não representam a realidade de maneira absoluta. Por isso, é importante compreender suas limitações e identificar situações nas quais os dados disponíveis não são suficientes para gerar uma previsão confiável.

O sistema deve se calar quando não houver informações suficientes ou quando as medições apresentarem inconsistências. Por exemplo, um histórico com menos de cinco leituras pode não representar adequadamente o comportamento de uma região. Embora as estratégias implementadas consigam calcular uma previsão com um histórico menor, isso não significa que o resultado seja confiável. Em uma aplicação real, seria mais adequado sinalizar a insuficiência de dados e aguardar novas medições antes de apresentar uma previsão como confiável.

Outro ponto importante é compreender o que os dados não representam. Um único sensor por região não consegue descrever todas as condições ambientais de um território inteiro. O nível da água, a intensidade da chuva e a umidade do solo podem variar bastante entre diferentes pontos de uma mesma região. Além disso, fatores como drenagem, relevo, condições climáticas e infraestrutura urbana também influenciam os riscos de alagamento, mas não são completamente representados pelas medições utilizadas neste projeto.

Por fim, os alertas devem apoiar a decisão humana, e não decidir no lugar das pessoas. Um alerta indica uma condição que merece atenção, mas não possui conhecimento completo sobre o contexto de cada localidade. A avaliação de profissionais e autoridades responsáveis continua sendo essencial para interpretar as informações, verificar as condições reais e definir as medidas necessárias. Dessa forma, o sistema funciona como uma ferramenta de apoio ao monitoramento e à prevenção, sem substituir a responsabilidade humana.

---

**Territory Predictive Insights — Monitoramento inteligente para territórios mais seguros.**

Desenvolvido por Pedro Miranda Campos Riato e Israel Karacsony de Camargo Nunes — FIAP.