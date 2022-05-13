

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class MaxFlow
{
    HashMap<Integer,ArrayList<Edge>> adj_list;      // adjacency list representation of graph
    int []parent;                                   // parent array used in bfs
    int N;                                          // total number of nodes

    /**
    * initialize constructor function.
    */
    public MaxFlow(int N)
    {
        this.N = N+2;
        parent = new int[(this.N)]; //CHANGE HERE LATER

        for (int i = 0; i < parent.length; i++) {
            parent[i] = -1;
        }
        adj_list = new HashMap<>();
    }

    /** HELPER FUNCTIONS**/

    public void checkorder() {

        for(int i : adj_list.keySet()){
            ArrayList<Edge> edge_list = adj_list.get(i);
            System.out.printf(i + " :");
            for(Edge j : edge_list){
                System.out.printf(" (" + j.destination + "," + j.flow_rate + ") ");
            }
            System.out.printf("\n");
        }


    }

    // prints the helper function in an array form for checking
    public void printbfsparent() {
        int j = 0;
        for(int i : parent) {

            j += 1;
        }
    }

    //gets path of the bfs
    public ArrayList<Integer> getpath() {
        //find the index with the start value (the first vertex connected)
        Stack<Integer> stack = new Stack<>();
        ArrayList<Integer> path = new ArrayList<>();


        int the_index = N-1;

        if (parent[the_index] == 0) {
            path.add(0);
            path.add(the_index);
            return path;
        }

        while( parent[the_index] != 0) {
            stack.add(the_index);
            the_index = parent[the_index];
        }
        stack.add(the_index);
        stack.add(0);

        while (!stack.isEmpty()){
            int temp = stack.pop();
            path.add(temp);

        }


        return path;
    }



    /** HELPER FUNCTIONS**/

    /**
    * gradually build the graph by inserting edges
    * this function inserts a new edge into the graph
    */
    public void insEdge(int source, int destination, int flow_rate) {
        if (adj_list.get(source) == null) {

            ArrayList<Edge> edge_list = new ArrayList<>();
            Edge edge = new Edge(destination,flow_rate);
            edge_list.add(edge);
            adj_list.put(source,edge_list);

        } else {
            ArrayList<Edge> edge_list = adj_list.get(source);
            Edge edge = new Edge(destination,flow_rate);
            edge_list.add(edge);
            adj_list.put(source,edge_list);
        }

        //REVERSE

        if (adj_list.get(destination) == null) {

            ArrayList<Edge> edge_list = new ArrayList<>();
            Edge edge = new Edge(source,0);
            edge_list.add(edge);
            adj_list.put(destination,edge_list);
        }


        //checks if the reverse exist
        ArrayList<Edge> reverse_list = adj_list.get(destination);
        boolean repeated_exists = false;
        for( Edge i : reverse_list) {
            if(i.destination == source){
                repeated_exists = true;
            }
        }

        // if repeated doesnt exist appends
        if(!repeated_exists) {
            ArrayList<Edge> edge_list = adj_list.get(destination);
            Edge edge = new Edge(source,0);
            edge_list.add(edge);
            adj_list.put(destination,edge_list);
        }


    }

    boolean bfs2() {
        //initilize parent
        parent = new int[(this.N)];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = -1;
        }
        int source = 0;
        int sink = N-1;

        int []visited = new int[N];
        //itilizing the visited array
        for (int i = 0; i < visited.length; i++){
            visited[i] = -1;
        }

        // add , poll -> removes and gets the value , peek -> returns the value doesn't remove
        Queue<Integer> Q = new PriorityQueue<>();
        Q.add(source);

        while(!Q.isEmpty()){
            int u = Q.remove();

            // visiting the neighbors and adding them to the Q
            for (Edge i : adj_list.get(u)) {
                int each_neighbor = i.destination; // a key values each neighbor in a different ittr.
                int edge_flow_rate = i.flow_rate; //flow rate of the edge

                if (visited[each_neighbor] == -1 && edge_flow_rate != 0) {
                    Q.add(each_neighbor);
                    visited[each_neighbor] = 1;
                    parent[each_neighbor] = u;
                }
            }
        }


        if(parent[sink] != -1) {
            return true;
        }

        return false;
    }

    /**
    * implement BFS function        
    *
    */
    boolean bfs() {
        int source = 0;
        int sink = N-1;
        // add , poll -> removes and gets the value , peek -> returns the value doesn't remove
        Queue<Integer> queue = new PriorityQueue<>();

        //adds the source to the queue
        int current = source;
        queue.add(current); // adds the source to the Q


        while ( current != sink && queue.size() != 0){

            // visiting the neighbors and adding them to the Q
            for (Edge i : adj_list.get(current)) {

                int each_neighbor = i.destination; // a key values each neighbor in a different ittr.
                int edge_flow_rate = i.flow_rate; //flow rate of the edge

                //check if the value already exist in the Q
                boolean exists = false;
                for( int j : queue) {
                    if (j == each_neighbor) {
                        exists = true;
                    }
                }
                //add the neighbor to the queue if it doesn't already exist or it's weight is zero
                if(!exists && edge_flow_rate != 0) {
                    queue.add(each_neighbor);
                }

            }


            int popped_node = queue.remove();


            if (queue.peek() == null) { //if queue has only one value left ends
                break;
            }
            parent[queue.peek()] = popped_node; //set the before relation. index popped_nodes value is the previously popped value

            current = queue.peek(); // for updating the loop

        }

        if(parent[sink] != -1) {
            return true;
        }



        return false;
    }



    /** TODO
    * implement path augmentation
    *
    */
    int pathAugmentation()
    {
        int pond = 0;
        int barrel = N - 1; //because N is the total # of nodes --- counts 0 as first index
        int maxflow = 0;
        /*default value provided*/

        int counter = 0;
        while(bfs2() ) {
            counter = counter + 1;

            ArrayList<Integer> P = new ArrayList<>();
            P = getpath(); // print the array

            int flow = Integer.MAX_VALUE;


            //finding the minimum flow
            for ( int i = 0; i < P.size() - 1; i++) { //P.size() because of the edge calling method I implement to avoid going beyond the index range
                int ew = getFlow(P.get(i),P.get(i + 1));

                flow = Math.min(flow , ew);

            }

            // substract that flow from all paths
            for ( int j = 0; j < P.size() - 1; j++) {
                int current_flowrate = getFlow( P.get(j) , P.get(j+1) );
                setFlow(P.get(j),P.get(j+1), current_flowrate - flow);
                setFlow(P.get(j + 1),   P.get(j),  flow);

            }

            maxflow = maxflow + flow;


       }


        return maxflow;
    }

    /**
    * get the flow along a certain edge
    */
    int getFlow(int source, int destination)
    {
        ArrayList<Edge> edge_list = adj_list.get(source);
        int flow = 0;
        for (Edge i : edge_list){
            if(i.destination == destination){
                flow = i.flow_rate;
            }
        }
       return flow;
    }

    /**
    * set the value of flow along a certain edge
    */
    void setFlow(int source, int destination, int flow_rate)
    {

        for (Edge i : adj_list.get(source)){
            if(i.destination == destination){
                i.flow_rate = flow_rate;
            }
        }
       
    }

    public static void main(String []args)
    {
        try {
            MaxFlow obmax = new MaxFlow(0);
            File myObj = new File("/Users/dehaay/Desktop/JavaCS251/Project 4/testFiles/max_flow_testcases/9.txt");
            Scanner myReader = new Scanner(myObj);
            int line = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                if(line == 0)
                {
                    int tot = Integer.parseInt(data);

                    obmax = new MaxFlow(tot);
                }
                else
                {
                    String []comp = data.split(" ");
                    int s = Integer.parseInt(comp[0]);
                    int d = Integer.parseInt(comp[1]);
                    int f = Integer.parseInt(comp[2]);

                    obmax.insEdge(s, d, f);
                }
                line += 1;
            }

            obmax.checkorder();
            boolean c = obmax.bfs2();


            myReader.close();
            int mflow = obmax.pathAugmentation();
            System.out.println("Maxflow is: "+mflow);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
