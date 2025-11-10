## BaseFramework - Libreria comune per microservizi

Questo modulo è una libreria comune (packaging: jar) pensata per essere riutilizzata all'interno di microservizi basati su Spring Boot. Fornisce componenti di base per operazioni CRUD generiche, configurazioni infrastrutturali (Kafka, Security) e gestione centralizzata delle eccezioni.

### Panoramica
- Linguaggio: Java 21
- Framework: Spring Boot 3.5.7
- Scopo: fornire building block riutilizzabili per microservizi (entità base, repository generico, servizio/controller CRUD, configurazioni Kafka e Security).

### Componenti principali
- `it.alf.baseframework.model.BaseEntity`
  - `@MappedSuperclass` con campo `id` Long generato automaticamente.
- `it.alf.baseframework.repository.GenericRepository<T extends BaseEntity>`
  - Estende `JpaRepository<T, Long>` per repository generici.
- `it.alf.baseframework.service.GenericCrudService<T extends BaseEntity>`
  - Classe astratta che fornisce operazioni CRUD: create, update, delete, findById, findAll. È pensata per essere estesa da servizi concreti del microservizio (usa constructor injection del repository).
- `it.alf.baseframework.controller.GenericCrudController<T extends BaseEntity>`
  - Controller REST astratto che espone endpoint CRUD (POST, PUT /{id}, DELETE /{id}, GET /{id}, GET). Non è annotato con `@RestController` né ha un `@RequestMapping` default: il servizio che estende questa classe deve dichiarare il path e l'annotazione del controller.
- `it.alf.baseframework.exception.CustomException` e `GlobalExceptionHandler`
  - Eccezione custom e handler che mappa `CustomException` in risposta 400 Bad Request.
- `it.alf.baseframework.config.KafkaConfig`
  - Bean `ConsumerFactory<String,String>` che legge `spring.kafka.bootstrap-servers` e `spring.kafka.consumer.group-id` (ha valori di default). Fornisce inoltre `ProducerFactory<String,String>` e `KafkaTemplate<String,String>` per l'invio di messaggi. Le proprietà del producer possono essere configurate dal microservizio (es. `spring.kafka.producer.acks`).
- `it.alf.baseframework.config.SecurityConfig`
  - Configurazione Spring Security con HTTP Basic attivata di default; può essere disattivata dal microservizio impostando la proprietà `baseframework.security.enabled=false`.

### Dipendenze rilevanti
- `spring-boot-starter`, `spring-boot-starter-security`, `spring-boot-starter-data-jpa`
- `spring-kafka`
- `postgresql` (driver, scope runtime)
 - `mapstruct` (per generazione automatica dei mapper DTO <-> entity)
 - `mapstruct` (per generazione automatica dei mapper DTO <-> entity) — versione attuale 1.5.5.Final

### Come utilizzare questa libreria (linee guida rapide)
1. Aggiungi il modulo `baseframework` come dipendenza Maven nel tuo microservizio.
2. Crea le entità del tuo dominio estendendo `BaseEntity`:

```java
public class MyEntity extends BaseEntity {
    // campi, getter/setter
}
```

3. Crea una repository specifica (opzionale ma consigliata):

```java
public interface MyEntityRepository extends GenericRepository<MyEntity> {}
```

4. Usa o estendi il servizio/controller generico:
   - È consigliabile creare classi concrete per consentire a Spring di risolvere i bean generici. Esempio di implementazione concreta con constructor injection:

```java
@Service
public class MyEntityService extends GenericCrudService<MyEntity> {
  public MyEntityService(MyEntityRepository repository) {
    super(repository);
  }
}

@RestController
@RequestMapping("/api/my-entities")
public class MyEntityController extends GenericCrudController<MyEntity> {
  public MyEntityController(MyEntityService service) {
    super(service);
  }
}
```

5. Configura `application.properties` del microservizio:
   - Impostazioni DB (spring.datasource.url/username/password)
  - Kafka: `spring.kafka.bootstrap-servers=...` e `spring.kafka.consumer.group-id=...`
  - Producer Kafka: `spring.kafka.producer.acks=all` (opzionale, default `all`)

### Note tecniche e avvertenze
- Il controller generico usa il path `/api` e non differenzia le risorse: nella pratica è preferibile esporre controller concreti con path specifici (es. `/api/my-entities`).
- Per usare correttamente i bean generici in Spring spesso è necessario creare sottoclassi concrete (altrimenti l'iniezione può fallire a causa dell'erasure dei generics).
 - `KafkaConfig` legge ora le proprietà `spring.kafka.bootstrap-servers` e `spring.kafka.consumer.group-id` con valori di default; personalizza queste proprietà nel microservizio.
 - `SecurityConfig` usa la proprietà `baseframework.security.enabled` (default `true`) per abilitare o disabilitare il controllo di accesso predefinito.
 - `KafkaConfig` legge ora le proprietà `spring.kafka.bootstrap-servers` e `spring.kafka.consumer.group-id` con valori di default; fornisce anche `ProducerFactory` e `KafkaTemplate` e supporta la proprietà `spring.kafka.producer.acks`.
 - `SecurityConfig` usa la proprietà `baseframework.security.enabled` (default `true`) per abilitare o disabilitare il controllo di accesso predefinito.

### Esempio rapido di `application.properties`

spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=myuser
spring.datasource.password=mypass


spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=baseframework-group

# per disabilitare la security fornita dal framework (non raccomandato in produzione)
baseframework.security.enabled=true

### Suggerimenti e funzionalità incluse
- `KafkaConfig` è già parametrizzabile tramite properties; valutare l'introduzione di `@ConfigurationProperties` per una configurazione più strutturata.
- Il progetto include MapStruct come dipendenza: aggiungi mapper concreti nel microservizio estendendo `GenericMapper<E,D>` e annotandoli con `@Mapper(componentModel = "spring")`.
- Fornire test di integrazione d'esempio e template di microservizio che usa la libreria è raccomandato per accelerare l'onboarding.

### Licenza
Inserire qui la licenza desiderata (es. MIT/Proprietaria) o rimuovere la sezione se non rilevante.

---
Se vuoi, posso:
- aggiungere un esempio concreto (entità + repository + controller) nel progetto;
- parametrizzare `KafkaConfig` per leggere da `application.properties`;
- creare un piccolo template di microservizio che usa questa libreria.
