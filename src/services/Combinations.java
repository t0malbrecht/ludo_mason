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
            for(int j=0;j<range.length;j++){
                for(int k=0;k<range.length;k++) {
                        for (int l = 0; l < range.length; l++) {
                                firstDigit = range[i];
                                secondDigit = range[j];
                                thirdDigit = range[k];
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
