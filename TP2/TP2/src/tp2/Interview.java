package tp2;

import java.io.*;
import java.util.HashMap;

public final class Interview {

    /** TODO
     * This function returns if the two texts are similar based on if they have a similar entropy of the HashMap
     * @return boolean based on if the entropy is similar
     */
    public static Double compareEntropies(String filename1, String filename2) throws IOException {
        HashMap<Character, Integer> mapText1 = getFrequencyHashTable(readFile(filename1));
        HashMap<Character, Integer> mapText2 = getFrequencyHashTable(readFile(filename2));
        return Math.abs(calculateEntropy(mapText1) - calculateEntropy(mapText2));
    }

    /** TODO
     * This function returns the difference in frequencies of two HashMaps which corresponds
     * to the sum of the differences of frequencies for each letter.
     * @return the difference in frequencies of two HashMaps
     */
    public static Integer compareFrequencies(String filename1, String filename2) throws IOException{
        Integer comparedFrequency = 0;
        HashMap<Character, Integer> mapText1 = getFrequencyHashTable(readFile(filename1));
        HashMap<Character, Integer> mapText2 = getFrequencyHashTable(readFile(filename2));
        for ( HashMap.Entry<Character, Integer> characterText1 : mapText1.entrySet() ) {
            Integer characterRateText2 = mapText2.get(characterText1.getKey());
            if (characterRateText2 == null){
                comparedFrequency += characterText1.getValue();
            }
            else
                comparedFrequency += Math.abs(characterText1.getValue() - characterRateText2);
        }
        for ( HashMap.Entry<Character, Integer> characterText2 : mapText2.entrySet() ) {
            Integer characterRateText1 = mapText1.get(characterText2.getKey());
            if (mapText1.containsKey(characterText2.getKey())){
                continue;
            }
            if (characterRateText1 == null){
                comparedFrequency += characterText2.getValue();
            }
            else
                comparedFrequency += Math.abs(characterText2.getValue() - characterRateText1);
        }
        return comparedFrequency;
    }

    /** TODO
     * @return This function returns the entropy of the HashMap
     */
    public static Double calculateEntropy(HashMap<Character, Integer> map){
        Double numCharacters = 0.0;
        Double entropy = 0.0;
        for (Integer value : map.values())
            numCharacters += value;
        for (Integer value : map.values()) {
            entropy += (value/numCharacters)*(Math.log((numCharacters/value))/Math.log(2));
        }
        return entropy;
    }

    /**
     * This function reads a text file {filenamme} and returns the appended string of all lines
     * in the text file
     */
    public static String readFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String lineRead;
        StringBuilder text = new StringBuilder();
        while ((lineRead = br.readLine()) != null) {
            text.append(lineRead);
        }
        br.close();
        return text.toString();
    }

    /** TODO
     * This function takes a string as a parameter and creates and returns a HashTable
     * of character frequencies
     */
    public static HashMap<Character, Integer> getFrequencyHashTable(String text) {
        HashMap<Character, Integer> map = new HashMap<>();
        char[] textArray = text.toCharArray();
        for (Character currentCharacter : textArray) {
            if (isAlphabetic(currentCharacter)) {
                int characterRate = 0;
                for (Character comparingCharacter : textArray) {
                    if (currentCharacter.equals(comparingCharacter))
                        characterRate++;
                }
                map.put(currentCharacter, characterRate);
            }

        }
        return map;
    }

    /** TODO
     * This function takes a character as a parameter and returns if it is a letter in the alphabet
     */
    public static Boolean isAlphabetic(Character c){
        return Character.isAlphabetic(c);
    }
}
