package com.programmers.java.baseball;

import com.programmers.java.baseball.engine.BaseBall;
import com.programmers.java.baseball.engine.io.Input;
import com.programmers.java.baseball.engine.io.NumberGenerator;
import com.programmers.java.baseball.engine.io.Output;

// 어플리케이션에서는 new BaseBall().run();을 하면 게임이 실행된다.
// BaseBall()에는 세 개의 인자가 필요하다.

public class App {
    public static void main(String[] args) {

        NumberGenerator generator = new HackFakerNumberGenerator(); // 인터페이스라 하나 만들 필요가 있다?? (뭔솔)? -> FakerNumberGenerator
        Console console = new Console();
                                // 왜 둘 다 사용이 될까? d인풋, 아웃풋
        new BaseBall(generator, console, console).run();
    }
}

