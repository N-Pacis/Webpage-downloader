package Helpers;

public class WarningMessageLogger {
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";

    public void log(String message){
        System.out.println(YELLOW+message+RESET);
    }
}
