import Jama.Matrix;
import com.sun.javafx.geom.transform.SingularMatrixException;

import java.util.*;

public class EquationSolver {

    static Map<Character,String> equations;
    static ArrayList<Character> variables;
    static Map<String,String> equationsCanonical;

    public static void main(String[] args){
        equations = new HashMap<>();
        equationsCanonical = new HashMap<>();
        String input = "a=b+4\nb=c+d\nd=4\nc=3+2\n";
        String input2 = "a=b-4\nb=c+d\nd=4\nc=3+2\ne=6+8\n";

        findVariables(input2);
        System.out.println(equations.toString());
        convert2Matrix();
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
                System.out.println("Key: " + key2String + " -> " + "Value: " + value.toString());
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
     * Input: a=4+b+5
     * Result: a=b+9
     * Convert to Canonical Form: a-b=9
     * Matrix Line Should be: [1,-1,...]  [9,...]
     * */
    public static void convert2Matrix(){
        ArrayList<Integer> independentNumbers = new ArrayList<>();
        double[][] matrix = new double[variables.size()][variables.size()];
        double[] line = new double[variables.size()];
        double[] results = new double[variables.size()];

        for (Map.Entry<Character, String> entry : equations.entrySet()) {
            char key = entry.getKey();
            String value = entry.getValue();

            for(int i = 0; i<value.length();i++){
                // filling line if a letter was found
                if(Character.isLetter(value.charAt(i))){
                    if(variables.contains(value.charAt(i))){
                        int indexToPut = variables.indexOf(value.charAt(i));
                        if(i-1>0){
                            if(value.charAt(i-1)=='-'){
                                line[indexToPut] = -1;
                            }else{
                                line[indexToPut] = 1;
                            }
                        }else{
                            line[indexToPut] = 1;
                        }
                        System.out.println("Filling line with 1s");
                    }else{
                        System.out.println("Invalid variable");
                    }
                }//If it's a number it could be 4a, 4, 4+4
                else if(Character.isDigit(value.charAt(i))){
                    //If possible checks upfront
                    if(i+1<value.length()){
                        //If it's a number it could be 4a
                        if(Character.isLetter(value.charAt(i+1))){
                            int multiplier = Integer.parseInt(String.valueOf(value.charAt(i)));
                            if(i-1>0){
                                if(value.charAt(i-1)=='-'){
                                    multiplier *= -1;
                                }
                            }
                            if(variables.contains(value.charAt(i))){
                                int indexToPut = variables.indexOf(value.charAt(i+1));
                                line[indexToPut] = multiplier;
                                System.out.println("Changed line index: "+ indexToPut + " to: " + multiplier);
                            }else{
                                System.out.println("Invalid variable");
                            }
                        }
                        //If it's a number it could be 4+4
                        else if(isSignal(value.charAt(i+1))){
                            char cAux = value.charAt(i);
                            int j = Integer.parseInt(Character.toString(cAux));
                            independentNumbers.add(j);
                            System.out.println("Found Number: "+ j);
                        }
                        //If it's a number it could be 44
                        //To be defined ...
                    }else{
                        char cAux = value.charAt(i);
                        int j = Integer.parseInt(Character.toString(cAux));
                        if(i-1>0){
                            if(value.charAt(i-1)=='-'){
                                j *= -1;
                            }
                        }
                        independentNumbers.add(j);
                        System.out.println("Found Number at end position: "+ value.charAt(i));
                    }
                }else if(isSignal(value.charAt(i))){
                    System.out.println("Found Signal: "+ value.charAt(i));
                }
            }
            //If more than one number was found
            if(independentNumbers.size()>=1){
                int sum = independentNumbers.stream()
                        .mapToInt(a -> a)
                        .sum();
                System.out.println(sum);
                results[variables.indexOf(key)] = sum;
            }
            line[variables.indexOf(key)] = 1;
            System.out.println(Arrays.toString(line));
            changeSignal(line,value,key);
            System.out.println(Arrays.toString(line));
            matrix[variables.indexOf(key)] = line;
            line = new double[variables.size()];
            independentNumbers.clear();
            System.out.println("Key: " + key + " -> Value: " + value);
        }
        printMatrix(matrix);
        System.out.println(Arrays.toString(results));
        try{
            resolve(matrix,results);
        }catch (Exception e){
            System.out.println("Non-Singular Matrix");
        }
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

    private static void changeSignal(double[]line, String exp, char variable){
        int countPlus = 0, countMinus = 0, countSigns = 0;
        for(int i = 0;i<exp.length();i++){
            if(isSignal(exp.charAt(i))){
                if(exp.charAt(i) == '-'){
                    countMinus++;
                }else{
                    countPlus++;
                }
                countSigns++;
            }
        }
        // All plus signs
        if(countPlus == countSigns){
            for(int i = 0;i<line.length;i++){
                if(i!=variables.indexOf(variable) && line[i]!=0){
                    line[i] *= -1;
                }
            }
        }
        // All minus signs
        else if(countMinus == countSigns){
            for(int i = 0;i<line.length;i++){
                if(i!=variables.indexOf(variable) && line[i]!=0){
                    line[i] *= -1;
                }
            }
        }
    }

    public static boolean isSignal(char charAt) {
        return (charAt == '+'|| charAt == '-');
    }

    public static void resolve(double[][] matrix, double[] results)throws SingularMatrixException {
        //double[][] lhsArray = {{1, -1, 0, 0}, {0, 1, -1, -1}, {0, 0, 0, 1}, {0, 0, 1, 0}};
        //double[] rhsArray = {4, 0, 4, 5};
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
