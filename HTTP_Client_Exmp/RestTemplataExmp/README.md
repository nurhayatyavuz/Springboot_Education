# Spring Boot RestTemplate Örneği

Spring Boot ile yazılmış, **`RestTemplate`** kullanarak başka bir REST servisine (Car servisi) istek atan bir örnek projedir. Proje bir **ara katman (gateway / client servis)** gibi çalışır: gelen istekleri alır, `http://localhost:8081` üzerinde çalışan Car servisine yönlendirir ve cevabı istemciye döndürür.

`RestTemplate`'in farklı kullanım biçimlerini (`getForObject`, `getForEntity`, `postForObject`, `postForEntity`, `exchange`, `put`, `delete`) yan yana görmek ve öğrenmek amacıyla hazırlanmıştır.

---

##  İçindekiler

- [Teknolojiler](#-teknolojiler)
- [Mimari](#-mimari)
- [Proje Yapısı](#-proje-yapısı)
- [Gereksinimler](#-gereksinimler)
- [Kurulum ve Çalıştırma](#-kurulum-ve-çalıştırma)
- [Yapılandırma](#-yapılandırma)
- [API Endpointleri](#-api-endpointleri)
- [Örnek İstekler](#-örnek-istekler)
- [RestTemplate Yöntemleri](#-resttemplate-yöntemleri)
- [Doğrulama (Validation)](#-doğrulama-validation)
- [Interceptor](#-interceptor)

---

##  Teknolojiler

| Teknoloji | Sürüm / Açıklama |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.2 |
| Spring Web MVC | REST controller'lar |
| Spring Data JPA | Bağımlılık olarak ekli |
| PostgreSQL | Veritabanı sürücüsü |
| Spring Validation | `jakarta.validation` ile istek doğrulama |
| Spring Boot Actuator | Uygulama izleme |
| Lombok | Boilerplate kod azaltma |
| Maven | Derleme ve bağımlılık yönetimi (Maven Wrapper dahil) |

---

##  Mimari

```
┌──────────┐    HTTP     ┌────────────────────┐   RestTemplate   ┌──────────────────┐
│ İstemci  │ ──────────► │  Surucu (bu proje) │ ───────────────► │  Car Servisi     │
│ (Postman)│  :8080      │  /api/v1/surucu    │                  │  :8081           │
└──────────┘ ◄────────── └────────────────────┘ ◄─────────────── │  /api/car/v1     │
                                                                  └──────────────────┘
```

- **Bu proje:** `localhost:8080` – `/api/v1/surucu/**`
- **Hedef servis:** `localhost:8081` – `/api/car/v1/**` (ayrı bir projedir ve bu repoda **yer almaz**)

>  Bu uygulamanın uçlarını denemek için Car servisinin `8081` portunda ayakta olması gerekir.

---

##  Proje Yapısı

```
RestTemplataExmp
├── pom.xml
├── mvnw / mvnw.cmd
└── src
    ├── main
    │   ├── java/com/exampla/surucu
    │   │   ├── SurucuApplication.java          # Uygulama giriş noktası
    │   │   ├── CarRequest.java                 # İstek DTO'su (validation kurallarıyla)
    │   │   ├── CarResponse.java                # Cevap DTO'su
    │   │   ├── ErrorResponse.java              # Hata cevabı DTO'su
    │   │   ├── config
    │   │   │   ├── ConfigProperty.java         # RestTemplate bean tanımı
    │   │   │   └── CustomIntercepter.java      # İstek loglayan interceptor
    │   │   ├── controller
    │   │   │   └── SurucuController.java       # REST endpointleri
    │   │   └── service
    │   │       ├── SurucuService.java          # getForObject / getForEntity örnekleri
    │   │       └── SurucuServiceWExchange.java # exchange / post / put / delete örnekleri
    │   └── resources
    │       └── application.properties
    └── test
        └── java/com/exampla/surucu
            └── SurucuApplicationTests.java
```

---

##  Gereksinimler

- **JDK 17** veya üzeri
- **Maven 3.9+** (veya projedeki `mvnw` wrapper)
- **PostgreSQL** (varsayılan ayarlarla `localhost:5432`)
- `localhost:8081` üzerinde çalışan **Car servisi**

---

##  Kurulum ve Çalıştırma

```bash
# 1) Repoyu klonla
git clone https://github.com/<kullanici-adi>/RestTemplataExmp.git
cd RestTemplataExmp

# 2) application.properties içindeki veritabanı bilgilerini düzenle

# 3) Uygulamayı çalıştır
./mvnw spring-boot:run          # Linux / macOS
mvnw.cmd spring-boot:run        # Windows
```

Uygulama `http://localhost:8080` adresinde ayağa kalkar.

Jar olarak paketlemek için:

```bash
./mvnw clean package
java -jar target/surucu-0.0.1-SNAPSHOT.jar
```

---

##  Yapılandırma

`src/main/resources/application.properties`:

```properties
spring.application.name=surucu
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=<sifreniz>
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

server.error.include-stacktrace=never
```

>  **Güvenlik notu:** Gerçek veritabanı şifrenizi GitHub'a göndermeyin. Ortam değişkeni (`${DB_PASSWORD}`) veya `application-local.properties` (`.gitignore`'a eklenmiş) kullanmanız önerilir.

Car servisinin adresi şu an servis sınıflarında sabit (`http://localhost:8081/api/car/v1`) olarak yazılıdır.

---

##  API Endpointleri

**Base URL:** `http://localhost:8080/api/v1/surucu`

### GET

| Endpoint | Açıklama | Yönlendirilen Car Servisi Adresi |
|---|---|---|
| `GET /{id}` | ID ile araç getirir (4xx hatalarını `ErrorResponse` olarak döner) | `GET /api/car/v1/{id}` |
| `GET /` | Tüm araçları listeler (`ParameterizedTypeReference`) | `GET /api/car/v1` |
| `GET /findbymodel?model=` | Modele göre araç getirir | `GET /api/car/v1/carmodel?model=` |
| `GET /forSurucuServiceWexchange/{id}` | ID ile araç (`exchange` + header'lar) | `GET /api/car/v1/{id}` |
| `GET /forSurucuServiceWexchange/model?model=` | Modele göre araç (`exchange`) | `GET /api/car/v1/carmodel?model=` |
| `GET /sum/{sayi1}/{sayi2}` | İki sayıyı toplar – Path Variable (`Map` ile) | `GET /api/car/v1/get-sum/{sayi1}/{sayi2}` |
| `GET /sumUri/{sayi1}/{sayi2}` | İki sayıyı toplar – Path Variable (`UriComponentsBuilder` ile) | `GET /api/car/v1/get-sum/{sayi1}/{sayi2}` |
| `GET /toplam3?sayiA=&sayiB=&sayiC=` | Üç sayıyı toplar – Request Param | `GET /get-toplam-deger?...` |
| `GET /toplam3Uri?sayiA=&sayiB=&sayiC=` | Üç sayıyı toplar – Request Param (`UriComponentsBuilder` ile) | `GET /api/car/v1/get-toplam-deger?...` |
| `GET /ardisikKelime/{adet}?kelime=` | Kelimeyi `adet` kadar ardışık yazdırır | `GET /api/car/v1/kelime/{adet}?kelime=` |

### POST

| Endpoint | Açıklama | RestTemplate Metodu |
|---|---|---|
| `POST /addCar` | Araç ekler, `CarResponse` döner | `postForObject` |
| `POST /addCar2` | Araç ekler, `ResponseEntity` döner | `postForEntity` |
| `POST /addCar3` | Araç ekler, header ile birlikte, hata yönetimli | `exchange` |
| `POST /addCarModel/{model}?fiyat=` | Model ve fiyat parametreleriyle araç ekler | `exchange` |

### PUT

| Endpoint | Açıklama | RestTemplate Metodu |
|---|---|---|
| `PUT /update/{id}` | Aracı günceller | `put` |

### DELETE

| Endpoint | Açıklama | RestTemplate Metodu |
|---|---|---|
| `DELETE /delete/{id}` | Aracı siler (`204 No Content`) | `delete` |
| `DELETE /delete2/{id}` | Aracı siler (`204 No Content`) | `exchange` |

---

##  Örnek İstekler

### Araç ekleme

```bash
curl -X POST http://localhost:8080/api/v1/surucu/addCar \
  -H "Content-Type: application/json" \
  -d '{
        "marka": "Toyota",
        "model": "Corolla",
        "fiyat": 850000,
        "yil": 5
      }'
```

### Araç getirme

```bash
curl http://localhost:8080/api/v1/surucu/1
```

### Modele göre arama

```bash
curl "http://localhost:8080/api/v1/surucu/findbymodel?model=Corolla"
```

### Toplama (Path Variable)

```bash
curl http://localhost:8080/api/v1/surucu/sum/5/7
# 12
```

### Toplama (Request Param)

```bash
curl "http://localhost:8080/api/v1/surucu/toplam3Uri?sayiA=1&sayiB=2&sayiC=3"
# 6
```

### Araç silme

```bash
curl -X DELETE http://localhost:8080/api/v1/surucu/delete/1
```

### Örnek hata cevabı

Car servisi 4xx döndüğünde cevap aşağıdaki gibi `ErrorResponse` yapısında iletilir:

```json
{
  "message": "Araç bulunamadı",
  "timestamp": "2026-11-08T14:30:00",
  "statusCode": 404,
  "path": "/api/car/v1/99"
}
```

---

##  RestTemplate Yöntemleri

Projede hangi yöntemin nerede kullanıldığını gösteren özet:

| Yöntem | Kullanım Yeri | Ne Zaman? |
|---|---|---|
| `getForObject` | `SurucuService` | Sadece gövde (body) lazım olduğunda |
| `getForEntity` | `SurucuService` | Status code ve header'lar da lazım olduğunda |
| `postForObject` | `addCar` | POST sonrası sadece gövde lazımsa |
| `postForEntity` | `addCar2` | POST sonrası `ResponseEntity` lazımsa |
| `exchange` | `SurucuServiceWExchange` | Her HTTP metodu, özel header ve generic tip (`List<CarResponse>`) gerektiğinde |
| `put` | `UpdateCar` | Güncelleme, cevap beklenmiyorsa |
| `delete` | `DeleteCar` | Silme işlemi |

**URL oluşturma yöntemleri:**

- Şablon string: `"http://.../{id}"` + parametre (varargs veya `Map`)
- `UriComponentsBuilder` ile `buildAndExpand(...)` (path variable) ve `.queryParam(...)` (request param)

**Generic liste dönüşü için:**

```java
restTemplate.exchange(
    "http://localhost:8081/api/car/v1",
    HttpMethod.GET,
    requestEntity,
    new ParameterizedTypeReference<List<CarResponse>>() {}
);
```

---

##  Doğrulama (Validation)

`CarRequest` üzerindeki kurallar, controller'daki `@Valid` ile devreye girer:

| Alan | Kural |
|---|---|
| `marka` | 3–20 karakter |
| `model` | Boş olamaz, 3–25 karakter |
| `fiyat` | Kısıt yok |
| `yil` | Boş olamaz, en fazla `10` |

---

##  Interceptor

`ConfigProperty` içinde tanımlanan `RestTemplate` bean'ine `CustomIntercepter` eklenmiştir. Dışarı giden her istekte:

- `X-Private-Info` header'ını ekler,
- İstek URI'sini, HTTP metodunu ve header'ları loglar.

```java
@Bean
public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add(new CustomIntercepter());
    return restTemplate;
}
```

---

##  Lisans

Bu proje eğitim amaçlı hazırlanmıştır. İstediğiniz lisansı (örn. MIT) ekleyebilirsiniz.
