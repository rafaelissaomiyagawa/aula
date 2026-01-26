# Roteiro da Aula Magna: Código de Qualidade com Spring Boot

**Data:** 27 de Janeiro de 2026
**Duração Alvo:** 40-50 minutos
**Tema:** Transformando código "que funciona" em código "profissional" através de Refatoração, TDD com Gradle e Testes de Integração.

---

## 💡 Kit Didático (Para Iniciantes)

### 🎯 Estratégia de Aula: "Foco Laser" (Contexto Mínimo)
*   **O Gancho de Venda:** Não explique como criar Controllers ou Repositories. Trate-os como "Caixas Pretas" que funcionam.
*   **Discurso:** *"Pessoal, no curso completo nós construímos as APIs e o Banco do zero. Hoje, eu quero que vocês confiem que os dados chegam até aqui. Vamos focar onde o dinheiro é ganho ou perdido: na **Regra de Negócio** (Service)."*

### 🥪 Analogias de Apoio (Só se alguém perguntar)
*   **O que é Spring Boot?**
    *   *Analogy:* "É como uma **Empreiteira**. Em vez de você fabricar o tijolo e a fiação, a empreiteira te entrega a estrutura pronta. Você foca em decorar a casa (Regra de Negócio)."
*   **As Camadas (Visão Simplificada):**
    *   **Controller:** A Porta de Entrada. (Não mexe hoje).
    *   **Repository:** O Banco de Dados. (Não mexe hoje).
    *   **Service:** **O Cérebro.** É aqui que a mágica acontece e onde vamos operar.
*   **Por que não na Entidade? (A Polêmica):**
    *   *Analogy:* "A Entidade `Order` é a **Comanda de Papel**. Ela tem os dados (valor, itens), mas papel não pensa. Papel não envia e-mail. Quem 'pensa' é o Chef (Service/Policy)."
4.  **Injeção de Dependência:**
    *   *Analogy:* "O Spring deixa a caixa de ferramentas na sua mesa. Você não precisa ir comprar um martelo, você só declara: 'Preciso de um martelo' (`@Autowired`) e ele aparece."

---

## 🕒 Cronograma (40-50 min)

| Tempo | Seção | Atividade Principal |
| :--- | :--- | :--- |
| **00-05** | **Intro** | Apresentação, Contexto do Curso, "O que é Qualidade?". |
| **05-15** | **A Dor** | Implementação "Suja" no Service + Dificuldade de Testar. |
| **15-30** | **A Cura** | Refatoração (Extract Class), TDD com Gradle `--continuous`. |
| **30-40** | **A Garantia** | Integração com Spring Boot e Testcontainers. |
| **40-45** | **Fechamento** | Recap, "Isso é só o começo", Venda do Curso. |
| **45-50** | **Q&A** | Perguntas finais. |

---

## 🚀 Feature Request: O Cliente VIP
**O Desafio:** O Product Owner (PO) solicitou uma nova regra de negócio para fidelização.
*   **Regra Atual:** Frete grátis para todos os pedidos acima de **R$ 100,00**.
*   **Nova Regra:** Se o cliente for **VIP**, o benefício do frete grátis deve ser aplicado a partir de **R$ 50,00**.
*   **Requisito Técnico:** O sistema deve ser resiliente. Se a integração antiga não enviar a informação de "VIP", o sistema deve assumir que o cliente é "Standard" (não quebrar).

---

## 🛠️ Passo a Passo Técnico

### 0. Setup Inicial (Antes da Live)
*   **IDE:** Aberta no projeto.
*   **Abas Abertas:** `OrderService.java`, `libs.versions.toml`, `build.gradle`.
*   **Terminal:** Limpo e pronto.
*   **Snippet Pronto:** Tenha o código do `ShippingPolicy` e do Teste num bloco de notas para copiar se o tempo apertar.
*   **Modificação Prévia:** Adicione `boolean isVip` ao record `OrderRequest.java` para não perder tempo com DTOs.

### 1. Introdução (5 min)
*   **Pitch:** "Todo mundo sabe fazer um `if`. Mas onde colocar esse `if` é o que define a senioridade."
*   **Ferramentas:** Mostrar rapidinho o `libs.versions.toml` (Version Catalog) e o `spotless` no `build.gradle` como exemplos de automação que "tiram a sujeira da frente".

### 2. O Cenário "Sujo" (10 min)
*   **Contexto:** PO pediu Frete Grátis para VIPs acima de R$ 50 (Normal é R$ 100).
*   **Ação:** Editar `OrderService.placeOrder`.
    ```java
    // No final do método placeOrder, substituir a lógica existente:
    
    // COMO GERALMENTE FAZEMOS (Acoplado)
    BigDecimal freeShippingLimit = new BigDecimal("100.00");
    if (request.isVip()) {
        freeShippingLimit = new BigDecimal("50.00");
    }

    if (totalAmount.compareTo(freeShippingLimit) > 0) {
        order.setFreeShipping(true);
        // log...
    }
    ```
*   **A Dor:** Tente (ou simule tentar) escrever um teste unitário para o `OrderService`.
    *   *Argumento:* "Olha quanta coisa eu preciso mockar (`ProductRepository`, `Publisher`, `Event`) só para testar uma matemática simples de `50 > 100`."

### 2.1. O Momento de Arquitetura (A Discussão) - "Por que não na Entidade?"
*   **A Provocação:** "Pessoal, o `Order` tem o `totalAmount`. Por que não colocamos um método `order.calculateShipping()`? Isso não seria mais Orientação a Objetos?"
*   **A Análise (Rich vs Anemic):**
    *   "Sim! O ideal seria o Modelo Rico. Dados e Comportamento juntos."
*   **O "Porém" do Spring/JPA:**
    *   "Mas e se o valor de R$ 100 vier do `application.properties`? Ou de uma tabela de parâmetros no banco?"
    *   "A Entidade `Order` é criada pelo Hibernate, não pelo Spring. Ela não aceita `@Autowired` ou `@Value`. Ela nasce 'desconectada'."
*   **A Decisão Profissional:**
    *   "Para resolver isso, criamos uma **Policy** (ou Domain Service). É uma classe pura, gerenciada pelo Spring (pode ler configs), que recebe os dados e devolve a decisão."
    *   "O `OrderService` continua orquestrando, mas a regra vai para um especialista: `ShippingPolicy`."

### 3. A Refatoração & TDD (15 min)
*   **Solução:** "Vamos extrair a regra. O Service orquestra, ele não deve saber matemática de frete."
*   **Terminal:** Execute o modo contínuo do Gradle antes de codar o teste.
    ```bash
    ./gradlew test --tests ShippingPolicyTest --continuous
    ```
    *(Vai falhar ou dizer que não existem testes, ok)*.
*   **Coding (TDD):**
    1.  Crie `src/test/java/.../service/ShippingPolicyTest.java`.
    2.  Escreva o teste falhando (Red).
    3.  Crie a classe `src/main/java/.../service/ShippingPolicy.java` (Green).
    4.  Veja o terminal ficar verde instantaneamente.

### 3.1 Design Evolutivo & Null Safety
*   **A Preocupação:** "E se o cliente da API for antigo e não mandar o campo `isVip`? Ou se mandar `null`?"
*   **Design Defensivo:**
    *   No `OrderRequest`, usamos `Boolean` (Wrapper) em vez de `boolean` (primitivo) para permitir nulo.
    *   Na `ShippingPolicy`, tratamos o nulo com `Boolean.TRUE.equals(isVip)`.
    *   *Lição:* "Código maduro não quebra com `NullPointerException`. Ele tem padrões seguros (fallback)."

*   **Refatoração (Blue/Refactor):**
    *   Melhore os nomes, extraia constantes (`STANDARD_LIMIT`, `VIP_LIMIT`) dentro da Policy.
    *   *Argumento:* "Isso é Feedback Loop curto. Eu tenho certeza que minha regra funciona em milissegundos."

### 4. Integração (10 min)
*   **Conectando os pontos:**
    1.  Injete `ShippingPolicy` no `OrderService` (Lombok `@RequiredArgsConstructor` ajuda).
    2.  Substitua o `if` feio pela chamada elegante:
        ```java
        // Passamos o valor, mesmo que seja null. A Policy que se vire.
        if (shippingPolicy.shouldApplyFreeShipping(totalAmount, request.isVip())) { ... }
        ```
*   **Teste de Integração:**
    *   Vá para `InventoryControllerIntegrationTest`.
    *   Mostre/Crie um teste rápido `shouldApplyFreeShippingForVipOrder`.
    *   Rode com `./gradlew integrationTest`.
    *   *Argumento:* "O teste unitário garantiu a lógica. Esse teste garante que o Spring conseguiu injetar tudo e que o banco salvou corretamente. É a dupla defesa."

### 5. Conclusão (5 min)
*   **Recap:**
    1.  Começamos com código acoplado (difícil de testar).
    2.  Refatoramos para `ShippingPolicy` (SRP - Princípio da Responsabilidade Única).
    3.  Usamos Gradle Continuous para produtividade.
    4.  Fechamos com Teste de Integração para segurança.
*   **Call to Action:** "No curso completo, fazemos isso para o sistema todo, com Docker, CI/CD e muito mais. Inscrevam-se."

---

## 📝 Snippets de Código (Para Emergência)

**ShippingPolicy.java**
```java
@Component
public class ShippingPolicy {
    private static final BigDecimal STANDARD_LIMIT = new BigDecimal("100.00");
    private static final BigDecimal VIP_LIMIT = new BigDecimal("50.00");

    // Recebe Boolean (wrapper) para suportar nulos de clientes legados
    public boolean shouldApplyFreeShipping(BigDecimal amount, Boolean isVip) {
        // Design Defensivo: Trata null como false (Retrocompatibilidade)
        boolean safeIsVip = Boolean.TRUE.equals(isVip);
        
        BigDecimal limit = safeIsVip ? VIP_LIMIT : STANDARD_LIMIT;
        return amount.compareTo(limit) > 0;
    }
}
```

**ShippingPolicyTest.java**
```java
class ShippingPolicyTest {
    private final ShippingPolicy policy = new ShippingPolicy();

    @Test
    void vipShouldHaveLowerLimit() {
        assertTrue(policy.shouldApplyFreeShipping(new BigDecimal("51.00"), true));
    }
    
    @Test
    void standardShouldHaveHigherLimit() {
        assertFalse(policy.shouldApplyFreeShipping(new BigDecimal("99.00"), false));
    }
    
    @Test
    void nullVipShouldBeTreatedAsStandard() {
        // Garante que não quebra com clientes antigos
        assertFalse(policy.shouldApplyFreeShipping(new BigDecimal("99.00"), null));
        assertTrue(policy.shouldApplyFreeShipping(new BigDecimal("101.00"), null));
    }
}
```
