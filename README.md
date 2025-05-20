# Tucil3_13523125_13523143

# Rush Hour Solver in Java

## a. Deskripsi Singkat

Program ini adalah penyelesai permainan **Rush Hour** berbasis Java. Permainan ini bertujuan memindahkan mobil utama (PrimaryPiece) ke posisi keluar (PintuKeluar) dari papan permainan yang berisi beberapa mobil penghalang.  
Program ini menggunakan tiga algoritma pencarian:

- **Uniform Cost Search (UCS)**
- **Greedy Best First Search (GBFS)**
- **A* Search (A-Star)**

Setiap algoritma akan menghasilkan solusi berupa urutan langkah pemindahan **PrimaryPiece** hingga mencapai pintu keluar, beserta informasi statistiknya.

---

## b. Requirement & Instalasi

### Requirement:

- **Java Development Kit (JDK) versi 11 atau lebih tinggi**
- Sistem operasi apapun yang mendukung Java (Windows, Linux, macOS)
- Tidak diperlukan library eksternal (menggunakan Java Standard Library)

---

## c. Cara Kompilasi Program

1. Buka terminal atau command prompt.
2. Arahkan direktori ke folder tempat seluruh file `.java` berada:
   ```bash
   cd src
   ```
3. Kompilasi seluruh file `.java`:
   ```bash
   javac -d ../bin *.java
   ```
   Perintah ini akan menghasilkan file `.class` yang dapat dijalankan dan tersimpan ke dalam folder `bin`.
4. Pindah ke direktori `bin` dengan cara:
   ```bash
   cd ../bin
   ```
5. Jalankan program utama dengan perintah:
   ```
   java Main
   ```

---

## d. Cara Menjalankan & Menggunakan Program

### Format Input

Program menerima input dari file `.txt` yang merepresentasikan konfigurasi awal papan Rush Hour

Contoh cara pemanggilan file input:
```
../test/1.txt
```

### Format Output
Program dapat menyimpan output ke file `.txt` dengan contoh format:
```
../hasil1.txt
```

### Output Program

Program akan mencetak:

- Solusi berupa langkah-langkah pemindahan kendaraan
- Jumlah langkah
- Jumlah node yang dikunjungi
- Waktu eksekusi

---

# AUTHOR
1. Dita Maheswari 13523125
2. Amira Izani 13523143

---
