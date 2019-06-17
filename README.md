# Morph-csv
## How to enable OBDA query-translation over real CSVs?

Use CSVW annotations and RML FnO mappings (following YARRRML spec) to generate R2RML mappings and an enriched RDB to enable OBDA query translation over real CSV files. This framework can be embedded in the top of any R2RML-compliant engine.

- Docker image: https://hub.docker.com/r/dchaves1/morph-csv

### How it works?
![Morph-csv workflow](figures/morphcsv.png?raw=true "Morph-CSV workflow")

## How to run it?
Using the example of our last tutorial at ESWC2019 - Virtual Knowledge Graph Generation (https://tutorials.oeg-upm.net/vkg2019/)
```bash
git clone https://github.com/oeg-upm/vkg-tutorial-eswc2019
cd vkg-tutorial-eswc2019/morph-csv
docker-compose up -d
cd run-scripts
./run-XXX
```


## Examples:
At the evaluation folder you find original data, mappings, queries and results of 4 examples:
- Comments and persons (at motivating-example folder)
- Linking Open City data (at open-city-data-validation folder)
- Virtual Bio2RDF (at bio2rdf folder)
- Performance over GTFS transport data (at transport-performance folder)