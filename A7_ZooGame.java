import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Using trees as a data structure, ZooGame demonstrates that programs can
 * learn and interact with their users. Recursion is used to navigate trees.
 *
 * Requires A7_Branch.java
 *
 * @author Dustin Fay
 */

public class A7_ZooGame
{

    /**
     * Main method creates tree and calls startGame method,
     * passes in root from the tree as a parameter.
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        Branch y1 = new Branch("frog", null, null);
        Branch n1 = new Branch("moose", null, null);
        Branch root = new Branch("\nDoes it live in water?", y1, n1);
        System.out.println("Welcome to the zoo game. \nLet's begin...");
        startGame(root);
    }// end main

    /**
     * startGame method decides which type of game to play, either
     * interactively or with a file. If the input is an undesirable answer, the
     * method is recursively called again, with the root as a parameter.
     * Otherwise, according to the answer, the correct play method is called,
     * and the game begins.
     *
     * @param root
     *            - the root passed in.
     * @throws FileNotFoundException
     */
    @SuppressWarnings({})
    public static void startGame(Branch root) throws FileNotFoundException
    {
        System.out.println("Are we playing interactively?");
        String ans = getYesOrNoAnswer();
        if (ans.compareToIgnoreCase("yes") == 0)
        {
            // call Interactive method
            playInteractively(root);
        }// end if
        // otherwise
        else if (ans.compareToIgnoreCase("no") == 0)
        {
            // get input from zoo.txt
            try
            {
            	playFromList(root);
            }catch (Exception e){//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }// end else-if
        System.exit(1);
    }// end startGame

    /**
     * playInteractively method plays interactive mode with the user.
     */
    public static void playInteractively(Branch root)
    {
        // curr keeps track of the current node during traversal
        Branch curr = root;
        Scanner scan = new Scanner(System.in);
        // while not a leaf
        while (curr.getNoReference() != null)
        {
            // Print initial question
            System.out.println("" + curr.getData());
            String line = getYesOrNoAnswer();
            if (line.compareToIgnoreCase("yes") == 0)
            {
                // curr moves down to the left child
                curr = curr.getYesReference();
                // if it's not a leaf
                while (curr.getYesReference() != null)
                {
                    System.out.println(curr.getData());
                    String answer = getYesOrNoAnswer();
                    if (answer.compareToIgnoreCase("yes") == 0)
                    {
                        // curr moves down to it's left child
                        curr = curr.getYesReference();
                    }
                    else if (answer.compareToIgnoreCase("no") == 0)
                    {
                        // curr moves down to it's right child
                        curr = curr.getNoReference();
                    }
                }// end while not a leaf

                // we have reached a leaf, so we make a quess
                System.out.println("Is it a " + curr.getData() + "?");
                String answer = getYesOrNoAnswer();
                if (answer.compareToIgnoreCase("yes") == 0)
                {
                    System.out.println("Congratualtions. I was thinking of a "
                                       + curr.getData() + ".");
                    System.out.println("Do you want to play again?");
                    String playAgain = getYesOrNoAnswer();
                    if (playAgain.compareToIgnoreCase("yes") == 0)
                    {
                        curr = root;
                    }
                    else
                    {
                        // Quit.
                        System.out.println("Goodebye!");
                        return;
                    }
                }
                // for incorrect guess
                else if (answer.compareToIgnoreCase("no") == 0)
                {
                    System.out.println("What animal are you thinking of?");
                    String animal = scan.nextLine();
                    // create a new node for the new animal
                    Branch newNode = new Branch(animal, null, null);
                    System.out.println("Give me a question that will distinguish a "
                                       + animal + " from a " + curr.getData() + ".");
                    String question = scan.nextLine();
                    System.out.println("What is the correct answer for the question relating to a "
                                       + animal + "?");
                    String correct_answer = getYesOrNoAnswer();

                    // set nodes. If the correct answer is no
                    if (correct_answer.compareToIgnoreCase("no") == 0)
                    {
                        // set the current node to reference the new node for the new animal
                        curr.setNoReference(newNode);
                        // temp to hold the data in the current cell
                        String temp_data = curr.getData();
                        // set the curr data to the question that distinguishes the two
                        curr.setData(question);
                        // create the new node for the old animal
                        Branch replacementNode = new Branch(temp_data, null, null);
                        // set the current node to reference the old animal as it's left child
                        curr.setYesReference(replacementNode);
                    }// end if

                    // set nodes. If the correct answer for the animal were inserting is yes
                    if (correct_answer.compareToIgnoreCase("yes") == 0)
                    {
                        // set the yes reference to the new animal node
                        curr.setYesReference(newNode);
                        // temp to hold the data in the current cell
                        String temp_data = curr.getData();
                        // set the current node to the question
                        curr.setData(question);
                        // create a new node to hold the old animal
                        Branch replacementNode = new Branch(temp_data, null, null);
                        // set the curr to reference the old animal as it's right child
                        curr.setNoReference(replacementNode);
                    }// end if

                    System.out.println("Do you want to play again?");
                    String playAgain = getYesOrNoAnswer();
                    if (playAgain.compareToIgnoreCase("yes") == 0)
                    {
                        // reset the curr to the root
                        curr = root;
                    }
                    else
                    {
                        System.out.println("Goodbye!");
                        return;
                    }
                }// end else if
            }// end if for yes side

            // if no initial question
            if (line.compareToIgnoreCase("no") == 0)
            {
                // curr moves down to the left child
                curr = curr.getNoReference();
                // if it's not a leaf
                while (curr.getNoReference() != null)
                {
                    System.out.println(curr.getData());

                    String answer = getYesOrNoAnswer();
                    if (answer.compareToIgnoreCase("yes") == 0)
                    {
                        // curr moves down to it's left child
                        curr = curr.getYesReference();
                    }
                    else if (answer.compareToIgnoreCase("no") == 0)
                    {
                        // curr moves down to it's right child
                        curr = curr.getNoReference();
                    }
                }// end while not a leaf

                System.out.println("Is it a " + curr.getData());
                String answer = getYesOrNoAnswer();
                if (answer.compareToIgnoreCase("yes") == 0)
                {
                    System.out.println("Congradualtions. I was thinking of a "
                                       + curr.getData() + ".");
                    System.out.println("Do you want to play again?");
                    String playAgain = getYesOrNoAnswer();
                    if (playAgain.compareToIgnoreCase("yes") == 0)
                    {
                        // reset curr to root
                        curr = root;
                    }
                    else
                    {
                        // quit.
                        System.out.println("Goodbye!");
                        return;
                    }
                }// end if for yes

                // incorrect guess
                else if (answer.compareToIgnoreCase("no") == 0)
                {
                    System.out.println("What animal are you thinking of?");
                    String animal = scan.nextLine();
                    // new node to store the new animal -> pass it animal
                    Branch newNode = new Branch(animal, null, null);
                    System.out.println("Give me a question that distinguishes a "
                                    + animal + " from a " + curr.getData()
                                    + ".");
                    String question = scan.nextLine();
                    System.out.println("What is the correct answer for the question relating to a "
                                     + animal + "?");
                    String correct_answer = getYesOrNoAnswer();

                    // set nodes. If the correct answer is no
                    if (correct_answer.compareToIgnoreCase("no") == 0)
                    {
                        // set the current node to reference the new node for the new animal
                        curr.setNoReference(newNode);
                        // temp to hold the data in the current cell (replace data & copy old)
                        String temp_data = curr.getData();
                        // set the curr data to the question that distinguishes the two
                        curr.setData(question);
                        // Create the new node for the old animal
                        Branch replacementNode = new Branch(temp_data, null, null);
                        // set the current node to reference the old animal as it's left child
                        curr.setYesReference(replacementNode);
                    }// end if

                    // if the correct answer for the animal were inserting is yes
                    if (correct_answer.compareToIgnoreCase("yes") == 0)
                    {
                        // set the yes reference to the new animal node
                        curr.setYesReference(newNode);
                        // temp to hold the data in the current cell
                        String temp_data = curr.getData();
                        // set the current node to the question
                        curr.setData(question);
                        // Create a new node to hold the old animal
                        Branch replacementNode = new Branch(temp_data, null, null);
                        // set the curr to reference the old animal as it's right child
                        curr.setNoReference(replacementNode);
                    }
                    
                    System.out.println("Do you want to play again?");
                    String playAgain = getYesOrNoAnswer();
                    if (playAgain.compareToIgnoreCase("yes") == 0)
                    {
                        // reset the curr to the root
                        curr = root;
                    }
                    else
                    {
                        // output
                        System.out.println("Goodbye!");
                        return;
                    }
                }// end else if
            }// end if for yes side
        }// end while
    }// end playInteractively()
    
    
    /**
     * playFromList method plays game using input from zoo.txt.
     */
    public static void playFromList(Branch root) throws IOException
    {
    	// curr to keep track of the current node during traversal
        Branch curr = root;
        // read from file zoo.txt
        FileInputStream fstream = new FileInputStream("zoo.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));	
        // while not a leaf
        while (curr.getNoReference() != null)
        {
            // Print initial question
            System.out.println("" + curr.getData());
            String line = br.readLine();
            if (line.compareToIgnoreCase("yes") == 0)
            {
                // curr moves down to the left child
                curr = curr.getYesReference();
                // if it's not a leaf
                while (curr.getYesReference() != null)
                {
                    System.out.println(curr.getData());
                    String answer = br.readLine();
                    if (answer.compareToIgnoreCase("yes") == 0)
                    {
                        // curr moves down to it's left child
                        curr = curr.getYesReference();
                    }
                    else if (answer.compareToIgnoreCase("no") == 0)
                    {
                        // curr moves down to it's right child
                        curr = curr.getNoReference();
                    }
                }// end while not a leaf

                // we have reached a leaf, so we make a quess
                System.out.println("Is it a " + curr.getData() + "?");
                String answer = br.readLine();
                if (answer.compareToIgnoreCase("yes") == 0)
                {
                    System.out.println("Congratualtions. I was thinking of a "
                                       + curr.getData() + ".");
                    System.out.println("Do you want to play again?");
                    String playAgain = br.readLine();
                    // System.out.println(playAgain);
                    if (playAgain.compareToIgnoreCase("yes") == 0)
                    {
                        curr = root;
                    }
                    else
                    {
                        // Quit.
                        System.out.println("Goodebye!");
                        return;
                    }
                }
                // for incorrect guess
                else if (answer.compareToIgnoreCase("no") == 0)
                {
                    System.out.println("What animal are you thinking of?");
                    // animal gets the next line
                    String animal = br.readLine();
                    // create a new node for the new animal
                    Branch newNode = new Branch(animal, null, null);
                    System.out.println("Give me a question that will distinguish a "
                                      + animal + " from a " + curr.getData() + ".");
                    String question = br.readLine();
                    System.out.println("What is the correct answer for the question relating to a "
                                       + animal + "?");

                    String correct_answer = br.readLine();
                    // set nodes. If the correct answer is no
                    if (correct_answer.compareToIgnoreCase("no") == 0)
                    {
                        // set the current node to reference the new node for the new animal
                        curr.setNoReference(newNode);
                        // temp to hold the data in the current cell
                        String temp_data = curr.getData();
                        // set the curr data to the question that distinguishes the two
                        curr.setData(question);
                        // create the new node for the old animal
                        Branch replacementNode = new Branch(temp_data, null, null);
                        // set the current node to reference the old animal as it's left child
                        curr.setYesReference(replacementNode);
                     }// end if
                    // set nodes. If the correct answer for the animal were inserting is yes
                    if (correct_answer.compareToIgnoreCase("yes") == 0)
                    {
                        // set the yes reference to the new animal node
                        curr.setYesReference(newNode);
                        // temp to hold the data in the current cell
                        String temp_data = curr.getData();
                        // set the current node to the question
                        curr.setData(question);
                        // create a new node to hold the old animal
                        Branch replacementNode = new Branch(temp_data, null, null);
                        // set the curr to reference the old animal as it's right child
                        curr.setNoReference(replacementNode);
                    }// end if
                    
                    System.out.println("Do you want to play again?");
                    String playAgain = br.readLine();
                    if (playAgain.compareToIgnoreCase("yes") == 0)
                    {
                        // reset the curr to the root
                        curr = root;
                    }
                    else
                    {
                        System.out.println("Goodbye!");
                        return;
                    }
                }// end else if
            }// end if for yes side

            // if no initial question
            if (line.compareToIgnoreCase("no") == 0)
            {
            	// curr moves down to the left child
            	curr = curr.getNoReference();
                // if it's not a leeaf
                while (curr.getNoReference() != null)
                {
                    // output
                    System.out.println(curr.getData());
                    // answer gets next line
                    String answer = br.readLine();
                    // if yes
                    if (answer.compareToIgnoreCase("yes") == 0)
                    {
                        // curr moves down to it's left child
                        curr = curr.getYesReference();
                    }// end if
                    // if no...
                    else if (answer.compareToIgnoreCase("no") == 0)
                    {
                        // curr moves down to it's right child
                        curr = curr.getNoReference();
                    }// end else if
                }// end while not a leaf
                
                // Ask if it is animal in current node
                System.out.println("Is it a " + curr.getData());
                String answer = br.readLine();
                if (answer.compareToIgnoreCase("yes") == 0)
                {
                    System.out.println("Congradualtions. I was thinking of a "
                                       + curr.getData() + ".");
                    System.out.println("Do you want to play again?");
                    String playAgain = br.readLine();
                    // if yes
                    if (playAgain.compareToIgnoreCase("yes") == 0)
                    {
                        // reset curr to root
                        curr = root;
                    }
                    else
                    {
                        // quit.
                        System.out.println("Goodbye!");
                        return;
                    }
                }// end if for yes
                // if incorrect guess
                else if (answer.compareToIgnoreCase("no") == 0)
                {
                    System.out.println("What animal are you thinking of?");
                    String animal = br.readLine();
                    // We need a new node to store the new animal -> pass it the animal
                    Branch newNode = new Branch(animal, null, null);
                    System.out.println("Give me a question that distinguishes a "
                                       + animal + " from a " + curr.getData() + ".");
                    // question gets the next line
                    String question = br.readLine();
                    System.out.println("What is the correct answer for the question relating to a "
                                       + animal + "?");
                    // correct_answer reads the next line
                    String correct_answer = br.readLine();
                    // set nodes. If the correct answer is no...
                    if (correct_answer.compareToIgnoreCase("no") == 0)
                    {
                        // set current node to reference new node for new animal
                        curr.setNoReference(newNode);
                        // temp to hold data in the current cell (replace data & copy old)
                        String temp_data = curr.getData();
                        // set the curr data to the question that distinguishes the two
                        curr.setData(question);
                        // Create the new node for the old animal
                        Branch replacementNode = new Branch(temp_data, null, null);
                        // set the current node to reference the old animal as it's left child
                        curr.setYesReference(replacementNode);
                    }// end if
                    
                    // if the correct answer for the animal were inserting is yes
                    if (correct_answer.compareToIgnoreCase("yes") == 0)
                    {
                        // setThe yes reference to the new animal node
                        curr.setYesReference(newNode);
                        // temp to hold the data in the current cell
                        String temp_data = curr.getData();
                        // set the current node to the question
                        curr.setData(question);
                        // Create a new node to hold the old animal
                        Branch replacementNode = new Branch(temp_data, null, null);
                        // set the curr to reference the old animal as it's right child
                        curr.setNoReference(replacementNode);
                    }// end if
                    
                    System.out.println("Do you want to play again?");
                    // playAgain gets the next line.
                    String playAgain = br.readLine();
                    if (playAgain.compareToIgnoreCase("yes") == 0)
                    {
                        // reset the curr to the root
                        curr = root;
                    }
                    else
                    {
                        System.out.println("Goodbye!");
                        return;
                    }
                }// end else if
            }// end if no
        }// end while
        //Close the input stream
        br.close();
    }// end playFromList

    /**
     * The getYesOrNoAnswer() method gets the next line from the scanner,
     * validates that it is in fact a "yes" or "no", and returns the answer.
     *
     * @return either the answer if it is an acceptable answer, or a recursive
     *         method call that prompts the user again for an answer.
     */
    private static String getYesOrNoAnswer()
    {
        Scanner scan = new Scanner(System.in);
        String answer = scan.next();
        // check if yes or no
        if (answer.compareToIgnoreCase("yes") == 0 || answer.compareToIgnoreCase("no") == 0)
        {
            return answer;
        }
        // otherwise, output to the user "must be yes or no".
        else
        {
            System.out.println("You must enter a yes or no.");
            // return the method itself
            return getYesOrNoAnswer();
        }
    }// end getYesOrNoAnswer

    /**
     * The isAnimalInList() is a recursive search method to determine if
     * animal is in the tree or not.
     *
     * @param root
     *            - the root passed in.
     * @param animal
     *            - the animal passed in.
     * @return either true if the animal is found, or a recursive call with
     *         either the left or right child of current as a parameter.
     */
    public Boolean isAnimalInList(Branch root, String animal)
    {
        // base case
        if (root.getData() == animal)
        {
            return true;
        }
        // If the current root has a left child, call recursively with 
        // left child as the new parameter
        if (root.getYesReference() != null)
        {
            return isAnimalInList(root.getYesReference(), animal);
        }
        // If the current root has a right child, call recursively with
        // right child as the new parameter
        if (root.getNoReference() != null)
        {
            return isAnimalInList(root.getNoReference(), animal);
        }
        // if not found, return false, -> insert in tree
        return false;
    }// end method

}// end ZooGame
