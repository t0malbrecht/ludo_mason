package services;

import java.util.ArrayList;
import java.util.List;

public class Combinations {
    public static ArrayList savedCombinations = null;
    public static ArrayList getCombinations() {
        if(savedCombinations != null){
            return savedCombinations;
        }
        ArrayList<int[]> combinations = new ArrayList<>();
        int range[] = {0,1,2,3,4,5};
        int firstDigit;
        int secondDigit;
        int thirdDigit;
        int fourthDigit;

        for(int i=0;i<range.length;i++){
            firstDigit = range[i];
            secondDigit = range[0];
            thirdDigit = range[0];
            fourthDigit = range[0];
            combinations.add(new int[] {firstDigit,secondDigit, thirdDigit, fourthDigit});
            for(int j=1;j<range.length;j++){
                secondDigit = range[j];
                thirdDigit = range[0];
                fourthDigit = range[0];
                combinations.add(new int[] {firstDigit,secondDigit, thirdDigit, fourthDigit});
                for(int k=0;k<range.length;k++){
                    thirdDigit = range[k];
                    fourthDigit = range[0];
                    combinations.add(new int[] {firstDigit,secondDigit, thirdDigit, fourthDigit});
                    for(int l=0;l<range.length;l++) {
                        fourthDigit = range[l];
                        combinations.add(new int[]{firstDigit, secondDigit, thirdDigit, fourthDigit});
                    }
                }
            }

        }
        savedCombinations = combinations;
        return combinations;
    }
}
