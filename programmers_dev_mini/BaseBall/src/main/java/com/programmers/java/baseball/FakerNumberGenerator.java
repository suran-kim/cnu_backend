package com.programmers.java.baseball;

import com.github.javafaker.Faker;
import com.programmers.java.baseball.engine.io.NumberGenerator;
import com.programmers.java.baseball.engine.model.Numbers;

import java.util.stream.Stream;

// NumberGeneraotr를 상속받은  FakerNumberGenerator로 숫자 생성 클래스 구현 의존성역전..?
public class FakerNumberGenerator implements NumberGenerator {

    private final Faker faker = new Faker(); // 한 번 만들어서 계속 쓰기 때문에 final

    public Numbers generate(int count) {

        return new Numbers(   // new Numbers의 인자로 전달해서 return
                Stream.generate(() -> faker.number().randomDigitNotZero()) // 0이 아닌 가짜 숫자 생성
                        .distinct()
                        .limit(count)
                        .toArray(Integer[]::new) // Integer의 어레이로 만든다.
        );

    }
}
