package com.github.stigmata.birthmarks.wsp;

/**
 * Weight を計算するためのクラス．
 * 
 * @author Haruaki Tamada
 */
public class WeightCalculator{
    public int calculateWeight(int[][] wcs){
        int weight = 0;
        boolean[][] availableFlag = new boolean[wcs.length][wcs[0].length];
        for(int i = 0; i < wcs.length; i++){
            for(int j = 0; j < wcs[i].length; j++){
                availableFlag[i][j] = true;
            }
        }

        int length = wcs.length;
        if(length < wcs[0].length){
            length = wcs[0].length;
        }
        for(int k = 0; k < length; k++){
            int max = Integer.MIN_VALUE;
            int column = -1;
            int row = -1;
            for(int i = 0; i < wcs.length; i++){
                for(int j = 0; j < wcs[i].length; j++){
                    if(max < wcs[i][j] && availableFlag[i][j]){
                        max = wcs[i][j];
                        row = i;
                        column = j;
                    }
                }
            }
            if(column >= 0 && row >= 0){
                for(int i = 0; i < wcs.length; i++){
                    availableFlag[i][column] = false;
                }
                for(int j = 0; j < wcs[0].length; j++){
                    availableFlag[row][j] = false;
                }
                weight += wcs[row][column];
            }
        }

        return weight;
    }
}
