# SpecificationExmp

Spring Boot ve Spring Data JPA kullanılarak geliştirilen bu örnek proje, **Specification ile dinamik filtreleme**, **sayfalama (pagination)**, **sıralama (sorting)** ve **MapStruct ile DTO dönüşümü** konularını uygulamalı olarak öğrenmek amacıyla hazırlanmıştır.

Proje kapsamında `User` verileri üzerinde farklı filtreleme senaryoları oluşturulabilir ve REST API üzerinden test edilebilir.

## Kullanılan Teknolojiler
- Java 17
- Spring Boot 4.1.1
- Spring Data JPA
- Spring Data REST
- PostgreSQL
- MapStruct 1.6.3
- Lombok
- springdoc-openapi
- Maven Wrapper

## Özellikler

- **Dinamik filtreleme:** `Specification` kullanarak isim, soyisim, yaş ve şehir alanlarına göre sorgular oluşturma.
- **Birleşik koşullar:** Birden fazla filtreyi `and` ile birleştirme.
- **Aralık sorguları:** `between` kullanarak yaş aralığına göre filtreleme.
- **Request body ile filtreleme:** `UserFilter` DTO'su üzerinden filtreleme ve doğrulama.
- **Sayfalama:** `Pageable` ve `PageRequest` ile sonuçları sayfalara ayırma.
- **Sıralama:** `Sort` ile sonuçları belirtilen alana ve yöne göre sıralama.
- **JPQL sorguları:** `@Query` kullanarak sayfalı sorgular oluşturma.
- **Toplu kayıt:** Tek istekte birden fazla kullanıcı ekleme.
- **DTO dönüşümü:** MapStruct ile `User` entity'sini `UserDto` nesnesine dönüştürme.
- **API dokümantasyonu:** Swagger UI üzerinden endpoint'leri inceleme ve test etme.

## Proje Yapısı

```text
Specification_Pagination_Exmp/
├── src/
│   ├── main/
│   │   ├── java/com/example/specificationexmp/
│   │   │   ├── SpecificationExmpApplication.java
│   │   │   ├── controller/
│   │   │   │   └── UserController.java
│   │   │   ├── dto/
│   │   │   │   └── UserDto.java
│   │   │   ├── entity/
│   │   │   │   └── User.java
│   │   │   ├── filter/
│   │   │   │   └── UserFilter.java
│   │   │   ├── mapper/
│   │   │   │   └── UserMapper.java
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java
│   │   │   └── specs/
│   │   │       └── UserSpecification.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── .mvn/
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## Veri Modeli

Proje, PostgreSQL üzerinde `users` tablosunda tutulan kullanıcı verileri üzerinden çalışır.

| Alan | Tip | Açıklama |
|---|---|---|
| `id` | Integer | Otomatik artan kullanıcı ID'si |
| `name` | String | Kullanıcının adı |
| `surname` | String | Kullanıcının soyadı |
| `age` | Integer | Kullanıcının yaşı |
| `city` | String | Kullanıcının şehri |
| `active` | Boolean | Kullanıcının aktiflik durumu |

## Gereksinimler

Projeyi çalıştırmadan önce aşağıdakilerin kurulu olması gerekir:

- JDK 17 veya üzeri
- PostgreSQL
- IntelliJ IDEA veya tercih edilen bir Java IDE'si

Maven Wrapper proje içerisinde bulunduğu için ayrıca Maven kurmak zorunlu değildir.

## Kurulum ve Çalıştırma

### 1. Depoyu klonlayın

Ana eğitim reposunu henüz klonlamadıysanız:

```bash
git clone https://github.com/nurhayatyavuz/Springboot_Education.git
cd Springboot_Education/Specification_Pagination_Exmp
```

### 2. Veritabanını yapılandırın

PostgreSQL üzerinde kullanılacak veritabanını hazırlayın. Ardından `src/main/resources/application.properties` dosyasındaki bağlantı bilgilerini kendi ortamınıza göre düzenleyin.

Örnek yapılandırma:

```properties
server.port=8081

spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

`DB_PASSWORD` ortam değişkenini kendi PostgreSQL şifrenizle eşleştirin. Gerçek şifreleri GitHub'a yüklemeyin.

> Not: Yukarıdaki yapılandırma örnektir. Projenizdeki mevcut `application.properties` dosyasını ve kullandığınız veritabanı ayarlarını kontrol edin.

### 3. Uygulamayı çalıştırın

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
./mvnw spring-boot:run
```

Uygulama başarıyla başlatıldığında varsayılan olarak `http://localhost:8081` adresinde çalışır.

## API Dokümantasyonu

Uygulama çalışırken Swagger UI arayüzünü açın:

[http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

Swagger üzerinden endpoint'leri inceleyebilir ve örnek istekler gönderebilirsiniz.

## API Endpoint'leri

Temel endpoint yolu: `/api/v1/userss`

| HTTP Metodu | Endpoint | Açıklama |
|---|---|---|
| `POST` | `/api/v1/userss/bulk` | Birden fazla kullanıcı kaydeder. |
| `GET` | `/api/v1/userss/spec` | URL parametreleriyle dinamik filtreleme yapar. |
| `POST` | `/api/v1/userss/filter` | Request body üzerinden filtreleme yapar. |
| `GET` | `/api/v1/userss/filter-spec` | Yaş aralığına göre filtreleyerek sayfalı DTO sonucu döndürür. |
| `GET` | `/api/v1/userss/filter-spec-sort` | Filtreleme, sayfalama ve sıralamayı birleştirir. |
| `GET` | `/api/v1/userss/pagination-jpql-test1` | İsme göre JPQL ile sayfalı arama yapar. |
| `GET` | `/api/v1/userss/pagination-jpql-test2` | JPQL ile tüm kullanıcıları sayfalı listeler. |

> Endpoint açıklamaları proje kodundaki mevcut uygulamayı temel alır. Gerçek parametreler ve varsayılan değerler için `UserController` sınıfını kontrol edin.

## Örnek İstekler

### 1. Toplu kullanıcı ekleme

**İstek**

```http
POST http://localhost:8081/api/v1/userss/bulk
Content-Type: application/json
```

**Request body**

```json
[
  {
    "name": "Ahmet",
    "surname": "Yılmaz",
    "age": 28,
    "city": "Ankara",
    "active": true
  },
  {
    "name": "Ayşe",
    "surname": "Demir",
    "age": 34,
    "city": "İstanbul",
    "active": true
  }
]
```

### 2. Dinamik filtreleme

Ankara'da yaşayan ve yaşı 28 olan kullanıcıları aramak için:

```http
GET http://localhost:8081/api/v1/userss/spec?city=Ankara&age=28
```

Desteklenen parametreler ve bunların zorunlu olup olmadığı controller içindeki metoda bağlıdır.

### 3. Request body ile filtreleme

**İstek**

```http
POST http://localhost:8081/api/v1/userss/filter
Content-Type: application/json
```

**Request body**

```json
{
  "name": "Ahmet",
  "surname": "Yılmaz",
  "age": 28,
  "city": "Ankara",
  "active": true
}
```

### 4. Sayfalama ve sıralama

Örneğin, yaşa göre azalan sıralamayla ilk sayfada 5 kayıt almak için:

```http
GET http://localhost:8081/api/v1/userss/filter-spec-sort?page=0&size=5&sortField=age&sortDirection=desc
```

- `page=0`: İlk sayfa.
- `size=5`: Sayfa başına 5 kayıt.
- `sortField=age`: Yaş alanına göre sıralama.
- `sortDirection=desc`: Azalan sıralama.

Bu parametrelerin geçerli değerleri controller koduna göre değişebilir.

## Öğrenilen Konular

Bu projeyle aşağıdaki Spring Boot ve Spring Data JPA konuları uygulamalı olarak incelenebilir:

- `JpaRepository` ve `JpaSpecificationExecutor`
- `Specification` ile dinamik sorgu oluşturma
- Criteria API ve birleştirilebilir filtreler
- `Pageable`, `PageRequest` ve `Sort`
- JPQL ile özel sorgular
- Entity ve DTO arasındaki dönüşüm
- MapStruct kullanımı
- REST API tasarımı ve Swagger UI

## Bilinen Sorunlar ve Geliştirme Fikirleri

Proje eğitim amaçlıdır. Geliştirme sırasında ele alınabilecek konular:

- Filtre parametreleri verilmediğinde varsayılan değerlerin ve `null` kontrollerinin iyileştirilmesi.
- `UserFilter` için kısmi filtreleme desteği eklenmesi.
- Şehir filtresinin doğru Specification metodu ile eşleştirildiğinin doğrulanması.
- Kullanılmayan import ve bağımlılıkların temizlenmesi.
- Birim ve entegrasyon testlerinin artırılması.
- Veritabanı bağlantı bilgilerinin ortam değişkenleriyle yönetilmesi.
- Sayfalama ve dinamik sorgular için performans testleri yapılması.

## Lisans

Bu proje eğitim ve öğrenme amacıyla hazırlanmıştır. Herhangi bir lisans belirtilmemiştir.
