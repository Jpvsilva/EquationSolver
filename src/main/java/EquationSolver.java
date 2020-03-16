import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationSolver {

    static Map<String,String> equations;
    static Map<String,String> equationsCanonical;

    public static void main(String[] args){
        equations = new HashMap<>();
        equationsCanonical = new HashMap<>();
        String input = "a=4+b\nb=c+d\nd=4\nc=3+2\n";
        findIncognitos(input);
        System.out.println(equations.toString());
        canonicalFormStage1();
        System.out.println(equationsCanonical);
        canonicalFormStage2();
    }

    public static void findIncognitos(String input){
        Character key = null;
        boolean keyIsFound = false;
        StringBuilder value = new StringBuilder();
        for(int i = 0; i<input.length();i++){
            if(input.charAt(i)=='\n'){
                assert key != null;
                String key2String = key.toString();
                System.out.println("Key: " + key2String + " -> " + "Value: " + value.toString());
                equations.put(key2String,value.toString());
                //System.out.println("Found Newline");
                key = null;
                value = new StringBuilder();
                keyIsFound = false;
            }
            else if(input.charAt(i)=='='){
                key = input.charAt(i-1);
                keyIsFound = true;
                //System.out.println("Found Incognito: " + input.charAt(i-1));
            }else if(keyIsFound){
                value.append(input.charAt(i));
            }
        }
    }

    /**
     * Find Numbers to sum up
     * Input: 4+b+5
     * Result: b+9
     * */
    public static void canonicalFormStage1(){
        ArrayList<Integer> numbers = new ArrayList<>();
        ArrayList<Integer> indexDecimals = new ArrayList<>();
        ArrayList<Integer> indexNumbers = new ArrayList<>();
        int indexNumber=-1;

        for (Map.Entry<String, String> entry : equations.entrySet()) {
            String aux = entry.getValue();
            StringBuilder newValue = new StringBuilder();
            for(int i = 0; i<aux.length();i++){
                /*If it's a number it could be 4a, 4, 4+4 */
                if(Character.isDigit(aux.charAt(i))){
                    /*If possible checks upfront*/
                    if(i+1<aux.length()){
                        /*If it's a number it could be 4a*/
                        if(Character.isLetter(aux.charAt(i+1))){
                            char multiplier = aux.charAt(i);
                            char incognito = aux.charAt(i+1);
                            String exp = Character.toString(multiplier) + incognito;
                            System.out.println("Full expression: " + exp);
                        }
                        /*If it's a number it could be 4+4 */
                        else if(isSignal(aux.charAt(i+1))){
                            char cAux = aux.charAt(i);
                            int j = Integer.parseInt(Character.toString(cAux));
                            indexNumber = i;
                            indexNumbers.add(i);
                            numbers.add(j);
                            System.out.println("Found Number: "+ j);
                        }
                        /*If it's a number it could be 44*/
                        else if(Character.isDigit(i+1)){
                            indexDecimals.add(i);
                            /**
                             * Missing this feature
                             * */
                            indexNumbers.add(i);
                        }
                    }else{
                        char cAux = aux.charAt(i);
                        int j = Integer.parseInt(Character.toString(cAux));
                        numbers.add(j);
                        System.out.println("Found Number At End Position: "+ j);
                    }
                }else if(Character.isLetter(aux.charAt(i))){
                    newValue.append(aux.charAt(i));
                    System.out.println("Found Letter: "+ aux.charAt(i));
                }else if(isSignal(aux.charAt(i))){
                    newValue.append(aux.charAt(i));
                    System.out.println("Found Signal: "+ aux.charAt(i));
                }
            }
            /*If more than one number was found*/
            if(numbers.size()>1){
                int sum = numbers.stream()
                        .mapToInt(a -> a)
                        .sum();
                System.out.println(sum);
                /*If expression was only numbers with plus signal then put sum */
                if(containsOnlySignals(newValue.toString())){
                    System.out.println(newValue);
                    newValue = new StringBuilder();
                    newValue.append(sum);
                /*Else if a char is between numbers*/
                }else{
                    String newValueCopy = getExpression(newValue,sum);
                    System.out.println("New Value Copy: " + newValueCopy);
                    newValue.delete(0,newValue.length());
                    newValue.append(newValueCopy);
                }
            }/*If just one number was found*/
            else if(numbers.size() == 1){
                /*Put on the respective order*/
                if(indexNumber!=-1){
                    String newValueCopy = newValue.toString().substring(0, indexNumber) +
                            numbers.get(0) +
                            newValue.toString().substring(indexNumber, newValue.length());
                    System.out.println("New Value Copy: " + newValueCopy);
                    newValue.delete(0,newValue.length());
                    newValue.append(newValueCopy);
                }/*Else was stored on the head meaning it was at the end*/
                else{
                    newValue.append(numbers.get(0));
                }
            }
            System.out.println("Key: " + entry.getKey() + " -> Value: " + newValue.toString());
            equationsCanonical.put(entry.getKey(), newValue.toString());
            numbers = new ArrayList<>();
        }
    }

    /**
     * Put equation in canonical form
     * a=b+4 <=> a-b=4
     * b=c+d <=> b-(c+d)=0 <=> b-c-d=0
     * */
    private static void canonicalFormStage2() {
        
    }

    private static String getExpression(StringBuilder newValue, int sum) {
        StringBuilder newValueCopy = new StringBuilder();
        for(int i = 0;i<newValue.length();i++){
            if(!isSignal(newValue.charAt(i))){
                newValueCopy.append(newValue.charAt(i));
            }
        }
        newValueCopy.append("+").append(sum);
        return newValueCopy.toString();
    }

    public static boolean containsOnlySignals(String string) {
        Pattern p = Pattern.compile("[a-zA-Z0-9]");
        Matcher m = p.matcher(string);
        return !m.find();
    }

    public static boolean isSignal(char charAt) {
        return (charAt == '+'|| charAt == '-');
    }
}
