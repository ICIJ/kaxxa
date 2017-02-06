# ICIJ Kaxxa

A suite of libraries for dealing with concurrency, parallelization and data streams.

## Releasing

First, release a snapshot version to staging:

```bash
mvn clean deploy
```

After inspecting the snapshot, release to the Central Repository:

```bash
mvn nexus-staging:release
```
