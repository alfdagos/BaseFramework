# Sample application (samples)

This folder contains a small Spring Boot sample that demonstrates how to use the `baseframework` library.

How it is organized
- `src/main/java/it/alf/sample` - sample application sources
  - `model/MyEntity` - extends `BaseEntity`
  - `dto/MyDto` - extends `BaseDto`
  - `repository/MyEntityRepository` - extends `GenericRepository<MyEntity>`
  - `service/MyEntityService` - extends `GenericCrudService<MyEntity>`
  - `controller/MyEntityController` - extends `GenericCrudController<MyEntity>` and exposes `/api/my-entities`
  - `mapper/MyMapper` - MapStruct mapper extending `GenericMapper`

Important note before running
1. The sample depends on the `baseframework` artifact (groupId `it.alf`, artifactId `baseframework`, version `1.0-SNAPSHOT`).
   You need to install the library into your local Maven repository before running the sample. From the `baseframework` module root run:

```powershell
mvn -DskipTests=false install
```

2. Then run the sample (from the `samples` directory):

```powershell
mvn spring-boot:run
```

The application will start on port 8081 and expose the CRUD endpoints for `MyEntity` at `/api/my-entities`.

Notes
- The sample uses H2 in-memory database for simplicity.
- `baseframework` security is disabled in `application.properties` for easy testing; change `baseframework.security.enabled=true` for real security.
