package com.lyft.data.baseapp.auth;

import lombok.Data;

import java.util.Set;

@Data
public class AuthConfiguration {
  private String user;

  private Set<String> roles;

  private String password;
}