# Data & Persistency — PnH-opdrachten (Hibernate)

Joris Fietje — herkansing Data & Persistency, Hogeschool Utrecht.

Dit is het project voor de **PnH-opdrachten**, waarin de persistentie met **Hibernate/JPA**
wordt gedaan. Het bevat **geen JDBC-code**: geen `*DaoPsql`-klassen, geen
`PreparedStatement`s en geen Psql-tests. De P-opdrachten staan in een aparte repository.

## Inhoud

| Map | Inhoud |
|---|---|
| `practicum/` | Het persistentiepracticum met Hibernate |
| `casus/` | SQL om de casusdatabases (ov-chipkaart, bedrijf, sales) aan te maken |

## Opzet van `practicum/src/main/java`

| Package | Inhoud |
|---|---|
| `domain` | De JPA-entities: `Reiziger` (`@Entity`, `@Table`, `@Id`, `@Column`) |
| `infra.dao` | De DAO-interfaces |
| `infra.hibernate` | De Hibernate-implementaties, via de `EntityManager` |
| `globals` | Database- en Hibernate-configuratie |
| `Main` | `testReizigerDAOHibernate()`, waarin elke CRUD-operatie wordt uitgevoerd |

De `EntityManager` wordt via dependency injection aan de DAO meegegeven, net zoals de
`Connection` dat in het P-project doet.

## Zelf draaien

Vul vóór het draaien je eigen PostgreSQL-wachtwoord in op twee plekken:

* `practicum/src/main/java/globals/Database.java` — gebruikt door het testraamwerk om de
  testdatabase aan te maken en op te ruimen
* `practicum/src/main/resources/META-INF/persistence.xml` — de verbinding die Hibernate zelf
  gebruikt

```bash
cd practicum
mvn test
```

Het testraamwerk maakt de database uit `globals/Hibernate.java` (`ovchiphibernate`) zelf aan
en ruimt die na afloop weer op.
