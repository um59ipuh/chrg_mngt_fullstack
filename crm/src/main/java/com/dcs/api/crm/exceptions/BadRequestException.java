package com.dcs.api.crm.exceptions;

public class BadRequestException extends RuntimeException {

  private static final long serialVersionUID = 7119061997419521530L;

  public BadRequestException(String code) {
    super(code);
  }
}
