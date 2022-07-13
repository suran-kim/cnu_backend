package com.programmers.java.baseball;


import com.programmers.java.baseball.engine.io.Input;
import com.programmers.java.baseball.engine.io.Output;
import com.programmers.java.baseball.engine.model.BallCount;

import java.util.Scanner;

// 설계상 input과 Output을 이놈이 다 처리
public class Console implements Input, Output {
    private final Scanner scanner = new Scanner(System.in); // 한 번 쓰면 계속 재사용 final


    // input을 받아들인다
    @Override
    public String input(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    // 프롬프트 출력
    @Override
    public void ballCount(BallCount bc) {
        System.out.println(bc.getStrike() + "Strikes, " + bc.getBall() + "Balls");
    }

    @Override
    public void inputError() {
        System.out.println("입력이 잘못되었습니다.");
    }

    @Override
    public void correct() {
        System.out.println("정답입니다.");
    }
}
