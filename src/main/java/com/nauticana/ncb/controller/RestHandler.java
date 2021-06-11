package com.nauticana.ncb.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * Extends Nauticana Core Backend handlers without any additional implementation.
 * Methods are mapped to REST URLs
 */

@RestController
@RequestMapping("/rest")
public class RestHandler extends NlbHandler {

	@Value("nauticana.rest.rootMapping")
	public static String rootMapping;
	
	@Override
    @GetMapping("/list")
	public ResponseEntity<String> list(@RequestParam Map<String, String> requestParameters) {
		return super.list(requestParameters);
	}

	@Override
    @GetMapping("/getall")
	public ResponseEntity<String> getAll(@RequestParam Map<String, String> requestParameters) {
		return super.getAll(requestParameters);
	}

	@Override
    @GetMapping("/get")
	public ResponseEntity<String> get(@RequestParam Map<String, String> requestParameters) {
		return super.get(requestParameters);
	}

	@Override
    @DeleteMapping("/delete")
	public ResponseEntity<String> delete(@RequestParam Map<String, String> requestParameters) {
		return super.delete(requestParameters);
	}

	@Override
    @PostMapping("/put")
	public ResponseEntity<String> update(@RequestParam Map<String, String> requestParameters, @RequestBody String requestBody) {
		return super.update(requestParameters, requestBody);
	}

	@Override
    @GetMapping("/classes")
	public ResponseEntity<String> classes(@RequestParam Map<String, String> requestParameters) {
		return super.classes(requestParameters);
	}

	@Override
    @GetMapping("/class")
	public ResponseEntity<String> tableDefinition(@RequestParam Map<String, String> requestParameters) {
		return super.tableDefinition(requestParameters);
	}

	@Override
    @GetMapping("/relations")
	public ResponseEntity<String> relations() {
		return super.relations();
	}

	@Override
    @GetMapping("/jauntPaths")
	public ResponseEntity<String> jauntPaths() {
		return super.jauntPaths();
	}

	@Override
    @GetMapping("/jauntPath")
	public ResponseEntity<String> jauntPath(@RequestParam Map<String, String> requestParameters) {
		return super.jauntPath(requestParameters);
	}

	@Override
    @GetMapping("/basePath")
	public ResponseEntity<String> basePath(@RequestParam Map<String, String> requestParameters) {
		return super.basePath(requestParameters);
	}

	@Override
    @GetMapping("/firstLevelPath")
	public ResponseEntity<String> firstLevelPath(@RequestParam Map<String, String> requestParameters) {
		return super.firstLevelPath(requestParameters);
	}

	@Override
    @GetMapping(value = "/login")
	public ResponseEntity<String> login(@RequestParam Map<String, String> requestParameters) {
		return super.login(requestParameters);
	}

	

}
