package com.citrya.sosshop;


import com.citrya.dao.DatabaseAccess;
import com.citrya.sosparse.ParseData;
import com.citrya.sosparse.ParseMatchData;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;



import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;




public class TestGoogle {



	private static  String encodeFileToBase64Binary(File file){
		String encodedfile = null;
		try {
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int)file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedfile;
	}
	//AIzaSyCTtg3tN8WwwVBvFf85PswsQN749bAl8ks
	public static void main(String[] args) throws Exception {
		// Instantiates a client4
		try{  String re = "";
		String url = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyCTtg3tN8WwwVBvFf85PswsQN749bAl8ks";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		// BufferedImage img = ImageIO.read(new File("c://users//uma//desktop//1.jpg"));
		String imgstr = encodeFileToBase64Binary(new File("E:\\UMA\\9.jpg"));

		//    imgstr = encodeFileToBase64Binary(img, "png");

		//add reuqest header
		con.setRequestMethod("POST");

		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

		String urlParameters = "{\n"
				+ "  \"requests\":[\n"
				+ "    {\n"
				+ "      \"image\":{\n"
				+ "        \"content\":\"" + imgstr + "\"\n"
				+ "      },\n"
				+ "      \"features\":[\n"
				+ "        {\n"
				+ "          \"type\":\"TEXT_DETECTION\",\n"
				+ "          \"maxResults\":1\n"
				+ "        }\n"
				+ "      ]\n"
				+ "    }\n"
				+ "  ]\n"
				+ "}";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		System.out.println("1");

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		System.out.println("12");
		while ((inputLine = in.readLine()) != null) {
			//System.out.println(inputLine);
			response.append(inputLine);
		}
		in.close();

		re = response.toString();
		
		String text = new DatabaseAccess().getTextValueOfImage(re);
		
		//String shopname = new DatabaseAccess().getShopName(text); //not using anymore because providing shopname as input
		//String shopname = "relay";
		String shopname = "maiyas";
		//String shopname = "shoppers stop";
		//String shopname = "jubilant";
		System.out.println("printing shop name: " +shopname);
		
		ParseData data = new ParseData(shopname.toLowerCase());
		String[] result = data.getResult(text);
		
		System.out.println("Invoice no : "+result[0]);
		System.out.println("Date : "+result[1]);
		System.out.println("Total Amount : "+result[2]);
		
		/*int result;
		if(shopname.equals("Maiyas"))
			result = 1;
		else
			result = 2;
		
		if(result==1)
		{
			//It must be Maiyas Restaurants
			
			String invoiceNo = new DatabaseAccess().getInvoice1(text);
	        String amount=new DatabaseAccess().getAmount1(text);
	        String date=new DatabaseAccess().getDate1(text);
			System.out.println("Invoice no :"+ invoiceNo);
			System.out.println("Total Amount :"+amount);
			System.out.println("Date :"+date);
			
		}
		
		else if(result==2)
		{
		
			// It must be FreshMenu.
			
		String invoiceNo = new DatabaseAccess().getInvoice2(text);
        String amount=new DatabaseAccess().getAmount2(text);
        String date=new DatabaseAccess().getDate2(text);
		System.out.println("Invoice no :"+ invoiceNo);
		System.out.println("Total Amount :"+amount);
		System.out.println("Date :"+date);
		}*/

		try(  PrintWriter out = new PrintWriter( "E:\\UMA\\9.txt" )  ){
			out.println(text);
		}
		Thread.sleep(10000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


		//  detectText("c:\\users\\uma\\desktop\\1.jpg","c:\\users\\uma\\desktop\\1re.txt");
		/**try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

      // The path to the image file to annotate
      String fileName = "./resources/wakeupcat.jpg";

      // Reads the image file into memory
      Path path = Paths.get(fileName);
      byte[] data = Files.readAllBytes(path);
      ByteString imgBytes = ByteString.copyFrom(data);

      // Builds the image annotation request
      List<AnnotateImageRequest> requests = new ArrayList<>();
      Image img = Image.newBuilder().setContent(imgBytes).build();
      Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
      AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
          .addFeatures(feat)
          .setImage(img)
          .build();
      requests.add(request);

      // Performs label detection on the image file
      BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
      List<AnnotateImageResponse> responses = response.getResponsesList();

      for (AnnotateImageResponse res : responses) {
        if (res.hasError()) {
          System.out.printf("Error: %s\n", res.getError().getMessage());
          return;
        }

        for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
          Object k;
		//Object v;
		//annotation.getAllFields().forEach((k, v)->
       //       System.out.printf("%s : %s\n", k, v.toString()));
        }
      }
    }**/
	}

	public static void detectText(String filePath, PrintStream out) throws Exception, IOException {
		List<AnnotateImageRequest> requests = new ArrayList<>();

		ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
		AnnotateImageRequest request =
				AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
					out.printf("Error: %s\n", res.getError().getMessage());
					return;
				}

				// For full list of available annotations, see http://g.co/cloud/vision/docs
				for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
					out.printf("Text: %s\n", annotation.getDescription());
					out.printf("Position : %s\n", annotation.getBoundingPoly());
				}
			}
		}
	}





}

