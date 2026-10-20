# JPQLExmp

Java eğitim serisinde öğrendiklerimi pekiştirmek amacıyla geliştirdiğim **Spring Boot + JPA** örnek projesidir. Projede özellikle **JPQL sorguları, Spring Data JPA derived query methods, pagination (sayfalama), sıralama, DTO ve MapStruct** kullanımları üzerine örnekler bulunmaktadır.

## Kullanılan Teknolojiler

- **Java 17**
- **Spring Boot 4.1.1**
  - Spring Web MVC
  - Spring Data JPA
  - Spring Data REST
- **PostgreSQL**
- **Lombok**
- **MapStruct 1.6.3**
- **springdoc-openapi (Swagger UI)**
- **Maven**

## Proje Yapısı

```text
src/main/java/com/example/jpqlexmp
├── controller
│   ├── UserController
│   └── CarController
├── dto
│   ├── UserNameAndAgeDto
│   ├── CarRequest
│   └── CarResponse
├── entity
│   ├── User
│   └── Car
├── mapper
│   ├── CarMapper
│   └── UserMapper
├── repository
│   ├── UserRepository
│   └── CarRepository
└── service
```

## Projede Ele Alınan Konular

### 1. JPQL Sorguları (`UserRepository`)

`@Query` anotasyonu kullanılarak özel JPQL sorguları yazılmıştır.

| Metot | Açıklama |
|---|---|
| `tumKullaniciYazdir()` | `SELECT u FROM User u` sorgusuyla tüm kullanıcıları getirir. |
| `isimListes()` | Kullanıcıların yalnızca isimlerini `List<String>` olarak döndürür. |
| `yas(...)` | `@Param` kullanarak belirli bir yaştaki kullanıcıyı arar. |
| `sayiBul(age)` | Belirtilen yaştan büyük kullanıcıların sayısını `COUNT` ile hesaplar. |
| `yasToplam()` | Tüm kullanıcıların yaşları toplamını `SUM` ile hesaplar. |
| `findNameAndAgeFind()` | Kullanıcıların isim ve yaş bilgilerini `UserNameAndAgeDto` nesnesine dönüştürür. |
| `findNameAndAgeFindParams(name, age)` | İsim ve yaş filtreleriyle kullanıcı bilgilerini DTO olarak döndürür. |

Bu örnekler aracılığıyla JPQL ile entity sorgulama, belirli alanları seçme, parametre kullanımı ve aggregate fonksiyonları incelenmiştir.

### 2. DTO Projeksiyonu (`UserNameAndAgeDto`)

JPQL sorgularında `SELECT new ...` constructor expression kullanılarak yalnızca ihtiyaç duyulan alanlar DTO nesnesine aktarılır.

Bu yaklaşım sayesinde tüm entity alanlarını döndürmek yerine yalnızca gerekli veriler seçilebilir.

### 3. Derived Query Methods (`CarRepository`)

Spring Data JPA'nın metot isimlerinden otomatik sorgu oluşturma özelliği kullanılmıştır.

Örnek metotlar:

- `findByModel(String model)`
- `findByModel(String model, Pageable pageable)`
- `findByMarka(String marka, Pageable pageable)`
- `findByYas(Integer yas, Pageable pageable)`
- `findByFiyat(Long fiyat, Pageable pageable)`

Bu metotlar aracılığıyla farklı alanlara göre filtreleme ve sayfalı sorgulama örnekleri hazırlanmıştır.

### 4. Pagination ve Sıralama (`CarController`)

#### Modele göre sayfalama

```http
GET /api/v1/cars/modelsirala?page=0&size=10&model=...
```

Modele göre arama yapar ve sonuçları sayfalı olarak döndürür. Sonuçlar `CarMapper` aracılığıyla `CarResponse` DTO nesnelerine dönüştürülerek `PageImpl` içerisinde sunulur.

#### Dinamik alana göre arama

```http
GET /api/v1/cars/field?page=0&size=10&sort=...&field=...&deger=...
```

`field` parametresine göre hangi alanda arama yapılacağı belirlenir. `switch` yapısı kullanılarak ilgili repository metoduna yönlendirme yapılır.

Desteklenen alanlar:

- `fiyat`
- `yas`
- `marka`
- `model`

#### İncelenen diğer pagination ve sorting örnekleri

Controller içerisinde yorum satırlarında bulunan örnekler:

- **`/bilgi`**: `Page` nesnesinin toplam eleman sayısı, toplam sayfa sayısı ve ilk/son sayfa bilgileri.
- **`/teklisiralama`**: Tek bir alana göre artan (`asc`) veya azalan (`desc`) sıralama.
- **`/coklusiralama`**: Birden fazla alana göre sıralama.

  ```java
  Sort.by("marka").and(Sort.by("yas"))
  ```

- **`/listsiralama`**: Sıralı sayfalama ve sonuçların DTO nesnelerine dönüştürülmesi.

### 5. MapStruct (`CarMapper`)

MapStruct kullanılarak entity ve DTO nesneleri arasında otomatik dönüşüm örnekleri hazırlanmıştır.

| Metot | Açıklama |
|---|---|
| `toCAr(CarRequest)` | `CarRequest` nesnesini `Car` entity nesnesine dönüştürür. |
| `toCarResponse(Car)` | `Car` entity nesnesini `CarResponse` DTO nesnesine dönüştürür. |
| `toCarResponseList(List<Car>)` | `Car` listesini DTO listesine dönüştürür. |

Bu bölümde request DTO, response DTO ve entity arasındaki dönüşüm mantığı incelenmiştir.

### 6. UserController

`/api/v1/user` altında JPQL sorgularını REST endpoint'lerine bağlayan örnekler bulunmaktadır.

Aktif endpoint:

```http
GET /api/v1/user?name=...&age=...
```

İsim ve yaş parametrelerine göre filtreleme yaparak `UserNameAndAgeDto` listesini döndürür.

Eğitim boyunca geliştirilen diğer endpoint örneklerinin birçoğu, sırayla test edilebilmesi amacıyla yorum satırlarında bırakılmıştır.

## Kurulum ve Çalıştırma

### Gereksinimler

- Java 17
- Maven
- PostgreSQL

### 1. Projeyi klonlayın

```bash
git clone <repository-url>
cd JPQLExmp
```

`<repository-url>` kısmına kendi GitHub repository adresinizi yazın.

### 2. Veritabanı bağlantısını yapılandırın

PostgreSQL'i kurun ve `src/main/resources/application.properties` dosyasındaki bağlantı bilgilerini kendi ortamınıza göre düzenleyin.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=<kullanici_adi>
spring.datasource.password=<sifre>
```

Veritabanı adını, kullanıcı adını ve parolayı kendi PostgreSQL yapılandırmanıza göre güncelleyin.

### 3. Uygulamayı çalıştırın

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

Alternatif olarak projeyi IntelliJ IDEA üzerinden çalıştırabilirsiniz.

### 4. Endpoint'leri test edin

Uygulama `8081` portunda çalışacak şekilde yapılandırılmıştır.

- **Uygulama:** http://localhost:8081
- **Swagger UI:** http://localhost:8081/swagger-ui/index.html

Swagger arayüzünü kullanarak tanımlı API endpoint'lerini inceleyebilir ve istek gönderebilirsiniz.

## Önemli Notlar

- Bu proje bir **öğrenme ve pratik çalışmasıdır**.
- Amaç, Java ve Spring Boot eğitiminde öğrenilen konuları uygulamalı olarak pekiştirmektir.
- Bazı endpoint'ler ve örnekler yorum satırlarında bırakılmıştır.
- `service` paketi ve bazı sınıflar ilerleyen çalışmalar için boş olabilir.
- Veritabanı tablolarının otomatik güncellenmesi için `ddl-auto=update` yapılandırması kullanılmıştır.
- Veritabanı bağlantı bilgileri çalıştırmadan önce yerel ortama göre düzenlenmelidir.

