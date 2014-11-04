# Jipsy

This is a simple language similar to [Brainfu**](https://en.wikipedia.org/wiki/Brainfuck). It is useful only to demostrate
the [Jitescript](https://github.com/qmx/jitescript) library.

## Usage

```
$ mvn clean compile
$ mvn exec:java -Dexec.mainClass="Compiler" -Dexec.args="> + + + + + + [ - s + + + + + + + s ] < p"
$ java Main
42
```
