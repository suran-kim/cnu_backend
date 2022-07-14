package com.programmers.java.collection;
import java.util.ArrayList;
import java.util.List; // List사용을 위해 import
import java.util.function.Consumer;
import java.util.function.Function;

// T타입의 리스트를 전달받는 Mycollection클래스
public class MyCollection<T> {
    private List<T> list;

    public MyCollection(List<T> list) {
        this.list = list;
    }
    // 기능만 있는 메소드 map() 구현!
    // return 타입은 자기자신.
    // T타입으로부터 U타입을 만들어낼 수 있는 fuction을 인자로 받겠다.
    // 클래스에서는 T밖에 모르는데 이 메서드에서 U가 나왔다 => 이 메서드에서만 사용되는 제너릭 타입
    // 메소드에서 사용한다고 public뒤에 <U>를 선언해줘야한다.
    // Function은 이미 존재하는 함수인터페이스이다.
    // 입력받은 T타입 리스트들을 U타입으로 바꾼다음 U타입으로만 이루어진 MyCollection을 리턴하는 기능
    public <U> MyCollection<U> map(Function<T, U> function) {
        List<U> newList = new ArrayList<>(); // List<U>타입의 참조변수 newList에 ArrayList<>()을 대입한다.

        foreach(d -> newList.add(function.apply(d))); //데이터가 하나 나오면 function.apply(d)로 연결시켜서 apply해준다.
                                            // (구체적인 apply의 기능은 호스트가 정의한다.)
                                            // Function은 T타입으로 받아 U타입을 리턴하는 함수형 인터페이스로 설정했으므로
                                            // U타입이 리턴된다. 리턴된 U타입을 ArrayList를 참조하는 참조변수 newList에 집어넣는다.
        return new MyCollection<>(newList);
    }

    public void foreach(Consumer<T> consumer) {
        for(int i = 0 ; i < list.size() ; i++) {
            // 여기서 뭔가를 한다.
            T data = list.get(i);
            consumer.accept(data);
        }
    }
}
