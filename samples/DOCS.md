# DOCS - Guida rapida d'uso del `baseframework` (sample)

Questa documentazione raccoglie i principali punti illustrati con commenti inline nel codice del sample.
È pensata per essere letta da uno sviluppatore che vuole usare il `baseframework` come base per un microservizio.

## Scopo del framework
- Fornire building block riutilizzabili: `BaseEntity`, `GenericRepository`, `GenericCrudService`, `GenericCrudController`, configurazioni condivise (Kafka, Security), `GenericMapper` per MapStruct.
- Ridurre boilerplate in ogni microservizio delegando a componenti generici le operazioni ripetitive (CRUD, mapping, gestione eccezioni).

## Pattern consigliati (estratti)

1) Entità

   - Estendere `it.alf.baseframework.model.BaseEntity` per ereditare l'identificatore primario e qualsiasi comportamento/attributo comune.

   Esempio (sample): `it.alf.sample.model.MyEntity` extends `BaseEntity`.

2) Repository

   - Dichiarare l'interfaccia repository estendendo `it.alf.baseframework.repository.GenericRepository<T>`.
   - Questo eredita metodi JPA standard (findAll, save, delete, ecc.).

   Esempio (sample): `MyEntityRepository extends GenericRepository<MyEntity>`

3) Service

   - Estendere `it.alf.baseframework.service.GenericCrudService<T>`.
   - Usare constructor injection nel servizio concreto per passare la repository.
   - Non annotare la classe astratta nel framework con `@Service`; il progetto consumer deve annotare la classe concreta con `@Service`.

   Esempio (sample):

   - `MyEntityService extends GenericCrudService<MyEntity>` e `@Service` sulla classe concreta.

4) Controller

   - Estendere `it.alf.baseframework.controller.GenericCrudController<T>`.
   - Annotare il controller concreto con `@RestController` e definire `@RequestMapping` per il path di risorsa.
   - Il controller concreto rimane sottile: delega al `GenericCrudService`.

   Esempio (sample): `MyEntityController extends GenericCrudController<MyEntity>` espone `/api/my-entities`.

5) DTO e Mapper

   - Usare DTO per il modello di input/output REST. Estendere `it.alf.baseframework.dto.BaseDto` se necessario per campi comuni.
   - Definire mapper MapStruct che estendono `it.alf.baseframework.mapper.GenericMapper<E,D>` e annotarli `@Mapper(componentModel = "spring")`.

   Esempio (sample): `MyMapper extends GenericMapper<MyEntity, MyDto>`.

6) Sicurezza

   - `SecurityConfig` è fornito dal framework e legge `baseframework.security.enabled`.
   - Il sample mostra due approcci:
     - Importare esplicitamente `it.alf.baseframework.config.SecurityConfig` nella `@SpringBootApplication` del progetto consumer (come fatto nel sample) e impostare `baseframework.security.enabled=false` in `application.properties` per disabilitare la security nel demo.
     - Oppure, sovrascrivere la configuration o definire proprie regole di sicurezza nel progetto consumer.

7) Kafka

   - `KafkaConfig` nel framework fornisce `ConsumerFactory`, `ProducerFactory` e `KafkaTemplate` basati su proprietà `spring.kafka.*`.
   - Configurare `spring.kafka.bootstrap-servers` e `spring.kafka.consumer.group-id` nel `application.properties` del progetto consumer.

## Come eseguire il sample (riassunto)

1. Dalla root del modulo `baseframework` installa nel repository locale:

```powershell
mvn -DskipTests=false install
```

2. Vai nella cartella `samples` ed esegui:

```powershell
mvn -DskipTests=false test      # esegue i test, incluso il MockMvc integration test
mvn spring-boot:run            # avvia l'app sample su porta 8081
```

3. API disponibili (sample):

- GET  /api/my-entities     -> lista di MyEntity
- POST /api/my-entities     -> crea MyEntity (body JSON: {"name":"..."})

## Best practices e suggerimenti

- Creare sempre classi concrete per service e controller che estendono i tipi generici: questo facilita l'autowiring e mantiene il framework libero di implementare solo la logica generica.
- Usare DTO + mapper per separare modello di persistenza da modello REST.
- Tenere la configurazione infrastrutturale (Kafka, DB) nel progetto consumer; il framework legge proprietà con valori di default ma non impone dipendenze esterne.
- Considerare l'adozione di `@ConfigurationProperties` per gruppi di proprietà complessi (es. Kafka) nell'evoluzione del framework.

## Esempi rapidi (snippet)

Entity:

```java
@Entity
public class MyEntity extends BaseEntity { private String name; /* getter/setter */ }
```

Repository:

```java
public interface MyEntityRepository extends GenericRepository<MyEntity> {}
```

Service:

```java
@Service
public class MyEntityService extends GenericCrudService<MyEntity> {
  public MyEntityService(MyEntityRepository repo) { super(repo); }
}
```

Controller:

```java
@RestController
@RequestMapping("/api/my-entities")
public class MyEntityController extends GenericCrudController<MyEntity> {
  public MyEntityController(MyEntityService s) { super(s); }
}
```

Mapper (MapStruct):

```java
@Mapper(componentModel = "spring")
public interface MyMapper extends GenericMapper<MyEntity, MyDto> {}
```

## Domande frequenti rapide

- D: Posso usare il framework senza MapStruct? R: Sì, MapStruct è opzionale — puoi mappare manualmente o usare altre soluzioni.
- D: Il framework fornisce endpoint REST pronti? R: Sì, `GenericCrudController` espone endpoint CRUD generici; il consumer deve creare controller concreti con `@RestController` e percorso specifico.

---

Se preferisci, posso anche aggiornare il `samples/README.md` includendo questi estratti o generare una versione PDF/HTML della documentazione. Vuoi che li incorpori anche in `samples/README.md`?
