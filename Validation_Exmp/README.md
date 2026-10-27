# ValidationExmp

Spring Boot ile geliştirilmiş, **Bean Validation (Jakarta Validation)** yapısını hem hazır anotasyonlarla hem de **özel (custom) validator'larla** göstermeyi amaçlayan bir örnek projedir.

## İçindekiler

- [Proje Hakkında](#proje-hakkında)
- [Özellikler](#özellikler)
- [Teknolojiler](#teknolojiler)
- [Proje Yapısı](#proje-yapısı)
- [Kurulum](#kurulum)
- [Yapılandırma](#yapılandırma)
- [API Kullanımı](#api-kullanımı)
- [Doğrulama Kuralları](#doğrulama-kuralları)
- [Özel Validator'lar Nasıl Çalışır?](#özel-validatorlar-nasıl-çalışır)
- [Katkıda Bulunma](#katkıda-bulunma)
- [Lisans](#lisans)

---

## Proje Hakkında

Bu proje, gelen istek (request) verilerinin Spring Boot uygulamasında nasıl doğrulanacağını öğrenmek isteyenler için hazırlanmış küçük bir örnektir. İçinde üç tane özel doğrulama anotasyonu bulunur:

| Anotasyon | Seviye | Amaç |
|-----------|--------|------|
| `@NotAdmin` | Alan (field) | Değerin `admin` olmasını engeller (büyük/küçük harf duyarsız) |
| `@NotCiftSayi` | Alan (field) | Çift sayı girilmesini engeller, yalnızca tek sayı kabul edilir |
| `@ConfirmPassword` | Sınıf (class) | `password` ve `confirmPassword` alanlarının eşleşmesini kontrol eder |

## Özellikler

- Hazır anotasyonlar: `@Size`, `@NotBlank`, `@NotNull`, `@Max`
- Alan seviyesinde özel doğrulama (`@NotAdmin`, `@NotCiftSayi`)
- Sınıf seviyesinde, birden fazla alanı karşılaştıran özel doğrulama (`@ConfirmPassword`)
- `@Valid` ile controller katmanında otomatik doğrulama
- Türkçe hata mesajları
- JPA + PostgreSQL altyapısı (`Car` entity'si)
- Lombok ile sade kod

## Teknolojiler

- **Java 17**
- **Spring Boot 4.1.2**
  - Spring Web MVC
  - Spring Data JPA
  - Spring Validation
  - Spring Boot Actuator
  - Spring Boot DevTools
- **PostgreSQL**
- **Lombok**
- **MapStruct** 1.6.3
- **Maven** (Maven Wrapper dahil)

## Proje Yapısı

```
ValidationExmp
├── src
│   ├── main
│   │   ├── java/com/example/ValidationExmp
│   │   │   ├── annotation          # Özel doğrulama anotasyonları
│   │   │   │   ├── ConfirmPassword.java
│   │   │   │   ├── NotAdmin.java
│   │   │   │   └── NotCiftSayi.java
│   │   │   ├── controller
│   │   │   │   └── RegisterController.java
│   │   │   ├── dto
│   │   │   │   ├── CarRequest.java
│   │   │   │   ├── CarResponse.java
│   │   │   │   └── UserRegisterDto.java
│   │   │   ├── entity
│   │   │   │   └── Car.java
│   │   │   ├── validation          # Anotasyonların validator sınıfları
│   │   │   │   ├── AdminValidator.java
│   │   │   │   ├── CiftSayiValidator.java
│   │   │   │   └── ConfirmPasswordValidator.java
│   │   │   └── ValidationExmpApplication.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java/com/example/ValidationExmp
│           └── ValidationExmpApplicationTests.java
├── pom.xml
├── mvnw / mvnw.cmd
└── README.md
```

## Kurulum

### Gereksinimler

- JDK 17 veya üzeri
- PostgreSQL (varsayılan olarak `localhost:5432`)
- Maven (opsiyonel, projedeki `mvnw` kullanılabilir)

### Adımlar

1. Projeyi klonlayın:

   ```bash
   git clone https://github.com/<kullanici-adi>/ValidationExmp.git
   cd ValidationExmp
   ```

2. PostgreSQL üzerinde bir veritabanı oluşturun (veya mevcut `postgres` veritabanını kullanın) ve `application.properties` dosyasındaki bağlantı bilgilerini kendi ortamınıza göre düzenleyin.

3. Uygulamayı çalıştırın:

   **Linux / macOS**
   ```bash
   ./mvnw spring-boot:run
   ```

   **Windows**
   ```bash
   mvnw.cmd spring-boot:run
   ```

4. Uygulama varsayılan olarak `http://localhost:8080` adresinde çalışır.

## Yapılandırma

Ayarlar `src/main/resources/application.properties` dosyasındadır:

```properties
spring.application.name=ValidationExmp
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=<sifreniz>
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

> **Güvenlik notu:** Veritabanı şifresini doğrudan repoya göndermeyin. Ortam değişkeni kullanabilirsiniz:
> `spring.datasource.password=${DB_PASSWORD}`

## API Kullanımı

### `POST /register`

Kullanıcı kayıt isteğini alır ve `@ConfirmPassword` doğrulamasından geçirir.

**İstek**

```http
POST /register
Content-Type: application/json
```

```json
{
  "username": "ahmet",
  "password": "123456",
  "confirmPassword": "123456",
  "email": "ahmet@example.com"
}
```

**Başarılı yanıt** — `200 OK`

```
user register successfully
```

**Hatalı istek** (şifreler eşleşmiyor) — `400 Bad Request`

```json
{
  "username": "ahmet",
  "password": "123456",
  "confirmPassword": "654321",
  "email": "ahmet@example.com"
}
```

Doğrulama hatası mesajı: `şifreler eşleşmiyor...`

## Doğrulama Kuralları

### `CarRequest`

| Alan | Kural | Hata Mesajı |
|------|-------|-------------|
| `marka` | 3–20 karakter, `admin` olamaz | `{isim 'admin' olamaz}` |
| `model` | Boş olamaz, 3–25 karakter | `model boş olamaz` / `karakter sınırı yetersiz veya aşıldı` |
| `fiyat` | Çift sayı olamaz (yalnızca tek sayı) | `{girilen değer çift olmamalıdır}` |
| `yil` | Boş olamaz, en fazla 10 | `yılı bos olamaz` / `10 yılı geçmemeli` |

### `UserRegisterDto`

| Kural | Hata Mesajı |
|-------|-------------|
| `password` ve `confirmPassword` aynı olmalı (ikisi de `null` olamaz) | `şifreler eşleşmiyor...` |

## Özel Validator'lar Nasıl Çalışır?

Her özel doğrulama iki parçadan oluşur:

1. **Anotasyon** (`annotation` paketi): `@Constraint(validatedBy = ...)` ile validator sınıfına bağlanır ve varsayılan hata mesajını tanımlar.
2. **Validator sınıfı** (`validation` paketi): `ConstraintValidator<A, T>` arayüzünü uygular, `isValid` metodunda asıl kontrolü yapar.

**Örnek: `@NotAdmin`**

```java
public class AdminValidator implements ConstraintValidator<NotAdmin, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.equalsIgnoreCase("admin");
    }
}
```

> `null` değerler özel validator'larda geçerli sayılır. Boş değer kontrolü için `@NotNull` veya `@NotBlank` ile birlikte kullanılmalıdır.

**Kullanım**

```java
@Size(min = 3, max = 20)
@NotAdmin
private String marka;
```

## Testler

```bash
./mvnw test
```

> Not: `contextLoads` testi uygulama bağlamını başlatır, bu nedenle çalışan bir PostgreSQL bağlantısı gerektirir.

## Katkıda Bulunma

1. Projeyi fork'layın
2. Yeni bir branch oluşturun (`git checkout -b ozellik/yeni-ozellik`)
3. Değişikliklerinizi commit edin (`git commit -m "Yeni özellik eklendi"`)
4. Branch'inizi push edin (`git push origin ozellik/yeni-ozellik`)
5. Pull Request açın

## Lisans

Bu proje için henüz bir lisans belirlenmemiştir. İstediğiniz lisansı (ör. MIT) ekleyebilirsiniz.
