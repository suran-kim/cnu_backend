package AlgorithmStudy;

public class BinarySearchTest2 {
    public static int binarySearch(int[] arrLst, int find) {
        int mid;
        int left = 0;
        int right =  arrLst.length - 1; // 배열의 마지막 요소 인덱스

        BubleSortTest.BubbleSort(arrLst); // 이진탐색 코드에 버블정렬 추가
        
        // 배열 크기가 1이 될 때까지 반복
        while (left <= right){
            mid = (right + left) / 2; // mid는 배열의 중간요소 인덱스

            // 원하는 값을 찾았을 때 그 위치를 변경
            if (arrLst[mid] == find){
                return mid;
            }

            if (arrLst[mid] == find){
                return mid;
            } else {
                left = mid + 1;
            }

        }
        // 원하는 값을 찾지 못함
        return -1;
    }


    public static void main(String[] args) {
        int arrLst[] = {3, 7, 20, 4, 11, 0, 8, 9};
        int index = 0;


        for (int i = 0; i < arrLst.length; i++) {
            System.out.print(arrLst[i] + " ");
        }

        System.out.println(" ");

        index = binarySearch(arrLst, 7);

        if (index > 0) {
            System.out.println("당신의 숫자는 인덱스 " + index + "번 에 들어있습니다."); // 검색결과는 3
        } else {
            System.out.println(" ");
            System.out.println("검색결과가 없습니다?!"); 
        }

    }
}

