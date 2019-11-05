package com.lyft.data.baseapp.auth;

import java.util.Set;

import lombok.Data;

@Data
public class AuthConfiguration {
  private String user;

  private Set<String> roles;

  private String password;
}
