import java.util.ArrayList;
import java.util.Map;

public class Equation {
    private char variable;
    private String expression;
    private Map<Character,Integer> signalIndex;
    private ArrayList<Integer> independentNumbers;
    private ArrayList<Integer> biggerThan10;

    public Equation(char newVariable, String expression, Map<Character, Integer> signalIndex){
        this.variable = newVariable;
        this.expression = expression;
        this.signalIndex = fillSignalIndex(expression);
        fillSignalIndex(expression);
    }

    public Map<Character, Integer> fillSignalIndex(String expression){
        String aux = this.expression;
        StringBuilder newValue = new StringBuilder();
        for(int i = 0; i<aux.length();i++){
            //If it's a number it could be 4a, 4, 4+4
            if(Character.isDigit(aux.charAt(i))){
                //If possible checks upfront
                if(i+1<aux.length()){
                    //If it's a number it could be 4a
                    if(Character.isLetter(aux.charAt(i+1))){
                        char multiplier = aux.charAt(i);
                        char incognito = aux.charAt(i+1);
                        String exp = Character.toString(multiplier) + incognito;
                        System.out.println("Full expression: " + exp);
                    }
                    //If it's a number it could be 4+4
                    else if(isSignal(aux.charAt(i+1))){
                        char cAux = aux.charAt(i);
                        int j = Integer.parseInt(Character.toString(cAux));
                        //indexNumber = i;
                        //indexNumbers.add(i);
                        //numbers.add(j);
                        independentNumbers.add(j);
                        System.out.println("Found Number: "+ j);
                    }
                    //If it's a number it could be 44
                    else if(Character.isDigit(i+1)){
                        //Missing this feature
                        char cAux = aux.charAt(i);
                        int j = Integer.parseInt(Character.toString(cAux));
                        biggerThan10.add(j);
                    }
                }else{
                    char cAux = aux.charAt(i);
                    int j = Integer.parseInt(Character.toString(cAux));
                    independentNumbers.add(j);
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
        return null;
    }

    public static boolean isSignal(char charAt) {
        return (charAt == '+'|| charAt == '-');
    }
}
