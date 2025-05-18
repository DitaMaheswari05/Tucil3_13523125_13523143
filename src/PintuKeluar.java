public class PintuKeluar {
    private Position position;
    private boolean isHorizontal;

    public PintuKeluar(int row, int col, boolean isHorizontal) {
        this.position = new Position(row, col);
        this.isHorizontal = isHorizontal;
    }

    public Position getPosition() {
        return position;
    }

    public int getRow() {
        return position.getRow();
    }

    public int getCol() {
        return position.getCol();
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    // memeriksa apakah pintu keluar cocok dengan orientasi piece
    public boolean matchOrientation(Orientation orientation) {
        return (isHorizontal && orientation == Orientation.HORIZONTAL) || 
               (!isHorizontal && orientation == Orientation.VERTICAL);
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


