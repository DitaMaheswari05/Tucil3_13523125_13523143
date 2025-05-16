import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CLI {
    private static final String TEST_FOLDER = "../test/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Papan papan = processFile(scanner);

            System.out.println("\nPapan permainan setelah diproses:");
            papan.printPapan();

            int algoritmaPilihan = pilihAlgoritma(scanner);

            executeAlgorithm(algoritmaPilihan, papan);

        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static Papan processFile(Scanner scanner) throws IOException {
        System.out.println("Masukkan File .txt Konfigurasi Permainan:");
        String inputFile = scanner.next();
        File inputFileLocation = new File(TEST_FOLDER + inputFile);

        if (!inputFileLocation.exists()) {
            throw new IOException("File tidak ditemukan: " + inputFileLocation.getAbsolutePath());
        }

        System.out.println("Memproses file: " + inputFileLocation.getAbsolutePath());
        return InputHandler.processInputFile(inputFileLocation.getPath());
    }

    private static int pilihAlgoritma(Scanner scanner) {
        System.out.println("\nPilih Algoritma pathfinding:");
        System.out.println("1. Greedy Best First Search");
        System.out.println("2. UCS (Uniform Cost Search)");
        System.out.println("3. A*");

        int pilihan = 0;
        while (pilihan < 1 || pilihan > 3) {
            System.out.print("Masukkan pilihan (1-3): ");
            try {
                pilihan = scanner.nextInt();
                if (pilihan < 1 || pilihan > 3) {
                    System.out.println("Pilihan tidak valid! Silakan pilih 1-3.");
                }
            } catch (Exception e) {
                System.out.println("Input tidak valid! Masukkan angka 1-3.");
                scanner.next();
            }
        }

        return pilihan;
    }

    private static void executeAlgorithm(int pilihan, Papan papan) {
        System.out.println("\nMenjalankan algoritma: ");

        switch (pilihan) {
            case 1:
                System.out.println("Greedy Best First Search");
                // Panggil fungsi Greedy Best First Search
                break;

            case 2:
                System.out.println("UCS (Uniform Cost Search)");
                // Panggil fungsi UCS
                break;

            case 3:
                System.out.println("A*");
                // Panggil fungsi A*
                break;
        }

        // papan.printPapan();

    }
}