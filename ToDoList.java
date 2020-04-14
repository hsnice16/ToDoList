
package javaproject;

import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;      //package for formatting of date and time
import java.time.LocalDate;                 //package to get local date
import javaproject.ValidDate;

class Date
{
    private static int day;
    private static int month;
    private static int year;

    public static String insertDate(Scanner sc)
    {
        System.out.println("\nWrite date to which you want to make list :");
        System.out.print(" day(dd): ");
        day = sc.nextInt();
        System.out.print(" month(mm): ");
        month = sc.nextInt();
        System.out.print(" year(yyyy): ");
        year  = sc.nextInt() ;

        if( !(ValidDate.isValid(day, month, year)) )    //check's Date is valid or not
        {
            System.out.println("date entered by you is not correct.");
            System.exit(101);
        }
        return (day +"/"+month+"/" + year);
    }// insertDate()

    public static String giveLocalDate()     // returns Today's Date
    {
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate now =  LocalDate.now();   // it gives today's date

        return (now.format(dtf)) ;
    }

}//class Date

public class ToDoList implements Serializable
{
    private String date;
    private ArrayList<String> text = new ArrayList<String>();   //ArrayList to store task.
    private static ArrayList<ToDoList> myList = new ArrayList<ToDoList>();  //ArrayList to store object.

    public static void main(String[] args) {

        int option; //to store the number of choice

        // Scanner object
        Scanner sc = new Scanner(System.in);

        //menu
        System.out.println("ToDoList APPLICATION.");
        System.out.println("1.) Make a new List of other Date.");
        System.out.println("2.) Make a new List of Today's Date.");
        System.out.println("3.) Show Today's List.");
        System.out.println("4.) Show List of other Date.");
        System.out.println("5.) Show All Lists.");
        System.out.println("6.) Remove the Today's List.");
        System.out.println("7.) Remove other date List.");

        System.out.print("Write the number what you want: ");
        option = sc.nextInt();

        // ToDoList object
        ToDoList obj = new ToDoList();

        switch (option) {
            case 1:
                obj.readData(sc,option);
                obj.addList();
                break;

            case 2:
                obj.readData(sc,option);
                obj.addList();
                break;

            case 3: 
                obj.DateList(sc,option);
                break;
            
            case 4:
                obj.DateList(sc,option);
                break;

            case 5:
                obj.AllLists();
                break;
            
            case 6:
                obj.deleteList(sc, option);
                break;

            case 7:
                obj.deleteList(sc, option);
                break;

            default:
                System.out.println("Please choose the available options.");
                break;
        }// switch loop
    }// main loop

    private void readData(Scanner sc , int option)
    {
        System.out.println("\nWrite save on next line to make a new list.");

        if(option == 1)         //will call when user Make a new List of other Date.
        {
            this.date = Date.insertDate(sc);   // of which date user wants to make list
        }
        else        //will call when user Make a list of today's date
        {
            this.date = Date.giveLocalDate();
        }
        
        String str ;
        
        System.out.println("Start text :- ");

        while( !(str = sc.nextLine()).equals("save"))
        {
            this.text.add(str);
        }
    }// readData ends

    private String giveDate()
    {
        return (this.date);
    }

    private void writeData()
    {
        System.out.println("Your List: ");
        for(int i = 0 ; i < text.size() ;i++)
        {
            System.out.println(text.get(i));
        }// for loop
    }//writeData ends

    private void addList()
    {
        myList.add(this);  // this adds the new list in array list

        File file = new File("ListContainer.txt");

        if(file.length() != 0 )
        {
            // first we add all the previous list present in the ListContainer file in the array list
            try(FileInputStream fin = new FileInputStream("ListContainer.txt");
                ObjectInputStream readObj = new ObjectInputStream(fin))
            {
                ToDoList tempObj ;
                while(fin.available() != 0)     // available() returns number of byte available
                {
                    tempObj = (ToDoList)readObj.readObject();
                    myList.add(tempObj);
                }

            }catch(IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        // then we will write all the list including the new one that's present in the array list in ListContainer file
        try(ObjectOutputStream writeObj = new ObjectOutputStream(new FileOutputStream("ListContainer.txt")))
        {
            for(int i = 0 ; i < myList.size() ; i++)
            {
                writeObj.writeObject(myList.get(i));
            }

        }catch(IOException  e)
        {
            System.out.println("File not created.");
        }
    }// addList meth() ends

    private void DateList(Scanner sc, int option)
    {
        String date ;
        if(option == 3)         //will call when user wants to see today's date list
        {
            date = Date.giveLocalDate();
        }
        else        //will call when user wants to see some another date list
        {
            date = Date.insertDate(sc);
        }
        
        boolean DatePresent = false;

        try(FileInputStream fin = new FileInputStream("ListContainer.txt");
            ObjectInputStream readObj = new ObjectInputStream(fin))
        {
            ToDoList tempObj;
            while(fin.available() != 0)
            {
                tempObj = (ToDoList)readObj.readObject();
                if( (tempObj.giveDate()).compareTo(date) == 0) 
                {
                    tempObj.writeData();
                    DatePresent = true;
                }
            }

            if(!DatePresent)
            {
                System.out.println("No List Found.");
            }
        }catch(IOException | ClassNotFoundException e)
        {
            System.out.println("File not Found.");
        }// try-catch block
    }//DateList meth()

    private void AllLists()
    {
        myList.clear(); //it clears the arrayList

        try(FileInputStream fin = new FileInputStream("ListContainer.txt");
            ObjectInputStream readObj = new ObjectInputStream(fin))
        {
            if(fin.available() == 0)
            {
                System.out.println("No List present.");
            }
            else
            {
                ToDoList tempObj;
                while(fin.available() != 0)
                {
                    tempObj = (ToDoList)readObj.readObject();
                    System.out.println("\nDate : "+tempObj.giveDate());
                    tempObj.writeData();
                }
            }

        }catch(IOException | ClassNotFoundException e)
        {
            System.out.println("File not Found.");
        }// try-catch block
    }// AllLists()


    private void deleteList(Scanner sc , int option)
    {
        String date;
        boolean DatePresent = true;

        if(option == 6)     //to delete today's list
        {
            date = Date.giveLocalDate();
        }
        else    //to delete other's date list
        {
            date = Date.insertDate(sc) ;
        }

        try(FileInputStream fin = new FileInputStream("ListContainer.txt");
            ObjectInputStream readObj = new ObjectInputStream(fin))
        {
            ToDoList tempObj ;
            while(fin.available() != 0)     // available() returns number of byte available
            {
                tempObj = (ToDoList)readObj.readObject();
                myList.add(tempObj);            //it adds all the list in file
            }

            for(int index = 0 ; index < myList.size() ; index++)
            {
                if( ((myList.get(index)).giveDate()).equals(date) )
                {
                    myList.remove(index) ;          // it delete's the list.
                    DatePresent = false;
                    index--;        //helps if two same list of same date present
                }
            }

            // writes the modified arraylist
            try(ObjectOutputStream writeObj = new ObjectOutputStream(new FileOutputStream("ListContainer.txt")))
            {
                for(int i = 0 ; i < myList.size() ; i++)
                {
                    writeObj.writeObject(myList.get(i));
                }
            }

            if(DatePresent)
            {
                System.out.println("No list found of this Date.");
            }
            else
            {
                System.out.println("List deleted.");
            }
        }catch(IOException | ClassNotFoundException e)
        {
            System.out.println("File not Found.");
        }
    }

}//class ToDoList