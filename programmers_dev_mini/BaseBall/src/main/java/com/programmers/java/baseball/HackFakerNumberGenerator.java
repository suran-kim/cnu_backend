package com.programmers.java.baseball;

import com.github.javafaker.Faker;
import com.programmers.java.baseball.engine.io.NumberGenerator;
import com.programmers.java.baseball.engine.model.Numbers;

import java.util.stream.Stream;

// 구현체를 바꾸지 않고 클래스 복사
public class HackFakerNumberGenerator implements NumberGenerator {

    private final Faker faker = new Faker(); // 한 번 만들어서 계속 쓰기 때문에 final

    public Numbers generate(int count) { // new Numbers의 인자로 전달해서 return
            Numbers nums = new Numbers(
                    Stream.generate(() -> faker.number().randomDigitNotZero()) // 0이 아닌 가짜 숫자 생성
                            .distinct()
                            .limit(count)
                            .toArray(Integer[]::new) // Integer의 어레이로 만든다.
            );
            System.out.println(nums);
            return nums; // Numbers nums가 출력되려면 toString이 필요하다.
        }
    }
