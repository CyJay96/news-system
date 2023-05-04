package ru.clevertec.ecl.newsservice.integration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

@UtilityClass
public class WireMockRequestUtil {

    private final ObjectMapper mapper = new ObjectMapper();

    public void initRequest() throws IOException {
        stubFor(get(urlPathMatching("/api/v0/users/byUsername/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        WireMockRequestUtil.class.getClassLoader().getResourceAsStream("json/response.json"),
                                        defaultCharset()))));
    }

    public ResponseEntity<APIResponse<UserDtoResponse>> buildUserDtoResponse() throws IOException {
        UserDtoResponse userDtoResponse = mapper.readValue(Paths.get("src/test/resources/json/response.json").toFile(),
                UserDtoResponse.class);

        return APIResponse.of(
                "",
                "",
                HttpStatus.OK,
                userDtoResponse
        );
    }
}
