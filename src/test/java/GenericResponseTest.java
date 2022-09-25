import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class GenericResponseTest {

    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }

    abstract static private class Response{

    }

    @Data
    @NoArgsConstructor
    private static class Photo extends Response{
        Integer albumId;
        Integer id;
        String title;
        String url;
        String thumbnailUrl;
    }

    @Data
    @NoArgsConstructor
    private static class Todo extends Response{
        Integer userId;
        Integer id;
        String title;
        String completed;
    }

    public Photo requestPhoto() {
        Photo result = getWebClient().get()
                .uri("/photos/1")
                .retrieve()
                .bodyToMono(Photo.class)
                .block();

        return result;
    }

    public Todo requestTodo() {
        Todo result = getWebClient().get()
                .uri("/todos/1")
                .retrieve()
                .bodyToMono(Todo.class)
                .block();

        return result;
    }


    @Test
    public void test(){
        assertEquals(requestPhoto().getTitle(),"accusamus beatae ad facilis cum similique qui sunt");
        assertEquals(requestTodo().getTitle(),"delectus aut autem");
    }


    public <T extends Response> T requestApi(String url, Class<T> classType) {
        T result = (T) getWebClient().get()
                .uri("/{url}/1",url)
                .retrieve()
                .bodyToMono(classType)
                .block();

        return (T)result;
    }

    @Test
    public void test2(){
        assertEquals(requestApi("photos", Photo.class).getTitle(),"accusamus beatae ad facilis cum similique qui sunt");
        assertEquals(requestApi("todos", Todo.class).getTitle(),"delectus aut autem");
    }




}
