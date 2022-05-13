import jdk.swing.interop.SwingInterOpUtils;

import java.util.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner;

public class BipartiteMatching {
    HashMap<Integer,ArrayList<Node>> adj_list;          // adjacency list representation of graph
    int []match;                                        // which node a node is matched with
    int M, N;                                           // M, N are number of machines and cards respectively    
    boolean []used;                                     // whether a node has been already used in matching or not

    /**
    * initialize constructor function.
    */
    public BipartiteMatching(int M, int N)
    {
        adj_list = new HashMap<>();
		this.M = M;
        this.N = N;
        //Might cause an error deleted the "-1" because when both are 0 gives array size -1
        used = new boolean[(M+N)];
        match = new int[M+N];

        //initilizing all matches to 0

        for (int i = 0; i < match.length; i++) {
            match[i] = -1;
        }
    }

    /**
     * gradually build the graph by inserting edges
     * this function inserts all the nodes connected with node u
     * @param u : node under consideration
     * @param node_list : all the nodes connected with node u
    **/
    public void insList(int u, int []node_list)
    {		
        //M is the start of the Card keys so 1st Card is key'ed M (due to end of the machines is M-1 -> M-1 + 1 = M)

        //connecting cards with the machine
        ArrayList<Node> machine_node_array = new ArrayList<>();
        for ( Integer i : node_list ) {

            machine_node_array.add(new Node(i - 1));
        }
        //System.out.println(node_array.toString());

        adj_list.put(M + u, machine_node_array);

        //I know it's redundant easier to follow
        // *Connecting machines with the card*
        //int array is given without the 0 start thats why the -1 is there
        for ( Integer i : node_list ) {
            ArrayList<Node> node_array = new ArrayList<>();
            node_array.add(new Node(u + M));

            if(adj_list.get(i-1) == null) { //if the key isn't already place, place it
                adj_list.put(i - 1, node_array);
            } else { //if key already placed append the current key's arraylist
                node_array = adj_list.get(i-1);
                node_array.add(new Node(u + M));
                adj_list.put(i - 1, node_array);
            }

        }

    }




    /**
    * implement DFS function
    * @return true if there is an augment path; if no, return false.
    */
    boolean dfs(int v) {
        if( used[v] == true){
            return false;
        }

        this.used[v] = true;

        ArrayList<Node> adj_list_of_v = adj_list.get(v);

        for ( Node u : adj_list_of_v){
            //System.out.println(u.node_id);
            if ( match[u.node_id] == -1) {
                match[u.node_id] = v;
                match[v] = u.node_id;

                return true;

            } else if (match[u.node_id] != -1) {
                int w = match[u.node_id];


                if (used[w] == false && (dfs(w) == true) ) {
                    match[u.node_id] = v;
                    match[v] = u.node_id; //might DELETE later added are we matching both ways?

                    return true;
                }

            }

        }
        return false;
    }

    /**
    *
    * implement the bipartite matching algorithm
    * traverse the nodes
    * call dfs to see if there exists any augment path
    */
    int bipartiteMatching()
    {
        int res = 0;
        //!!!!Caution started i from 0 since the vertex names start from 0
        for( int i = 0; i < M; i++){
            used = new boolean[(M + N)]; //!!!I have already done that in the initilizer
            //Check here
            if (match[i] == -1) {
                if(dfs(i)){
                    res = res + 1;
                    //used[i] = true; //DELETE
                }
            }
        }
        return res;
    }


    /** HELPER FUNCTIONS**/

    public void update_hasmap(int key_vertex, int connected_vertex){
        ArrayList<Node> node_array = adj_list.get(key_vertex);

        int count = 0;
        //checks if new "connected_vertex" value is inside node_array
        for(Node i : node_array){
            if(i.node_id == connected_vertex) {
                count = count + 1;
            }
        }

        //if not exist
        if(count == 0) {
            ArrayList<Node> new_node_array = adj_list.get(key_vertex);
            new_node_array.add(new Node(connected_vertex));
            adj_list.put(key_vertex,new_node_array);
        }
        //if exist dont do anything
    }

    public void printadjmatrix(HashMap<Integer, ArrayList<Node>> adj_map){
        System.out.println(adj_map.keySet());
        ArrayList<Node> node_array = new ArrayList<>();
        for( Integer i : adj_map.keySet() ) {
            node_array = adj_map.get(i);
            System.out.printf("%s : ",i);
            for (Node j : node_array) {
                System.out.printf("%s ",j.node_id);
            }
            System.out.printf("\n");
        }
    }
    public void printadjmatrix_simplified(HashMap<Integer, ArrayList<Node>> adj_map){
        System.out.println(adj_map.keySet());
        ArrayList<Node> node_array = new ArrayList<>();
        for( Integer i : adj_map.keySet() ) {
            node_array = adj_map.get(i);
            if (i >= M) {
                System.out.printf("Card %s : ",i - (M-1));
                for (Node j : node_array) {
                    System.out.printf("%s ",j.node_id + 1);
                }
            } else {
                System.out.printf("Machine %s : ", i + 1 );
                for (Node j : node_array) {
                    System.out.printf("%s ",j.node_id + 1 - M);
                }
            }

            System.out.printf("\n");
        }
    }
    /** HELPER FUNCTIONS END **/


    public static void main(String []args)
    {
        try {
            BipartiteMatching model = new BipartiteMatching(0, 0);
            File myObj = new File("/Users/dehaay/Desktop/JavaCS251/Project 4/testFiles/bipartite_matching_testcases/5.txt");
            Scanner myReader = new Scanner(myObj);
            int line = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
//                System.out.println(data);
                if(line == 0)
                {
                    String []str = data.split(" ");
                    int M = Integer.parseInt(str[0]);
                    int N = Integer.parseInt(str[1]);
                    System.out.println(M + "  " + N);
                    model = new BipartiteMatching(M, N);
                }
                else
                {
                    //converts all string line values into integer
                    String []str = data.split(" "); //reads line stores it in a string array
                    int [] input = new int[str.length]; //sets up an INTEGER array the size of the new input
                    for (int i=0; i<str.length; i++)
                        input[i] = Integer.parseInt(str[i]); //converts string numbers into integer numbers for each line


                    //"input" here is all lines other than the first line in array from
                    // line is the i^th credit card, represents the current line we are - first line.
                    model.insList(line-1, input);
                }
                line += 1;
            }
            myReader.close();
            int res = model.bipartiteMatching();
            System.out.println("BipartiteMatching is: "+res);
            

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }



    }
}