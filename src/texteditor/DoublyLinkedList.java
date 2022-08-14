package texteditor;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Scanner;
import javax.swing.JOptionPane;
class undoCmd {
    int lineNumber;
    String text;
    int commandNumber;
    int mLine;
    int nLine;
};

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
    int numOfLines = 0;
    Stack<undoCmd> undoStack = new Stack<undoCmd>();
    // =============== Methods ===============
    Scanner in = new Scanner(System.in);
    
    public DoublyLinkedList(){
        head = null;
        tail = null;
        int choice = -1;
        int prevPagePrinted = 1;
        
        
        while (choice != 0) {
            System.out.println("<-<-<-<-<-<-<-<-<-< TEXT EDITOR >->->->->->->->->->");
            System.out.println("Please choose what you want to do:\n   1. Insert text into Line N");
            System.out.println("   2. Delete line N\n   3. Replace text in Line N");
            System.out.println("   4. Print all\n   5. How many characters in Line N.\n   6. Save into the file\n   7. Open a file");
            System.out.print("Your Choice ==> "); choice = in.nextInt();
            System.out.println();
            //-----------------------------------------------------------
            if (choice == 1) //Insertion a new line.
            {
                int lineGiven;
                String dataGiven;
                System.out.print("Enter line you want the text to be placed into : ");
                lineGiven = in.nextInt();
                System.out.print("Enter text : ");
                in.nextLine(); // To clear input buffer.
                dataGiven = in.nextLine();
                dataGiven += "\n";
                if (lineGiven == 1) {
                    addToHead(dataGiven);
                } 
                else if (lineGiven > numOfLines) {
                    insertFurtherAway(dataGiven, lineGiven);
                } 
                else if (lineGiven < numOfLines) {
                    insertTextInBetween(dataGiven, lineGiven);
                } 
                else if (lineGiven == numOfLines) {
                    int selection;
                    System.out.println("Enter 1 to replace the last line, enter 2 to insert a new line");
                    selection = in.nextInt();
                    if (selection == 1) {
                        replaceTextInLine(dataGiven, lineGiven);
                    } else if (selection == 2) {
                        addToTail(dataGiven);
                    }
                }
            }
            //-------------------------------------------------------
            else if (choice == 2) //Deletion of a line, any line.
            {
                if(isEmpty())
                    System.out.println("The linked List is Already Empty\n");
                else{
                    int lineGiven;
                    System.out.print("Enter the line you want to delete : ");
                    lineGiven = in.nextInt();
                    deleteLine(lineGiven);
                }
            }
            //-------------------------------------------------------
            else if (choice == 3) // Replace text in Line N
            {
                if(isEmpty())
                    System.out.println("The linked List is Already Empty\n");
                else{
                    int lineGiven;
                    String dataGiven;
                    System.out.print("Enter line you want to change the content of : ");
                    lineGiven = in.nextInt();
                    if (lineGiven > numOfLines) {
                        System.out.println("The line you entered exceeds the existing number of lines\n");
                    } 
                    else {
                        System.out.print("Enter the new text : ");
                        in.nextLine(); // To clear input buffer.
                        dataGiven = in.nextLine();
                        dataGiven += "\n";
                        replaceTextInLine(dataGiven, lineGiven);
                    }
                }
            }
            //-------------------------------------------------------
            else if (choice == 4)   //Printing the whole list, works fine
            {
                printall();
            }
            //-------------------------------------------------------
            else if (choice == 5)   //How many characters ?
            {
                if(isEmpty())
                    System.out.println("The linked List is Already Empty\n");
                else{
                    int lineGiven;
                    System.out.print("Enter the line you want to Count its characters : ");
                    lineGiven = in.nextInt();
                    numOfCharInLine(lineGiven);
                }
            }
            //-------------------------------------------------------
            else if (choice == 6)   //Saving the list into a txt file
            {
                saveAll();
            }
            //-------------------------------------------------------
            else if (choice == 7)   //Read a file in Linked list.
            {
                readFile();
            }
	}
    }
    
    public boolean isEmpty() {
        return head == null;
    }
    
    public void addToHead(String dataGiven) { //this function will add to Head
        if (isEmpty()){ //no node, empty linked list
            node new_node = new node(dataGiven);
            new_node.next = null;
            head = new_node;
            tail = head;
            numOfLines++;
        } 
        else{ //one or more than one node
            node new_node = new node(dataGiven);
            new_node.next = null;
            new_node.next = head;
            head = new_node;
            numOfLines++;
        }
        undoCmd adddedToHead = new undoCmd();
        adddedToHead.lineNumber = 1;
        adddedToHead.commandNumber = 1;
        undoStack.push(adddedToHead);
    }
    
    public void insertFurtherAway(String dataGiven, int lineGiven){  //will print /n lines if given line is larger than numOfLines
	undoCmd insertedFurtherAway = new undoCmd();
        insertedFurtherAway.lineNumber = 0;
        insertedFurtherAway.commandNumber = 9;
        if (head == null) {
            while (numOfLines < lineGiven - 1) {
                whateverAddToTail("\n");
                insertedFurtherAway.lineNumber++;
            }
            // insertedFurtherAway.lineNumber++;
            whateverAddToTail(dataGiven);
        } 
        else {
            while (numOfLines < lineGiven - 1) {
                whateverAddToTail("\n");
                insertedFurtherAway.lineNumber++;
            }
            whateverAddToTail(dataGiven);
        }
        undoStack.push(insertedFurtherAway);
    }
    
    public void whateverAddToTail(String dataGiven){	//an extra function used to add to tail, had to implement to make Undo function work, ignore this one please
        if (head == null) //no node, empty linked list
        {
            node temp = new node(dataGiven);
            temp.next = null;
            head = temp;
            tail = head;
            numOfLines++;
        } 
        else //one or more than one node
        {
            node temp = new node(dataGiven);
            temp.next = null;
            tail.next = temp;
            tail = temp;
            numOfLines++;
        }
    }
    
    public void insertTextInBetween(String dataGiven, int lineGiven){		//this function will insert text in the given line, and will push all the other lines
        if (lineGiven == 0) {
            System.out.println("There's no line 0, did you mean 1\n");
        }
        else if (lineGiven == 1) {
            if (head == null) //no node, empty linked list
            {
                node temp = new node(dataGiven);
                temp.next = null;
                head = temp;
                tail = head;
                numOfLines++;
            }
            else //one or more than one node
            {
                node temp = new node(dataGiven);
                temp.next = null;
                temp.next = null;
                temp.next = head;
                head = temp;
                numOfLines++;
            }
            //May be unnecessary, dunno
            undoCmd insertedToHead = new undoCmd();
            insertedToHead.lineNumber = 1;
            insertedToHead.commandNumber = 5;
            undoStack.push(insertedToHead);
            // addToHead(dataGiven);
            // numOfLines++;
        }
	else{
            node  prevNode = head;
            node  nextNode = head;
            node  temp = new node(dataGiven);
            temp.next = null;
            int iterator = 2;
            while (iterator < lineGiven) {
                prevNode = prevNode.next;
                nextNode = nextNode.next;
                iterator++;
            }
            nextNode = nextNode.next;
            prevNode.next = temp;
            temp.next = nextNode;
            numOfLines++;
            undoCmd insertedInBetween = new undoCmd();
            insertedInBetween.lineNumber = lineGiven;
            insertedInBetween.commandNumber = 6;
            undoStack.push(insertedInBetween);
        }
    }
    
    public void replaceTextInLine(String dataGiven,int lineGiven){		//this function will overwrite anything written in the given line
	if(lineGiven == 0)
            System.out.println("There's no line 0, did you mean 1\n");
        else if (lineGiven == 1) {
            this.head.data = head.data.replaceAll(head.data, dataGiven);
        }
        else if (lineGiven == numOfLines) {
            this.tail.data = tail.data.replaceAll(tail.data, dataGiven);
        }
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
    }
    
    public void addToTail(String dataGiven){		//this function will add to Tail
        if (head == null) //no node, empty linked list
        {
            node temp = new node(dataGiven);
            temp.next = null;
            head = temp;
            tail = head;
            numOfLines++;
        }
        else //one or more than one node
        {
            node temp = new node(dataGiven);
            temp.next = null;
            tail.next = temp;
            tail = temp;
            numOfLines++;
        }
        undoCmd addedToTail = new undoCmd();
        addedToTail.lineNumber = 1;
        addedToTail.commandNumber = 8;
        undoStack.push(addedToTail);
	}
    
    public void deleteLine(int lineGiven){							//this function should delete anything in the given line, also decreases the numOfLines
        if (head == null) {
            System.out.println("There is no line to be deleted/removed.\n");
        }
	else if (head == tail) {
            node temp = head;
            head = null;
            tail = null;
            numOfLines--;
        }	      
	else if(lineGiven == 0){
            System.out.println("There's no line 0, did you mean 1\n");	         
	}
        else if (lineGiven == 1) {
            String backup = head.data;
            node temp = head;
            node nextNode = head.next;
            head = nextNode;
            numOfLines--;
            undoCmd headRemoved = new undoCmd();
            headRemoved.text = backup;
            headRemoved.lineNumber = 1;
            headRemoved.commandNumber = 12;
            undoStack.push(headRemoved);
        }
        else if (lineGiven == numOfLines) {
            node temp = head;
            undoCmd deletedLine = new undoCmd();
            deletedLine.commandNumber = 11;
            while (temp.next != null && temp.next.next != null) {
                temp = temp.next;
            }
            tail = temp;
            String backup = temp.next.data;
            temp.next = null;
            numOfLines--;
            deletedLine.text = backup;
            deletedLine.lineNumber = lineGiven;
            undoStack.push(deletedLine);

        }
	else if (lineGiven > numOfLines) {
            System.out.println("Entered line is larger than existing lines...\n");
        }
	else if (lineGiven < numOfLines) {
            undoCmd deletedLine = new undoCmd();
            deletedLine.commandNumber = 10;
            node prevNode = head;
            node nextNode = head;
            node temp = head;
            int iterator = 2;
            while (iterator < lineGiven) {
                prevNode = prevNode.next;
                nextNode = nextNode.next;
                iterator++;
            }
            nextNode = nextNode.next;
            temp = nextNode;
            nextNode = nextNode.next;
            prevNode.next = nextNode;
            String backup = temp.data;
            numOfLines--;
            deletedLine.text = backup;
            deletedLine.lineNumber = lineGiven;
            undoStack.push(deletedLine);
        }
    }
    
    public void printall(){	//function used to print the whole linked list
	node temp = head;
        int linePrinted = 1;
        if (isEmpty()) {
            System.out.println("!! There Are No Elements Yet !!\n");
        } 
        else {
            while (temp != null) {
                if (linePrinted == 1) {
                    System.out.println("------------------- Content -------------------");
                } 
                System.out.print(linePrinted + ") " + temp.data);
                temp = temp.next;
                linePrinted++;
            }
            System.out.println("------------------- Content -------------------\n");
        }
    }

    public void saveAll(){
        node temp = head;
        int linePrinted = 1;
        int pagePrinted = 2;
        String fileName;
        fileName = "src\\texteditor\\test.txt";
        try{
            // Clearing file before inserting the data into it.
            File file = new File(fileName);
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
            // Inserting the data into it.
            FileWriter file1 = new FileWriter(fileName, true);
            while (temp != null) {
                file1.write(temp.data);
                temp = temp.next;
                linePrinted++;
            }
            file1.close();
            JOptionPane.showMessageDialog(null, "Saved Successfully");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        } 
    }
    
    public void numOfCharInLine(int lineGiven){	//count num of characters
        if(lineGiven == 0)
            System.out.println("There's no line 0, did you mean 1\n");
        else if (lineGiven > numOfLines)
            System.out.println("Entered line is larger than existing lines...\n");
        else if (lineGiven == 1) {
            int count = 0;
            for(int i = 0; i < head.data.length(); ++i){
                if(head.data.charAt(i) == ' ')
                    continue;
                count++;
            }
            System.out.println("Number of characters: " + --count + "\n");
        }
        else if (lineGiven == numOfLines) {
            int count = 0;
            for(int i = 0; i < tail.data.length(); ++i){
                if(tail.data.charAt(i) == ' ')
                    continue;
                count++;
            }
            System.out.println("Number of characters: " + --count + "\n");
        }
	else {
            int count = 0;
            node current = head.next;
            int iterator = 2;
            while (current.next != null) {
                if(iterator == lineGiven)
                    break;
                current = current.next;
                iterator++;
            }
            for(int i = 0; i < current.data.length(); ++i){
                if(current.data.charAt(i) == ' ')
                    continue;
                count++;
            }
            System.out.println("Number of characters: " + --count + "\n");
        }
    }
    
    public void insertBack(String value){ // To push into the linked list.
        node new_node = new node(value);
        if(isEmpty()){
            head = tail = new_node;
            new_node.next = new_node.previous = null;
        }
        else{
            new_node.next = null;
            new_node.previous = tail;
            tail.next = new_node;
            tail = new_node;
        }
    }
    
    public void readFile(){
        // Get The data from the file
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
        // Pushing the data into the linked list.
        for(int i = 0; i<info.length; ++i){
            info[i] += "\n";
            insertBack(info[i]);
            numOfLines++;
        }
        JOptionPane.showMessageDialog(null, "File Read Successfully");
    }
    
}
