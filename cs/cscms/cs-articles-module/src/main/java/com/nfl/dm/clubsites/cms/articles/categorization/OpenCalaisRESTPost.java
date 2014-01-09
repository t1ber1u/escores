package com.nfl.dm.clubsites.cms.articles.categorization;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author arash.shokoufandeh
 */
public class OpenCalaisRESTPost {
    private static final String CALAIS_URL = "http://api.opencalais.com/tag/rs/enrich";

    private String input;
    private HttpClient client;

    public Map<String, ArrayList<String>> execute(Node node) {
        OpenCalaisRESTPost openCalaisRESTPost = new OpenCalaisRESTPost();
        try {
            openCalaisRESTPost.input = node.getProperty("body").getString();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        openCalaisRESTPost.client = new HttpClient();
        openCalaisRESTPost.client.getParams().setParameter("http.useragent", "Calais Rest Client");

        return openCalaisRESTPost.run();
    }

    private Map<String, ArrayList<String>> run() {
        try {
            if (!input.isEmpty()) {
                return postString(input, createPostMethod());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, ArrayList<String>>();
    }

    private Map<String, ArrayList<String>> postString(String input, PostMethod method) throws IOException {
        method.setRequestEntity(new StringRequestEntity(input, null, null));
        return doRequest(input, method);
    }

    private Map<String, ArrayList<String>> doRequest(String input, PostMethod method) {
        ArrayList<String> person = new ArrayList<String>();
        ArrayList<String> organization = new ArrayList<String>();
        Map<String, ArrayList<String>> output = new HashMap<String, ArrayList<String>>();
        try {
            int returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                System.err.println("The Post method is not implemented by this URI");
                // still consume the response body
                method.getResponseBodyAsString();
            } else if (returnCode == HttpStatus.SC_OK) {
                System.out.println("Post succeeded: " + input);
                JSONObject json = (JSONObject) JSONSerializer.toJSON(method.getResponseBodyAsString());
                Iterator<?> keys = json.keys();
                while (keys.hasNext()){
                    String key = (String) keys.next();
                    if (json.get(key) instanceof JSONObject && !key.equals("doc")){
                        JSONObject jsonElement = (JSONObject) json.get(key);
                        if (jsonElement.get("_typeGroup").equals("entities")) {
                            if (jsonElement.get("_type").equals("Person")) {
                                person.add(jsonElement.get("name").toString());
                            } else if (jsonElement.get("_type").equals("Organization")) {
                                organization.add(jsonElement.get("name").toString());
                            }
                        }
                    }
                }
                output.put("person", person);
                output.put("organization", organization);
            } else {
                System.err.println("Post failed: " + input);
                System.err.println("Got code: " + returnCode);
                System.err.println("response: "+ method.getResponseBodyAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return output;
    }

    private PostMethod createPostMethod() {
        PostMethod method = new PostMethod(CALAIS_URL);

        // Set mandatory parameters
        method.setRequestHeader("x-calais-licenseID", "sfna2j7bwgdfsnpvvjyudz2q");

        // Set input content type
        //method.setRequestHeader("Content-Type", "text/raw; charset=UTF-8");
        //method.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");
        method.setRequestHeader("Content-Type", "text/html; charset=UTF-8");

        // Set response/output format
        //method.setRequestHeader("Accept", "xml/rdf");
        method.setRequestHeader("Accept", "application/json");

        // Enable Social Tags processing
        method.setRequestHeader("enableMetadataType", "SocialTags");

        return method;
    }
}
