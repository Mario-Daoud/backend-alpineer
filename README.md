# eindopdracht2023-Mario-Daoud
eindopdracht2023-Mario-Daoud created by GitHub Classroom

## Docker compose file
### Database
PostgreSQL (:5432) + pgAdmin (:5050)
```yaml
version: "3.8"
services:
  db:
    container_name: postgres
    image: postgres
    restart: always
    environment:
      POSTGRES_USER: root
      POSTGRES_PASSWORD: root
      POSTGRES_DB: test_db
    ports:
      - "5432:5432"
  pgadmin:
    container_name: pgadmin
    image: dpage/pgadmin4
    restart: always
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@admin.com
      PGADMIN_DEFAULT_PASSWORD: root
    ports:
      - "5050:80"
```
