import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

public class InputHandler {
    private int width;
    private int height;
    private int numPieces;
    private Map<String, Piece> pieces;
    private PrimaryPiece primaryPiece;
    private PintuKeluar pintuKeluar;
    private Papan papan;

    private static final Logger logger = Logger.getLogger(InputHandler.class.getName());

    public InputHandler() {
        pieces = new HashMap<>();
    }

    /**
     * Reads the puzzle configuration from a file
     * 
     * @param fileName the name of the file to read
     * @throws IOException if there's an error reading the file
     */
    public void readConfigFromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        // Read dimensions
        String[] dimensions = reader.readLine().split(" ");
        height = Integer.parseInt(dimensions[0]);
        width = Integer.parseInt(dimensions[1]);

        // Read number of non-primary pieces (unused in this example, but read anyway)
        numPieces = Integer.parseInt(reader.readLine());

        // Read all remaining lines (including possible extra border lines)
        ArrayList<String> allLines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                allLines.add(line);
            }
        }
        reader.close();

        // Cari posisi K (pintu keluar)
        // Bisa di luar papan (di baris sebelum papan, sesudah papan, atau di luar
        // kolom)
        boolean foundK = false;
        int exitRow = -1, exitCol = -1;
        boolean isHorizontal = false;

        // Misal papan dimulai di baris tertentu dalam allLines:
        // Karena kita baca height baris papan, coba cari window baris mana yang jadi
        // papan:
        // Asumsi: papan ada di tengah allLines, bisa coba cari window height baris yang
        // cocok (atau asumsi langsung di tengah)

        // Sederhana: cari semua K di allLines, cari posisi dengan memperhitungkan papan
        // di tengah:
        for (int i = 0; i < allLines.size(); i++) {
            String currLine = allLines.get(i);
            for (int j = 0; j < currLine.length(); j++) {
                if (currLine.charAt(j) == 'K') {
                    foundK = true;
                    exitRow = i;
                    exitCol = j;
                    break;
                }
            }
            if (foundK)
                break;
        }

        if (!foundK) {
            throw new IllegalArgumentException("Pintu keluar (K) tidak ditemukan di papan.");
        }

        // Sekarang kita asumsikan papan berada di allLines dari baris tertentu
        // Kita cari window baris di allLines yang ukurannya height dengan asumsi papan
        // ada di tengah (atau coba cocokkan)
        // Supaya mudah, coba papan dimulai di baris: startIndex = exitRow - height/2
        // (asumsi pintu keluar di luar board)
        int startIndex = -1;

        // Coba cari startIndex agar allLines.subList(startIndex, startIndex + height)
        // panjangnya height dan tiap baris >= width
        for (int possibleStart = 0; possibleStart <= allLines.size() - height; possibleStart++) {
            boolean valid = true;
            for (int k = 0; k < height; k++) {
                if (allLines.get(possibleStart + k).length() < width) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                startIndex = possibleStart;
                break;
            }
        }

        if (startIndex == -1) {
            throw new IllegalArgumentException("Tidak ditemukan baris papan yang valid di file input.");
        }

        // Inisialisasi papan 2D
        char[][] board = new char[height][width];

        for (int i = 0; i < height; i++) {
            String rowLine = allLines.get(startIndex + i);
            for (int j = 0; j < width; j++) {
                board[i][j] = rowLine.charAt(j);
            }
        }

        // Tentukan apakah pintu keluar horizontal atau vertical
        // Jika exitRow < startIndex → pintu keluar di atas papan → horizontal
        // Jika exitRow >= startIndex + height → pintu keluar di bawah papan →
        // horizontal
        // Jika exitRow di dalam range papan → pintu keluar di samping → vertical
        if (exitRow < startIndex || exitRow >= startIndex + height) {
            isHorizontal = false;
            // Karena pintu di luar baris papan, exitRow relatif ke papan = -1 (atas) atau
            // height (bawah)
            if (exitRow < startIndex) {
                exitRow = -1; // atas
                logger.info("Pintu keluar di atas papan.");
            } else {
                exitRow = height; // bawah
                logger.info("Pintu keluar di bawah papan.");
            }
        } else {
            isHorizontal = true;
            // pintu keluar di samping kiri (-1) atau kanan (width)
            if (exitCol < 0) {
                exitCol = -1;
                logger.info("Pintu keluar di kiri papan.");
            } else if (exitCol >= width) {
                exitCol = width;
                logger.info("Pintu keluar di kanan papan.");
            }
            // exitRow sudah disesuaikan karena di dalam papan
            exitRow = exitRow - startIndex;
        }

        pintuKeluar = new PintuKeluar(exitRow, exitCol, isHorizontal);

        // Simpan dan proses papan
        logger.info("Papan dimensi: " + height + " x " + width);
        papan = new Papan(width, height);
        processBoardConfiguration(board);
        printBoard(papan, pintuKeluar);
    }

    /**
     * Processes the board configuration to identify all pieces
     * 
     * @param board the board configuration as a 2D array of characters
     */
    private void processBoardConfiguration(char[][] board) {
        processPieces(board);
        for (Piece piece : pieces.values()) {
            papan.addPiece(piece);
        }
    }

    /**
     * Identifies all pieces in the board configuration
     * 
     * @param board the board configuration
     */
    private void processPieces(char[][] board) {
        boolean[][] processed = new boolean[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (processed[i][j] || board[i][j] == '.' || board[i][j] == 'K') {
                    continue;
                }

                String pieceId = String.valueOf(board[i][j]);

                Piece piece;
                if (pieceId.equals("P")) {
                    primaryPiece = new PrimaryPiece();
                    piece = primaryPiece;
                } else {
                    piece = new Piece(pieceId);
                }

                identifyPieceCells(board, processed, piece, i, j, pieceId);
                piece.determineOrientation();
                pieces.put(pieceId, piece);
            }
        }
    }

    private void identifyPieceCells(char[][] board, boolean[][] processed, Piece piece, int startRow, int startCol,
            String pieceId) {
        piece.addPosition(startRow, startCol);
        processed[startRow][startCol] = true;

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
        for (int[] dir : directions) {
            int newRow = startRow + dir[0];
            int newCol = startCol + dir[1];

            if (isValidCell(newRow, newCol) && !processed[newRow][newCol] &&
                    board[newRow][newCol] == pieceId.charAt(0)) {
                identifyPieceCells(board, processed, piece, newRow, newCol, pieceId);
            }
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    public Map<String, Piece> getPieces() {
        return pieces;
    }

    public PrimaryPiece getPrimaryPiece() {
        return primaryPiece;
    }

    public PintuKeluar getPintuKeluar() {
        return pintuKeluar;
    }

    public Papan getPapan() {
        return papan;
    }

    private void printBoard(Papan papan, PintuKeluar pintuKeluar) {
        // ANSI color codes
        final String RESET = "\u001B[0m";
        final String PRIMARY_COLOR = "\u001B[32m"; // Green for primary piece
        final String EXIT_COLOR = "\u001B[35m"; // Purple for exit door
        final String MOVED_COLOR = "\u001B[31m"; // Red for moved piece

        int height = papan.getHeight();
        int width = papan.getWidth();

        boolean isHorizontalExit = pintuKeluar.isHorizontal();
        int exitRow = pintuKeluar.getRow();
        int exitCol = pintuKeluar.getCol();

        // Print top exit if it exists (exitRow == -1)
        if (exitRow == -1) {
            // Print top border with exit
            for (int col = 0; col < width; col++) {
                if (col == exitCol) {
                    System.out.print(EXIT_COLOR + "K" + RESET);
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        // Print the board rows
        for (int row = 0; row < height; row++) {
            // Print left exit if it exists (exitCol == -1 and current row matches exitRow)
            if (exitCol == -1 && row == exitRow) {
                System.out.print(EXIT_COLOR + "K" + RESET);
            } else if (exitCol == -1) {
                System.out.print(" ");
            }

            // Print board content
            for (int col = 0; col < width; col++) {
                Piece piece = papan.getPiece(row, col);

                if (piece != null) {
                    String pieceId = piece.getId();

                    if (pieceId.equals("P")) {
                        System.out.print(PRIMARY_COLOR + pieceId + RESET);
                    } else {
                        System.out.print(pieceId);
                    }
                } else {
                    System.out.print(".");
                }
            }

            // Print right exit if it exists (exitCol == width and current row matches
            // exitRow)
            if (exitCol == width && row == exitRow) {
                System.out.print(EXIT_COLOR + "K" + RESET);
            }

            System.out.println();
        }

        // Print bottom exit if it exists (exitRow == height)
        if (exitRow == height) {
            // Print bottom border with exit
            for (int col = 0; col < width; col++) {
                if (col == exitCol) {
                    System.out.print(EXIT_COLOR + "K" + RESET);
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}