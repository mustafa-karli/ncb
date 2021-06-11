package com.nauticana.ncb.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface INlbHandler {

    public ResponseEntity<String> list(Map<String,String> requestParameters);

    public ResponseEntity<String> getAll(Map<String,String> requestParameters);

    public ResponseEntity<String> get(Map<String,String> requestParameters);

    public ResponseEntity<String> delete(Map<String,String> requestParameters);

    public ResponseEntity<String> update(Map<String,String> requestParameters, String requestBody);

    public ResponseEntity<String> classes(Map<String,String> requestParameters);

    public ResponseEntity<String> tableDefinition(Map<String,String> requestParameters);

    public ResponseEntity<String> relations();

    public ResponseEntity<String> jauntPaths();

    public ResponseEntity<String> jauntPath(Map<String,String> requestParameters);

    public ResponseEntity<String> basePath(Map<String,String> requestParameters);

    public ResponseEntity<String> firstLevelPath(Map<String,String> requestParameters);

	public ResponseEntity<String> login(Map<String,String> requestParameters);

}
