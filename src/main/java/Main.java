import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;
import java.util.List;

public class Main {
    public static final String REMOTE_SERVICE_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        try(CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("Get rating about cats")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build()) {

            // создание объекта запроса
            HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

            // отправка запроса
            CloseableHttpResponse response = httpClient.execute(request);
            List<InfoAboutCats> infoAboutCats = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<InfoAboutCats>>() {});
            infoAboutCats.stream()
                    .filter(value -> value.getUpvotes() > 0)
                    .sorted((cat1, cat2) -> cat2.getUpvotes() - cat1.getUpvotes())
                    .forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
