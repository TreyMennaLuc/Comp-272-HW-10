import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DirectedGraph {
    ArrayList<DirectedNodeList> dGraph;
    int numVertex;
    boolean [] marked;
    ArrayList<Integer> finishing;
    ArrayList<Integer> SCCLeaderIndex;
    HashedDirectedGraph reducedGraph;
    ArrayList<Edge> edgeSet;
    HashMap<Integer, ArrayList<Integer>> SCC;



    public DirectedGraph() {
        dGraph = new ArrayList<>();
        numVertex=0;
        finishing = new ArrayList<>();
        reducedGraph = new HashedDirectedGraph();
        edgeSet = new ArrayList<>();
        SCCLeaderIndex = new ArrayList<>();
        SCC = new HashMap<>();

    }

    public DirectedGraph(int n) {
        numVertex =n;
        dGraph = new ArrayList<>(n);
        finishing = new ArrayList<>();
        marked= new boolean[n];
        reducedGraph = new HashedDirectedGraph();
        edgeSet = new ArrayList<>();
        SCCLeaderIndex = new ArrayList<>();
        SCC = new HashMap<>();
        for (int i=0;i<numVertex;i++)
        {
            dGraph.add(new DirectedNodeList());
            SCCLeaderIndex.add(0);
        }
    }

    public int getLeader(int n)
    {
        return SCCLeaderIndex.get(n);
    }

    public void unMarkAll()
    {
       marked = new boolean[numVertex];
    }

    public void addEdge(Edge e, int u, int v) {
        // assume all vertices are created
        // directed edge u to v will cause outdegree of u to go up and indegree of v to go up.
        edgeSet.add(e);
        if (u>=0 && u<numVertex && v>=0 && v<numVertex) {
            if (u!=v) {
                getNeighborList(u).addToOutList(v);
                getNeighborList(v).addToInList(u);
            }
        }
        else throw new IndexOutOfBoundsException();
    }

    public DirectedNodeList getNeighborList(int u) {
        return dGraph.get(u);
    }

    public void printAdjacency(int u) {
        DirectedNodeList dnl = getNeighborList(u);
        System.out.println ("vertices going into "+u+"  "+dnl.getInList());
        System.out.println ("vertices going out of "+u+"  "+dnl.getOutList());
        System.out.println();
    }

    public void postOrderDepthFirstTraversal() {
        for (int i=0;i<numVertex;i++)
            if (!marked[i])
                postOrderDFT(i);

    }
    public void postOrderDFT(int v){

        marked[v]=true;

        for (Integer u: dGraph.get(v).getInList()) //Changed to getInList
        {
            if (!marked[u]) postOrderDFT(u);
            finishing.add(v);
        }
    }
    public void finishingDepthFirstTraversal()
    {

        for (int x = numVertex - 1; x >= 0; x--)
        {
            int y = finishing.get(x);
            if (!marked[y]) {
                SCCLeaderIndex.set(x,y);
                fDFT(y,x);
            }
        }

    }

    public void fDFT(int v, int leader){
        marked[v]=true;
        for (Integer u:dGraph.get(v).getOutList()) {
            if (!marked[u]) {
                SCCLeaderIndex.set(u, leader);
                fDFT(u, leader);
            }
        }

    }

    public void createSCCList()
    {
        HashSet<Integer> leaders = new HashSet<>();
        leaders.addAll(SCCLeaderIndex);
        Iterator<Integer> it = leaders.iterator();
        while (it.hasNext())
        {
            int key = it.next();
            SCC.put(key, new ArrayList<>());
            reducedGraph.addVertex(key);
        }
        for(int x = 0; x < SCCLeaderIndex.size(); x++)
        {
            int leader = SCCLeaderIndex.get(x);
            SCC.get(leader).add(x);
        }
    }

    public void createReducedGraph()
    {
        this.createSCCList();
        int numSCC = reducedGraph.numVertex;
        int largestCC = 0;
        System.out.println("number of SCC: " + numSCC);
        Iterator<Integer> itr = SCC.keySet().iterator();
        while (itr.hasNext())
        {
            int key = itr.next();
            if(SCC.get(key).size() > largestCC)
                largestCC = SCC.get(key).size();
        }
        System.out.println("max size among those components: " + largestCC);

        for(Edge edge : edgeSet)
        {
            int x = edge.v1;
            int y = edge.v2;
            int xLeader = getLeader(x);
            int yLeader = getLeader(y);
            if(!(xLeader == yLeader))
                reducedGraph.addEdge(xLeader,yLeader);

        }

    }



    public static void main(String[] args) {
        DirectedGraph dg = new DirectedGraph(82168);
        File file = new File("Slashdot0902.txt");
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                int a1 = sc.nextInt();
                int a2 = sc.nextInt();
                Edge thisEdge = new Edge(a1,a2);
                dg.addEdge(thisEdge, a1 ,a2);
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
        dg.postOrderDepthFirstTraversal();
        dg.unMarkAll();
        dg.finishingDepthFirstTraversal();
        dg.createReducedGraph();

    }


}