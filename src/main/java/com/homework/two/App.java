package com.homework.two;

import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        Scanner sc = new Scanner(System.in);
        HomeworkTwo h = new HomeworkTwo();
        h.initialPrompt();

        while (true) {
            System.out.println("\n");
            System.out.println("===================\n");
            System.out.println("a.) New Table");
            System.out.println("b.) Edit Table");
            System.out.println("c.) Search Table");
            System.out.println("d.) Print Table");
            System.out.println("e.) Sort Table");
            System.out.println("f.) Add Column");
            System.out.println("g.) Exit\n");
            System.out.print("Input: ");

            String choice = sc.next();

            switch (choice) {
                case ("a"):
                    h.initializeTable();
                    break;
                case ("b"):
                    h.editTable(h.getTableList());
                    break;
                case ("c"):
                    h.searchTable(h.getTableList());
                    break;
                case ("d"):
                    h.printTable(h.getTableList());
                    break;
                case ("e"):
                    h.sortTable(h.getTableList());
                    break;
                case ("f"):
                    h.addColumn(h.getTableList());
                    break;
                case ("g"):
                    h.endSession();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }//end while
    }
}

