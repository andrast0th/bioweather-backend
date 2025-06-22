package com.example.bioweatherbackend.dto.expo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/** Reponse including receipts for tickets. */
@Data
@EqualsAndHashCode(callSuper = false)
public final class ReceiptResponse extends ExpoBaseResponse<Map<String, ReceiptResponse.Receipt>> {

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class Receipt extends ExpoBaseResponse.GenericData {

    @Data
    public static class Details {

      public enum Error {
        @JsonProperty("DeviceNotRegistered")
        DEVICE_NOT_REGISTERED,

        @JsonProperty("MessageTooBig")
        MESSAGE_TOO_BIG,

        @JsonProperty("MessageRateExceeded")
        MESSAGE_RATE_EXCEEDED,

        @JsonProperty("InvalidCredentials")
        INVALID_CREDENTIALS,

        @JsonProperty("InvalidProviderToken")
        INVALID_PROVIDERTOKEN
      }

      private Error error;
      private Integer sentAt;
      private String errorCodeEnum;
      private JsonNode additionalProperties;
    }

    private Status status;
    private String message;
    private Details details;
  }

  private Map<String, Receipt> data;
}
