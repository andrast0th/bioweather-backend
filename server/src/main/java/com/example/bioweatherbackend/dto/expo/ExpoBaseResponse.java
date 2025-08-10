package com.example.bioweatherbackend.dto.expo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class ExpoBaseResponse<T> {

  protected static class GenericData {

    /** Store unmapped data in case actual response is varying from specification. */
    private Map<String, JsonNode> any;

    @JsonAnyGetter
    public Map<String, JsonNode> getAny() {
      return any;
    }

    @JsonAnySetter
    public void addAny(String key, JsonNode value) {
      if (any == null) {
        any = new HashMap<>();
      }
      any.put(key, value);
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class Error extends GenericData {
    private String code;
    private String message;
  }

  public abstract T getData();

  private List<Error> errors;
}