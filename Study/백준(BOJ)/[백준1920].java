package backjun_bronze;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class jun_1920 {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt();
        int[] arr = new int[N]; // 배열의 크기 입력받기

        for (int i = 0; i < N ; i++){  // 배열에 숫자 채우기
            arr[i] = sc.nextInt();
        }

        // 이진탐색 정렬 특징 : 배열이 반드시 정렬되어 있어야 한다.
        Arrays.sort(arr);

        int M = sc.nextInt(); // 검색할 숫자의 수 입력하기

        StringBuffer sb = new StringBuffer();
        for (int i = 0 ; i < M ; i++) {
            if(binarySearch(arr, sc.nextInt()) >= 0 ) {
                sb.append(1).append('\n');
            }
            else {
                sb.append(0).append('\n');
            }
        }
        System.out.println(sb);
    }




    public static int binarySearch(int[] arr, int key) {
        int left = 0;
        int right = arr.length - 1;

        // left가 right보다 커지기 전까지 반복
        while (left <= right) {

            int mid = (left + right) / 2;

            // key값이 mid값보다 작을 때
            if (key < arr[mid]) {
                right = mid - 1;
            }

            // key값이 mid값보다 클 때
            else if (key > arr[mid]) {
                left = mid + 1;
            }

            // key값이 mid값일 때
            else {
                return mid;
            }

        }

        // 찾는 값이 없다
        return -1;
    }

}


// 1. arrayindexoutofboundsexception
// 매우 어이없는 실수. 이진 탐색 메소드의   int mid = (left + right) / 2; 코드에 괄호를 닫지 않아 매번 right / 2 연산이 먼저 되는 사태가 발생

// 2. StringBuffer의 사용
// StringBuffer메소드를 처음 사용해봤는데 조금 더 공부해볼 필요가 있을 것 같다.

// 3. 성능 문제
// 입력방법으로 Scanner 대신 BufferedReader를 사용하면 메모리 사용량과 실행 시간이 반으로 줄어든다고 한다.
// 다음에 시도해볼 예정