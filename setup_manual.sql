-- Veritabanı Kurulum Komutları
-- Bu komutları psql'de manuel olarak çalıştırın

-- ADIM 1: Veritabanını oluştur
CREATE DATABASE "DB-Project";

-- ADIM 2: Yeni kullanıcı oluştur
CREATE USER art_school_user WITH PASSWORD 'ArtSchool2024!';

-- ADIM 3: Yetkileri ver
GRANT ALL PRIVILEGES ON DATABASE "DB-Project" TO art_school_user;

-- ADIM 4: DB-Project veritabanına geç
\c "DB-Project"

-- ADIM 5: Schema yetkilerini ver
GRANT ALL ON SCHEMA public TO art_school_user;

-- Artık schema.sql dosyasını çalıştırabilirsiniz
