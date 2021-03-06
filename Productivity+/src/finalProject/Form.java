package finalProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * The java file will get inputs from the set schedule page and sends information
 * to other java files
 *
 * Form.fxml file creates the necessary objects to display on GUI page
 */

public class Form extends VBox {
    @FXML private ComboBox nameSelector;
    @FXML private ComboBox startHourSelector;
    @FXML private ComboBox startMinuteSelector;
    @FXML private ComboBox startPeriodSelector;
    @FXML private ComboBox endHourSelector;
    @FXML private ComboBox endMinuteSelector;
    @FXML private ComboBox endPeriodSelector;
    private Controller controller;

    public Form(Controller controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Form.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.controller = controller;

        // Sets all of the values in the combo boxes
        startHourSelector.setItems(createHours());
        startMinuteSelector.setItems(createMinutes());
        startPeriodSelector.setItems(FXCollections.observableArrayList("AM","PM"));

        endHourSelector.setItems(createHours());
        endMinuteSelector.setItems(createMinutes());
        endPeriodSelector.setItems(FXCollections.observableArrayList("AM","PM"));
    }

    // Makes sure all of the form is filled out before allowing the user to create a task
    private boolean isComplete() {
        boolean nameComplete = !isNull(getName()) && !getName().isEmpty();
        boolean startComplete = !isNull(getStartHour()) && !isNull(getStartMinute()) && !isNull(getStartPeriod());
        boolean endComplete = !isNull(getEndHour()) && !isNull(getEndMinute()) && !isNull(getEndPeriod());
        return nameComplete && startComplete && endComplete;
    }

    // Add a new task name to the drop down menu of tasks previously created
    public void updateNameSelector(String name) {
        nameSelector.getItems().add(name);
    }

    // Gets the input of the form in the desired format unless there were user input errors
    public TaskOccurrence getContents() throws FormNotCompleteException, InvertedTimelineException {
        if (!isComplete()) {
            throw new FormNotCompleteException("Please fill out everything.");
        }
        String name = getName().toUpperCase();
        Date start = getStart();
        Date end = getEnd();
        // Makes sure the user doesn't want the task to end before it starts
        if (end.before(start)) {
            throw new InvertedTimelineException("End date occurs before start date.");
        }
        TaskOccurrence contents = new TaskOccurrence(name, start, end);
        return contents;
    }

    private Date getStart() {
        Date start = convertToDate(getStartHour(), getStartMinute(), getStartPeriod());
        return start;
    }

    private Date getEnd() {
        Date end = convertToDate(getEndHour(), getEndMinute(), getEndPeriod());
        return end;
    }

    // Called with the user wants to add a task to their ideal schedule
    @FXML
    protected void addOccurrenceClicked() {
        controller.tryAddScheduleOccurrence();
    }

    // Called when the user is done making their schedule and wants to use it for the day
    @FXML
    protected void useScheduleClicked() {
        controller.useSchedule();
    }

    /***** THE FOLLOWING 7 METHODS GET DATA FROM THE INPUT FIELDS *****/
    private String getName() {
        return (String) nameSelector.getValue();
    }

    private String getStartHour() {
        return (String) startHourSelector.getValue();
    }

    private String getStartMinute() {
        return (String) startMinuteSelector.getValue();
    }

    private String getStartPeriod() {
        return (String) startPeriodSelector.getValue();
    }

    private String getEndHour() {
        return (String) endHourSelector.getValue();
    }

    private String getEndMinute() {
        return (String) endMinuteSelector.getValue();
    }

    private String getEndPeriod() {
        return (String) endPeriodSelector.getValue();
    }

    /***** STATIC METHOD THAT CLEANS UP TIME INPUTS BEFORE FURTHER USE *****/
    private static Date convertToDate(String hour, String minute, String period) {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DAY_OF_MONTH);
        String fullDay = month + " " + day + " " + hour + ":" + minute + ":00 " + period + " " + year;
        DateFormat formatter = new SimpleDateFormat("MM dd hh:mm:ss a yyyy");
        Date date = null;
        try {
            date = formatter.parse(fullDay);
        } catch (ParseException e) {
            System.out.println("This definitely should not happen: " + e.getMessage());
            System.exit(-1);
        }
        return date;
    }

    /***** THE FOLLOWING 2 STATIC METHODS CREATE WHAT GOES IN TO TIME FIELDS *****/
    private static ObservableList<String> createMinutes() {
        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i=0; i<60; i++) {
            String minute = String.format("%02d", i);
            minutes.add(minute);
        }
        return minutes;
    }

    private static ObservableList<String> createHours() {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 1; i < 13; i++) {
            String hour = Integer.toString(i);
            hours.add(hour);
        }
        return hours;
    }

    /***** SHORTHAND STATIC METHOD FOR CLEANER CODE *****/
    private static boolean isNull(Object o) {
        return (o == null);
    }

}
