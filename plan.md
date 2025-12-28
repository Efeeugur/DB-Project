# Art School: A Creative Education System - Proje Planı

## 📋 Proje Özeti

Bu proje, kullanıcıların mevsimsel sanat kurslarına katılabileceği, eğitmenlerden öğrenebileceği ve yapılandırılmış dersler aracılığıyla sanatsal becerilerini geliştirebileceği interaktif bir **Sanat Okulu Yönetim Sistemi**dir.

### Grup Üyeleri
- 64210015 - Nurefşan Ergören
- 64210039 - Efe Uğur
- 64220034 - Mustafa Alperen Erçevik

---

## 🏗️ Mimari: MVC (Model-View-Controller)

```
src/
├── model/                    # Veri katmanı
│   ├── entity/               # POJO sınıfları
│   │   ├── Student.java
│   │   ├── Instructor.java
│   │   ├── Course.java
│   │   ├── Session.java
│   │   ├── Attendance.java
│   │   ├── Payment.java
│   │   ├── SkillTest.java
│   │   └── Enrollment.java
│   │
│   └── dao/                  # Data Access Objects
│       ├── GenericDAO.java           # Temel CRUD interface
│       ├── StudentDAO.java
│       ├── InstructorDAO.java
│       ├── CourseDAO.java
│       ├── SessionDAO.java
│       ├── AttendanceDAO.java
│       ├── PaymentDAO.java
│       └── impl/                     # PostgreSQL implementasyonları
│           ├── StudentDAOImpl.java
│           ├── InstructorDAOImpl.java
│           └── ...
│
├── view/                     # Kullanıcı arayüzü (Console)
│   ├── MainMenuView.java
│   ├── StudentView.java
│   ├── InstructorView.java
│   ├── CourseView.java
│   ├── AttendanceView.java
│   ├── PaymentView.java
│   └── ReportView.java
│
├── controller/               # İş mantığı
│   ├── StudentController.java
│   ├── InstructorController.java
│   ├── CourseController.java
│   ├── AttendanceController.java
│   ├── PaymentController.java
│   └── ReportController.java
│
├── util/                     # Yardımcı sınıflar
│   ├── DatabaseConnection.java
│   ├── InputValidator.java
│   └── ConsoleHelper.java
│
├── config/                   # Konfigürasyon
│   └── DatabaseConfig.java
│
└── Main.java                 # Uygulama giriş noktası
```

---

## 📊 Veritabanı Şeması (PostgreSQL)

### Tablolar

#### 1. `instructors` - Eğitmenler
| Kolon | Tip | Açıklama |
|-------|-----|----------|
| id | SERIAL PRIMARY KEY | Benzersiz ID |
| first_name | VARCHAR(50) | Ad |
| last_name | VARCHAR(50) | Soyad |
| email | VARCHAR(100) UNIQUE | E-posta |
| phone | VARCHAR(20) | Telefon |
| specialization | VARCHAR(100) | Uzmanlık alanı (Resim, Heykel, vb.) |
| created_at | TIMESTAMP | Kayıt tarihi |

#### 2. `students` - Öğrenciler
| Kolon | Tip | Açıklama |
|-------|-----|----------|
| id | SERIAL PRIMARY KEY | Benzersiz ID |
| first_name | VARCHAR(50) | Ad |
| last_name | VARCHAR(50) | Soyad |
| email | VARCHAR(100) UNIQUE | E-posta |
| phone | VARCHAR(20) | Telefon |
| date_of_birth | DATE | Doğum tarihi |
| skill_level | VARCHAR(20) | Beginner/Intermediate/Advanced |
| created_at | TIMESTAMP | Kayıt tarihi |

#### 3. `courses` - Kurslar
| Kolon | Tip | Açıklama |
|-------|-----|----------|
| id | SERIAL PRIMARY KEY | Benzersiz ID |
| name | VARCHAR(100) | Kurs adı |
| description | TEXT | Açıklama |
| term | VARCHAR(20) | Summer/Winter |
| skill_level | VARCHAR(20) | Beginner/Intermediate/Advanced |
| instructor_id | INTEGER FK | Eğitmen referansı |
| max_capacity | INTEGER | Maksimum öğrenci sayısı |
| fee | DECIMAL(10,2) | Kurs ücreti |
| start_date | DATE | Başlangıç tarihi |
| end_date | DATE | Bitiş tarihi |

#### 4. `sessions` - Haftalık Oturumlar
| Kolon | Tip | Açıklama |
|-------|-----|----------|
| id | SERIAL PRIMARY KEY | Benzersiz ID |
| course_id | INTEGER FK | Kurs referansı |
| session_date | DATE | Oturum tarihi |
| start_time | TIME | Başlangıç saati |
| end_time | TIME | Bitiş saati |
| topic | VARCHAR(200) | Oturum konusu |

#### 5. `enrollments` - Kayıtlar
| Kolon | Tip | Açıklama |
|-------|-----|----------|
| id | SERIAL PRIMARY KEY | Benzersiz ID |
| student_id | INTEGER FK | Öğrenci referansı |
| course_id | INTEGER FK | Kurs referansı |
| enrollment_date | TIMESTAMP | Kayıt tarihi |
| status | VARCHAR(20) | Active/Completed/Dropped |

#### 6. `attendance` - Yoklama
| Kolon | Tip | Açıklama |
|-------|-----|----------|
| id | SERIAL PRIMARY KEY | Benzersiz ID |
| enrollment_id | INTEGER FK | Kayıt referansı |
| session_id | INTEGER FK | Oturum referansı |
| status | VARCHAR(20) | Present/Absent/Late |
| notes | TEXT | Notlar |

#### 7. `payments` - Ödemeler
| Kolon | Tip | Açıklama |
|-------|-----|----------|
| id | SERIAL PRIMARY KEY | Benzersiz ID |
| enrollment_id | INTEGER FK | Kayıt referansı |
| amount | DECIMAL(10,2) | Ödeme miktarı |
| payment_date | TIMESTAMP | Ödeme tarihi |
| payment_method | VARCHAR(50) | Ödeme yöntemi |
| status | VARCHAR(20) | Pending/Completed/Refunded |

#### 8. `skill_tests` - Seviye Testleri
| Kolon | Tip | Açıklama |
|-------|-----|----------|
| id | SERIAL PRIMARY KEY | Benzersiz ID |
| student_id | INTEGER FK | Öğrenci referansı |
| test_date | TIMESTAMP | Test tarihi |
| score | INTEGER | Puan (0-100) |
| assigned_level | VARCHAR(20) | Atanan seviye |
| notes | TEXT | Değerlendirme notları |

---

## 🎯 Ana Özellikler

### 1. Kayıt Yönetimi (Registration Management)
- Öğrenci kaydı
- Eğitmen kaydı
- Kurs kaydı
- Kayıt güncelleme ve silme

### 2. Mevsimsel Kurs Sistemi (Seasonal Course System)
- Yaz dönemi kursları
- Kış dönemi kursları
- Dönem bazlı kurs yönetimi

### 3. Beceri Bazlı Sınıf Yerleştirme (Skill-based Placement)
- Seviye belirleme testleri
- Beginner (Başlangıç)
- Intermediate (Orta)
- Advanced (İleri)
- Otomatik sınıf atama

### 4. Eğitmen Liderliğinde Oturumlar
- Haftalık ders planlaması
- Ders içeriği yönetimi

### 5. Yoklama Takibi (Attendance Tracking)
- Oturum bazlı yoklama
- Devamsızlık raporları

### 6. Ödeme Yönetimi (Payment Management)
- Kurs ücreti ödemeleri
- Ödeme geçmişi
- Bekleyen ödemeler

---

## 📅 Geliştirme Aşamaları

### Aşama 1: Temel Model Katmanı (Entity Sınıfları)
1. Tüm Entity sınıflarını oluştur
2. Constructor, getter/setter metodları
3. toString metodları

### Aşama 2: DAO Katmanı (Veritabanı Erişimi)
1. GenericDAO interface'i
2. Her entity için DAO interface'leri
3. PostgreSQL implementasyonları (ileride)
4. **Geçici**: In-Memory implementasyonlar (test için)

### Aşama 3: Controller Katmanı (İş Mantığı)
1. Her entity için Controller sınıfları
2. CRUD operasyonları
3. İş kuralları validasyonu
4. Seviye belirleme algoritması

### Aşama 4: View Katmanı (Konsol Arayüzü)
1. Ana menü tasarımı
2. Alt menüler
3. Input/Output işlemleri
4. Hata mesajları

### Aşama 5: PostgreSQL Entegrasyonu
1. JDBC bağlantısı
2. Connection pool
3. SQL sorgularını entegre et
4. Migration scriptleri

---

## 🔧 Kullanılacak Teknolojiler

| Teknoloji | Kullanım Alanı |
|-----------|----------------|
| Java 17+ | Ana programlama dili |
| PostgreSQL | Veritabanı |
| JDBC | Veritabanı bağlantısı |
| Maven/Gradle | Bağımlılık yönetimi |

---

## 📝 Konsol Menü Yapısı

```
╔══════════════════════════════════════════╗
║     ART SCHOOL MANAGEMENT SYSTEM         ║
╠══════════════════════════════════════════╣
║  1. Student Management                   ║
║  2. Instructor Management                ║
║  3. Course Management                    ║
║  4. Enrollment Management                ║
║  5. Attendance Management                ║
║  6. Payment Management                   ║
║  7. Reports                              ║
║  0. Exit                                 ║
╚══════════════════════════════════════════╝
```

---

## ✅ Doğrulama Planı

### Manuel Test
1. Uygulamayı çalıştır: `java -jar artschool.jar`
2. Her menü seçeneğini test et
3. CRUD operasyonlarını doğrula
4. Hata durumlarını kontrol et

### PostgreSQL Entegrasyonu Sonrası
1. Veritabanı bağlantısını test et
2. CRUD işlemlerinin veritabanına yansıdığını doğrula
3. Raporların doğru veri döndürdüğünü kontrol et

---

## 🚀 Sonraki Adımlar

1. ✅ Plan onayı al
2. ✅ Proje klasör yapısını oluştur
3. ✅ Entity sınıflarını implement et
4. ✅ DAO katmanını implement et (In-Memory)
5. ✅ Controller katmanını implement et
6. ✅ View katmanını implement et
7. ⬜ PostgreSQL bağlantısını ekle
8. ⬜ SQL Migration scriptlerini oluştur
9. ⬜ PostgreSQL DAO implementasyonlarını yaz
