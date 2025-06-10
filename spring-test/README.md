# Non-Functional Testing: Security Testing

## 1. Introdução sobre o Tema

### 1.1 Histórico
O teste de segurança (Security Testing) surgiu como resposta à crescente preocupação com a proteção de sistemas computacionais contra ameaças, vulnerabilidades e ataques. Com o avanço da internet e a popularização de aplicações web, a necessidade de garantir a confidencialidade, integridade e disponibilidade das informações tornou-se fundamental. Desde os anos 2000, com o aumento dos ataques cibernéticos, o Security Testing passou a ser uma etapa obrigatória no ciclo de desenvolvimento de software.

### 1.2 O que é?
Security Testing é uma abordagem de testes não-funcionais que visa identificar vulnerabilidades, ameaças e riscos em um sistema, garantindo que dados e recursos estejam protegidos contra possíveis invasões ou acessos não autorizados. O objetivo é avaliar se a aplicação está devidamente protegida contra ataques como SQL Injection, Cross-Site Scripting (XSS), Cross-Site Request Forgery (CSRF), entre outros.

### 1.3 Qual o propósito?
O propósito do Security Testing é assegurar que o sistema:
- Protege informações sensíveis.
- Restringe acessos não autorizados.
- Resiste a ataques conhecidos.
- Cumpre requisitos de conformidade e regulamentação.

### 1.4 Principais vantagens e desvantagens
**Vantagens:**
- Redução de riscos de ataques e vazamento de dados.
- Melhoria da confiança dos usuários e clientes.
- Cumprimento de normas e regulamentações.
- Prevenção de prejuízos financeiros e danos à reputação.

**Desvantagens:**
- Pode aumentar o tempo e custo de desenvolvimento.
- Requer conhecimento especializado.
- Ferramentas avançadas podem ser caras.
- Nem todas as vulnerabilidades podem ser identificadas apenas com testes automatizados.

### 1.5 Exemplos de ferramentas/frameworks

A seguir, detalho algumas das principais ferramentas e frameworks para testes de segurança, indicando também quais foram utilizadas neste repositório:

- **OWASP ZAP**: Ferramenta open-source para análise de segurança em aplicações web, focada em identificar vulnerabilidades como XSS, CSRF e SQL Injection por meio de testes dinâmicos (DAST). Não foi utilizada diretamente neste repositório, mas é recomendada para testes de segurança em ambientes de homologação e produção.

- **Burp Suite**: Plataforma integrada para testes de segurança em aplicações web, muito utilizada por profissionais de pentest para interceptar, modificar e analisar o tráfego HTTP. Não foi utilizada neste repositório, mas pode complementar a análise de segurança manual.

- **SonarQube**: Ferramenta de análise contínua de código fonte, capaz de identificar vulnerabilidades, bugs e code smells. Não foi utilizada neste repositório, mas pode ser integrada ao pipeline de CI/CD para inspeção estática de código.

- **Spring Security**: Framework utilizado neste projeto para implementar autenticação e autorização. Ele protege os endpoints REST exigindo credenciais válidas para acesso, além de permitir a configuração de regras de acesso detalhadas.

- **JUnit**: Framework de testes unitários utilizado neste repositório para criar e executar testes automatizados, incluindo testes de segurança. Os testes garantem que endpoints protegidos exigem autenticação e que acessos não autorizados são corretamente bloqueados.

**Ferramentas utilizadas neste repositório:**

- **Spring Security**: Responsável pela implementação da camada de segurança da aplicação, configurando autenticação básica HTTP e restrição de acesso aos endpoints.
- **JUnit**: Utilizado para a criação de testes automatizados, incluindo testes de segurança presentes no arquivo `SecurityTests.java`.

Ferramentas como OWASP ZAP, Burp Suite e SonarQube são recomendadas para complementar a segurança, mas não foram aplicadas diretamente neste exemplo prático.

---

## 2. Ilustração lúcida sobre o exemplo prático

Neste projeto, foi desenvolvida uma aplicação Java Spring com endpoints REST para manipulação de dados de frutas. A segurança foi implementada utilizando o Spring Security, configurando autenticação básica HTTP e restrição de acesso aos endpoints. Foram criados testes automatizados para validar o comportamento seguro da aplicação, como a exigência de autenticação e a proteção contra acessos não autorizados.

---

## 3. Demonstração do exemplo prático

### Estrutura do Projeto

```
src/
  main/
    java/
      com/javabycode/
        controller/
        security/
        ...
  test/
    java/
      SecurityTests.java
```

### Principais arquivos de segurança
- `SecurityConfiguration.java`: Configura as regras de autenticação e autorização.
- `MyBasicAuthenticationEntryPoint.java`: Personaliza a resposta para falhas de autenticação.
- `SecurityTests.java`: Testes automatizados para validar a segurança dos endpoints.

### Como executar

1. **Build do projeto:**
   ```bash
   mvn clean install
   ```

2. **Executar a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

3. **Executar os testes de segurança:**
   ```bash
   mvn test
   ```

4. **Exemplo de requisição protegida:**
   ```bash
   curl -u user:password http://localhost:8080/fruits
   ```

### Exemplo de teste automatizado
No arquivo `SecurityTests.java`, são realizados testes para garantir que endpoints protegidos exigem autenticação e que acessos não autorizados são bloqueados.

---

## 4. Referências

- OWASP Foundation. [OWASP ZAP](https://owasp.org/www-project-zap/)
- PortSwigger. [Burp Suite](https://portswigger.net/burp)
- SonarSource. [SonarQube](https://www.sonarqube.org/)
- Spring. [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- Pressman, R. S. (2016). Engenharia de Software. McGraw-Hill.
- Sommerville, I. (2011). Engenharia de Software. Pearson.
- JUnit. [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

---

## Expansão dos Tópicos para Enriquecimento do Trabalho

### Histórico (complementar)
- Surgimento do OWASP (Open Web Application Security Project) em 2001, referência mundial em segurança de aplicações.
- Evolução dos ataques cibernéticos e resposta da indústria com normas como ISO/IEC 27001 e PCI DSS.
- Integração dos testes de segurança em práticas modernas como DevSecOps e pipelines de CI/CD.

### O que é? (complementar)
- Security Testing pode ser realizado de forma dinâmica (DAST), estática (SAST) ou interativa (IAST).
- Pode envolver testes manuais (pentest) ou automatizados.

### Qual o propósito? (complementar)
- Garantir que senhas não sejam armazenadas em texto claro.
- Validar expiração correta de sessões.
- Assegurar que dados sensíveis não sejam expostos em logs.

### Principais vantagens e desvantagens (exemplos reais)
- Empresas que sofreram vazamentos por falta de testes de segurança (ex: Yahoo, Equifax).
- Impacto positivo na reputação e conformidade legal ao adotar práticas de Security Testing.

### Exemplos de ferramentas/frameworks (categorias ampliadas)
- **SAST:** SonarQube, Checkmarx, Fortify.
- **DAST:** OWASP ZAP, Burp Suite, Acunetix.
- **Gerenciamento de dependências:** Snyk, Dependabot.
- **Frameworks de autenticação/autorização:** Spring Security, OAuth2, Keycloak.
- **Testes unitários e de integração:** JUnit, Mockito.

### Ilustração lúcida sobre o exemplo prático (diagrama em texto)

Fluxo de autenticação:
```
Usuário → [Requisição HTTP] → [Spring Security] → [Validação de Credenciais] → [Controller]
```
Se não autenticado:
```
Usuário → [Requisição HTTP] → [Spring Security] → [Erro 401 Unauthorized]
```

### Demonstração do exemplo prático (complementar)
- Trechos de código relevantes:
  - Configuração de segurança (`SecurityConfiguration.java`)
  - Teste de endpoint protegido (`SecurityTests.java`)
- Sugestão de integração de ferramentas externas:
  - Rodar OWASP ZAP manualmente ou via CI/CD para análise dinâmica.

### Referências adicionais
- OWASP Top 10: https://owasp.org/www-project-top-ten/
- ISO/IEC 27001:2013
- IEEE Security & Privacy (revista)
- ACM Computing Surveys (revista)

---
