import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import junit.framework.TestCase;
import submission.hw5.WikiCrawler;

@RunWith(JUnit4.class)
public class HomeWork5Test extends TestCase{
	
	// Enter student name here before you run the tests
	static String studentName = "Test Student";
	
	// we use an error collector so that we can check all the asserts in a test before exiting. Otherwise, the
	// test would exit on first failure
	@Rule
    public ErrorCollector collector = new ErrorCollector();
	
	// TODO: the paths are windows file path format. If you use a Mac or a linux, please change them accordingly
	// this is the directory where the project is stored in the computer. This needs to be changed as on each computer
	private static final String PROJECT_DIRECTORY = "C://Users//Sriram//Desktop//Study//Workspace//311HWTest//";
	
	static StringBuilder commentsBuilder = new StringBuilder("");
	
	static  int totalPoints = 135;
	
	/*Path currentRelativePath = Paths.get("");
	String s = currentRelativePath.toAbsolutePath().toString();
	System.out.println("Current relative path is: " + s);*/
	
	public static final String EXTRACT_LINKS_INPUT_LINK_BEFORE_P_TAG = "<html><body> <a href=\"/wiki/NotValid.html\"> Link before p tag </a> <p> Sample body </p></body></html>";
	public static final String EXTRACT_LINKS_INPUT_NON_WIKI_LINK = "<html><body><p> Sample body </p> <a href=\"/NotValid.html\"> Invalid Link after p tag </a> </body></html>";
	public static final String EXTRACT_LINKS_INPUT_LINK_WITH_HASH = "<html><body><p> Sample body </p> <a href=\"/wiki/NotValid.html#hashTag\"> Invalid Link after p tag </a> </body></html>";
	public static final String EXTRACT_LINKS_INPUT_LINK_WITH_COLON = "<html><body><p> Sample body </p> <a href=\"/wiki/NotValid.html:colon\"> Invalid Link after p tag </a> </body></html>";
	public static final String EXTRACT_LINKS_INPUT_ABSOLUTE_URI = "<html><body><p> Sample body </p> <a href=\"http://en.wikipedia.org/wiki/NotValid.html\"> Invalid Link after p tag </a> </body></html>";
	//public static final String EXTRACT_LINKS_INPUT_CORRECTNESS_TEST = "<html><body><p> Sample body </p> <a href=\"/wiki/Valid.html\" title=\"Valid link\"> Valid Link after p tag </a> </body></html>";
	public static final String EXTRACT_LINKS_INPUT_CORRECTNESS_TEST = "<p> <a href=\"/wiki/B.html\" title=\"Valid link\">Link to page B</a> </p>	</body></html";
	
	@Test
	public void testBaseURIConstant() {
		try {
			Class crawlerClass = Class.forName("submission.hw5.WikiCrawler");
			Field baseUrlField = crawlerClass.getField("BASE_URL");
		} catch (Exception e) {
			commentsBuilder.append("No field named BASE_URL in WikiCrawler(0 points). ");
			totalPoints = 0;
			Assert.fail("No field named BASE_URL");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testExtractLinksInvalidLink1() {
		WikiCrawler crawler = new WikiCrawler("/wiki/Computer_Science", 100, "WikiCS.txt");
		String[] links = crawler.extractLinks(EXTRACT_LINKS_INPUT_LINK_BEFORE_P_TAG);
		if(links.length != 0){
			totalPoints -= 5;
			commentsBuilder.append("Your program extracted a link before the first <p> tag(-5 points). ");
			Assert.fail("Incorrect link extracted");
		}
	}
	
	@Test
	public void testExtractLinksInvalidLink2() {
		WikiCrawler crawler = new WikiCrawler("/wiki/Computer_Science", 100, "WikiCS.txt");
		String[] links = crawler.extractLinks(EXTRACT_LINKS_INPUT_NON_WIKI_LINK);
		if(links.length != 0){
			totalPoints -= 5;
			commentsBuilder.append("Your program extracted a link without the proper prefix(/wiki/)(-5 points). ");
			Assert.fail("Incorrect link extracted");
		}
	}
	
	@Test
	public void testExtractLinksInvalidLink3() {
		WikiCrawler crawler = new WikiCrawler("/wiki/Computer_Science", 100, "WikiCS.txt");
		String[] links = crawler.extractLinks(EXTRACT_LINKS_INPUT_LINK_WITH_HASH);
		if(links.length != 0){
			totalPoints -= 5;
			commentsBuilder.append("Your program extracted a link with a hashtag(-5 points). ");
			Assert.fail("Incorrect link extracted");
		}
	}
	
	@Test
	public void testExtractLinksInvalidLink4() {
		WikiCrawler crawler = new WikiCrawler("/wiki/Computer_Science", 100, "WikiCS.txt");
		String[] links = crawler.extractLinks(EXTRACT_LINKS_INPUT_LINK_WITH_COLON);
		if(links.length != 0){
			totalPoints -= 5;
			commentsBuilder.append("Your program extracted a link with a colon(-5 points). ");
			Assert.fail("Incorrect link extracted");
		}
	}
	
	@Test
	public void testExtractLinksValidLink() {
		WikiCrawler crawler = new WikiCrawler("/wiki/Computer_Science", 100, "WikiCS.txt");
		String[] links = crawler.extractLinks(EXTRACT_LINKS_INPUT_CORRECTNESS_TEST);
		if(links.length == 0 || links.length != 1 || !links[0].equals("/wiki/Valid.html")){
			totalPoints -= 5;
			commentsBuilder.append("Your program failed to extract a valid link(-5 points). ");
			Assert.fail("Incorrect link extracted");
		}
	}
	
	private List<String> getEdgesFromFile(String fileName) {
		File file = new File(fileName);
		file.getAbsolutePath();
		List<String> edges = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = null;
			while((line = reader.readLine()) != null) {
				edges.add(line);
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return edges;
	}
	
	@Test
	public void testGraph1() {
		WikiCrawler crawler = new WikiCrawler("/wiki/A.html", 100, "Graph1.txt");
		//crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph1.txt");
		if (!edges.contains("/wiki/A.html /wiki/B.html") 
				|| !edges.contains("/wiki/B.html /wiki/C.html") 
				|| !edges.contains("/wiki/C.html /wiki/A.html")) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly for a simple graph with a directional triange(-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}
	
	@Test
	public void testGraph2() {
		WikiCrawler crawler = new WikiCrawler("/wiki/D.html", 100, "Graph2.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph2.txt");
		if ((!edges.contains("/wiki/E.html /wiki/F.html") 
				|| !edges.contains("/wiki/D.html /wiki/E.html"))
				&& (edges.size() != 3)) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly when there are multiple links between pages(-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}
	
	@Test
	public void testGraph3() {
		WikiCrawler crawler = new WikiCrawler("/wiki/G.html", 100, "Graph3.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph3.txt");
		if ((!edges.contains("/wiki/G.html /wiki/H.html"))
				&& (edges.size() != 2)) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly when there are self links in pages(-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}
	
	@Test
	public void testGraph4() {
		WikiCrawler crawler = new WikiCrawler("/wiki/I.html", 100, "Graph4.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph4.txt");
		if (!edges.contains("/wiki/I.html /wiki/J.html") 
				|| !edges.contains("/wiki/J.html /wiki/I.html")) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly when there are links between two pages(-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}
	
	@Test
	public void testGraph5() {
		WikiCrawler crawler = new WikiCrawler("/wiki/K.html", 3, "Graph5.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph4.txt");
		if (!edges.contains("/wiki/K.html /wiki/M.html") 
				|| !edges.contains("/wiki/K.html /wiki/L.html")
				|| !edges.contains("/wiki/L.html /wiki/K.html")
				|| !edges.contains("/wiki/L.html /wiki/M.html")
				|| !edges.contains("/wiki/M.html /wiki/L.html")
				|| !edges.contains("/wiki/M.html /wiki/K.html")
				|| edges.contains("/wiki/M.html /wiki/N.html")
				|| edges.contains("/wiki/M.html /wiki/O.html")) {
			totalPoints -= 50;
			commentsBuilder.append("Web graph formed incorrectly. Did not stop or run till specified number of nodes(-50 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}
	
	
	
	@AfterClass
	public static void writeCommentsAndPoints(){
		try {
			FileWriter csvWriter = new FileWriter("points.csv", true);
			csvWriter.append(studentName + "," + totalPoints + "," + commentsBuilder.toString());
			csvWriter.append('\n');
			csvWriter.flush();
			csvWriter.close();
			System.out.println("Results added to points.csv");
		} catch (Exception e){
			System.out.println("Error writing to file. Write it yourself!");
			System.out.println("Result : " + studentName + "," + totalPoints + "," + commentsBuilder.toString());
		} 
	}
}
