package AlgorithmStudy;

public class LinearSearchTest {
    public static int LinearSearch(int[] arrLst, int find) {
        System.out.println(arrLst.length-1);
        for(int i = 0 ; i < arrLst.length-1  ; i++) {  // 배열을 순회하며 매개변수로 입력된 숫자를 찾는다.
            if (find == arrLst[i]) {
                return i; // 숫자가 들어있는 인덱스를 리턴
            }
        }
        return -1; // 숫자가 배열에 없으면 -1을 리턴
    }


    public static void main(String[] args) {
        int arrLst[] = {3, 55, 20, 4, 11, 0, 7, 9};

        int num = LinearSearch(arrLst, 7);

        if (num > 0)
            System.out.println("당신의 숫자는 인덱스 " + num + "번 에 들어있습니다.");
        else
            System.out.println("검색결과가 없습니다?!");
    }
}
