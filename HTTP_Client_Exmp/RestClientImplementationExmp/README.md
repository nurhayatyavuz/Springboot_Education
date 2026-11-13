# RestClient Implementation Example

Spring Boot 4 ile geliştirilmiş, iki uygulamadan oluşan basit bir **`RestClient`** örneğidir.

- **`game`** → `Game` verisini PostgreSQL'de tutan ve REST API olarak sunan servis (port `8080`)
- **`client`** → `game` servisini Spring'in `RestClient` sınıfı ile çağıran ve kendi endpoint'leri üzerinden dışarıya açan istemci uygulaması (port `8081`)

```
   İstek                  RestClient                     JPA
 ─────────▶  client:8081 ───────────▶  game:8080 ───────────▶  PostgreSQL
 (curl/Swagger)  (ClientCont)          (Spring Data REST)        (postgres:5432)
```

## Teknolojiler

| Teknoloji | Sürüm / Not |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.2 |
| Spring Web MVC + `RestClient` | HTTP istemcisi |
| Spring Data JPA + Spring Data REST | `game` tarafında REST endpoint'lerini otomatik üretir |
| PostgreSQL | Veritabanı |
| Lombok / MapStruct | Boilerplate azaltma |
| springdoc-openapi | 3.1.1 – Swagger UI |
| Maven Wrapper | `mvnw` / `mvnw.cmd` |

## Proje Yapısı

```
RestClientImplementationExmp/
├── game/                                  # REST API (sunucu)
│   └── src/main/java/com/example/game/
│       ├── GameApplication.java
│       ├── Game.java                      # JPA entity
│       └── GameRepository.java            # JpaRepository → /games endpoint'lerini üretir
└── client/                                # RestClient kullanan istemci
    └── src/main/java/com/example/client/
        ├── ClientApplication.java
        ├── Game.java                      # İstek/yanıt modeli
        ├── config/ConfigProperty.java     # RestClient bean tanımı
        └── controller/ClientCont.java     # /games endpoint'leri (game servisine yönlendirir)
```

## Nasıl Çalışır?

### `game` servisi
Controller yazılmamıştır. `GameRepository`, `JpaRepository` türevi olduğu ve projede `spring-boot-starter-data-rest` bulunduğu için Spring Data REST, `Game` entity'si için `/games` altında CRUD endpoint'lerini otomatik oluşturur.

### `client` servisi
`ConfigProperty` sınıfında `http://localhost:8080` adresini baz alan, `Accept: application/json` başlığı tanımlı bir `RestClient` bean'i oluşturulur. `ClientCont` bu bean'i kullanarak gelen istekleri `game` servisine iletir:

| Client endpoint (`:8081`) | HTTP | Game servisine yaptığı çağrı (`:8080`) |
|---|---|---|
| `/games/{id}` | `GET` | `GET /games/{id}` |
| `/games` | `POST` | `POST /games` |
| `/games/{id}` | `PUT` | `PUT /games/{id}` |
| `/games/{id}` | `DELETE` | `DELETE /games/{id}` |

### Veri modeli (`Game`)

| Alan | Tip | Açıklama |
|---|---|---|
| `id` | `Integer` | Otomatik üretilir (`SEQUENCE`) |
| `name` | `String` | Oyun adı |
| `rule` | `String` | Oyun kuralı |
| `playerCount` | `Integer` | Oyuncu sayısı |

## Gereksinimler

- JDK 17+
- PostgreSQL (varsayılan: `localhost:5432`, veritabanı `postgres`)
- Maven (veya projedeki `mvnw`)

## Kurulum ve Çalıştırma

### 1. Veritabanını hazırlayın

Örneğin Docker ile:

```bash
docker run --name game-db -e POSTGRES_PASSWORD=12345 -p 5432:5432 -d postgres
```

Bağlantı bilgileri her iki projenin `src/main/resources/application.properties` dosyasında tanımlıdır. Kendi ortamınıza göre güncelleyin.

### 2. Önce `game` servisini başlatın

```bash
cd game
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

Servis `http://localhost:8080` adresinde çalışır.

### 3. Ardından `client` uygulamasını başlatın

```bash
cd client
./mvnw spring-boot:run
```

Uygulama `http://localhost:8081` adresinde çalışır.

## Örnek İstekler

Tüm istekler **client** (`8081`) üzerinden yapılır:

```bash
# Oyun getir
curl http://localhost:8081/games/1

# Oyun güncelle
curl -X PUT http://localhost:8081/games/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Satranç","rule":"Şah mat","playerCount":2}'

# Oyun sil
curl -X DELETE http://localhost:8081/games/1
```

Oyun eklemek için (game servisine doğrudan):

```bash
curl -X POST http://localhost:8080/games \
  -H "Content-Type: application/json" \
  -d '{"name":"Satranç","rule":"Şah mat","playerCount":2}'
```

## API Dokümantasyonu

`springdoc-openapi` her iki uygulamada da ekli olduğundan Swagger arayüzüne şuradan ulaşabilirsiniz:

- Client: http://localhost:8081/swagger-ui.html
- Game: http://localhost:8080/swagger-ui.html

## Bilinen Eksikler / Geliştirme Notları

- **`POST /games` (client):** `ClientCont.postGames` metodunda istek gövdesi `game` servisine iletilmiyor. `.post().uri("/games")` ifadesinden sonra `.body(game)` eklenmelidir (`putGamesId` metodunda olduğu gibi).
- **Gereksiz bağımlılıklar:** `client` uygulaması veritabanına ihtiyaç duymadan sadece HTTP çağrısı yaptığı için JPA/PostgreSQL ayarları ve `Game` sınıfındaki `@Entity` anotasyonu kaldırılabilir. Şu an her iki uygulama da aynı veritabanına bağlanıyor.
- **Gizli bilgiler:** Veritabanı şifresi `application.properties` içinde düz metin olarak duruyor. GitHub'a yüklemeden önce ortam değişkenlerine (`${DB_PASSWORD}`) taşımanız veya örnek bir `application.properties.example` dosyası kullanmanız önerilir.
- **Eksik endpoint'ler:** Client tarafında tüm oyunları listeleyen (`GET /games`) bir endpoint yok.
- **Hata yönetimi:** Game servisi `4xx/5xx` döndüğünde `RestClient` bir istisna fırlatır; bunu yakalayan bir `@ControllerAdvice` yoktur.
- **Sabit adres:** Game servisinin adresi (`http://localhost:8080`) kodda sabit; `application.properties` üzerinden yapılandırılabilir hale getirilebilir.

## Lisans

Bu proje eğitim amaçlı bir örnektir. İstediğiniz lisansı ekleyebilirsiniz.
