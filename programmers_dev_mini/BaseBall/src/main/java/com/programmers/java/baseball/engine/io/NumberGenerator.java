package com.programmers.java.baseball.engine.io;


import com.programmers.java.baseball.engine.model.Numbers;

public interface NumberGenerator {
    // random을 통해서 만들 수도 있고 javaFaker를 통해서도 가능!! 헐
    // 하지만 engine은 핵심 로직이다.
    // 핵심 로직은 어떤 외부 dependency로직도 안가져가는 게 좋다.
    // 그럼 Numbers generate를 인터페이스로 만들자
    Numbers generate(int count);
}

    // BaseBall쪽에서 구현할 의무가 사라짐