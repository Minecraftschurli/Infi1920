package gburkl;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Georg Burkl
 * @version 2019-10-03
 */
public class CSVReader {
    private File file;
    private String[] header;
    private int size;
    private String delimiter;

    /**
     * Creates a new {@link CSVReader} for the given {@link File} object with the flag for forcing the {@link JFileChooser} prompt to open and a given delimiter
     * @param file the {@link File} for the file to read
     * @param chooser the flag to force the {@link JFileChooser} prompt to open
     * @param delimiter the delimiter to use when reading
     */
    public CSVReader (File file, boolean chooser, String delimiter){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        while (file == null || !file.exists()){
            if(chooser || JOptionPane.showConfirmDialog(null, Constants.FILE_DOESNT_EXIST) == JOptionPane.OK_OPTION){
                chooser = false;
                while (file == null) {
                    int code = fileChooser.showOpenDialog(null);
                    if (code != JFileChooser.APPROVE_OPTION) System.exit(0);
                    file = fileChooser.getSelectedFile();
                }
            }
        }
        this.setDelimiter(delimiter);
        this.delimiter = delimiter;

        this.setFile(file);

        try (Scanner s = new Scanner(new BufferedReader(new FileReader(this.file)))) {
            s.useDelimiter("\n");
            int c = -2;
            while (s.hasNext()) {
                s.next();
                c++;
            }
            this.size = c;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new {@link CSVReader} for the given {@link File} object with the flag for forcing the {@link JFileChooser} prompt to open
     * @param file the {@link File} for the file to read
     * @param chooser the flag to force the {@link JFileChooser} prompt to open
     */
    public CSVReader(File file, boolean chooser) {
        this(file, chooser, ";");
    }

    /**
     * Creates a new {@link CSVReader} for the given {@link File} object
     * @param file the {@link File} for the file to read
     */
    public CSVReader(File file){
        this(file, false);
    }

    /**
     * Creates a new {@link CSVReader} with default settings
     */
    public CSVReader(){
        this(null, true);
    }

    /**
     * Gets the entry at the given index as a {@link Map}
     * @param index the index to fetch the entry from
     * @return the map of the entry
     */
    public Map<String, String> getAsMap(int index) {
        String[] data = read(index+1);
        return IntStream.range(0, this.header.length).boxed().collect(Collectors.toMap(i -> this.header[i], i -> data[i]));
    }

    /**
     * Gets the header for the file
     * @return the header of the file
     */
    public String[] getHeader() {
        return this.header;
    }

    /**
     * Get the entry at the given index
     * @param index the index for the entry
     * @return the entry as {@link String[]}
     */
    public String[] get(int index) {
        return read(index+1);
    }

    /**
     * Set the {@link File} for this {@link CSVReader}
     * @param file the {@link File} to set
     */
    private void setFile(File file) {
        this.file = file;
        this.header = this.readHeader();
    }

    /**
     * Sets the delimiter to use when reading
     * @param delimiter the delimiter to use
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Read the header row of the previously set file and return it as {@link String[]}
     * @return the header of the file
     */
    private String[] readHeader() {
        return read(0);
    }

    /**
     * Read the row at the given index and return it as {@link String[]} split at the previously set delimiter
     * @param line the index of the line to read
     * @return the read line split at the previously set delimiter as {@link String[]} or a {@link String[]} of length 0
     */
    private String[] read(int line) {
        try (Scanner s = new Scanner(new BufferedReader(new FileReader(this.file)))) {
            s.useDelimiter("\n");
            int c = 0;
            while (s.hasNext()) {
                if (c == line) {
                    return s.next().split(this.delimiter);
                } else {
                    s.next();
                    c++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    /**
     * Gets the number of entries in the file
     * @return the amount of entries in the file
     */
    public int size() {
        return this.size;
    }
}
