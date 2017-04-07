import java.util.*;
import java.io.*;

public class GraphProcessor {
	HashMap<String, Integer> vertexToInt; //Data Strucrure that to get ID of a vertex
	String[] intToVertex; ///Converts into to Vertex
	VertexList[] adjList; //Adj list of Graph
	VertexList[] revAdjList; //Adj list of reverse Graph
	int[] componentID; // For vertex i, componetID[i] is the componen to which i belongs to
	VertexList[] sccComponents; // list of SCC Components;
	int numVertices;
	boolean[] marked;
	int[] finishTimes; //Finish times for DFS on reverse graph
	int finishCounter = -1;
	int numComponents = -1;
	HashSet visited;
	ArrayList<Integer> bfsQ;

	/*
	 * Constructor
	 */

	public GraphProcessor(String fileName) throws FileNotFoundException {
		File f = new File(fileName);
		Scanner fileIn = new Scanner(f);
		numVertices = fileIn.nextInt();
		initializeAll();
		int counter = -1;
		while(fileIn.hasNext()) {

			String uVertex = fileIn.next();
			if (!vertexToInt.containsKey(uVertex)) {
				counter++;
				vertexToInt.put(uVertex, counter);
				intToVertex[counter] = uVertex;
				//System.out.println(uVertex + " " + counter);

			}
			String vVertex = fileIn.next();
			if (!vertexToInt.containsKey(vVertex)) {
				counter++;
				vertexToInt.put(vVertex, counter);
				intToVertex[counter] = vVertex;
				//System.out.println(vVertex + " " + counter);

			}

			int uId = getVertexID(uVertex);
			int vId = getVertexID(vVertex);
			adjList[uId].add(vId);
			revAdjList[vId].add(uId);


		}
		scc();
		System.out.println("STARTING BFS");
		allBFS();
	}


	/*
	 * Return outdegree of vertex v
	 */

	public int outDegree(String v) {
		int x = getVertexID(v);
		return adjList[x].size();
	}




	/*
	 * Return number of components
	 */

	public int numComponents() {
		return numComponents+1;

	}

	/*
	 * Checks if two vertices u and v are in the same component
	 */

	public boolean sameComponent(String u, String v) {
		int uID = getVertexID(u);
		int vID = getVertexID(v);
		if (componentID[uID]==componentID[vID])
			return true;
		else
			return false;

	}

	/*
	 * Returns all vertices that are in the same component as vertex
	 */
	public ArrayList<String> componentVertices(String vertex) {

		int v = getVertexID(vertex);
		int cID = componentID[v];
		VertexList list = sccComponents[cID];
		ArrayList<String> l = new ArrayList<String>();
		for(int i =0; i<list.size(); i++)
			l.add(intToVertex[list.get(i)]);
		return l;
	}





	/*
	 * Initialize all relavanet data structures baed on number of vertices
	 */

	private void initializeAll() {
		//System.out.println("Begin INIT");
		int n = numVertices;
		visited = new HashSet();
		bfsQ = new ArrayList<Integer>();
		vertexToInt = new HashMap<String, Integer>();
		intToVertex = new String[n];
		adjList = new VertexList[n];
		revAdjList = new VertexList[n];
		sccComponents = new VertexList[n];
		//System.out.println("loop");
		for(int i =0; i<n; i++){
			adjList[i] = new VertexList();
			revAdjList[i] = new VertexList();
			sccComponents[i] = new VertexList();
		}
		//System.out.println("End loop");

		componentID = new int[n];
		marked = new boolean[n];
		for(int i=0; i<n; i++)
			marked[i] = false;
		finishTimes = new int[n];
		//System.out.println("END INIT");
	}


	private void scc() {
		computeFinishTimes();
		computeSCC();
	}


	private void computeSCC() {
		for(int i =0; i<numVertices; i++)
			marked[i] = false;
		for(int i =numVertices-1; i>=0; i--){
			int v = finishTimes[i];
			if (!marked[v]){
				numComponents++;
				sccComponents[numComponents] = new VertexList();
				sccDFS(v);
			}
		}


	}


	private void sccDFS(int v) {
		marked[v] = true;
		sccComponents[numComponents].add(v);
		componentID[v] = numComponents;
		int outDegree = adjList[v].size();
		for(int i = 0; i<outDegree; i++) {
			int w = adjList[v].get(i);
			if (!marked[w])
				sccDFS(w);
		}

	}

	public void computeFinishTimes() {
		finishCounter = -1;
		for(int i = 0; i<numVertices; i++)
			if (!marked[i])
				finishDFS(i);

	}


	private void finishDFS(int v) {
		//System.out.println("Entering DFS " + intToVertex[v]);
		marked[v] = true;
		int outDegreeV = revAdjList[v].size();
		for(int i = 0; i<outDegreeV; i++) {
			int w = revAdjList[v].get(i);
			if (!marked[w])
				finishDFS(w);
		}
		finishCounter++;
		//System.out.println("Finished " + v + " " + intToVertex[v]);
		finishTimes[finishCounter] = v;

	}


	public void printFinishTimes() {
		for (int i =0; i<numVertices; i++) {
			//System.out.println(i + " " + intToVertex[finishTimes[i]]);
		}
	}



	public boolean isEdge(String u, String v) {
		int uID = getVertexID(u);
		int vID = getVertexID(v);
		if (adjList[uID].search(vID))
			return true;

		return false;

	}


	public boolean isReverseEdge(String u, String v) {
		int uID = getVertexID(u);
		int vID = getVertexID(v);
		if (revAdjList[uID].search(vID))
			return true;

		return false;

	}


	private int getVertexID(String vertex) {
		return vertexToInt.get(vertex);
	}


	private String getNthVertex(int i) {
		return intToVertex[i];
	}


	private void allBFS() {
		for(int i =0; i<numVertices; i++) {
			visited = new HashSet();
			bfsQ = new ArrayList<Integer>();
			bfs(i);

		}

	}

	private void bfs(int i) {
		//System.out.println("BFS on " + i);
		visited.add(i);
		bfsQ.add(i);
		while (bfsQ.size()!=0) {
			int first = bfsQ.get(0);
			bfsQ.remove(0);
			for(int j =0; j<adjList[first].size(); j++) {
				int v = adjList[first].get(j);
				if (!visited.contains(v)) {
					visited.add(v);
					bfsQ.add(v);
				}
			}

		}

	}


	public static void main(String[] args) throws FileNotFoundException {
		Scanner stdin = new Scanner(System.in);
		//System.out.println("Enter file name");
		//String file = stdin.next();
		long start = System.currentTimeMillis();
		GraphProcessor g = new GraphProcessor("agraph.txt");
		long end = System.currentTimeMillis();


		System.out.println(g.numComponents());
		System.out.println((end-start)/1000.0 + " seconds");



	}

	public int largestComponent() {
		return 1;
	}

	public List<String> bfsPath(String src, String dest) {
		return null;
	}


}
