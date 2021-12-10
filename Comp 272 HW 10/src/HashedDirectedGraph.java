import java.util.*;
import java.io.*;
public class HashedDirectedGraph  {
    HashMap<Integer, DirectedNodeList> hDGraph;
    int numVertex = 0;
    int numEdges = 0;

//getter methods

    public HashedDirectedGraph() {
        hDGraph = new HashMap<>();
        numVertex=0;

    }
    //n is number of SCC’s
    public HashedDirectedGraph(int n) {
        numVertex =n;
        hDGraph = new HashMap<>(n);
    }
//using the keySet of  the other hashMap
//k is the leader
    public void addVertex(int k){
    //k is within the bounds of 0 and max Vertex label of the original directed graph
        hDGraph.put(k, new DirectedNodeList());
        numVertex++;
    }
    public boolean isEdgePresent(int u, int v)
    {
    //check if (u,v) is already present in the reduced hDGraph
        //check if u has v in out
            ArrayList<Integer> check  = hDGraph.get(u).getOutList();
            return check.contains(v);
    }

    public void addEdge(int u, int v) {
        // assume all vertices are created

        if (!isEdgePresent(u,v)) {
            if(u!=v && getNeighborList(u).getOutList().size() == 0)
            {
                getNeighborList(u).addToOutList(v);
            }
        }
        numEdges++;
    }

    public DirectedNodeList getNeighborList(int u) {
        return hDGraph.get(u);
    }

}
