package com.szczygiel.bibtex;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Main program class.
 * <p>
 * You run this program with following arguments:
 * <p>
 * -f, --file=FILE                    path to BiBteX file
 * -a, --author=AUTHOR[,AUTHOR...]    author(s) to search for
 * -t, --type=TYPE[,TYPE...]          entry type(s) to search for
 * <p>
 * -h, --help                         show help message and exit
 * -v, --version                      print version information and exit
 */
@Command(name = "bibtex",
        mixinStandardHelpOptions = true,
        sortOptions = false,
        version = "BiBteX parser v0.1 by Piotr Szczygieł",
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "Display filtered entries from BiBteX file.",
        description = "Parses the specified BiBteX file, then displays only those entries " +
                "that match specified filters")
public class Main implements Runnable {
    /*
    This program uses picocli package to handle program arguments.
    It enables us to do simple member annotations like those below.
    Picocli automatically assigns given arguments to those members.
     */

    @Option(names = {"-f", "--file"}, required = true, paramLabel = "FILE", description = "path to BiBteX file")
    private File file;

    @Option(names = {"-a", "--author"}, split = ",", paramLabel = "AUTHOR", description = "author(s) to search for")
    private Set<String> authors = new HashSet<>();

    @Option(names = {"-t", "--type"}, split = ",", paramLabel = "TYPE", description = "entry type(s) to search for")
    private Set<String> entryTypes = new HashSet<>();

    /**
     * Entry point for program.
     *
     * @param args arguments passed from command line
     */
    public static void main(String[] args) {
        CommandLine.run(new Main(), args);
    }

    /**
     * Main program logic.
     * <p>
     * Display filtered entries.
     */
    public void run() {
        Document document = new Document();
        if (document.load(file)) {
            document.parse();

            List<Entry> filteredEntries = document.getEntries();
            if (!entryTypes.isEmpty()) {
                filteredEntries = Filter.filterTypes(filteredEntries, entryTypes);
            }
            if (!authors.isEmpty()) {
                filteredEntries = Filter.filterAuthors(filteredEntries, authors);
            }

            for (Entry entry : filteredEntries) {
                System.out.println(entry);
            }
        }
    }
}
