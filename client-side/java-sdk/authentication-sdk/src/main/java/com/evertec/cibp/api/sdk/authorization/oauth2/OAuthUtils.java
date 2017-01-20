package com.evertec.cibp.api.sdk.authorization.oauth2;
	
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.AUTHORIZATION;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.CLIENT_GRANT_METHOD;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.CLIENT_ID;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.CLIENT_SECRET;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.CONTENT_TYPE;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.GRANT_TYPE;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.JSON_CONTENT;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.METHOD_GET;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.METHOD_POST;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.PASSWORD;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.RESOURCE_OWNER_GRANT;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.SCOPE;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.URL_ENCODED_CONTENT;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.USERNAME;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.evertec.cibp.api.sdk.authorization.client.OAuth2Client;
import com.evertec.cibp.api.sdk.authorization.util.JsonConverter;
import com.evertec.cibp.api.sdk.common.model.JsonResponse;
import com.evertec.cibp.api.sdk.common.model.security.Token;
	
public class OAuthUtils {
		
		public static JsonResponse getProtectedResource(OAuth2Client client, Token token, String path) {
			
			String response = "";
			URL obj;
			JsonResponse jsonResponse;
			
			try {
			
				obj = new URL(path);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				
				con.setRequestMethod(METHOD_GET);
				con.setRequestProperty(CONTENT_TYPE, JSON_CONTENT);
		        con.setRequestProperty(AUTHORIZATION, 
		        		getAuthorizationHeaderForAccessToken(token.getAccessToken()));
		        		        
		        if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
		        	
		    		response = getStreamAsString(con.getInputStream());
		    		
		    		jsonResponse = new JsonResponse(response);
		    		
		    		
	            }  else {
	            	jsonResponse = new JsonResponse(con.getResponseCode(), con.getResponseMessage(), null);
	            }
			
			} catch (MalformedURLException e) {
				jsonResponse = responseWhitException(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				jsonResponse = responseWhitException(e.getMessage());
				e.printStackTrace();
			}
			
			
			return jsonResponse;
		}
		
		private static JsonResponse responseWhitException(String message) {
			return new JsonResponse(null, null, message);
		}
		
		

//		public static String getProtectedResource(OAuth2Client client, Token token, String path) {
//			
//			String resourceURL = /*client.getSite() +*/ path;
//			
//			HttpGet get = new HttpGet(resourceURL);
//			
//			get.addHeader(OAuthConstants.AUTHORIZATION,
//					getAuthorizationHeaderForAccessToken(token
//							.getAccessToken()));
//			
//			URLConnection httpClient = new URLConnection();
//			
//			HttpResponse response = null;
//			
//			String responseString = "";
//			
//			int code = -1;
//			
//			try {
//				response = httpClient.execute(get);
//				code = response.getStatusLine().getStatusCode();
//				if (code >= 400) {
//					throw new RuntimeException(
//									"Could not access protected resource. Server returned http code: "
//											+ code);
//						}
//
//				HttpEntity entity = response.getEntity();
//				responseString = EntityUtils.toString(entity, "UTF-8");
//				System.out.println(responseString);
//				//handleResponse(response);
//
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					response.getEntity().consumeContent();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}	
//			}
//			return responseString;
//		}
		
		public static Token getAccessToken(OAuth2Config oauthDetails) {
			
			Token token = null;
			
			String clientId = oauthDetails.getClientId();
			String clientSecret = oauthDetails.getClientSecret();
			String scope = oauthDetails.getScope();
			String grantMethod = oauthDetails.getGrantMethod();
			
			URL obj;
			
			try {
				
				String response = "";
				
				//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 2222));
			
				obj = new URL(oauthDetails.getTokenEndPointUrl());
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				
				con.setDoOutput(true);
				con.setDoInput(true);
				
				con.setRequestMethod(METHOD_POST);
				con.setRequestProperty(CONTENT_TYPE, URL_ENCODED_CONTENT);
		        con.setRequestProperty(GRANT_TYPE, oauthDetails.getGrantType());
		        
		        
		        if (isValid(scope)) {
					con.setRequestProperty(SCOPE, scope);
				}
		        
				
				if (CLIENT_GRANT_METHOD == grantMethod) {
					
					con.setRequestProperty(AUTHORIZATION, 
	            			getBasicAuthorizationHeader(oauthDetails.getClientId(),
									oauthDetails.getClientSecret()));
				
				} else if (RESOURCE_OWNER_GRANT == grantMethod) {
					
					if (isValid(clientId)) {
			        	con.setRequestProperty(CLIENT_ID, clientId);
					}
					
					if (isValid(clientSecret)) {
						con.setRequestProperty(CLIENT_SECRET, clientSecret);
					}
					
					
					con.setRequestProperty(USERNAME, oauthDetails.getUsername());
			        con.setRequestProperty(PASSWORD, oauthDetails.getPassword());
			        
					con.setRequestProperty(AUTHORIZATION, 
	            			getBasicAuthorizationHeader(oauthDetails.getUsername(),
									oauthDetails.getPassword()));
				}
				
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes("grant_type=client_credentials&scope=api_access");
				wr.flush();
				wr.close();
				
				System.out.println("con.getResponseCode() " + con.getResponseCode() + 
						" con.getResponseMessage() " + con.getResponseMessage());
		        		        
		        if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
		    		response = getStreamAsString(con.getInputStream());
		    		
		    		System.out.println(response);
		    		
		    		token =  JsonConverter.getJsonAsToken(response);
	            } 
			
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return token;
		}
		
//		public static Token getAccessToken(OAuth2Config oauthDetails) {
//			HttpPost post = new HttpPost(oauthDetails.getTokenEndPointUrl());
//			String clientId = oauthDetails.getClientId();
//			String clientSecret = oauthDetails.getClientSecret();
//			String scope = oauthDetails.getScope();
//
//			List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
//			parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE,
//					oauthDetails.getGrantType()));
//			parametersBody.add(new BasicNameValuePair(OAuthConstants.USERNAME,
//					oauthDetails.getUsername()));
//			parametersBody.add(new BasicNameValuePair(OAuthConstants.PASSWORD,
//					oauthDetails.getPassword()));
//
//			if (isValid(clientId)) {
//				parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID,
//						clientId));
//			}
//			if (isValid(clientSecret)) {
//				parametersBody.add(new BasicNameValuePair(
//						OAuthConstants.CLIENT_SECRET, clientSecret));
//			}
//			if (isValid(scope)) {
//				parametersBody.add(new BasicNameValuePair(OAuthConstants.SCOPE,
//						scope));
//			}
//
//			DefaultHttpClient client = new DefaultHttpClient();
//			HttpResponse response = null;
//			Token accessToken = null;
//			try {
//				post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));
//
//				response = client.execute(post);
//				int code = response.getStatusLine().getStatusCode();
//				if (code >= 400) {
//					System.out
//							.println("Authorization server expects Basic authentication");
//					// Add Basic Authorization header
//					post.addHeader(
//							OAuthConstants.AUTHORIZATION,
//							getBasicAuthorizationHeader(oauthDetails.getUsername(),
//									oauthDetails.getPassword()));
//					System.out.println("Retry with login credentials");
//					
//					try {
//						response.getEntity().consumeContent();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}	
//
//					response = client.execute(post);
//					code = response.getStatusLine().getStatusCode();
//					if (code >= 400) {
//						System.out.println("Retry with client credentials");
//						post.removeHeaders(OAuthConstants.AUTHORIZATION);
//						post.addHeader(
//								OAuthConstants.AUTHORIZATION,
//								getBasicAuthorizationHeader(
//										oauthDetails.getClientId(),
//										oauthDetails.getClientSecret()));
//
//						try {
//							response.getEntity().consumeContent();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}	
//
//						response = client.execute(post);
//						code = response.getStatusLine().getStatusCode();
//						if (code >= 400) {
//							throw new RuntimeException(
//									"Could not retrieve access token for user: "
//											+ oauthDetails.getUsername());
//						}
//					}
//
//				}
//				Map<String, Object> map = handleResponse(response);
//				accessToken = new Token(new Long((Integer) map.get(OAuthConstants.EXPIRES_IN)), (String) map.get(OAuthConstants.TOKEN_TYPE), (String) map.get(OAuthConstants.REFRESH_TOKEN), (String) map.get(OAuthConstants.ACCESS_TOKEN));
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			return accessToken;
//		}

		public static Token refreshAccessToken(Token token, OAuth2Config oauthDetails) {
			return null;
		}
		
//		public static Token refreshAccessToken(Token token, OAuth2Config oauthDetails) {
//			HttpPost post = new HttpPost(oauthDetails.getTokenEndPointUrl());
//			String clientId = oauthDetails.getClientId();
//			String clientSecret = oauthDetails.getClientSecret();
//
//			List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
//			parametersBody.add(new BasicNameValuePair(GRANT_TYPE,
//					REFRESH_TOKEN_GRANT));
//			parametersBody.add(new BasicNameValuePair(REFRESH_TOKEN,
//					token.getRefreshToken()));
//			
//
//			if (isValid(clientId)) {
//				parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID,
//						clientId));
//			}
//			if (isValid(clientSecret)) {
//				parametersBody.add(new BasicNameValuePair(
//						OAuthConstants.CLIENT_SECRET, clientSecret));
//			}
//
//			DefaultHttpClient client = new DefaultHttpClient();
//			HttpResponse response = null;
//			Token accessToken = null;
//			try {
//				post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));
//
//				response = client.execute(post);
//				System.out.println(response.getStatusLine().getStatusCode());
//				int code = response.getStatusLine().getStatusCode();
//					if (code >= 400) {
//						System.out.println("Retry with client credentials");
//						post.removeHeaders(OAuthConstants.AUTHORIZATION);
//						post.addHeader(
//								OAuthConstants.AUTHORIZATION,
//								getBasicAuthorizationHeader(
//										oauthDetails.getClientId(),
//										oauthDetails.getClientSecret()));
//
//						try {
//							response.getEntity().consumeContent();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}	
//
//						
//						response = client.execute(post);
//						code = response.getStatusLine().getStatusCode();
//						if (code >= 400) {
//							throw new RuntimeException(
//									"Could not retrieve access token for user: "
//											+ oauthDetails.getUsername());
//						}
//					}
//
//					Map<String, Object> map = handleResponse(response);
//					accessToken = new Token(new Long((Integer) map.get(OAuthConstants.EXPIRES_IN)), (String) map.get(OAuthConstants.TOKEN_TYPE), (String) map.get(OAuthConstants.REFRESH_TOKEN), (String) map.get(OAuthConstants.ACCESS_TOKEN));
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			return accessToken;
//		}
		
		public static Map handleResponse(HttpResponse response) {
			String contentType = OAuthConstants.JSON_CONTENT;
			if (response.getEntity().getContentType() != null) {
				contentType = response.getEntity().getContentType().getValue();
				System.out.println(response.getEntity().getContentType().getValue());
				
			}
			if (contentType.contains(OAuthConstants.JSON_CONTENT)) {
				return handleJsonResponse(response);
			} else if (contentType.contains(OAuthConstants.URL_ENCODED_CONTENT)) {
				return handleURLEncodedResponse(response);
			} else if (contentType.contains(OAuthConstants.XML_CONTENT)) {
				return handleXMLResponse(response);
			} else {
				// Unsupported Content type
				throw new RuntimeException(
						"Cannot handle "
								+ contentType
								+ " content type. Supported content types include JSON, XML and URLEncoded");
			}

		}

		public static Map handleJsonResponse(HttpResponse response) {
			JSONObject oauthLoginResponse = null;
			String contentType = response.getEntity().getContentType().getValue();
			try {
				oauthLoginResponse = new JSONObject(EntityUtils.toString(response.getEntity()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			System.out.println();
			System.out.println("********** JSON Response Received **********");
			
			Map<String, Object> outMap = new HashMap<String, Object>();
			Iterator<String> keysIterator = oauthLoginResponse.keys();
			while (keysIterator.hasNext()) 
			{
			        String keyStr = (String)keysIterator.next();
			        Object value = null;
					try {
						value = oauthLoginResponse.get(keyStr);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					outMap.put(keyStr,value);
			        System.out.println(String.format("  %s = %s", keyStr, value));
			}
				
			return outMap;
		}

		public static Map handleURLEncodedResponse(HttpResponse response) {
			Map<String, Charset> map = Charset.availableCharsets();
			Map<String, String> oauthResponse = new HashMap<String, String>();
			Set<Map.Entry<String, Charset>> set = map.entrySet();
			Charset charset = null;
			HttpEntity entity = response.getEntity();

			System.out.println();
			System.out.println("********** URL Encoded Response Received **********");

			for (Map.Entry<String, Charset> entry : set) {
				System.out.println(String.format("  %s = %s", entry.getKey(),
						entry.getValue()));
				if (entry.getKey().equalsIgnoreCase(HTTP.UTF_8)) {
					charset = entry.getValue();
				}
			}

			try {
				List<NameValuePair> list = URLEncodedUtils.parse(entity);
				for (NameValuePair pair : list) {
					System.out.println(String.format("  %s = %s", pair.getName(),
							pair.getValue()));
					oauthResponse.put(pair.getName(), pair.getValue());
				}

			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not parse URLEncoded Response");
			}

			return oauthResponse;
		}

		public static Map handleXMLResponse(HttpResponse response) {
			Map<String, String> oauthResponse = new HashMap<String, String>();
			try {

				String xmlString = EntityUtils.toString(response.getEntity());
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = factory.newDocumentBuilder();
				InputSource inStream = new InputSource();
				inStream.setCharacterStream(new StringReader(xmlString));
				Document doc = db.parse(inStream);

				System.out.println("********** XML Response Received **********");
				parseXMLDoc(null, doc, oauthResponse);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(
						"Exception occurred while parsing XML response");
			}
			return oauthResponse;
		}

		public static void parseXMLDoc(Element element, Document doc,
				Map<String, String> oauthResponse) {
			NodeList child = null;
			if (element == null) {
				child = doc.getChildNodes();

			} else {
				child = element.getChildNodes();
			}
			for (int j = 0; j < child.getLength(); j++) {
				if (child.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					org.w3c.dom.Element childElement = (org.w3c.dom.Element) child
							.item(j);
					if (childElement.hasChildNodes()) {
						System.out.println(childElement.getTagName() + " : "
								+ childElement.getTextContent());
						oauthResponse.put(childElement.getTagName(),
								childElement.getTextContent());
						parseXMLDoc(childElement, null, oauthResponse);
					}

				}
			}
		}

		public static String getAuthorizationHeaderForAccessToken(String accessToken) {
			return OAuthConstants.BEARER + " " + accessToken;
		}

		public static String getBasicAuthorizationHeader(String username,
				String password) {
			return OAuthConstants.BASIC + " "
					+ encodeCredentials(username, password);
		}

		public static String encodeCredentials(String username, String password) {
			String cred = username + ":" + password;
			String encodedValue = null;
			byte[] encodedBytes = Base64.encodeBase64(cred.getBytes());
			encodedValue = new String(encodedBytes);
			System.out.println("encodedBytes " + new String(encodedBytes));

			byte[] decodedBytes = Base64.encodeBase64(encodedBytes);
			System.out.println("decodedBytes " + new String(decodedBytes));

			return encodedValue;

		}

		public static boolean isValid(String str) {
			return (str != null && str.trim().length() > 0);
		}
		
		private static String getStreamAsString(InputStream inputStream) {
			
			BufferedReader in = new BufferedReader(
    		        new InputStreamReader(inputStream));
			
			StringBuffer responseBuffer = new StringBuffer();

    		try {
    			
    			String inputLine;
    			
				while ((inputLine = in.readLine()) != null) {
					responseBuffer.append(inputLine);
				}
				
				in.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
    		
    		return responseBuffer.toString();
    		
		}

}
