#!/bin/bash

# PostgreSQL Database Setup Script
# Art School Management System

echo "🗄️  PostgreSQL Veritabanı Kurulum Scripti"
echo "=========================================="
echo ""

# PostgreSQL binary path
PSQL="/Library/PostgreSQL/18/bin/psql"

# Configuration
DB_NAME="DB-Project"
DB_USER="art_school_user"
DB_PASSWORD="ArtSchool2024!"  # Default password - change this!
DB_HOST="localhost"
DB_PORT="5432"

echo "Yapılandırma:"
echo "  Veritabanı: $DB_NAME"
echo "  Kullanıcı: $DB_USER"
echo "  Host: $DB_HOST"
echo "  Port: $DB_PORT"
echo ""

echo "⚠️  NOT: Bu script postgres kullanıcısı ile çalışmalıdır."
echo "PostgreSQL şifrenizi girmeniz istenecek."
echo ""

# Create database and user
echo "📝 Veritabanı ve kullanıcı oluşturuluyor..."
$PSQL -U postgres -h $DB_HOST -p $DB_PORT -c "CREATE DATABASE \"$DB_NAME\";" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ Veritabanı oluşturuldu: $DB_NAME"
else
    echo "⚠️  Veritabanı zaten mevcut veya hata oluştu"
fi

# Create user
$PSQL -U postgres -h $DB_HOST -p $DB_PORT -c "CREATE USER $DB_USER WITH PASSWORD '$DB_PASSWORD';" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ Kullanıcı oluşturuldu: $DB_USER"
else
    echo "⚠️  Kullanıcı zaten mevcut veya hata oluştu"
fi

# Grant privileges
echo "🔑 Yetkiler veriliyor..."
$PSQL -U postgres -h $DB_HOST -p $DB_PORT -c "GRANT ALL PRIVILEGES ON DATABASE \"$DB_NAME\" TO $DB_USER;"
$PSQL -U postgres -h $DB_HOST -p $DB_PORT -d "$DB_NAME" -c "GRANT ALL ON SCHEMA public TO $DB_USER;"

echo "✅ Yetkiler verildi"
echo ""

# Create tables
echo "📊 Tablolar oluşturuluyor..."
$PSQL -U $DB_USER -h $DB_HOST -p $DB_PORT -d "$DB_NAME" -f "$(dirname "$0")/schema.sql"

if [ $? -eq 0 ]; then
    echo "✅ Tablolar başarıyla oluşturuldu"
else
    echo "❌ Tablo oluşturma hatası"
    exit 1
fi

echo ""
echo "✅ Kurulum tamamlandı!"
echo ""
echo "📌 Bağlantı Bilgileri:"
echo "  Veritabanı: $DB_NAME"
echo "  Kullanıcı: $DB_USER"
echo "  Şifre: $DB_PASSWORD"
echo "  JDBC URL: jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME"
echo ""
echo "⚠️  ÖNEMLİ: Şifreyi değiştirmeyi unutmayın!"
