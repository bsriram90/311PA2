import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

@RunWith(JUnit4.class)
public class HomeWork5Test extends TestCase{
	
	// Enter student name here before you run the tests
	static String studentName = "Test Student";
	
	// we use an error collector so that we can check all the asserts in a test before exiting. Otherwise, the
	// test would exit on first failure
	@Rule
    public ErrorCollector collector = new ErrorCollector();
	
	static StringBuilder commentsBuilder = new StringBuilder("");

	// replace this with absolute path if running from command prompt
	static String projectDirectory = System.getProperty("user.dir");

	static String inputDirectory = projectDirectory + "/input/";

	static  int totalPoints = 400;

	public static final String EXTRACT_LINKS_INPUT_LINK_BEFORE_P_TAG = "<html><body> <a href=\"/wiki/NotValid.html\"> Link before p tag </a> <p> Sample body </p></body></html>";
	public static final String EXTRACT_LINKS_INPUT_NON_WIKI_LINK = "<html><body><p> Sample body </p> <a href=\"/NotValid.html\"> Invalid Link after p tag </a> </body></html>";
	public static final String EXTRACT_LINKS_INPUT_LINK_WITH_HASH = "<html><body><p> Sample body </p> <a href=\"/wiki/NotValid.html#hashTag\"> Invalid Link after p tag </a> </body></html>";
	public static final String EXTRACT_LINKS_INPUT_LINK_WITH_COLON = "<html><body><p> Sample body </p> <a href=\"/wiki/NotValid.html:colon\"> Invalid Link after p tag </a> </body></html>";
	public static final String EXTRACT_LINKS_INPUT_CORRECTNESS_TEST = "<html><body><p> Sample body </p> <a href=\"/wiki/Valid.html\" title=\"Valid link\"> Valid Link after p tag </a> </body></html>";

	// test extract link - start

	@Test
	public void testExtractLinksInvalidLink1() {
		WikiCrawler crawler = new WikiCrawler("/wiki/Computer_Science", 100, "WikiCS.txt");
		List<String> links = crawler.extractLinks(EXTRACT_LINKS_INPUT_LINK_BEFORE_P_TAG);
		if(links.size() != 0){
			totalPoints -= 5;
			commentsBuilder.append("Your program extracted a link before the first <p> tag(-5 points). ");
			collector.addError(new AssertionFailedError("Incorrect link extracted"));
			;
		}
	}

	@Test
	public void testExtractLinksInvalidLink2() {
		WikiCrawler crawler = new WikiCrawler("/wiki/Computer_Science", 100, "WikiCS.txt");
		List<String> links = crawler.extractLinks(EXTRACT_LINKS_INPUT_NON_WIKI_LINK);
		if(links.size() != 0){
			totalPoints -= 5;
			commentsBuilder.append("Your program extracted a link without the proper prefix(/wiki/)(-5 points). ");
			collector.addError(new AssertionFailedError("Incorrect link extracted"));
		}
	}

	@Test
	public void testExtractLinksInvalidLink3() {
		WikiCrawler crawler = new WikiCrawler("/wiki/Computer_Science", 100, "WikiCS.txt");
		List<String> links = crawler.extractLinks(EXTRACT_LINKS_INPUT_LINK_WITH_HASH);
		if(links.size() != 0){
			totalPoints -= 5;
			commentsBuilder.append("Your program extracted a link with a hashtag(-5 points). ");
			collector.addError(new AssertionFailedError("Incorrect link extracted"));
		}
	}

	@Test
	public void testExtractLinksInvalidLink4() {
		WikiCrawler crawler = new WikiCrawler("/wiki/Computer_Science", 100, "WikiCS.txt");
		List<String> links = crawler.extractLinks(EXTRACT_LINKS_INPUT_LINK_WITH_COLON);
		if(links.size() != 0){
			totalPoints -= 5;
			commentsBuilder.append("Your program extracted a link with a colon(-5 points). ");
			collector.addError(new AssertionFailedError("Incorrect link extracted"));
		}
	}

	@Test
	public void testExtractLinksValidLink() {
		WikiCrawler crawler = new WikiCrawler("/wiki/Computer_Science", 100, "WikiCS.txt");
		List<String> links = crawler.extractLinks(EXTRACT_LINKS_INPUT_CORRECTNESS_TEST);
		if(links.size() == 0 || links.size() != 1 || !links.get(0).equals("/wiki/Valid.html")){
			totalPoints -= 5;
			commentsBuilder.append("Your program failed to extract a valid link(-5 points). ");
			collector.addError(new AssertionFailedError("Incorrect link extracted"));
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
				if(!line.trim().equals("")){
					edges.add(line);
				}
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return edges;
	}

	// test extract link - end
	// test simple graph - start
	
	@Test
	public void testSimpleGraph1() {
		WikiCrawler crawler = new WikiCrawler("/wiki/A.html", 100, "Graph-S1.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-S1.txt");
		if (!edges.contains("/wiki/A.html /wiki/B.html") 
				|| !edges.contains("/wiki/B.html /wiki/C.html") 
				|| !edges.contains("/wiki/C.html /wiki/A.html")) {
			totalPoints -= 5;
			commentsBuilder.append("Web graph formed incorrectly for a simple graph with a directional triangle[Graph-1](-5 points). ");
			collector.addError(new AssertionFailedError("Web graph formed incorrectly"));
		}
	}

	@Test
	public void testSimpleGraph2() {
		WikiCrawler crawler = new WikiCrawler("/wiki/A1.html", 100, "Graph-S2.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-S2.txt");
		if (!edges.contains("/wiki/A1.html /wiki/B1.html")
				|| !edges.contains("/wiki/A1.html /wiki/C1.html")
				|| !edges.contains("/wiki/B1.html /wiki/C1.html")
				|| !edges.contains("/wiki/B1.html /wiki/A1.html")
				|| !edges.contains("/wiki/C1.html /wiki/A1.html")
				|| !edges.contains("/wiki/C1.html /wiki/B1.html")) {
			totalPoints -= 5;
			commentsBuilder.append("Web graph formed incorrectly for a simple fully connected graph[Graph-2](-5 points). ");
			collector.addError(new AssertionFailedError("Web graph formed incorrectly"));
		}
	}

	@Test
	public void testSimpleGraph3() {
		WikiCrawler crawler = new WikiCrawler("/wiki/A2.html", 100, "Graph-S3.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-S3.txt");
		if (!edges.contains("/wiki/A2.html /wiki/B2.html")
				|| !edges.contains("/wiki/A2.html /wiki/C2.html")
				|| !edges.contains("/wiki/B2.html /wiki/C2.html")) {
			totalPoints -= 5;
			commentsBuilder.append("Web graph formed incorrectly for a simple graph with 3 edges and 3 nodes[Graph-3](-5 points). ");
			collector.addError(new AssertionFailedError("Web graph formed incorrectly"));
		}
	}

	// test simple graph - end
	// test multiple edges - start

	@Test
	public void testMultipleEdges1() {
		WikiCrawler crawler = new WikiCrawler("/wiki/D.html", 100, "Graph-M1.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-M1.txt");
		if (hasDuplicateEdges(edges) || ((!edges.contains("/wiki/E.html /wiki/F.html")
				|| !edges.contains("/wiki/D.html /wiki/E.html")
				|| !edges.contains("/wiki/F.html /wiki/D.html"))
				&& (edges.size() != 4))) {
			totalPoints -= 5;
			commentsBuilder.append("Web graph formed incorrectly when there are multiple links between pages[Graph-4](-5 points). ");
			collector.addError(new AssertionFailedError("Web graph formed incorrectly"));
		}
	}

	@Test
	public void testMultipleEdges2() {
		WikiCrawler crawler = new WikiCrawler("/wiki/D1.html", 100, "Graph-M2.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-M2.txt");
		if (hasDuplicateEdges(edges) || ((!edges.contains("/wiki/E1.html /wiki/F1.html")
				|| !edges.contains("/wiki/D1.html /wiki/E1.html")
				|| !edges.contains("/wiki/F1.html /wiki/D1.html"))
				&& (edges.size() != 4))) {
			totalPoints -= 5;
			commentsBuilder.append("Web graph formed incorrectly when there are multiple links between pages[Graph-5](-5 points). ");
			collector.addError(new AssertionFailedError("Web graph formed incorrectly"));
		}
	}

	@Test
	public void testMultipleEdges3() {
		WikiCrawler crawler = new WikiCrawler("/wiki/D2.html", 100, "Graph-M3.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-M3.txt");
		if (hasDuplicateEdges(edges) || ((!edges.contains("/wiki/E2.html /wiki/F2.html")
				|| !edges.contains("/wiki/D2.html /wiki/E2.html"))
				&& (edges.size() != 3))) {
			totalPoints -= 5;
			commentsBuilder.append("Web graph formed incorrectly when there are multiple links between pages[Graph-6](-5 points). ");
			collector.addError(new AssertionFailedError("Web graph formed incorrectly"));
		}
	}
	
	private boolean hasDuplicateEdges(List<String> edges) {
		Set<String> bag = new HashSet<String>();
		for (String edge : edges){
			bag.add(edge);
		}
		if(bag.size() == edges.size()){
			return false;
		} else {
			return true;
		}
	}

	// test multiple edges - end
	// test self edges - start

	@Test
	public void testSelfEdges1() {
		WikiCrawler crawler = new WikiCrawler("/wiki/G.html", 100, "Graph-L1.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-L1.txt");
		if (hasSelfEdge(edges) ||
				((!edges.contains("/wiki/G.html /wiki/H.html")
						|| !edges.contains("/wiki/H.html /wiki/I.html"))
				&& (edges.size() != 3))) {
			totalPoints -= 5;
			commentsBuilder.append("Web graph formed incorrectly when there are self links in pages[Graph-7](-5 points). ");
			collector.addError(new AssertionFailedError("Web graph formed incorrectly"));
		}
	}

	@Test
	public void testSelfEdges2() {
		WikiCrawler crawler = new WikiCrawler("/wiki/G1.html", 100, "Graph-L2.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-L2.txt");
		if (hasSelfEdge(edges) ||
				((!edges.contains("/wiki/G1.html /wiki/H1.html")
						|| !edges.contains("/wiki/H1.html /wiki/I1.html"))
						&& (edges.size() != 3))) {
			totalPoints -= 5;
			commentsBuilder.append("Web graph formed incorrectly when there are self links in pages[Graph-8](-5 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}

	@Test
	public void testSelfEdges3() {
		WikiCrawler crawler = new WikiCrawler("/wiki/G2.html", 100, "Graph-L3.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-L3.txt");
		if (hasSelfEdge(edges) ||
				((!edges.contains("/wiki/G2.html /wiki/H2.html")
						|| !edges.contains("/wiki/H2.html /wiki/I2.html"))
						&& (edges.size() != 3))) {
			totalPoints -= 5;
			commentsBuilder.append("Web graph formed incorrectly when there are self links in pages[Graph-9](-5 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}
	
	private boolean hasSelfEdge(List<String> edges) {
		for(String edge : edges){
			String[] nodes = edge.split(" ");
			if(nodes.length == 2) {
				if( nodes[0].equals(nodes[1])) {
					return true;
				}
			}
		}
		return false;
	}

	// test self edges - end
	// test valid loop - start

	@Test
	public void testValidLoop() {
		WikiCrawler crawler = new WikiCrawler("/wiki/J.html", 100, "Graph-V1.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-V1.txt");
		if (!edges.contains("/wiki/J.html /wiki/K.html")
				|| !edges.contains("/wiki/K.html /wiki/J.html")) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly when there are links between two pages[Graph-10](-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}

	// test valid loop - end
	// test max nodes - start
	
	@Test
	public void testMaxNodes1() {
		WikiCrawler crawler = new WikiCrawler("/wiki/A.html", 2, "Graph-N1.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-N1.txt");
		if (!edges.contains("/wiki/A.html /wiki/B.html")) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly. Did not stop or run till specified number of nodes[Graph-1](-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}

	@Test
	public void testMaxNodes2() {
		WikiCrawler crawler = new WikiCrawler("/wiki/AA.html", 2, "Graph-N2.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-N2.txt");
		if (!edges.contains("/wiki/AA.html /wiki/BB.html")
				|| !edges.contains("/wiki/BB.html /wiki/AA.html")) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly. Did not stop or run till specified number of nodes[Graph-11](-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}

	@Test
	public void testMaxNodes3() {
		WikiCrawler crawler = new WikiCrawler("/wiki/A1.html", 2, "Graph-N3.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-N3.txt");
		if (!edges.contains("/wiki/A1.html /wiki/B1.html")
				|| !edges.contains("/wiki/B1.html /wiki/A1.html")) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly. Did not stop or run till specified number of nodes[Graph-2](-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}

	@Test
	public void testMaxNodes4() {
		WikiCrawler crawler = new WikiCrawler("/wiki/L.html", 3, "Graph-N4.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-N4.txt");
		if (!edges.contains("/wiki/L.html /wiki/M.html")
				|| !edges.contains("/wiki/L.html /wiki/N.html")
				|| !edges.contains("/wiki/M.html /wiki/N.html")
				|| !edges.contains("/wiki/M.html /wiki/L.html")
				|| !edges.contains("/wiki/N.html /wiki/M.html")
				|| !edges.contains("/wiki/N.html /wiki/L.html")) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly. Did not stop or run till specified number of nodes[Graph-12](-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}

	@Test
	public void testMaxNodes5() {
		WikiCrawler crawler = new WikiCrawler("/wiki/L1.html", 3, "Graph-N5.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-N5.txt");
		if (!edges.contains("/wiki/L1.html /wiki/M1.html")
				|| !edges.contains("/wiki/L1.html /wiki/N1.html")
				|| !edges.contains("/wiki/M1.html /wiki/N1.html")
				|| !edges.contains("/wiki/M1.html /wiki/L1.html")
				|| !edges.contains("/wiki/N1.html /wiki/M1.html")
				|| !edges.contains("/wiki/N1.html /wiki/L1.html")) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly. Did not stop or run till specified number of nodes[Graph-13](-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}

	@Test
	public void testMaxNodes6() {
		WikiCrawler crawler = new WikiCrawler("/wiki/L2.html", 3, "Graph-N6.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-N6.txt");
		if (!edges.contains("/wiki/L2.html /wiki/M2.html")
				|| !edges.contains("/wiki/L2.html /wiki/N2.html")
				|| !edges.contains("/wiki/M2.html /wiki/N2.html")
				|| !edges.contains("/wiki/M2.html /wiki/L2.html")
				|| !edges.contains("/wiki/N2.html /wiki/M2.html")
				|| !edges.contains("/wiki/N2.html /wiki/L2.html")) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly. Did not stop or run till specified number of nodes[Graph-14](-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}

	@Test
	public void testMaxNodes7() {
		WikiCrawler crawler = new WikiCrawler("/wiki/L3.html", 4, "Graph-N7.txt");
		crawler.crawl();
		List<String> edges = getEdgesFromFile("Graph-N7.txt");
		if (!edges.contains("/wiki/L3.html /wiki/M3.html")
				|| !edges.contains("/wiki/L3.html /wiki/N3.html")
				|| !edges.contains("/wiki/M3.html /wiki/N3.html")
				|| !edges.contains("/wiki/M3.html /wiki/L3.html")
				|| !edges.contains("/wiki/N3.html /wiki/M3.html")
				|| !edges.contains("/wiki/N3.html /wiki/L3.html")
				|| !edges.contains("/wiki/M3.html /wiki/O3.html")) {
			totalPoints -= 10;
			commentsBuilder.append("Web graph formed incorrectly. Did not stop or run till specified number of nodes[Graph-15](-10 points). ");
			Assert.fail("Web graph formed incorrectly");
		}
	}

	// test max nodes - end
	// test out degrees - start

	@Test
	public void testOutDegree1() {
		// TODO add final points
		checkOutDegree("outdegree-1.txt",1,4);
	}

	@Test
	public void testOutDegree2() {
		// TODO add final points
		checkOutDegree("outdegree-2.txt",4,4);
	}

	@Test
	public void testOutDegree3() {
		// TODO add final points
		checkOutDegree("outdegree-3.txt",0,4);
	}

	private void checkOutDegree(String fileName, int expectedOutput, int points) {
		String deduct = "(-" + points + " points). ";
		try {
			GraphProcessor graphProcessor = new GraphProcessor(inputDirectory + fileName);
			if(graphProcessor.outDegree("v") != expectedOutput) {
				totalPoints -= points;
				commentsBuilder.append("Incorrect out-degree returned" + deduct);
				Assert.fail("Incorrect out degree");
			}
		} catch (Exception e) {
			totalPoints -= points;
			commentsBuilder.append("Exception creating GraphProcessor while testing out degree. Exception - " + e.getClass().getCanonicalName() +", " + deduct);
			Assert.fail("Exception testing out degree");
		}
	}

	// test out degrees - end
	// test scc - start

	private void checkSameComponent(GraphProcessor graphProcessor, String vertex1, String vertex2, boolean expectedValue, int points, String graph) {
		String deduct = "[" + graph + "](-" + points + " points). ";
		try {
			if(graphProcessor.sameComponent(vertex1,vertex2) != expectedValue) {
				totalPoints -= points;
				commentsBuilder.append("Incorrect value returned for GraphProcessor.sameComponent(" + vertex1 + "," + vertex2 + ")" + deduct);
				collector.addError(new AssertionFailedError("Incorrect result fors scc"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			totalPoints -= points;
			commentsBuilder.append("Exception creating GraphProcessor while testing same component. Exception - " + e.getClass().getCanonicalName() +", " + deduct);
			Assert.fail("Exception creating GraphProcessor while testing same component.");
		}
	}

	private void checkComponentVertices(GraphProcessor graphProcessor, String vertex, List<String> expectedValue, int points, String graph) {
		String deduct = "[" + graph + "](-" + points + " points). ";
		try {
			List<String> vertices = graphProcessor.componentVertices(vertex);
			// we use containsAll here because order does not matter
			if(!vertices.containsAll(expectedValue)) {
				totalPoints -= points;
				commentsBuilder.append("Incorrect value returned for GraphProcessor.componentVertices(" + vertex + ")" + deduct);
				collector.addError(new AssertionFailedError("Incorrect value returned for GraphProcessor.componentVertices"));
			}
		} catch (Exception e) {
			totalPoints -= points;
			commentsBuilder.append("Exception creating GraphProcessor while testing componentVertices. Exception - " + e.getClass().getCanonicalName() +", " + deduct);
			collector.addError(new AssertionFailedError("Exception testing scc"));
		}
	}

	private GraphProcessor createGraphProcessor(String fileName, int points) {
		String deduct = "(-" + points + " points). ";
		GraphProcessor graphProcessor = null;
		try {
			graphProcessor = new GraphProcessor(inputDirectory + fileName);
		} catch (Exception e) {
			totalPoints -= points;
			commentsBuilder.append("Exception creating GraphProcessor while testing SCC. Exception - " + e.getClass().getCanonicalName() +", " + deduct);
			Assert.fail("Exception testing scc");
		}
		return graphProcessor;
	}

	@Test
	public void testSCC1() {
		// TODO decide final points
		GraphProcessor graphProcessor = createGraphProcessor("scc-1.txt", 5);
		String graphId = "scc-1";
		// TODO decide final points
		checkSameComponent(graphProcessor, "A", "B", true, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "A", "C", true, 5, graphId);

		String[] components = new String[]{"A","B","C"};
		List<String> expectedComponent = Arrays.asList(components);
		// TODO decide final points
		checkComponentVertices(graphProcessor,"A",expectedComponent,5,graphId);
		// TODO decide final points
		checkComponentVertices(graphProcessor,"B",expectedComponent,5,graphId);
	}

	@Test
	public void testSCC2() {
		// TODO decide final points
		GraphProcessor graphProcessor = createGraphProcessor("scc-2.txt", 5);
		String graphId = "scc-2";
		// TODO decide final points
		checkSameComponent(graphProcessor, "A", "B", false, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "B", "C", true, 5, graphId);

		String[] component1 = new String[]{"A"};
		String[] component2 = new String[]{"B","C"};
		// TODO decide final points
		checkComponentVertices(graphProcessor,"A",Arrays.asList(component1),5,graphId);
		// TODO decide final points
		checkComponentVertices(graphProcessor,"B",Arrays.asList(component2),5,graphId);
	}

	@Test
	public void testSCC3() {
		// TODO decide final points
		GraphProcessor graphProcessor = createGraphProcessor("scc-3.txt", 5);
		String graphId = "scc-3";
		// TODO decide final points
		checkSameComponent(graphProcessor, "A", "B", true, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "D", "E", true, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "D", "A", false, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "B", "F", false, 5, graphId);

		String[] component1 = new String[]{"A","B","C"};
		String[] component2 = new String[]{"D","E","F"};
		// TODO decide final points
		checkComponentVertices(graphProcessor,"A",Arrays.asList(component1),5,graphId);
		// TODO decide final points
		checkComponentVertices(graphProcessor,"C",Arrays.asList(component1),5,graphId);
		// TODO decide final points
		checkComponentVertices(graphProcessor,"D",Arrays.asList(component2),5,graphId);
		// TODO decide final points
		checkComponentVertices(graphProcessor,"E",Arrays.asList(component2),5,graphId);
	}

	@Test
	public void testSCC4() {
		// TODO decide final points
		GraphProcessor graphProcessor = createGraphProcessor("scc-4.txt", 5);
		String graphId = "scc-4";
		// TODO decide final points
		checkSameComponent(graphProcessor, "A", "D", true, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "E", "C", true, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "B", "F", true, 5, graphId);

		String[] component1 = new String[]{"A","B","C","D","E","F"};
		// TODO decide final points
		checkComponentVertices(graphProcessor,"A",Arrays.asList(component1),5,graphId);
		// TODO decide final points
		checkComponentVertices(graphProcessor,"F",Arrays.asList(component1),5,graphId);
	}

	@Test
	public void testSCC5() {
		// TODO decide final points
		GraphProcessor graphProcessor = createGraphProcessor("scc-5.txt", 5);
		String graphId = "scc-5";
		// TODO decide final points
		checkSameComponent(graphProcessor, "A", "D", true, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "C", "D", true, 5, graphId);

		String[] component1 = new String[]{"A","B","C","D"};
		// TODO decide final points
		checkComponentVertices(graphProcessor,"A",Arrays.asList(component1),5,graphId);
	}

	@Test
	public void testSCC6() {
		// TODO decide final points
		GraphProcessor graphProcessor = createGraphProcessor("scc-6.txt", 5);
		String graphId = "scc-6";
		// TODO decide final points
		checkSameComponent(graphProcessor, "C", "B", true, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "B", "E", false, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "D", "F", false, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "A", "E", false, 5, graphId);

		String[] component1 = new String[]{"A","B","C","D"};
		String[] component2 = new String[]{"E","F","G"};
		// TODO decide final points
		checkComponentVertices(graphProcessor,"A",Arrays.asList(component1),5,graphId);
		// TODO decide final points
		checkComponentVertices(graphProcessor,"E",Arrays.asList(component2),5,graphId);
	}

	@Test
	public void testSCC7() {
		// TODO decide final points
		GraphProcessor graphProcessor = createGraphProcessor("scc-7.txt", 5);
		String graphId = "scc-7";
		// TODO decide final points
		checkSameComponent(graphProcessor, "A", "D", false, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "D", "E", false, 5, graphId);
		// TODO decide final points
		checkSameComponent(graphProcessor, "C", "E", false, 5, graphId);

		String[] component1 = new String[]{"A","B","C"};
		String[] component2 = new String[]{"D"};
		String[] component3 = new String[]{"E"};

		// TODO decide final points
		checkComponentVertices(graphProcessor,"A",Arrays.asList(component1),5,graphId);
		// TODO decide final points
		checkComponentVertices(graphProcessor,"D",Arrays.asList(component2),5,graphId);
		// TODO decide final points
		checkComponentVertices(graphProcessor,"E",Arrays.asList(component3),5,graphId);
	}

	// test scc - end
	// test BFS path - start

	private void checkBFSPath(GraphProcessor graphProcessor, String source, String dest, List<String> expectedPath, int points, String graphId) {
		String deduct = "[" + graphId + "](-" + points + " points). ";
		try {
			List<String> path = graphProcessor.bfsPath(source, dest);

			if((expectedPath == null && (path == null || path.size() == 0)) || (!path.equals(expectedPath))) {
				totalPoints -= points;
				commentsBuilder.append("Incorrect value returned for GraphProcessor.bfsPath(" + source + "," + dest + ")" + deduct);
			}
		} catch (Exception e) {
			totalPoints -= points;
			commentsBuilder.append("Exception creating GraphProcessor while testing bfsPath. Exception - " + e.getClass().getCanonicalName() +", " + deduct);
			Assert.fail("Exception testing scc");
		}
	}

	@Test
	public void testBFSPath1() {
		// TODO decide final points
		GraphProcessor graphProcessor = createGraphProcessor("bfs-1.txt",5);
		String graphId = "bfs-1";

		String[] path1 = new String[]{"C","D","B","A"};
		String[] path2 = new String[]{"A","B","C"};

		// TODO decide final points
		checkBFSPath(graphProcessor,"C", "A",Arrays.asList(path1),5,graphId);
		// TODO decide final points
		checkBFSPath(graphProcessor,"A", "C",Arrays.asList(path2),5,graphId);
	}

	@Test
	public void testBFSPath2() {
		// TODO decide final points
		GraphProcessor graphProcessor = createGraphProcessor("bfs-2.txt",5);
		String graphId = "bfs-2";

		String[] path1 = new String[]{"B","C","E"};
		String[] path2 = new String[]{"A","B","C"};

		// TODO decide final points
		checkBFSPath(graphProcessor,"B", "E",Arrays.asList(path1),5,graphId);
		// TODO decide final points
		checkBFSPath(graphProcessor,"A", "C",Arrays.asList(path2),5,graphId);
		// TODO decide final points
		checkBFSPath(graphProcessor,"D", "F",null,5,graphId);
	}

	@Test
	public void testBFSPath3() {
		// TODO decide final points
		GraphProcessor graphProcessor = createGraphProcessor("bfs-3.txt",5);
		String graphId = "bfs-3";

		String[] path1 = new String[]{"C","E","B","D"};
		String[] path2 = new String[]{"A","B","D"};

		// TODO decide final points
		checkBFSPath(graphProcessor,"C", "D",Arrays.asList(path1),5,graphId);
		// TODO decide final points
		checkBFSPath(graphProcessor,"A", "D",Arrays.asList(path2),5,graphId);
		// TODO decide final points
		checkBFSPath(graphProcessor,"B", "C",null,5,graphId);
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
