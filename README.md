# HSE Reporter Pro 👷‍♂️📱

HSE Reporter Pro, İş Sağlığı ve Güvenliği (İSG) profesyonelleri için özel olarak geliştirilmiş, saha denetimlerini ve raporlama süreçlerini dijitalleştiren modern bir Android uygulamasıdır. 

Kullanıcı dostu arayüzü sayesinde sahada hızlıca veri girişi yapılabilir, fotoğraflar eklenebilir ve anında PDF formatında profesyonel raporlar üretilebilir. Uygulama tamamen çevrimdışı (offline) çalışacak şekilde tasarlanmıştır.

---

## 🚀 Temel Özellikler

* **Dinamik Gösterge Paneli (Sol Menü):** * Kullanıcı profili ve resmi.
    * Kayıtlı rapor ve firma sayılarını anlık gösteren canlı rozetler (Badges).
* **Saha Gözlem Raporları:** * Konum, risk seviyesi ve detay girişi.
    * Cihaz kamerası veya galeriden anlık fotoğraf ekleme yeteneği.
    * Raporları tek tıkla A4 formatında PDF'e dönüştürme ve cihaza kaydetme.
* **Ramak Kala (Near Miss) Raporları:** * Olay yeri, tanık ve yönetim bildirim durumlarını kayıt altına alma.
* **İş Kazası Raporları:** * Yaralanan personel, kaza tipi ve personel statüsü gibi kritik verilerin işlenmesi.
* **Firma Yönetimi:** * Hizmet verilen firmaların tehlike sınıflarına (Az Tehlikeli, Tehlikeli, Çok Tehlikeli) göre renk kodlarıyla kaydedilmesi ve iletişim bilgilerinin tutulması.
* **Mevzuat ve Döküman Kütüphanesi:** * Türkiye güncel İSG mevzuatlarında hızlı arama yapma ve resmi kaynaklara (mevzuat.gov.tr) doğrudan yönlendirme.
    * Kullanıcının kendi PDF dökümanlarını uygulamaya entegre edebilmesi.

---

## 🛠️ Kullanılan Teknolojiler ve Mimari

Bu proje, modern Android geliştirme standartlarına uygun olarak **MVVM (Model-View-ViewModel)** mimarisi üzerine inşa edilmiştir.

* **Kullanıcı Arayüzü (UI):** Jetpack Compose, Material Design 3
* **Programlama Dili:** Kotlin
* **Yerel Veritabanı:** Room Database (SQLite tabanlı, asenkron veri akışı için Coroutines & Flow)
* **Navigasyon:** Jetpack Navigation Compose
* **Görsel İşleme:** Coil (Asenkron resim yükleme ve kırpma işlemleri)
* **Dosya Yönetimi & Çıktı:** AndroidX PDFDocument, MediaStore API (Modern ve güvenli dosya indirme/okuma)

---

## 📥 Kurulum ve Çalıştırma

Projeyi kendi cihazınızda veya emülatörünüzde çalıştırmak için aşağıdaki adımları izleyebilirsiniz:

1.  Bu depoyu bilgisayarınıza klonlayın:
    ```bash
    git clone [https://github.com/KULLANICI_ADIN/HSEReporterPro.git](https://github.com/KULLANICI_ADIN/HSEReporterPro.git)
    ```
2.  Android Studio'yu açın ve `Open an existing project` seçeneği ile klonladığınız klasörü seçin.
3.  Gradle senkronizasyonunun tamamlanmasını bekleyin.
4.  Cihazınızı bağlayın veya bir emülatör başlatın.
5.  Üst menüden **Run > Run 'app'** (Yeşil Ok) butonuna tıklayarak uygulamayı derleyin ve çalıştırın.

---

## 📸 Ekran Görüntüleri

*(Not: GitHub deponuza yüklediğiniz ekran görüntülerini buraya `![Açıklama](resim_linki.jpg)` formatında ekleyebilirsiniz.)*

---

## 👨‍💻 Geliştirici

**Alaaddin**
*Yazılım Geliştirici & İş Sağlığı ve Güvenliği Uzmanı*

İSG sahasındaki pratik ihtiyaçları, modern yazılım teknolojileriyle buluşturmayı hedefleyerek geliştirilmiştir.
