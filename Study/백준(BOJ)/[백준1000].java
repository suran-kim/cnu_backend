package backjun_bronze;
import java.util.Scanner;

public class plusAandB {
    public static void main(String[] args) {
        int a = 0, b = 0;

        Scanner sc = new Scanner(System.in);
        a = sc.nextInt();
        b = sc.nextInt();

        System.out.println(a + b);
    }

}

/* 6번째 라인에서 초기화는 안해도 될 듯?
* '첫째 줄에 A와 B가 주어진다. (0 < A, B < 10)'
*  라는 문장이 있어서 입력값의 범위를 제한해야 하는 건가 싶었는데 그건 아닌 듯
*  무슨 의미인지 잘 모르겠다
* */