import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Client extends Application {
    // IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
//Defining the Name text field
        TextField tfWeight = new TextField();
        tfWeight.setPromptText("Enter your Weight: ");
        tfWeight.setPrefColumnCount(10);
        tfWeight.getText();
        GridPane.setConstraints(tfWeight, 0, 0);
        grid.getChildren().add(tfWeight);
//Defining the Last Name text field
        TextField tfHeight = new TextField();
        tfHeight.setPromptText("Enter your Height: ");
        GridPane.setConstraints(tfHeight, 0, 1);
        grid.getChildren().add(tfHeight);

//Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);

        BorderPane mainPane = new BorderPane();
        // Text area to display contents
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(grid);

        // Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Client"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage


        submit.setOnAction((ActionEvent e) -> {
            try {
                // Get the tfWeight from the text field
                double weight = Double.parseDouble(tfWeight.getText().trim());
                double height = Double.parseDouble(tfHeight.getText().trim());

                // Send the tfWeight to the server
                toServer.writeDouble(weight);
                toServer.writeDouble(height);
                toServer.flush();


                // Get bmi from the server
                double bmi = fromServer.readDouble();

                // Display to the text bmi
                ta.appendText("Weight is " + weight + "\n");
                ta.appendText("Height is " + height + "\n");
                ta.appendText("BMI received from the server is "
                        + bmi + '\n');
            }
            catch (IOException ex) {
                System.err.println(ex);
            }
        });

        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);
            // Socket socket = new Socket("130.254.204.36", 8000);
            // Socket socket = new Socket("drake.Armstrong.edu", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            ta.appendText(ex.toString() + '\n');
        }
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}