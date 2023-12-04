package preet.commonHelpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class FileLineReader implements Iterator<String>, Iterable<String> {

    private final Scanner scanner;
    public FileLineReader(final String pathToFile) throws FileNotFoundException {
        this.scanner = new Scanner(new File(pathToFile));
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNextLine();
    }

    @Override
    public String next() {
        return scanner.nextLine();
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }
}

