package ru.clevertec.ecl.newsservice.integration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.newsservice.client.model.dto.UserDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.APIResponse;

import java.io.IOException;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

@UtilityClass
public class WireMockRequestUtil {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String USER_REQUEST_PATH = "/api/v0/users/byToken/.*";
    private static final String USER_JSON_RESPONSE_PATH = "src/test/resources/json/userResponse.json";

    private final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    public void initRequest() throws IOException {
        stubFor(get(urlPathMatching(USER_REQUEST_PATH))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(buildUserDtoResponse())));
    }

    private String buildUserDtoResponse() throws IOException {
        UserDtoResponse userDtoResponse = mapper.readValue(Paths.get(USER_JSON_RESPONSE_PATH).toFile(),
                UserDtoResponse.class);

        ResponseEntity<APIResponse<UserDtoResponse>> userApiResponse = APIResponse.of(
                "",
                USER_REQUEST_PATH,
                HttpStatus.OK,
                userDtoResponse
        );

        return mapper.writeValueAsString(userApiResponse.getBody());
    }
}
