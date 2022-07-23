package AlgorithmStudy;

public class BubleSortTest {
    public static void BubbleSort(int[] arr){
        final int length = arr.length;
        int iTmp = 0;
        for (int iCnt = 0 ; iCnt < length ; iCnt++) { // 배열의 길이만큼 돈다.
            for (int iCn = 0 ; iCn < length - iCnt -1 ; iCn++) {
                if (arr[iCn] > arr[iCn + 1]) {
                    iTmp = arr[iCn];
                    arr[iCn] = arr[iCn + 1];
                    arr[iCn + 1] = iTmp;
                }
            }

            // 과정을 출력하는 코드
            System.out.print((iCnt + 1) + "회전 - 현재 과정: ");

            for (int iPos : arr)
                System.out.print(iPos + " ");

            System.out.println();
        }

    }

    public static void main(String[] args) {
        int arr[] = {3, 55, 20, 4, 11, 0, 7, 9};
        BubbleSort(arr);

        System.out.println();
        System.out.println("버블 정렬결과는 ... ");
        for (int iCnt : arr)
            System.out.print(iCnt + " ");
    }
}
