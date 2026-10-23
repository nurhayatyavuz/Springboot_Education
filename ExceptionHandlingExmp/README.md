# ExceptionHandlingExmp

Spring Boot ile geliştirilmiş, **araç (Car) yönetimi** üzerinden katmanlı mimari ve **özel exception (hata) yönetimi** mantığını gösteren bir REST API örnek projesidir.

## İçindekiler

- [Özellikler](#özellikler)
- [Kullanılan Teknolojiler](#kullanılan-teknolojiler)
- [Proje Yapısı](#proje-yapısı)
- [Gereksinimler](#gereksinimler)
- [Kurulum ve Çalıştırma](#kurulum-ve-çalıştırma)
- [API Endpoint'leri](#api-endpointleri)
- [Örnek İstekler](#örnek-istekler)
- [Exception Yönetimi](#exception-yönetimi)
- [Swagger / OpenAPI](#swagger--openapi)

## Özellikler

- Araç ekleme, listeleme, ID ile getirme, güncelleme ve silme (CRUD)
- Katmanlı mimari: Controller → Service → Repository
- Entity ile API modelini ayıran DTO yapısı (`CarRequest`, `CarResponse`)
- MapStruct ile otomatik Entity ↔ DTO dönüşümü
- Özel exception sınıfları (`CarNotFoundException`, `CarAlreadyExistException`)
- Standart hata cevabı modeli (`ErrorResponse`)
- PostgreSQL ile veri kalıcılığı
- Swagger UI ile API dokümantasyonu

## Kullanılan Teknolojiler

| Teknoloji | Açıklama |
|-----------|----------|
| Java 17 | Programlama dili |
| Spring Boot 4.1.2 | Uygulama çatısı |
| Spring Data JPA | Veritabanı erişimi |
| PostgreSQL | Veritabanı |
| MapStruct 1.6.3 | Entity ↔ DTO dönüşümü |
| Lombok | Boilerplate kod azaltma |
| springdoc-openapi 3.1.1 | Swagger UI / OpenAPI dokümantasyonu |
| Spring Boot Actuator | Uygulama sağlık ve izleme uç noktaları |
| Maven | Bağımlılık ve build yönetimi |

## Proje Yapısı

```
ExceptionHandlingExmp
├── src/main/java/com/example/ExceptionHandlingExmp
│   ├── controller
│   │   └── CarController.java
│   ├── dto
│   │   ├── CarRequest.java
│   │   └── CarResponse.java
│   ├── entity
│   │   └── Car.java
│   ├── exception
│   │   ├── CarAlreadyExistException.java
│   │   ├── CarNotFoundException.java
│   │   └── ErrorResponse.java
│   ├── mapper
│   │   └── CarMapper.java
│   ├── repository
│   │   └── CarRepository.java
│   ├── service
│   │   └── CarService.java
│   └── ExceptionHandlingExmpApplication.java
├── src/main/resources
│   └── application.properties
└── pom.xml
```

## Gereksinimler

- JDK 17 veya üzeri
- Maven (veya projedeki `mvnw` wrapper)
- PostgreSQL (çalışır durumda, varsayılan port `5432`)

## Kurulum ve Çalıştırma

**1. Repository'yi klonlayın**

```bash
git clone https://github.com/nurhayatyavuz/Springboot_Education.git
cd Springboot_Education/ExceptionHandlingExmp
```

**2. Veritabanı ayarlarını yapın**

`src/main/resources/application.properties` dosyasını kendi PostgreSQL bilgilerinize göre düzenleyin:

```properties
spring.application.name=ExceptionHandlingExmp
server.port=8081

spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=<kullanici_adi>
spring.datasource.password=<sifre>
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> `ddl-auto=update` ayarı sayesinde `car` tablosu uygulama ilk çalıştığında otomatik oluşturulur.

**3. Uygulamayı başlatın**

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Uygulama varsayılan olarak **http://localhost:8081** adresinde çalışır.

## API Endpoint'leri

Base URL: `http://localhost:8081/api/car/v1`

| Metot | Endpoint | Açıklama | Başarılı Cevap |
|-------|----------|----------|----------------|
| `POST` | `/api/car/v1` | Yeni araç ekler | `200 OK` |
| `GET` | `/api/car/v1` | Tüm araçları listeler | `200 OK` |
| `GET` | `/api/car/v1/{id}` | ID ile araç getirir | `200 OK` |
| `PUT` | `/api/car/v1/{id}` | Aracın fiyat ve yaş bilgisini günceller | `200 OK` |
| `DELETE` | `/api/car/v1/{id}` | Aracı siler | `204 No Content` |

### Veri Modeli

**CarRequest**

| Alan | Tip | Açıklama |
|------|-----|----------|
| `marka` | String | Araç markası |
| `model` | String | Araç modeli (benzersiz olmalıdır) |
| `fiyat` | Integer | Araç fiyatı |
| `yas` | Integer | Araç yaşı |

**CarResponse**: `CarRequest` alanlarına ek olarak veritabanında üretilen `id` alanını (Integer) içerir.

## Örnek İstekler

**Araç ekleme**

```bash
curl -X POST http://localhost:8081/api/car/v1 \
  -H "Content-Type: application/json" \
  -d '{"marka":"Toyota","model":"Corolla","fiyat":850000,"yas":2}'
```

```json
{
  "id": 1,
  "marka": "Toyota",
  "model": "Corolla",
  "fiyat": 850000,
  "yas": 2
}
```

**Araç güncelleme**

```bash
curl -X PUT http://localhost:8081/api/car/v1/1 \
  -H "Content-Type: application/json" \
  -d '{"fiyat":800000,"yas":3}'
```

**Araç silme**

```bash
curl -X DELETE http://localhost:8081/api/car/v1/1
```

## Exception Yönetimi

Proje, iş kurallarının ihlal edildiği durumlarda anlamlı hatalar fırlatmak için özel exception sınıfları kullanır:

| Exception | Ne zaman fırlatılır? |
|-----------|----------------------|
| `CarAlreadyExistException` | Aynı `model` değerine sahip bir araç zaten varsa (araç ekleme sırasında) |
| `CarNotFoundException` | Güncellenmek veya silinmek istenen ID'ye ait araç bulunamazsa |

Hata cevapları için `ErrorResponse` modeli kullanılır:

```json
{
  "message": "Car not found",
  "path": "/api/car/v1/99",
  "status": 404,
  "timestamp": "2026-10-23T12:00:00"
}
```

## Swagger / OpenAPI

Uygulama çalışırken API dokümantasyonuna tarayıcıdan ulaşabilirsiniz:

- Swagger UI: http://localhost:8081/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8081/v3/api-docs

## Yazar

**Nurhayat Yavuz**  
[GitHub: nurhayatyavuz](https://github.com/nurhayatyavuz)
