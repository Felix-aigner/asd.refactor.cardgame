# Sockn


### Run with Maven (IntelliJ)
To run the project, you have to install the Maven dependencies and open the Maven Projects Window in IntelliJ. Click on `Sockn -> Plugins -> javafx -> javafx:compile` to compile the project, and click on `Sockn -> Plugins -> javafx -> javafx:run` to execute it.  

### Run with IntelliJ
To execute the project using the running configuration of IntelliJ, you have to [download JavaFX](https://gluonhq.com/products/javafx/) to your local machine. Once you downloaded JavaFX, place it somewhere where it's accessible. Next, open the running configuration and add `--module-path /<PATH_TO_FX>/javafx-sdk-13.0.1/lib --add-modules javafx.base,javafx.controls,javafx.media,javafx.fxml` to the VM options.

### Run with Terminal (Linux/Mac)
To execute the project using the Terminal, you have to [download JavaFX](https://gluonhq.com/products/javafx/) to your local machine. Once you downloaded JavaFX, use `export PATH_TO_FX=path/to/javafx-sdk-13/lib` to add an environment variable pointing to the lib directory of the runtime.
Now use `javac --module-path $PATH_TO_FX --add-modules javafx.base,javafx.controls,javafx.media,javafx.fxml Main.java` to compile the application. Next, run the application using `java --module-path $PATH_TO_FX --add-modules javafx.base,javafx.controls,javafx.media,javafx.fxml Main`.


You can now compile and run JavaFX applications. Enjoy! 