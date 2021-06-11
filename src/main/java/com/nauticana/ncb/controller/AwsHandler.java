//package com.nauticana.ncb.controller;
//
//import java.util.Map;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//
///*
// * Extends Nauticana Core Backend handler methods as AWS Lambda functions
// * Implements RequestHandler to export each method as single function
// * 
// */
//
//public class AwsHandler extends NlbHandler implements RequestHandler<Map<String,String>, ResponseEntity<String>> {
//
//	public static final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AwsHandler.class.getPackage().getName());
//
//    public AwsHandler() {
//        ctx.getAutowireCapableBeanFactory().autowireBean(this);
//    }
//
//    @Override
//	public ResponseEntity<String> handleRequest(Map<String, String> input, Context context) {
//		String s =
//				"ENVIRONMENT VARIABLES: " + System.getenv() + System.lineSeparator() +
//				"CONTEXT: " + context + System.lineSeparator() +
//				"EVENT: " + input + System.lineSeparator() +
//				"EVENT TYPE: " + input.getClass().toString();
//		context.getLogger().log(s);
//		return new ResponseEntity<String>(s, HttpStatus.OK);
//	}
//
//	public ResponseEntity<String> list(Map<String, String> requestParameters, Context context) {
//		return super.list(requestParameters);
//	}
//
//	public ResponseEntity<String> getAll(Map<String, String> requestParameters, Context context) {
//		return super.getAll(requestParameters);
//	}
//
//	public ResponseEntity<String> get(Map<String, String> requestParameters, Context context) {
//		return super.get(requestParameters);
//	}
//
//	public ResponseEntity<String> delete(Map<String, String> requestParameters, Context context) {
//		return super.delete(requestParameters);
//	}
//
//	public ResponseEntity<String> update(Map<String, String> requestParameters, Context context) {
//		String requestBody = requestParameters.get(DATA);
//		return super.update(requestParameters, requestBody);
//	}
//
//	public ResponseEntity<String> classes(Map<String, String> requestParameters, Context context) {
//		return super.classes(requestParameters);
//	}
//
//	public ResponseEntity<String> tableDefinition(Map<String, String> requestParameters, Context context) {
//		return super.tableDefinition(requestParameters);
//	}
//
//	public ResponseEntity<String> relations(Map<String, String> requestParameters, Context context) {
//		return super.relations();
//	}
//
//	public ResponseEntity<String> jauntPaths(Map<String, String> requestParameters, Context context) {
//		return super.jauntPaths();
//	}
//
//	public ResponseEntity<String> jauntPath(Map<String, String> requestParameters, Context context) {
//		return super.jauntPath(requestParameters);
//	}
//
//	public ResponseEntity<String> basePath(Map<String, String> requestParameters, Context context) {
//		return super.basePath(requestParameters);
//	}
//
//	public ResponseEntity<String> firstLevelPath(Map<String, String> requestParameters, Context context) {
//		return super.firstLevelPath(requestParameters);
//	}
//
//	public ResponseEntity<String> login(Map<String, String> requestParameters, Context context) {
//		return super.login(requestParameters);
//	}
//
//
//}
