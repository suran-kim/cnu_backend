package com.programmers.java.baseball.engine;

import com.programmers.java.baseball.engine.io.Input;
import com.programmers.java.baseball.engine.io.NumberGenerator;
import com.programmers.java.baseball.engine.io.Output;
import com.programmers.java.baseball.engine.model.BallCount;
import com.programmers.java.baseball.engine.model.Numbers;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

// 외부 의존도를 input, output에 가지고 있지만 추상체로만 가지고 있다.
// @AllArgsConstructor를 통해 의존성 주입
// 처음에 NumberGenerator를 이 안에 구현하려다가 Faker를 쓰는 부분이 엔진에 있으면 안되겠다 싶어서 밖에 뺐음
// 어떤 것에도 외부 dependency가 없어야한다.
// 외부에 의해 수정될 여지, handling될 여지 없애기기
// 내가 만든 모델과 로직으로, 스스로 캡슐레이션 돼서 동작하는데 문제가 없도록 만들고
// 호스트쪽에서 로직에 맞춰서 모델도 전달하고 인터페이스 구현체도 전달하는 방식으로 작성하는 게 좋다.
// oop, 솔리드, 디자인패턴이 종합활용돼서 사용됨
// 작성해나가는 이 과정. 어떤 생각으로 이렇게 만들었고 처음엔 클래스로 만들었다가 인터페이스로 뺀 이유 같은 걸... 실습 프로젝트를 통해
// 감 익히기 : 흐름 : 설계하는 과정에 대한 걸 경험.

@AllArgsConstructor
public class BaseBall implements Runnable{
    private final int COUNT_OF_NUMBERS = 4; // 자릿수 설정

    // @AllArgsConstructor를 통해 이 부분을 인자로 받는다.
    private NumberGenerator generator;
    private Input input;
    private Output output;

    @Override
    public void run() {
        // 시작의 엔트리 포인트로 설정
        Numbers answer = generator.generate(COUNT_OF_NUMBERS); // Number타입으로 answer를 받는다. answer은 크기 3의 어레이

        //루프
        while(true) {
            String inputString =  input.input("숫자를 입력해주세요: "); // input객체로부터 input을 받는다. 사용자 입력은 String

            //옵셔널로 들어온 입력이 null값인지
            Optional<Numbers> inputNumbers = parse(inputString); // 게임 엔진 내에서 해야할 작업이므로 Baseball클래스에 메소드 작성
            if(inputNumbers.isEmpty()) {  // 입력값이 잘못들어오면
                output.inputError();    // 에러메세지 출력
                continue;     // 루프 계속
            }

            BallCount bc = ballCount(answer, inputNumbers.get());  // 이것도 게임 엔진 내에서 해줄만한 일. Optional inputNumbers에서 값을 꺼내서 입력해준다.

            output.ballCount(bc); // output의 ballCount에서 bc를 전달받아 출력
            if(bc.getStrike() == COUNT_OF_NUMBERS){   // 정답을 맞춘경우
                output.correct();
                break;
            }
        }
    }
                                    // 답                // 사용자가 입력한 것
    private BallCount ballCount(Numbers answer, Numbers inputNumbers) {
        AtomicInteger strike = new AtomicInteger();
        AtomicInteger ball = new AtomicInteger();

        answer.indexedForEach((a, i) -> {
            inputNumbers.indexedForEach((n, j) -> {
                if (!a.equals(n)) return;
                if (i.equals(j)) strike.addAndGet(1);
                else ball.addAndGet(1);
            });
        });
        return new BallCount(strike.get(),ball.get());
    }


    // String을 int[]로 변환
    // 사용자 입력이 잘못되는 경우 여기서 null리턴
    private Optional<Numbers> parse(String inputString) {
        // 입력값이 3글자가 아닌 경우
        if(inputString.length() != COUNT_OF_NUMBERS) return Optional.empty();

        // 입력값이 3글자인 경우
        // return이 Instream 타입으로 나온다. char는 2바이트. int값으로도 다 표현 가능. stream타입이라 한글자씩 다 떨어짐
        long count = inputString.chars()
                .filter(Character::isDigit) // 숫자가 아니면 통과못함
                .map(Character::getNumericValue) // char타입을  int형으로 바꾼다.
                .filter(i -> i > 0)  // 양수만 가져온다.
                .distinct() // 중복 제거
                .count();

        // 영문자가 포함된 경우, 중복인 수가 있는 경우 제거되어 글자수가 달라지게 됨
        if (count != COUNT_OF_NUMBERS) return Optional.empty();

        // 모든 검증 이후 출력
        return Optional.of(
                new Numbers(
                        inputString.chars()
                                .map(Character::getNumericValue)
                                .boxed()  // char의 결과는 intStream이므로 Integer타입으로 랩핑 시켜준다.
                                .toArray(Integer[]::new)  // Integer형 Array로 gogo
                )
        );
    }
}
