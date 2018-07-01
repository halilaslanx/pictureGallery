package pictureGallery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PictureGallery {
	private List<String> images = new ArrayList<>();

	private String imageDirStr = "images/";
	private String pageTitle = "My Image Gallery";
	private String titleBackground = "_banner.jpg";
	private int imagesPerRow = 4;

	private String pageInit;
	private String pageEnd;
	
	public PictureGallery(String imageDirStr, int imagesPerRow, String pageTitle, String titleBackground) {
		this.imageDirStr = imageDirStr;
		this.imagesPerRow = imagesPerRow;
		this.pageTitle = pageTitle;
		this.titleBackground = titleBackground;
	}
	
	private void initPage() {
		this.pageInit = 
				"<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"	<head>\n" + 	
				// ideally you wouldn't use inline CSS and put your CSS blocks here
				"		<title>Image Gallery</title>\n" + 
				"	</head>\n" + 
				"	<body>\n" + 
				"		<center>\n" + 
				"			<div style=\"text-align: center;\n" + 
				"						vertical-align: middle;\n" + 
				"						line-height: 100px;\n" + 
				"						width: 90%;\n" + 
				"						background-image:url('" + imageDirStr + "/" + titleBackground + "');\">\n" + 
				"      			<center style=\"color:#ffffff; font-size: 35px\">\n" + 
				"        			" + pageTitle + "\n" + 
				"      			</center>\n" + 
				"    		</div>\n" + 
				"    		<br>\n" +
				"	<table style=\"width:90%; font-family:'Verdana'; font-size: 10px \">\n"
				;	
	
		this.pageEnd = 
				"	</table>\n" + 
				"		</center>\n" +
				"	</body>\n" + 
				"</html>"
				;
	}
	
	public void buildPage() {
		initPage();

		StringBuilder webPage = new StringBuilder();
		webPage.append(pageInit);
		
		File imageDir = new File(imageDirStr + ".");
		getAllFiles(imageDir);
		
		Iterator<String> it = images.iterator();
		
		while(it.hasNext()) {
			// load this line to a temp Array
			// because we will use it two times (images and image names rows)
			List<String> thisLine = new ArrayList<>();
			for(int i=0; i < imagesPerRow; i++) {
				if(it.hasNext())
					thisLine.add(it.next());
			}

			webPage.append("		<tr>\n");
			for (String thisImage : thisLine) {
				// each cell of the table has to be 1/n of the table row (n: imagesPerLine)
				webPage.append("			<td width = " + 100/imagesPerRow + "% align = 'center'>\n");
				webPage.append("				<a href= \'" + imageDirStr + thisImage + "'>\n");
				//webPage.append("<a href= \'" + imageDirStr + str + "' target=\"_blank\">\n");
				webPage.append("					<img width = 95% src=\'" + imageDirStr + thisImage +"'>\n");
				webPage.append("				</a>\n");
				webPage.append("			</td>\n");
			}
			webPage.append("		</tr>\n");

			webPage.append("		<tr>\n");
			for (String thisImage : thisLine) {
				// shorten the image name if it's too long
				if(thisImage.length()>100/imagesPerRow)
					thisImage = thisImage.substring(0, 100/imagesPerRow - 2) + "...";
				webPage.append("			<td width = " + 100/imagesPerRow + "% align = 'center' >");
				webPage.append(thisImage+"</td>\n");
			}
			webPage.append("		</tr>\n"); 

			webPage.append("		<tr><td><br></td></tr> \n");
		}
	
		webPage.append(pageEnd);
		
		// you don't really need BufferedWriter in this project
		// try with resources closes the file (guarantees the file is written
		// and not locked)
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("index.html"))) {
			bw.write(webPage.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getAllFiles(File directory) {
		File[] dirItems = directory.listFiles();
		for (File item : dirItems) {
			if (item.isFile())
				images.add(item.getName());
			if (item.isDirectory())	// if we want to include subfolders
				getAllFiles(item);
		}
	}

	public static void main(String[] args) {
		PictureGallery pg = new PictureGallery("images/", 4, "My Image Gallery", "_banner.jpg");
		pg.buildPage();
	}
}
