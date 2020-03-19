import Jama.Matrix;

import java.util.*;

public class EquationSolver {

    static Map<Character,String> equations;
    static ArrayList<Character> variables;
    static Map<Character,Equation> equationMap;
    static Map<String,String> equationsCanonical;

    public static void main(String[] args){
        equations = new HashMap<>();
        equationsCanonical = new HashMap<>();
        String input = "a=b+4\nb=c+d\nd=4\nc=3+2\n";
        //resolve();

        findVariables(input);
        System.out.println(equations.toString());
        canonicalFormStage1();
        System.out.println(equationsCanonical);
        //canonicalFormStage2();
    }

    /**
     * Only finds variables if equation is in format
     * y = ax+b
     * */
    public static void findVariables(String input){
        Set<Character> variablesSet = new HashSet<>();
        Character key = null;
        boolean keyIsFound = false;
        StringBuilder value = new StringBuilder();
        for(int i = 0; i<input.length();i++){
            //Found Newline;
            if(input.charAt(i)=='\n'){
                assert key != null;
                String key2String = key.toString();
                //Equation eq = new Equation(key,value.toString(), signalIndex);
                System.out.println("Key: " + key2String + " -> " + "Value: " + value.toString());
                //equationMap.put(key,eq);
                variablesSet.add(key);
                equations.put(key,value.toString());
                key = null;
                value = new StringBuilder();
                keyIsFound = false;
            }
            //Found = signal meaning it found a variable Ex: a=
            else if(input.charAt(i)=='='){
                key = input.charAt(i-1);
                keyIsFound = true;
            //If variable was found get the rest of the string as an expression
            }else if(keyIsFound){
                value.append(input.charAt(i));
            }
        }
        variables = new ArrayList<>(variablesSet);
    }

    /**
     * Find Numbers to sum up and converts equation to matrix
     * Input: 4+b+5
     * Result: b+9
     * */
    public static void canonicalFormStage1(){
        ArrayList<Integer> independentNumbers = new ArrayList<>();
        double[][] matrix = new double[variables.size()][variables.size()];
        double[] line = new double[variables.size()];
        double[] results = new double[variables.size()];

        for (Map.Entry<Character, String> entry : equations.entrySet()) {
            String aux = entry.getValue();
            StringBuilder newValue = new StringBuilder();
            for(int i = 0; i<aux.length();i++){
                // filling line if a letter was found
                // only accepting + signal
                if(Character.isLetter(aux.charAt(i))){
                    if(variables.contains(aux.charAt(i))){
                        int indexToPut = variables.indexOf(aux.charAt(i));
                        line[indexToPut] = 1;
                        System.out.println("Filling line with 1s");
                    }else{
                        System.out.println("Invalid variable");
                    }
                }//If it's a number it could be 4a, 4, 4+4
                else if(Character.isDigit(aux.charAt(i))){
                    //If possible checks upfront
                    if(i+1<aux.length()){
                        //If it's a number it could be 4a
                        if(Character.isLetter(aux.charAt(i+1))){
                            int multiplier = Integer.parseInt(String.valueOf(aux.charAt(i)));
                            if(variables.contains(aux.charAt(i))){
                                int indexToPut = variables.indexOf(aux.charAt(i+1));
                                line[indexToPut] = multiplier;
                                System.out.println("Changed line index: "+ indexToPut + " to: " + multiplier);
                            }else{
                                System.out.println("Invalid variable");
                            }
                        }
                        //If it's a number it could be 4+4
                        else if(isSignal(aux.charAt(i+1))){
                            char cAux = aux.charAt(i);
                            int j = Integer.parseInt(Character.toString(cAux));
                            independentNumbers.add(j);
                            System.out.println("Found Number: "+ j);
                        }
                        //If it's a number it could be 44
                    }else{
                        char cAux = aux.charAt(i);
                        int j = Integer.parseInt(Character.toString(cAux));
                        independentNumbers.add(j);
                        System.out.println("Found Number at end position: "+ aux.charAt(i));
                    }
                }else if(isSignal(aux.charAt(i))){
                    //newValue.append(aux.charAt(i));
                    System.out.println("Found Signal: "+ aux.charAt(i));
                }
            }
            //If more than one number was found
            if(independentNumbers.size()>=1){
                int sum = independentNumbers.stream()
                        .mapToInt(a -> a)
                        .sum();
                System.out.println(sum);
                results[variables.indexOf(entry.getKey())] = sum;
            }
            line[variables.indexOf(entry.getKey())] = 1;
            System.out.println(Arrays.toString(line));
            matrix[variables.indexOf(entry.getKey())] = line;
            line = new double[variables.size()];
            independentNumbers.clear();
            System.out.println("Key: " + entry.getKey() + " -> Value: " + newValue.toString());
            //equationsCanonical.put(String.valueOf(entry.getKey()), newValue.toString());
        }
        printMatrix(matrix);
        //double[][] lhsArray = {{1, -1, 0, 0}, {0, 1, -1, -1}, {0, 0, 0, 1}, {0, 0, 1, 0}};
        System.out.println(Arrays.toString(results));
        //resolve(lhsArray,results);
    }

    private static void printMatrix(double[][] matrix) {
        for (double[] doubles : matrix) {
            System.out.print("[");
            for (double aDouble : doubles) {
                System.out.print(aDouble + " ");
            }
            System.out.print("]\n");
        }
    }

    private static void changeSignal(double[]line, String exp){

    }

    public static boolean isSignal(char charAt) {
        return (charAt == '+'|| charAt == '-');
    }

    public static void resolve(double[][] matrix, double[] results){
        double[][] lhsArray = {{1, -1, 0, 0}, {0, 1, -1, -1}, {0, 0, 0, 1}, {0, 0, 1, 0}};
        double[] rhsArray = {4, 0, 4, 5};
        //Creating Matrix Objects with arrays
        Matrix lhs = new Matrix(matrix);
        Matrix rhs = new Matrix(results, variables.size());
        //Calculate Solved Matrix
        Matrix ans = lhs.solve(rhs);
        //Printing Answers
        int index = 0;
        for(char a: variables){
            System.out.println(a + " = " + Math.round(ans.get(index, 0)));
            index++;
        }
    }
}
