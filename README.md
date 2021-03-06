<h1 align="center">BibTeX parser</h1>
<p align="center">
    Simple
    <a href="https://www.ctan.org/pkg/bibtex">BibTeX</a>
    parser written in
    <a href="https://www.java.com">Java</a>.
</p>
<p align="center">
    <a href="https://www.ctan.org/pkg/bibtex">
        <img height=70 src="https://upload.wikimedia.org/wikipedia/commons/3/30/BibTeX_logo.svg" alt="BibTeX logo">
    </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="https://www.java.com">
        <img height=150 src="https://upload.wikimedia.org/wikipedia/en/thumb/3/30/Java_programming_language_logo.svg/1024px-Java_programming_language_logo.svg.png" alt="Java logo">
    </a>
</p>


## Usage

```console
piotr@szczygiel:bibtex-parser$ java -jar bibtex-parser.jar -f <file> -t <type1,type2,...> -a <author1,author2,...>
```

<p align="center">
    <img height=350 src="usage.png" alt="Usage">
</p>

## Example

```console
piotr@szczygiel:bibtex-parser$ java -jar bibtex-parser.jar -f example.bib -t book -a Knuth
```
<p align="center">
    <img height=350 src="example.png" alt="Example">
</p>
