package com.example.bioweatherbackend.dto.expo;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Status of push notification. */
public enum Status {
  @JsonProperty("ok")
  OK,

  @JsonProperty("error")
  ERROR
}
