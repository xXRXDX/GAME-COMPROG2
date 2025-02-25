package main;

public class GridReference {

    /**
     * Returns a tile ID like "A-1", "B-1", etc.
     *
     * @param colIndex zero-based column index
     * @param rowIndex zero-based row index
     * @return A string like "A-1" or "AA-10"
     */
    public static String getTileID(int colIndex, int rowIndex) {
        String columnLabel = getColumnLabel(colIndex);
        // Rows are typically 1-based in "spreadsheet" style
        int rowLabel = rowIndex + 1;
        return columnLabel + "-" + rowLabel;
    }

    /**
     * Converts 0 -> A, 1 -> B, ..., 25 -> Z, 26 -> AA, 27 -> AB, etc.
     */
    private static String getColumnLabel(int colIndex) {
        StringBuilder label = new StringBuilder();
        int temp = colIndex;
        while (temp >= 0) {
            // Prepend the character for the current "digit"
            label.insert(0, (char) ('A' + (temp % 26)));
            temp = (temp / 26) - 1;
        }
        return label.toString();
    }
}
