<<<<<<< HEAD

=======
# webclient-example

Spring `WebClient` ile araç (Car) servisine REST çağrıları yapan örnek proje.
Hedef servis: `http://localhost:8081/api/car/v1`

| Metot  | Endpoint                    | Açıklama            |
|--------|-----------------------------|---------------------|
| GET    | /webclient                  | Tüm araçlar         |
| GET    | /webclient/{id}             | Id'ye göre araç     |
| GET    | /webclient/carmodel?model=  | Modele göre araç    |
| POST   | /webclient                  | Araç oluştur        |
| PUT    | /webclient/{id}             | Araç güncelle       |
| DELETE | /webclient/{id}             | Araç sil            |

Çalıştırma: `./mvnw spring-boot:run` (uygulama 8080 portunda açılır).
>>>>>>> a28fccf (WebClient Example)
