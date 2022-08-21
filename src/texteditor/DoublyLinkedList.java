package texteditor;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

class node {  
    String data;
    node previous;
    node next;
    
    public node(String data) 
    {  
        this.data = data;  
    }
}  

public class DoublyLinkedList {
    // =============== Data Members ===============
    node head;
    node tail;
    int numOfLines;
    // ===============   Methods    ===============
    Scanner in = new Scanner(System.in);
    
    public DoublyLinkedList(){
        // ----- Data Members' Intialization -----
        head = null;
        tail = null;
        numOfLines = 0;
        
        // ----- Menu Section -----
        int choice = -1;
        while (choice != 8) {
            menu();
            choice = in.nextInt();
            System.out.println();
            handleChoice(choice);
	}
    }
    
    // ===============   Front-End Methods   ===============
    public void menu(){
        System.out.println("<-<-<-<-<-<-<-<-<-< TEXT EDITOR >->->->->->->->->->");
        System.out.println("Please choose what you want to do:\n   1. Insert text into Line N");
        System.out.println("   2. Delete line N\n   3. Replace text in Line N\n   4. Print all");
        System.out.println("   5. How many characters in Line N.\n   6. Save into the file\n   7. Open a file\n   8. Exit");
        System.out.print("Your Choice ==> ");
    }
    
    public void handleChoice(int choice){
        switch (choice) {
            // ******************* Insertion a new line *******************
            case 1: {
                int lineGiven;
                System.out.print("Enter the Line Number you want the text to be placed into : ");
                lineGiven = in.nextInt();
                // 1. Invalid line number
                if (lineGiven == 0) {
                    System.out.println("\n!!!! There's no line 0, Did you mean 1 !!!!\n");
                }
                // 2. Valid line number
                else{
                    String dataGiven;
                    System.out.print("Enter text : ");
                    in.nextLine(); // To clear input buffer.
                    dataGiven = in.nextLine();
                    dataGiven += "\n"; // Next String will be iserted in a new line.

                    // Case 1: Insert First
                    if (lineGiven == 1) {
                        addToHead(dataGiven);
                    }
                    // Case 2: Insert Last
                    else if (lineGiven > numOfLines) {
                        insertFurtherAway(dataGiven, lineGiven);
                        System.out.println("Done :)\n");
                    }
                    // Case 3: Insert Between Lines
                    else if (lineGiven < numOfLines) {
                        insertTextInBetween(dataGiven, lineGiven);
                    }
                    // Case 4: Insert Before Last
                    else if (lineGiven == numOfLines) {
                        int selection;
                        System.out.println("Enter 1 to replace the last line, enter 2 to insert a new line");
                        selection = in.nextInt();
                        if (selection == 1) {
                            replaceTextInLine(dataGiven, lineGiven);
                        }
                        else if (selection == 2) {
                            addBeforeTail(dataGiven);
                        }
                    }
                }}
                break;
            // ******************* Deletion of a line *******************
            case 2:
                if(isEmpty())
                    System.out.println("The linked List is Already Empty\n");
                else{
                    int lineGiven;
                    System.out.print("Enter the line you want to delete : ");
                    lineGiven = in.nextInt();
                    deleteLine(lineGiven);
                }
                break;
            // ******************* Replace text in Line N *******************
            case 3:
                if(isEmpty())
                    System.out.println("You can't replace any line, The linked List is Empty\n");
                else{
                    int lineGiven;
                    System.out.print("Enter line Number you want to change the content of : ");
                    lineGiven = in.nextInt();
                    
                    // First I will check if the line number is valid or not ??
                    if (lineGiven > numOfLines) { // Not Valid
                        System.out.println("The line you entered exceeds the existing number of lines\n");
                    } 
                    else { // Valid
                        String dataGiven;
                        System.out.print("Enter the new text : ");
                        in.nextLine(); // To clear input buffer.
                        dataGiven = in.nextLine();
                        dataGiven += "\n";
                        replaceTextInLine(dataGiven, lineGiven);
                    }
                }
                break;
            // ******************* Printing the whole list *******************
            case 4:
                printall();
                break;
            // *******************  How many characters ?  *******************
            case 5:
                if(isEmpty())
                    System.out.println("You can't, The linked List Empty\n");
                else{
                    int lineGiven;
                    System.out.print("Enter the line Number you want to Count its characters : ");
                    lineGiven = in.nextInt();
                    numOfCharInLine(lineGiven);
                }
                break;
            // ******************* Saving the list into a txt file *******************
            case 6:
                saveAll();
                break;
            // *******************   Read a file in Linked list.   *******************
            case 7:
                readFile();
                break;
            case 8:
                break;
            default:
                System.out.println("!!!! Invalid Choice !!!!\n");
        }
    }
    
    public void printall(){  // Function to print the whole linked list
        if (isEmpty()) {
            System.out.println("!! There Are No Elements Yet !!\n");
        } 
        else {
            node cur = head;
            int linePrinted = 1;
            while (cur != null) {
                if (linePrinted == 1) {
                    System.out.println("------------------- Content -------------------");
                } 
                System.out.print(linePrinted + ") " + cur.data);
                cur = cur.next;
                linePrinted++;
            }
            System.out.println("## Number of Lines: " + numOfLines + " ##\n------------------- Content -------------------\n");
        }
    }
    
    // ===============   Back-End Methods    ===============
    public boolean isEmpty() {
        return head == null;
    }
    
    public void addToHead(String dataGiven) { // This function will add to Head
        node new_node = new node(dataGiven);
        if (isEmpty()){ // No node, empty linked list
            new_node.next = new_node.previous = null;
            head = tail = new_node;
        } 
        else{ // One or more than one node
            new_node.previous = null;
            new_node.next = head;
            head.previous = new_node;
            head = new_node;
        }
        numOfLines++;
        System.out.println("Done :)\n");
    }
    
    public void insertFurtherAway(String dataGiven, int lineGiven){  // Will print \n lines if given line is larger than numOfLines
        while (numOfLines < lineGiven - 1)
            addToTail("\n");
        addToTail(dataGiven);
        System.out.println("Done :)\n");
    }
    
    public void insertTextInBetween(String dataGiven, int lineGiven){  // This function will insert text in the given line,
                                                                       // And will re-arrange all the other lines' order
        if (lineGiven == 0) { // Invalid line number "More Surness"
            System.out.println("There's no line 0, Did you mean 1 !!!!\n");
        }
        else if (lineGiven == 1) { // Will be the first line "More Surness"
            addToHead(dataGiven);
            System.out.println("Done :)\n");
        }
        else { // Will insert the new node before the one has been chosen
            node new_node = new node(dataGiven);
            node current = head.next;
            int iterator = 2;
            while (current.next != null) {
                if(iterator == lineGiven)
                    break;
                current = current.next;
                iterator++;
            }
            new_node.previous = current.previous; // Previous of new node will point at the previous node.
            new_node.next = current;  // Next of new node will point at the current node.
            current.previous.next = new_node; // Previous node will point at the new one.
            current.previous = new_node; // Current node will point at the new one.
            numOfLines++;
            System.out.println("Done :)\n");
        }
    }
    
    public void replaceTextInLine(String dataGiven,int lineGiven){  // This function will overwrite anything written in the given line
	if(lineGiven == 0)
            System.out.println("There's no line 0, did you mean 1\n");
        else {
            // Case 1: Replace First
            if (lineGiven == 1) {
                this.head.data = head.data.replaceAll(head.data, dataGiven);
            }
            // Case 2: Replace Last
            else if (lineGiven == numOfLines) {
                this.tail.data = tail.data.replaceAll(tail.data, dataGiven);
            }
            // Case 3: Replace A Specific Line
            else {
                node current = this.head.next;
                int iterator = 2;
                while (current.next != null) {
                    if(iterator == lineGiven)
                        break;
                    current = current.next;
                    iterator++;
                }
                current.data = current.data.replaceAll(current.data, dataGiven);
            }
            System.out.println("Done :)\n");
        }
    }
    
    public void addToTail(String dataGiven){
        node new_node = new node(dataGiven);
        if (isEmpty()) // Empty linked list
        {
            new_node.next = new_node.previous = null;
            head = tail = new_node;
        }
        else // One or more than one node in linked list
        {
            new_node.next = null;
            new_node.previous = tail;
            tail.next = new_node;
            tail = new_node;
        }
        numOfLines++;
    }
    
    public void addBeforeTail(String dataGiven){
        node new_node = new node(dataGiven);
        new_node.previous = tail.previous;
        new_node.next = tail;
        tail.previous.next = new_node;
        tail.previous = new_node;
        numOfLines++;
        System.out.println("Done :)\n");
    }
    
    public void deleteLine(int lineGiven){  // This function should delete anything in the given line,
                                            // Also decreases the numOfLines by 1.
        // ================   Empty   ================
        if (isEmpty()) { 
            System.out.println("There is no line to be deleted/removed.\n");
        }
        // ================ Not Empty ================
        else{
            // <<<<<<<<<<<<<<<< Invalid Line Number >>>>>>>>>>>>>>>>>>
            if(lineGiven == 0){
                System.out.println("!!!! There's no line 0, did you mean 1 !!!!\n");	         
            }
            else if (lineGiven > numOfLines) {
                System.out.println("!!!! Entered line is larger than existing lines !!!!\n");
            }
            // <<<<<<<<<<<<<<<<  Valid Line Number  >>>>>>>>>>>>>>>>>>
            else{
                // Case 1: Delete First
                if (lineGiven == 1) {
                    deleteHead();
                }
                // Case 2: Delete Last
                else if (lineGiven == numOfLines) {
                    deleteLast();
                }
                // Case 3: Delete Specific Node
                else if (lineGiven < numOfLines) {
                    deleteNode(lineGiven);
                }
                System.out.println("Done :)\n");
            }
        }
    }
    
    public void deleteHead(){
        if(isEmpty()){ // Empty
            System.out.println("Liked List Is Already Empty");
            return;
        }
        else if (numOfLines == 1) // Only one node exist
        {
            head = tail = null;
        } 
        else // Not Empty
        {
            head.next.previous = null;
            head = head.next;
        }
        numOfLines--;
    }
    
    public void deleteLast(){
        if(isEmpty()){ // Empty
            System.out.println("Liked List Is Already Empty");
            return;
        }
        else if (numOfLines == 1) // Only one node exist
        {
            head = tail = null;
        }
        else // Not Empty
        {
            tail.previous.next = null;
            tail = tail.previous;
        }
        numOfLines--;
    }
    
    public void deleteNode(int lineGiven){
        node current = head.next;
        int iterator = 2;
        while (current.next != null) {
            if (iterator == lineGiven) {
                break;
            }
            current = current.next;
            iterator++;
        }
        current.previous.next = current.next; // Making the previous node poiting at the next one.
        current.next.previous = current.previous; // Making the next node pointing at the previous one.
        current.next = current.previous = null;
        numOfLines--;
    }
    
    public void numOfCharInLine(int lineGiven){	// Counting Line's Characters.
        // ================ Invalid Line Number ================
        if(lineGiven == 0)
            System.out.println("There's no line 0, did you mean 1\n");
        else if (lineGiven > numOfLines)
            System.out.println("Entered line is larger than existing lines...\n");
        
        // ================  valid Line Number  ================
        else{
            // Case 1: Count First Line
            if (lineGiven == 1) {
                int count = 0;
                for(int i = 0; i < head.data.length(); ++i){
                    if(head.data.charAt(i) == ' ')
                        continue;
                    count++;
                }
                System.out.println("Number of characters: " + --count + "\n");
            }
            // Case 2: Count Last Line
            else if (lineGiven == numOfLines) {
                int count = 0;
                for(int i = 0; i < tail.data.length(); ++i){
                    if(tail.data.charAt(i) == ' ')
                        continue;
                    count++;
                }
                System.out.println("Number of characters: " + --count + "\n");
            }
            // Case 3: Count A Specific Line
            else {
                // 1. Specifying the line to count
                node current = head.next;
                int iterator = 2;
                while (current.next != null) {
                    if(iterator == lineGiven)
                        break;
                    current = current.next;
                    iterator++;
                }
                // 2. Then count it.
                int count = 0;
                for(int i = 0; i < current.data.length(); ++i){
                    if(current.data.charAt(i) == ' ')
                        continue;
                    count++;
                }
                System.out.println("Number of characters: " + --count + "\n");
            }
        }
    }

    // ===============   Files Methods    ===============
    public void saveAll(){
        if(isEmpty())
            System.out.println("!! Nothing to save, Linked list is empty !!");
        else{
            String filePath = "src\\texteditor\\test.txt";
            try{
                // 1. Clearing file before inserting the data into it.
                File file = new File(filePath);
                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();

                // 2. Inserting the data into it.
                node temp = head;
                FileWriter file1 = new FileWriter(filePath, true);
                while (temp != null) {
                    file1.write(temp.data);
                    temp = temp.next;
                }
                file1.close();
                JOptionPane.showMessageDialog(null, "Saved Successfully");
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, e);
            } 
        }
    }
    
    public void readFile(){
        // 1. Get The data from the file
        ArrayList<String> data = new ArrayList<String>();
        String[] info = null;
        File file = new File("src\\texteditor\\test.txt");
        try{
            Scanner in = new Scanner(file);
            while(in.hasNext()){
                data.add(in.nextLine());
            }
            info = data.toArray(new String[0]);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
        // 2. Pushing the data into the linked list.
        for(int i = 0; i<info.length; ++i){
            info[i] += "\n";
            addToTail(info[i]);
        }
        if(isEmpty())
            System.out.println("!! File has no data to read !!");
        else
            JOptionPane.showMessageDialog(null, "File Read Successfully");
    }
    
}
