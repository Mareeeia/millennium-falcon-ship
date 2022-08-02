# millennium-falcon-ship

# Readme

To build the project, run:

```
gradle build
```

To run, use the new .jar:

```
java -jar build/libs/millennium-falcon-1.0-SNAPSHOT.jar
```

With this you can use the console (R2D2), or open https://localhost:8080 and use the frontend.

Now, as per challenge instructions, you will need the paths to the empire, falcon and DB files. Make sure your
falcon file contains absolute path to the DB under the `"routes_db"` key.
If the value of `"routes_db"` is "DEFAULT", it will use the db in the resources folder.
Then to get odds you can do:

```
give-me-the-odds -F /path/to/falcon_config.json -E /path/to/empire.json
```

E.g. from the millennium-falcon project root you can run:

```
give-me-the-odds -F src/main/resources/falcon_config/falcon_initial_config.json -E src/main/resources/empire/basic_empire.json
```

To use the frontend, replace the `resources/falcon_initial_config.json` with your falcon config and upload your empire
file to the program.

## General architecture

`DijkstraShortestPath` - class used as a heuristic to evaluate the best case scenario for arrival to the destination
from every planet. This lets us know which routes to exclude if they aren't viable.

`OnboardComputer` - main business logic. Simulates the ship movement.

`PlanetMapper` - turns the object we got from UniverseMap into a graph-like structure of planets.

`UploadController` - code used for uploading empire intercepts, works with the css and html files in the resources.

`FileUtil` and `DBUtil` - util classes for acquiring data from json/db.

`model` package - contains various model/data classes.

`cli` package - contains the logic for the CLI.