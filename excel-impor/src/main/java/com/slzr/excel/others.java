package com.slzr.excel;

public class others {

    public static void main(String[] args) {
        int [][] arr = {{1,2,3},{4,5,6}}; //定义了两行三列的二维数组并赋值
        for(int x = 0; x<arr.length; x++){ //定位行
            for(int y = 0; y<arr[x].length; y++){ //定位每行的元素个数
                System.out.print(arr[x][y]);
            }
            System.out.println("/n");
        }
    }
}
