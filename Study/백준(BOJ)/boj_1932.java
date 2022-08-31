package backjun_silver;

import java.io.*;
import java.util.*;

public class dp_1932 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine()); // 삼각형의 크기 입력받기

        int dpArray[][] = new int[n + 1][n + 1]; // 동적 배열
        int array[][] = new int[n + 1][n + 1];

        // 삼각형 값을 입력받아 배열에 저장
        for (int i = 1; i <= n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= i; j++) {
                array[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 점화식을 이용해 dpArray의 최하단층에 최댓값을 모아둔다.
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                dpArray[i][j] = Math.max(dpArray[i - 1][j - 1], dpArray[i - 1][j]) + array[i][j]; // 누적합이 2개인 경우 비교하여 최댓값을 저장
            }
        }

        int max = 0;
        // dpArray의 최하단층에서 가장 큰 값을 찾아준다.
        for (int i = 1; i <= n; i++) {
            if (max < dpArray[n][i]) max = dpArray[n][i];
        }

        System.out.println(max);
    }
}


//int N= 10;
//        int M= 10;
//
//        int arr_max[][] = new int[n + 1][n + 1];
//        int arr[][] = new int[n + 1][n + 1];
//
//// 2차배열
//// 0부터 사용
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < M; j++) {
//                if (i == 0 && j == 0)
//                    arr_max[i][j] = arr[i][j];
//                else if(i == 0)
//                    arr_max[i][j] = arr[i][j] + arr[i][j-1];
//                else if (j == 0)
//                    arr_max[i][j] = arr[i][j] + arr[i - 1][j];
//                else
//                    arr_max[i][j] = arr[i][j] + arr[i][j - 1] + arr[i - 1][j];
//            }
//        }
//// 1부터 사용
//        for (int i = 1; i <= N; i++) {
//            for (int j = 1; j <= M; j++) {
//                arr_max[i][j] = arr[i][j] + arr[i][j - 1] + arr[i - 1][j];
//            }
//        }
//    }
//}


//        for (int i = 1; i <= n; i++) {
//            for (int j = 1; j <= n; j++) {
//                System.out.print(array[i][j]);
//            }
//            System.out.println();
//        }
