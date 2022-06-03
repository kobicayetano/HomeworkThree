package com.homework.two;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import com.homework.two.InvalidUserInputException;
import org.apache.commons.lang3.StringUtils;

public class HomeworkTwo {

    private List<List<String>> tableList = new ArrayList<>();
    //set default file name if no file path provided
    private String fileName = "myFile.txt";

    public void setTableList(List<List<String>> tableList) {
        this.tableList = tableList;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<List<String>> getTableList() {
        return this.tableList;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void editTable(List<List<String>> tableList) {

        Scanner scanner = new Scanner(System.in);
        String[] indexData;
        String newValue;
        String option;
        int tableRow, tableColumn;
        try {
            System.out.println("\n===================");
            System.out.println("Edit Table");
            System.out.println("Array index: ");
            System.out.print("x: ");
            tableRow = scanner.nextInt();
            System.out.print("y: ");
            tableColumn = scanner.nextInt();
            System.out.format("User Input: Index %dx%d\n", tableRow, tableColumn);
            System.out.println("Key or Value?");
            System.out.print("Input: ");
            option = StringUtils.upperCase(scanner.next());//sc.next().toUpperCase();
            System.out.print("New Value: ");
            newValue = scanner.next();
            indexData = StringUtils.split(tableList.get(tableRow).get(tableColumn), "="); //tableList.get(tableRow).get(tableColumn).split("=");

            switch (option) {
                case "KEY":
                    indexData[0] = newValue;
                    tableList.get(tableRow).set(tableColumn, indexData[0] + "=" + indexData[1]);
                    System.out.println("Edit successful.");
                    saveToFile(getFileName(), getTableList());
                    break;
                case "VALUE":
                    indexData[1] = newValue;
                    tableList.get(tableRow).set(tableColumn, indexData[0] + "=" + indexData[1]);
                    System.out.println("Edit successful.");
                    saveToFile(getFileName(), getTableList());
                    break;
                default:
                    System.out.println("Please choose Key or Value only.");
                    System.out.println("No changes made.");
                    break;
            }

        } catch (IndexOutOfBoundsException IOBE) {
            System.out.println("Invalid Table Index.");
            System.out.println("No changes made.");
        } catch (InputMismatchException IME) {
            System.out.println("Check your input.");
            System.out.println("No changes made.");
        }
    }

    //Loads the table from an existing path
    public void loadTableFromFile(String path) {
        try {
            Scanner reader = new Scanner(new File(path));
            List<List<String>> Table = new ArrayList<>();
            List<String> rowInTable;


            while (reader.hasNextLine()) {
                rowInTable = new ArrayList<>();
                String currentLine = StringUtils.trim(reader.nextLine());
                String[] lineData = StringUtils.split(currentLine,",");//reader.nextLine().trim().split(",");
                for (int j = 0; j < lineData.length; j++) {
                    rowInTable.add(lineData[j]);
                }
                Table.add(rowInTable);
            }

            setTableList(Table);
            setFileName(path);
        } catch (FileNotFoundException FNFE) {
            System.out.println("File not found.");
            System.out.println("Create new array.");
            initializeTable();
        } catch (Exception e) {
            System.out.println("An error occured.");
            initializeTable();
        }

    }

    //Starts the application by loading/creating new table
    public void initialPrompt() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 'y/Y' to read file.");
        System.out.println("Enter any key to create new table.");
        System.out.print("Input: ");
        String choice = scanner.next();
        if (StringUtils.equals(StringUtils.upperCase(choice),"Y")) {
            System.out.print("File path: ");
            String path = scanner.next();
            loadTableFromFile(path);
        } else {
            initializeTable();
        }
    }

    //Prints the contents of the table
    public void printTable(List<List<String>> table) {
        
        for (List<String> rowInTable : table) {
            for (String data : rowInTable) {
                System.out.print(StringUtils.replace(data,"=", ",") + "   ");
            }
            System.out.println("");
        }
    }
    
   
    //Creates and populates a table  of NxM size
    public void initializeTable() {

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        List<String> rowInTable;
        List<List<String>> table = new ArrayList<>();
        String generatedKey = "";
        String generatedValue = "";
        int tableRow;
        int tableColumn;
        final String characterPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+?/.><[]";

        System.out.println("\n===================");
        System.out.println("Reset Table");

        try {
            //ask user for size of new array
            System.out.println("Array size: ");
            System.out.print("x: ");
            tableRow = scanner.nextInt();
            System.out.print("y: ");
            tableColumn = scanner.nextInt();
            System.out.format("User Input: %dx%d\n", tableRow, tableColumn);
            checkZeroInput(tableRow);
            checkZeroInput(tableColumn);

            //populate table
            for (int i = 0; i < tableRow; i++) {
                rowInTable = new ArrayList<>();
                for (int j = 0; j < tableColumn; j++) {
                    for (int k = 0; k < 3; k++) {
                        generatedKey += characterPool.charAt(random.nextInt(characterPool.length()));
                        generatedValue += characterPool.charAt(random.nextInt(characterPool.length()));
                    }
                    rowInTable.add(generatedKey + "=" + generatedValue);
                    generatedKey = "";
                    generatedValue = "";
                }
                table.add(rowInTable);
            }
            System.out.println("Table Created.");
            setTableList(table);
            saveToFile(getFileName(), getTableList());
        } catch (InvalidUserInputException e) {
            System.out.println("Check your input.");
            initializeTable();
        } catch (InputMismatchException IME) {
            System.out.println("Check your input.");
            initializeTable();
        }
    }

    //Check input for possible exception
    public void checkZeroInput(int n) throws InvalidUserInputException {
        if (n <= 0) {
            throw new InvalidUserInputException("Invalid input.");
        }
    }

    //Saves table to a text file
    public void saveToFile(String filename, List<List<String>> table) {
        try {
            List<String> rowInTable;
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < table.size(); i++) {
                rowInTable = new ArrayList<>();
                rowInTable = table.get(i);
                for (int j = 0; j < rowInTable.size(); j++) {
                    if (j == rowInTable.size() - 1) {
                        outputWriter.write(rowInTable.get(j));
                    } else {
                        outputWriter.write(rowInTable.get(j) + ",");
                    }
                }
                outputWriter.newLine();
            }
            outputWriter.flush();
            outputWriter.close();
            System.out.println("File upated.");
        } catch (IOException IOE) {
            System.out.println("Error saving to file.");
        }
    }

    public void searchTable(List<List<String>> table) {
        //search a string in the array
        Scanner scanner = new Scanner(System.in);
        String stringToFind;
        int tableRow = table.size();
        int tableColumn;
        int occuranceInKey;
        int fromIndexOfKey;
        int occuranceInValue;
        int fromIndexOfValue;
        System.out.println("\n===================");
        System.out.println("Search Table");
        try {
            System.out.print("Search: ");
            stringToFind = scanner.next();
            for (int i = 0; i < tableRow; i++) {
                tableColumn = table.get(i).size();
                for (int j = 0; j < tableColumn; j++) {
                    //drives inner loop for arr
                    String currentData = table.get(i).get(j);
                    String[] stringToSearch = StringUtils.split(currentData,"=");//result.split("=");
                    occuranceInKey = 0;
                    fromIndexOfKey = 0;
                    occuranceInValue = 0;
                    fromIndexOfValue = 0;
                    //return -1 if value cannot  be found (Loop stop mechanism) 
                    //search in KEY
                    while ((fromIndexOfKey = StringUtils.indexOf(stringToSearch[0],stringToFind, fromIndexOfKey)) != -1) {
                        occuranceInKey++;
                        fromIndexOfKey++;
                    }
                    if (occuranceInKey > 0) {
                        System.out.format("Output: [%d,%d] - %d Occurance in Key field\n", i, j, occuranceInKey);
                    }
                    //return -1 if value cannot  be found (Loop stop mechanism)
                    //search in VALUE
                    while ((fromIndexOfValue = StringUtils.indexOf(stringToSearch[1],stringToFind, fromIndexOfValue)) != -1) {
                        occuranceInValue++;
                        fromIndexOfValue++;
                    }
                    if (occuranceInValue > 0) {
                        System.out.format("Output: [%d,%d] - %d Occurance in Value field\n", i, j, occuranceInValue);
                    }
                }//end  for loop
            }
        } catch (ArrayIndexOutOfBoundsException AIOBE) {
            System.out.println("Invalid Table Index.");
        } catch (IndexOutOfBoundsException AIOBE) {
            System.out.println("Invalid Table Index.");
        }
    }

    //sorts table rows in ascending/descending order
    public void sortTable(List<List<String>> table) {

        System.out.println("\n===================");
        System.out.println("Sort Array:");
        System.out.println("A=Ascending D=Descending");
        System.out.print("Input: ");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.next();
        try {
            switch (StringUtils.upperCase(option)) {
                case "A":
                    for (List<String> rowInTable : table) {
                        Collections.sort(rowInTable);
                    }
                    saveToFile(getFileName(), getTableList());
                    System.out.println("Table sorted in ascending order.");
                    break;
                case "D":
                    for (List<String> rowInTable : table) {
                        Collections.sort(rowInTable, Collections.reverseOrder());
                    }
                    System.out.println("Table sorted in descending order.");
                    saveToFile(getFileName(), getTableList());
                    break;
                default:
                    System.out.println("Please choose A or D only.");
                    System.out.println("No changes made.");
                    break;
            }
        } catch (Exception E) {
            System.out.println("An error occured.");
            System.out.println("No changes made.");
        }

    }

    //Adds a column to a specified row
    public void addColumn(List<List<String>> table) {

        Scanner scanner = new Scanner(System.in);
        String value;
        String key;
        int rowToAdd;

        System.out.println("\n===================");
        System.out.println("Add to Table");
        System.out.print("Add in row: ");
        rowToAdd = scanner.nextInt();
        System.out.print("Key: ");
        key = scanner.next();
        System.out.print("Value: ");
        value = scanner.next();

        try {
            table.get(rowToAdd).add(key + "=" + value);
            System.out.println("Table Updated.");
            saveToFile(getFileName(), getTableList());

        } catch (NullPointerException NPE) {
            System.out.println("An error occured.");
            System.out.println("No changes made.");
        } catch (IndexOutOfBoundsException IOBE) {
            System.out.println("Input exceeds table size.");
            System.out.println("No changes made.");
        }

    }

    //Stops process
    public void endSession() {
        System.out.println("Process terminated.");
        System.exit(0);
        
    }

}
