
public class PrimaryPiece extends Piece {

    public PrimaryPiece(int size, boolean isHorizontal) {
        super(size, isHorizontal, "P");
    }
    // id piece nya p
    // permainan bergantung jika primry piece sudah keluar dari pintu keluar atau K
    // K akan selalu sebaris dengan P, ketika P horizontal, K juga horizontal
    // ketika P vertikal, K juga vertikal
    // K sebagai penanda
}

// jadi dalam p dan k dalam txt merupakan input untuk primary piece dan k
// merupakan pintu keeluar. jadi nanti akan dibuat class primary piece dan pintu
// keluar. untuk saat ini belum ada impelemntasi khusus untuk keduanya, anmun
// tolong buat input handlernya dulu
