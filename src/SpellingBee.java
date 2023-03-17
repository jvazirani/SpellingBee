import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);
    }

    public void makeWords(String leftSide, String remaining){
        words.add(leftSide);
        // If the right string is empty
        if(remaining.length() == 0){
            return;
        }
        for(int i = 0; i < remaining.length(); i++){
            // Take one letter out of the remaining
            String newLetter = remaining.substring(i, i+1);
            // Get everything but that letter
            String everythingLeft = remaining.substring(0,i) + remaining.substring(i+1);
            // Add that letter to the left branch
            makeWords(leftSide + newLetter, everythingLeft);
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        mergeSort(0, words.size() - 1);
    }

    public void mergeSort(int low, int high){
        if(low >= high){
            return;
        }
        // Med is average of high and low
        int med = (high + low) / 2;
        // Recurse on first half
        mergeSort(low, med);
        // Recurse on second half
        mergeSort(med + 1, high);
        merge(low, med, high);
    }

    public void merge(int low, int med, int high){
        ArrayList<String> merged = new ArrayList<String>();
        // i represents first half, j represents second half
        int i = low;
        int j = med + 1;
        while ((i <= med) && (j <= high)){
            // If words(i) is greater than words(j)
            // Add words(i) to new list
            if(words.get(i).compareTo(words.get(j)) < 0){
                merged.add(words.get(i));
                i++;
            }
            else {
                merged.add(words.get(j));
                j++;
            }
        }
        // Add the leftovers
        while(i <= med){
            merged.add(words.get(i));
            i++;
        }
        while(j <= high){
            merged.add(words.get(j));
            j++;
        }
        // make words a copy of merged
        for(int l = 0; l < merged.size(); l++){
            words.set(l+low, merged.get(l));
        }

    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        for(int i = 0; i < words.size(); i++){
            // If word not found, remove the word
            if (!(found(words.get(i), 0, DICTIONARY_SIZE -1))){
               words.remove(i);
               // Decrease i because .remove skips over it otherwise
               i--;
            }
        }
    }

    public boolean found(String target, int low, int high){
        // Base cases
        if (low > high)
            return false;
        int med = (high + low) / 2;
        if (DICTIONARY[med].equals(target)){
            return true;
        }
        else if(DICTIONARY[med].compareTo(target) > 0){
            high = med - 1;
        }
        else{
            low = med + 1;
        }
        // Recursive step
        return found(target, low, high);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
