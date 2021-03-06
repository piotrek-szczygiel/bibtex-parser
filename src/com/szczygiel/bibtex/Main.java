package com.szczygiel.bibtex;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Main program class.
 * <p>
 * You run this program with following arguments:
 * <ul>
 * <li>-f, --file=FILE<br>path to BibTeX file</li>
 * <li>-a, --author=AUTHOR[,AUTHOR...]<br>author(s) to search for</li>
 * <li>-t, --type=TYPE[,TYPE...]<br>entry type(s) to search for</li>
 * <li>-h, --help<br>show help message and exit</li>
 * <li>-v, --version<br>print version information and exit</li>
 * </ul>
 */
@Command(name = "bibtex-parser",
        mixinStandardHelpOptions = true,
        sortOptions = false,
        version = "BibTeX parser v0.1 by Piotr Szczygieł 2018",
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "Display filtered entries from BibTeX file.",
        description = "Parses the specified BibTeX file, then displays only those entries " +
                "that match specified filters.")
public class Main implements Runnable {
    /*
    This program uses picocli package to handle program arguments.
    It enables us to do simple member annotations like those below.
    Picocli automatically assigns given arguments to those members.
     */

    /**
     * {@link File} to parse.
     */
    @Option(names = {"-f", "--file"}, required = true, paramLabel = "FILE", description = "path to BibTeX file")
    private File file;

    /**
     * Authors' names used for filtering.
     */
    @Option(names = {"-a", "--author"}, split = ",", paramLabel = "AUTHOR", description = "author(s) last name(s) to " +
            "search for")
    private Set<String> authors = new LinkedHashSet<>();

    /**
     * Entry types used for filtering.
     */
    @Option(names = {"-t", "--type"}, split = ",", paramLabel = "TYPE", description = "entry type(s) to search for")
    private Set<String> entryTypes = new LinkedHashSet<>();

    /**
     * Entry point of the program.
     *
     * @param args arguments passed from a command line
     */
    public static void main(String[] args) {
        CommandLine.run(new Main(), args);
    }

    /**
     * Main program logic.
     * <p>
     * Display {@link Filter filtered} {@link Entry entries}.
     */
    @Override
    public void run() {
        // Convert entry types from arguments to lowercase, so they match those from entry objects
        entryTypes = entryTypes.stream().map(String::toLowerCase).collect(Collectors.toSet());

        Document document = new Document();
        if (document.loadFile(file)) {
            document.parse();

            // Filter entries
            List<Entry> filteredEntries = document.getEntries();
            if (!entryTypes.isEmpty()) {
                filteredEntries = Filter.filterTypes(filteredEntries, entryTypes);
            }
            if (!authors.isEmpty()) {
                filteredEntries = Filter.filterAuthors(filteredEntries, authors);
            }

            // Display filtered entries count
            int entriesCount = filteredEntries.size();
            if (entriesCount == 0) {
                System.out.println("No entries matched specified filters.\n");
            } else if (entriesCount == 1) {
                System.out.println("Showing 1 entry:");
            } else {
                System.out.println("Showing " + entriesCount + " entries:");
            }

            // Display filtered entries
            for (Entry entry : filteredEntries) {
                System.out.println("\n\n" + PrettyFormat.table(entry));
            }
        }
    }
}
