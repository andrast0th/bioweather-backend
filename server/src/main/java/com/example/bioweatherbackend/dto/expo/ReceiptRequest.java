package com.example.bioweatherbackend.dto.expo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
public final class ReceiptRequest {

  @NonNull private final List<String> ids;
}
