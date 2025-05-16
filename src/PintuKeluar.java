public class PintuKeluar 

{
    private int x;
    private int y;
    private boolean isHorizontal;

    public PintuKeluar(int x, int y, boolean isHorizontal) {
        this.x = x;
        this.y = y;
        this.isHorizontal = isHorizontal;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isHorizontal() {
        return isHorizontal;
    }
}
    // ditandai dengan K

    // K harus memiliki orientasi yang sama dengan P (misal P baris pertama, K juga
    // baris pertama)
    // pintu keluar hanya bisa diakses oleh primary piece
    // pintu keluar tidak bisa diakses oleh piece lain
    // apabila primary piece, berhasil melewati K, maka permainan berakhir

    // berhubungan dengan papan
    // K diluar papan
    // akan tetapi K perlu mengetahui panjang dan lebar papan
    // misalnya (papan 4x4)

    // PP..K
    // ....
    // ....
    // ....

    // atau

    // P...
    // P...
    // ....
    // ....
    // K

    // contoh dibawah tidak bisa

    // PP..
    // ....
    // ....
    // ....
    // K

    // PP..
    // ....
    // ....
    // ....
    // K


