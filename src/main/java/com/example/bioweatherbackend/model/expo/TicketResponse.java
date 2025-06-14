package com.example.bioweatherbackend.model.expo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public final class TicketResponse extends ExpoBaseResponse<List<TicketResponse.Ticket>> {

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class Ticket extends ExpoBaseResponse.GenericData {

    public enum Error {
      @JsonProperty("DeviceNotRegistered")
      DEVICE_NOT_REGISTERED,
      @JsonProperty("InvalidCredentials")
      INVALID_CREDENTIALS;
    }

    @Data
    public static class Details {
      private Error error;
      private Integer sentAt;
      private JsonNode additionalProperties;
      private String expoPushToken;
    }

    private String id;
    private Status status;
    private String message;
    private Details details;
  }

  private List<TicketResponse.Ticket> data;
}
